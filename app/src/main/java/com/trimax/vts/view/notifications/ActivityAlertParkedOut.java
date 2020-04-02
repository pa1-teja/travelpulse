package com.trimax.vts.view.notifications;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.maps.RealTimeTrackingActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class ActivityAlertParkedOut extends AppCompatActivity {
    private static final String TAG = "ActivityAlertParkedOut";
    TextView vehicleNo,date,notification_subtypee,locationn;
    ImageView mute ,cancel_alram;
    MediaPlayer mPlayer;
    RelativeLayout noti_image_type;
    Button button_show,button_deactivated;
    ProgressDialog dialog;
    Intent ii;
    Resources res;
    PowerManager.WakeLock partialWakeLock,fullWakeLock;
    String msg="",notification_type="",vehicle_Id="";
    int media_current_volume, media_max_volume;
    private TravelpulseInfoPref infoPref;
    String UserTypeid,Userid,player_id,device_id;
    public Switch parking_mode;
    AssetFileDescriptor af;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        infoPref = new TravelpulseInfoPref(this);
        mute =(ImageView) findViewById(R.id.mute);
        cancel_alram =(ImageView)findViewById(R.id.cancel_alram);
        button_show =(Button)findViewById(R.id.button_show);
        button_deactivated = (Button) findViewById(R.id.button_deactivated);
        parking_mode=(Switch)findViewById(R.id.switch_parking_mode);
        parking_mode.setChecked(true);
        noti_image_type =(RelativeLayout)findViewById(R.id.noti_image_type);
        vehicleNo=findViewById(R.id.vehicleNo);
        date=findViewById(R.id.date);
        notification_subtypee=findViewById(R.id.notification_subtype);
        locationn=findViewById(R.id.location);

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer.isPlaying()){
                    mPlayer.pause();
                    mute.setImageResource(R.drawable.ic_volume_off_black_24dp);
                }
                else{
                    mute.setImageResource(R.drawable.ic_volume_mute_black_24dp);
                    mPlayer.start();
                }
            }
        });
        cancel_alram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.pause();
                mPlayer.stop();
               finishAffinity();
            }
        });

        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserTypeid.equalsIgnoreCase("") && Userid.equalsIgnoreCase("")){
                    Toast.makeText(ActivityAlertParkedOut.this,"Please Login Into Application",Toast.LENGTH_LONG).show();
                }else {
                    mPlayer.stop();
                    Intent intent = new Intent(ActivityAlertParkedOut.this, RealTimeTrackingActivity.class);
                    intent.putExtra("vid", ii.getStringExtra("vehicle_id"));
                    intent.putExtra("noti_lat", ii.getStringExtra("vehicle_lat"));
                    intent.putExtra("noti_lang", ii.getStringExtra("vehicle_long"));
                    intent.putExtra("noti_msg", ii.getStringExtra("msg"));
                    intent.putExtra("noti_address", ii.getStringExtra("location"));
                    intent.putExtra("noti_datetime", ii.getStringExtra("date_time"));
                    intent.putExtra("noti_speed", ii.getStringExtra("speed"));
                    intent.putExtra("noti_ign", ii.getStringExtra("ign"));
                    intent.putExtra("noti_ac", ii.getStringExtra("ac"));

                    infoPref.putString("vid", ii.getStringExtra("vehicle_id"), PrefEnum.Login);
                    infoPref.putString("livetrack", "",PrefEnum.Login);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });

        parking_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    mPlayer.pause();
                    mPlayer.stop();
                    fnParkingmodeOff(Userid,UserTypeid,vehicle_Id,"0");
                }
            }
        });

        button_deactivated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.pause();
                mPlayer.stop();
                fnParkingmodeOff(Userid,UserTypeid,vehicle_Id,"0");
            }
        });

    }

    private void setNotificationAlarm() {
        createWakeLocks();
        wakeDevice();
        res = getResources(); //resource handle
        ii = getIntent();

        msg= ii.getStringExtra("msg");
        notification_type= ii.getStringExtra("notification_type");
        vehicle_Id=ii.getStringExtra("vehicle_id");
        String vehicle_type_id=ii.getStringExtra("vehicle_type_id");
        UserTypeid=infoPref.getString("user_type_id",PrefEnum.Login);
        Userid = infoPref.getString("id",PrefEnum.Login);
        player_id= infoPref.getString("GT_PLAYER_ID",PrefEnum.OneSignal);
        device_id= infoPref.getString("record_id",PrefEnum.OneSignal);

        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        media_current_volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("Current Volum"+ media_current_volume,"");
        media_max_volume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d("Max Volum"+ media_max_volume,"");

        mPlayer = CommonClass.getMediaPlayer();
       /* if(mPlayer.isPlaying()){
            mPlayer.stop();
        }*/
        mPlayer.reset();


        if(notification_type.equalsIgnoreCase("parked_out")){
            Drawable drawable = res.getDrawable(R.drawable.parking_out); //new Image that was added to the res folder
            noti_image_type.setBackground(drawable);
            af = getResources().openRawResourceFd(R.raw.parkingon);
        }else if(notification_type.equalsIgnoreCase("dangerous_speed")){
            Drawable drawable = res.getDrawable(R.drawable.danger_speed); //new Image that was added to the res folder
            noti_image_type.setBackground(drawable);
            if(vehicle_type_id !=null && vehicle_type_id.equalsIgnoreCase("8")){
                af = getResources().openRawResourceFd(R.raw.two_wheelar);
            }
            else{
                af = getResources().openRawResourceFd(R.raw.dangerousspeed);
            }
        }else if(notification_type.equalsIgnoreCase("towing")){
            Drawable drawable = res.getDrawable(R.drawable.towing_alert); //new Image that was added to the res folder
            noti_image_type.setBackground(drawable);
            af = getResources().openRawResourceFd(R.raw.towing_alarm);
        }else if(notification_type.equalsIgnoreCase("device_tamper")){
            Drawable drawable = res.getDrawable(R.drawable.tamper); //new Image that was added to the res folder
            noti_image_type.setBackground(drawable);
        }else if(notification_type.equalsIgnoreCase("vehicle_idle_repeat")){
            Drawable drawable = res.getDrawable(R.drawable.car_idle); //new Image that was added to the res folder
            noti_image_type.setBackground(drawable);
        }else if(notification_type.equalsIgnoreCase("ac_idle_repeat")){
            Drawable drawable = res.getDrawable(R.drawable.ac_idle); //new Image that was added to the res folder
            noti_image_type.setBackground(drawable);
            af = getResources().openRawResourceFd(R.raw.ac_idling);
        }

        try {
            mPlayer.setDataSource(af.getFileDescriptor(),af.getStartOffset(),af.getLength());
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.start();
        mAudioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, // Stream type
                media_max_volume, // Index
                AudioManager.FLAG_SHOW_UI // Flags
        );


        String [] sp=msg.split(",");


        vehicleNo.setText(sp[0]);
        date.setText(ii.getStringExtra("date_time"));
        locationn.setText(ii.getStringExtra("location"));
        notification_subtypee.setText(msg);
    }

    // Called from onCreate
    @SuppressLint("InvalidWakeLockTag")
    protected void createWakeLocks(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Loneworker - PARTIAL WAKE LOCK");
    }

    // Called implicitly when device is about to sleep or application is backgrounded
    protected void onPause(){
        super.onPause();
        if(isDeviceLocked(ActivityAlertParkedOut.this)){
            //mPlayer.pause();
            partialWakeLock.acquire();

/*        }else{
            mPlayer.pause();*/

        }
        if(fullWakeLock.isHeld()){
            fullWakeLock.release();
        }
        if(partialWakeLock.isHeld()){
            partialWakeLock.release();
        }
    }

    // Called implicitly when device is about to wake up or foregrounded
    protected void onResume(){
        super.onResume();
        setNotificationAlarm();
    }

    // Called whenever we need to wake up the device
    public void wakeDevice() {
        fullWakeLock.acquire();
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
    }


    public void fnParkingmodeOff(String strUserid, final String UserTypeId,final String VehicleId,String Parkig_mode) {
        try {
            dialog = new ProgressDialog(ActivityAlertParkedOut.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait...");
            dialog.show();
            Call<ResponseBody> call= RepositryInstance.getNotificationRepository().setParkingModeOnOff(strUserid,UserTypeId,VehicleId,Parkig_mode,player_id,device_id);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        // progressbar_login.setVisibility(View.INVISIBLE);
                        if(dialog!=null){
                            dialog.dismiss();
                        }

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("","Responce code"+strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                //CommonClass.DisplayToast(context,strResponse,"center");
                                JSONObject myObject = new JSONObject(strResponse);
                                String strStatus = myObject.getString("status");
                                String strMessage = myObject.getString("msg");
                                if (strStatus.trim().equals("success"))
                                {
                                    Toast.makeText(ActivityAlertParkedOut.this,"Parking Mode Updated Successfully", Toast.LENGTH_LONG).show();

                                }
                                else {
                                    Toast.makeText(ActivityAlertParkedOut.this,"Parking Mode Updated Successfully", Toast.LENGTH_LONG).show();


                                }
                                 new AlertDialog.Builder(ActivityAlertParkedOut.this)
                                            .setMessage("Parking Mode Updated Successfully")
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    finishAffinity();
                                                }

                                            }).show();

                                break;
                            case 400:
                                case 500:
                                Log.e("response code 500","Internal server error");
                                break;
                        }

                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        new AlertDialog.Builder(ActivityAlertParkedOut.this)
                                .setMessage("")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }

                                }).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Log.e("","Api fail");
            new AlertDialog.Builder(ActivityAlertParkedOut.this)
                    .setMessage("")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }

                    }).show();
        }
    }



    public  boolean isDeviceLocked(Context context) {
        boolean isLocked = false;

        // First we check the locked state
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        assert keyguardManager != null;
        boolean inKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode();

        if (inKeyguardRestrictedInputMode) {
            isLocked = true;

        } else {
            PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            if (powerManager!=null) {
                isLocked = !powerManager.isInteractive();
            }
        }

        return isLocked;
    }

}
