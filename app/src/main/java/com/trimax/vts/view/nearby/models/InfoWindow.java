package com.trimax.vts.view.nearby.models;

public class InfoWindow {
    private String image;
    private String name;
    private String address;
    private String distance;

    public InfoWindow(String image, String name, String address,String distance) {
        this.image = image;
        this.name = name;
        this.address = address;
        this.distance=distance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
