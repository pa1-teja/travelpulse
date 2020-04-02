package com.trimax.vts.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.trimax.vts.api.ApiClient;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.notifications.send.Alert;
import com.trimax.vts.view.notifications.send.Contents;
import com.trimax.vts.view.notifications.send.NotificationData;
import com.trimax.vts.view.notifications.send.SendNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotificationService extends JobService {
    private static final String TAG = "SendNotificationService";
    private TravelpulseInfoPref infoPref;
    private Random random;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        random = new Random();
        infoPref = new TravelpulseInfoPref(this);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ");
        sendNotification();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void sendNotification() {
        final int min = 10;
        final int max = 99999;
        String playerId = infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
        List<String> array = new ArrayList<>();
        array.add(playerId);

        Alert notification = new Alert();
        notification.setNotificationId(1/*(random.nextInt((max - min) + 1)+min)*/);
        notification.setCustomerId(infoPref.getString("id",PrefEnum.Login));
        notification.setUserId(infoPref.getString("id",PrefEnum.Login));
        notification.setUserTypeId(infoPref.getString("user_type_id",PrefEnum.Login));
        notification.setVehicleId(infoPref.getString("vid",PrefEnum.Login));
        notification.setDriverId("");
        notification.setImei("");
        notification.setNotificationType("");
        notification.setNotificationSubtype("");
        notification.setMsg("Syncing notifications "/*+(random.nextInt((max - min) + 1)+min)*/);
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
        content.setEn("Syncing notifications");

        NotificationData alert = new NotificationData();
        alert.setAlert(notification);

        SendNotification data = new SendNotification();
        data.setAppId("1e4f9b8d-5226-4e44-b2ea-690b9a17ebb6");
        data.setIncludePlayerIds(array);
        data.setContents(content);
        data.setData(alert);


        Log.d(TAG, "sendNotification: "+data);
        Call<ResponseBody> call = ApiClient.getApiClientOfOneSignal().sendNotification(data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: "+response);

                Log.d(TAG, "onResponse: "+ CommonClass.bodyToString(call.request().body()));
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }
}
