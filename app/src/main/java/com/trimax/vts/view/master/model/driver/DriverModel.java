package com.trimax.vts.view.master.model.driver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("license_number")
    @Expose
    private Object licenseNumber;
    @SerializedName("license_expiry")
    @Expose
    private Object licenseExpiry;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("self_driven")
    @Expose
    private String selfDriven;
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

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("file_name")
    @Expose
    private String file_name;
    @SerializedName("file_path")
    @Expose
    private String file_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(Object licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Object getLicenseExpiry() {
        return licenseExpiry;
    }

    public void setLicenseExpiry(Object licenseExpiry) {
        this.licenseExpiry = licenseExpiry;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSelfDriven() {
        return selfDriven;
    }

    public void setSelfDriven(String selfDriven) {
        this.selfDriven = selfDriven;
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


    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}