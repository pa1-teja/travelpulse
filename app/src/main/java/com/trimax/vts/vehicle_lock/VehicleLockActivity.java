package com.trimax.vts.vehicle_lock;

import android.content.Intent;
import android.os.Bundle;

import com.trimax.vts.view.R;
import com.trimax.vts.view.master.activity.BaseActivity;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleMasterModel;

public class VehicleLockActivity extends BaseActivity implements OnVehicleLockListener{
    VehicleMasterModel vehicleMasterModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_lock);

        getSupportActionBar().setTitle(R.string.vehicleLock);
            Bundle data = getIntent().getExtras();
            vehicleMasterModel = (VehicleMasterModel) data.getSerializable("selectedVehicle");
            if(null != vehicleMasterModel) {
                assert vehicleMasterModel != null;
                String temp = vehicleMasterModel.getMake();
            }
    }

    @Override
    public VehicleMasterModel getVehicleDetails() {
        return vehicleMasterModel;
    }

    @Override
    public void setVehicleDetails() {

    }
}
