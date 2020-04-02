package com.trimax.vts.view.master.callbacks;

public interface AddGroupCallback {
    void updateVehiclesHashmap(String key, Boolean value);
    void updateUsersHashmap(String key, Boolean value);
    void onOkClick(boolean isVehicle);
}
