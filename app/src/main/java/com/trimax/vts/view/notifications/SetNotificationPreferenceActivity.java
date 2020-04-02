package com.trimax.vts.view.notifications;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.view.master.activity.BaseActivity;
import com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference.NotificationPrefModel;
import com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference.NotificationPrefModelResponse;
import com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference.NotificationPrefModelSetPrefResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.R;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.notifications.adapter.NotificationPreffAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class SetNotificationPreferenceActivity extends BaseActivity implements View.OnClickListener {

    private Button btnSetNotificationPref;
    private String TAG = SetNotificationPreferenceActivity.class.getSimpleName();
    String[] ac_notification = {"Ac Idle", "Ac Idle 10+10", "Ac", "Emergency ON"};
    private boolean p_mode;
    private List<NotificationPrefModel> notificationPrefModels;
    private NotificationPreffAdapter adapter;
    ProgressDialog progressDialog ;
    SharedPreferences sharedPreferences,sharedPreferencesnew;
    private String cust_id, usertype_id, vehicleId, uid, uid_type, token, player_id, device_id, vehicle_type_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification_preference);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        closeKeyBoard(this);
        initView();
        loadDefaultData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        notificationPrefModels = new ArrayList<>();

        btnSetNotificationPref = findViewById(R.id.btn_set_pref);
        btnSetNotificationPref.setOnClickListener(this);
        RecyclerView rv_notifications = findViewById(R.id.rv_notifications);

        adapter = new NotificationPreffAdapter(notificationPrefModels,this);
        rv_notifications.setLayoutManager(new LinearLayoutManager(this));
        rv_notifications.setHasFixedSize(true);
        rv_notifications.setItemAnimator(new DefaultItemAnimator());
        rv_notifications.setAdapter(adapter);
    }

    private void loadDefaultData() {
        if (notificationPrefModels == null)
            notificationPrefModels = new ArrayList<>();

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        sharedPreferencesnew = getSharedPreferences("OneSignal", MODE_PRIVATE);

        cust_id = sharedPreferences.getString("id", "");
        usertype_id = sharedPreferences.getString(getString(R.string.user_type_id), "");
        uid=sharedPreferences.getString("imei", "");
        uid_type=sharedPreferences.getString(getString(R.string.user_type_id), "");
        player_id=sharedPreferences.getString("GT_PLAYER_ID", "");
        token=sharedPreferences.getString("token", "");
        device_id=sharedPreferencesnew.getString("record_id", "");

        Intent intent = getIntent();
        vehicleId = intent.getStringExtra(Constants.IVehicle.VEHICLE_ID);
        String vehicleNo = intent.getStringExtra(Constants.IVehicle.TTILE);
        vehicle_type_id = intent.getStringExtra(Constants.IVehicle.vehicle_type);
        if (vehicleNo!=null && !vehicleNo.isEmpty())
            setTitle(vehicleNo);
        getNotificationPref();
    }

    @Override
    public void onClick(View view) {
        if (view == btnSetNotificationPref) {
            String data = adapter.getNotificationData();
            setNotificationPref(data.substring(0,data.lastIndexOf("|")));
        }
    }


    private void getNotificationPref() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Please wait..");
        Call<NotificationPrefModelResponse.NotificationData> call = RepositryInstance.getNotificationRepository().getNotificationPreference(usertype_id, cust_id, vehicleId, player_id, device_id);
        call.enqueue(new Callback<NotificationPrefModelResponse.NotificationData>() {
            @Override
            public void onResponse(Call<NotificationPrefModelResponse.NotificationData> call, Response<NotificationPrefModelResponse.NotificationData> response) {
                hideProgressDialog(progressDialog);
                try {
                    notificationPrefModels = response.body().getNData().getData();
                    List<NotificationPrefModel> prefModels = new ArrayList<>();
                    prefModels.addAll(notificationPrefModels);
                    p_mode = response.body().getNData().getParking_mode();

                    if (notificationPrefModels != null) {
                        int count = 0;
                        if (vehicle_type_id.equalsIgnoreCase("8")) {
                            List<String> ac_noti = Arrays.asList(ac_notification);
                            for (int i = 0; i < prefModels.size(); i++) {
                                NotificationPrefModel model = prefModels.get(i);

                                if (ac_noti.contains(model.getTitle())) {
                                    notificationPrefModels.remove(i - count);
                                    ++count;
                                }
                            }
                        }
                        adapter.addNotifiation(notificationPrefModels);
                    }


                } catch (Exception ex) {
                    Log.e(TAG, "This is an error GetNotificationPref: " + ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NotificationPrefModelResponse.NotificationData> call, Throwable t) {
                hideProgressDialog(progressDialog);
                Toast.makeText(SetNotificationPreferenceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hideProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    private void setNotificationPref(String data) {
        progressDialog = ProgressDialog.show(this, "", "Please wait..");
        auth = auth.replace("\n", "");
        Call<NotificationPrefModelSetPrefResponse> call = RepositryInstance.getNotificationRepository().setNotificationPreference(usertype_id, cust_id, vehicleId, data,uid,uid_type,token,player_id ,p_mode?"1":"0",device_id);
        call.enqueue(new Callback<NotificationPrefModelSetPrefResponse>() {
            @Override
            public void onResponse(Call<NotificationPrefModelSetPrefResponse> call, Response<NotificationPrefModelSetPrefResponse> response) {
                hideProgressDialog(progressDialog);
                try {
                    Log.e(TAG, "Response SetNotificationPref" + response);
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (response.body().getData() != null) {
                            notificationPrefModels = response.body().getData();
                            Toast.makeText(SetNotificationPreferenceActivity.this, "Notification preferences set successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(SetNotificationPreferenceActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "This is an error SetNotificationPref: " + ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NotificationPrefModelSetPrefResponse> call, Throwable t) {
                Log.e(TAG, "Responce SetNotificationPref " + call + t);
                hideProgressDialog(progressDialog);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
