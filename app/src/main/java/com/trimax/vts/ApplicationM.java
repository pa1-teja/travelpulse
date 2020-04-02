package com.trimax.vts;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.database.AppDatabase;
import com.trimax.vts.database.DatabaseClass;
import com.trimax.vts.services.NotificationReceiveHandler;
import com.trimax.vts.services.BackgroundNotificationService;
import com.trimax.vts.services.NotificationOpenHandler;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class ApplicationM extends MultiDexApplication {
    private static final String TAG = "ApplicationM";

    private static Context context;
    String deviceiid="";
    private static TravelpulseInfoPref infoPref;
    private static String advertId="";
    //private RefWatcher refWatcher;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        DatabaseClass database = new DatabaseClass(this);
        database.copyDb(this);
        context = getApplicationContext();
        infoPref = new TravelpulseInfoPref(this);
        MultiDex.install(this);
        Fabric.with(context, new Crashlytics());
        Utils.getPlayerId(this,infoPref);
        task.execute();
        Log.d(TAG, "onCreate: "+ FirebaseInstanceId.getInstance().getToken());
        Log.d(TAG, "onCreate: "+ FirebaseInstanceId.getInstance().getId());
        if (!infoPref.isKeyContains("deviceid",PrefEnum.OneSignal)) {
            deviceiid = CommonClass.getDevice_Id(this);
            infoPref.putString("deviceid", deviceiid, PrefEnum.OneSignal);
        }else {
            deviceiid = infoPref.getString("deviceid",PrefEnum.OneSignal);
        }

        if( !TextUtils.isEmpty(infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal)) && TextUtils.isEmpty(infoPref.getString("record_id",PrefEnum.OneSignal))){
            Utils.getRecordId(infoPref.getString("deviceid", PrefEnum.OneSignal), "IMEI", infoPref.getString("token", PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),infoPref);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), BackgroundNotificationService.class));
        } else {
            try {
                startService(new Intent(getApplicationContext(),BackgroundNotificationService.class));
            } catch (Exception e) {
                Log.e("=====Exception=====", "=====Exception====="+e.getMessage());
            }
        }
    }



    /*public static RefWatcher getRefWatcher(Context context) {
        ApplicationM application = (ApplicationM) context.getApplicationContext();
        return application.refWatcher;
    }*/

    public static String getAdvertId() {
        return advertId;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

        @Override
        protected String doInBackground(Void... params) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String advertId = null;
            try{
                advertId = idInfo.getId();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            return advertId;
        }

        @Override
        protected void onPostExecute(String advertiseId) {
            advertId =advertiseId;
            Log.d(TAG, "onPostExecute: "+advertId);
            //Toast.makeText(getApplicationContext(), advertId, Toast.LENGTH_SHORT).show();
        }

    };

}
