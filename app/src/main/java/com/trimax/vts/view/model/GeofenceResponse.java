package com.trimax.vts.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trimax.vts.view.model.replay.RealtimeData;

import java.util.ArrayList;

public class GeofenceResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("realtime_data")
    @Expose
    private RealtimeData realtimeData = null;


    @SerializedName("geo_fence_details")
    @Expose
    private ArrayList<GeoFenceModel> geo_fence_details = null;


    public ArrayList<GeoFenceModel> getGeo_fence_details() {
        return geo_fence_details;
    }

    public void setGeo_fence_details(ArrayList<GeoFenceModel> geo_fence_details) {
        this.geo_fence_details = geo_fence_details;
    }


    public  RealtimeData getRealtimeData() {
        return realtimeData;
    }

    public void setRealtimeData(RealtimeData realtimeData) {
        this.realtimeData = realtimeData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
