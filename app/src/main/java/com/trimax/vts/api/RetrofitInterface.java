package com.trimax.vts.api;

import com.trimax.vts.model.notifications.NotificationListData;
import com.trimax.vts.vehicle_lock.ResponseLockUnlock;
import com.trimax.vts.vehicle_lock.ResponseSentOTP;
import com.trimax.vts.vehicle_lock.ResponseSetTPin;
import com.trimax.vts.view.complaints.models.CommentResponse;
import com.trimax.vts.view.complaints.models.DataSaveResponse;
import com.trimax.vts.view.complaints.models.TicketTypeResponse;
import com.trimax.vts.view.complaints.models.TicketsResponse;
import com.trimax.vts.vehicle_lock.VehicleLockStatusResponse;
import com.trimax.vts.view.login.model.LoginCheckResponse;
import com.trimax.vts.view.login.model.LoginResponse;
import com.trimax.vts.view.login.OTPResponse;
import com.trimax.vts.view.master.model.driver.DriverModelResponse;
import com.trimax.vts.view.master.model.FleetSubUsers.FleetSubUsersModelResponse;
import com.trimax.vts.view.master.model.VehicleGroup.EditGroup.EditGroupModelResponse;
import com.trimax.vts.view.master.model.VehicleGroup.FleetUsers.UsersForAddGroupModelResponse;
import com.trimax.vts.view.master.model.VehicleGroup.VehicleGroupModelResponse;
import com.trimax.vts.view.master.model.VehicleGroup.VehiclesList.VehiclesForAddGroupModelResponse;
import com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference.NotificationPrefModelResponse;
import com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference.NotificationPrefModelSetPrefResponse;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleMasterModelResponse;
import com.trimax.vts.view.master.model.VehicleMaster.VehicleProfileResponse;
import com.trimax.vts.view.master.model.documents.UploadDocumentListResponse;
import com.trimax.vts.view.master.model.documents.UploadDocumentResponse;
import com.trimax.vts.view.master.model.documents.UploadDocumentTypeResponse;
import com.trimax.vts.model.GeoFenceStatus;
import com.trimax.vts.model.TravelPerformaceResponse;
import com.trimax.vts.model.vehicle.VehicleResponse;
import com.trimax.vts.view.model.GeofenceResponse;
import com.trimax.vts.view.model.replay.ReplayResponse;
import com.trimax.vts.view.model.route.RouteResponse;
import com.trimax.vts.view.notifications.send.SendNotification;
import com.trimax.vts.view.reports.AlertReportResponse;
import com.trimax.vts.view.reports.ReportDetailResponse;
import com.trimax.vts.view.reports.ReportResponse;
import com.trimax.vts.view.vas.VASProviderModelResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


/**
 * Created by genuus on 31/1/18.
 */

public interface RetrofitInterface {

    //Send dummy notification using onesignal
    @Headers("Content-Type: application/json")
    @POST("api/v1/notifications")
    Call<ResponseBody> sendNotification(/*@Header("Content-Type") String contentType,*/ @Body SendNotification body);

    //get token
    @FormUrlEncoded
    @POST(ApiConstant.URLNotification)
    Call<ResponseBody> addDevice(@Field("uid") String uid,
                                 @Field("uid_type") String uid_type,
                                 @Field("token") String token,
                                 @Field("player_id") String player_id,
                                 @Field("delivery_system") String delivery_system,
                                 @Field("status") String status);

    //Add device
    @FormUrlEncoded
    @POST(ApiConstant.URLAddDevice)
    Call<ResponseBody> changeUserStatus(/*@Header("Authorization") String strAuthorizationKey,
                                        @Header("X-API-KEY") String strApiKey,*/
                                        @Field("device_id") String device_id,
                                        @Field("player_id") String playerid,
                                        @Field("token") String token,
                                        @Field("user_type_id") String user_type_id,
                                        @Field("user_id") String user_id,
                                        @Field("status") String status);

    @FormUrlEncoded
    @POST(ApiConstant.UrlChechIflogin)
    Call<LoginCheckResponse> ChechIflogin(@Field("device_id") String deviceId,
                                          @Field("player_id") String playerId,
                                          @Field("token") String token,
                                          @Field("user_id") String password,
                                          @Field("user_type_id") String status);

