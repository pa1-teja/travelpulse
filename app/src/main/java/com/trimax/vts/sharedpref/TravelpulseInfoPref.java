package com.trimax.vts.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.trimax.vts.utils.Constants;
import com.trimax.vts.utils.PrefEnum;

public class TravelpulseInfoPref {
    SharedPreferences loginPref,appPref,oneSignalPref;
    SharedPreferences.Editor loginEditor,appEditor,oneSignalEditor;

    //Constants
    public String GEOFENCE_LAT="geofence_lat";
    public String GEOFENCE_LNG="geofence_lng";
    public String GEOFENCE_PLACE="geofence_place";
    public String GEOFENCE_ADDRESS="geofence_address";
    public String APP_ALARM="app_alarm";

    public String APP_UPDATE_TIME="app_update_time";

    public TravelpulseInfoPref(Context context){
        loginPref = context.getSharedPreferences(Constants.app_preference_login,Context.MODE_PRIVATE);
        appPref = context.getSharedPreferences(Constants.app_preference,Context.MODE_PRIVATE);
        oneSignalPref = context.getSharedPreferences(Constants.app_preference_OneSignal,Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getLoginEditor(){
        if (loginEditor==null){
            loginEditor = loginPref.edit();
        }
        return loginEditor;
    }

    public SharedPreferences.Editor getAppEditor(){
        if (appEditor==null){
            appEditor = appPref.edit();
        }
        return appEditor;
    }

    public SharedPreferences.Editor getOneSignalEditor(){
        if (oneSignalEditor==null){
            oneSignalEditor = oneSignalPref.edit();
        }
        return oneSignalEditor;
    }

    public SharedPreferences getLoginPref(){
        return loginPref;
    }

    public SharedPreferences getAppPref() {
        return appPref;
    }

    public SharedPreferences getOneSignalPref() {
        return oneSignalPref;
    }

    public void putString(String key, String value, PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            getLoginEditor().putString(key,value).apply();
        else if (pref.equals(PrefEnum.App))
            getAppEditor().putString(key,value).apply();
        else
            getOneSignalEditor().putString(key,value).apply();
    }

    public String getString(String key,PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            return loginPref.getString(key,"");
        else if (pref.equals(PrefEnum.App))
            return appPref.getString(key,"");
        else
            return oneSignalPref.getString(key,"");
    }

    public void putInt(String key,int val, PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            getLoginEditor().putInt(key,val).apply();
        else if (pref.equals(PrefEnum.App))
            getAppEditor().putInt(key,val).apply();
        else
            getOneSignalEditor().putInt(key,val).apply();
    }

    public int getInt(String key,PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            return loginPref.getInt(key,0);
        else if (pref.equals(PrefEnum.App))
            return appPref.getInt(key,0);
        else
            return oneSignalPref.getInt(key,0);
    }

    public void putLong(String key,long val, PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            getLoginEditor().putLong(key,val).apply();
        else if (pref.equals(PrefEnum.App))
            getAppEditor().putLong(key,val).apply();
        else
            getOneSignalEditor().putLong(key,val).apply();
    }

    public long getLong(String key,PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            return loginPref.getLong(key,0);
        else if (pref.equals(PrefEnum.App))
            return appPref.getLong(key,0);
        else
            return oneSignalPref.getLong(key,0);
    }

    public void putFloat(String key, float val, PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            getLoginEditor().putFloat(key,val).apply();
        else if (pref.equals(PrefEnum.App))
            getAppEditor().putFloat(key,val).apply();
        else
            getOneSignalEditor().putFloat(key,val).apply();
    }

    public float getFloat(String key,PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            return loginPref.getFloat(key,0);
        else if (pref.equals(PrefEnum.App))
            return appPref.getFloat(key,0);
        else
            return oneSignalPref.getFloat(key,0);
    }

    public void putBoolean(String key, boolean val, PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            getLoginEditor().putBoolean(key,val).apply();
        else if (pref.equals(PrefEnum.App))
            getAppEditor().putBoolean(key,val).apply();
        else
            getOneSignalEditor().putBoolean(key,val).apply();
    }

    public boolean getBoolean(String key,PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            return loginPref.getBoolean(key,false);
        else if (pref.equals(PrefEnum.App))
            return appPref.getBoolean(key,false);
        else
            return oneSignalPref.getBoolean(key,false);
    }

    public boolean isKeyContains(String key,PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            return loginPref.contains(key);
        else if (pref.equals(PrefEnum.App))
            return appPref.contains(key);
        else
            return oneSignalPref.contains(key);
    }

    public void remove(String key, PrefEnum pref){
        if (pref.equals(PrefEnum.Login))
            getLoginEditor().remove(key).apply();
        else if (pref.equals(PrefEnum.App))
            getAppEditor().remove(key).apply();
        else
            getOneSignalEditor().remove(key).apply();
    }
}
