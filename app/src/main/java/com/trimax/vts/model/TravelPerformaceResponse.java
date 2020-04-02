package com.trimax.vts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelPerformaceResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("travelled_kms")
    @Expose
    private String travelled_kms;
    @SerializedName("data")
    @Expose
    private ModelTravelPerformance data = null;

    public String getTravelled_kms() {
        return travelled_kms;
    }

    public void setTravelled_kms(String travelled_kms) {
        this.travelled_kms = travelled_kms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ModelTravelPerformance getData() {
        return data;
    }

    public void setData(ModelTravelPerformance data) {
        this.data = data;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
