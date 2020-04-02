package com.trimax.vts.view.master.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.master.adapter.VehicleGroup.Adapter_Vehicle_Group;
import com.trimax.vts.view.master.model.VehicleGroup.VehicleGroupModel;
import com.trimax.vts.view.master.model.VehicleGroup.VehicleGroupModelResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;


/**
 * Created by kiranp on 5/11/2018.
 */

public class Activity_Vehical_Group extends BaseActivity implements View.OnClickListener {
    private String TAG = Activity_Vehical_Group.class.getSimpleName();
    private ListView listview_vehiclegroup;
    private FloatingActionButton floatingActionMenu;
    private  ArrayList<VehicleGroupModel> vehicleGrpModels;
    private String cust_id,usertype_id;
    private Adapter_Vehicle_Group adapter;
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle__group);
        infoPref = new TravelpulseInfoPref(this);
        InitView();
        loadDefaultData();
    }

    private void loadDefaultData() {
        cust_id = infoPref.getString("id", PrefEnum.Login);
        usertype_id = infoPref.getString(getString(R.string.user_type_id), PrefEnum.Login);
        loadAllVehicleGroupList("");
    }

    public void updateList(String grpId){
        for (VehicleGroupModel model : vehicleGrpModels){
            if (model.getId().trim().equalsIgnoreCase(grpId.trim())){
                vehicleGrpModels.remove(model);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }


    private void InitView () {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_action_bar_color)));
        vehicleGrpModels = new ArrayList<>();
        listview_vehiclegroup = findViewById(R.id.listview_vehiclegroup);
        floatingActionMenu = findViewById(R.id.fab);

        adapter = new Adapter_Vehicle_Group(Activity_Vehical_Group.this, vehicleGrpModels);
        listview_vehiclegroup.setAdapter(adapter);
        listview_vehiclegroup.setDividerHeight(0);

        //setFabColor();
        floatingActionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Vehical_Group.this, AddVehicleGroupActivity.class);
                intent.putExtra(Constants.IVehicle.ACTION,"add");
                startActivity(intent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setFabColor(){
        floatingActionMenu.setBackgroundColor(getColor(R.color.red_color));
    }


    private void loadAllVehicleGroupList(String message){
        progressDialogStart(this, "Please Wait..."+ message);
        try {
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<VehicleGroupModelResponse> call = objRetrofitInterface.fnGetVehicleGroupList(auth, apiKey, cust_id, usertype_id);

            Log.d(TAG, "strId: "+cust_id);
            Log.d(TAG, "strUserId: "+usertype_id);

            call.enqueue(new Callback<VehicleGroupModelResponse>() {
                @Override
                public void onResponse(Call<VehicleGroupModelResponse> call, Response<VehicleGroupModelResponse> response) {
                    try {
                        progressDialogStop();
                        Log.e(TAG, "Response " + response);
                        vehicleGrpModels = (ArrayList<VehicleGroupModel>) response.body().getData();
                        Log.e(TAG, "vehicleMasterModels: " + vehicleGrpModels.size());
                        if (vehicleGrpModels.size() > 0) {
                            adapter = new Adapter_Vehicle_Group(Activity_Vehical_Group.this, (ArrayList<VehicleGroupModel>) vehicleGrpModels);
                            listview_vehiclegroup.setAdapter(adapter);
                        }
                    }catch(Exception ex) {
                        progressDialogStop();
                        Log.e(TAG, "This is an error: "+ex.getMessage());
                    }
                }

                @Override
                public void onFailure (Call <VehicleGroupModelResponse> call, Throwable t){
                    Log.e(TAG, "Responce " + call + t);
                    progressDialogStop();
                }
            });
        } catch(Exception ex) {
            progressDialogStop();
            Log.e(TAG, "Api fail: "+ex.getMessage());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drivers, menu);
        MenuItem item = menu.findItem(R.id.search);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adapter.getFilter().filter(text);
                return false;
            }
        });
        return true;
    }


    @Override
    public void onClick(View v) {

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
