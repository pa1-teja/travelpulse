package com.trimax.vts.model;

import java.io.Serializable;

public class ProviderDetails implements Serializable 
{
	private int id;
	private int providerId;
	private String outletName;
	private String address;
	private Integer areaId;
	private Integer cityId;
	private Integer pin;
	private double latitude;
	private double longitude;
	private String regnNo;
	private String contactPersonName;
	private String calculatedETA;
	public Integer getRates() {
		return rates;
	}
	private String contactPersonTelNo;
	private String contactPersonMobile;
	private String contactPersonEmailId;
	private Integer triamxPreferenceRating;
	private Integer referralPointesEarned;
	private Integer responseTime;
	private Integer rates;
	private boolean callCenterControl;

	/*public ProviderDetails() {
	}*/

	public ProviderDetails(int id, int providerId, String outletName, String address, Integer areaId, Integer cityId,
						   Integer pin, double latitude, double longitude, String regnNo, String contactPersonName,
						   String calculatedETA, String contactPersonTelNo, String contactPersonMobile,
						   String contactPersonEmailId, Integer triamxPreferenceRating,
						   Integer referralPointesEarned, Integer responseTime, Integer rates, boolean callCenterControl) {
		this.id = id;
		this.providerId = providerId;
		this.outletName = outletName;
		this.address = address;
		this.areaId = areaId;
		this.cityId = cityId;
		this.pin = pin;
		this.latitude = latitude;
		this.longitude = longitude;
		this.regnNo = regnNo;
		this.contactPersonName = contactPersonName;
		this.calculatedETA = calculatedETA;
		this.contactPersonTelNo = contactPersonTelNo;
		this.contactPersonMobile = contactPersonMobile;
		this.contactPersonEmailId = contactPersonEmailId;
		this.triamxPreferenceRating = triamxPreferenceRating;
		this.referralPointesEarned = referralPointesEarned;
		this.responseTime = responseTime;
		this.rates = rates;
		this.callCenterControl = callCenterControl;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}
	public void setRates(Integer rates) {
		this.rates = rates;
	}

	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getRegnNo() {
		return regnNo;
	}
	public void setRegnNo(String regnNo) {
		this.regnNo = regnNo;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	public String getContactPersonTelNo() {
		return contactPersonTelNo;
	}
	public void setContactPersonTelNo(String contactPersonTelNo) {
		this.contactPersonTelNo = contactPersonTelNo;
	}
	public String getContactPersonMobile() {
		return contactPersonMobile;
	}
	public void setContactPersonMobile(String contactPersonMobile) {
		this.contactPersonMobile = contactPersonMobile;
	}
	public String getContactPersonEmailId() {
		return contactPersonEmailId;
	}
	public void setContactPersonEmailId(String contactPersonEmailId) {
		this.contactPersonEmailId = contactPersonEmailId;
	}
	public Integer getTriamxPreferenceRating() {
		return triamxPreferenceRating;
	}
	public void setTriamxPreferenceRating(Integer triamxPreferenceRating) {
		this.triamxPreferenceRating = triamxPreferenceRating;
	}
	public Integer getReferralPointesEarned() {
		return referralPointesEarned;
	}
	public void setReferralPointesEarned(Integer referralPointesEarned) {
		this.referralPointesEarned = referralPointesEarned;
	}
	public String getCalculatedETA() {
		return calculatedETA;
	}
	public void setCalculatedETA(String calculatedETA) {
		this.calculatedETA = calculatedETA;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Integer getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Integer responseTime) {
		this.responseTime = responseTime;
	}

	public boolean isCallCenterControl() {
		return callCenterControl;
	}

	public void setCallCenterControl(boolean callCenterControl) {
		this.callCenterControl = callCenterControl;
	}
}
