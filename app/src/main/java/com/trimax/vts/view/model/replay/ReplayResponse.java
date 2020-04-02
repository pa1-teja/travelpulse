package com.trimax.vts.view.model.replay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReplayResponse {
    @SerializedName("status")
    @Expose
    private String status;
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
    private RealtimeData realtimeData = null;

    @SerializedName("msg")
    @Expose
    private ReplayError error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public RealtimeData getRealtimeData() {
        return realtimeData;
    }

    public void setRealtimeData(RealtimeData realtimeData) {
        this.realtimeData = realtimeData;
    }

    public ReplayError getError() {
        return error;
    }

    public void setError(ReplayError error) {
        this.error = error;
    }
}
