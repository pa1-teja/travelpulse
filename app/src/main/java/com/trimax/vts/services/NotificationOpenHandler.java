package com.trimax.vts.services;

import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.trimax.vts.ApplicationM;
import com.trimax.vts.view.notifications.NotificationActivity;

import org.json.JSONObject;

public class NotificationOpenHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;
        if (result.notification.payload.additionalData.has("ntype")){

            }
       else if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken || actionType == OSNotificationAction.ActionType.Opened) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            Intent intent = new Intent(ApplicationM.getContext(), NotificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationM.getContext().startActivity(intent);
        }
    }
}

