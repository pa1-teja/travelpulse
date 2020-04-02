package com.trimax.vts.view.master.pojo.documents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UploadDocumentTypeResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ArrayList<UploadDocumentTypeList> data = null;

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

    public ArrayList<UploadDocumentTypeList> getData() {
        return data;
    }

    public void setData(ArrayList<UploadDocumentTypeList> data) {
        this.data = data;
    }

}
