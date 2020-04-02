package com.trimax.vts.view.master.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.master.callbacks.AddGroupCallback;
import com.trimax.vts.view.master.fragment.VehicleListDialogFragment;
import com.trimax.vts.view.master.model.VehicleGroup.EditGroup.EditGroupModel;
import com.trimax.vts.view.master.model.VehicleGroup.EditGroup.EditGroupModelResponse;
import com.trimax.vts.view.master.model.VehicleGroup.EditGroup.EditGroupSubUser;
import com.trimax.vts.view.master.model.VehicleGroup.EditGroup.Vehicle;
import com.trimax.vts.view.master.model.VehicleGroup.FleetUsers.UsersForAddGroupModel;
import com.trimax.vts.view.master.model.VehicleGroup.FleetUsers.UsersForAddGroupModelResponse;
import com.trimax.vts.view.master.model.VehicleGroup.VehiclesList.VehiclesForAddGroupModel;
import com.trimax.vts.view.master.model.VehicleGroup.VehiclesList.VehiclesForAddGroupModelResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class AddVehicleGroupActivity extends BaseActivity implements View.OnClickListener, AddGroupCallback {
    private String TAG = AddVehicleGroupActivity.class.getSimpleName();
    private String cust_id, usertype_id, gruopId;
    private HashMap<String, Boolean> vehiclesHashMap, usersHashMap;
    private TextInputEditText etTitle, etVehicles, etUsers;
    private Button btnAddGrp;
    private boolean isUpdate = false;
    private ArrayList<VehiclesForAddGroupModel> vehiclesForAddGroupModels;
    private ArrayList<Vehicle> vehicleForAddGroupModels;
    private ArrayList<EditGroupSubUser> userForAddGroupModels;
    private ArrayList<UsersForAddGroupModel> usersForAddGroupModels;
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_group);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        closeKeyBoard(this);
        infoPref = new TravelpulseInfoPref(this);
        initView();
        setListner();
        loadDefaultData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
       String action = intent.getStringExtra(Constants.IVehicle.ACTION);
        btnAddGrp = findViewById(R.id.btn_add_grp);

        if(action.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle(R.string.addvehiclegroup);
            btnAddGrp.setText("Add Vehicle Group");
            getSupportActionBar().setTitle("Add Vehicle Group");
        }else{
            btnAddGrp.setText("Update Vehicle Group");
            getSupportActionBar().setTitle("Edit Vehicle Group");

        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_action_bar_color)));

        if (vehiclesHashMap == null)
            vehiclesHashMap = new HashMap<>();
        if (usersHashMap == null)
            usersHashMap = new HashMap<>();

        btnAddGrp = findViewById(R.id.btn_add_grp);
        etTitle = findViewById(R.id.et_group_title);
        etVehicles = findViewById(R.id.et_group_vehicles1);
        etUsers = findViewById(R.id.et_group_users);
    }

    private void setListner() {
        btnAddGrp.setOnClickListener(this);
        etVehicles.setOnClickListener(this);
        etUsers.setOnClickListener(this);
    }

    private void loadDefaultData() {
        cust_id = infoPref.getString("id", PrefEnum.Login);
        usertype_id = infoPref.getString(getString(R.string.user_type_id), PrefEnum.Login);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.IVehicle.GROUP_ID)) {
            gruopId = intent.getStringExtra(Constants.IVehicle.GROUP_ID);
            etTitle.setText(intent.getStringExtra(Constants.IVehicle.TTILE));
            isUpdate = true;
        }

        if (isUpdate) {
            getGroupwiseExistingData();
        }
        else{
            getVehiclesList();
            getFleetUsersList();

        }
    }

    public void updateVehicleList() {
        String vehicles = "";
        int count = 0;
        for (String s : vehiclesHashMap.keySet()) {
//            Log.d(TAG, "Vehiclesmap: " + s + " " + vehiclesHashMap.get(s));
            if (vehiclesHashMap.get(s)) {
                count += 1;
                if (vehicles.trim().equalsIgnoreCase(""))
                    vehicles = s;
                else
                    vehicles = vehicles + " ," + s;
            }
        }
        Log.d(TAG, "Vehicles count: " + count);
        Log.d(TAG, "Vehicles: " + vehicles);
        etVehicles.setText(count+" Vehicle Selected");

    }

    public void updateUsersList() {
        String users = "";
        int count = 0;
        for (String s : usersHashMap.keySet()) {
            Log.d(TAG, "UsersHashMap: " + s + " " + usersHashMap.get(s));
            if (usersHashMap.get(s)) {
                count += 1;
                if (users.trim().equalsIgnoreCase(""))
                    users = s;
                else
                    users = users + " ," + s;
            }
        }
        Log.d(TAG, "users count: " + count);
        Log.d(TAG, "users: " + users);
        etUsers.setText(count+" User Selected");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vehiclesHashMap.clear();
        usersHashMap.clear();
    }

    private void editGroup() {
            progressDialogStart(this, "Please Wait...");
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.fnUpdateVehicleGroup(auth, apiKey, usertype_id,
                    cust_id,
                    getAllSelectedVehicleIds() + "",
                    getAllSelectedUserIds() + ""
                    , etTitle.getText().toString().trim(),
                    gruopId
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
//                        progressDialogStart(AddVehicleGroupActivity.this, "Please wait...");
                        Log.e(TAG, "Response " + response);
                        ResponseBody responseBody = response.body();
                        Log.e(TAG, "Response body: " + responseBody.toString());
                        int strResponceCode = response.code();
                        Log.e(TAG, "Responce code" + strResponceCode);
                        progressDialogStop();
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                    Log.e(TAG, "Responce strResponse " + strResponse);
                                JSONObject jsonObject = new JSONObject(strResponse);

                                String msg = "";
                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {

                                    new AlertDialog.Builder(AddVehicleGroupActivity.this)
                                            .setMessage("Vehicle Group is updated successfully")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(AddVehicleGroupActivity.this, Activity_Vehical_Group.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }).show();

                                }
                                else {

                                    if(jsonObject.has("data")) {

                                        new androidx.appcompat.app.AlertDialog.Builder(AddVehicleGroupActivity.this)
                                                .setMessage(msg)
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }

                                                }).show();
                                    }
                                    else {
                                        new androidx.appcompat.app.AlertDialog.Builder(AddVehicleGroupActivity.this)
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
                                progressDialogStop();
                                break;
                        }

                    } catch (Exception ex) {
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                        progressDialogStop();
                        new androidx.appcompat.app.AlertDialog.Builder(AddVehicleGroupActivity.this)
                                .setMessage("Server not responding.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Responce " + call + t);
                    stopProgressDialog();
                    new AlertDialog.Builder(AddVehicleGroupActivity.this)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });

    }


    private String getAllSelectedVehicleIds() {
        ArrayList<Integer> vehicleIds = new ArrayList<>();
        for (String key : vehiclesHashMap.keySet()) {
            if (vehiclesHashMap.get(key))
            vehicleIds.add((getVehicleModelUpdate(key).getId()));
        }

        Log.d(TAG, "vehicleIds: " + vehicleIds.size());
        String idList = vehicleIds.toString();
        String csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
        return csv;
    }

    private String getAllSelectedUserIds() {
        ArrayList<Integer> vehicleIds = new ArrayList<>();
        for (String key : usersHashMap.keySet()) {
            if (usersHashMap.get(key))
                //vehicleIds.add(Integer.parseInt(getUserModel(key).getId()));
            vehicleIds.add(Integer.parseInt(getUserModelUpdate(key).getId()));


        }
        Log.d(TAG, "userIds: " + vehicleIds.toString());
        String idList = vehicleIds.toString();
        String csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
        return csv;
    }

    private String getAllSelectedVehicleIdsADD() {
        ArrayList<Integer> vehicleIds = new ArrayList<>();
        for (String key : vehiclesHashMap.keySet()) {
            if (vehiclesHashMap.get(key))
                vehicleIds.add((getVehicleModel(key).getId()));
        }
        Log.d(TAG, "vehicleIds: " + vehicleIds.size());
        String idList = vehicleIds.toString();
        String csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
        return csv;
    }

    private String getAllSelectedUserIdsADD() {
        ArrayList<Integer> vehicleIds = new ArrayList<>();
        for (String key : usersHashMap.keySet()) {
            if (usersHashMap.get(key))
                vehicleIds.add(Integer.parseInt(getUserModel(key).getId()));
        }

        Log.d(TAG, "userIds: " + vehicleIds.toString());
        String idList = vehicleIds.toString();
        String csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
        return csv;
    }


    private VehiclesForAddGroupModel getVehicleModel(String vehicleNo) {
        for (VehiclesForAddGroupModel v : vehiclesForAddGroupModels) {
            if (v.getVehicleNo().trim().equalsIgnoreCase(vehicleNo.trim()))
                return v;
        }
        return null;
    }
    private Vehicle getVehicleModelUpdate(String vehicleNo) {
        for (Vehicle v : vehicleForAddGroupModels) {
            if (v.getVehicleNo().trim().equalsIgnoreCase(vehicleNo.trim()))
                return v;
        }
        return null;
    }

    private UsersForAddGroupModel getUserModel(String userName) {
        for (UsersForAddGroupModel v : usersForAddGroupModels) {
            String nm = v.getFirstName() + " " + v.getLastName();
            if (nm.trim().equalsIgnoreCase(userName.trim()))
                return v;
        }
        return null;
    }
    private EditGroupSubUser getUserModelUpdate(String userName) {
        for (EditGroupSubUser v : userForAddGroupModels) {
            String nm = v.getFirstName() + " " + v.getLastName();
            if (nm.trim().equalsIgnoreCase(userName.trim()))
                return v;
        }
        return null;
    }

    private void addGroup() {
         progressDialogStart(AddVehicleGroupActivity.this, "Please wait...");

            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.fnAddGroup(auth, apiKey, usertype_id,
                    cust_id,
                    getAllSelectedVehicleIdsADD() + "",
                    getAllSelectedUserIdsADD() + "",
                    etTitle.getText().toString().trim()
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.e(TAG, "Response " + response);
                        ResponseBody responseBody = response.body();
                        Log.e(TAG, "Response body: " + responseBody.toString());
                        int strResponceCode = response.code();
                        Log.e(TAG, "Responce code" + strResponceCode);
                        progressDialogStop();
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                Log.e(TAG, "Responce strResponse " + strResponse);
                                JSONObject jsonObject = new JSONObject(strResponse);
                                String msg = "";
                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {

                                    new AlertDialog.Builder(AddVehicleGroupActivity.this)
                                            .setMessage("Vehicle Group is added successfully")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(AddVehicleGroupActivity.this, Activity_Vehical_Group.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }).show();
                                }
                                else {
                                    if (jsonObject.has("data"))
                                        msg = jsonObject.optString("data");
                                    else if (jsonObject.has("msg"))
                                        msg = jsonObject.optString("msg");
                                    else msg = "Server not responding.";


                                    new AlertDialog.Builder(AddVehicleGroupActivity.this)
                                            .setMessage(msg)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                    stopProgressDialog();
                                }
//                                Utils.stopProgressDialog();
                                break;
                            case 400:
                                stopProgressDialog();
//                                 Utils.stopProgressDialog();
                                break;
                        }

                    } catch (Exception ex) {
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                        stopProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Responce " + call + t);
                    stopProgressDialog();
                    new AlertDialog.Builder(AddVehicleGroupActivity.this)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
    }

    @Override
    public void onClick(View v) {
        if (v == etVehicles) {
            closeKeyBoard(this);
            if (vehiclesHashMap.size() > 0) {

                VehicleListDialogFragment vehicleListDialogFragment =
                        new VehicleListDialogFragment(AddVehicleGroupActivity.this, Constants.IVehicle.VEHICLE_LIST,
                                vehiclesHashMap, this);
                vehicleListDialogFragment.show(getFragmentManager(), "Select Vehicles");
            }
            else {
                Toast.makeText(AddVehicleGroupActivity.this,"Please wait while loading",Toast.LENGTH_LONG).show();
                getVehiclesList();
            }
        }

        if (v == etUsers) {
            closeKeyBoard(this);
            if (usersHashMap.size() > 0) {
                VehicleListDialogFragment vehicleListDialogFragment =
                        new VehicleListDialogFragment(AddVehicleGroupActivity.this, "", usersHashMap, this);
                vehicleListDialogFragment.show(getFragmentManager(), "Select Users");
            } else {
                Toast.makeText(AddVehicleGroupActivity.this,"please wait While Loading",Toast.LENGTH_LONG);
                getFleetUsersList();
            }
        }

        if (v == btnAddGrp) {
            if (isValid()) {
                if (isUpdate) {
                    editGroup();
                } else
                    addGroup();
            } else {
                Toast.makeText(AddVehicleGroupActivity.this, "Please enter all details..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValid() {
        boolean isValid = false;
        if (etTitle.getText().toString().trim().length() == 0) {
            isValid = false;
            etTitle.setError("Please enter valid value.");
        } else if (etVehicles.getText().toString().trim().length() == 0) {
            isValid = false;
            etVehicles.setError("Please select vehicles.");
        }else if (etVehicles.getText().toString().equalsIgnoreCase("0")) {
            isValid = false;
            etVehicles.setError("Please select vehicles.");
        }else if (etUsers.getText().toString().equalsIgnoreCase("0")) {
            isValid = false;
            etUsers.setError("Please select User.");
        } else if (etUsers.getText().toString().trim().length() == 0) {
            isValid = false;
            etUsers.setError("Please select User.");
        }
        else isValid = true;
        etTitle.setError(null);
        etVehicles.setError(null);
        etUsers.setError(null);

        return isValid;
    }

    @Override
    public void updateVehiclesHashmap(String key, Boolean value) {
        vehiclesHashMap.put(key, value);
    }

    @Override
    public void updateUsersHashmap(String key, Boolean value) {
        usersHashMap.put(key, value);
    }

    @Override
    public void onOkClick(boolean isVehicle) {
        if (isVehicle)
            updateVehicleList();
        else updateUsersList();
    }


    private void getVehiclesList() {
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<VehiclesForAddGroupModelResponse> call = objRetrofitInterface.fnGetVehicleListForAddGroup(auth,
                    apiKey, usertype_id,
                    cust_id);

            Log.d(TAG, "strId: Vehicle " + cust_id);
            Log.d(TAG, "strUserId: Vehicle " + usertype_id);

            call.enqueue(new Callback<VehiclesForAddGroupModelResponse>() {
                @Override
                public void onResponse(Call<VehiclesForAddGroupModelResponse> call, Response<VehiclesForAddGroupModelResponse> response) {
                    stopProgressDialog();
                    try {
                        Log.e(TAG, "Response Vehicle " + response);
                        vehiclesForAddGroupModels = (ArrayList<VehiclesForAddGroupModel>) response.body().getData();
                        Log.e(TAG, "Response body Vehicles: " + vehiclesForAddGroupModels.size());

                        setVehicles(vehiclesForAddGroupModels);
                    } catch (Exception ex) {
                        Log.e(TAG, "This is an error: Vehicle " + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<VehiclesForAddGroupModelResponse> call, Throwable t) {
                    Log.e(TAG, "Responce Vehicle " + call + t);
                    stopProgressDialog();
                }
            });
    }


    private void getFleetUsersList() {
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<UsersForAddGroupModelResponse> call = objRetrofitInterface.fnGetFleetSubUsersListForAddGroup(auth, apiKey, usertype_id,
                    cust_id);

            Log.d(TAG, "strId: User " + cust_id);
            Log.d(TAG, "strUserId: User " + usertype_id);

            call.enqueue(new Callback<UsersForAddGroupModelResponse>() {
                @Override
                public void onResponse(Call<UsersForAddGroupModelResponse> call, Response<UsersForAddGroupModelResponse> response) {

                    stopProgressDialog();
                    try {
                        Log.e(TAG, "Response User " + response);
                        usersForAddGroupModels = (ArrayList<UsersForAddGroupModel>) response.body().getData();

                        Log.e(TAG, "Response Users : " + usersForAddGroupModels.size());

                        setUsers(usersForAddGroupModels);
                    } catch (Exception ex) {
                        Log.e(TAG, "This is an error: User " + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UsersForAddGroupModelResponse> call, Throwable t) {
                    Log.e(TAG, "Responce User " + call + t);
                    stopProgressDialog();
                }
            });
    }


    private void setVehicles(List<VehiclesForAddGroupModel> vehicles) {
        try {
            if (vehicles != null) {
                if (vehicles.size() > 0) {
                    for (VehiclesForAddGroupModel vehicle : vehicles) {
                        vehiclesHashMap.put(vehicle.getVehicleNo(), false);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void setVehiclesNew(List<Vehicle> vehicles) {
        try {
            if (vehicles != null) {
                if (vehicles.size() > 0) {
                    for (Vehicle vehicle : vehicles) {
                        vehiclesHashMap.put(vehicle.getVehicleNo(), false);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


    private void setUsers(List<UsersForAddGroupModel> users) {
        try {
            if (users != null) {
                if (users.size() > 0) {
                    for (UsersForAddGroupModel user : users) {
                        usersHashMap.put(user.getFirstName() + " " + user.getLastName(), false);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void setUsersNew(List<EditGroupSubUser> users) {
        try {
            if (users != null) {
                if (users.size() > 0) {
                    for (EditGroupSubUser user : users) {
                        usersHashMap.put(user.getFirstName() + " " + user.getLastName(), false);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddVehicleGroupActivity.this, Activity_Vehical_Group.class);
        startActivity(intent);
        finish();
    }


    private void getGroupwiseExistingData() {
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<EditGroupModelResponse> call = objRetrofitInterface.fnEditGroup(auth, apiKey, usertype_id, cust_id, gruopId);

            Log.d(TAG, "strId GrpwiseVehicles: " + cust_id);
            Log.d(TAG, "strUserId GrpwiseVehicles: " + usertype_id);
            Log.d(TAG, "strUserId GrpwiseVehicles--gruopId: " + gruopId);
//            Log.d(TAG, "groupId: " + groupId);
                        progressDialogStart(AddVehicleGroupActivity.this, "Please wait...");

            call.enqueue(new Callback<EditGroupModelResponse>() {
                @Override
                public void onResponse(Call<EditGroupModelResponse> call, Response<EditGroupModelResponse> response) {
                        progressDialogStop();
                        Log.e(TAG, "Response GrpwiseVehicles" + response);
                        EditGroupModel editGroupModel = response.body().getData();
                        Log.e(TAG, "groupwiseVehicleModels GrpwiseVehicles " + editGroupModel);
                        setExistingGroupwiseData(editGroupModel);
                }

                @Override
                public void onFailure(Call<EditGroupModelResponse> call, Throwable t) {
                    Log.e(TAG, "Responce GrpwiseVehicles " + call + t);
                    stopProgressDialog();
                }
            });
    }

    private void setExistingGroupwiseData(EditGroupModel editGroupModel) {
        try {
            Log.d(TAG, "vehiclesHashMap inside: " + vehiclesHashMap);
            Log.d(TAG, "editGroupModel inside: " + editGroupModel);
            if (editGroupModel != null) {
                vehicleForAddGroupModels= (ArrayList<Vehicle>) editGroupModel.getVehicles();
                userForAddGroupModels= (ArrayList<EditGroupSubUser>) editGroupModel.getSubusers();
                setVehiclesNew(editGroupModel.getVehicles());

                setUsersNew(editGroupModel.getSubusers());
                setExstingVehicleList(editGroupModel.getVehicles(), editGroupModel.getSelectedVehicles());
                setExstingUsersList(editGroupModel.getSubusers(), editGroupModel.getSubUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void setExstingVehicleList(List<Vehicle> vehicles, List<String> selectedVehicles) {
        try {
            Log.d(TAG, "setExstingVehicleList: selectedVehicles: " + selectedVehicles + "\n vehicles: " + vehicles);
            if (selectedVehicles == null)
                selectedVehicles = new ArrayList<>();

            if (vehicles.size() > 0) {
                if (vehiclesHashMap.size() > 0) {
                    for (Vehicle vehicle : vehicles) {
                        Log.d(TAG, "vehicleNO: " + vehicle.getId() + " selectedVehicles: " + selectedVehicles);
                        if (selectedVehicles.contains(String.valueOf(vehicle.getId()))) {
                            vehiclesHashMap.put(vehicle.getVehicleNo().trim(), true);
                        } else {
                            vehiclesHashMap.put(vehicle.getVehicleNo().trim(), false);
                        }
                    }
                } else {
                    for (Vehicle vehicle : vehicles) {
                        Log.d(TAG, "vehicleNO: " + vehicle.getId() + " selectedVehicles: " + selectedVehicles);
                        if (selectedVehicles.contains(String.valueOf(vehicle.getId()))) {
                            vehiclesHashMap.put(vehicle.getVehicleNo().trim(), true);
                        } else {
                            vehiclesHashMap.put(vehicle.getVehicleNo().trim(), false);
                        }
                    }
                }
            }
            updateVehicleList();
        } catch (Exception e) {
            Log.d(TAG, "Vehicle error: " + e.getMessage());
        }
    }

    private void setExstingUsersList(List<EditGroupSubUser> users, List<String> selectedUsers) {
        try {
            Log.d(TAG, "setExstingUsersList: selectedUsers: " + selectedUsers + "\n users: " + users);
            if (selectedUsers == null)
                selectedUsers = new ArrayList<>();

            if (users.size() > 0) {
                if (usersHashMap.size() > 0) {
                    for (EditGroupSubUser user : users) {
                        Log.d(TAG, "user: " + user.getId() + " selectedUsers: " + selectedUsers);
                        if (selectedUsers.contains(user.getId().trim())) {
                            String username = user.getFirstName() + " " + user.getLastName();
                            usersHashMap.put(username, true);
                        }
                    }
                } else {
                    for (EditGroupSubUser user : users) {
                        Log.d(TAG, "user: " + user.getId() + " selectedUsers: " + selectedUsers);
                        if (selectedUsers.contains(user.getId().trim())) {
                            String username = user.getFirstName() + " " + user.getLastName();
                            usersHashMap.put(username, true);
                        }
                    }
                }
            }
            updateUsersList();
        } catch (Exception e) {
            Log.d(TAG, "User error: " + e.getMessage());
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
}


