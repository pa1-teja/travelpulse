package com.trimax.vts.view.model.replay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReplayData {
    @SerializedName("result")
    @Expose
    private List<ReplayLatLng> result = null;
    @SerializedName("notification_data")
    @Expose
    private List<NotificationData> notificationData = null;
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
    private List<RealtimeData> realtimeData = null;

    public List<ReplayLatLng> getResult() {
        return result;
    }

    public void setResult(List<ReplayLatLng> result) {
        this.result = result;
    }

    public List<NotificationData> getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(List<NotificationData> notificationData) {
        this.notificationData = notificationData;
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

    public List<RealtimeData> getRealtimeData() {
        return realtimeData;
    }

    public void setRealtimeData(List<RealtimeData> realtimeData) {
        this.realtimeData = realtimeData;
    }
}
