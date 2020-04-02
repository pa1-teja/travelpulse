package com.trimax.vts.vehicle_lock;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleMasterModel;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleLockFragment extends DialogFragment implements View.OnClickListener  {
    private static final String ARG_REQUEST_TYPE = "param1";
    private static final String ARG_VEHICLE_NO = "param2";
    OnVehicleLockListener mListener;
    // TODO: Rename and change types of parameters


    private String userTypeId = "1";
    private String userId = "1";
    private String vehicleId = "65";
    private String vehicleNo;
    private String mobileDeviceId = "1";

    private String mRequestType;
    private String mParam2;
    private String tPin;
    private TextInputEditText tvPin;
    private String mPlatform = "android";
    private String mCommandChannel = "GPRS";
    // OnDialogBoxDismissListener onDialogBoxDismissListener;
    public VehicleLockFragment() {
        // Required empty public constructor
    }

    public static VehicleLockFragment newInstance(String requestType, String vechileNo) {
        VehicleLockFragment fragment = new VehicleLockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REQUEST_TYPE, requestType);
        args.putString(ARG_VEHICLE_NO, vechileNo);
        fragment.setArguments(args);
        return fragment;
    }
    public void setContext(VehicleLockHomeFragment context) {
        /*if(context instanceof OnDialogBoxDismissListener) {
            onDialogBoxDismissListener = (OnDialogBoxDismissListener) context;
        }*/
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
            mRequestType = getArguments().getString(ARG_REQUEST_TYPE);
            vehicleNo = getArguments().getString(ARG_VEHICLE_NO);
        }
        VehicleMasterModel data = mListener.getVehicleDetails();
        TravelpulseInfoPref infoPref = new TravelpulseInfoPref(Objects.requireNonNull(getActivity()));
        userTypeId = infoPref.getString("user_type_id", PrefEnum.Login);
        userId = infoPref.getString("id", PrefEnum.Login);
        vehicleId = data.getVehicleId();// infoPref.getString("vid", PrefEnum.Login);
        vehicleNo = data.getVehicleNo();// infoPref.getString("vno", PrefEnum.Login);
        mobileDeviceId = infoPref.getString("record_id", PrefEnum.OneSignal);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicle_lock, container, false);
    }
    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tv_generate_tpin).setOnClickListener(this);
        view.findViewById(R.id.btn_save).setOnClickListener(this);
        view.findViewById(R.id.close).setOnClickListener(this);
        tvPin = view.findViewById(R.id.t_pin);
        ((TextInputEditText)view.findViewById(R.id.vehicle_no)).setText(vehicleNo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_generate_tpin:
                showLockView();
                break;
            case R.id.btn_save:
                tPin = Objects.requireNonNull(tvPin.getText()).toString();
                if(tPin.length()>=4) {
                    lockUnlockVehicle(vehicleId, tPin, mRequestType.toLowerCase(), mPlatform, mCommandChannel, mobileDeviceId, userId, userTypeId);
                } else {
                    showAlertDialog("Sorry!!", " Please enter the valid T-PIN.");
                }
                break;
            case R.id.close:
                dismiss();
                // onDialogBoxDismissListener.onOk();
                break;
        }
    }

    private void lockUnlockVehicle(String vehicleId, String tpin, String requestType, String platform,String commandChannel, String mobileDeviceId,
                                   String requestUserId, String requestUserTypeId){
        ProgressDialog progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(false);
        progressBar.setMessage(null);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        Call<ResponseLockUnlock> call = RepositryInstance.getImmobilizationRepository().lockUnlockVehicle(vehicleId,tpin,requestType,platform,commandChannel,mobileDeviceId,requestUserId,requestUserTypeId);
        call.enqueue(new Callback<ResponseLockUnlock>() {
            @Override
            public void onResponse(Call<ResponseLockUnlock> call, Response<ResponseLockUnlock> response) {
                if (response.isSuccessful()) {

                    progressBar.dismiss();
                    String title = Objects.requireNonNull(response.body()).getStatus();
                    String msg = Objects.requireNonNull(response.body()).getMsg();
                    if(title.length()>0 && !title.equalsIgnoreCase("success")) {
                        title = title+"!!";
                    } else {
                        title = title+"..";
                    }
                    showAlertDialog(title, msg);
                } else {
                    progressBar.dismiss();
                    showAlertDialog("Sorry!!", "Failed to validate request.");
                }
            }

            @Override
            public void onFailure(Call<ResponseLockUnlock> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                progressBar.dismiss();
                showAlertDialog("Sorry!!", t.getMessage());
            }
        });
    }
    private void showLockView() {
        // this.dismiss();
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        Fragment prev = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag("pinValidationFragment");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment pinValidationFragment = PinValidationFragment.newInstance("", "");
        pinValidationFragment.setCancelable(false);
        pinValidationFragment.show(ft, "pinValidationFragment");
    }

    private void showAlertDialog(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(title.toUpperCase())
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (title.equalsIgnoreCase("success")) {
                            VehicleLockFragment.this.dismiss();
                            // onDialogBoxDismissListener.onOk();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
