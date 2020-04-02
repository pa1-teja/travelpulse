package com.trimax.vts.view.model.replay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplayLatLng {

    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lang")
    @Expose
    private String lang;

    public ReplayLatLng(String lat, String lang) {
        this.lat = lat;
        this.lang = lang;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
