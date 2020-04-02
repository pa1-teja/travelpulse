package com.trimax.vts.model;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelVehiclePerformance {


    private String Notification_name;

    private String Notification_count;

    private Drawable progressDrawble;


    public ModelVehiclePerformance(String notification_name, String notification_count, Drawable progressDrawble) {
        Notification_name = notification_name;
        Notification_count = notification_count;
        this.progressDrawble = progressDrawble;
    }

    public String getNotification_name() {
        return Notification_name;
    }

    public void setNotification_name(String notification_name) {
        Notification_name = notification_name;
    }

    public String getNotification_count() {
        return Notification_count;
    }

    public void setNotification_count(String notification_count) {
        Notification_count = notification_count;
    }

    public Drawable getProgressDrawble() {
        return progressDrawble;
    }

    public void setProgressDrawble(Drawable progressDrawble) {
        this.progressDrawble = progressDrawble;
    }
}






