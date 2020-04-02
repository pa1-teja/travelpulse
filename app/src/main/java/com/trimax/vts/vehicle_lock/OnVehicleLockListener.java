package com.trimax.vts.vehicle_lock;

import com.trimax.vts.view.master.model.VehicleMaster.VehicleMasterModel;

public interface OnVehicleLockListener {
    VehicleMasterModel getVehicleDetails();
    void setVehicleDetails();
}
