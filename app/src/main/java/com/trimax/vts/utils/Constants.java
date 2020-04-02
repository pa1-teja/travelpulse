package com.trimax.vts.utils;

public class Constants {

	public static final String dialogMessageToShowBusStops="Select Vehicle Group";
	public static final String dialogMessageToShowBusTypes="Select Vehicle";

	public static final double RADIUS_OF_EARTH_METERS = 6371009;
	public static final String DATABASE_NAME="VTS.sqlite";
	public static String app_preference = "Trimax_VTS";
	public static String app_preference_login="login";
	public static String app_preference_OneSignal="OneSignal";

	public interface IDriver {
		String NAME = "name";
		String LIECENSE_NO = "liecense_number";
		String LIECENSE_EXPIRY = "liecense_expiry";
		String MOBILE_NUMBER = "mobile_number";
		String ACTION = "action";
		String FILE_NAME = "file_name";
		String DESCRIPTION = "des";
		String ACTION_EDIT_DRIVER_DETAILS = "edit_driver_details";
		String DRIVER_ID = "driver_id";
		String ACTION_ADD_DRIVER = "add_driver";
		String UPDATE = "Update";
	}

	public interface IVehicle {
		String VEHICLE_ID = "vehicle_id";
		String VEHICLE_LIST = "vehicleList";
		String GROUP_ID = "group_id";
		String TTILE = "title";
        String ACTION = "action";
        String vehicle_type = "vehicle_type";
    }

	public interface ISubUser {
		String FIRST_NAME = "firstName";
		String LAST_NAME = "lastName";
		String EMAIL = "email";
		String MOBILE_NUMBER = "mobile";
		String USER_ID = "userId";
		String UPDATE = "Update User";
		String ACTION = "action";
	}

    public interface IVASService {
		String ADV_SEARCH_FLAG = "ADVSearchFlag";
		String DIRECT_CALL = "DirectCall";
		String SERVICE_CODE = "serviceCode";
		String ADDRESS_WS = "AddressWs";
		String SEARCH_RADIUS_WS = "SearchRadiusWs";
		String ADVANCED_SEARCH = "advanced_search";
		String CUR_USER_LATTITUDE = "CurrUserLattitude";
		String CUR_USER_LONGITUDE = "CurrUserLongitude";
		String ID = "id";
		String FUEL = "FUEL";
		String TIRE_REPAIR = "TIREREPAIR";
		String CAR_TOWING = "CARTOWING";
		String CAR_WORKSHOP = "CARWORKSHOP";
		String AREA = "area";
		String SEARCH_RADIUS = "searchRadius";
        String NORMAL_SEARCH_TYPE = "NORMAL";
        String ADVANCE_SEARCH_TYPE = "ADVANCED";
    }

	public interface IReport {
        String VEHICLE_GROUP = "vehicleGroup";
        String VEHICLES = "vehicles";
        String ALERT = "alert";
        String OK_SELECTION = "Ok";
        String CANCEL_SELECTION = "cancel";
        String ALL_SELECTION = "all";
        String NONE = "none";
    }

}