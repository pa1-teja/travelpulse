package com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationPrefModel {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("notification_type_id")
    @Expose
    private String notificationTypeId;
    @SerializedName("is_on")
    @Expose
    private String isOn;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("alarm_applicable")
    @Expose
    private String alarmApplicable;
    @SerializedName("alarm_on")
    @Expose
    private String alarmOn;

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

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getAlarmApplicable() {
        return alarmApplicable;
    }

    public void setAlarmApplicable(String alarmApplicable) {
        this.alarmApplicable = alarmApplicable;
    }

    public String getAlarmOn() {
        return alarmOn;
    }

    public void setAlarmOn(String alarmOn) {
        this.alarmOn = alarmOn;
    }

}