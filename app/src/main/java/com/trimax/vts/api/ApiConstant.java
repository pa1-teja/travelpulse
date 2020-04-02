package com.trimax.vts.api;

/**
 * Created by genuus on 31/1/18.
 */

public interface ApiConstant {

    //This are the main function url
    String UrlChechIflogin="api_v6/NotificationPush/checkIfLoggedIn";
    String UrlLogin = "api_v6/login/loginvalidate";
    String UrlForgot ="https://rp.roadpulse.net/api_v6/forgotpwd/forgotpassword";
    String UrlParkedOut = "api_v6/notification/updateParkingStatus";
    String UrlGetAllNotification = "api_v6/notification/getallnotfication";
    String URLNotification="api_v6/NotificationPush/addDevice";
    String URLAddDevice="api_v6/NotificationPush/deviceUsers";
    String UrlGetRealTimeTrackingDeviceLive ="api_v6/Realtimetracking/realtime_android";
    String UrlGetRealTimeTrackingLive = "api_v6/vehiclemovement/vmr_android";
    String UrlGetgetDashboardDetails ="api_v6/dashboard/getDashboardDetails";
    String UrlGetgetindivichuldetails ="api_v6/realtimetracking/current_vehicle_status";
    String UrlGetgetvehicledetails ="api_v6/vehicle/getvehiclelist";
    String UrlGetVehicles ="api_v6/support_services/get_vehicles_with_location";
    String UrlGetgetvehiclecount =" api_v6/vehicle/getvehiclecount";
    String UrlchangePAssword="api_v6/changepassword/changepassword";
    String UrlGetgetvehiclecmarked = "api_v6/vehicle/markvehicles_active";
    String notificationtype = "api_v6/notification/getnotificationtypelist";
    String notification ="api_v6/notification/getnotification";
    String URLSaveAllGeoFenceByVehicle ="/api_v6/geofence/savegeofence";
    String URLUpdateGeoFenceByVehicle ="api_v6/geofence/updategeofencename";
    String URLDeleteGeoFenceByVehicle ="api_v6/geofence/deletefence";
    String URLGetAllGeoFenceByVehicle ="api_v6/geofence/getAllfencebyvehicles";
    String URLUpdateGeoFenceByid ="api_v6/geofence/updategeofence";

    String UrlGetAlertReport = "api_v6/report/Alert_report/getAlertreport";
    String UrlGetAlertAlertsummaryreport = "api_v6/report/Alert_report_summary/getAlertsummaryreport";
    String UrlGetVehiclestatusreport = "api_v6/report/Current_vehiclestatus_report/getVehiclestatusreport";
    String UrlGetVehiclesummaryreport = "api_v6/report/Vehicle_summary/getVehiclesummaryreport";

    String UrlGetGroupvehicle ="api_v6/Vehiclegroup/getgroupwisevehicle";
    String UrlGetAllVehiclegroup ="api_v6/Vehiclegroup/getVehiclegroup";
    String UrlGetVehicleStopreport = "api_v6/report/stop/getVehiclestopreport";
    String UrlGetVehicleRunreport = "api_v6/report/run/getVehiclerunreport";
    String UrlGetVehicleIdleeport = "api_v6/report/idle/getVehicleidlereport";

    /*Manage Masters Api: Added by Apeksha Gunjal*/
    String UrlGetAllDrivers = "api_v6/driver/getAlldrivers";
    String UrlAddDriver = "api_v6/Driver/addDriver"; //"user_type_id, id, name, license_number, license_expiry, mobile_no
    String UrlEditDriver = "api_v6/driver/editDriver"; //"user_type_id, id, driver_id
    String UrlUpdateDriver = "api_v6/Driver/updateDriver";//"user_type_id, id, name, license_number, license_expiry, mobile_no, driver_id
    String UrlRemoveDriver = "api_v6/Driver/removeDriver";//"user_type_id, id, driver_id

