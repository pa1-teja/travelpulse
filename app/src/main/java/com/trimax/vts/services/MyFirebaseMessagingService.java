package com.trimax.vts.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.trimax.vts.database.AppDatabase;
import com.trimax.vts.view.R;
import com.trimax.vts.view.notifications.ActivityAlertParkedOut;
import com.trimax.vts.view.notifications.NotificationActivity;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        createWakeLock();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getFrom());
        acquireWakeLock();
        /*if (remoteMessage.getData().size()>0){
            Log.d(TAG, "onMessageReceived: "+remoteMessage.getData().get("alert"));
            com.trimax.vts.database.entity.Notification notification = new Gson().fromJson(remoteMessage.getData().get("alert"), com.trimax.vts.database.entity.Notification.class);
            Log.d(TAG, "onMessageReceived: "+notification);
            String currentTime = String.valueOf(System.currentTimeMillis());
            List<com.trimax.vts.database.entity.Notification> duplicateNotifications = AppDatabase.getDbInstance(this).notificationDao().getNotifications(notification.getNotificationId());
            if (duplicateNotifications == null || duplicateNotifications.size()==0) {
                notification.setReceivedAt(currentTime);
                long value = AppDatabase.getDbInstance(this).notificationDao().insertNotification(notification);
                createNotification(notification.getTitle(), notification.getMsg());

                if (notification.getNotificationType().equalsIgnoreCase("parked_out")) {
                    if (notification.getShowAlarm().equalsIgnoreCase("Y")) {
                        Intent ii = new Intent(getApplicationContext(), ActivityAlertParkedOut.class);
                        ii.putExtra("msg", notification.getMsg());
                        ii.putExtra("notification_type", notification.getNotificationType());
                        ii.putExtra("date_time", notification.getDateTime());
                        ii.putExtra("location", notification.getLocation());
                        ii.putExtra("notification_subtype", notification.getNotificationSubtype());
                        ii.putExtra("vehicle_id", notification.getVehicleId());
                        ii.putExtra("speed", notification.getSpeed());
                        ii.putExtra("vehicle_type_id", notification.getVehicleTypeId());

                        ii.putExtra("vehicle_lat", notification.getVehicleLat());
                        ii.putExtra("vehicle_long", notification.getVehicleLng());
                        ii.putExtra("ign", notification.getIgn());
                        ii.putExtra("ac", notification.getAc());
                        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(ii);
                    }
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            createNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }*/
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "onNewToken: "+token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token){
        
    }

    private void createNotification(String title, String message) {
        String CHANNEL_ID="Travelpulse";

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.tpicon)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setGroup("Travelpulse")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);
        notificationManager.notify(1, notificationBuilder.build());
        releaseWakeLock();
    }

    private synchronized void createWakeLock() {
        // Create a new wake lock if we haven't made one yet.
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FCM:Notification");
            mWakeLock.setReferenceCounted(false);
        }
    }

    private void acquireWakeLock() {
        // It's okay to double-acquire this because we are not using it
        // in reference-counted mode.
        mWakeLock.acquire();
    }

    private void releaseWakeLock() {
        // Don't release the wake lock if it hasn't been created and acquired.
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceTask = new Intent(getApplicationContext(),MyFirebaseMessagingService.class);
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
}
