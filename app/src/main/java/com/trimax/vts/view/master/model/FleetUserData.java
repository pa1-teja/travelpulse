package com.trimax.vts.view.master.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FleetUserData {

    @SerializedName("vehicle_status_moving")
    @Expose
    private Integer vehicleStatusMoving;
    @SerializedName("vehicle_status_stop")
    @Expose
    private Integer vehicleStatusStop;
    @SerializedName("vehicle_status_idle")
    @Expose
    private Integer vehicleStatusIdle;
    @SerializedName("vehicle_status_no_network")
    @Expose
    private Integer vehicleStatusNoNetwork;
    @SerializedName("vehicle_detail")
    @Expose
    private List<VehicleDetail> vehicleDetail = null;

    public Integer getVehicleStatusMoving() {
        return vehicleStatusMoving;
    }

    public void setVehicleStatusMoving(Integer vehicleStatusMoving) {
        this.vehicleStatusMoving = vehicleStatusMoving;
    }

    public Integer getVehicleStatusStop() {
        return vehicleStatusStop;
    }

    public void setVehicleStatusStop(Integer vehicleStatusStop) {
        this.vehicleStatusStop = vehicleStatusStop;
    }

    public Integer getVehicleStatusIdle() {
        return vehicleStatusIdle;
    }

    public void setVehicleStatusIdle(Integer vehicleStatusIdle) {
        this.vehicleStatusIdle = vehicleStatusIdle;
    }

    public Integer getVehicleStatusNoNetwork() {
        return vehicleStatusNoNetwork;
    }

    public void setVehicleStatusNoNetwork(Integer vehicleStatusNoNetwork) {
        this.vehicleStatusNoNetwork = vehicleStatusNoNetwork;
    }

    public List<VehicleDetail> getVehicleDetail() {
        return vehicleDetail;
    }

    public void setVehicleDetail(List<VehicleDetail> vehicleDetail) {
        this.vehicleDetail = vehicleDetail;
    }



   public class vehicle {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("data")
        @Expose
        private FleetUserData data;

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

        public FleetUserData getData() {
            return data;
        }

        public void setData(FleetUserData data) {
            this.data = data;
        }

    }

    public class VehicleDetail {

        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("network")
        @Expose
        private String network;
        @SerializedName("ignition")
        @Expose
        private String ignition;
        @SerializedName("ac")
        @Expose
        private String ac;
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("speed")
        @Expose
        private String speed;
        @SerializedName("driver_name")
        @Expose
        private String driverName;
        @SerializedName("owner_name")
        @Expose
        private String ownerName;
        @SerializedName("group_name")
        @Expose
        private String groupName;
        @SerializedName("updated_on")
        @Expose
        private String updatedOn;
        @SerializedName("vehicle_id")
        @Expose
        private String vehicleId;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("vehicle_type_id")
        @Expose
        private String vehicle_type_id;
        @SerializedName("gps")
        @Expose
        private String gps;


        public String getVehicle_type_id() {
            return vehicle_type_id;
        }

        public void setVehicle_type_id(String vehicle_type_id) {
            this.vehicle_type_id = vehicle_type_id;
        }


        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getIgnition() {
            return ignition;
        }

        public void setIgnition(String ignition) {
            this.ignition = ignition;
        }

        public String getAc() {
            return ac;
        }

        public void setAc(String ac) {
            this.ac = ac;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getGps() {
            return gps;
        }

        public void setGps(String gps) {
            this.gps = gps;
        }
    }

}