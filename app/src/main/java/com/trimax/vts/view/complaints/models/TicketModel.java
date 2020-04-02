package com.trimax.vts.view.complaints.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TicketModel implements Serializable {

    @SerializedName("ticket_id")
    @Expose
    private String id;

    @SerializedName("ticket_no")
    @Expose
    private String ticketNo;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("problem_faced")
    @Expose
    private String problemFaced;
    @SerializedName("solution")
    @Expose
    private String solution;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getProblemFaced() {
        return problemFaced;
    }

    public void setProblemFaced(String problemFaced) {
        this.problemFaced = problemFaced;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
