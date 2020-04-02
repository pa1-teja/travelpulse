package com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationPrefModelSetPrefResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<NotificationPrefModel> data = null;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("msg")
    @Expose
    private String msg;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NotificationPrefModel> getData() {
        return data;
    }

    public void setData(List<NotificationPrefModel> data) {
        this.data = data;
    }
}
