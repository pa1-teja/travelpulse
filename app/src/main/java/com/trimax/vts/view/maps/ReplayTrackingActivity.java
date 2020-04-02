package com.trimax.vts.view.maps;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.DrawableRes;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.search.Address;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.helper.MapWrapperLayout;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.BuildConfig;
import com.trimax.vts.view.R;
import com.trimax.vts.view.maps.adapters.CustomInfoViewAdapter;
import com.trimax.vts.view.model.GeofenceResponse;
import com.trimax.vts.view.model.replay.NotificationData;
import com.trimax.vts.view.model.replay.RealtimeData;
import com.trimax.vts.view.model.replay.ReplayError;
import com.trimax.vts.view.model.replay.ReplayLatLng;
import com.trimax.vts.view.model.replay.ReplayResponse;
import com.trimax.vts.view.model.route.RouteLink;
import com.trimax.vts.view.model.route.RouteResponse;
import com.trimax.vts.view.vehicle.VTSSetVehicalActivity;
import com.trimax.vts.model.InfoWindowData;
import com.trimax.vts.model.Notification1;
import com.trimax.vts.database.entity.Notification;
import com.trimax.vts.utils.CommonClass;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

@Keep
public class ReplayTrackingActivity extends AppCompatActivity implements OnMapReadyCallback,OnClickListener {
    private static final String TAG = "ReplayTrackingActivity";

    TextView totalDistTxt, speedTextviewReplay, RegNoTextviewReplay, VehNameReplay, IgnitionTextviewReplay, car, AcTextviewReplay, DateTimeTextviewReplay;
    TextView tv_clr_from_date, clnFrmTime, tv_clr_to_date, clnToTime;
    TextView trckProgressIcon, replyinpro, setPanButton, changeMap, ic_pro_Done;
    EditText ed_from_date, ed_to_date, ed_from_time, ed_to_time;
    TextInputLayout frmDateLay, frmtimeLay, toDateLay, toTimeLay;
    LinearLayout progressDone, linProgress, distanceLayout, FromDateLayout, ToDateLayout;
    Button Track;
    ProgressDialog dialog;
    View customMarkerView;
    RangeBar rangeBar;

    Calendar calendar;
    Typeface type, font_awesome;

    Context context;
    private String click_VehicleValue = "", callFromDashboard = "", Alrm = "", VehicleValueNotification = "", Vehicle_id_Alrm, replay_notification = "";
    private String vehicle_type = "", maptType, user_type_id = "", id = "";
    private String toDate="",fromDate="",toTime="",fromTime="";

    GoogleMap mMap;
    private MapEngine mapEngine;
    double totalDistance = 0;
    CommonClass cc;

    ArrayList<Notification1> arrayList_notification;
    List<Notification> latlnglistnotification;
    ArrayList<Marker> polyMarkerList, polyMarkerEdgeList;
    ArrayList<MarkerOptions> arrowOptionList;
    ArrayList<Polygon> polygonList;
    private RealtimeData latlnglistfortrack, returnedData, latlnglistNew;
    private Animation animation;
    private Double TrackerLat = null, TrackerLong = null;
    private MarkerOptions animMarkerOption, flagMarkerOption, markerOptions;
    private LatLng setPanLatLng,trackpoint;
    private Marker markerreplayMapClick, animMarker, flagMarker,markerreplay;
    private Boolean setPan = true,fetchaddress=false;
    Runnable runnable;
    Handler handler;
    float distance = 0;
    private int m, speed = 100;

    List<ReplayLatLng> animatedLatLngNew;
    CustomMapFragment supportMapFragment;
    private String Track_latitide="",Track_longitute="",TrackMsg="",next_lat="",next_lng="",next_TrackMsg="";

