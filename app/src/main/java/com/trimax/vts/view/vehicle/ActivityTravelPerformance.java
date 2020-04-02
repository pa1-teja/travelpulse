package com.trimax.vts.view.vehicle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.model.ModelTravelPerformance;
import com.trimax.vts.model.ModelVehiclePerformance;
import com.trimax.vts.model.TravelPerformaceResponse;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class ActivityTravelPerformance extends AppCompatActivity {

    private RecyclerView rv_performance;
    private List<ModelVehiclePerformance> performanceModels;
    private VehiclePerformanceAdapter adapter;
    ProgressDialog dialog;
    public String count="0";
    public String Todays_Kms="";
    Context context;
    public TextView vehicle_no_performace,no_of_instance,todays_driving;
    String user_type_id = "", user_id = "", VehicleValue = "", Vehicle_Name = "", vehicle_type_id = "";
    SharedPreferences sharedpreferencenew;
    private RelativeLayout rl_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_performance);
        Intview();
        getTravelPerfornamce(user_type_id, user_id, VehicleValue);
    }

    public void getTravelPerfornamce(String usertype_id, final String user_id, final String vehicle_id) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        RetrofitInterface objRetrofitInterface = ApiClient.getClientForReplay().create(RetrofitInterface.class);
        Call<TravelPerformaceResponse> call = objRetrofitInterface.fngetTravelperformace(auth, apiKey, usertype_id, user_id, vehicle_id);
        call.enqueue(new Callback<TravelPerformaceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<TravelPerformaceResponse> call, Response<TravelPerformaceResponse> response) {
                try {
                    //  Utils.stopProgressDialog();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    int strResponceCode = response.code();
                    Log.e("", "Response code" + strResponceCode);

                    switch (strResponceCode) {
                        case 200:
                            rl_container.setVisibility(View.VISIBLE);
                            ModelTravelPerformance modelTravelPerformance = response.body().getData();

                            if (modelTravelPerformance != null) {

                                Todays_Kms = response.body().getTravelled_kms();

                                no_of_instance.setText("Above are number of instances as on today \n" + getDate());
                                todays_driving.setText("Today's Driving so far : " + Todays_Kms + " km");
                                performanceModels.clear();

                                if (modelTravelPerformance.getDangerous_speed() != null) {
                                    String speedLimit = vehicle_type_id.equalsIgnoreCase("8") ? "Dangerous Speed ( > 70 km/hr )" : "Dangerous Speed ( > 90 km/hr )";
                                    if (Integer.parseInt(modelTravelPerformance.getDangerous_speed()) > 3) {
                                        performanceModels.add(new ModelVehiclePerformance(speedLimit, modelTravelPerformance.getDangerous_speed(), ContextCompat.getDrawable(ActivityTravelPerformance.this, R.drawable.bg_progress_red)));
                                    } else {
                                        performanceModels.add(new ModelVehiclePerformance(speedLimit, modelTravelPerformance.getDangerous_speed(), ContextCompat.getDrawable(ActivityTravelPerformance.this, R.drawable.bg_progress_green)));
                                    }
                                }
                                if (modelTravelPerformance.getHarsh_acceleration() != null) {
                                    if (Integer.parseInt(modelTravelPerformance.getHarsh_acceleration()) > 3) {
                                        performanceModels.add(new ModelVehiclePerformance("Harsh Acceleration", modelTravelPerformance.getHarsh_acceleration(), ContextCompat.getDrawable(ActivityTravelPerformance.this, R.drawable.bg_progress_red)));
                                    } else {
                                        performanceModels.add(new ModelVehiclePerformance("Harsh Acceleration", modelTravelPerformance.getHarsh_acceleration(), ContextCompat.getDrawable(ActivityTravelPerformance.this, R.drawable.bg_progress_green)));
                                    }
                                }
                                if (modelTravelPerformance.getHarsh_braking() != null) {
                                    if (Integer.parseInt(modelTravelPerformance.getHarsh_braking()) > 3) {
                                        performanceModels.add(new ModelVehiclePerformance("Harsh Braking", modelTravelPerformance.getHarsh_braking(), ContextCompat.getDrawable(ActivityTravelPerformance.this, R.drawable.bg_progress_red)));
                                    } else {
                                        performanceModels.add(new ModelVehiclePerformance("Harsh Braking", modelTravelPerformance.getHarsh_braking(), ContextCompat.getDrawable(ActivityTravelPerformance.this, R.drawable.bg_progress_green)));
                                    }
                                }

                                if (modelTravelPerformance.getAc_idle_repeat() != null) {
                                    if (!vehicle_type_id.equalsIgnoreCase("8")) {

                                        if (Integer.parseInt(modelTravelPerformance.getAc_idle_repeat()) > 2) {
                                            performanceModels.add(new ModelVehiclePerformance("AC Idling ( > 20 minutes )", modelTravelPerformance.getAc_idle_repeat(), ContextCompat.getDrawable(ActivityTravelPerformance.this, R.drawable.bg_progress_red)));
                                        } else {
                                            performanceModels.add(new ModelVehiclePerformance("AC Idling ( > 20 minutes )", modelTravelPerformance.getAc_idle_repeat(), ContextCompat.getDrawable(ActivityTravelPerformance.this, R.drawable.bg_progress_green)));
                                        }
                                    }
                                }
                            }
                            adapter = new VehiclePerformanceAdapter(performanceModels);
                            rv_performance.setAdapter(adapter);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(context, "Data Not Found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TravelPerformaceResponse> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(context, "Data Not Found..", Toast.LENGTH_LONG).show();
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


    public void Intview(){
        context = this;
        Intent intent = getIntent();
        VehicleValue = intent.getStringExtra(Constants.IVehicle.VEHICLE_ID);
        Vehicle_Name = intent.getStringExtra(Constants.IVehicle.TTILE);
        vehicle_type_id = intent.getStringExtra(Constants.IVehicle.vehicle_type);

        setTitle("Vehicle Travel Performance(VTP): "+Vehicle_Name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        rl_container = findViewById(R.id.rl_container);
        vehicle_no_performace=findViewById(R.id.vehicle_no_performace);
        no_of_instance=findViewById(R.id.no_of_instance);
        todays_driving=findViewById(R.id.todays_driving);
        vehicle_no_performace.setText("Vehicle No : "+Vehicle_Name);
        performanceModels = new ArrayList<>();
        sharedpreferencenew = getSharedPreferences(Constants.app_preference_login, 0);
        user_type_id=sharedpreferencenew.getString("user_type_id","");
        user_id=sharedpreferencenew.getString("id","");
        rv_performance = findViewById(R.id.rv_performance);
        rv_performance.setLayoutManager(new LinearLayoutManager(this));
        rv_performance.setHasFixedSize(true);
        rv_performance.setItemAnimator(new DefaultItemAnimator());
        performanceModels.clear();
    }


    public String getDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("MMMM  dd, yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
