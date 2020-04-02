package com.trimax.vts.model;

public class UserProfileUpdate
{
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String mobileNo;
	private String emergencyContact1;
	private String emergencyContact2;
	private String emergencyContact3;
	private String bloodGroup;
	private String bloodPressure;
	private String suger;
	private String heartTrouble;
	private String landline;
    private String ownerLicenceNo;

	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmergencyContact1() {
		return emergencyContact1;
	}
	public void setEmergencyContact1(String emergencyContact1) {
		this.emergencyContact1 = emergencyContact1;
	}
	public String getEmergencyContact2() {
		return emergencyContact2;
	}
	public void setEmergencyContact2(String emergencyContact2) {
		this.emergencyContact2 = emergencyContact2;
	}
	public String getEmergencyContact3() {
		return emergencyContact3;
	}
	public void setEmergencyContact3(String emergencyContact3) {
		this.emergencyContact3 = emergencyContact3;
	}
	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public String getSuger() {
		return suger;
	}

	public void setSuger(String suger) {
		this.suger = suger;
	}

	public String getHeartTrouble() {
		return heartTrouble;
	}

	public void setHeartTrouble(String heartTrouble) {
		this.heartTrouble = heartTrouble;
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	public String getOwnerLicenceNo() {
		return ownerLicenceNo;
	}

	public void setOwnerLicenceNo(String ownerLicenceNo) {
		this.ownerLicenceNo = ownerLicenceNo;
	}
}
