package com.trimax.vts.view.master.model.VehicleMaster;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleMasterModel implements Serializable {
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
    @SerializedName("tracker_imei_no")
    @Expose
    private String trackerImeiNo;
    @SerializedName("vehicle_group_id")
    @Expose
    private Object vehicleGroupId;
    @SerializedName("group_name")
    @Expose
    private Object groupName;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("vehicle_type_id")
    @Expose
    private String vehicle_type_id;
    @SerializedName("parking_mode")
    @Expose
    private String parking_mode;

    public String getParking_mode() {
        return parking_mode;
    }

    public void setParking_mode(String parking_mode) {
        this.parking_mode = parking_mode;
    }

    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
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

    public String getTrackerImeiNo() {
        return trackerImeiNo;
    }

    public void setTrackerImeiNo(String trackerImeiNo) {
        this.trackerImeiNo = trackerImeiNo;
    }

    public Object getVehicleGroupId() {
        return vehicleGroupId;
    }

    public void setVehicleGroupId(Object vehicleGroupId) {
        this.vehicleGroupId = vehicleGroupId;
    }

    public Object getGroupName() {
        return groupName;
    }

    public void setGroupName(Object groupName) {
        this.groupName = groupName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
