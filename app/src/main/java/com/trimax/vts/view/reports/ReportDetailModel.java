package com.trimax.vts.view.reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportDetailModel {
    @SerializedName("vehicle_status")
    @Expose
    private String vehicleStatus;
    @SerializedName("start_point_time")
    @Expose
    private String startDate;
    @SerializedName("start_point_location")
    @Expose
    private String startPointLocation;

    @SerializedName("end_point_location")
    @Expose
    private String endPointLocation;
    @SerializedName("end_point_time")
    @Expose
    private String endDate;
    @SerializedName("duration_hours_min")
    @Expose
    private String durationHoursMin;
    @SerializedName("travelled_km")
    @Expose
    private String distance;

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public ReportDetailModel(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartPointLocation() {
        return startPointLocation;
    }

    public void setStartPointLocation(String startPointLocation) {
        this.startPointLocation = startPointLocation;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDurationHoursMin() {
        return durationHoursMin;
    }

    public void setDurationHoursMin(String durationHoursMin) {
        this.durationHoursMin = durationHoursMin;
    }

    public String getEndPointLocation() {
        return endPointLocation;
    }

    public void setEndPointLocation(String endPointLocation) {
        this.endPointLocation = endPointLocation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
