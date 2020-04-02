package com.trimax.vts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current_Status {

    @SerializedName("1")
    @Expose
    private String _1;
    @SerializedName("vid")
    @Expose
    private String vid;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("vehicle_status")
    @Expose
    private String vehicleStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("status_updated_human")
    @Expose
    private String statuUpdatedHuman;
    @SerializedName("status_updated")
    @Expose
    private String statusUpdated;

    public String get1() {
        return _1;
    }

    public void set1(String _1) {
        this._1 = _1;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getStatuUpdatedHuman() {
        return statuUpdatedHuman;
    }

    public void setStatuUpdatedHuman(String statuUpdatedHuman) {
        this.statuUpdatedHuman = statuUpdatedHuman;
    }

    public String getStatusUpdated() {
        return statusUpdated;
    }

    public void setStatusUpdated(String statusUpdated) {
        this.statusUpdated = statusUpdated;
    }
}
