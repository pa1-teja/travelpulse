package com.trimax.vts.view.notifications.send;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alert {
    @SerializedName("notification_id")
    @Expose
    private int notificationId;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type_id")
    @Expose
    private String userTypeId;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
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
    @SerializedName("vehicle_lat")
    @Expose
    private String vehicleLat;
    @SerializedName("vehicle_long")
    @Expose
    private String vehicleLong;
    @SerializedName("severity")
    @Expose
    private String severity;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("vehicle_type_id")
    @Expose
    private String vehicleTypeId;
    @SerializedName("ign")
    @Expose
    private String ign;
    @SerializedName("ac")
    @Expose
    private String ac;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("show_alarm")
    @Expose
    private String showAlarm;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
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

    public String getShowAlarm() {
        return showAlarm;
    }

    public void setShowAlarm(String showAlarm) {
        this.showAlarm = showAlarm;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
