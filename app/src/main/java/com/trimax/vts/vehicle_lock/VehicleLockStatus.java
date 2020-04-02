package com.trimax.vts.vehicle_lock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleLockStatus {
    @SerializedName("ctime")
    @Expose
    private String ctime;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("vehicle_type_id")
    @Expose
    private String vehicleTypeId;
    @SerializedName("tracker_imei_no")
    @Expose
    private String trackerImeiNo;
    @SerializedName("tracker_sim_no")
    @Expose
    private String trackerSimNo;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("set_no")
    @Expose
    private Object setNo;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("tpin")
    @Expose
    private String tpin;
    @SerializedName("protocol")
    @Expose
    private String protocol;
    @SerializedName("protocol_version")
    @Expose
    private Object protocolVersion;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("gps")
    @Expose
    private String gps;
    @SerializedName("ign")
    @Expose
    private String ign;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("tamper")
    @Expose
    private String tamper;
    @SerializedName("tracker_datetime")
    @Expose
    private String trackerDatetime;
    @SerializedName("remote_immobilize")
    @Expose
    private String remoteImmobilize;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("vehicle_status")
    @Expose
    private String vehicleStatus;
    @SerializedName("button_type")
    @Expose
    private String buttonType;
    @SerializedName("show_button")
    @Expose
    private String showButton;
    @SerializedName("request_type_lable")
    @Expose
    private String requestTypeLabel;

    @SerializedName("request_type")
    @Expose
    private String requestType;

    @SerializedName("request_status")
    @Expose
    private String requestStatus;
    @SerializedName("request_time")
    @Expose
    private String requestTime;
    @SerializedName("updated_timestamp")
    @Expose
    private String updatedTimestamp;
    @SerializedName("request_msg")
    @Expose
    private String requestMsg;

    public String getRequestTypeLabel() {
        return requestTypeLabel;
    }

    public void setRequestTypeLabel(String requestTypeLabel) {
        this.requestTypeLabel = requestTypeLabel;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getTrackerImeiNo() {
        return trackerImeiNo;
    }

    public void setTrackerImeiNo(String trackerImeiNo) {
        this.trackerImeiNo = trackerImeiNo;
    }

    public String getTrackerSimNo() {
        return trackerSimNo;
    }

    public void setTrackerSimNo(String trackerSimNo) {
        this.trackerSimNo = trackerSimNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Object getSetNo() {
        return setNo;
    }

    public void setSetNo(Object setNo) {
        this.setNo = setNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Object getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(Object protocolVersion) {
        this.protocolVersion = protocolVersion;
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

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getIgn() {
        return ign;
    }

    public void setIgn(String ign) {
        this.ign = ign;
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

    public String getTrackerDatetime() {
        return trackerDatetime;
    }

    public void setTrackerDatetime(String trackerDatetime) {
        this.trackerDatetime = trackerDatetime;
    }

    public String getRemoteImmobilize() {
        return remoteImmobilize;
    }

    public void setRemoteImmobilize(String remoteImmobilize) {
        this.remoteImmobilize = remoteImmobilize;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getShowButton() {
        return showButton;
    }

    public void setShowButton(String showButton) {
        this.showButton = showButton;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getRequestMsg() {
        return requestMsg;
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }
}
