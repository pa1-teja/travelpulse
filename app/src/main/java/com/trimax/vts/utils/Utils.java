package com.trimax.vts.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.services.NotificationOpenHandler;
import com.trimax.vts.services.NotificationReceiveHandler;
import com.trimax.vts.services.SendNotificationService;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    static Location mLastKnownLocation=null;

    public static void showMessage(Activity activity, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.show();
    }

    static ProgressDialog progressDialog;

    public static void showProgressDialog(Activity context, String dialogText) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(dialogText);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public static String getTimeInAMPM(String dateString){
        if (dateString==null)
            return "00:00";
        String time="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(dateString);
            //new format
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
            //formatting the given time to new format with AM/PM
            time=sdf1.format(date);
            System.out.println("Given date and time in AM/PM: "+time);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return time;
    }

    public static String getTimeInMins(String timeString){
        if (timeString==null || timeString.equalsIgnoreCase("0"))
            return "00:00";
        String time="";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try{
            Date date = sdf.parse(timeString);
            //new format
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            //formatting the given time to new format with AM/PM
            time=sdf1.format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return time;
    }

    public static String getDateTime(String timeString){
        if (timeString==null)
            return "";
        String dateTime="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(timeString);
            //new format
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy  hh:mm aa");
            //formatting the given time to new format with AM/PM
            dateTime=sdf1.format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String secondsToHourMins(String seconds){
        if (seconds==null || seconds.isEmpty())
            return "00:00";
        int totalSecs = Integer.parseInt(seconds);
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public static void stopProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
           // Log.d(TAG,"no internet connection");
            return false;
        }
        else {
            if(info.isConnected()) {
              //  Log.d(TAG," internet connection available...");
                return true;
            }
            else {
             //   Log.d(TAG," internet connection");
                return true;
            }
        }
    }


    public static Location getDeviceLocation(Context context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            final LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            mLastKnownLocation= location;
                            Log.d(TAG, "onLocationResult: "+location);
                        }
                    }
                }
            };
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            LocationServices.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mLastKnownLocation=location;
                    Log.d(TAG, "onSuccess: "+location);
                }
            });
            return mLastKnownLocation;
        }
        return null;
    }

    public static void blinkView(View view){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }


    public static void remove_shareprefernce(SharedPreferences sharedpreference){

        sharedpreference.edit().remove("vid").apply();
        sharedpreference.edit().remove("vidgeo").apply();
        sharedpreference.edit().remove("livetrack").apply();
        sharedpreference.edit().remove("logout").apply();

        sharedpreference.edit().remove("password").apply();
        sharedpreference.edit().remove("doc_vid").apply();
        sharedpreference.edit().remove("last_name").apply();
        sharedpreference.edit().putString("customer_id","y").apply();

        sharedpreference.edit().remove("status").apply();
        sharedpreference.edit().remove("vidgeo").apply();
        sharedpreference.edit().remove("id").apply();
        sharedpreference.edit().putString("vehicleLastLat","y").apply();

        sharedpreference.edit().remove("access").apply();
        sharedpreference.edit().remove("emailAddress").apply();
        sharedpreference.edit().remove("first_name").apply();
        sharedpreference.edit().remove("user_type_id").apply();

        sharedpreference.edit().remove("mobile_number").apply();
        sharedpreference.edit().remove("shareString").apply();
        sharedpreference.edit().remove("vehicleLastLng").apply();
    }

    public static void getPlayerId(Context context,TravelpulseInfoPref infoPref) {
        OneSignal.startInit(context)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new NotificationOpenHandler())
                .setNotificationReceivedHandler( new NotificationReceiveHandler() )
                .init();
        OneSignal.setSubscription(true);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getSubscriptionStatus().getSubscribed();
        status.getPermissionStatus().getEnabled();
        status.getSubscriptionStatus().getUserSubscriptionSetting();
        infoPref.putString("GT_PLAYER_ID",status.getSubscriptionStatus().getUserId(), PrefEnum.OneSignal);
        infoPref.putString("token",status.getSubscriptionStatus().getPushToken(),PrefEnum.OneSignal);
    }

    public static void fndeviceadd(String recordId,String pid,String token,String UserTypeId, final String userID ,String strStatus,final TravelpulseInfoPref infoPref) {
        Log.d(TAG, "fndeviceadd: ");
        Call<ResponseBody> call=RepositryInstance.getNotificationRepository().changeUserStatus(/*auth,apiKey,*/recordId,pid,token,UserTypeId,userID,strStatus);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody responseBody = response.body();
                    int strResponceCode = response.code();
                    Log.d(TAG,"Responce code "+responseBody);
                    switch (strResponceCode) {
                        case 200:
                            assert responseBody != null;
                            String strResponse = responseBody.string();
                            JSONObject myObject = new JSONObject(strResponse);
                            String strStatus = myObject.getString("status");
                            Log.d(TAG, "onResponse: "+strStatus);
                            if(strStatus.equalsIgnoreCase("failure")){
                                //remove_shareprefernce(infoPref.getLoginPref());
                            }else if (strStatus.equalsIgnoreCase("success")){
                                infoPref.putBoolean("isDeviceAdded",true,PrefEnum.Login);
                            }
                            break;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d(TAG, "onResponse: "+ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }


    public static void getRecordId(String uid, String uidType, String tokenNo, String playerId,final TravelpulseInfoPref infoPref) {
        Log.d(TAG, "getRecordId: "+uid+" "+uidType+" "+tokenNo);
        Log.d(TAG, "getRecordId: "+playerId);
        if (!TextUtils.isEmpty(tokenNo) && !TextUtils.isEmpty(playerId)) {
            Call<ResponseBody> call = RepositryInstance.getNotificationRepository().addDevice(uid, uidType, tokenNo, playerId);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();

                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String strStatus = myObject.getString("status");
                                Log.d(TAG, "onResponse: recordId  " + strStatus);
                                if (strStatus.trim().equals("success")) {
                                    int r_id = myObject.getInt("data");
                                    infoPref.putString("record_id", String.valueOf(r_id), PrefEnum.OneSignal);
                                    Log.d(TAG, "record_id: " + r_id);
                                    if (!infoPref.getBoolean("isDeviceAdded",PrefEnum.Login) )
                                        fndeviceadd(String.valueOf(r_id), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),
                                            infoPref.getString("token", PrefEnum.OneSignal), infoPref.getString("user_type_id", PrefEnum.OneSignal),
                                            infoPref.getString("id", PrefEnum.OneSignal), "A", infoPref);
                                }
                                break;
                            case 400:
                                break;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.d(TAG, "onResponse: " + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "Api fail-----------" + t.getMessage());
                }
            });
        }
    }

    public static void scheduleJob(Context context){
        JobScheduler mScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        int JOB_ID=101;
        ComponentName serviceName = new ComponentName(context.getPackageName(), SendNotificationService.class.getName());

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setPeriodic(10*60*1000)
                .setRequiresCharging(false);

        JobInfo myJobInfo = builder.build();
        assert mScheduler != null;
        mScheduler.schedule(myJobInfo);
    }
}
