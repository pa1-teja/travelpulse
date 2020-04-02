package com.trimax.vts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GeoFenceStatus {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("geofences")
    @Expose
    private ArrayList<GeoFence> geofences = null;

    public ArrayList<GeoFence> getGeofences() {
        return geofences;
    }

    public void setGeofences(ArrayList<GeoFence> geofences) {
        this.geofences = geofences;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
