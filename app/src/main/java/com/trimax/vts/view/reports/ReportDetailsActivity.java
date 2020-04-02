package com.trimax.vts.view.reports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ReportDetailsActivity";
    private Chip cp_all,cp_moving,cp_idle,cp_stop;
    private ProgressBar pb_progress;
    private ReportsDetailAdapter adapter;
    private List<ReportDetailModel> reportDetailModels,filteredReport;
    private String selectedDate="",status="",userId="",vehicleId="",vehicleNo="";
    private TravelpulseInfoPref infoPref;
    private Gson gson;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Vehicle Timeline");
        gson = new Gson();
        cal = Calendar.getInstance();
        infoPref = new TravelpulseInfoPref(this);

        selectedDate = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");
        userId = getIntent().getStringExtra("userId");
        vehicleId = getIntent().getStringExtra("vehicleId");
        vehicleNo = getIntent().getStringExtra("vehicleNo");

        reportDetailModels = new ArrayList<>();
        filteredReport = new ArrayList<>();

        pb_progress = findViewById(R.id.pb_progress);
        cp_all = findViewById(R.id.cp_all);
        cp_moving = findViewById(R.id.cp_moving);
        cp_idle = findViewById(R.id.cp_idle);
        cp_stop = findViewById(R.id.cp_stop);
        RecyclerView rv_reports = findViewById(R.id.rv_reports);
        rv_reports.setLayoutManager(new LinearLayoutManager(this));
        rv_reports.setItemAnimator(new DefaultItemAnimator());
        rv_reports.setHasFixedSize(true);
        adapter = new ReportsDetailAdapter(reportDetailModels);
        rv_reports.setAdapter(adapter);

        cp_all.setOnClickListener(this);
        cp_moving.setOnClickListener(this);
        cp_idle.setOnClickListener(this);
        cp_stop.setOnClickListener(this);
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
        String key = vehicleNo+"_time_"+selectedDate;
        long time = infoPref.getLong(key,PrefEnum.Login);
        long currentTime = cal.getTimeInMillis();
        if (time==0) {
            getDetailedReport();
        }
        else {

            long time1=20*60000 +time;
            if (currentTime<time1){
                String vehicleData = infoPref.getString(vehicleNo+"_"+selectedDate,PrefEnum.Login);
                reportDetailModels = gson.fromJson(vehicleData,new TypeToken<List<ReportDetailModel>>(){}.getType());
                if (reportDetailModels!=null)
                    filterReport(status);
                else
                    getDetailedReport();
            }
            else{
                getDetailedReport();
            }
        }

        if (status.equalsIgnoreCase("run"))
            changeCheckedChip(R.id.cp_moving);
        else if (status.equalsIgnoreCase("idle"))
            changeCheckedChip(R.id.cp_idle);
        else
            changeCheckedChip(R.id.cp_stop);
    }

    private void getDetailedReport(){
        pb_progress.setVisibility(View.VISIBLE);
        Call<ReportDetailResponse> call = RepositryInstance.getReportRepository().getDeatilStatuReport(userId,vehicleId,selectedDate,status);
        call.enqueue(new Callback<ReportDetailResponse>() {
            @Override
            public void onResponse(Call<ReportDetailResponse> call, Response<ReportDetailResponse> response) {
                pb_progress.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    ReportDetailResponse data = response.body();
                    if (data.getStatus().equalsIgnoreCase("success")){
                        reportDetailModels = data.getData();
                        infoPref.putString(vehicleNo+"_"+selectedDate,gson.toJson(reportDetailModels), PrefEnum.Login);
                        infoPref.putLong(vehicleNo+"_time_"+selectedDate,cal.getTimeInMillis(),PrefEnum.Login);
                        filterReport(status);
                        //adapter.add(reportDetailModels);
                    }else {
                        Toast.makeText(ReportDetailsActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportDetailResponse> call, Throwable t) {
                pb_progress.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(ReportDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        changeCheckedChip(view.getId());
    }

    private void changeCheckedChip(int id){
        if (id == R.id.cp_all){
            cp_all.setChecked(true);
            /*cp_all.setChipStrokeColorResource(R.color.red_200);
            cp_all.setChipStrokeWidth(3);*/
            cp_all.setTextColor(Color.WHITE);
            filterReport("all");

            cp_moving.setChecked(false);
            cp_moving.setTextColor(Color.BLACK);
            cp_idle.setChecked(false);
            cp_idle.setTextColor(Color.BLACK);
            cp_stop.setChecked(false);
            cp_stop.setTextColor(Color.BLACK);
        }else if (id == R.id.cp_moving){
            cp_moving.setChecked(true);
            cp_moving.setTextColor(Color.WHITE);
            filterReport("run");

            cp_all.setChecked(false);
            cp_all.setTextColor(Color.BLACK);
            cp_idle.setChecked(false);
            cp_idle.setTextColor(Color.BLACK);
            cp_stop.setChecked(false);
            cp_stop.setTextColor(Color.BLACK);
        }else if (id==R.id.cp_idle){
            cp_idle.setChecked(true);
            cp_idle.setTextColor(Color.WHITE);
            filterReport("idle");

            cp_moving.setChecked(false);
            cp_moving.setTextColor(Color.BLACK);
            cp_all.setChecked(false);
            cp_all.setTextColor(Color.BLACK);
            cp_stop.setChecked(false);
            cp_stop.setTextColor(Color.BLACK);
        }else if (id==R.id.cp_stop){
            cp_stop.setChecked(true);
            cp_stop.setTextColor(Color.WHITE);
            filterReport("stop");

            cp_moving.setChecked(false);
            cp_moving.setTextColor(Color.BLACK);
            cp_idle.setChecked(false);
            cp_idle.setTextColor(Color.BLACK);
            cp_all.setChecked(false);
            cp_all.setTextColor(Color.BLACK);
        }
    }

    private void filterReport(String status){
        switch (status){
            case "all":
                adapter.add(reportDetailModels);
                break;
            case "run":
                filteredReport.clear();
                for (int i = 0; i <reportDetailModels.size() ; i++) {
                    if(reportDetailModels.get(i).getVehicleStatus().equalsIgnoreCase("run")){
                        filteredReport.add(reportDetailModels.get(i));
                    }
                }
                adapter.add(filteredReport);
                break;
            case "idle":
                filteredReport.clear();
                for (int i = 0; i <reportDetailModels.size() ; i++) {
                    if(reportDetailModels.get(i).getVehicleStatus().equalsIgnoreCase("idle")){
                        filteredReport.add(reportDetailModels.get(i));
                    }
                }
                adapter.add(filteredReport);
                break;
            case "stop":
                filteredReport.clear();
                for (int i = 0; i <reportDetailModels.size() ; i++) {
                    if(reportDetailModels.get(i).getVehicleStatus().equalsIgnoreCase("stop")){
                        filteredReport.add(reportDetailModels.get(i));
                    }
                }
                adapter.add(filteredReport);
                break;
        }
    }
}
