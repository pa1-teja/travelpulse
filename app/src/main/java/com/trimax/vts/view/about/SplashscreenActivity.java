package com.trimax.vts.view.about;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.TimeUtils;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.services.SendNotificationService;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.database.DatabaseClass;
import com.trimax.vts.model.MenuModel;
import com.trimax.vts.utils.Permissions;
import com.trimax.vts.view.BuildConfig;
import com.trimax.vts.view.R;
import com.trimax.vts.view.dashboard.CustomerDashboardActivity;
import com.trimax.vts.view.dashboard.FleetUserDashBoard;
import com.trimax.vts.view.login.LoginActivity;
import com.trimax.vts.view.login.model.LoginCheckResponse;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.api.ApiClient.BASE_URL;

public class SplashscreenActivity extends AppCompatActivity implements OSSubscriptionObserver{
	private final String TAG = "SplashscreenActivity";
	private String user_type_id = "",email="",password="", token ="",strMessage="Please Try Again";
	private String id = "",url ="", playerId ="", dataLogIn="", imei;
	private ArrayList<MenuModel> menumodels = new ArrayList<>();
	private DatabaseClass db;

	private ProgressDialog dialogdata;
	private TextView version_name;
	private TravelpulseInfoPref infoPref;
	private FirebaseInstanceId instanceId;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		Fabric.with(this, new Crashlytics());
        infoPref = new TravelpulseInfoPref(this);
		OneSignal.addSubscriptionObserver(this);
		db = new DatabaseClass(this);

		menumodels = db.getMenuPermission();
        version_name =findViewById(R.id.version_name);
		instanceId = FirebaseInstanceId.getInstance();
		Runnable thread = () -> {
			Log.d(TAG, "onCreate: "+instanceId.getId());
			Log.d(TAG, "onCreate: "+instanceId.getToken());
		};
		thread.run();

        if(BuildConfig.DEBUG){
			version_name.setText("Demo Version "+BuildConfig.VERSION_NAME);
		}
		else{
			version_name.setText("Version "+BuildConfig.VERSION_NAME);
		}

		imei = infoPref.getString("deviceid", PrefEnum.OneSignal);

		infoPref.putString("imei",imei,PrefEnum.Login);
		infoPref.putString("access","",PrefEnum.Login);
		user_type_id = infoPref.getString("user_type_id", PrefEnum.Login);
		id = infoPref.getString("id", PrefEnum.Login);
		url=infoPref.getString("url", PrefEnum.Login);
		email = infoPref.getString("username", PrefEnum.Login);
		password = infoPref.getString("password", PrefEnum.Login);

        if(infoPref.isKeyContains("GT_PLAYER_ID",PrefEnum.OneSignal)) {
            playerId = infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
            token =infoPref.getString("token", PrefEnum.OneSignal);
        }
        if (!infoPref.isKeyContains("record_id",PrefEnum.OneSignal)){
			Log.d(TAG, "onCreate: RecordId  "+token);
			if (TextUtils.isEmpty(playerId))
				Utils.getPlayerId(this,infoPref);
			Utils.getRecordId(imei, "IMEI", token, playerId,infoPref);
		}
        validateUser();
    }

	private void validateUser() {
		if (!BASE_URL.equalsIgnoreCase(url)) {
			clearVehicleId();
			infoPref.getLoginEditor().clear().apply();
			return;
		}
		fncheckIfLogin(infoPref.getString("record_id", PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal), token, id, user_type_id);
	}


	private void loginMethod() {
		if(dataLogIn.equalsIgnoreCase("A")) {
			if(menumodels !=null && menumodels.size()>0 ){
				if(infoPref.getString("user_type_id", PrefEnum.Login).equalsIgnoreCase("5") || infoPref.getString("user_type_id", PrefEnum.Login).equalsIgnoreCase("6")) {
					Intent i = new Intent(SplashscreenActivity.this, FleetUserDashBoard.class);
					i.putExtra("menumodel", menumodels);
					startActivity(i);
					finishAffinity();
				}else if(infoPref.getString("user_type_id", PrefEnum.Login).equalsIgnoreCase("7")){
					Intent i = new Intent(SplashscreenActivity.this, CustomerDashboardActivity.class);
					i.putExtra("menumodel", menumodels);
					startActivity(i);
					finishAffinity();
				}
			}else if (menumodels !=null && menumodels.size()==0){
				startActivity(new Intent(this, LoginActivity.class));
				finishAffinity();
			}
			else if (infoPref.getString("user_type_id", PrefEnum.Login).equalsIgnoreCase("")) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						startActivity(new Intent(SplashscreenActivity.this, LoginActivity.class));
						finishAffinity();
					}
				},1000);

			}
		}
		else if(dataLogIn.equalsIgnoreCase("I")){
			clearVehicleId();
		}

		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public void fncheckIfLogin(String deviceid,String playerId,String token, final String usertypeid,final String userid) {
		Call<LoginCheckResponse> call= RepositryInstance.getLoginRepository().checkIfLogin(deviceid,playerId,token,usertypeid,userid);
			call.enqueue(new Callback<LoginCheckResponse>() {
				@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
				@Override
				public void onResponse(Call<LoginCheckResponse> call, Response<LoginCheckResponse> response) {
					if (response.isSuccessful()){
						if (response.body().getStatus().trim().equals("success")) {
							dataLogIn = response.body().getData();
							infoPref.putString("loginstatus",dataLogIn,PrefEnum.Login);
						}else if(response.body().getStatus().trim().equalsIgnoreCase("failure")){
							Utils.remove_shareprefernce(infoPref.getLoginPref());
						}
						loginMethod();
					}
				}

				@Override
				public void onFailure(Call<LoginCheckResponse> call, Throwable t) {
					Log.d(TAG, "onFailure: ");
					new AlertDialog.Builder(SplashscreenActivity.this)
											.setMessage("Please Check Your Internet Connectivity and Retry")
											.setCancelable(false)
											.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialogInterface, int i) {
													fncheckIfLogin(infoPref.getString("record_id", PrefEnum.OneSignal),infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal), SplashscreenActivity.this.token,id,user_type_id);
													loginMethod();
												}

											})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									dialogInterface.dismiss();
									finishAffinity();
								}
							}).create()
							.show();
				}
			});
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
		Permissions.notifyIfNotGranted(this);
	}

	public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
		if (!stateChanges.getFrom().getSubscribed() && stateChanges.getTo().getSubscribed()) {
			stateChanges.getTo().getUserId();
			infoPref.putString("token",stateChanges.getTo().getPushToken(),PrefEnum.OneSignal);
			infoPref.putString("GT_PLAYER_ID",stateChanges.getTo().getUserId(),PrefEnum.OneSignal);
			playerId =infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
		}
	}

	public void clearVehicleId(){
		infoPref.remove("vid",PrefEnum.Login);
		infoPref.remove("livetrack",PrefEnum.Login);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
				finishAffinity();
			}
		},2000);
	}


}
