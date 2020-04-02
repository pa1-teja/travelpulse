package com.trimax.vts.view.master.pojo.FleetSubUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FleetSubUsersModelResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<FleetSubUsersModel> data = null;

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

    public List<FleetSubUsersModel> getData() {
        return data;
    }

    public void setData(List<FleetSubUsersModel> data) {
        this.data = data;
    }

}
