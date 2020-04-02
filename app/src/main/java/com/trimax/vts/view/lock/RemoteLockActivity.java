package com.trimax.vts.view.lock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.vehicle_lock.ResponseLockUnlock;
import com.trimax.vts.vehicle_lock.VehicleLockStatus;
import com.trimax.vts.vehicle_lock.VehicleLockStatusResponse;
import com.trimax.vts.view.R;
import com.trimax.vts.view.vehicle.VTSSetVehicalActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteLockActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "RemoteLockActivity";
    private final int VEHICLE_REQUEST=2000;
    private Chip cp_status;
    private TextView tv_location,tv_vehicle_no,tv_see_more,tv_vehicle_status,tv_make_model;
    private MaterialButton btn_lock_vehicle;
    private RecyclerView rv_requests;
    private boolean isLocationExpanded=false;
    private LockRequestAdapter adapter;
    private List<RequestModel> requests;
    private EditText ed_one,ed_two,ed_three,ed_four;
    private AlertDialog lockDialog,tpinDialog;
    private String vehicleId="", vehicleNo="",userTypeId="",userId="",mobileDeviceId="";
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_lock);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Remote Lock");

        infoPref = new TravelpulseInfoPref(this);
        userTypeId = infoPref.getString("user_type_id", PrefEnum.Login);
        userId = infoPref.getString("id", PrefEnum.Login);
        vehicleId = infoPref.getString("vid", PrefEnum.Login);
        vehicleNo = infoPref.getString("vno", PrefEnum.Login);
        mobileDeviceId = infoPref.getString("record_id", PrefEnum.OneSignal);

        requests = new ArrayList<>();
        requests.add(new RequestModel("Type","Time","Status",""));
        requests.add(new RequestModel("Lock","12:20:30","Processing",""));

        cp_status = findViewById(R.id.cp_status);
        rv_requests = findViewById(R.id.rv_requests);
        tv_location = findViewById(R.id.tv_location);
        tv_make_model = findViewById(R.id.tv_make_model);
        tv_vehicle_no = findViewById(R.id.tv_vehicle_no);
        tv_vehicle_status = findViewById(R.id.tv_vehicle_status);
        tv_see_more = findViewById(R.id.tv_see_more);
        btn_lock_vehicle = findViewById(R.id.btn_lock_vehicle);

        rv_requests.setHasFixedSize(true);
        rv_requests.setItemAnimator(new DefaultItemAnimator());
        rv_requests.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LockRequestAdapter(requests);
        rv_requests.setAdapter(adapter);

        tv_vehicle_no.setText(vehicleNo);

        cp_status.setOnClickListener(this);
        tv_vehicle_no.setOnClickListener(this);
        tv_see_more.setOnClickListener(this);
        btn_lock_vehicle.setOnClickListener(this);
        cp_status.setOnCloseIconClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVehicleStatus(vehicleId,mobileDeviceId,userId,userTypeId);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cp_status:
                if (!isLocationExpanded) {
                    cp_status.setCloseIconResource(R.drawable.ic_arrow_down);
                    tv_location.setVisibility(View.VISIBLE);
                }
                else {
                    cp_status.setCloseIconResource(R.drawable.ic_arrow_right);
                    tv_location.setVisibility(View.GONE);
                }
                isLocationExpanded=!isLocationExpanded;
                break;
            case R.id.tv_vehicle_no:
                startActivityForResult(new Intent(this, VTSSetVehicalActivity.class).putExtra("Call","remote_lock"),VEHICLE_REQUEST);
                break;
            case R.id.btn_lock_vehicle:
                showLockUnlockDialog("Lock Vehicle");
                break;
            case R.id.tv_see_more:
                startActivity(new Intent(this,RemoteLockHistoryActivity.class));
                break;
            case R.id.tv_generate_tpin:
                showGenerateTpinDialog();
                break;
        }
    }

    private void showLockUnlockDialog(String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.lock_unlock_dialog,null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_generate_tpin = view.findViewById(R.id.tv_generate_tpin);

        tv_title.setText(title);
        lockDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
         lockDialog.show();

        tv_generate_tpin.setOnClickListener(RemoteLockActivity.this);
    }

    private void showGenerateTpinDialog(){
        if (lockDialog!=null)
            lockDialog.dismiss();

        View view = LayoutInflater.from(this).inflate(R.layout.lock_tpin_dialog,null);
        ed_one = view.findViewById(R.id.ed_one);
        ed_two = view.findViewById(R.id.ed_two);
        ed_three = view.findViewById(R.id.ed_three);
        ed_four = view.findViewById(R.id.edt_four);
        ed_one.addTextChangedListener(this);
        ed_two.addTextChangedListener(this);
        ed_three.addTextChangedListener(this);
        ed_four.addTextChangedListener(this);

        tpinDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        tpinDialog.show();

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 1) {
            if (ed_one.length() == 1) {
                ed_two.requestFocus();
            }

            if (ed_two.length() == 1) {
                ed_three.requestFocus();
            }
            if (ed_three.length() == 1) {
                ed_four.requestFocus();
            }
        } else if (editable.length() == 0) {
            if (ed_four.length() == 0) {
                ed_three.requestFocus();
            }
            if (ed_three.length() == 0) {
                ed_two.requestFocus();
            }
            if (ed_two.length() == 0) {
                ed_one.requestFocus();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==VEHICLE_REQUEST && resultCode==RESULT_OK){
            if (data !=null) {
                vehicleNo = data.getStringExtra("vehicleNo");
                vehicleId = data.getStringExtra("vehicleId");
                String lat = data.getStringExtra("lat");
                String lag = data.getStringExtra("lag");
                tv_vehicle_no.setText(vehicleNo);
            }
        }
    }


    private void getVehicleStatus(String vehicleId, String mobileDeviceId, String requestUserId, String requestUserTypeId){
        Call<VehicleLockStatusResponse> call = RepositryInstance.getImmobilizationRepository().getVehicleStatus(vehicleId,mobileDeviceId,requestUserId,requestUserTypeId);
        call.enqueue(new Callback<VehicleLockStatusResponse>() {
            @Override
            public void onResponse(Call<VehicleLockStatusResponse> call, Response<VehicleLockStatusResponse> response) {
                if (response.isSuccessful()){
                    List<VehicleLockStatus> lockStatuses = response.body().getData();
                    if (lockStatuses!=null && lockStatuses.size()>0){
                        for (int i = 0; i <lockStatuses.size() ; i++) {
                            VehicleLockStatus status = lockStatuses.get(i);
                            tv_location.setText("Location : "+status.getLocation()+"\nSpeed : "+status.getSpeed());
                            tv_make_model.setText("Model : "+status.getMake()+", "+status.getModel());
                            tv_vehicle_status.setText("Vehicle Status : "+status.getVehicleStatus());
                            btn_lock_vehicle.setText(status.getButtonType());
                            //cp_status.setText(status.);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleLockStatusResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

//    private void generateTPin(String userId, String mobileDeviceId, String userTypeId){
//        Call<ResponseBody> call = RepositryInstance.getImmobilizationRepository().setTPin(userId,mobileDeviceId,userTypeId);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()){
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "onFailure: "+t.getMessage());
//            }
//        });
//    }
//
//    private void setTPin( String userId, String tpin, String cnfTpin, String otp, String mobileDeviceId, String userTypeId){
//        Call<ResponseBody> call = RepositryInstance.getImmobilizationRepository().setTPin(userId,tpin,cnfTpin,otp,mobileDeviceId,userTypeId);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()){
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "onFailure: ");
//            }
//        });
//    }

    private void lockUnlockVehicle(String vehicleId, String tpin, String requestType, String platform,String commandChannel, String mobileDeviceId,
                                   String requestUserId, String requestUserTypeId){
        Call<ResponseLockUnlock> call = RepositryInstance.getImmobilizationRepository().lockUnlockVehicle(vehicleId,tpin,requestType,platform,commandChannel,mobileDeviceId,requestUserId,requestUserTypeId);
        call.enqueue(new Callback<ResponseLockUnlock>() {
            @Override
            public void onResponse(Call<ResponseLockUnlock> call, Response<ResponseLockUnlock> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<ResponseLockUnlock> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
