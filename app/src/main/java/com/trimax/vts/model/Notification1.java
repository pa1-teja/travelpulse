package com.trimax.vts.model;

public class Notification1 {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getNotification_subtype() {
        return notification_subtype;
    }

    public void setNotification_subtype(String notification_subtype) {
        this.notification_subtype = notification_subtype;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVehicle_lat() {
        return vehicle_lat;
    }

    public void setVehicle_lat(String vehicle_lat) {
        this.vehicle_lat = vehicle_lat;
    }

    public String getVehicle_long() {
        return vehicle_long;
    }

    public void setVehicle_long(String vehicle_long) {
        this.vehicle_long = vehicle_long;
    }

    public String getIgn() {
        return ign;
    }

    public void setIgn(String ign) {
        this.ign = ign;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getSnap_lat() {
        return snap_lat;
    }

    public void setSnap_lat(String snap_lat) {
        this.snap_lat = snap_lat;
    }

    public String getSnap_long() {
        return snap_long;
    }

    public void setSnap_long(String snap_long) {
        this.snap_long = snap_long;
    }

    String id;
    String notification_type;
    String notification_subtype;
    String msg;
    String vehicle_lat;
    String vehicle_long;
    String ign;
    String ac;
    String speed;
    String location;
    String date_time;
    String snap_lat;
    String snap_long;

    public Notification1(String id, String notification_type, String notification_subtype, String msg, String vehicle_lat, String vehicle_long, String ign, String ac, String speed, String location, String date_time, String snap_lat, String snap_long) {
        this.id = id;
        this.notification_type = notification_type;
        this.notification_subtype = notification_subtype;
        this.msg = msg;
        this.vehicle_lat = vehicle_lat;
        this.vehicle_long = vehicle_long;
        this.ign = ign;
        this.ac = ac;
        this.speed = speed;
        this.location = location;
        this.date_time = date_time;
        this.snap_lat = snap_lat;
        this.snap_long = snap_long;
    }







}
