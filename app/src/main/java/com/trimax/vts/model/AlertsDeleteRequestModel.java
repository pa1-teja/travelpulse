package com.trimax.vts.model;

/**
 * Created by nameeta.choudhari on 6/3/2016.
 */
public class AlertsDeleteRequestModel {
    Integer [] alertIds;
    boolean deleteAll;
    Integer userId;

    public Integer[] getAlertIds() {
        return alertIds;
    }

    public void setAlertIds(Integer[] alertIds) {
        this.alertIds = alertIds;
    }

    public boolean isDeleteAll() {
        return deleteAll;
    }

    public void setDeleteAll(boolean deleteAll) {
        this.deleteAll = deleteAll;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
