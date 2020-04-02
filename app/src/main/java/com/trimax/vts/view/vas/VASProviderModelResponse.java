package com.trimax.vts.view.vas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VASProviderModelResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private VASProviderModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VASProviderModel getData() {
        return data;
    }

    public void setData(VASProviderModel data) {
        this.data = data;
    }
}
