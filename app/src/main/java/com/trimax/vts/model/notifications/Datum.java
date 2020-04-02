package com.trimax.vts.model.notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("notification_type")
    @Expose
    private String notificationType;
    @SerializedName("notification_subtype")
    @Expose
    private String notificationSubtype;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("vehicle_long")
    @Expose
    private String vehicleLong;

    @SerializedName("vehicle_lat")
    @Expose
    private String vehicle_lat;

    @SerializedName("date_time")
    @Expose
    private String dateTime;

    @SerializedName("daysago")
    @Expose
    private String daysago;

    @SerializedName("vehicle_id")
    @Expose

    private String vehicle_id;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("ign")
    @Expose
    private String ign;
    @SerializedName("ac")
    @Expose
    private String ac;


    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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

    public String getVehicle_lat() {
        return vehicle_lat;
    }

    public void setVehicle_lat(String vehicle_lat) {
        this.vehicle_lat = vehicle_lat;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }


    public String getDaysago() {
        return daysago;
    }

    public void setDaysago(String daysago) {
        this.daysago = daysago;
    }
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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

    public String getVehicleLong() {
        return vehicleLong;
    }

    public void setVehicleLong(String vehicleLong) {
        this.vehicleLong = vehicleLong;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
