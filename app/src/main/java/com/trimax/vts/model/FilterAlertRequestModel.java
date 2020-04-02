package com.trimax.vts.model;

/**
 * Created by nameeta.choudhari on 6/2/2016.
 */
public class FilterAlertRequestModel {
    Integer startIndex;
    Integer endIndex;
    Integer userId;
    String [] alertTypes;
    Integer [] vehicleIds;
    String alertDate;
    String user_id;
    String user_type_id;
    String vehicle_id;
    String notification_type_id;

    public Integer[] getVehicleIds() {
        return vehicleIds;
    }

    public void setVehicleIds(Integer[] vehicleIds) {
        this.vehicleIds = vehicleIds;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(String user_type_id) {
        this.user_type_id = user_type_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getNotification_type_id() {
        return notification_type_id;
    }

    public void setNotification_type_id(String notification_type_id) {
        this.notification_type_id = notification_type_id;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    String limit;
    String start;
    public Integer[] getVehicleId() {
        return vehicleIds;
    }

    public void setVehicleId(Integer[] vehicleIds) {
        this.vehicleIds = vehicleIds;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String[] getAlertTypes() {
        return alertTypes;
    }

    public void setAlertTypes(String[] alertTypes) {
        this.alertTypes = alertTypes;
    }

    public String getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(String alertDate) {
        this.alertDate = alertDate;
    }
}
