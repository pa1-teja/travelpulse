package com.trimax.vts.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.receiver.ScreenOnOffReceiver;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.R;
import com.trimax.vts.view.about.SplashscreenActivity;
import com.trimax.vts.view.notifications.send.Alert;
import com.trimax.vts.view.notifications.send.Contents;
import com.trimax.vts.view.notifications.send.NotificationData;
import com.trimax.vts.view.notifications.send.SendNotification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class BackgroundNotificationService extends Service {
    private static final String TAG = "BackgroundNotificationS";
    private Location mLastKnownLocation;
    private ScreenOnOffReceiver screenOnOffReceiver = null;
    private TravelpulseInfoPref infoPref;
    private Calendar cal;
    private String deviceiid="", playerId ="",token="";
    private int i=0;
    Random random;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        random = new Random();
        infoPref = new TravelpulseInfoPref(this);
        cal = Calendar.getInstance();
        if (!infoPref.isKeyContains("deviceid",PrefEnum.OneSignal)) {
            deviceiid = CommonClass.getDevice_Id(this);
            infoPref.putString("deviceid", deviceiid, PrefEnum.OneSignal);
        }else {
            deviceiid = infoPref.getString("deviceid",PrefEnum.OneSignal);
        }
        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.setPriority(100);
        screenOnOffReceiver = new ScreenOnOffReceiver();
        registerReceiver(screenOnOffReceiver, intentFilter);


        Log.d(TAG, "Service onCreate: screenOnOffReceiver is registered.");
    }

    private void getToken() {
        Log.d(TAG, "getToken: Service  ");
        playerId =infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
        token=infoPref.getString("token", PrefEnum.OneSignal);
		infoPref.putString("deviceid",deviceiid,PrefEnum.OneSignal);
		if (TextUtils.isEmpty(playerId) || TextUtils.isEmpty(token)) {
           Utils.getPlayerId(this,infoPref);
        }
        if (TextUtils.isEmpty(infoPref.getString("record_id", PrefEnum.OneSignal))) {
            infoPref.putString("GT_PLAYER_ID", playerId, PrefEnum.OneSignal);
            infoPref.putString("token", token, PrefEnum.OneSignal);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.getRecordId(deviceiid, "IMEI", token, playerId, infoPref);
                }
            }, 400);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService(new Intent(this,MyFirebaseMessagingService.class));
        Log.d(TAG, "onStartCommand: ");
        if (TextUtils.isEmpty(infoPref.getString("GT_PLAYER_ID",PrefEnum.OneSignal))){
            getToken();
        }
        if (TextUtils.isEmpty(infoPref.getString("record_id", PrefEnum.OneSignal))){
            Utils.getRecordId(infoPref.getString("deviceid",PrefEnum.OneSignal), "IMEI", infoPref.getString("token",PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID",PrefEnum.OneSignal),infoPref);
        }
        if (!infoPref.getBoolean("isDeviceAdded",PrefEnum.Login) /*&& infoPref.getString("loginstatus",PrefEnum.Login).equalsIgnoreCase("I")*/){
            Utils.fndeviceadd(infoPref.getString("record_id", PrefEnum.OneSignal),infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),infoPref.getString("token", PrefEnum.OneSignal), infoPref.getString("user_type_id", PrefEnum.Login), infoPref.getString("customer_id", PrefEnum.Login), "A",infoPref);
        }

        //Utils.getDeviceLocation(this);
        startForeground();
        Log.d(TAG, "onStartCommand: ");
        //Repeating task
        /*Timer timer = new Timer();
        //Set the schedule function
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //callToServer();
                    sendNotification();
            }
            }, 0, 60*1000);*/
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForeground() {
        Intent resultIntent = new Intent(this, SplashscreenActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = getPackageName()+"_";
            String channelName = "Travelpulse";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.RED);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            notificationBuilder.setContentIntent(resultPendingIntent);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("Travelpulse")
                    .setContentText("App is running in background")
                    .setPriority(IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();

            startForeground(2, notification);
        }
        else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Travelpulse")
                    .setContentText("App is running in background")
                    .setPriority(IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2,notification);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceTask = new Intent(getApplicationContext(),BackgroundNotificationService.class);
        restartServiceTask.setPackage(getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(restartServiceTask);
            Log.d(TAG, "onTaskRemoved: ");
        }else {
            startService(restartServiceTask);
            Log.d(TAG, "onTaskRemoved: ");
        }
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenOnOffReceiver);
    }


    private void callToServer(){
        Call<ResponseBody> call = ApiClient.getApiClient().callToServer("http://test.roadpulse.net/");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: "+(++i));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void sendNotification() {
        final int min = 10;
        final int max = 99999;
        String playerId = infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
        List<String> array = new ArrayList<>();
        array.add(playerId);

        Alert notification = new Alert();
        notification.setNotificationId((random.nextInt((max - min) + 1)+min));
        notification.setCustomerId(infoPref.getString("id",PrefEnum.Login));
        notification.setUserId(infoPref.getString("id",PrefEnum.Login));
        notification.setUserTypeId(infoPref.getString("user_type_id",PrefEnum.Login));
        notification.setVehicleId(infoPref.getString("vid",PrefEnum.Login));
        notification.setDriverId("");
        notification.setImei("");
        notification.setNotificationType("");
        notification.setNotificationSubtype("");
        notification.setMsg("Dummy notification");
        notification.setVehicleLat("");
        notification.setVehicleLong("");
        notification.setSeverity("");
        notification.setLocation("");
        notification.setVehicleTypeId("");
        notification.setIgn("0");
        notification.setAc("0");
        notification.setSpeed("0");
        notification.setShowAlarm("N");
        notification.setDateTime("");
        notification.setCreatedAt("");

        Contents content = new Contents();
        content.setEn("Dummy test messgae");

        NotificationData alert = new NotificationData();
        alert.setAlert(notification);

        SendNotification data = new SendNotification();
        data.setAppId("1e4f9b8d-5226-4e44-b2ea-690b9a17ebb6");
        data.setIncludePlayerIds(array);
        data.setContents(content);
        data.setData(alert);


        Log.d(TAG, "sendNotification: "+data);
        Call<ResponseBody> call = ApiClient.getApiClientOfOneSignal().sendNotification(/*"application/json; charset=UTF-8",*/ data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: "+response);

                Log.d(TAG, "onResponse: "+CommonClass.bodyToString(call.request().body()));
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

        //Log.d(TAG, "sendNotification:2222 "+json);
    }
}
