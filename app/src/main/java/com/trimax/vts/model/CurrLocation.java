package com.trimax.vts.model;

import java.io.Serializable;

public class CurrLocation implements Serializable {
	private Double latitude;
	private Double longitude;
	private String area;
	private String serviceCode;
	private Integer providerId;
	private String outletname;
	private Integer eta;
	private Integer searchRadius;
	private Integer cost;
	private Boolean useLocation;
	private Integer userId;
	
	public Integer getProviderId() {
		return providerId;
	}
	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	public Integer getEta() {
		return eta;
	}
	public void setEta(Integer eta) {
		this.eta = eta;
	}
	public Integer getSearchRadius() {
		return searchRadius;
	}
	public void setSearchRadius(Integer searchRadius) {
		this.searchRadius = searchRadius;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getOutletname() {
		return outletname;
	}
	public void setOutletname(String outletname) {
		this.outletname = outletname;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Boolean getUseLocation() {
		return useLocation;
	}

	public void setUseLocation(Boolean useLocation) {
		this.useLocation = useLocation;
	}
}