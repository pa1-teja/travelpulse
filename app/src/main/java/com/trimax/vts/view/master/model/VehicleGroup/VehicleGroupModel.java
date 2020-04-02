package com.trimax.vts.view.master.model.VehicleGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleGroupModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

}
