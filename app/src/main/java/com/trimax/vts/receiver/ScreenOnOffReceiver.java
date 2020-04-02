package com.trimax.vts.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.trimax.vts.services.BackgroundNotificationService;
import com.trimax.vts.services.MyFirebaseMessagingService;
import com.trimax.vts.services.MyNotificationExtenderService;

public class ScreenOnOffReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenOnOffReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        context.startService(new Intent(context, MyFirebaseMessagingService.class));
        if(Intent.ACTION_SCREEN_OFF.equals(action)) {
            Log.d(TAG, "Screen is turn off.");
        }else if(Intent.ACTION_SCREEN_ON.equals(action)) {
            Log.d(TAG, "Screen is turn on.");
            //context.startService(new Intent(context,MyNotificationExtenderService.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, BackgroundNotificationService.class));
                //context.startForegroundService(new Intent(context, MyNotificationExtenderService.class));
            }else {
                context.startService(new Intent(context,BackgroundNotificationService.class));
            }
        }
    }
}
