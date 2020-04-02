package com.trimax.vts.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.trimax.vts.services.StartAppService;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.BuildConfig;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = BuildConfig.APPLICATION_ID;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: alarm");
        //Send notification from device
        Utils.scheduleJob(context);
        Intent i = new Intent(context, StartAppService.class);
        context.startService(i);
    }
}
