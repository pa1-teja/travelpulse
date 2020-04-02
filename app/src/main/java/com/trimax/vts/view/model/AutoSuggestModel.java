package com.trimax.vts.view.model;

public class AutoSuggestModel {
    private String title;
    private String address;
    private double lat;
    private double lng;

    public AutoSuggestModel() {
    }

    public AutoSuggestModel(String title, String address, double lat, double lng) {
        this.title = title;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
