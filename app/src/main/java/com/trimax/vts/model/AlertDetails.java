package com.trimax.vts.model;

import java.io.Serializable;

public class AlertDetails implements Serializable
{
	private int id;
	private String  alertDetails;
	private String alerttype;
	private String alertSourceType;
	private String readStatus;
	private String deliveryStatus;
	private String serviceName;
	private String timeStamp;
	private String serviceCode;
	private Double trackerLat;
	private Double trackerLong;
	private String ac;
	private String ignition;
	private String trackerDateTime;
	private String devicetoken;
	private String vehicleName;
	private String vehicleRegNo;
	private String speed;
	private Integer userId;
	private Integer userRequestId;
	private String randomCounter;
	private String provider_id;
	private String Area;

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	private String cost;

	public String getEta() {
		return Eta;
	}

	public void setEta(String eta) {
		Eta = eta;
	}

	private String Eta;

	public String getCall_center_control() {
		return call_center_control;
	}

	public void setCall_center_control(String call_center_control) {
		this.call_center_control = call_center_control;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	private String call_center_control;
	private String service_name;

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	private String radius;

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}


	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}



	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(			String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getAlerttype() {
		return alerttype;
	}
	public void setAlerttype(String alerttype) {
		this.alerttype = alerttype;
	}
	public String getAlertSourceType() {
		return alertSourceType;
	}
	public void setAlertSourceType(String alertSourceType) {
		this.alertSourceType = alertSourceType;
	}
	public String getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAlertDetails() {
		return alertDetails;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public void setAlertDetails(String alertDetails) {
		this.alertDetails = alertDetails;
	}


	public Double getTrackerLat() {
		return trackerLat;
	}

	public void setTrackerLat(Double trackerLat) {
		this.trackerLat = trackerLat;
	}

	public Double getTrackerLong() {
		return trackerLong;
	}

	public void setTrackerLong(Double trackerLong) {
		this.trackerLong = trackerLong;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getIgnition() {
		return ignition;
	}

	public void setIgnition(String ignition) {
		this.ignition = ignition;
	}

	public String getTrackerDateTime() {
		return trackerDateTime;
	}

	public void setTrackerDateTime(String trackerDateTime) {
		this.trackerDateTime = trackerDateTime;
	}

	public String getDevicetoken() {
		return devicetoken;
	}

	public void setDevicetoken(String devicetoken) {
		this.devicetoken = devicetoken;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getVehicleRegNo() {
		return vehicleRegNo;
	}

	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserRequestId() {
		return userRequestId;
	}

	public void setUserRequestId(Integer userRequestId) {
		this.userRequestId = userRequestId;
	}

	public String getRandomCounter() {
		return randomCounter;
	}

	public void setRandomCounter(String randomCounter) {
		this.randomCounter = randomCounter;
	}
}
