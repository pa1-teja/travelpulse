package com.trimax.vts.model;

import com.trimax.vts.view.model.LatLngClass;

import java.util.ArrayList;

public class AllGeoFenceResponse {
	Integer fenceId;
	String name;
	ArrayList<LatLngClass> polygonCordinates;
	String alertsEnabled;
	
	public Integer getFenceId() {
		return fenceId;
	}
	public void setFenceId(Integer fenceId) {
		this.fenceId = fenceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<LatLngClass> getPolygonCordinates() {
		return polygonCordinates;
	}
	public void setPolygonCordinates(ArrayList<LatLngClass> polygonCordinates) {
		this.polygonCordinates = polygonCordinates;
	}
	public String getAlertsEnabled() {
		return alertsEnabled;
	}
	public void setAlertsEnabled(String alertsEnabled) {
		this.alertsEnabled = alertsEnabled;
	}
}
