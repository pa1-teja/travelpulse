package com.trimax.vts.view.master.model.VehicleGroup.VehiclesList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclesForAddGroupModelResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<VehiclesForAddGroupModel> data = null;

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

    public List<VehiclesForAddGroupModel> getData() {
        return data;
    }

    public void setData(List<VehiclesForAddGroupModel> data) {
        this.data = data;
    }

}
