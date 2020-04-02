package com.trimax.vts.view.master.model.VehicleGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupwiseVehicleModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("tracker_imei_no")
    @Expose
    private String trackerImeiNo;

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

    public String getTrackerImeiNo() {
        return trackerImeiNo;
    }

    public void setTrackerImeiNo(String trackerImeiNo) {
        this.trackerImeiNo = trackerImeiNo;
    }

}
