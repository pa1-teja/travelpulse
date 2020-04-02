package com.trimax.vts.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.dashboard.CustomerDashboardActivity;
import com.trimax.vts.view.dashboard.FleetUserDashBoard;
import com.trimax.vts.view.R;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.database.DatabaseClass;
import com.trimax.vts.model.MenuModel;
import com.trimax.vts.view.login.model.FeatureModel;
import com.trimax.vts.view.login.model.LoginData;
import com.trimax.vts.view.login.model.LoginResponse;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity  {
    private static final String TAG = "LoginActivity";
    private EditText emailEdittext, passwordEdittext;
    private Context context;
    ProgressDialog dialog;
    DatabaseClass db;
    ArrayList<MenuModel> menumodels = new ArrayList<>();
    private TravelpulseInfoPref infoPref;

    CommonClass cc;
    private LocationManager locationManager;
    String email;

    String strMessage="Please Try Again";
   ProgressBar progressbar_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().hide();
        infoPref = new TravelpulseInfoPref(this);
        context = this;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*getLocationPermission();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,2000);
        }*/

        cc = new CommonClass(context);
        progressbar_login= findViewById(R.id.progressbar_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Button login = findViewById(R.id.bLogin);
        TextView forgetPassword = findViewById(R.id.tvforget);
        emailEdittext =  findViewById(R.id.eTUserName);
        passwordEdittext = findViewById(R.id.eTPassword);

        emailEdittext.setText((infoPref.getString("username", PrefEnum.Login)));
        passwordEdittext.setText((infoPref.getString("password", PrefEnum.Login)));
        if (!infoPref.isKeyContains("record_id",PrefEnum.OneSignal)){
            if (TextUtils.isEmpty(infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal)))
                Utils.getPlayerId(this,infoPref);

            Log.d(TAG, "onCreate: RecordId  "+infoPref.getString("token", PrefEnum.OneSignal)+"  "+infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal));
            Utils.getRecordId(infoPref.getString("deviceid", PrefEnum.OneSignal), "IMEI", infoPref.getString("token", PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),infoPref);
        }
        forgetPassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ii = new Intent(LoginActivity.this, ForgetPasswordActivityNew.class);
                startActivity(ii);
            }
        });

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                    email = emailEdittext.getText().toString();
                    if (CommonClass.isNullOrEmpty(email)) {
                        CommonClass.DisplayToast(context,"Enter valid email","center");
                    }
                    else if(email.length()>=50){
                        CommonClass.DisplayToast(context,"email length exceed","center");
                    }
                    else if (!isValidEmail(email)) {
                            CommonClass.DisplayToast(context,"Enter valid email or mobile","center");
                        }

                    String pass = passwordEdittext.getText().toString();
                    if (CommonClass.isNullOrEmpty(email)) {
                        CommonClass.DisplayToast(context,"Enter valid password","center");
                    }

                    else if (!isValidPassword(pass)) {
                            CommonClass.DisplayToast(context,"Enter valid password","center");
                    }

                    if (isValidEmail(email) && isValidPassword(pass)) {
                        if (TextUtils.isDigitsOnly(email)){
                            if (email.length()!=10 || email.contains(".")){
                                Toast.makeText(context, "Invalid mobile number", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (cc.isConnected(context)) {
                            fnLogin(email, pass,"A");
                        } else {
                            CommonClass.DisplayToast(context, getString(R.string.network_error_message), "bottom");
                        }
                    }
                    else {
                         Toast.makeText(context,"Enter valid details",Toast.LENGTH_LONG).show();
                    }
            }

        });

    }

    /*private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
        }
    }*/

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches() || TextUtils.isDigitsOnly(email);
    }

    private boolean isValidPassword(String pass) {
        return !TextUtils.isEmpty(pass);
    }

    public void fnLogin(String strUserName, final String strPassword,final String status) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait...");
            dialog.show();
            Call<LoginResponse> call = RepositryInstance.getLoginRepository().doLogin(strUserName,strPassword,status);
            call.enqueue(new Callback<LoginResponse>() {

                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                    if (response.isSuccessful()){
                        if (response.body()!=null) {
                            if (response.body().getStatus().equalsIgnoreCase("success")) {
                                LoginData data = response.body().getData();
                                infoPref.putString("id", data.getId(), PrefEnum.Login);
                                infoPref.putString("first_name", data.getFirstName(), PrefEnum.Login);
                                infoPref.putString("last_name", data.getLastName(), PrefEnum.Login);
                                infoPref.putString("emailAddress", data.getEmail(), PrefEnum.Login);
                                infoPref.putString("mobile_number", data.getMobileNo(), PrefEnum.Login);
                                infoPref.putString("user_type_id", data.getUserTypeId(), PrefEnum.Login);
                                infoPref.putString("status", data.getStatus(), PrefEnum.Login);
                                infoPref.putString("customer_id", data.getCustomerId(), PrefEnum.Login);
                                infoPref.putString("password", strPassword.trim(), PrefEnum.Login);
                                infoPref.putString("username", data.getUsername(), PrefEnum.Login);
                                infoPref.putString("url", ApiClient.BASE_URL, PrefEnum.Login);
                                infoPref.putString("logout", "N", PrefEnum.Login);
                                infoPref.putString("vid", data.getVehicleId(), PrefEnum.Login);
                                infoPref.putString("vno", data.getVehicleNo(), PrefEnum.Login);

                                db = new DatabaseClass(context);
                                db.deletedata();
                                menumodels.clear();

                                for (int i = 0; i < data.getFeature().size(); i++) {
                                    FeatureModel model = data.getFeature().get(i);
                                    MenuModel mm = new MenuModel(Integer.parseInt(model.getAndroidappMenuStatus()), model.getAndroidappMenuSequence());
                                    menumodels.add(mm);
                                }

                                if (infoPref.getString("user_type_id", PrefEnum.Login).equalsIgnoreCase("5") || infoPref.getString("user_type_id", PrefEnum.Login).equalsIgnoreCase("6")) {
                                    Intent i = new Intent(LoginActivity.this, FleetUserDashBoard.class);
                                    i.putExtra("menumodel", menumodels);
                                    startActivity(i);
                                    finishAffinity();
                                } else if (infoPref.getString("user_type_id", PrefEnum.Login).equalsIgnoreCase("7")) {
                                    Intent i = new Intent(LoginActivity.this, CustomerDashboardActivity.class);
                                    i.putExtra("menumodel", menumodels);
                                    startActivity(i);
                                    finishAffinity();
                                }
                            } else {
                                Crashlytics.log(strMessage + "  Login failure");
                                Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                }
            });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