    //LoginReq Structure
    @FormUrlEncoded
    @POST(ApiConstant.UrlLogin)
    Call<LoginResponse> fnLogin(/*@Header("Authorization") String strAuthorizationKey,
                               @Header("X-API-KEY") String strApiKey,*/
                               @Header("accept") String type,
                               @Field("emailaddress") String strEmail,
                               @Field("password") String strPassword,
                               @Field("status") String status);
    //LoginReq Structure
    @FormUrlEncoded
    @POST(ApiConstant.UrlForgot)
    Call<ResponseBody> fnForgotPassword(@Header("Authorization") String strAuthorizationKey,
                               @Header("X-API-KEY") String strApiKey,
                               @Field("email") String strEmail);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetRealTimeTrackingLive)
    Call<ResponseBody> fnGetRealTimeData(@Header("Authorization") String strAuthorizationKey,
                                         @Header("X-API-KEY") String strApiKey,
                                         @Field("imeinumber") String strEmail,
                                         @Field("from_date") String strFromDate, @Field("to_date") String strTodate);

    /*real time traking  */
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetRealTimeTrackingDeviceLive)
    Call<GeofenceResponse> fnGetRealTimeDataDevice(@Header("Authorization") String strAuthorizationKey, @Header("X-API-KEY") String strApiKey,
                                                   @Field("user_type_id") String user_type_id,
                                                   @Field("user_id") String user_id,
                                                   @Field("is_first_req") String is_first_req,
                                                   @Field("vehicle_ID") String vehicle_ID,
                                                   @Field("player_id") String player_id,
                                                   @Field("device_id") String device_id);

    /*replay*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetRealTimeTrackingLive)
    Call<ReplayResponse> fnGetRealTimeVehicleReplayDataDevice(@Header("Authorization") String strAuthorizationKey,
                                                              @Header("X-API-KEY") String strApiKey,
                                                              @Field("from_date") String from_date,
                                                              @Field("to_date") String toDate,
                                                              @Field("user_type_id") String iser_type_id,
                                                              @Field("user_id") String user_id,
                                                              @Field("vehicle_id") String vehicle_id);


    @GET
    Call<RouteResponse> getLatLangFromHere(/*@Header("Content-Type") String content_type,*/@Url String url);

    /*@POST(ApiConstant.matchRouteApi)
    //@FormUrlEncoded
    Call<RouteResponse> getLatLangFromHere(@Header("Content-Type") String content_type,
                                           @Body String data);*/

    /*markVehicle*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetgetvehiclecmarked)
    Call<ResponseBody> fnMarkedVehicle(@Header("Authorization") String strAuthorizationKey,
                                       @Header("X-API-KEY") String strApiKey,
                                       @Field("user_type_id") String user_type_id,
                                       @Field("user_id") String user_id,
                                       @Field("vehicle_id") String vehicle_idd);
    /*markVehicle*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetgetvehiclecPerformace)
    Call<TravelPerformaceResponse> fngetTravelperformace(@Header("Authorization") String strAuthorizationKey,
                                                       @Header("X-API-KEY") String strApiKey,
                                                       @Field("user_type_id") String user_type_id,
                                                       @Field("user_id") String user_id,
                                                       @Field("vehicle_id") String vehicle_idd);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetgetDashboardDetails)
    Call<ResponseBody> current_vehicle_status_dashboard(@Header("Authorization") String strAuthorizationKey,
                                                        @Header("X-API-KEY") String strApiKey,
                                                        @Field("user_type_id") String user_type_id,
                                                        @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(ApiConstant.notificationtype)
    Call<ResponseBody> getNotificationType(@Header("Authorization") String strAuthorizationKey,
                                           @Header("X-API-KEY") String strApiKey,
                                           @Field("user_type_id") String user_type_id,
                                           @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(ApiConstant.notification)
    Call<NotificationListData> getNotification(@Field("user_type_id") String user_type_id,
                                       @Field("user_id") String user_id,
                                       @Field("player_id") String player_id,
                                       @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetAllNotification)
    Call<ResponseBody> GetAllNotification(@Field("user_type_id") String user_type_id,
                                          @Field("user_id") String user_id,
                                          @Field("vehicle_id") String vehicle_id,
                                          @Field("notification_type_id") String notification_type_id,
                                          @Field("limit") String limit,
                                          @Field("start") String start,
                                          @Field("player_id") String player_id,
                                          @Field("device_id") String device_id);

    //Parking Mode
    @FormUrlEncoded
    @POST(ApiConstant.UrlParkedOut)
    Call<ResponseBody> fnUpdateParkingMode(@Field("user_id") String user_id,
                                           @Field("user_type_id") String user_type_id,
                                           @Field("vehicle_id") String vehicle_id,
                                           @Field("parking_mode") String parking_mode,
                                           @Field("player_id") String player_id,
                                           @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetgetindivichuldetails)
    Call<ResponseBody> current_vehicle_status_indivichul(@Header("Authorization") String strAuthorizationKey,
                                                         @Header("X-API-KEY") String strApiKey,
                                                         @Field("user_type_id") String user_type_id,
                                                         @Field("user_id") String user_id,
                                                         @Field("status_type") String type);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetgetvehicledetails)
    Call<ResponseBody> current_vehicle_details(@Header("Authorization") String strAuthorizationKey,
                                               @Header("X-API-KEY") String strApiKey,
                                               @Field("user_type_id") String user_type_id,
                                               @Field("user_id") String user_id,
                                               @Field("mobile_device_id") String device_id);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVehicles)
    Call<VehicleResponse> getVehicles(@Header("Authorization") String strAuthorizationKey,
                                      @Header("X-API-KEY") String strApiKey,
                                      @Field("user_type_id") String user_type_id,
                                      @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(ApiConstant.UrlGetgetvehiclecount)
    Call<ResponseBody> current_vehicle_count(@Header("Authorization") String strAuthorizationKey,
                                             @Header("X-API-KEY") String strApiKey,
                                             @Field("user_type_id") String user_type_id,
                                             @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(ApiConstant.UrlchangePAssword)
    Call<ResponseBody> chnagePassword(@Header("Authorization") String strAuthorizationKey,
                                      @Header("X-API-KEY") String strApiKey,
                                      @Field("id") String user_type_id,
                                      @Field("oldPassword") String oldPassword,
                                      @Field("newPassword") String newPassword,
                                      @Field("cnfPassword") String cnfPassword);


    /*Alert Report*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetAlertReport)
    Call<ResponseBody> getAlertReport(@Header("Authorization") String strAuthorizationKey, @Header("X-API-KEY") String strApiKey,
                                      @Field("user_type_id") String user_type_id,
                                      @Field("user_id") String user_id,
                                      @Field("fromdate") String from_date,
                                      @Field("todate") String to_date,
                                      @Field("select_vehicles_group") String select_vehicles_group,
                                      @Field("select_vehicles") String select_vehicles,
                                      @Field("select_alert") String select_alert);


    /*Vehicle Stop  Report*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVehicleStopreport)
    Call<ResponseBody> getVehicleStopReport(@Header("Authorization") String strAuthorizationKey, @Header("X-API-KEY") String strApiKey,
                                            @Field("user_type_id") String user_type_id, @Field("user_id") String user_id,
                                            @Field("fromdate") String from_date,
                                            @Field("todate") String to_date,
                                            @Field("report_type") String report_type,
                                            @Field("chart_type") String chart_type,
                                            @Field("select_vehicles_group") String select_vehicles_group,
                                            @Field("select_vehicles") String select_vehicles);

    /*Vehicle Run  Report*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVehicleRunreport)
    Call<ResponseBody> getVehicleRunReport(@Header("Authorization") String strAuthorizationKey,
                                           @Header("X-API-KEY") String strApiKey,
                                           @Field("user_type_id") String user_type_id,
                                           @Field("user_id") String user_id,
                                           @Field("fromdate") String from_date,
                                           @Field("todate") String to_date,
                                           @Field("report_type") String report_type,
                                           @Field("chart_type") String chart_type,
                                           @Field("select_vehicles_group") String select_vehicles_group,
                                           @Field("select_vehicles") String select_vehicles);

    /*Vehicle IDLe  Report*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVehicleIdleeport)
    Call<ResponseBody> getVehicleIdleReport(@Header("Authorization") String strAuthorizationKey,
                                            @Header("X-API-KEY") String strApiKey,
                                            @Field("user_type_id") String user_type_id,
                                            @Field("user_id") String user_id,
                                            @Field("fromdate") String from_date,
                                            @Field("todate") String to_date,
                                            @Field("report_type") String report_type,
                                            @Field("chart_type") String chart_type,
                                            @Field("select_vehicles_group") String select_vehicles_group,
                                            @Field("select_vehicles") String select_vehicles);

    /* Alert Summary Report*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetAlertAlertsummaryreport)
    Call<ResponseBody> getGetAlertsummaryreport(@Header("Authorization") String strAuthorizationKey,
                                                @Header("X-API-KEY") String strApiKey,
                                                @Field("user_type_id") String user_type_id,
                                                @Field("user_id") String user_id,
                                                @Field("fromdate") String from_date,
                                                @Field("todate") String to_date,
                                                @Field("select_vehicles") String select_vehicles,
                                                @Field("select_vehicles_group") String select_vehicles_group);

    /* Vehicle Summary Report*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVehiclesummaryreport)
    Call<ResponseBody> getGetVehiclesummaryreport(@Header("Authorization") String strAuthorizationKey,
                                                  @Header("X-API-KEY") String strApiKey,
                                                  @Field("user_type_id") String user_type_id,
                                                  @Field("user_id") String user_id,
                                                  @Field("fromdate") String from_date,
                                                  @Field("todate") String to_date,
                                                  @Field("select_vehicles_group") String select_vehicles_group,
                                                  @Field("select_vehicles") String select_vehicles);

    /*CurrentVehiclestatusreport Report*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVehiclestatusreport)
    Call<ResponseBody> GetVehiclestatusreport(@Header("Authorization") String strAuthorizationKey,
                                              @Header("X-API-KEY") String strApiKey,
                                              @Field("user_type_id") String user_type_id,
                                              @Field("user_id") String user_id,
                                              @Field("select_vehicles_group") String select_vehicles_group,
                                              @Field("select_vehicles") String select_vehicles,
                                              @Field("select_vehicle_status") String select_vehicle_status);

    /*vehicle group*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetAllVehiclegroup)
    Call<ResponseBody> getAllGroup(@Header("Authorization") String strAuthorizationKey,
                                   @Header("X-API-KEY") String strApiKey,
                                   @Field("user_type_id") String user_type_id,
                                   @Field("customerid") String user_id);

    /*vehicle group Vehicle*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetGroupvehicle)
    Call<ResponseBody> getGropWiseVehicle(@Header("Authorization") String strAuthorizationKey, @Header("X-API-KEY") String strApiKey, @Field("group_id") String group_id);


    /*Reports*/
    @FormUrlEncoded
    @POST(ApiConstant.urlReportData)
    Call<ReportResponse> getStatuReport(@Field("user_id") String userId, @Field("user_type_id") String userTypeId,
                                        @Field("vehicle_id")String vehicleId, @Field("fordate")String forDate);

    @FormUrlEncoded
    @POST(ApiConstant.urlReportDetailData)
    Call<ReportDetailResponse> getDetailStatuReport(@Field("user_id") String userId, @Field("vehicle_id")String vehicleId,
                                                             @Field("fordate")String forDate, @Field("status") String status);

    @FormUrlEncoded
    @POST(ApiConstant.urlReportDetailNotification)
    Call<AlertReportResponse> getDetailNotificationReport(@Field("user_id") String userId, @Field("vehicle_id")String vehicleId,
                                                          @Field("fordate")String forDate);



    /*Manage masters api: Added by Apeksha Gunjal*/
    @FormUrlEncoded
    @POST(ApiConstant.UrlGetAllDrivers)
    Call<DriverModelResponse> fnGetAllDriverDetails(@Header("Authorization") String strAuthorizationKey,
                                                    @Header("X-API-KEY") String strApiKey,
                                                    @Field("id") String strPassword,
                                                    @Field("user_type_id") String strEmail);

    @FormUrlEncoded
    @POST(ApiConstant.UrlRemoveDriver)
    Call<ResponseBody> fnRemoveDriver(@Header("Authorization") String strAuthorizationKey,
                                      @Header("X-API-KEY") String strApiKey,
                                      @Field("id") String id,
                                      @Field("user_type_id") String userTypeId,
                                      @Field("driver_id") String driverId);

    @Multipart
    @POST(ApiConstant.UrlUpdateDriver)
    Call<ResponseBody> fnUpdateDriver(@Header("Authorization") String strAuthorizationKey,
                                      @Header("X-API-KEY") String strApiKey,
                                      @Part("id") RequestBody id,
                                      @Part("user_type_id") RequestBody userTypeId,
                                      @Part("name") RequestBody name,
                                      @Part("license_number") RequestBody liecenseNumber,
                                      @Part("license_expiry") RequestBody liecenseExpiry,
                                      @Part("mobile_no") RequestBody mobileNumber,
                                      @Part("driver_id") RequestBody driverId,
                                      @Part("description") RequestBody description,
                                      @Part MultipartBody.Part file);
    @FormUrlEncoded
    @POST(ApiConstant.UrlEditDriver)
    Call<ResponseBody> fnEditDriver(@Header("Authorization") String strAuthorizationKey,
                                    @Header("X-API-KEY") String strApiKey,
                                    @Field("id") String id,
                                    @Field("user_type_id") String userTypeId,
                                    @Field("driver_id") String driverId);


    @Multipart
    @POST(ApiConstant.UrlAddDriver)
    Call<ResponseBody> fnAddDriver(@Header("Authorization") String strAuthorizationKey,
                                        @Header("X-API-KEY") String strApiKey,
                                        @Part("id") RequestBody id,
                                        @Part("user_type_id") RequestBody userTypeId,
                                        @Part("name") RequestBody name,
                                        @Part("license_number") RequestBody liecenseNumber,
                                        @Part("license_expiry") RequestBody liecenseExpiry,
                                        @Part("mobile_no") RequestBody mobileNumber,
                                        @Part("description") RequestBody description,
                                        @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVehicleList)
    Call<VehicleMasterModelResponse> fnGetAllVehicleList(@Header("Authorization") String strAuthorizationKey,
                                                         @Header("X-API-KEY") String strApiKey,
                                                         @Field("user_id") String userId,
                                                         @Field("user_type_id") String userTypeId,
                                                         @Field("mobile_device_id") String device_id);

    @FormUrlEncoded
    @POST(ApiConstant.GetListOfUploadDocument)
    Call<UploadDocumentListResponse> fnGetAllDocumentList(@Header("Authorization") String strAuthorizationKey,
                                                          @Header("X-API-KEY") String strApiKey,
                                                          @Field("vehicle_id") String userId);

    @FormUrlEncoded
    @POST(ApiConstant.GetListOfInsurancecompany)
    Call<UploadDocumentTypeResponse> fnGetAllInsuCompanyList(@Header("Authorization") String strAuthorizationKey,
                                                             @Header("X-API-KEY") String strApiKey,
                                                             @Field("vehicle_id") String userId);
    @Multipart
    @POST(ApiConstant.UploadDocument)
    Call<UploadDocumentResponse> fnUploadDocument(@Header("Authorization") String strAuthorizationKey,
                                                  @Header("X-API-KEY") String strApiKey,
                                                  // @Header("Content-Type") String contentType,
                                                  @Part("user_id") RequestBody user_id,
                                                  @Part("user_type_id") RequestBody user_type_id,
                                                  @Part("vehicle_id") RequestBody vehicle_id,
                                                  @Part("document_type") RequestBody document_type,
                                                  @Part("description") RequestBody description,
                                                  @Part("insurance_amount") RequestBody insurance_amount,
                                                  @Part("expiry_date") RequestBody expiry_date,
                                                  @Part("insurance_company") RequestBody insurance_company,
                                                  @Part("insurance_policy_no") RequestBody insurance_policy_no,
                                                  @Part MultipartBody.Part file);




    @FormUrlEncoded
    @POST(ApiConstant.UrlRetriveVehicleProfile)
    Call<VehicleProfileResponse> fnRetriveVehicleProfile(@Header("Authorization") String strAuthorizationKey,
                                                         @Header("X-API-KEY") String strApiKey,
                                                         @Field("vehicle_id") String vehicleId);

    @FormUrlEncoded
    @POST(ApiConstant.UrlUpdateVehicleProfile)
    Call<ResponseBody> fnUpdateVehicleProfile(@Header("Authorization") String strAuthorizationKey,
                                              @Header("X-API-KEY") String strApiKey,
                                              @Field("user_type_id") String userTypeId,
                                              @Field("user_id") String userId,
                                              @Field("vehicle_id") String vehicleId,
                                              @Field("default_driver_id") String driverId,
                                              @Field("equipmentType") String equipmentType,
                                              @Field("speedLimit") String speedLimit,
                                              @Field("chassis_no") String chassisNo,
                                              @Field("make") String make,
                                              @Field("model") String model,
                                              @Field("yr_of_manufacture") String yrOfManufacture,
                                              @Field("color") String color,
                                              @Field("fuel_type") String fuelType,
                                              @Field("first_owner") String firstOwner,
                                              @Field("insurance_company") String insuranceCompany,
                                              @Field("insurance_policy_no") String insurancePolicyNo,
                                              @Field("insurance_expiry_date") String insuranceExpiryDate);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVehicleGroupList)
