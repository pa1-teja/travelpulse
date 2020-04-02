package com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationPrefModelResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("parking_mode")
    @Expose
    private boolean parking_mode;

    public boolean getParking_mode() {
        return parking_mode;
    }

    public void setParking_mode(boolean parking_mode) {
        this.parking_mode = parking_mode;
    }

    @SerializedName("preference_arr")
    @Expose

    private List<NotificationPrefModel> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NotificationPrefModel> getData() {
        return data;
    }

    public void setData(List<NotificationPrefModel> data) {
        this.data = data;
    }



    public static class NotificationData {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("data")
        @Expose
        private NotificationPrefModelResponse data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public NotificationPrefModelResponse getNData() {
            return data;
        }

        public void setNData(NotificationPrefModelResponse data) {
            this.data = data;
        }

    }

}
