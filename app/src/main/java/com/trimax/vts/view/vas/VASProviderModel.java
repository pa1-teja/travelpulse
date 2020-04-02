package com.trimax.vts.view.vas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VASProviderModel {
    @SerializedName("search_type")
    @Expose
    private String searchType;
    @SerializedName("service_code")
    @Expose
    private String serviceCode;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("use_current_location")
    @Expose
    private String useCurrentLocation;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("search_radius")
    @Expose
    private String searchRadius;
    @SerializedName("eta")
    @Expose
    private String eta;
    @SerializedName("outlet_name")
    @Expose
    private String outletName;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("final_provider_list")
    @Expose
    private List<VASFinalProviderListModel> finalProviderList = null;


    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUseCurrentLocation() {
        return useCurrentLocation;
    }

    public void setUseCurrentLocation(String useCurrentLocation) {
        this.useCurrentLocation = useCurrentLocation;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(String searchRadius) {
        this.searchRadius = searchRadius;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public List<VASFinalProviderListModel> getFinalProviderList() {
        return finalProviderList;
    }

    public void setFinalProviderList(List<VASFinalProviderListModel> finalProviderList) {
        this.finalProviderList = finalProviderList;
    }
}
