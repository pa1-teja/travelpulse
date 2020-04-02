package com.trimax.vts.view.master.pojo.VehicleMaster;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleProfileModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("vehicle_type_id")
    @Expose
    private String vehicleTypeId;//this is equipment type id
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("yr_of_manufacture")
    @Expose
    private String yrOfManufacture;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("fuel_type")
    @Expose
    private String fuelType;
    @SerializedName("chassis_no")
    @Expose
    private String chassisNo;
    @SerializedName("engine_no")
    @Expose
    private String engineNo;
    @SerializedName("speed_limit")
    @Expose
    private String speedLimit;
    @SerializedName("first_owner")
    @Expose
    private String firstOwner;
    @SerializedName("default_driver_id")
    @Expose
    private String defaultDriverId;
    @SerializedName("insurance_company")
    @Expose
    private String insuranceCompany;
    @SerializedName("insurance_policy_no")
    @Expose
    private String insurancePolicyNo;
    @SerializedName("insurance_expiry_date")
    @Expose
    private String insuranceExpiryDate;
    @SerializedName("tracker_sim_no")
    @Expose
    private String trackerSimNo;
    @SerializedName("tracker_imei_no")
    @Expose
    private String trackerImeiNo;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("created_by_usertype")
    @Expose
    private String createdByUsertype;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_on")
    @Expose
    private String updatedOn;
    @SerializedName("updated_by_usertype")
    @Expose
    private String updatedByUsertype;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("is_delete")
    @Expose
    private String isDelete;
    @SerializedName("installation_date")
    @Expose
    private Object installationDate;
    @SerializedName("remark")
    @Expose
    private Object remark;
    @SerializedName("tracking_device_type")
    @Expose
    private String trackingDeviceType;
    @SerializedName("is_used_with_asset")
    @Expose
    private String isUsedWithAsset;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
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

    public String getYrOfManufacture() {
        return yrOfManufacture;
    }

    public void setYrOfManufacture(String yrOfManufacture) {
        this.yrOfManufacture = yrOfManufacture;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(String speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getFirstOwner() {
        return firstOwner;
    }

    public void setFirstOwner(String firstOwner) {
        this.firstOwner = firstOwner;
    }

    public String getDefaultDriverId() {
        return defaultDriverId;
    }

    public void setDefaultDriverId(String defaultDriverId) {
        this.defaultDriverId = defaultDriverId;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsurancePolicyNo() {
        return insurancePolicyNo;
    }

    public void setInsurancePolicyNo(String insurancePolicyNo) {
        this.insurancePolicyNo = insurancePolicyNo;
    }

    public String getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(String insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public String getTrackerSimNo() {
        return trackerSimNo;
    }

    public void setTrackerSimNo(String trackerSimNo) {
        this.trackerSimNo = trackerSimNo;
    }

    public String getTrackerImeiNo() {
        return trackerImeiNo;
    }

    public void setTrackerImeiNo(String trackerImeiNo) {
        this.trackerImeiNo = trackerImeiNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedByUsertype() {
        return createdByUsertype;
    }

    public void setCreatedByUsertype(String createdByUsertype) {
        this.createdByUsertype = createdByUsertype;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedByUsertype() {
        return updatedByUsertype;
    }

    public void setUpdatedByUsertype(String updatedByUsertype) {
        this.updatedByUsertype = updatedByUsertype;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Object getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Object installationDate) {
        this.installationDate = installationDate;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getTrackingDeviceType() {
        return trackingDeviceType;
    }

    public void setTrackingDeviceType(String trackingDeviceType) {
        this.trackingDeviceType = trackingDeviceType;
    }

    public String getIsUsedWithAsset() {
        return isUsedWithAsset;
    }

    public void setIsUsedWithAsset(String isUsedWithAsset) {
        this.isUsedWithAsset = isUsedWithAsset;
    }


}
