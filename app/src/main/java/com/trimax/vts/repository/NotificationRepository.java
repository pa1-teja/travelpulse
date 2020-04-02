package com.trimax.vts.repository;

import com.trimax.vts.api.ApiClient;
import com.trimax.vts.model.notifications.NotificationListData;
import com.trimax.vts.view.complaints.models.DataSaveResponse;
import com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference.NotificationPrefModelResponse;
import com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference.NotificationPrefModelSetPrefResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class NotificationRepository {
    public Call<ResponseBody> changeUserStatus(String recordId,String pid,String token,String UserTypeId, final String userID ,String strStatus){
        return ApiClient.getApiClient().changeUserStatus(recordId,pid,token,UserTypeId,userID,strStatus);
    }

    public Call<ResponseBody> addDevice(String uid, String uidType, String token, String playerId){
        return ApiClient.getApiClient().addDevice(uid,uidType,token,playerId,"GCM","A");
    }

    public Call<NotificationListData> getNotifications(String userTypeId, final String userId, final String playerId, final String deviceId){
        return ApiClient.getApiClient().getNotification(userTypeId,userId,playerId,deviceId);
    }

    public Call<ResponseBody> setNotificationPrefOnLogin(String userTypeId, String playerId, String mobileDeviceId, String customerId){
        return ApiClient.getApiClient().setNotificationPrefOnLogin(userTypeId,playerId,mobileDeviceId,customerId);
    }

    public Call<ResponseBody> getAllNotifications(String userTypeId, final String userId, final String vehicleId, final String notificationType, final String limit , final String start,final String playerId,String deviceId){
        return ApiClient.getApiClient().GetAllNotification(userTypeId,userId,vehicleId,notificationType,limit,start,playerId,deviceId);
    }

    public Call<NotificationPrefModelSetPrefResponse> setNotificationPreference(String userTypeId,String custId,String vehicleId,String data,String uid,String uidType,String token,String playerId ,String pMode,String deviceId){
        return ApiClient.getApiClient().setNotificationPreference(userTypeId,custId,vehicleId,data,uid,uidType,token,playerId,pMode,deviceId);
    }

    public Call<NotificationPrefModelResponse.NotificationData> getNotificationPreference(String userTypeId,String custId,String vehicleId,String playerId,String deviceId){
        return ApiClient.getApiClient().getNotificationPreference(userTypeId,custId,vehicleId,playerId,deviceId);
    }

    public Call<ResponseBody> setParkingModeOnOff(String strUserid, final String userTypeId, final String vehicleId, final String parkig_mode,String playerId, String deviceId){
        return ApiClient.getApiClient().fnUpdateParkingMode(strUserid,userTypeId,vehicleId,parkig_mode,playerId,deviceId);
    }

    public Call<DataSaveResponse> setNotificationLog(String notificationId, String deviceId, String receivedAt){
        return ApiClient.getApiClient().setNotificationReceivedLog(notificationId,deviceId,receivedAt);
    }
}
