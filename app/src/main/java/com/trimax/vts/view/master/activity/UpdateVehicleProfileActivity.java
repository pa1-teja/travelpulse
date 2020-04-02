package com.trimax.vts.view.master.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleProfileModel;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleProfileResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class UpdateVehicleProfileActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = UpdateVehicleProfileActivity.class.getSimpleName();
    private String userId, userTypeId, vehicleId;
    private VehicleProfileModel vehicleProfileModel;
    private Button btnUpdateProfile;
    private TextInputEditText etVehicleNo, etMake, etModel, etColor, etFuelType, etSpeedLimit, etYearOfManufacture;
    String ReportType = "Diesel";
    String YearType = "2019";
    String SpeedType = "10";
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicle_profile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        infoPref = new TravelpulseInfoPref(this);
        setTitle("Edit Vehicle");
        initView();
        setListener();
        loadDefaultData();
    }

    private void loadDefaultData() {
        userId = infoPref.getString("id", PrefEnum.Login);
        userTypeId = infoPref.getString(getString(R.string.user_type_id), PrefEnum.Login);
        Intent intent = getIntent();
        vehicleId = intent.getStringExtra(Constants.IVehicle.VEHICLE_ID);
        retrieveVehicleProfile();
    }

    private void setListener() {
        btnUpdateProfile.setOnClickListener(this);
        etSpeedLimit.setOnClickListener(this);
        etFuelType.setOnClickListener(this);
        etYearOfManufacture.setOnClickListener(this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_action_bar_color)));

        btnUpdateProfile = findViewById(R.id.btn_update_vehicle_profile);
        etVehicleNo = findViewById(R.id.et_vehicle_no);
        etMake = findViewById(R.id.et_make);
        etModel = findViewById(R.id.et_model);
        etColor = findViewById(R.id.et_vehicle_color);
        etFuelType = findViewById(R.id.et_fuel_type);
        etSpeedLimit = findViewById(R.id.et_speed_limit);
        etYearOfManufacture = findViewById(R.id.et_year_of_manufacture);

        etFuelType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });

        etSpeedLimit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });
        etYearOfManufacture.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {

        if(v==etFuelType){

         AlertDialog.Builder builder = new AlertDialog.Builder(UpdateVehicleProfileActivity.this);
                builder.setTitle("Choose Fuel Type");

                String[] animals = {"Diesel", "Petrol", "Gas"};
                int checkedItem = 0; // cow

                builder.setSingleChoiceItems(animals, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // horse
                                ReportType = "Diesel";
                                break;
                            case 1: // horse
                                ReportType = "Petrol";
                                break;
                            case 2: // horse
                                ReportType = "Gas";
                                break;
                        }
                    }
                });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicked OK
                    etFuelType.setText(ReportType);

                }

        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        }
        else if(v==etSpeedLimit){
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateVehicleProfileActivity.this);
            builder.setTitle("Choose Overspeed Limit");

            String[] animals = {"10", "20", "30","40","50","60","70","80","90","100","110","120","130","140","150"};
            int checkedItem = 0; // cow

            builder.setSingleChoiceItems(animals, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                    switch (which) {
                        case 0: // horse
                            SpeedType = "10";
                            break;
                        case 1: // horse
                            SpeedType = "20";
                            break;
                        case 2: // horse
                            SpeedType = "30";
                            break;
                        case 3: // horse
                            SpeedType = "40";
                            break;
                        case 4: // horse
                            SpeedType = "50";
                            break;
                        case 5: // horse
                            SpeedType = "60";
                            break;
                        case 6: // horse
                            SpeedType = "70";
                            break;
                        case 7: // horse
                            SpeedType = "80";
                            break;
                        case 8: // horse
                            SpeedType = "90";
                            break;
                        case 9: // horse
                            SpeedType = "100";
                            break;
                            case 10: // horse
                                SpeedType = "110";
                            break;
                        case 11: // horse
                            SpeedType = "120";
                            break;
                        case 12: // horse
                            SpeedType = "130";
                            break;
                        case 13: // horse
                            SpeedType = "140";
                            break;
                        case 14: // horse
                            SpeedType = "150";
                            break;



                    }
                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicked OK
                    etSpeedLimit.setText(SpeedType);

                }

            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(v==etYearOfManufacture){
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateVehicleProfileActivity.this);
            builder.setTitle("Choose Year Of Manufacture");

            String[] animals = {"2019", "2018", "2017","2016","2015","2014","2013","2012","2011","2010","2009"};
            int checkedItem = 0; // cow,

            builder.setSingleChoiceItems(animals, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                    switch (which) {
                        case 0: // horse
                            YearType = "2019";
                            break;
                        case 1: // horse
                            YearType = "2018";
                            break;
                        case 2: // horse
                            YearType = "2017";
                            break;
                        case 3: // horse
                            YearType = "2016";
                            break;
                        case 4: // horse
                            YearType = "2015";
                            break;
                        case 5: // horse
                            YearType = "2014";
                            break;
                        case 6: // horse
                            YearType = "2013";
                            break;
                        case 7: // horse
                            YearType = "2012";
                            break;
                        case 8: // horse
                            YearType = "2011";
                            break;
                        case 9: // horse
                            YearType = "2010";
                            break;
                        case 10: // horse
                            YearType = "2009";
                            break;




                    }
                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicked OK
                    etYearOfManufacture.setText(YearType);

                }

            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (v == btnUpdateProfile) {
            if (isValid())
                updateVehicleProfile();
            else Toast.makeText(UpdateVehicleProfileActivity.this, "Please enter all valid values", Toast.LENGTH_SHORT).show();
        }

    }

    private void setVehicleDetails() {
        if (vehicleProfileModel != null) {
            etVehicleNo.setText(vehicleProfileModel.getVehicleNo());
            etMake.setText(vehicleProfileModel.getMake());
            etModel.setText(vehicleProfileModel.getModel());
            etColor.setText(vehicleProfileModel.getColor());
            etFuelType.setText(vehicleProfileModel.getFuelType());
            etSpeedLimit.setText(vehicleProfileModel.getSpeedLimit());
            etYearOfManufacture.setText(vehicleProfileModel.getYrOfManufacture());
        }
    }

    private void updateVehicleProfile() {
        progressDialogStart(UpdateVehicleProfileActivity.this, "Please wait...");
        try {
            auth = auth.replace("\n", "");
            RetrofitInterface retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = retrofitInterface.fnUpdateVehicleProfile(auth, apiKey,
                    userTypeId,
                    userId,
                    vehicleId,
                    vehicleProfileModel.getDefaultDriverId(),
                    vehicleProfileModel.getVehicleTypeId(),
                    etSpeedLimit.getText().toString().trim(),
                    vehicleProfileModel.getChassisNo(),
                    etMake.getText().toString().trim(),
                    etModel.getText().toString().trim(),
                    etYearOfManufacture.getText().toString().trim(),
                    etColor.getText().toString().trim(),
                    etFuelType.getText().toString().trim(),
                    vehicleProfileModel.getFirstOwner(),
                    vehicleProfileModel.getInsuranceCompany(),
                    vehicleProfileModel.getInsurancePolicyNo(),
                    vehicleProfileModel.getInsuranceExpiryDate());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        progressDialogStop();
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code: " + strResponceCode);
                        Log.e(TAG, "Response body: " + responseBody.toString());
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                Log.e(TAG, "Response strResponse: " + strResponse);
                                JSONObject jsonObject = new JSONObject(strResponse);

                                String msg = "";
                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                    if (jsonObject.has("data"))
                                        msg = jsonObject.optString("data");
                                    else if (jsonObject.has("msg"))
                                        msg = jsonObject.optString("msg");
                                    else msg = "Server not responding.";
                                    new AlertDialog.Builder(UpdateVehicleProfileActivity.this)
                                            .setMessage(msg)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .show();
                                } else {
                                    if(jsonObject.has("data")) {

                                        JSONObject jsonObjectt = jsonObject.optJSONObject("data");
                                        Iterator<String> keys = jsonObjectt.keys();
                                        while (keys.hasNext()) {
                                            String key = keys.next();
                                            String value = msg + jsonObjectt.getString(key);
                                            msg = value+"\n";
                                            Log.v("category key", key);
                                            Log.v("category value", value);
                                        }
                                        new androidx.appcompat.app.AlertDialog.Builder(UpdateVehicleProfileActivity.this)
                                                .setMessage(msg)
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }

                                                }).show();
                                    }
                                    else {
                                        new androidx.appcompat.app.AlertDialog.Builder(UpdateVehicleProfileActivity.this)
                                                .setMessage(jsonObject.optString("msg"))
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }

                                                }).show();
                                    }
                                }

                                break;
                            case 400:
                                Log.d(TAG, "response code: " + 400);
                                finish();
                                break;
                        }
                    } catch (Exception ex) {
                        progressDialogStop();
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialogStop();
                  /*  new android.support.v7.app.AlertDialog.Builder(UpdateVehicleProfileActivity.this)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();*/
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void retrieveVehicleProfile() {
        progressDialogStart(this, "Please Wait...");
        try {
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<VehicleProfileResponse> call = objRetrofitInterface.fnRetriveVehicleProfile(auth, apiKey, vehicleId);

            Log.d(TAG, "vehicleId: " + vehicleId);

            call.enqueue(new Callback<VehicleProfileResponse>() {
                @Override
                public void onResponse(Call<VehicleProfileResponse> call, Response<VehicleProfileResponse> response) {
                    try {
                        progressDialogStop();
                        Log.e(TAG, "Response " + response);
                        List<VehicleProfileModel> vehicleProfileModels = response.body().getData();
                        Log.e(TAG, "vehicleMasterModels: " + vehicleProfileModels.size());
                        vehicleProfileModel = vehicleProfileModels.get(0);
                        setVehicleDetails();
                    } catch (Exception ex) {
                        progressDialogStop();
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<VehicleProfileResponse> call, Throwable t) {
                    Log.e(TAG, "Responce " + call + t);
                    progressDialogStop();
                    new androidx.appcompat.app.AlertDialog.Builder(UpdateVehicleProfileActivity.this)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                }
            });
        } catch (Exception ex) {
            progressDialogStop();
            Log.e(TAG, "Api fail: " + ex.getMessage());
        }

    }

    private boolean isValid(){
        boolean isValid  = false;
        if (etVehicleNo.getText().toString().trim().length() == 0){
            isValid = false;
            etVehicleNo.setError("Please enter valid value");
        }else if (etMake.getText().toString().trim().length() == 0){
            isValid = false;
            etMake.setError("Please enter valid value");
        }else if (etModel.getText().toString().trim().length() == 0){
            isValid = false;
            etModel.setError("Please enter valid value");
        }else if (etColor.getText().toString().trim().length() == 0){
            isValid = false;
            etColor.setError("Please enter valid value");
        }else if (etFuelType.getText().toString().trim().length() == 0){
            isValid = false;
            etFuelType.setError("Please enter valid value");
        }else if (etSpeedLimit.getText().toString().trim().length() == 0){
            isValid = false;
            etSpeedLimit.setError("Please enter valid value");
        }else if (etYearOfManufacture.getText().toString().trim().length() == 0){
            isValid = false;
            etYearOfManufacture.setError("Please enter valid value");
        }else if (etYearOfManufacture.getText().toString().trim().length() != 4){
            isValid = false;
            etYearOfManufacture.setError("Please enter valid value");
        }else {
            isValid = true;
            etVehicleNo.setError(null);
            etYearOfManufacture.setError(null);
            etSpeedLimit.setError(null);
            etColor.setError(null);
            etMake.setError(null);
        }
        return isValid;
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
//vehicleNo, model, color, fuel_type, speedlimit, yr_of_manufacture

/*
user_type_id*
user_id*
"vehicle_id*
default_driver_id*
equipmentType*        (Car, Bus, Truck)
speedLimit*
chassis_no
engine_no
make
model
yr_of_manufacture
color
fuel_type
first_owner
insurance_company
insurance_policy_no
insurance_expiry_date"
 */