package com.trimax.vts.vehicle_lock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trimax.vts.vehicle_lock.VehicleLockStatus;

import java.util.List;

public class VehicleLockStatusResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<VehicleLockStatus> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<VehicleLockStatus> getData() {
        return data;
    }

    public void setData(List<VehicleLockStatus> data) {
        this.data = data;
    }
}
