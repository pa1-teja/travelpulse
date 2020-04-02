package com.trimax.vts.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.about.SplashscreenActivity;

public class StartAppService extends IntentService {
    private static final String TAG = "StartAppService";
    public StartAppService() {
        super(TAG);
        Log.d(TAG, "StartAppService: ");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TravelpulseInfoPref infoPref = new TravelpulseInfoPref(this);
        Log.d(TAG, "onHandleIntent: "+infoPref.getBoolean(infoPref.APP_ALARM, PrefEnum.Login));
        if (infoPref.getBoolean(infoPref.APP_ALARM,PrefEnum.Login))
            startActivity(new Intent(this, SplashscreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        Log.d(TAG, "onHandleIntent: ");
    }
}
