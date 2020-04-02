package com.trimax.vts.vehicle_lock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.lock.LockRequestAdapter;
import com.trimax.vts.view.lock.RequestModel;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleMasterModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleLockHomeFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tv_date, tv_location,tv_vehicle_no,tv_see_more,tv_vehicle_status,tv_make_model;

    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private AppCompatButton btn_lock_vehicle;
    private String userTypeId = "1";
    private String userId = "1";
    private String vehicleId = "65";
    private String vehicleNo;
    private String mobileDeviceId = "1";
    private String requestType;

    LockRequestAdapter adapter;
    private String requestTypeLabel;

    ArrayList<RequestModel> requests = new ArrayList<>();
    private OnVehicleLockListener mListener;

    public VehicleLockHomeFragment() {
        // Required empty public constructor
    }

    public static VehicleLockHomeFragment newInstance(String param1, String param2) {
        VehicleLockHomeFragment fragment = new VehicleLockHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnVehicleLockListener) {
            mListener = (OnVehicleLockListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        TravelpulseInfoPref infoPref = new TravelpulseInfoPref(Objects.requireNonNull(getActivity()));
        userTypeId = infoPref.getString("user_type_id", PrefEnum.Login);
        userId = infoPref.getString("id", PrefEnum.Login);

        VehicleMasterModel data = mListener.getVehicleDetails();
        vehicleId = data.getVehicleId();// infoPref.getString("vid", PrefEnum.Login);
        vehicleNo = data.getVehicleNo();// infoPref.getString("vno", PrefEnum.Login);
        /*vehicleId = infoPref.getString("vid", PrefEnum.Login);
        vehicleNo = infoPref.getString("vno", PrefEnum.Login);*/
        mobileDeviceId = infoPref.getString("record_id", PrefEnum.OneSignal);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_lock_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_location = view.findViewById(R.id.location);
        tv_make_model = view.findViewById(R.id.tv_make_model);
        tv_vehicle_status = view.findViewById(R.id.tv_current_status);
        tv_vehicle_no = view.findViewById(R.id.tv_vehicle_no);
        tv_vehicle_no.setText(vehicleNo);
        tv_date = view.findViewById(R.id.date);
        btn_lock_vehicle = view.findViewById(R.id.change_lock_status);
        view.findViewById(R.id.refresh).setOnClickListener(this);
        adapter = new LockRequestAdapter(requests);

        recyclerView = view.findViewById(R.id.rv_requests);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        btn_lock_vehicle.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        Fragment prev = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag("vehicleLockFragment");
        if (prev == null) {
            getVehicleStatus(vehicleId, mobileDeviceId, userId, userTypeId);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void getVehicleStatus(String vehicleId, String mobileDeviceId, String requestUserId, String requestUserTypeId){
        ProgressDialog progressBar = new ProgressDialog(getActivity(), R.style.MySpinnerTheme);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressBar.show();

        Call<VehicleLockStatusResponse> call = RepositryInstance.getImmobilizationRepository().getVehicleStatus(vehicleId,mobileDeviceId,requestUserId,requestUserTypeId);
        call.enqueue(new Callback<VehicleLockStatusResponse>() {
            @Override
            public void onResponse(Call<VehicleLockStatusResponse> call, Response<VehicleLockStatusResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus().equalsIgnoreCase("success")) {
                        List<VehicleLockStatus> lockStatuses = response.body().getData();
                        if (lockStatuses!=null && lockStatuses.size()>0) {
                            for (int i = 0; i <lockStatuses.size() ; i++) {
                                VehicleLockStatus status = lockStatuses.get(i);

                                tv_location.setText(status.getLocation());
                                tv_make_model.setText("Model : "+status.getMake()+", "+status.getModel());
                                tv_vehicle_status.setText("Vehicle Status : "+status.getVehicleStatus());
                                tv_vehicle_no.setText(status.getVehicleNo());
                                tv_date.setText(status.getCtime());
                                requestTypeLabel = status.getButtonType();
                                btn_lock_vehicle.setText(requestTypeLabel);

                                // ArrayList<RequestModel> requests = new ArrayList<>();
                                requests.clear();
                                requests.add(new RequestModel("Type","Time","Status",""));
                                requests.add(new RequestModel(status.getRequestType(),status.getRequestTime(),status.getRequestMsg(),""));
                                recyclerView.setAdapter(adapter);
                                // btn_lock_vehicle.setText(status.getButtonType());
                                // cp_status.setText(status.);
                            }
                        }
                    } else {
                        showAlertDialog(response.body().getStatus()+"!!", response.body().getMsg());
                    }
                    progressBar.dismiss();
                } else {
                    showAlertDialog("Sorry!!", "Failed to validate request.");
                }
            }

            @Override
            public void onFailure(Call<VehicleLockStatusResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                progressBar.dismiss();
            }
        });
    }
    private void showAlertDialog(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(title.toUpperCase())
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(text.contains("not found")) {
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.refresh) {
            getVehicleStatus(vehicleId, mobileDeviceId, userId, userTypeId);
        } else {
            FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            Fragment prev = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag("vehicleLockFragment");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment vehicleLockFragment = VehicleLockFragment.newInstance(requestTypeLabel.toLowerCase(), tv_vehicle_no.getText().toString());
            vehicleLockFragment.setCancelable(false);
            vehicleLockFragment.show(ft, "vehicleLockFragment");
            // NavController navController = Navigation.findNavController(view);
            // navController.navigate(R.id.action_vehicleLockHomeFragment_to_pinValidationFragment);
        }

    }
}
