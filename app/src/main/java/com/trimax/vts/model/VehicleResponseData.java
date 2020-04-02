package com.trimax.vts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trimax.vts.model.vehicle.VehicleList;

import java.util.ArrayList;

public class VehicleResponseData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ArrayList<VehicleList> data = null;

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

    public ArrayList<VehicleList> getData() {
        return data;
    }

    public void setData(ArrayList<VehicleList> data) {
        this.data = data;
    }

}
