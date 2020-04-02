package com.trimax.vts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveTrakingResponseNew {

    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("ign")
    @Expose
    private String ign;
    @SerializedName("ac")
    @Expose
    private String ac;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("tamper")
    @Expose
    private String tamper;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("tracker_datetime")
    @Expose
    private String trackerDatetime;
    @SerializedName("formatted_date_time")
    @Expose
    private String formattedDateTime;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("driver_name")
    @Expose
    private String driverName;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
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

    public String getTamper() {
        return tamper;
    }

    public void setTamper(String tamper) {
        this.tamper = tamper;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public String getTrackerDatetime() {
        return trackerDatetime;
    }

    public void setTrackerDatetime(String trackerDatetime) {
        this.trackerDatetime = trackerDatetime;
    }

    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public void setFormattedDateTime(String formattedDateTime) {
        this.formattedDateTime = formattedDateTime;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

}
