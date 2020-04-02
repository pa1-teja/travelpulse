package com.trimax.vts.view.reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertReportModel {
    @SerializedName("ntime")
    @Expose
    private String dateTime;
    @SerializedName("notification_type_id")
    @Expose
    private String notificationTypeId;
    @SerializedName("notification_type")
    @Expose
    private String notificationType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("vehicle_lat")
    @Expose
    private String vehicleLat;
    @SerializedName("vehicle_long")
    @Expose
    private String vehicleLong;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(String notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getVehicleLat() {
        return vehicleLat;
    }

    public void setVehicleLat(String vehicleLat) {
        this.vehicleLat = vehicleLat;
    }

    public String getVehicleLong() {
        return vehicleLong;
    }

    public void setVehicleLong(String vehicleLong) {
        this.vehicleLong = vehicleLong;
    }
}
