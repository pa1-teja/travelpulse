package com.trimax.vts.view.reports;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.model.ModelVehiclePerformance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.R;
import com.trimax.vts.view.vehicle.VTSSetVehicalActivity;
import com.trimax.vts.view.vehicle.VehiclePerformanceAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ReportsActivity";
    private RecyclerView rv_performance;
    private RelativeLayout rl_progress;
    private ImageView iv_back;
    private LinearLayout ll_moving,ll_idle,ll_stop,ll_max_speed,ll_distance;
    private MaterialButton btn_all_alerts;
    private TextView tv_date,tv_vehicle,tv_speed,tv_distance,tv_moving_duration,tv_idle_duration,tv_park_duration;
    private List<ModelVehiclePerformance> performanceModels = new ArrayList<>();
    private VehiclePerformanceAdapter adapter;
    private  Calendar cal;
    private boolean isDataAvailable=false;
    private String vehicleNo="",vehicleId="",selectedDate="",userId="",userTypeId;
    private String months[]={"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    private TravelpulseInfoPref infoPref;
    private final NumberFormat format = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        infoPref = new TravelpulseInfoPref(this);
        userTypeId = infoPref.getString("user_type_id", PrefEnum.Login);
        userId = infoPref.getString("id", PrefEnum.Login);
        vehicleId = infoPref.getString("vid", PrefEnum.Login);
        vehicleNo = infoPref.getString("vno", PrefEnum.Login);
        cal = Calendar.getInstance();
        initView();
        rv_performance.setLayoutManager(new LinearLayoutManager(this));
        rv_performance.setHasFixedSize(true);
        rv_performance.setItemAnimator(new DefaultItemAnimator());
        adapter = new VehiclePerformanceAdapter(performanceModels);
        rv_performance.setAdapter(adapter);

        setupAdapter();
    }

    private void setupAdapter() {
        performanceModels.clear();
        performanceModels.add(new ModelVehiclePerformance("Dangerous Speed(>90 km/h)","0", ContextCompat.getDrawable(ReportsActivity.this, R.drawable.bg_progress_neon_green)));
        performanceModels.add(new ModelVehiclePerformance("Harsh Acceleration","0", ContextCompat.getDrawable(ReportsActivity.this, R.drawable.bg_progress_neon_green)));
        performanceModels.add(new ModelVehiclePerformance("Harsh Braking","0", ContextCompat.getDrawable(ReportsActivity.this, R.drawable.bg_progress_neon_green)));
        performanceModels.add(new ModelVehiclePerformance("Ac Idling","0", ContextCompat.getDrawable(ReportsActivity.this, R.drawable.bg_progress_neon_green)));
        adapter.addPerformance(performanceModels,this);
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_date = findViewById(R.id.tv_date);
        tv_vehicle = findViewById(R.id.tv_vehicle);
        tv_speed = findViewById(R.id.tv_speed);
        tv_distance = findViewById(R.id.tv_distance);
        tv_moving_duration = findViewById(R.id.tv_moving_duration);
        tv_idle_duration = findViewById(R.id.tv_idle_duration);
        tv_park_duration = findViewById(R.id.tv_park_duration);
        rv_performance = findViewById(R.id.rv_performance);
        ll_distance = findViewById(R.id.ll_distance);
        ll_max_speed = findViewById(R.id.ll_max_speed);
        ll_stop = findViewById(R.id.ll_stop);
        ll_idle = findViewById(R.id.ll_idle);
        ll_moving = findViewById(R.id.ll_moving);
        rl_progress = findViewById(R.id.rl_progress);
        btn_all_alerts = findViewById(R.id.btn_all_alerts);

        String strDate=format.format(cal.get(Calendar.DAY_OF_MONTH))+"-"+format.format((cal.get(Calendar.MONTH)+1))+"-"+cal.get(Calendar.YEAR);
        selectedDate=format.format(cal.get(Calendar.YEAR))+"-"+format.format((cal.get(Calendar.MONTH)+1))+"-"+format.format(cal.get(Calendar.DAY_OF_MONTH));
        tv_date.setText(strDate);
        if (vehicleNo.isEmpty())
            tv_vehicle.setText("Select Vehicle");
        else
            tv_vehicle.setText(vehicleNo);

        iv_back.setOnClickListener(this);
        btn_all_alerts.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_vehicle.setOnClickListener(this);
        ll_moving.setOnClickListener(this);
        ll_idle.setOnClickListener(this);
        ll_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.progressBar:
                ModelVehiclePerformance model = (ModelVehiclePerformance) view.getTag();
                //Toast.makeText(this, model.getNotification_name(), Toast.LENGTH_SHORT).show();
                /*switch (model.getNotification_name()){
                    case "Dangerous Speed(>90 km/h)":
                        //if (Integer.parseInt(model.getNotification_count())>0)
                        startActivity(new Intent(this,ReportAlertActivity.class).putExtra("date",selectedDate).putExtra("status","idle").putExtra("userId",userId).putExtra("vehicleId",vehicleId).putExtra("vehicleNo",vehicleNo));
                        break;
                    case "Harsh Acceleration":
                        //if (Integer.parseInt(model.getNotification_count())>0)
                        startActivity(new Intent(this,ReportAlertActivity.class).putExtra("date",selectedDate).putExtra("status","idle").putExtra("userId",userId).putExtra("vehicleId",vehicleId).putExtra("vehicleNo",vehicleNo));
                        break;
                    case "Harsh Braking":
                        //if (Integer.parseInt(model.getNotification_count())>0)
                        startActivity(new Intent(this,ReportAlertActivity.class).putExtra("date",selectedDate).putExtra("status","idle").putExtra("userId",userId).putExtra("vehicleId",vehicleId).putExtra("vehicleNo",vehicleNo));
                        break;
                    case "Ac Idling":
                        //if (Integer.parseInt(model.getNotification_count())>0)
                        startActivity(new Intent(this,ReportAlertActivity.class).putExtra("date",selectedDate).putExtra("status","idle").putExtra("userId",userId).putExtra("vehicleId",vehicleId).putExtra("vehicleNo",vehicleNo));
                        break;
                }*/
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_all_alerts:
                if (!vehicleNo.isEmpty())
                    startActivity(new Intent(this,ReportAlertActivity.class).putExtra("date",selectedDate).putExtra("status","idle").putExtra("userId",userId).putExtra("vehicleId",vehicleId).putExtra("vehicleNo",vehicleNo));
                else
                    Toast.makeText(this, "Select vehicle", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_date:
                showDatePickerDialog();
                break;
            case R.id.tv_vehicle:
                startActivityForResult(new Intent(this, VTSSetVehicalActivity.class).putExtra("Call","reports"),2000);
                break;
            case R.id.ll_moving:
                if (isDataAvailable)
                    startActivity(new Intent(this,ReportDetailsActivity.class).putExtra("date",selectedDate).putExtra("status","run").putExtra("userId",userId).putExtra("vehicleId",vehicleId).putExtra("vehicleNo",vehicleNo));
                break;
            case R.id.ll_idle:
                if (isDataAvailable)
                    startActivity(new Intent(this,ReportDetailsActivity.class).putExtra("date",selectedDate).putExtra("status","idle").putExtra("userId",userId).putExtra("vehicleId",vehicleId).putExtra("vehicleNo",vehicleNo));
                break;
            case R.id.ll_stop:
                if (isDataAvailable)
                    startActivity(new Intent(this,ReportDetailsActivity.class).putExtra("date",selectedDate).putExtra("status","stop").putExtra("userId",userId).putExtra("vehicleId",vehicleId).putExtra("vehicleNo",vehicleNo));
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //String strDate=day+"th "+months[month]+" "+year;
                String strDate=format.format(day)+"-"+format.format(month+1)+"-"+year;
                selectedDate=format.format(year)+"-"+format.format(month+1)+"-"+format.format(day);
                tv_date.setText(strDate);
                getStatusReport();
            }
        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2000 && resultCode==RESULT_OK){
            if (data!=null) {
                vehicleNo = data.getStringExtra("vehicleNo");
                vehicleId = data.getStringExtra("vehicleId");
                assert vehicleId != null;
                vehicleId = vehicleId.replace(",","");
                tv_vehicle.setText(vehicleNo);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStatusReport();
    }

    private void getStatusReport(){
        rl_progress.setVisibility(View.VISIBLE);
        if (vehicleId.isEmpty() || userId.isEmpty() || selectedDate.isEmpty() || userTypeId.isEmpty()){
            Toast.makeText(this, "Select date and vehicle.", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<ReportResponse> call = RepositryInstance.getReportRepository().getStatuReport(userId,vehicleId,selectedDate,userTypeId);
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                rl_progress.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    ReportResponse reportResponse = response.body();
                    if (reportResponse.getStatus().equalsIgnoreCase("success")){
                        if (reportResponse.getData()!=null && reportResponse.getData().size()>0) {
                            isDataAvailable=true;
                            ReportData data = reportResponse.getData().get(0);
                            tv_speed.setText(data.getMaxSpeed());
                            tv_distance.setText(data.getDistance());
                            tv_moving_duration.setText(data.getMoving());
                            tv_idle_duration.setText(data.getIdle());
                            tv_park_duration.setText(data.getStop());

                            performanceModels.clear();
                            performanceModels.add(new ModelVehiclePerformance("Dangerous Speed(>90 km/h)", data.getDangerousSpeed() + "", ContextCompat.getDrawable(ReportsActivity.this, R.drawable.bg_progress_neon_green)));
                            performanceModels.add(new ModelVehiclePerformance("Harsh Acceleration", data.getHarshAcceleration() + "", ContextCompat.getDrawable(ReportsActivity.this, R.drawable.bg_progress_neon_green)));
                            performanceModels.add(new ModelVehiclePerformance("Harsh Braking", data.getHarshBraking() + "", ContextCompat.getDrawable(ReportsActivity.this, R.drawable.bg_progress_neon_green)));
                            if (data.getAcIdleRepeat() != null)
                                performanceModels.add(new ModelVehiclePerformance("Ac Idling(>20 Minutes)", data.getAcIdleRepeat() + "", ContextCompat.getDrawable(ReportsActivity.this, R.drawable.bg_progress_neon_green)));
                        }
                        adapter.addPerformance(performanceModels,ReportsActivity.this);
                    }else {
                        Toast.makeText(ReportsActivity.this, reportResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                rl_progress.setVisibility(View.GONE);
                Toast.makeText(ReportsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
