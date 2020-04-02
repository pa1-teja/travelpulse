package com.trimax.vts.view.master.model.VehicleGroup.EditGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditGroupModelResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private EditGroupModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EditGroupModel getData() {
        return data;
    }

    public void setData(EditGroupModel data) {
        this.data = data;
    }
}