//"customerid , user_type_id
    Call<VehicleGroupModelResponse> fnGetVehicleGroupList(@Header("Authorization") String strAuthorizationKey,
                                                          @Header("X-API-KEY") String strApiKey,
                                                          @Field("customerid") String customerId,
                                                          @Field("user_type_id") String userTypeId);

    @FormUrlEncoded
    @POST(ApiConstant.UrlRemoveGroup)
    Call<ResponseBody> fnRemoveGroup(@Header("Authorization") String strAuthorizationKey,
                                     @Header("X-API-KEY") String strApiKey,
                                     @Field("user_type_id") String userTypeId,
                                     @Field("user_id") String userId,
                                     @Field("vehicle_group_id") String vehicleGroupId);

    @FormUrlEncoded
    @POST(ApiConstant.UrlUpdateVehicleGroup)
    Call<ResponseBody> fnUpdateVehicleGroup(@Header("Authorization") String strAuthorizationKey,
                                            @Header("X-API-KEY") String strApiKey,
                                            @Field("user_type_id") String userTypeId,
                                            @Field("customerid") String customerId,
                                            @Field("vehicle_id") String vehicleId,
                                            @Field("subusers") String subusers,
                                            @Field("title") String title,
                                            @Field("vehicle_group_id") String vehicleGroupId);

    @FormUrlEncoded
    @POST(ApiConstant.UrlAddVehicleGroup)
    Call<ResponseBody> fnAddGroup(@Header("Authorization") String strAuthorizationKey,
                                  @Header("X-API-KEY") String strApiKey,
                                  @Field("user_type_id") String userTypeId,
                                  @Field("customerid") String customerId,
                                  @Field("vehicle_id") String vehicleId,
                                  @Field("subusers") String subusers,
                                  @Field("title") String title);

    @FormUrlEncoded
    @POST(ApiConstant.UrlVehicleListForAddGroup)
    Call<VehiclesForAddGroupModelResponse> fnGetVehicleListForAddGroup(@Header("Authorization") String strAuthorizationKey,
                                                                       @Header("X-API-KEY") String strApiKey,
                                                                       @Field("user_type_id") String userTypeId,
                                                                       @Field("customerid") String customerId);

    @FormUrlEncoded
    @POST(ApiConstant.UrlFleetSubUsersListForAddGroup)
    Call<UsersForAddGroupModelResponse> fnGetFleetSubUsersListForAddGroup(@Header("Authorization") String strAuthorizationKey,
                                                                          @Header("X-API-KEY") String strApiKey,
                                                                          @Field("user_type_id") String userTypeId,
                                                                          @Field("customerid") String customerId);


    @FormUrlEncoded
    @POST(ApiConstant.UrlEditGroup)
    Call<EditGroupModelResponse> fnEditGroup(@Header("Authorization") String strAuthorizationKey,
                                             @Header("X-API-KEY") String strApiKey,
                                             @Field("user_type_id") String userTypeId,
                                             @Field("id") String id,
                                             @Field("vehicle_group_id") String vehicleGroupId);


    @FormUrlEncoded
    @POST(ApiConstant.UrlRemoveSubUser)
    Call<ResponseBody> fnRemoveSubUser(@Header("Authorization") String strAuthorizationKey,
                                       @Header("X-API-KEY") String strApiKey,
                                       @Field("user_type_id") String userTypeId,
                                       @Field("user_id") String userId,
                                       @Field("subuser_id") String subUserId);

    @FormUrlEncoded
    @POST(ApiConstant.UrlAddSubUser)
    Call<ResponseBody> fnAddSubUser(@Header("Authorization") String strAuthorizationKey,
                                    @Header("X-API-KEY") String strApiKey,
                                    @Field("user_type_id") String userTypeId,
                                    @Field("id") String id,
                                    @Field("first_name") String firstName,
                                    @Field("last_name") String lastName,
                                    @Field("mobile_no") String mobile,
                                    @Field("email") String email);


    @FormUrlEncoded
    @POST(ApiConstant.UrlUpdateSubUser)
    Call<ResponseBody> fnUpdateSubUser(@Header("Authorization") String strAuthorizationKey,
                                       @Header("X-API-KEY") String strApiKey,
                                       @Field("user_type_id") String userTypeId,
                                       @Field("id") String id,
                                       @Field("first_name") String firstName,
                                       @Field("last_name") String lastName,
                                       @Field("mobile_no") String mobile,
                                       @Field("email") String email,
                                       @Field("status") String status,
                                       @Field("subuser_id") String subUserId);
    @FormUrlEncoded
    @POST(ApiConstant.UrlSetNotificationPreference)
    Call<NotificationPrefModelSetPrefResponse> setNotificationPreference(@Field("user_type_id") String userTypeId,
                                                                         @Field("user_id") String id,
                                                                         @Field("vehicle_id") String vehicleId,//213
                                                                         @Field("notification_types_id") String notificationTypesId,
                                                                         @Field("uid") String uid,
                                                                         @Field("uid_type") String uid_type,
                                                                         @Field("token") String token,
                                                                         @Field("player_id") String player_id ,
                                                                         @Field("parking_mode") String p_mode,
                                                                         @Field("device_id") String d_id);//14,0|13,1|15,0|16,0|17,1|18,0


    @FormUrlEncoded
    @POST(ApiConstant.UrlSetNotificationPrefOnLogin)
    Call<ResponseBody> setNotificationPrefOnLogin(@Field("user_type_id") String userTypeId,
                                                  @Field("player_id") String playerId,
                                                  @Field("device_id") String deviceId,
                                                  @Field("user_id") String userId);


    @FormUrlEncoded
    @POST(ApiConstant.UrlGetNotificationPreference)
    Call<NotificationPrefModelResponse.NotificationData> getNotificationPreference(@Field("user_type_id") String usetTypeId,
                                                                                   @Field("user_id") String userId,
                                                                                   @Field("vehicle_id") String vehicleId ,
                                                                                   @Field("player_id") String player_id,
                                                                                   @Field("device_id") String device_id);

    @POST(ApiConstant.UrlSetNotificationReceivedLog)
    @FormUrlEncoded
    Call<DataSaveResponse> setNotificationReceivedLog(@Field("notification_id") String notificationId,
                                                      @Field("mobile_device_id") String deviceId,
                                                      @Field("updated_on") String receivedAt);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetAllFleetUsers)
    Call<FleetSubUsersModelResponse> fnGetAllFeeltUserDetails(@Header("Authorization") String strAuthorizationKey,
                                                              @Header("X-API-KEY") String strApiKey,
                                                              @Field("id") String strPassword,
                                                              @Field("user_type_id") String strEmail);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVASProviders)
    Call<VASProviderModelResponse> fnGetVASProvidersWithAdvancedSearch(@Header("Authorization") String strAuthorizationKey,
                                                                       @Header("X-API-KEY") String strApiKey,
                                                                       @Field("user_id") String userId,
                                                                       @Field("user_type_id") String userTypeId,
                                                                       @Field("search_type") String searchType,//(NORMAL OR ADVANCED)
                                                                       @Field("service_code") String serviceCode,//(TIREREPAIR OR FUEL OR CARTOWING)
                                                                       @Field("latitude") String lattitude,
                                                                       @Field("longitude") String longitude,
                                                                       @Field("use_current_location") String useCurrentLocation,//(Y OR N)
                                                                       @Field("area") String area,
                                                                       @Field("search_radius") String searchRadius,//(number unit km)
                                                                       @Field("eta") String eta,//(number unit minutes)
                                                                       @Field("outlet_name") String outletName,
                                                                       @Field("cost") String cost /*(number unit rupees)*/);

    @FormUrlEncoded
    @POST(ApiConstant.UrlGetVASProviders)
    Call<VASProviderModelResponse> fnGetVASProvidersWithNormalSearch(@Header("Authorization") String strAuthorizationKey,
                                                                     @Header("X-API-KEY") String strApiKey,
                                                                     @Field("user_id") String userId,
                                                                     @Field("user_type_id") String userTypeId,
                                                                     @Field("search_type") String searchType,//(NORMAL OR ADVANCED)
                                                                     @Field("service_code") String serviceCode//(TIREREPAIR OR FUEL OR CARTOWING)
    );


    /* GeoFences API*/
    @FormUrlEncoded
    @POST(ApiConstant.URLGetAllGeoFenceByVehicle)
    Call<GeoFenceStatus> getAllGeoFenceByVehicle(@Header("Authorization") String strAuthorizationKey,
                                                 @Header("X-API-KEY") String strApiKey,
                                                 @Field("vehicle_ID") String user_type_id);


    @FormUrlEncoded
    @POST(ApiConstant.URLSaveAllGeoFenceByVehicle)
    Call<ResponseBody> SaveAllGeoFenceByVehicle(@Header("Authorization") String strAuthorizationKey, @Header("X-API-KEY") String strApiKey,
                                                @Field("user_type_id") String user_type_id,
                                                @Field("user_id") String user_id,
                                                @Field("vehicle_ID") String vehicle_ID,
                                                @Field("map_center_lat") String map_center_lat,
                                                @Field("map_center_lang") String map_center_lang,
                                                @Field("map_zoom") String map_zoom,
                                                @Field("geo_fence_lat_langs") String geo_fence_lat_langs,
                                                @Field("drawtype") String drawtype,
                                                @Field("geofencename") String geofencename,
                                                @Field("status") String status);
    @FormUrlEncoded
    @POST(ApiConstant.URLUpdateGeoFenceByid)
    Call<ResponseBody> UpdateGeoFenceById(@Header("Authorization") String strAuthorizationKey, @Header("X-API-KEY") String strApiKey,
                                                @Field("user_type_id") String user_type_id,
                                                @Field("user_id") String user_id,
                                                @Field("geofenceid") String vehicle_ID,
                                                @Field("map_center_lat") String map_center_lat,
                                                @Field("map_center_lang") String map_center_lang,
                                                @Field("map_zoom") String map_zoom,
                                                @Field("geo_fence_lat_langs") String geo_fence_lat_langs,
                                                @Field("drawtype") String drawtype,
                                                @Field("geofencename") String geofencename,
                                                @Field("status") String status);


    @FormUrlEncoded
    @POST(ApiConstant.URLUpdateGeoFenceByVehicle)
    Call<ResponseBody> UpdateGeoFenceNameByVehicle(@Header("Authorization") String strAuthorizationKey,
                                                   @Header("X-API-KEY") String strApiKey,
                                                   @Field("geofenceid") String geofenceid,
                                                   @Field("geofencename") String geofencename);

    @FormUrlEncoded
    @POST(ApiConstant.URLDeleteGeoFenceByVehicle)
    Call<ResponseBody> DeleteGeoFenceByVehicle(@Header("Authorization") String strAuthorizationKey,
                                               @Header("X-API-KEY") String strApiKey,
                                               @Field("geofenceid") String geofenceid,
                                               @Field("vehicle_ID") String vehicle_ID);



    @POST(ApiConstant.urlPlacesApiError)
    @FormUrlEncoded
    Call<ResponseBody> uploadNearByPlacesApiError(@Header("Authorization") String strAuthorizationKey,
                                                  @Header("X-API-KEY") String strApiKey,
                                                  @Field("input") String error,
                                                  @Field("api")String api,
                                                  @Field("platform")String platform,
                                                  @Field("app_version")String app_version);



    /*Tickets*/
    @POST(ApiConstant.urlTicketType)
    Call<TicketTypeResponse> getTicketTypes();

    @POST(ApiConstant.urlSaveTicket)
    @FormUrlEncoded
    Call<DataSaveResponse> saveTicket(@Field("customer_id") String customerId, @Field("vehicle_id") String vehicleId,
                                      @Field("ticket_type") String ticketTypeId, @Field("issue_date") String issueDate,
                                      @Field("issue_source") String issueSource, @Field("mobile_number") String mobile,
                                      @Field("mobile_model")String mobileModel, @Field("application_version") String applicationVersion,
                                      @Field("problem_faced") String problem,@Field("mobile_os_version")String osVersion);

    @POST(ApiConstant.urlTicketComment)
    @FormUrlEncoded
    Call<DataSaveResponse> saveTicketComment(@Field("customer_id") String customerId, @Field("ticket_id") String ticketId,
                                      @Field("comment") String comment,@Field("ticket_status") String ticketStatus,
                                      @Field("parent_comment_id") String parentCommentId);

    @POST(ApiConstant.urlGetTicket)
    @FormUrlEncoded
    Call<TicketsResponse> getTickets(@Field("customer_id") String customerId);

    @POST(ApiConstant.urlGetTicketComment)
    @FormUrlEncoded
    Call<CommentResponse> getTicketComments(@Field("ticket_id") String ticketId);


    /*Remote Immobilization*/

    @FormUrlEncoded
    @POST(ApiConstant.urlTestVehicleStatus)
    Call<VehicleLockStatusResponse> getVehicleStatus(@Field("vehicle_id") String vehicleId,
                                                     @Field("mobile_device_id") String mobileDeviceId,
                                                     @Field("request_user_id") String requestUserId,
                                                     @Field("request_user_type_id") String requestUserTypeId);


    @FormUrlEncoded
    @POST(ApiConstant.urlVehicleLockUnlock)
    Call<ResponseLockUnlock> lockUnlockVehicle(@Field("vehicle_id") String vehicleId,
                                               @Field("tpin") String tpin,
                                               @Field("request_type") String requestType,
                                               @Field("request_platform") String platform, //android
                                               @Field("command_channel") String commandChannel, //GPRS
                                               @Field("mobile_device_id") String mobileDeviceId,
                                               @Field("request_user_id") String requestUserId,
                                               @Field("request_user_type_id") String requestUserTypeId);

    @FormUrlEncoded
    @POST(ApiConstant.urlTPinGenerate)
    Call<ResponseBody> generateTPin(@Field("user_id") String userId,
                                        @Field("mobile_device_id") String mobileDeviceId,
                                        @Field("user_type_id") String userTypeId);

    @POST(ApiConstant.urlPlacesApiError)
    @FormUrlEncoded
    Call<OTPResponse> sendOTP(@Field("mobile") String mobile);

    @POST(ApiConstant.urlPlacesApiError)
    @FormUrlEncoded
    Call<OTPResponse> verifyOTP(@Field("mobile") String mobile,@Field("otp") String otp);

    @GET
    Call<ResponseBody> callToServer(@Url String url);


    @POST(ApiConstant.urlSendOTP)
    @FormUrlEncoded
    Call<ResponseSentOTP> sendOTP(
            @Field("user_id") String requestUserId,
            @Field("user_type_id") String requestUserTypeId,
            @Field("mobile_device_id") String mobileDeviceId);


    @POST(ApiConstant.urlSetTPin)
    @FormUrlEncoded
    Call<ResponseSetTPin> setTPin(
            @Field("request_user_id") String requestUserId,
            @Field("request_user_type_id") String requestUserTypeId,
            @Field("mobile_device_id") String mobileDeviceId,
            @Field("tpin") String tpin,
            @Field("cnf_tpin") String cnfTpin,
            @Field("otp") String otp);
}


