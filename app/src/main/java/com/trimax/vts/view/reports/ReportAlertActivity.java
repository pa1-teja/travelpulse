package com.trimax.vts.view.reports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.view.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAlertActivity extends AppCompatActivity {
    private static final String TAG = "ReportAlertActivity";
    private AlertReportAdapter adapter;
    private ProgressBar pb_progress;
    private List<AlertReportModel> alertReports;
    private String selectedDate="",userId="",vehicleId="",vehicleNo="";
    private TravelpulseInfoPref infoPref;
    private Gson gson;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_alert);
        setTitle("Vehicle Alerts");
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        gson = new Gson();
        cal = Calendar.getInstance();
        infoPref = new TravelpulseInfoPref(this);

        selectedDate = getIntent().getStringExtra("date");
        userId = getIntent().getStringExtra("userId");
        vehicleId = getIntent().getStringExtra("vehicleId");
        vehicleNo = getIntent().getStringExtra("vehicleNo");

        alertReports = new ArrayList<>();

        RecyclerView rv_alerts = findViewById(R.id.rv_alerts);
        pb_progress = findViewById(R.id.pb_progress);
        rv_alerts.setLayoutManager(new LinearLayoutManager(this));
        rv_alerts.setItemAnimator(new DefaultItemAnimator());
        adapter = new AlertReportAdapter(alertReports);
        rv_alerts.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotificationReport();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void getNotificationReport(){
        pb_progress.setVisibility(View.VISIBLE);
        Call<AlertReportResponse> call = RepositryInstance.getReportRepository().getDetailNotificationReports(userId,vehicleId,selectedDate);
        call.enqueue(new Callback<AlertReportResponse>() {
            @Override
            public void onResponse(Call<AlertReportResponse> call, Response<AlertReportResponse> response) {
                pb_progress.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    AlertReportResponse data = response.body();
                    if (data.getStatus().equalsIgnoreCase("success")){
                        alertReports = data.getData();
                        adapter.addReports(alertReports);
                    }else {
                        Toast.makeText(ReportAlertActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AlertReportResponse> call, Throwable t) {
                pb_progress.setVisibility(View.GONE);
                Toast.makeText(ReportAlertActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
