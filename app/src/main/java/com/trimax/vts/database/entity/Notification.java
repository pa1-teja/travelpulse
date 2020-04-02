package com.trimax.vts.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "notification")
public class Notification {

    @SerializedName("id")
    @Expose
    @Ignore
    private String id;

    @SerializedName("title")
    @Expose
    @Ignore
    private String title;

    @SerializedName("notification_id")
    @Expose
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String notificationId;

    @SerializedName("notification_type")
    @Expose
    private String notificationType;

    @SerializedName("notification_type_id")
    @Expose
    private String notificationTypeId;

    @SerializedName("notification_subtype")
    @Expose
    private String notificationSubtype;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("show_alarm")
    @Expose
    private String showAlarm;

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;

    @SerializedName("vehicle_lat")
    @Expose
    private String vehicleLat;

    @SerializedName("vehicle_long")
    @Expose
    private String vehicleLng;

    @SerializedName("ign")
    @Expose
    private String ign;

    @SerializedName("ac")
    @Expose
    private String ac;

    @SerializedName("speed")
    @Expose
    private String speed;

    @SerializedName("driver_id")
    @Expose
    private String driverId;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    private String receivedAt;

    @SerializedName("vehicle_type_id")
    @Expose
    private String vehicleTypeId;

    @SerializedName("group_id")
    @Expose
    private String groupId;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public Notification(@NonNull String notificationId, String notificationType, String notificationTypeId, String notificationSubtype, String msg, String showAlarm, String customerId, String vehicleId, String vehicleLat, String vehicleLng, String ign, String ac, String speed, String driverId, String createdAt, String receivedAt, String vehicleTypeId, String groupId, String location, String dateTime) {
        this.notificationId = notificationId;
        this.notificationType = notificationType;
        this.notificationTypeId = notificationTypeId;
        this.notificationSubtype = notificationSubtype;
        this.msg = msg;
        this.showAlarm = showAlarm;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.vehicleLat = vehicleLat;
        this.vehicleLng = vehicleLng;
        this.ign = ign;
        this.ac = ac;
        this.speed = speed;
        this.driverId = driverId;
        this.createdAt = createdAt;
        this.receivedAt = receivedAt;
        this.vehicleTypeId = vehicleTypeId;
        this.groupId = groupId;
        this.location = location;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
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

    public String getVehicleLng() {
        return vehicleLng;
    }

    public void setVehicleLng(String vehicleLng) {
        this.vehicleLng = vehicleLng;
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

    public String getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(String notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getShowAlarm() {
        return showAlarm;
    }

    public void setShowAlarm(String showAlarm) {
        this.showAlarm = showAlarm;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
