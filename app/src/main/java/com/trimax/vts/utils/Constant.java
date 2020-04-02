package com.trimax.vts.utils;

import com.trimax.vts.model.NotificationPojo;
import com.trimax.vts.model.VehiclePojo;
import com.trimax.vts.model.VehicleViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Constant {

    public static ArrayList<String> selectedStrings = new ArrayList<String>();
    public static ArrayList<String> selectedStringstype = new ArrayList<String>();
    public static ArrayList<Integer> arraylist_position = new ArrayList<Integer>();
    public static Set<Integer> set = new HashSet<Integer>();
    public  static boolean vehicleflag=false;
    public  static boolean trackerflag=false;
    public  static boolean clickOkAlertDialog=false;

    public  static boolean s=false;
    public static ArrayList<Integer> arraylist_position_t = new ArrayList<>();
    public static ArrayList<Integer> arrayListcheck2 = new ArrayList<>();
    public static ArrayList<Integer> arralistcheck1 = new ArrayList<>();

    public static StringBuilder stringBuilder=new StringBuilder();

    public  static boolean check2=false;
    public  static boolean check1=false;

    public  static boolean first=false;
    public  static boolean second=false;

    public static ArrayList<VehiclePojo> arrayList_vehicle=new ArrayList<>();
    public static ArrayList<String> selectedGroupStringArray = new ArrayList<>();
    public static ArrayList<String> selectedTypeStringArray = new ArrayList<>();
    public static ArrayList<String> selectedVehicleStringArray = new ArrayList<>();

    public static ArrayList<Integer> selectedGroupPositionsArray = new ArrayList<>();
    public static ArrayList<Integer> selectedTypePositionsArray = new ArrayList<>();
    public static ArrayList<Integer> selectedVehiclePositionsArray = new ArrayList<>();

    public static ArrayList<Integer> arraylist_position_t_group = new ArrayList<>();
    public static ArrayList<Integer> arraylist_position_t_vehicle = new ArrayList<>();
    public static ArrayList<Integer> arraylist_position_t_type = new ArrayList<>();

    public static ArrayList<String> arraylist_alertTypeTitle_group = new ArrayList<String>();
    public static ArrayList<String> arraylist_alertTypeTitle_type = new ArrayList<String>();
    public static ArrayList<String> arraylist_alertTypeTitle_vehicle = new ArrayList<String>();

    public static ArrayList<VehicleViewGroup> arrayList_vehicle_group=new ArrayList<>();
    public static ArrayList<NotificationPojo> arrayList_alert_type=new ArrayList<>();

    public static ArrayList<String> arraylist_alertTypeTitle = new ArrayList<String>();

    public static ArrayList<String> selectedStringsVehicleGroupWise = new ArrayList<String>();
    public static ArrayList<Integer> selectedStringsVehicleGroupWisePosition = new ArrayList<Integer>();


    public static String getMonth(int month){
        String  S_month="";
          switch (month){
              case 1:
                  S_month="January";
                  break;
              case 2:
                  S_month="February";
                  break;
              case 3:
                  S_month="March";
                  break;
              case 4:
                  S_month="April";
                  break;
              case 5:
                  S_month="May";
                  break;
              case 6:
                  S_month="June";
                  break;
              case 7:
                  S_month="July";
                  break;
              case 8:
                  S_month="Agust";
                  break;
              case 9:
                  S_month="September";
                  break;
              case 10:
                  S_month="October";
                  break;
              case 11:
                  S_month="November";
                  break;
              case 12:
                  S_month="December";
                  break;
          }
          return S_month;
      }

}
