package com.trimax.vts.view.model.replay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("notification_type")
    @Expose
    private String notificationType;
    @SerializedName("notification_subtype")
    @Expose
    private String notificationSubtype;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("vehicle_lat")
    @Expose
    private String vehicleLat;
    @SerializedName("vehicle_long")
    @Expose
    private String vehicleLong;
    @SerializedName("ign")
    @Expose
    private String ign;
    @SerializedName("ac")
    @Expose
    private String ac;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationSubtype() {
        return notificationSubtype;
    }

    public void setNotificationSubtype(String notificationSubtype) {
        this.notificationSubtype = notificationSubtype;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getIgn() {
        return ign;
    }

    public void setIgn(String ign) {
        this.ign = ign;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
