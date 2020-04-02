package com.trimax.vts.vehicle_lock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSentOTP {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private ResponseDate responseDate;

    public ResponseDate getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(ResponseDate responseDate) {
        this.responseDate = responseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    class ResponseDate {
        @SerializedName("mobile_device_id")
        @Expose
        private String mobileDeviceID;

        public String getMobileDeviceID() {
            return mobileDeviceID;
        }

        public void setMobileDeviceID(String mobileDeviceID) {
            this.mobileDeviceID = mobileDeviceID;
        }
    }
}
