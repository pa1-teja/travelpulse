package com.trimax.vts.model;

public class VehiclePojo {

    String vehicle_no;

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public boolean isSelected() {
        return isSelected;
    }


    String vehicle_id;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

}
