package com.trimax.vts.view.vehicle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trimax.vts.view.R;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.model.CurrentStarusData;
import com.trimax.vts.model.Current_Status;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class VehicleStatusActivity extends AppCompatActivity {
    private RecyclerView listView;
    TextView nodata;
    private Context mContext;
    public CurrentStarusData currentStarusData;
    ProgressDialog dialog;
    private ArrayList<ArrayList<Current_Status>> cur_stutus_list;
    String user_type_id = "";
    String id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_status);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView = (RecyclerView) findViewById(R.id.ListView);
        nodata = (TextView) findViewById(R.id.nodata);
        cur_stutus_list = new ArrayList<>();
        Intent ii = getIntent();
        String status = ii.getStringExtra("status");
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        user_type_id = preferences.getString("user_type_id", "");
        id = preferences.getString("id", "");
        getSupportActionBar().setTitle("Current Vehicles Status");
        cur_stutus_list.clear();
        current_vehicle_status_indivichul(user_type_id, id, status);
    }


    public void current_vehicle_status_indivichul(String usertype_id, final String user_id, final String type) {
        dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.current_vehicle_status_indivichul(auth, apiKey, usertype_id, user_id, type);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String strStatus = myObject.getString("status");
                                if (strStatus.trim().equals("success")) {
                                    try {
                                        if (strStatus != null) {
                                            GsonBuilder builder = new GsonBuilder();
                                            Gson gson = builder.create();
                                            currentStarusData = gson.fromJson(strResponse, CurrentStarusData.class);
                                            if (cur_stutus_list.size() > 0) {
                                                cur_stutus_list.clear();
                                            }
                                            for (int i = 0; i < currentStarusData.getData().size(); i++) {
                                                cur_stutus_list.add(currentStarusData.getData());
                                            }
                                            listView.setVisibility(View.VISIBLE);
                                            nodata.setVisibility(View.GONE);
                                            VehicleStatusAdapter adapter_vehicle_status = new VehicleStatusAdapter(cur_stutus_list, mContext, 0);
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                            listView.setLayoutManager(mLayoutManager);
                                            listView.setHasFixedSize(true);
                                            listView.setItemAnimator(new DefaultItemAnimator());
                                            listView.setAdapter(adapter_vehicle_status);
                                            listView.setNestedScrollingEnabled(false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    listView.setVisibility(View.INVISIBLE);
                                    nodata.setVisibility(View.VISIBLE);
                                }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vehicle_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Moving:
                current_vehicle_status_indivichul(user_type_id, String.valueOf(id), "moving");
                return true;

            case R.id.stop:
                current_vehicle_status_indivichul(user_type_id, String.valueOf(id), "stop");
                return true;

            case R.id.Idle:
                current_vehicle_status_indivichul(user_type_id, String.valueOf(id), "idle");
                return true;

            case R.id.Network:
                current_vehicle_status_indivichul(user_type_id, String.valueOf(id), "nonetwork");
                return true;

            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
