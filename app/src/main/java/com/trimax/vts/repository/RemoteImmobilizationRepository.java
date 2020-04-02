package com.trimax.vts.repository;

import com.trimax.vts.api.ApiClient;
import com.trimax.vts.vehicle_lock.ResponseLockUnlock;
import com.trimax.vts.vehicle_lock.ResponseSentOTP;
import com.trimax.vts.vehicle_lock.ResponseSetTPin;
import com.trimax.vts.vehicle_lock.VehicleLockStatusResponse;

import retrofit2.Call;

public class RemoteImmobilizationRepository {

    public Call<VehicleLockStatusResponse> getVehicleStatus(String vehicleId, String mobileDeviceId, String requestUserId, String requestUserTypeId){
        return ApiClient.getApiClient().getVehicleStatus(vehicleId,mobileDeviceId,requestUserId,requestUserTypeId);
    }

    public Call<ResponseLockUnlock> lockUnlockVehicle(String vehicleId, String tpin, String requestType, String platform, String commandChannel, String mobileDeviceId,
                                                      String requestUserId, String requestUserTypeId){
        return ApiClient.getApiClient().lockUnlockVehicle(vehicleId,tpin,requestType,platform,commandChannel,mobileDeviceId,requestUserId,requestUserTypeId);
    }

    /*
        public Call<ResponseBody> generateTPin(String userId, String userTypeId, String mobileDeviceId){
            return ApiClient.getApiClient().generateTPin(userId,mobileDeviceId,userTypeId);
        }
    */

    public Call<ResponseSetTPin> setTPin(String userId, String userTypeId, String mobileDeviceId, String tpin, String cnfTpin, String otp){
        return ApiClient.getApiClient().setTPin(userId,userTypeId,mobileDeviceId,tpin,cnfTpin,otp);
    }

    public Call<ResponseSentOTP> sendOTP(String requestUserId, String requestUserTypeId, String mobileDeviceId){
        return ApiClient.getApiClient().sendOTP(requestUserId, requestUserTypeId, mobileDeviceId);
    }
}
