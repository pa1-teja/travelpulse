package com.trimax.vts.view.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class StringClusterItem implements ClusterItem {

    final String title;
    final LatLng latLng;
    String vehicle_no;
    String date;
    String speed;
    String ign;
    String gps;
    String acc;
    String nnw;
    String vid;
    String driver;
    String owner;
    String group;

    String vehicle_type_id;

    public StringClusterItem(String title, LatLng latLng) {
        this.title = title;
        this.latLng = latLng;
    }

    public StringClusterItem(String title, LatLng latLng, String vno, String dates, String speeds, String igns,String gps, String ac, String nw, String vidd, String group, String driver, String owner, String vehicle_type_id) {
        this.title = title;
        this.latLng = latLng;
        vehicle_no = vno;
        speed = speeds;
        acc = ac;
        date = dates;
        ign = igns;
        this.gps=gps;
        nnw = nw;
        vid = vidd;
        this.driver = driver;
        this.group = group;
        this.owner = owner;
        this.vehicle_type_id = vehicle_type_id;


    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getNnw() {
        return nnw;
    }

    public void setNnw(String nnw) {
        this.nnw = nnw;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getIgn() {
        return ign;
    }

    public void setIgn(String ign) {
        this.ign = ign;
    }

    public String getAc() {
        return acc;
    }

    public void setAc(String acc) {
        this.acc = acc;
    }

    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }
}