    String UrlGetVehicleList = "api_v6/vehicle/getvehiclelist"; //user_type_id, user_id
    String UrlRetriveVehicleProfile = "api_v6/vehicleprofile/retrievevehicleprofile"; //user_type_id, user_id
    String UrlUpdateVehicleProfile = "api_v6/vehicleprofile/updatevehicleprofile";

    String UrlGetGroupwiseVehicleList = "api_v6/Vehiclegroup/getgroupwisevehicle";//group_id
    String UrlGetVehicleGroupList = "api_v6/Vehiclegroup/getVehiclegroup"; //"customerid , user_type_id
    String UrlRemoveGroup = "api_v6/Vehiclegroup/removeGroup";//"user_type_id, user_id, vehicle_group_id
    String UrlUpdateVehicleGroup = "api_v6/Vehiclegroup/updateGroup";
    String UrlAddVehicleGroup = "api_v6/Vehiclegroup/addGroup";
    String UrlVehicleListForAddGroup = "api_v6/Vehiclegroup/getVehicles";
    String UrlFleetSubUsersListForAddGroup = "api_v6/Vehiclegroup/getFleetSubuser";
    String UrlEditGroup = "api_v6/Vehiclegroup/editGroup";

    String UrlRemoveSubUser =  "api_v6/fleetsubuser/removeSubuser";//"user_type_id:5user_id:59 subuser_id:4"
    String UrlAddSubUser = "api_v6/Fleetsubuser/addSubuser";
    String UrlUpdateSubUser = "api_v6/fleetsubuser/updateSubuser";
    String UrlGetAllFleetUsers ="api_v6/Fleetsubuser/getAllsubusers";

    String UrlSetNotificationPreference = "api_v6/notification/setPreference";
    String UrlGetNotificationPreference = "api_v6/Notification/getPreference";
    String UrlGetVASProviders = "api_v6/Vas/getProviders";

    /*Upload Document*/
    String GetListOfUploadDocument ="api_v6/Vehicledocuments/getvehicledocumentlist";
    String GetListOfInsurancecompany ="api_v6/Vehicledocuments/getInsuranceList";
    String UploadDocument="api_v6/Vehicledocuments/addVehicleDocuments";

    /*VTP*/
    String UrlGetgetvehiclecPerformace = "api_v6/TravelPerformance/getTravelPerformance";

    /*AutoNotificationPref*/
    String UrlSetNotificationPrefOnLogin = "api_v6/notification/setPreferenceOnLogin";


    String UrlSetNotificationReceivedLog = "api_v6/NotificationPush/updateNotificationLog";

    String urlPlacesApiError = "api_v6/log_error/log_googleapi_error";

    /*Reports*/
    String urlReportData = "api_v6/report/VehicleStatusReport/getVehicleStatusReport";
    String urlReportDetailData = "api_v6/report/VehicleStatusReport/getDetailedVehicleStatusReport";
    String urlReportDetailNotification = "api_v6/report/VehicleStatusReport/getDetailedNotification";


    /*Tickets*/
    String urlTicketType = "api_v6/Ticket_raise/getTicketType";
    String urlSaveTicket = "api_v6/Ticket_raise/saveTicketData";
    String urlGetTicket = "api_v6/Ticket_raise/getTicketData";
    String urlTicketComment = "api_v6/Ticket_raise/addTicketComment";
    String urlGetTicketComment = "api_v6/Ticket_raise/getCommentData";


    /*Remote immobilization*/
    String urlTPinGenerate = "api_v7/Remote_immobilize/send_otp";
    String urlSetTPin = "api_v7/Remote_immobilize/regenerate_tpin";
    String urlVehicleLockUnlock = "api_v7/Remote_immobilize/lock_unlock";
    String urlVehicleStatus = "api_v7/Remote_immobilize/get_current_status";
    String urlTestVehicleStatus = "api_v7/Remote_immobilize/get_current_status";
    String urlSendOTP = "api_v7/Remote_immobilize/send_otp";


    /*Here map rest api*/
    String matchRouteApi ="https://rme.api.here.com/2/matchroute.json?app_id=8jmFsrdU1zCTiuB1oYr5&app_code=FQmm3YCKNLpGTTHcqXhHsQ&routemode=car";
}





