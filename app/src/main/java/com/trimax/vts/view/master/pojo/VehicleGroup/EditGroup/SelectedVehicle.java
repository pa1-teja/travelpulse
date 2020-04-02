package com.trimax.vts.view.master.pojo.VehicleGroup.EditGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedVehicle {

    @SerializedName("id")
    @Expose
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