    private long fromDateTime;
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replaytracking);
        context = this;
        infoPref = new TravelpulseInfoPref(this);
        mapEngine = MapEngine.getInstance();
        ApplicationContext appContext = new ApplicationContext(context);
        mapEngine.init(appContext, new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // Post initialization code goes here
                } else {
                    // handle factory initialization failure
                }
            }
        });

        arrayList_notification = new ArrayList<>();

        supportMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.vtsgoogleMapReplay));
        supportMapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_red_color)));
        cc = new CommonClass(this);

        arrowOptionList = new ArrayList<>();
        latlnglistnotification = new ArrayList<>();
        polyMarkerList = new ArrayList<>();
        polyMarkerEdgeList = new ArrayList<>();
        polygonList = new ArrayList<>();
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        setPanButton =  findViewById(R.id.setPanId);
        speedTextviewReplay =  findViewById(R.id.txtVehSpeedReplay);
        RegNoTextviewReplay =  findViewById(R.id.txtVehRegNoReplay);
        VehNameReplay =  findViewById(R.id.txtVehNameReplay);
        IgnitionTextviewReplay =  findViewById(R.id.txtVehIgnitionReplay);
        AcTextviewReplay =  findViewById(R.id.txtVehAcReplay);
        car =  findViewById(R.id.vtsCarIcon);
        type = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        car.setTypeface(type);
        DateTimeTextviewReplay =  findViewById(R.id.txtVehDateTimeReplay);
        replyinpro =  findViewById(R.id.replyinProgress);
        progressDone =  findViewById(R.id.linProgressDone);
        linProgress =  findViewById(R.id.linProgress);
        ic_pro_Done =  findViewById(R.id.ic_progressDone);
        rangeBar = findViewById(R.id.rangebar);

        totalDistTxt =  findViewById(R.id.distanceTrvlID);
        distanceLayout =  findViewById(R.id.distLayId);
        changeMap =  findViewById(R.id.mapchangeID);
        frmDateLay =  findViewById(R.id.dateLayID);
        toDateLay =  findViewById(R.id.todateLayID);
        frmtimeLay =  findViewById(R.id.timeLayID);
        toTimeLay =  findViewById(R.id.toTimeLayID);

        tv_clr_from_date =  findViewById(R.id.xfrmdate);
        tv_clr_from_date.setTypeface(font_awesome);
        tv_clr_from_date.setVisibility(View.GONE);
        clnFrmTime =  findViewById(R.id.xfrmtime);
        clnFrmTime.setTypeface(font_awesome);
        clnFrmTime.setVisibility(View.GONE);
        tv_clr_to_date =  findViewById(R.id.xtodate);
        tv_clr_to_date.setTypeface(font_awesome);
        tv_clr_to_date.setVisibility(View.GONE);
        clnToTime =  findViewById(R.id.xtotime);
        clnToTime.setTypeface(font_awesome);
        clnToTime.setVisibility(View.GONE);
        frmDateLay.setTypeface(font_awesome);
        frmDateLay.setHint(getString(R.string.fa_calendar_plus_o) + "  From Date");
        frmtimeLay.setTypeface(font_awesome);
        frmtimeLay.setHint(getString(R.string.fa_clock_o) + "  From Time");

        toDateLay.setTypeface(font_awesome);
        toDateLay.setHint(getString(R.string.fa_calendar_plus_o) + "  To Date");
        toTimeLay.setTypeface(font_awesome);
        toTimeLay.setHint(getString(R.string.fa_clock_o) + "  To Time");
        maptType = "normal";
        customMarkerView = LayoutInflater.from(this).inflate(R.layout.svg_icon_layout, null);  //SVG ICON SET HERE
        rangeBar.setSeekPinByIndex(2);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            callFromDashboard = extras.getString("callFromMain", "");
            Alrm=extras.getString("alrm", "");
            Vehicle_id_Alrm=extras.getString("alrm_vid", "");
            replay_notification = extras.getString("replay_notification", "");
            VehicleValueNotification = extras.getString("noti_vid");
        }else{
            Alrm="N";
            Vehicle_id_Alrm="";
            VehicleValueNotification = "";
            replay_notification = "N";
        }

        trckProgressIcon =  findViewById(R.id.trackProgressIcon);
        trckProgressIcon.setTypeface(font_awesome);
        ic_pro_Done.setTypeface(font_awesome);
        setPanButton.setTypeface(font_awesome);
        changeMap.setTypeface(font_awesome);
        setPanButton.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        trckProgressIcon.setText("");
        replyinpro.setText("");

        FromDateLayout =  findViewById(R.id.FromDateLinearLayout);
        ToDateLayout =  findViewById(R.id.ToDateLinearLayout);
        Track =  findViewById(R.id.btTrack);

        click_VehicleValue = infoPref.getString("vid", PrefEnum.Login);
        user_type_id = infoPref.getString("user_type_id", PrefEnum.Login);
        id = infoPref.getString("id", PrefEnum.Login);

        calendar = Calendar.getInstance();

        ed_from_date = findViewById(R.id.txtFromValue);
        ed_from_date.setOnClickListener(this);

        tv_clr_from_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_from_date.setText("");
                tv_clr_from_date.setVisibility(View.GONE);
            }
        });
        ed_to_date =  findViewById(R.id.txtToValue);
        ed_to_date.setOnClickListener(this);
        tv_clr_to_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_to_date.setText("");
                tv_clr_to_date.setVisibility(View.GONE);
            }
        });

        ed_from_time =  findViewById(R.id.timeFromValue);
        ed_from_time.setOnClickListener(this);

        clnFrmTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_from_time.setText("");
                clnFrmTime.setVisibility(View.GONE);
            }
        });

        ed_to_time =  findViewById(R.id.timeToValue);
        ed_to_time.setOnClickListener(this);

        clnToTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_to_time.setText("");
                clnToTime.setVisibility(View.GONE);
            }
        });


        supportMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {
                setPan = false;
                setPanButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.txtFromValue:
                showDatePickerDialog(true);
            break;
            case R.id.txtToValue:
                showDatePickerDialog(false);
            break;
            case R.id.timeFromValue:
                showTimePickerDialog(true);
            break;
            case R.id.timeToValue:
                showTimePickerDialog(false);
            break;
        }
    }

    private void showDatePickerDialog(final boolean isFromDate){
        final Calendar calendar = Calendar.getInstance();
        final NumberFormat format = new DecimalFormat("00");
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if (isFromDate) {
                    ed_to_date.setText("");
                    ed_from_date.setText(i2+"-"+(i1+1)+"-"+i);
                    fromDate = format.format(i)+"-"+format.format((i1+1))+"-"+format.format(i2);
                    calendar.set(i,i1,i2);
                    fromDateTime = calendar.getTimeInMillis();
                    tv_clr_from_date.setVisibility(View.VISIBLE);
                }else {
                    toDate = format.format(i)+"-"+format.format((i1+1))+"-"+format.format(i2);
                    ed_to_date.setText(i2+"-"+(i1+1)+"-"+i);
                    tv_clr_to_date.setVisibility(View.VISIBLE);
                }

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        if (isFromDate) {
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        }
        else {
            dialog.getDatePicker().setMinDate(fromDateTime);
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        }
    }

    private void showTimePickerDialog(final boolean isFromTime){
        final Calendar calendar = Calendar.getInstance();
        final NumberFormat format = new DecimalFormat("00");
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if (isFromTime){
                    ed_from_time.setText(String.format("%s:%s", format.format(i), format.format(i1)));
                    fromTime=format.format(i)+":"+format.format(i1);
                    clnFrmTime.setVisibility(View.VISIBLE);
                }else {
                    ed_to_time.setText(String.format("%s:%s", format.format(i), format.format(i1)));
                    toTime=format.format(i)+":"+format.format(i1);
                    clnToTime.setVisibility(View.VISIBLE);
                }
            }
        },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    public void maxminMapClicked(View view) {
        RelativeLayout vtsF = findViewById(R.id.relvtsgoogleMapReplay);
        ImageView maxminimg = view.findViewById(R.id.maxminMapIconReplay);
        if (maxminimg.getTag().equals("min")) {
            FromDateLayout.setVisibility(View.GONE);
            ToDateLayout.setVisibility(View.GONE);
            Track.setVisibility(View.GONE);
            vtsF.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
            maxminimg.setImageResource(R.drawable.minimize);
            maxminimg.setTag("max");
        } else {
            FromDateLayout.setVisibility(View.VISIBLE);
            ToDateLayout.setVisibility(View.VISIBLE);
            Track.setVisibility(View.VISIBLE);
            vtsF.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
            maxminimg.setImageResource(R.drawable.maximize);
            maxminimg.setTag("min");
        }
    }


    private void ReplayCar(List<ReplayLatLng> animatedLatLng) {
        distanceLayout.setVisibility(View.GONE);
        speedTextviewReplay.setVisibility(View.GONE);
        IgnitionTextviewReplay.setVisibility(View.GONE);
        AcTextviewReplay.setVisibility(View.GONE);
        DateTimeTextviewReplay.setVisibility(View.GONE);
        RegNoTextviewReplay.setVisibility(View.GONE);
        progressDone.setVisibility(View.GONE);
        linProgress.setVisibility(View.VISIBLE);
        trckProgressIcon.startAnimation(animation);
        trckProgressIcon.setText(context.getString(R.string.fa_spinner));
        replyinpro.setText("  Replay in Progress");
        m = 0;
        handler = new Handler();
        StartAnimation();
        rotationCar((ArrayList<ReplayLatLng>) animatedLatLng);
    }


    public void rotationCar(ArrayList<ReplayLatLng> p) {
        final ArrayList<ReplayLatLng> Points = p;
        handler.post(runnable = new Runnable() {
            PolylineOptions lineOptions;

            @Override
            public void run() {
                lineOptions = new PolylineOptions();
                lineOptions.width(10);
                lineOptions.color(Color.parseColor("#00ff00"));

                if (m < Points.size() - 1) {
                    double srcLat = Double.parseDouble(Points.get(m).getLat());
                    double srcLng = Double.parseDouble(Points.get(m).getLang());
                    double destLat = Double.parseDouble(Points.get(m + 1).getLat());
                    double destLng = Double.parseDouble(Points.get(m + 1).getLang());
                    final LatLng startPosition = new LatLng(srcLat, srcLng);
                    final LatLng finalPosition = new LatLng(destLat, destLng);

                    lineOptions.add(startPosition);
                    lineOptions.add(finalPosition);
                    Polyline polyline = mMap.addPolyline(lineOptions);
                    polyline.setClickable(true);
                    if (animMarker != null) {
                        animMarker.setPosition(finalPosition);
                    }

                    Location srcLoc = new Location("");
                    srcLoc.setLatitude(srcLat);
                    srcLoc.setLongitude(srcLng);

                    float[] results = new float[1];
                    Location.distanceBetween(srcLat, srcLng, destLat, destLng, results);
                    distance = distance + results[0];
                    Log.d(TAG, "run: "+distance);
                    if (m < Points.size() - 2) {
                        setPanLatLng = finalPosition;
                        Location loc2 = new Location("");
                        loc2.setLatitude(Double.parseDouble(Points.get(m + 2).getLat()));
                        loc2.setLongitude(Double.parseDouble(Points.get(m + 2).getLang()));
                        float angle = srcLoc.bearingTo(loc2);

                        if (distance >= 300) {
                            distance = 0;
                        }
                        distance = 0;

                        if (animMarker != null) {
                            animMarker.setRotation(angle);
                        }
                    }
                    if (setPan) {
                        LatLng l = new LatLng(Double.parseDouble(Points.get(m).getLat()), Double.parseDouble(Points.get(m).getLang()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 16));
                    }
                    m++;
                    handler.postDelayed(runnable, speed);
                } else {
                    StopAnimation();
                }
            }
        });
    }

    private void StartAnimation() {
        setPan = true;
        distanceLayout.setVisibility(View.GONE);
        double fstLat = latlnglistNew!=null?Double.parseDouble(latlnglistNew.getLat()):0;
        double fstLng = latlnglistNew!=null?Double.parseDouble(latlnglistNew.getLang()):0;
        LatLng l = new LatLng(Double.parseDouble(animatedLatLngNew.get(0).getLat()), Double.parseDouble(animatedLatLngNew.get(0).getLang()));

        LatLng firstLatLng = new LatLng(fstLat, fstLng);
        flagMarkerOption = new MarkerOptions()
                .title("A")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(l);
        try {
            InfoWindowData infonewww = new InfoWindowData();
            infonewww.setLocation("");
            infonewww.setIgnition("");
            infonewww.setSpeed("");
            infonewww.setDate("");
            infonewww.setLocation("");
            infonewww.setNotiType("");
            infonewww.setAc("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        flagMarker = mMap.addMarker(flagMarkerOption);
        customMarkerView = LayoutInflater.from(this).inflate(R.layout.svg_icon_layout, null);  //SVG ICON SET HERE

        if (vehicle_type.equalsIgnoreCase("8")) {
            animMarkerOption = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon)))
                    .position(firstLatLng);
        } else {
            animMarkerOption = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon)))
                    .position(firstLatLng);
        }

        Location location = new Location("Test");
        location.setLatitude(fstLat);
        location.setLongitude(fstLng);
        getAddressFromLatLng(location);
        if (animMarker!=null)
            animMarker.remove();
        animMarker = mMap.addMarker(animMarkerOption);
        animMarker.setFlat(true);
        animMarker.setVisible(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 16));
    }

    private void StopAnimation() {
        try {
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            progressDone.setVisibility(View.VISIBLE);
            linProgress.setVisibility(View.GONE);
            ic_pro_Done.setText(context.getString(R.string.fa_check_circle_o) + " Replay done  ");

            flagMarkerOption.title("A").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            LatLng l = new LatLng(Double.parseDouble(animatedLatLngNew.get(animatedLatLngNew.size() - 1).getLat()), Double.parseDouble(animatedLatLngNew.get(animatedLatLngNew.size() - 1).getLang()));

            flagMarkerOption.position(l);

            if (animMarker != null) {
                animMarker.setPosition(l);
            }
            flagMarker = mMap.addMarker(flagMarkerOption);

            distanceLayout.setVisibility(View.VISIBLE);
            double totalDistanceInKm = totalDistance / 1000;
            String kmdist = String.format(Locale.ENGLISH,"%.2f", totalDistanceInKm);
            totalDistTxt.setText(kmdist + " Km");
            totalDistTxt.setText(totalDistance + " Km");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (animMarker != null) {
            animMarker.remove();
        }
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setMyLocationEnabled(true);
            mMap.setPadding(0, 0, 0, 200);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
                if (callFromDashboard != null && callFromDashboard.equals("Yes")) {
                    String vehicleLastLat = infoPref.getString("vehicleLastLat", PrefEnum.Login);
                    String vehicleLastLng = infoPref.getString("vehicleLastLng", PrefEnum.Login);
                    if (!vehicleLastLat.equals("") && !vehicleLastLng.equals("")) {
                        double lat = Double.parseDouble(vehicleLastLat);
                        double lng = Double.parseDouble(vehicleLastLng);
                        LatLng ln = new LatLng(lat, lng);

                        animMarkerOption = new MarkerOptions()
                                .title("A")
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon)))
                                .position(ln);
                        setPanLatLng = new LatLng(lat, lng);
                        animMarker = mMap.addMarker(animMarkerOption);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15.0f));
                        animMarker.setFlat(true);
                        animMarker.setVisible(true);
                    } else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.1028396, 72.9012723), 15.0f));
                    }
                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.1028396, 72.9012723), 10));
                }

                Crashlytics.log("click_VehicleValue Replay" + click_VehicleValue);
                Crashlytics.log("user_type_id Replay " + user_type_id);
                Crashlytics.log("id Replay" + id);

                if (Alrm.equalsIgnoreCase("y")) {
                    Real_Time_Tracking_function_for_on_Replay_FOR_start_point(user_type_id, id, "y", Vehicle_id_Alrm,infoPref.getString("GT_PLAYER_ID",PrefEnum.OneSignal),infoPref.getString("record_id",PrefEnum.OneSignal));
                } else if (replay_notification.equalsIgnoreCase("y")) {
                    Real_Time_Tracking_function_for_on_Replay_FOR_start_point(user_type_id, id, "y", VehicleValueNotification, infoPref.getString("GT_PLAYER_ID",PrefEnum.OneSignal), infoPref.getString("record_id",PrefEnum.OneSignal));
                } else {
                    Real_Time_Tracking_function_for_on_Replay_FOR_start_point(user_type_id, id, "y",click_VehicleValue ,infoPref.getString("GT_PLAYER_ID",PrefEnum.OneSignal),infoPref.getString("record_id",PrefEnum.OneSignal));
                }

            Track.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchaddress=true;
                    totalDistance = 0;
                    distance = 0;
                    if (animMarker != null) {
                        animMarker.remove();
                    }
                    if (runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                    m = 0;

                    if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate) || TextUtils.isEmpty(fromTime) || TextUtils.isEmpty(toTime)){
                        Toast.makeText(context, "All fields required!!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String finalFromDate = fromDate+" "+fromTime+":00";
                        String finalToDate = toDate+" "+toTime+":00";

                        Date from = dateFormat.parse(finalFromDate);
                        Date to = dateFormat.parse(finalToDate);
                        long diff = to.getTime() - from.getTime();
                        long diffHours = diff / (60 * 60 * 1000);
                        if (diffHours<0){
                            Toast.makeText(context, "To time must be greater than from time.", Toast.LENGTH_SHORT).show();
                            ed_to_time.setText("");
                            clnToTime.setVisibility(View.GONE);
                            toTime="";
                            return;
                        }
                        else if (diffHours==0){
                            if (diff/(60*1000)>1)
                                Real_Time_VehicleMovement_function(finalFromDate, finalToDate, user_type_id, id, click_VehicleValue);
                            else
                                Toast.makeText(context, "Select valid time.", Toast.LENGTH_SHORT).show();
                        }
                        else if (diffHours>24 || diffHours<1){
                            Toast.makeText(context, "Please select duration of 24 hours or less.", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            Real_Time_VehicleMovement_function(finalFromDate, finalToDate, user_type_id, id, click_VehicleValue);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            });


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(fetchaddress==true) {
                        Location location = new Location("Test");
                        location.setLatitude(latLng.latitude);
                        location.setLongitude(latLng.longitude);
                        getAddressFromLatLng(location);
                        //new GetAddressTask(ReplayTrackingActivity.this).execute(location);
                    }
                }

            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    String aValue2 = marker.getTitle();
                    LatLng lg = (marker.getPosition());
                    if (aValue2!=null && lg!=null) {
                        if (aValue2.equalsIgnoreCase("A")) {
                            marker.hideInfoWindow();
                            Location location = new Location("Test");
                            location.setLatitude(lg.latitude);
                            location.setLongitude(lg.longitude);
                            getAddressFromLatLng(location);
                            //new GetAddressTask(ReplayTrackingActivity.this).execute(location);
                        } else if (aValue2.equalsIgnoreCase("C")) {
                            marker.showInfoWindow();
                        } else if (aValue2.equalsIgnoreCase("D")) {
                            marker.showInfoWindow();
                        }
                    }
                    return true;
                }
            });


            changeMap.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMap != null) {
                        if (maptType.equals("normal")) {
                            changeMap.setTextColor(ContextCompat.getColor(context, R.color.dark_green));
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            maptType = "hybrid";
                        } else if (maptType.equals("hybrid")) {
                            changeMap.setTextColor(ContextCompat.getColor(context, R.color.white_color));
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            maptType = "normal";
                        }
                    }
                }
            });


            setPanButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPanButton.setVisibility(View.GONE);
                    if (setPanLatLng != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(setPanLatLng));
                    } else {
                        Toast.makeText(context, R.string.last_loc_not_found, Toast.LENGTH_SHORT).show();
                    }
                    setPan = true;
                }
            });
            rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                boolean f = true;
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                    speed = Integer.parseInt(rightPinValue);
                    if (handler != null && animatedLatLngNew != null && animatedLatLngNew.size() > 0) {
                        if (speed == 200 && handler != null) {
                            handler.removeCallbacks(runnable);
                            f = false;
                        } else if (speed != 200 && f == false) {
                            f = true;
                            handler = new Handler();
                            rotationCar((ArrayList<ReplayLatLng>) animatedLatLngNew);
                            handler.postDelayed(runnable, speed);
                        } else if ((handler != null) && f == true) {
                            handler.removeCallbacks(runnable);
                            handler = new Handler();
                            rotationCar((ArrayList<ReplayLatLng>) animatedLatLngNew);
                            handler.postDelayed(runnable, speed);
                        }
                    }
                }

            });
    }

    public void Real_Time_VehicleMovement_function(String formdate, String toDate, String usertype_id, final String user_id, final String vehicle_id) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        RetrofitInterface objRetrofitInterface = ApiClient.getClientForReplay().create(RetrofitInterface.class);
        Call<ReplayResponse> call = objRetrofitInterface.fnGetRealTimeVehicleReplayDataDevice(auth, apiKey, formdate, toDate, usertype_id, user_id, vehicle_id);
        call.enqueue(new Callback<ReplayResponse>() {
            @Override
            public void onResponse(Call<ReplayResponse> call, Response<ReplayResponse> response) {
                ReplayResponse replay = response.body();
                if (replay!=null && replay.getStatus().equalsIgnoreCase("success")){
                    if (replay.getTotalDistance()!=null)
                    totalDistance = Double.parseDouble(replay.getTotalDistance());
                    List<NotificationData> notificationData =replay.getNotificationData();
                    if (notificationData!=null) {
                        for (int i = 0; i < notificationData.size(); i++) {
                            String id = notificationData.get(i).getId();
                            String notification_type = notificationData.get(i).getNotificationType();
                            String notification_subtype = notificationData.get(i).getNotificationSubtype();
                            String msg = notificationData.get(i).getMsg();
                            String vehicle_lat = notificationData.get(i).getVehicleLat();
                            String vehicle_long = notificationData.get(i).getVehicleLong();
                            String ign = notificationData.get(i).getIgn();
                            String ac = notificationData.get(i).getAc();
                            String speed = notificationData.get(i).getSpeed();
                            String location = notificationData.get(i).getLocation();
                            String date_time = notificationData.get(i).getDateTime();
                            Notification1 notification_pojo = new Notification1(id, notification_type, notification_subtype, msg, vehicle_lat, vehicle_long, ign, ac, speed, location, date_time, "", "");
                            notification_pojo.setId(id);
                            notification_pojo.setNotification_type(notification_type);
                            notification_pojo.setNotification_subtype(notification_subtype);
                            notification_pojo.setMsg(msg);
                            notification_pojo.setVehicle_lat(vehicle_lat);
                            notification_pojo.setVehicle_long(vehicle_long);
                            notification_pojo.setIgn(ign);
                            notification_pojo.setAc(ac);
                            notification_pojo.setSpeed(speed);
                            notification_pojo.setLocation(location);
                            notification_pojo.setDate_time(date_time);
                            notification_pojo.setSnap_lat(vehicle_lat);
                            notification_pojo.setSnap_long(vehicle_long);
                            arrayList_notification.add(notification_pojo);
                            Log.e("array_notifi_size", String.valueOf(arrayList_notification.size()));
                        }
                    }
                    if (mMap != null) {
                        mMap.clear();
                    }

                    for (int marker = 0; marker < arrayList_notification.size(); marker++) {
                        next_TrackMsg = TrackMsg;
                        next_lat = Track_latitide;
                        next_lng = Track_longitute;
                        InfoWindowData info = new InfoWindowData();
                        info.setIgnition(arrayList_notification.get(marker).getIgn());
                        info.setSpeed(arrayList_notification.get(marker).getSpeed());
                        info.setDate(arrayList_notification.get(marker).getDate_time());
                        info.setLocation(arrayList_notification.get(marker).getLocation());
                        info.setNotiType(arrayList_notification.get(marker).getMsg());
                        info.setAc(arrayList_notification.get(marker).getAc());
                        info.setNotiTypeforMarker(arrayList_notification.get(marker).getNotification_type());
                        Track_latitide = (arrayList_notification.get(marker).getSnap_lat());
                        Track_longitute = (arrayList_notification.get(marker).getSnap_long());
                        TrackMsg = arrayList_notification.get(marker).getMsg();

                        if (!next_lat.equalsIgnoreCase("")) {
                            if (Track_latitide.equalsIgnoreCase(next_lat)) {
                                TrackMsg = arrayList_notification.get(marker).getMsg() + "#" + next_TrackMsg;
                                info.setNotiType(TrackMsg);
                                trackpoint = new LatLng(Double.parseDouble(Track_latitide), Double.parseDouble(Track_longitute));

                            } else {
                                TrackMsg = arrayList_notification.get(marker).getMsg();
                                info.setNotiType(next_TrackMsg);
                                trackpoint = new LatLng(Double.parseDouble(next_lat), Double.parseDouble(next_lng));
                            }

                            markerOptions = new MarkerOptions();
                            if (arrayList_notification.get(marker).getNotification_type().equalsIgnoreCase("ignition")) {
                                markerOptions.position(trackpoint).title("C")
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_skyblue)));
                            } else if (arrayList_notification.get(marker).getNotification_type().equalsIgnoreCase("vehicle_idle")) {
                                markerOptions.position(trackpoint).title("C")
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_orange)));
                            } else if (arrayList_notification.get(marker).getNotification_type().equalsIgnoreCase("ac_idle")) {
                                markerOptions.position(trackpoint).title("C")
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_orange)));
                            } else if (arrayList_notification.get(marker).getNotification_type().equalsIgnoreCase("geofence")) {
                                markerOptions.position(trackpoint).title("C")
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_violet)));
                            } else if (arrayList_notification.get(marker).getNotification_type().equalsIgnoreCase("device_tamper")) {
                                markerOptions.position(trackpoint).title("C")
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_red)));
                            } else if (arrayList_notification.get(marker).getNotification_type().equalsIgnoreCase("towing")) {
                                markerOptions.position(trackpoint).title("C")
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_yellow)));
                            }

                            Log.e("notification.size()-1", " " + (arrayList_notification.size() - 1));

                            if (next_lat.equalsIgnoreCase(Track_latitide)) {
                                if (marker == arrayList_notification.size() - 1) {
                                    markerreplay = mMap.addMarker(markerOptions);
                                    markerreplay.setTag(info);
                                    CustomInfoViewAdapter customInfoWindow = new CustomInfoViewAdapter(context);
                                    mMap.setInfoWindowAdapter(customInfoWindow);
                                } else {
                                    continue;
                                }
                            } else {
                                if (marker != 0) {
                                    markerreplay = mMap.addMarker(markerOptions);
                                    markerreplay.setTag(info);
                                    CustomInfoViewAdapter customInfoWindow = new CustomInfoViewAdapter(context);
                                    mMap.setInfoWindowAdapter(customInfoWindow);
                                }
                            }
                        }
                    }

                    if (replay.getRealtimeData() != null) {
                        latlnglistNew = replay.getRealtimeData();
                        animatedLatLngNew = replay.getResult();
                        totalDistance = Double.parseDouble(replay.getTotalDistance());

                        if (latlnglistNew==null) {
                            Toast.makeText(context, "No Tracking Data Found.", Toast.LENGTH_LONG).show();
                            progressDone.setVisibility(View.GONE);
                            linProgress.setVisibility(View.GONE);
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        } else {
                            if (animatedLatLngNew.size() > 0) {
                                hereRouteMatchRequest(animatedLatLngNew);
                            } else {
                                Toast.makeText(context, "No Tracking Data Found.", Toast.LENGTH_LONG).show();
                                progressDone.setVisibility(View.GONE);
                                linProgress.setVisibility(View.GONE);
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        }
                    } else if (latlnglistNew != null) {
                        progressDone.setVisibility(View.GONE);
                        linProgress.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(context, context.getString(R.string.unknown_error_message), Toast.LENGTH_LONG).show();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }

                }else {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (replay!=null) {
                        ReplayError error = replay.getError();
                        if (error.getFromDate() != null) {
                            Toast.makeText(context, error.getFromDate(), Toast.LENGTH_LONG).show();
                        } else if (error.getToDate() != null) {
                            Toast.makeText(context, error.getToDate(), Toast.LENGTH_LONG).show();
                        } else if (error.getUserId() != null) {
                            Toast.makeText(context, error.getUserId(), Toast.LENGTH_LONG).show();
                        } else if (error.getUserTypeId() != null) {
                            Toast.makeText(context, error.getUserTypeId(), Toast.LENGTH_LONG).show();
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<ReplayResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

    }

    public void Real_Time_Tracking_function_for_on_Replay_FOR_start_point(String usertype_id, final String user_id, final String is_first_req, final String vehicle_id, final String player_id, final String device_id) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait...");
            dialog.show();
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<GeofenceResponse> call = objRetrofitInterface.fnGetRealTimeDataDevice(auth, apiKey, usertype_id, user_id, is_first_req, vehicle_id,player_id,device_id);
            call.enqueue(new Callback<GeofenceResponse>() {
                @Override
                public void onResponse(Call<GeofenceResponse> call, Response<GeofenceResponse> response) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    GeofenceResponse data = response.body();
                    if (response.isSuccessful() && data!=null){
                        returnedData = data.getRealtimeData();
                        if (data.getStatus()!=null && data.getStatus().equalsIgnoreCase("success")){
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            if (mMap != null) {
                                mMap.clear();
                            }

                            if (returnedData != null) {
                                latlnglistfortrack = returnedData;
                                vehicle_type = latlnglistfortrack.getVehicleTypeId();
                                speedTextviewReplay.setText(latlnglistfortrack.getSpeed() + " km/hr");
                                if (latlnglistfortrack.getIgn() != null) {
                                    if (latlnglistfortrack.getIgn().equalsIgnoreCase("1")) {
                                        IgnitionTextviewReplay.setText("|" + "  " + "Ignition:" + "ON");
                                    } else {
                                        IgnitionTextviewReplay.setText("|" + "  " + "Ignition:" + "OFF");
                                    }
                                }
                                if (!vehicle_type.equalsIgnoreCase("8")) {
                                    if (latlnglistfortrack.getAc() != null) {
                                        if (latlnglistfortrack.getAc().equalsIgnoreCase("1")) {
                                            AcTextviewReplay.setText("|" + "  " + "AC:" + "ON");
                                        } else {
                                            AcTextviewReplay.setText("|" + "  " + "AC:" + "OFF");
                                        }
                                    }
                                }

                                DateTimeTextviewReplay.setText(String.valueOf(latlnglistfortrack.getTrackerDatetime()));
                                VehNameReplay.setText("Vehicle No:" + latlnglistfortrack.getVehicleNo());
                                TrackerLat = Double.parseDouble(String.valueOf(latlnglistfortrack.getLat()));
                                TrackerLong = Double.parseDouble(String.valueOf(latlnglistfortrack.getLang()));

                                if (TrackerLat != null && TrackerLong != null) {
                                    MarkerOptions optionsVehAlert = new MarkerOptions();
                                    LatLng LLVehAlert = new LatLng(TrackerLat, TrackerLong);
                                    setPanLatLng = new LatLng(TrackerLat, TrackerLong);
                                    optionsVehAlert.position(LLVehAlert);
                                    if (vehicle_type.equalsIgnoreCase("8")) {
                                        optionsVehAlert.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon)));
                                    } else {
                                        optionsVehAlert.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon)));
                                    }
                                    mMap.addMarker(optionsVehAlert);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TrackerLat, TrackerLong), 10));
                                    if(fetchaddress==true) {
                                        Location location = new Location("Test");
                                        location.setLatitude(TrackerLat);
                                        location.setLongitude(TrackerLong);
                                        getAddressFromLatLng(location);
                                        //new GetAddressTask(ReplayTrackingActivity.this).execute(location);
                                    }

                                } else {
                                    Toast.makeText(context, "No tracking data found", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeofenceResponse> call, Throwable t) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
    }


    public void vtsCarMapClicked(View view) {
        Intent intent = new Intent(context, VTSSetVehicalActivity.class);
        intent.putExtra("Call","CallFromReplay");
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                supportMapFragment.getMapAsync(this);
                if (mMap != null) {
                    mMap.clear();
                }
                if (!cc.isConnected(context)) {
                    cc.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
                }
            }
        }
    }

    private void getAddressFromLatLng(final Location location){
        GeoCoordinate coordinate = new GeoCoordinate( location.getLatitude(), location.getLongitude());
        ReverseGeocodeRequest request = new ReverseGeocodeRequest(coordinate);
        request.execute(new ResultListener<Address>() {
            @Override
            public void onCompleted(Address address, ErrorCode errorCode) {
                LatLng latlonggadd = new LatLng(location.getLatitude(),location.getLongitude());
                InfoWindowData mapclickdata = new InfoWindowData();
                mapclickdata.setLocation(address.getText());
                markerOptions = new MarkerOptions();
                markerOptions.position(latlonggadd).title("D")
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_skyblue)));
                markerreplayMapClick = mMap.addMarker(markerOptions);
                markerreplayMapClick.setTag(mapclickdata);

                CustomInfoViewAdapter customInfoWindow = new CustomInfoViewAdapter(context);
                mMap.setInfoWindowAdapter(customInfoWindow);
                markerreplayMapClick.setAlpha(0.0f);
                markerreplayMapClick.setInfoWindowAnchor(.5f,1.0f);
                markerreplayMapClick.showInfoWindow();
            }
        });
    }

    private void hereRouteMatchRequest(List<ReplayLatLng> latLngList){
        String latLongs = "LATITUDE,LONGITUDE"+"\n";
        for (int i = 0; i <latLngList.size() ; i++) {
            ReplayLatLng latLng = latLngList.get(i);
            latLongs+=latLng.getLat()+","+latLng.getLang()+"\n";
        }
        String base64LatLngs = Base64.encodeToString(latLongs.getBytes(), android.util.Base64.DEFAULT);
        String url = "https://rme.api.here.com/2/matchroute.json?app_id=8jmFsrdU1zCTiuB1oYr5&app_code=FQmm3YCKNLpGTTHcqXhHsQ&routemode=car&file="+base64LatLngs;
        Call<RouteResponse> call = ApiClient.getClient().create(RetrofitInterface.class).getLatLangFromHere(url);
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RouteLink> routes = response.body().getRouteLinks();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (routes != null && routes.size() > 0) {
                        List<ReplayLatLng> latLngs = new ArrayList<>();
                        for (int i = 0; i < routes.size(); i++) {
                            RouteLink link = routes.get(i);
                            if (link.getShape() != null) {
                                String[] shape = link.getShape().split(" ");
                                for (int j = 0; j < shape.length; j = j + 2) {
                                    latLngs.add(new ReplayLatLng(shape[j], shape[j + 1]));
                                }
                            }
                        }
                        animatedLatLngNew = latLngs;
                        ReplayCar(latLngs);
                    }
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(ReplayTrackingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logPlacesApiError(String err){
        auth = auth.replace("\n", "");
        RetrofitInterface obj = ApiClient.getClient().create(RetrofitInterface.class);
        Call<ResponseBody> call = obj.uploadNearByPlacesApiError(auth,apiKey,err,"Replay","android", BuildConfig.VERSION_NAME);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: "+response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}