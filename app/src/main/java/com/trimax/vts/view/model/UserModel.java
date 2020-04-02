package com.trimax.vts.view.model;

public class UserModel {
    private String name;
    private String mobile;
    private String vehicle;
    private String status;
    private String relation;

    public UserModel(String name, String mobile, String vehicle) {
        this.name = name;
        this.mobile = mobile;
        this.vehicle = vehicle;
        this.status="Active";
        this.relation="Family";
    }

    public UserModel(String name, String mobile, String vehicle, String relation) {
        this.name = name;
        this.mobile = mobile;
        this.vehicle = vehicle;
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
