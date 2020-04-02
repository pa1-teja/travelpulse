package com.trimax.vts.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trimax.vts.model.Current_Status;

import java.util.ArrayList;

public class CurrentStarusData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ArrayList<Current_Status> data = null;

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

    public ArrayList<Current_Status> getData() {
        return data;
    }

    public void setData(ArrayList<Current_Status> data) {
        this.data = data;
    }

}
