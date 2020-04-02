package com.trimax.vts.view.vas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VASFinalProviderListModel {
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("outlet_name")
    @Expose
    private String outletName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("contact_person_name")
    @Expose
    private String contactPersonName;
    @SerializedName("contact_person_tel_no")
    @Expose
    private String contactPersonTelNo;
    @SerializedName("contact_person_mobile")
    @Expose
    private String contactPersonMobile;
    @SerializedName("contact_person_email_id")
    @Expose
    private String contactPersonEmailId;
    @SerializedName("triamx_preference_rating")
    @Expose
    private String triamxPreferenceRating;
    @SerializedName("pstatus")
    @Expose
    private String pstatus;
    @SerializedName("call_center_control")
    @Expose
    private String callCenterControl;
    @SerializedName("sid")
    @Expose
    private String sid;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("group_service_code")
    @Expose
    private String groupServiceCode;
    @SerializedName("response_time")
    @Expose
    private String responseTime;
    @SerializedName("service_total_time")
    @Expose
    private String serviceTotalTime;
    @SerializedName("single_service_flag")
    @Expose
    private String singleServiceFlag;
    @SerializedName("free")
    @Expose
    private String free;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("search_radius")
    @Expose
    private String searchRadius;
    @SerializedName("location_basis")
    @Expose
    private String locationBasis;
    @SerializedName("sstatus")
    @Expose
    private String sstatus;
    @SerializedName("user_service_rating")
    @Expose
    private String userServiceRating;
    @SerializedName("rates")
    @Expose
    private String rates;
    @SerializedName("distance")
    @Expose
    private Integer distance;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonTelNo() {
        return contactPersonTelNo;
    }

    public void setContactPersonTelNo(String contactPersonTelNo) {
        this.contactPersonTelNo = contactPersonTelNo;
    }

    public String getContactPersonMobile() {
        return contactPersonMobile;
    }

    public void setContactPersonMobile(String contactPersonMobile) {
        this.contactPersonMobile = contactPersonMobile;
    }

    public String getContactPersonEmailId() {
        return contactPersonEmailId;
    }

    public void setContactPersonEmailId(String contactPersonEmailId) {
        this.contactPersonEmailId = contactPersonEmailId;
    }

    public String getTriamxPreferenceRating() {
        return triamxPreferenceRating;
    }

    public void setTriamxPreferenceRating(String triamxPreferenceRating) {
        this.triamxPreferenceRating = triamxPreferenceRating;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getCallCenterControl() {
        return callCenterControl;
    }

    public void setCallCenterControl(String callCenterControl) {
        this.callCenterControl = callCenterControl;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupServiceCode() {
        return groupServiceCode;
    }

    public void setGroupServiceCode(String groupServiceCode) {
        this.groupServiceCode = groupServiceCode;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getServiceTotalTime() {
        return serviceTotalTime;
    }

    public void setServiceTotalTime(String serviceTotalTime) {
        this.serviceTotalTime = serviceTotalTime;
    }

    public String getSingleServiceFlag() {
        return singleServiceFlag;
    }

    public void setSingleServiceFlag(String singleServiceFlag) {
        this.singleServiceFlag = singleServiceFlag;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(String searchRadius) {
        this.searchRadius = searchRadius;
    }

    public String getLocationBasis() {
        return locationBasis;
    }

    public void setLocationBasis(String locationBasis) {
        this.locationBasis = locationBasis;
    }

    public String getSstatus() {
        return sstatus;
    }

    public void setSstatus(String sstatus) {
        this.sstatus = sstatus;
    }

    public String getUserServiceRating() {
        return userServiceRating;
    }

    public void setUserServiceRating(String userServiceRating) {
        this.userServiceRating = userServiceRating;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}
