package com.trimax.vts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trimax.vts.view.maps.ReplayTrackingActivity;

import java.util.ArrayList;

public class LivetrackingwrapperResponseNew {

    public ReplayResponseNew replayResponseNew;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("total_distance")
    @Expose
    private String totalDistance;
    @SerializedName("realtime_data")
    @Expose
    private ArrayList<LiveTrakingResponseNew> realtimeData = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public ReplayResponseNew getReplayResponseNew() {
        return replayResponseNew;
    }

    public void setReplayResponseNew(ReplayResponseNew replayResponseNew) {
        this.replayResponseNew = replayResponseNew;
    }

    public ArrayList<LiveTrakingResponseNew> getRealtimeData() {
        return realtimeData;
    }

    public void setRealtimeData(ArrayList<LiveTrakingResponseNew> realtimeData) {
        this.realtimeData = realtimeData;
    }

}