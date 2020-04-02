package com.trimax.vts.view.nearby.models;

public class PlaceServiceModel {
    private String service;
    private String type;
    private int background;
    private int imgRes;
    private int markerRes;

    public PlaceServiceModel(String service, int background, int imgRes,String type,int markerRes) {
        this.service = service;
        this.background = background;
        this.imgRes = imgRes;
        this.type=type;
        this.markerRes=markerRes;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMarkerRes() {
        return markerRes;
    }

    public void setMarkerRes(int markerRes) {
        this.markerRes = markerRes;
    }
}
