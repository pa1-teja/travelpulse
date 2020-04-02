package com.trimax.vts.view.lock;

public class RequestModel {
    private String type;
    private String time;
    private String status;
    private String vehicleNo;

    public RequestModel(String type, String time, String status, String vehicleNo) {
        this.type = type;
        this.time = time;
        this.status = status;
        this.vehicleNo = vehicleNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
