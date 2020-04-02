package com.trimax.vts.view.master.model.VehicleGroup.EditGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditGroupModel {
    @SerializedName("vehicles")
    @Expose
    private List<Vehicle> vehicles = null;
    @SerializedName("subusers")
    @Expose
    private List<EditGroupSubUser> subusers = null;
    @SerializedName("selected_vehicles")
    @Expose
    private List<String> selectedVehicles = null;
    @SerializedName("sub_user_id")
    @Expose
    private List<String> subUserId = null;
    @SerializedName("vehicle_group")
    @Expose
    private EditGroupVehicle vehicleGroup;

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<EditGroupSubUser> getSubusers() {
        return subusers;
    }

    public void setSubusers(List<EditGroupSubUser> subusers) {
        this.subusers = subusers;
    }

    public List<String> getSelectedVehicles() {
        return selectedVehicles;
    }

    public void setSelectedVehicles(List<String> selectedVehicles) {
        this.selectedVehicles = selectedVehicles;
    }

    public List<String> getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(List<String> subUserId) {
        this.subUserId = subUserId;
    }

    public EditGroupVehicle getVehicleGroup() {
        return vehicleGroup;
    }

    public void setVehicleGroup(EditGroupVehicle vehicleGroup) {
        this.vehicleGroup = vehicleGroup;
    }
}
