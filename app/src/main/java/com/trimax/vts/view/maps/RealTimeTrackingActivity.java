package com.trimax.vts.view.maps;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.maps.adapters.VTSOptionsListAdapter;
import com.trimax.vts.model.notifications.BadgeView;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import com.trimax.vts.view.vehicle.VTSSetVehicalActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class RealTimeTrackingActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private static final String TAG = "RealTimeTrackingActivit";
    String vehiclelistValue="", UserAccessType,vehiclechangeflag = "",vid = "", vidAlarm = "";
    CommonClass cc;
    FragmentHomeMap FM = new FragmentHomeMap();

    BadgeView badge;
    TextView car, shareIcon;
    View customMarkerView;
    Typeface type;
    ListView lv;

    Activity context;
    private TravelpulseInfoPref infoPref;
    GoogleMap mMap;
    CustomMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_tracking);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_red_color)));
        context = this;
        infoPref = new TravelpulseInfoPref(this);

        car =  findViewById(R.id.vtsCarIcon);
        shareIcon =  findViewById(R.id.vtsShareIcon);


        customMarkerView = LayoutInflater.from(this).inflate(R.layout.svg_icon_layout, null);  //SVG ICON SET HERE
        UserAccessType = infoPref.getString("UserType", PrefEnum.App);
        if(getIntent()!=null) {
            Intent ii = getIntent();
            vid = ii.getStringExtra("vid");
            vidAlarm = ii.getStringExtra("vid_Alrm");
        }

        supportMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.vtsgoogleMap));
        supportMapFragment.getMapAsync(this);
        String[] VTSOptionsHeader = {"Replay Tracking"};
        String[] VTSOptionsDetail = {"Track the past routes taken by the vehicle"};
        cc = new CommonClass(context);
        type = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        car.setTypeface(type);
        shareIcon.setTypeface(type);
        lv =  findViewById(R.id.vtslistview);
        VTSOptionsListAdapter vtsOLAdapter = new VTSOptionsListAdapter(context, VTSOptionsHeader, VTSOptionsDetail);//VTSOptionsIndicator
        lv.setAdapter(vtsOLAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                    CommonClass.VehicleValue=vid;
                    infoPref.putString("vid",vid,PrefEnum.Login);
                    if (position == 0) {
                        Intent i = new Intent(RealTimeTrackingActivity.this, ReplayTrackingActivity.class);
                        Bundle b = new Bundle();
                        b.putString("callFromMain", "");
                        i.putExtras(b);
                        startActivity(i);
                    }
            }
        });
        badge = new BadgeView(this, car);
        badge.setBadgeMargin((int) getResources().getDimension(R.dimen.badge_view_horizontal_margin), (int) getResources().getDimension(R.dimen.badge_view_vertical_margin));
        badge.setTextSize(10);
        badge.setBadgeBackgroundColor(getResources().getColor(R.color.road_pulse_blue_color));
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

        if (cc.isConnected(context)) {
            current_vehicle_status_dashboard(infoPref.getString("user_type_id",PrefEnum.Login),infoPref.getString("id",PrefEnum.Login));
        } else {
            cc.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        finish();
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LinearLayout vtsLL = findViewById(R.id.vtsmapll);
        ImageView maxminimg = findViewById(R.id.maxminMapIcon);
        RelativeLayout vtsF = findViewById(R.id.relvtsgoogleMap);
        ListView vtsL = findViewById(R.id.vtslistview);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            vtsF.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0.5f));
            vtsL.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0.5f));
            vtsLL.setOrientation(LinearLayout.HORIZONTAL);
            maxminimg.setVisibility(View.INVISIBLE);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (maxminimg.getTag().equals("max")) {
                vtsF.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
            } else {
                vtsF.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.6f));
            }
            vtsL.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.4f));
            vtsLL.setOrientation(LinearLayout.VERTICAL);
            maxminimg.setVisibility(View.VISIBLE);
        }
    }

    public void vtsShareClicked(View view) {
        SharedPreferences sharedpref = getSharedPreferences(Constants.app_preference_login, Context.MODE_PRIVATE);
        String shareString = sharedpref.getString("shareString", "");
        PackageManager pm = getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            waIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        }
    }

    public void maxminMapClicked(View view) {
        RelativeLayout vtsF = findViewById(R.id.relvtsgoogleMap);
        ImageView maxminimg = view.findViewById(R.id.maxminMapIcon);
        if (maxminimg.getTag().equals("min")) {
            if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                vtsF.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
            }
            maxminimg.setImageResource(R.drawable.minimize);
            maxminimg.setTag("max");
        } else {
            if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                vtsF.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.90f));
            }
            maxminimg.setImageResource(R.drawable.maximize);
            maxminimg.setTag("min");
        }
    }

    public void vtsCarMapClicked(View view) {
        Intent intent = new Intent(context, VTSSetVehicalActivity.class);
        intent.putExtra("Call","CallFromRealTimeTracking");
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                supportMapFragment.getMapAsync(this);
                if(mMap!=null){
                    mMap.clear();
                }
                if (FM.markerPoints != null) {
                    FM.markerPoints.clear();
                }
                vehiclelistValue=data.getStringExtra("vid");
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        FM.stopTimerTask();
    }

    @Override
    protected void onResume() {
        FM.markerPoints = null;
        FM.markerPoints = new ArrayList<>();
        FM.pointRepeated = false;
        FM.wholeData = null;
        vehiclechangeflag = infoPref.getString("vehiclechangeflag", PrefEnum.App);
        if (vehiclechangeflag.equalsIgnoreCase("vehicle_changed")) {
            if(supportMapFragment!=null) {
                supportMapFragment.getMapAsync(this);
                if(mMap!=null){
                    mMap.clear();
                }
                FM.markerPoints = null;
                infoPref.putString("fromdashboard","N",PrefEnum.Login);
                FM.startTimer(supportMapFragment,context);
            }
        }
        else {
            if(supportMapFragment!=null)
                FM.startTimer(supportMapFragment,context);
        }
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Crashlytics.log("Vts onstop");
        FM.stopTimerTask();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if(getIntent()!=null) {
            Intent ii = getIntent();
            vid = ii.getStringExtra("vid");
            infoPref.putString("vid", vid,PrefEnum.Login);
        }
        FM.Index(context, supportMapFragment, customMarkerView);
    }

    public void current_vehicle_status_dashboard(String usertype_id, final String user_id) {
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call=objRetrofitInterface.current_vehicle_count(auth,apiKey,usertype_id,user_id);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String syncresponse = myObject.getString("data");

                                Log.e("response", syncresponse);
                                String strStatus = myObject.getString("status");

                                    if (strStatus.trim().equals("success")) {
                                            badge.setText(syncresponse);
                                            badge.show();
                                    }
                        }
                    }
                catch (Exception ex) {
                            Log.e("","Api fail");
                            Crashlytics.logException(ex);
                            Crashlytics.log("Api fail vehicle count");
                        }
                    }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

                }
            });
    }
}
