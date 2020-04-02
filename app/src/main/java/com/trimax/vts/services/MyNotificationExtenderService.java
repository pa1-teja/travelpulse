package com.trimax.vts.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.PowerManager;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;
import com.onesignal.OneSignal;
import com.trimax.vts.database.AppDatabase;
import com.trimax.vts.database.entity.Notification;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.complaints.models.DataSaveResponse;
import com.trimax.vts.view.notifications.ActivityAlertParkedOut;
import com.trimax.vts.ApplicationM;
import com.trimax.vts.view.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNotificationExtenderService extends NotificationExtenderService {
    private static final String TAG = "MyNotificationExtenderS";
    private Location mLastKnownLocation;
    public String additionalDataa;
    JSONObject js  ;
    private TravelpulseInfoPref infoPref;

    @Override
    public void onCreate() {
        super.onCreate();
        infoPref = new TravelpulseInfoPref(this);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected boolean onNotificationProcessing(final OSNotificationReceivedResult receivedResult) {

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        boolean isScreenOn = pm.isInteractive(); // check if screen is on
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE, "Travelpulse:notificationLock");
            wl.acquire(3000); //set your time in milliseconds
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"Travelpulse:MyCpuLock");
            wl_cpu.acquire(3000);
        }

       try {
           if (receivedResult.payload.additionalData.has("ntype")) {
               receivedResult.payload.additionalData.getString("ntype");
               OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.None);
               OSNotificationDisplayedResult displayedResult = displayNotification(null);
               return false;
           }else {
               additionalDataa = receivedResult.payload.additionalData.getString("alert");//
               js = new JSONObject(additionalDataa);
               Log.d(TAG, "onNotificationProcessing: "+js);
               Notification notification = new Gson().fromJson(js.toString(), Notification.class);

               //set notification log
               //setNoficationLog(notification.getNotificationId(),infoPref.getString("record_id", PrefEnum.OneSignal));

               String currentTime = String.valueOf(System.currentTimeMillis());
               List<Notification> duplicateNotifications = AppDatabase.getDbInstance(this).notificationDao().getNotifications(notification.getNotificationId());
               if (duplicateNotifications == null || duplicateNotifications.size()==0) {
                   notification.setReceivedAt(currentTime);
                   long value = AppDatabase.getDbInstance(this).notificationDao().insertNotification(notification);
                   OverrideSettings overrideSettings = new OverrideSettings();
               overrideSettings.extender = new NotificationCompat.Extender() {

                   @Override
                   public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                       // Sets the background notification color to Red on Android 5.0+ devices.
                       Bitmap icon = BitmapFactory.decodeResource(ApplicationM.getContext().getResources(), R.drawable.tpicon);
                       builder.setLargeIcon(icon);

                       try {
                           String n_type = js.getString("notification_type");

                           String n_sub_type = js.getString("notification_subtype");
                           String alrm_type = js.getString("show_alarm");

                           if (n_type.equalsIgnoreCase("dangerous_speed") && n_sub_type.equalsIgnoreCase("DANGEROUS SPEED")) {
                               if (alrm_type.equalsIgnoreCase("Y")) {
                                   Intent ii = new Intent(getApplicationContext(), ActivityAlertParkedOut.class);
                                   ii.putExtra("msg", js.getString("msg"));
                                   ii.putExtra("notification_type", js.getString("notification_type"));
                                   ii.putExtra("date_time", js.getString("date_time"));
                                   ii.putExtra("location", js.getString("location"));
                                   ii.putExtra("notification_subtype", js.getString("notification_subtype"));
                                   ii.putExtra("vehicle_id", js.getString("vehicle_id"));
                                   ii.putExtra("vehicle_type_id", js.getString("vehicle_type_id"));
                                   ii.putExtra("speed", js.getString("speed"));
                                   ii.putExtra("vehicle_lat", js.getString("vehicle_lat"));
                                   ii.putExtra("vehicle_long", js.getString("vehicle_long"));
                                   ii.putExtra("ign", js.getString("ign"));
                                   ii.putExtra("ac", js.getString("ac"));
                                   ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   startActivity(ii);
                               }
                           }
                           if (n_type.equalsIgnoreCase("parked_out")) {
                               if (alrm_type.equalsIgnoreCase("Y")) {
                                   Intent ii = new Intent(getApplicationContext(), ActivityAlertParkedOut.class);
                                   ii.putExtra("msg", js.getString("msg"));
                                   ii.putExtra("notification_type", js.getString("notification_type"));
                                   ii.putExtra("date_time", js.getString("date_time"));
                                   ii.putExtra("location", js.getString("location"));
                                   ii.putExtra("notification_subtype", js.getString("notification_subtype"));
                                   ii.putExtra("vehicle_id", js.getString("vehicle_id"));
                                   ii.putExtra("speed", js.getString("speed"));
                                   ii.putExtra("vehicle_type_id", js.getString("vehicle_type_id"));

                                   ii.putExtra("vehicle_lat", js.getString("vehicle_lat"));
                                   ii.putExtra("vehicle_long", js.getString("vehicle_long"));
                                   ii.putExtra("ign", js.getString("ign"));
                                   ii.putExtra("ac", js.getString("ac"));
                                   ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   startActivity(ii);
                               }
                           }

                           if (n_type.equalsIgnoreCase("ac_idle_repeat")) {
                               if (alrm_type.equalsIgnoreCase("Y")) {
                                   Intent ii = new Intent(getApplicationContext(), ActivityAlertParkedOut.class);
                                   ii.putExtra("msg", js.getString("msg"));
                                   ii.putExtra("notification_type", js.getString("notification_type"));
                                   ii.putExtra("date_time", js.getString("date_time"));
                                   ii.putExtra("location", js.getString("location"));
                                   ii.putExtra("notification_subtype", js.getString("notification_subtype"));
                                   ii.putExtra("vehicle_id", js.getString("vehicle_id"));
                                   ii.putExtra("vehicle_type_id", js.getString("vehicle_type_id"));

                                   ii.putExtra("speed", js.getString("speed"));
                                   ii.putExtra("vehicle_lat", js.getString("vehicle_lat"));
                                   ii.putExtra("vehicle_long", js.getString("vehicle_long"));
                                   ii.putExtra("ign", js.getString("ign"));
                                   ii.putExtra("ac", js.getString("ac"));
                                   ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   startActivity(ii);
                               }
                           }

                           if (n_type.equalsIgnoreCase("towing")) {
                               if (alrm_type.equalsIgnoreCase("Y")) {
                                   Intent ii = new Intent(getApplicationContext(), ActivityAlertParkedOut.class);
                                   ii.putExtra("msg", js.getString("msg"));
                                   ii.putExtra("notification_type", js.getString("notification_type"));
                                   ii.putExtra("date_time", js.getString("date_time"));
                                   ii.putExtra("location", js.getString("location"));
                                   ii.putExtra("notification_subtype", js.getString("notification_subtype"));
                                   ii.putExtra("vehicle_id", js.getString("vehicle_id"));
                                   ii.putExtra("vehicle_type_id", js.getString("vehicle_type_id"));
                                   ii.putExtra("speed", js.getString("speed"));
                                   ii.putExtra("vehicle_lat", js.getString("vehicle_lat"));
                                   ii.putExtra("vehicle_long", js.getString("vehicle_long"));
                                   ii.putExtra("ign", js.getString("ign"));
                                   ii.putExtra("ac", js.getString("ac"));
                                   ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   startActivity(ii);
                               }
                           }
                       } catch (JSONException ex) {
                           ex.printStackTrace();
                       }

                       return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
                   }
            };
                   OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
                   Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);
        }
        return true;
           }
       }
       catch (JSONException e){
           e.printStackTrace();
           return false;
       }
    }

    private void setNoficationLog(String notificationId, String deviceId){
        Calendar cal = Calendar.getInstance();
        String dateTime = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        Call<DataSaveResponse> call = RepositryInstance.getNotificationRepository().setNotificationLog(notificationId,deviceId,dateTime);
        call.enqueue(new Callback<DataSaveResponse>() {
            @Override
            public void onResponse(Call<DataSaveResponse> call, Response<DataSaveResponse> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<DataSaveResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceTask = new Intent(getApplicationContext(),MyNotificationExtenderService.class);
        restartServiceTask.setPackage(getPackageName());
        startService(restartServiceTask);
        super.onTaskRemoved(rootIntent);
    }
}
