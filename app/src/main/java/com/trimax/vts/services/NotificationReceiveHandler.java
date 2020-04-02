package com.trimax.vts.services;

import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class NotificationReceiveHandler implements OneSignal.NotificationReceivedHandler {
    public static final int DAILY_REMINDER_REQUEST_CODE=100;
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String customKey;

            if (data != null) {
                customKey = data.optString("alert", null);
                if (customKey != null)
                    Log.e("OneSignalExample", "customkey set with value: " + customKey);
            }
        }

    }

