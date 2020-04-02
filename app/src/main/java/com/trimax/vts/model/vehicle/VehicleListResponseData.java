package com.trimax.vts.model.vehicle;

import java.io.Serializable;

public class VehicleListResponseData implements Serializable {
    public int id;
    public String regnNo;
    public String make;
    public String model;
    public Integer TrackerId;
    public String TrackerNo;
    public String name;
    public String vehicle_name;
    public String tracker_imei_no;
    public String vehicle_no ;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

}
