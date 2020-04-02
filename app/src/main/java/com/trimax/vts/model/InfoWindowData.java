package com.trimax.vts.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class InfoWindowData implements ClusterItem {
    private String VehicleNo;
    private String Location;
    private String Speed;
    private String Ignition;
    private String ac;
    private String vehicle_type_id;
    private LatLng latLng;
    private String gps;


    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    private String latitude;
    private String longitute;

    private String notiType;
    private String notiTypeforMarker;


    public String getNotiTypeforMarker() {
        return notiTypeforMarker;
    }

    public void setNotiTypeforMarker(String notiTypeforMarker) {
        this.notiTypeforMarker = notiTypeforMarker;
    }


    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }


    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    private String Driver;
    private String Owner;
    private String Group;

    public InfoWindowData() {
    }


    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public LatLng getPosition() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String Vehicleno) {
        this.VehicleNo = Vehicleno;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        this.Speed = speed;
    }

    public String getIgnition() {
        return Ignition;
    }

    public void setIgnition(String ign) {
        this.Ignition = ign;
    }
}
