package com.trimax.vts.view.master.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.vehicle_lock.VehicleLockActivity;
import com.trimax.vts.view.document.Activity_Document;
import com.trimax.vts.view.lock.RemoteLockActivity;
import com.trimax.vts.view.master.adapter.VehicleMaster.VehicleMasterAdapter;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleMasterModel;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleMasterModelResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.vehicle.ActivityTravelPerformance;
import com.trimax.vts.view.R;
import com.trimax.vts.view.nearby.NearByPlaceServicesAdapter;
import com.trimax.vts.view.nearby.models.PlaceServiceModel;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.notifications.SetNotificationPreferenceActivity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class Activity_Vehicle_Master extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private String TAG = Activity_Vehicle_Master.class.getSimpleName();
    private RecyclerView recyclerView;
    private VehicleMasterAdapter adapter;
    ProgressDialog dialog;
    private SearchView searchView;
    private String strId, strUserId, mobile_device_id, player_id;
    private BottomSheetBehavior bottomSheetBehavior;
    private VehicleMasterModel selectedVehicle;
    private ArrayList<VehicleMasterModel> vehicleMasterModels;
    private int selectedIndex=-1;
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_master);
        infoPref = new TravelpulseInfoPref(this);
        initView();
        setListener();
        loadDefaultData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.vehicle);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_action_bar_color)));
        recyclerView = findViewById(R.id.recycler_view_vehicle_master);
        searchView = findViewById(R.id.search_view_vehicles);

        LinearLayout ll_bottom_sheet = findViewById(R.id.ll_bottom_sheet);
        RecyclerView rv_options = findViewById(R.id.rv_options);

        rv_options.setHasFixedSize(true);
        rv_options.setItemAnimator(new DefaultItemAnimator());
        rv_options.setLayoutManager(new GridLayoutManager(this, 2));
        mobile_device_id = infoPref.getString("record_id", PrefEnum.OneSignal);
        player_id = infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
        List<PlaceServiceModel> serviceModels = new ArrayList<>();

        if (!infoPref.getString("user_type_id", PrefEnum.Login).equalsIgnoreCase("6"))
            serviceModels.add(new PlaceServiceModel("Edit Vehicle", R.drawable.places_background_circle, R.drawable.ic_edit, "edit", R.drawable.ic_local_hospital_red));
        serviceModels.add(new PlaceServiceModel("Vehicle Lock", R.drawable.places_background_circle, R.drawable.ic_lock, "navigation", R.drawable.police_station_red));
        serviceModels.add(new PlaceServiceModel("Notification Preference", R.drawable.places_background_circle, R.drawable.ic_notifications, "notification", R.drawable.ic_local_gas_station_red));
        serviceModels.add(new PlaceServiceModel("Upload Document", R.drawable.places_background_circle, R.drawable.ic_insert_drive_file_black_24dp, "document", R.drawable.fire_station_red));
        serviceModels.add(new PlaceServiceModel("Travel Performance", R.drawable.places_background_circle, R.drawable.vehiclewhite, "travel", R.drawable.police_station_red));
        NearByPlaceServicesAdapter optionsAdapter = new NearByPlaceServicesAdapter(serviceModels, this);

        rv_options.setAdapter(optionsAdapter);

        bottomSheetBehavior = BottomSheetBehavior.from(ll_bottom_sheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void setListener() {
        search(searchView);
    }

    private void loadDefaultData() {
        strId = infoPref.getString("id", PrefEnum.Login);
        strUserId = infoPref.getString(getString(R.string.user_type_id), PrefEnum.Login);
        loadAllVehicleList();
    }

    private void loadAllVehicleList(){
        progressDialogStart(this, "Please Wait...");
        auth = auth.replace("\n", "");
        RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
        Call<VehicleMasterModelResponse> call = objRetrofitInterface.fnGetAllVehicleList(auth, apiKey, strId, strUserId, mobile_device_id);

            call.enqueue(new Callback<VehicleMasterModelResponse>() {
                @Override
                public void onResponse(Call<VehicleMasterModelResponse> call, Response<VehicleMasterModelResponse> response) {
                    try {
                        progressDialogStop();
                        Log.e(TAG, "Response " + response);
                        vehicleMasterModels = (ArrayList<VehicleMasterModel>) response.body().getData();
                        Log.e(TAG, "vehicleMasterModels: " + vehicleMasterModels.size());
                        if (vehicleMasterModels.size() > 0) {
                            adapter = new VehicleMasterAdapter(Activity_Vehicle_Master.this, vehicleMasterModels, Activity_Vehicle_Master.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Activity_Vehicle_Master.this));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setNestedScrollingEnabled(false);
                        }
                        else {
                            Toast.makeText(Activity_Vehicle_Master.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch(Exception ex) {
                        progressDialogStop();
                        Log.e(TAG, "This is an error: "+ex.getMessage());
                    }
                }

                @Override
                public void onFailure (Call <VehicleMasterModelResponse> call, Throwable t){
                    Log.e(TAG, "Responce " + call + t);
                    progressDialogStop();
                }
            });

    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_option) {
            selectedVehicle = (VehicleMasterModel) v.getTag();
            if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_HIDDEN)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            else if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            else
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            final PlaceServiceModel model = (PlaceServiceModel) v.getTag();
            if (model != null && selectedVehicle != null) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                switch (model.getService()) {
                    case "Edit Vehicle":
                        Intent intent = new Intent(this, UpdateVehicleProfileActivity.class);
                        intent.putExtra(Constants.IVehicle.VEHICLE_ID, selectedVehicle.getVehicleId());
                        startActivity(intent);
                        break;
                    case "Notification Preference":
                        intent = new Intent(this, SetNotificationPreferenceActivity.class);
                        intent.putExtra(Constants.IVehicle.VEHICLE_ID, selectedVehicle.getVehicleId());
                        intent.putExtra(Constants.IVehicle.TTILE, selectedVehicle.getVehicleNo());
                        intent.putExtra(Constants.IVehicle.vehicle_type, selectedVehicle.getVehicle_type_id());
                        startActivity(intent);
                        break;
                    case "Upload Document":
                        intent = new Intent(this, Activity_Document.class);
                        intent.putExtra(Constants.IVehicle.VEHICLE_ID, selectedVehicle.getVehicleId());
                        intent.putExtra(Constants.IVehicle.TTILE, selectedVehicle.getVehicleNo());
                        startActivity(intent);
                        break;
                    case "Travel Performance":
                        intent = new Intent(this, ActivityTravelPerformance.class);
                        intent.putExtra(Constants.IVehicle.VEHICLE_ID, selectedVehicle.getVehicleId());
                        intent.putExtra(Constants.IVehicle.TTILE, selectedVehicle.getVehicleNo());
                        intent.putExtra(Constants.IVehicle.vehicle_type, selectedVehicle.getVehicle_type_id());
                        startActivity(intent);
                        break;
                    case "Vehicle Lock":
                        intent = new Intent(this, VehicleLockActivity.class);
                        intent.putExtra("selectedVehicle", selectedVehicle);
                        intent.putExtra(Constants.IVehicle.VEHICLE_ID, selectedVehicle.getVehicleId());
                        intent.putExtra(Constants.IVehicle.TTILE, selectedVehicle.getVehicleNo());
                        intent.putExtra(Constants.IVehicle.vehicle_type, selectedVehicle.getVehicle_type_id());
                        startActivity(intent);
                        break;
                }
            }
        }

        }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        VehicleMasterModel v = (VehicleMasterModel) buttonView.getTag();
        selectedIndex = vehicleMasterModels.indexOf(v);
        if (isChecked) {
            fnParkingmodeOff(strId, strUserId, v.getVehicleId(), "1");
        } else {
            fnParkingmodeOff(strId, strUserId, v.getVehicleId(), "0");
        }
    }


    public void fnParkingmodeOff(String strUserid, final String UserTypeId, final String VehicleId, final String Parkig_mode) {
            dialog = new ProgressDialog(Activity_Vehicle_Master.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait...");
            dialog.show();
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = RepositryInstance.getNotificationRepository().setParkingModeOnOff(strUserid, UserTypeId, VehicleId, Parkig_mode, player_id, mobile_device_id);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    try {
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Responce code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String strStatus = myObject.getString("status");
                                String strMessage = myObject.getString("msg");
                                if (strStatus.equalsIgnoreCase("success")){
                                    if (selectedIndex!=-1)
                                    vehicleMasterModels.get(selectedIndex).setParking_mode(Parkig_mode);
                                }else {
                                    adapter.addVehicles(vehicleMasterModels);
                                }
                                Toast.makeText(Activity_Vehicle_Master.this, strMessage, Toast.LENGTH_LONG).show();
                                break;
                            case 400:
                            case 500:
                                Log.e("response code 500", "Internal server error");
                                break;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();


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
    public void onBackPressed() {
        if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else
            super.onBackPressed();
    }
}
