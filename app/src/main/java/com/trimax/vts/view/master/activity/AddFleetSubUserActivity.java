package com.trimax.vts.view.master.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class AddFleetSubUserActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = AddFleetSubUserActivity.class.getSimpleName();
    private TextInputEditText etFirstName, etLastName, etMobileNo, etEmail;
    private Button btnAddUser;
    private String action = "";
    private String subUserId;
    private String strId, strUserId;
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fleet_subuser);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        closeKeyBoard(this);
        infoPref = new TravelpulseInfoPref(this);
        initView();
        setListner();
        loadDefaultData();
    }

    private void loadDefaultData() {
        strId = infoPref.getString("id", PrefEnum.Login);
        strUserId = infoPref.getString(getString(R.string.user_type_id), PrefEnum.Login);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.ISubUser.ACTION)) {
            action = intent.getStringExtra(Constants.IDriver.ACTION);
            Log.d(TAG, "action: "+action);
            if (intent.hasExtra(Constants.ISubUser.FIRST_NAME)) {
                String firstName = intent.getStringExtra(Constants.ISubUser.FIRST_NAME);
                String lastName = intent.getStringExtra(Constants.ISubUser.LAST_NAME);
                String email = intent.getStringExtra(Constants.ISubUser.EMAIL);
                String mobileNumber = intent.getStringExtra(Constants.ISubUser.MOBILE_NUMBER);
                subUserId = intent.getStringExtra(Constants.ISubUser.USER_ID);
                btnAddUser.setText(Constants.ISubUser.UPDATE);
                etFirstName.setText(firstName);
                etMobileNo.setText(mobileNumber);
                etLastName.setText(lastName);
                etEmail.setText(email);
            }
        }
    }

    private void setListner() {
        btnAddUser.setOnClickListener(this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        action = intent.getStringExtra(Constants.ISubUser.ACTION);

        if(action.equalsIgnoreCase(Constants.ISubUser.UPDATE)){
            getSupportActionBar().setTitle(R.string.editsubuser);
        }else if(action.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle(R.string.addsubuser);

        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.road_pulse_action_bar_color)));

        etFirstName = findViewById(R.id.et_user_first_name);
        etLastName = findViewById(R.id.et_user_last_name);
        etMobileNo = findViewById(R.id.et_user_mobile_number);
        etEmail = findViewById(R.id.et_user_email);
        btnAddUser = findViewById(R.id.btn_add_sub_user);
    }

    private boolean isValid(){
        boolean isValid = false;

        if (etFirstName.getText().toString().trim().length() == 0) {
            isValid = false;
            etFirstName.setError("Please enter name.");
        } else if (etLastName.getText().toString().trim().length() == 0) {
            isValid = false;
            etLastName.setError("Please enter Last Name.");
        }else if (etEmail.getText().toString().trim().length() == 0) {
            isValid = false;
            etEmail.setError("Please enter valid Email");
        }
        else if (etEmail.getText().toString().equalsIgnoreCase("")) {
            isValid = false;
            etEmail.setError("Please enter valid Email");
        }else if (etMobileNo.getText().toString().trim().length() == 0) {
            isValid = false;
            etMobileNo.setError("Please enter mobile number.");
        }else if (etMobileNo.getText().toString().trim().length() != 10) {
            isValid = false;
            etMobileNo.setError("Please enter valid mobile number.");
        }else if(String.valueOf(etMobileNo.getText().toString().charAt(0)).equalsIgnoreCase("0")){
            isValid = false;
            etMobileNo.setError("Please enter valid  mobile number");
        }
        else {
            isValid = true;
            etFirstName.setError(null);
            etLastName.setError(null);
            etEmail.setError(null);
            etMobileNo.setError(null);
        }
        return isValid;
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddUser){
            if (isValid()){
                if (action.trim().equalsIgnoreCase(Constants.ISubUser.UPDATE.trim()))
                    editUser();
                else
                    addUser();
            }else Toast.makeText(AddFleetSubUserActivity.this, "Please enter all details.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editUser() {
        progressDialogStart(AddFleetSubUserActivity.this, "Please wait...");
        try {
            auth = auth.replace("\n", "");
            RetrofitInterface retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = retrofitInterface.fnUpdateSubUser(auth, apiKey,
                    strUserId,
                    strId,
                    etFirstName.getText().toString(),
                    etLastName.getText().toString(),
                    etMobileNo.getText().toString(),
                    etEmail.getText().toString(),
                    "1",
                    subUserId);

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
                                JSONObject obj = new JSONObject(strResponse);
                                String msg = obj.optString("msg");
                                if (obj.has("data"))
                                    msg = obj.optString("data");
                                new AlertDialog.Builder(AddFleetSubUserActivity.this)
                                        .setMessage(msg)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(AddFleetSubUserActivity.this,
                                                        Activity_FleetSubUsers.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                                break;
                            case 400:
                                Log.d(TAG, "response code: "+400);
                                Toast.makeText(AddFleetSubUserActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception ex) {
                        progressDialogStop();
                        Toast.makeText(AddFleetSubUserActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialogStop();
                    new androidx.appcompat.app.AlertDialog.Builder(AddFleetSubUserActivity.this)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
//                    Toast.makeText(AddFleetSubUserActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(AddFleetSubUserActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addUser() {
        progressDialogStart(AddFleetSubUserActivity.this, "Please wait...");
            auth = auth.replace("\n", "");
            RetrofitInterface retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = retrofitInterface.fnAddSubUser(auth, apiKey,
                    strUserId,
                    strId,
                    etFirstName.getText().toString(),
                    etLastName.getText().toString(),
                    etMobileNo.getText().toString(),
                    etEmail.getText().toString());

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
                                    new AlertDialog.Builder(AddFleetSubUserActivity.this)
                                            .setMessage(msg)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(AddFleetSubUserActivity.this,
                                                            Activity_FleetSubUsers.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }

                                else {

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
                                        new androidx.appcompat.app.AlertDialog.Builder(AddFleetSubUserActivity.this)
                                                .setMessage(msg)
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }

                                                }).show();
                                    }
                                    else {
                                        new androidx.appcompat.app.AlertDialog.Builder(AddFleetSubUserActivity.this)
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
                                Toast.makeText(AddFleetSubUserActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception ex) {
                        progressDialogStop();
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                        Toast.makeText(AddFleetSubUserActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialogStop();
                    new AlertDialog.Builder(AddFleetSubUserActivity.this)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(AddFleetSubUserActivity.this,
                                            Activity_FleetSubUsers.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
//                    Toast.makeText(AddFleetSubUserActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
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
