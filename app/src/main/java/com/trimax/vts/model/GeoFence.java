package com.trimax.vts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GeoFence {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("geo_fence_name")
        @Expose
        private String geoFenceName;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("map_center_lat")
        @Expose
        private Object mapCenterLat;
        @SerializedName("map_center_lang")
        @Expose
        private Object mapCenterLang;
        @SerializedName("map_zoom")
        @Expose
        private Object mapZoom;
        @SerializedName("overlay_type")
        @Expose
        private String overlayType;
        @SerializedName("geo_fence_lat_langs")
        @Expose
        private String geoFenceLatLangs;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_on")
        @Expose
        private String createdOn;
        @SerializedName("created_by_usertype")
        @Expose
        private String createdByUsertype;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("updated_on")
        @Expose
        private String updatedOn;
        @SerializedName("updated_by_usertype")
        @Expose
        private String updatedByUsertype;
        @SerializedName("updated_by")
        @Expose
        private String updatedBy;
        @SerializedName("auto_generated")
        @Expose
        private String autoGenerated;
        @SerializedName("created_pkt_date_time")
        @Expose
        private String createdPktDateTime;
        @SerializedName("updated_pkt_date_time")
        @Expose
        private String updatedPktDateTime;
        @SerializedName("is_delete")
        @Expose
        private String isDelete;
        @SerializedName("vid")
        @Expose
        private String vid;
        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGeoFenceName() {
            return geoFenceName;
        }

        public void setGeoFenceName(String geoFenceName) {
            this.geoFenceName = geoFenceName;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public Object getMapCenterLat() {
            return mapCenterLat;
        }

        public void setMapCenterLat(Object mapCenterLat) {
            this.mapCenterLat = mapCenterLat;
        }

        public Object getMapCenterLang() {
            return mapCenterLang;
        }

        public void setMapCenterLang(Object mapCenterLang) {
            this.mapCenterLang = mapCenterLang;
        }

        public Object getMapZoom() {
            return mapZoom;
        }

        public void setMapZoom(Object mapZoom) {
            this.mapZoom = mapZoom;
        }

        public String getOverlayType() {
            return overlayType;
        }

        public void setOverlayType(String overlayType) {
            this.overlayType = overlayType;
        }

        public String getGeoFenceLatLangs() {
            return geoFenceLatLangs;
        }

        public void setGeoFenceLatLangs(String geoFenceLatLangs) {
            this.geoFenceLatLangs = geoFenceLatLangs;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getCreatedByUsertype() {
            return createdByUsertype;
        }

        public void setCreatedByUsertype(String createdByUsertype) {
            this.createdByUsertype = createdByUsertype;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
        }

        public String getUpdatedByUsertype() {
            return updatedByUsertype;
        }

        public void setUpdatedByUsertype(String updatedByUsertype) {
            this.updatedByUsertype = updatedByUsertype;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public String getAutoGenerated() {
            return autoGenerated;
        }

        public void setAutoGenerated(String autoGenerated) {
            this.autoGenerated = autoGenerated;
        }

        public String getCreatedPktDateTime() {
            return createdPktDateTime;
        }

        public void setCreatedPktDateTime(String createdPktDateTime) {
            this.createdPktDateTime = createdPktDateTime;
        }

        public String getUpdatedPktDateTime() {
            return updatedPktDateTime;
        }

        public void setUpdatedPktDateTime(String updatedPktDateTime) {
            this.updatedPktDateTime = updatedPktDateTime;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

    }

