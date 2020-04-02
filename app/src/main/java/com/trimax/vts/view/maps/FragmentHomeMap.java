package com.trimax.vts.view.maps;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.Address;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.helper.MapWrapperLayout;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.maps.adapters.customInfoWindowMap;
import com.trimax.vts.view.model.GeoFenceModel;
import com.trimax.vts.view.model.GeofenceResponse;
import com.trimax.vts.view.model.RealtimeDatum;
import com.trimax.vts.model.InfoWindowData;
import com.trimax.vts.services.AppLocationService;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.view.model.replay.RealtimeData;
import com.trimax.vts.view.model.route.RouteLink;
import com.trimax.vts.view.model.route.RouteResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.maps.model.JointType.ROUND;
import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;
import static com.trimax.vts.utils.CommonClass.bodyToString;

public class FragmentHomeMap extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "FragmentHomeMap";

    TextView mapMessage, speedTextview, vehRegNoTextview, vehVehName, vehIgnitionTextview, vehAcTextview, vehDateTimeTextview;
    TextView getLocTxt, mapSymolTxt, setPanButton;
    LinearLayout carDetTxt, mapMsgLayout;
    ImageView changeMap, googleToolbar;
    View customMarkerView;

    Activity ctx;
    public ArrayList<LatLng> markerPoints, polylatlnglist;
    CommonClass commonClass;
    String fromdashboard = "", click_VehicleValue = "", SPCurrLat, SPCurrLng;

    GeofenceResponse returnedData;
    String user_type_id = "", id = "", player_id = "", devcie_id = "", mapType = "normal", GetCircleString = "";
    RealtimeData realtimeData;

    GoogleMap mMap;
    PolygonOptions polygonOptions = new PolygonOptions();
    List<Polyline> polylines = new ArrayList<Polyline>();
    private int count = 0,  polyCount = 0;
    private String user_id="",vehicleTypeId="";
    Timer timer;

    ArrayList<GeoFenceModel> geofence;
    TimerTask timerTask;
    CustomMapFragment supportMapFragment;
    MarkerOptions animMarkerOption;
    LatLng PreviousLastLatlong, setPanLatLng;
    ArrayList<ArrayList<LatLng>> data;
    public ArrayList<RealtimeData> wholeData;

    Marker animMarker, firstMarker, greenMarker, markerLocationAddress;
    boolean initialState = true, drawPolyMode = false, draw_geofence = false;
    ArrayList<Polyline> finalPolyline;
    ArrayList<PolylineOptions> polylineOptions;
    ArrayList<Marker> polyMarkerList, polyMarkerEdgeList;
    ArrayList<Polygon> polygonList;
    Typeface font_awesome;
    public boolean pointRepeated = true, setPan = true, isShowRouteclick = false;

    public final Handler handler = new Handler();
    PolylineOptions lineOptions;
    private List<LatLng> finalLatLngList;

    private int unRefreshCount = 0;
    private LatLng lastLatLng = null;
    private List<LatLng> currentLatLngList,oldLatLngList;
    private PolylineOptions polylineOption;
    private TravelpulseInfoPref infoPref;
    private MarkerOptions markerOptions;
    private boolean isUserReturn=false;

    public void Index(final Activity context, CustomMapFragment supportMapFragment1, View customMarkerView) {
        ctx = context;
        finalLatLngList = new ArrayList<>();
        oldLatLngList = new ArrayList<>();
        polylatlnglist = new ArrayList<>();
        infoPref = new TravelpulseInfoPref(context);

        font_awesome = Typeface.createFromAsset(ctx.getAssets(), "fontawesome-webfont.ttf");
        user_id = infoPref.getString("id", PrefEnum.Login);
        click_VehicleValue = infoPref.getString("vid", PrefEnum.Login);
        user_type_id = infoPref.getString("user_type_id", PrefEnum.Login);
        id = infoPref.getString("id", PrefEnum.Login);
        player_id = infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
        devcie_id = infoPref.getString("deviceid", PrefEnum.OneSignal);

        commonClass = new CommonClass(ctx);
        this.customMarkerView = customMarkerView;
        supportMapFragment = supportMapFragment1;
        supportMapFragment.getMapAsync(this);
        mapMessage = ((Activity) ctx).findViewById(R.id.mapMessage);

        speedTextview = ((Activity) ctx).findViewById(R.id.txtVehSpeed);
        vehRegNoTextview = ((Activity) ctx).findViewById(R.id.txtVehRegNo);
        vehVehName = ((Activity) ctx).findViewById(R.id.txtVehName);
        vehIgnitionTextview = ((Activity) ctx).findViewById(R.id.txtVehIgnition);
        vehAcTextview = ((Activity) ctx).findViewById(R.id.txtVehAc);
        vehDateTimeTextview = ((Activity) ctx).findViewById(R.id.txtVehDateTime);
        changeMap = ((Activity) ctx).findViewById(R.id.mapchangeID);
        setPanButton = (TextView) ((Activity) ctx).findViewById(R.id.setPanId);
        setPanButton.setVisibility(View.GONE);
        setPanButton.setTypeface(font_awesome);
        googleToolbar = ((Activity) ctx).findViewById(R.id.toolId);

        isShowRouteclick = false;
        carDetTxt = ((Activity) ctx).findViewById(R.id.linearTxtId);
        mapMsgLayout = ((Activity) ctx).findViewById(R.id.lineMapMsgID);
        mapSymolTxt = ((Activity) ctx).findViewById(R.id.mapIdSymbol);
        getLocTxt = ((Activity) ctx).findViewById(R.id.getLocaID);


        mapSymolTxt.setTypeface(font_awesome);
        polylatlnglist = new ArrayList<>();
        polyMarkerList = new ArrayList<>();
        polyMarkerEdgeList = new ArrayList<>();
        polygonList = new ArrayList<>();
        mapMessage.setVisibility(View.GONE);

        animMarkerOption = new MarkerOptions();
        markerPoints = null;
        pointRepeated = false;
        if (!isGooglePlayServicesAvailable()) {
            CommonClass.DisplayToast(ctx, "Google play service unavailable.", "bottom");
            finish();
            return;
        }
        if (!commonClass.isConnected(ctx)) {
            CommonClass.DisplayToast(ctx, ctx.getString(R.string.network_error_message) + "no connection", "bottom");
            carDetTxt.setVisibility(View.GONE);
            getLocTxt.setVisibility(View.GONE);
        }
        markerPoints = new ArrayList<>();
        Crashlytics.log("Before TimerTask");
        startTimer(supportMapFragment1, ctx);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, (Activity) ctx, 2).show();
            return false;
        }
    }

    public void startTimer(CustomMapFragment supportMapFragment1, Activity ctx) {
        Log.d(TAG, "startTimer: ");
        this.ctx = ctx;
        if (!isGooglePlayServicesAvailable()) {
            commonClass.DisplayToast(ctx, "Google play service unavailable.", "bottom");
            finish();
            return;
        }

        carDetTxt = ((Activity) ctx).findViewById(R.id.linearTxtId);
        getLocTxt = ((Activity) ctx).findViewById(R.id.getLocaID);
        getLocTxt.setVisibility(View.VISIBLE);
        getLocTxt.setText("  Fetching Location . . .");
        carDetTxt.setVisibility(View.GONE);

        animMarkerOption = new MarkerOptions();
        markerPoints = null;
        pointRepeated = false;
        data = new ArrayList<ArrayList<LatLng>>(1);
        wholeData = new ArrayList<>();
        data.add(new ArrayList<LatLng>());
        polylines.clear();
        initialState = true;
        if (animMarker != null) {
            animMarker.remove();
        }
        if (greenMarker != null) {
            greenMarker.remove();
        }
        animMarker = null;
        greenMarker = null;
        if (finalPolyline != null) {
            for (int i = 0; i < finalPolyline.size(); i++) {
                finalPolyline.get(i).remove();
            }
            finalPolyline = new ArrayList<>();
        }
        polylineOptions = new ArrayList<>();
        lineOptions = new PolylineOptions();

        PreviousLastLatlong = null;
        stopTimerTask();

        supportMapFragment = supportMapFragment1;
        supportMapFragment.getMapAsync(this);
        if (setPanLatLng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(setPanLatLng, 15.0f));
        }

        Crashlytics.log("Intialize TimerTask");
        isUserReturn=true;
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 10000);
    }

    @Override
    protected void onResume() {
        isUserReturn=true;
        initializeTimerTask();
        startTimer(supportMapFragment, ctx);
        super.onResume();
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {
                handler.post(new Runnable() {

                    public void run() {
                        if (commonClass.isConnected(ctx)) {
                            if (markerPoints == null) {
                                markerPoints = new ArrayList<LatLng>();
                            }
                            if (!mMap.getUiSettings().isZoomControlsEnabled()) {
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                            }
                            fromdashboard = infoPref.getString("livetrack", PrefEnum.Login);
                            if (fromdashboard.equalsIgnoreCase("y")) {
                                Real_Time_Tracking_function(user_type_id, id, "Y", "");
                            } else {
                                Real_Time_Tracking_function(user_type_id, id, "Y", click_VehicleValue);
                            }
                        } else {
                            getLocTxt.setVisibility(View.GONE);
                            mapMsgLayout.setVisibility(View.VISIBLE);
                            mapMessage.setVisibility(mapMessage.VISIBLE);
                            mapSymolTxt.setVisibility(View.VISIBLE);
                            mapSymolTxt.setText(R.string.wifi_symbol);
                            mapSymolTxt.setTextColor(Color.parseColor("#A32A10"));
                            mapMessage.setText(ctx.getString(R.string.network_error_message));
                        }
                    }

                });
            }

        };
    }


    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {
        ImageView markerImageView = customMarkerView.findViewById(R.id.profile_image);
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

    public void pointsInPolygon() {
        boolean flag = false;
        ArrayList<LatLng> finalpolyList = new ArrayList<>();
        if (wholeData != null) {
            if (wholeData.size() != 0) {
                MarkerOptions options1 = new MarkerOptions();
                options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon));
                for (int k = 0; k < wholeData.size(); k++) {
                    finalpolyList.add(new LatLng(Double.parseDouble(wholeData.get(k).getLat()), Double.parseDouble(wholeData.get(k).getLang())));

                }
                for (int i = 0; i < finalpolyList.size(); i++) {
                    boolean contain = PolyUtil.containsLocation(finalpolyList.get(i), polylatlnglist, true);
                    if (contain) {
                        polyMarkerList.add(mMap.addMarker(options1.position(finalpolyList.get(i))));
                        flag = true;
                    }
                }
                if (flag != true) {
                    commonClass.DisplayToast(ctx, "No Points to show", "bottom");
                }
            } else {
                commonClass.DisplayToast(ctx, "No Points to Show", "bottom");
            }
        } else {
            commonClass.DisplayToast(ctx, "No Points to Show", "bottom");
        }
        polylatlnglist = new ArrayList<>();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopTimerTask();
    }

    void clearAll() {
        animMarkerOption = new MarkerOptions();
        markerPoints = null;
        pointRepeated = false;
        data = new ArrayList<ArrayList<LatLng>>(1);
        wholeData = new ArrayList<>();
        data.add(new ArrayList<LatLng>());
        polylines.clear();
        initialState = true;
        if (animMarker != null) {
            animMarker.remove();
        }
        if (greenMarker != null) {
            greenMarker.remove();
        }
        animMarker = null;
        greenMarker = null;
        if (finalPolyline != null) {
            for (int i = 0; i < finalPolyline.size(); i++) {
                finalPolyline.get(i).remove();
            }
            finalPolyline = new ArrayList<>();
        }
        polylineOptions = new ArrayList<>();
        lineOptions = new PolylineOptions();
        PreviousLastLatlong = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        googleMap.setPadding(0, 0, 0, 110);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        fromdashboard = infoPref.getString("livetrack", PrefEnum.Login);
        clearAll();
        if (setPanLatLng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(setPanLatLng, 15.0f));
        }
        AppLocationService appLocationService = new AppLocationService(ctx);
        Location _currLocation = appLocationService.getBestLocation();
        //if no location found then set andheri lat long
        if (_currLocation != null) {
            SPCurrLat = Double.toString(_currLocation.getLatitude());
            SPCurrLng = Double.toString(_currLocation.getLongitude());
        } else {
            SPCurrLat = "19.1028396";
            SPCurrLng = "72.9012723";
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(SPCurrLat), Double.parseDouble(SPCurrLng)), 15.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                if (markerLocationAddress != null) {
                    markerLocationAddress.remove();
                }
                if (drawPolyMode == true) {
                    polyCount++;
                    polylatlnglist.add(point);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(point);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon));
                    polyMarkerEdgeList.add(mMap.addMarker(markerOptions));
                    polygonOptions.add(point).strokeColor(Color.parseColor("#801E90FF")).strokeWidth(0);
                    if (polyCount == 1) {
                        firstMarker = mMap.addMarker(markerOptions);
                    }
                    if (polyCount > 1) {
                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .add(polygonOptions.getPoints().get(polyCount - 2), polygonOptions.getPoints().get(polyCount - 1))
                                .width(6)
                                .color(Color.parseColor("#801E90FF")));
                        polylines.add(line);
                    }
                }
            }

        });

        googleToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude = infoPref.getString("vehicleLastLat", PrefEnum.Login);
                String longitude = infoPref.getString("vehicleLastLng", PrefEnum.Login);
                if (TextUtils.isEmpty(latitude) && TextUtils.isEmpty(longitude)) {
                    Toast.makeText(ctx, "Couldn't find your location right now. Please retry after some time.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude + "&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(ctx.getPackageManager()) != null) {
                    ctx.startActivity(mapIntent);
                }

            }
        });

        setPanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPan = true;
                setPanButton.setVisibility(View.GONE);
                if (setPanLatLng != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(setPanLatLng));
                } else {
                    Toast.makeText(ctx, R.string.last_loc_not_found, Toast.LENGTH_SHORT).show();
                }
            }
        });

        supportMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {
                setPan = false;
                setPanButton.setVisibility(View.VISIBLE);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (firstMarker != null && marker.getPosition().equals(firstMarker.getPosition())) {
                    try {
                        if (polyCount > 2) {
                            polygonList.add(mMap.addPolygon(polygonOptions.fillColor(Color.parseColor("#801E90FF"))));
                            for (Polyline line : polylines) {
                                line.remove();
                            }
                            polylines.clear();
                            polyCount = 0;
                            polygonOptions = new PolygonOptions();
                            drawPolyMode = false;

                            firstMarker.remove();
                            if (polyMarkerEdgeList.size() != 0) {
                                for (int i = 0; i < polyMarkerEdgeList.size(); i++) {
                                    polyMarkerEdgeList.get(i).remove();
                                }
                            }
                            marker.remove();
                            pointsInPolygon();
                            //METHOD TO CHECK POINTS IN POLYGON
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                final double lat = marker.getPosition().latitude;
                final double lng = marker.getPosition().longitude;
                try {
                    if (wholeData != null) {
                        if (markerLocationAddress != null) {
                            markerLocationAddress.remove();
                        }

                        for (int i = 0; i < wholeData.size(); i++) {
                            if (Double.parseDouble(wholeData.get(i).getLat()) == lat && Double.parseDouble(wholeData.get(i).getLang()) == lng) {

                                    GeoCoordinate coordinate = new GeoCoordinate( lat, lng);
                                    ReverseGeocodeRequest request = new ReverseGeocodeRequest(coordinate);
                                    request.execute(new ResultListener<Address>() {
                                        @Override
                                        public void onCompleted(Address address, ErrorCode errorCode) {
                                            LatLng latlonggadd = new LatLng(lat,lng);
                                            markerOptions = new MarkerOptions();

                                            IconGenerator m = new IconGenerator(ctx);
                                            m.setColor(Color.parseColor("#ffffff"));
                                            m.setTextAppearance(Color.BLACK);
                                            Bitmap iconBitMap = m.makeIcon(address.getText());
                                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconBitMap));
                                            markerOptions.position(latlonggadd);
                                            markerLocationAddress = mMap.addMarker(markerOptions);

                                        }
                                    });
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            MarkerOptions markerOptions;

            @Override
            public void onPolylineClick(Polyline polyline) {
                List<LatLng> ll = polyline.getPoints();
                final double lat = ll.get(0).latitude;
                final double lng = ll.get(0).longitude;
                GeoCoordinate coordinate = new GeoCoordinate( lat, lng);
                ReverseGeocodeRequest request = new ReverseGeocodeRequest(coordinate);
                request.execute(new ResultListener<Address>() {
                    @Override
                    public void onCompleted(Address address, ErrorCode errorCode) {
                        LatLng latlonggadd = new LatLng(lat,lng);
                        markerOptions = new MarkerOptions();

                        IconGenerator m = new IconGenerator(ctx);
                        m.setColor(Color.parseColor("#ffffff"));
                        m.setTextAppearance(Color.BLACK);
                        Bitmap iconBitMap = m.makeIcon(address.getText());
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconBitMap));
                        markerOptions.position(latlonggadd);
                        markerLocationAddress = mMap.addMarker(markerOptions);

                    }
                });
            }
        });

        changeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    if (mapType.equals("hybrid")) {
                        changeMap.setImageResource(R.drawable.ic_map);
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        mapType = "normal";
                    } else if (mapType.equals("normal")) {
                        changeMap.setImageResource(R.drawable.ic_map_green);
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        mapType = "hybrid";
                    }
                }
            }
        });


// Draw all geofences on map
        GetAllFenceOnMap(user_type_id, id, "Y", click_VehicleValue);
    }

    public void Real_Time_Tracking_function(String usertype_id, final String user_id, final String is_first_req, final String vehicle_id) {
        RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
        Call<GeofenceResponse> call = objRetrofitInterface.fnGetRealTimeDataDevice(auth, apiKey, usertype_id, user_id, is_first_req, vehicle_id, player_id, devcie_id);
        call.enqueue(new Callback<GeofenceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<GeofenceResponse> call, Response<GeofenceResponse> response) {
                String vehRegNo = "";
                currentLatLngList = new ArrayList<>();
                if (response.isSuccessful() && response.body()!=null){
                    GeofenceResponse data = response.body();
                    if (data.getStatus()!=null && data.getStatus().equalsIgnoreCase("success")){
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        getLocTxt.setVisibility(View.GONE);
                        carDetTxt.setVisibility(View.VISIBLE);
                        realtimeData = data.getRealtimeData();
                        if (realtimeData!=null){
                            vehRegNo= realtimeData.getVehicleNo() ;
                            if (vehRegNo != null) {
                                vehRegNoTextview.setText( vehRegNo );
                            }
                            String vehIgnition = realtimeData.getIgn();
                            String gps = realtimeData.getGps();
                            String vehAc = realtimeData.getAc();
                            String trackerDateTime = realtimeData.getTrackerDatetime();
                            vehicleTypeId = realtimeData.getVehicleTypeId();
                            String speed = realtimeData.getSpeed();
                            String network = realtimeData.getNetwork();

                            InfoWindowData info = new InfoWindowData();
                            info.setLocation("Location:" + realtimeData.getLocation());
                            customInfoWindowMap customInfoWindow = new customInfoWindowMap(ctx);
                            mMap.setInfoWindowAdapter(customInfoWindow);


                            speedTextview.setText(speed + " KM/H ");
                            String vehicleInfo = "";
                            if (vehIgnition != null) {
                                if (vehIgnition.equalsIgnoreCase("1")) {
                                    vehicleInfo = "  |" + " " + "IGN:" + "ON";
                                } else {
                                    vehicleInfo = "  |" + " " + "IGN:" + "OFF";
                                }
                            }
                            vehIgnitionTextview.setText(vehicleInfo);
                            if (!vehicleTypeId.equalsIgnoreCase("8")) {
                                if (vehAc != null) {
                                    if (vehAc.equalsIgnoreCase("1")) {
                                        vehAcTextview.setText("|" + " " + "AC:" + "ON");
                                    } else {
                                        vehAcTextview.setText("|" + " " + "AC:" + "OFF");
                                    }
                                } else {
                                    vehAcTextview.setText("");
                                }
                            }
                            if (network.equalsIgnoreCase("1")) {
                                /*if (gps != null) {
                                    if (gps.equalsIgnoreCase("0")) {
                                        vehIgnitionTextview.setText("Waiting for network");
                                    }
                                } else {
                                    vehVehName.setText("");
                                }*/
                            } else {
                                vehAcTextview.setText("");
                                speedTextview.setText("");
                                vehIgnitionTextview.setText("Waiting for network");
                                vehIgnitionTextview.setTypeface(vehIgnitionTextview.getTypeface(),Typeface.BOLD);
                                vehIgnitionTextview.setTextSize(16);
                            }
                            if (trackerDateTime != null) {
                                vehDateTimeTextview.setText(trackerDateTime);
                            } else {
                                vehDateTimeTextview.setText("");
                            }

                            LatLng latLng = new LatLng(Double.parseDouble(realtimeData.getLat()),Double.parseDouble(realtimeData.getLang()));
                            final String uri = "http://maps.google.com/maps?q=" + latLng.latitude + "," + latLng.longitude + "&iwloc=A  ";
                            infoPref.putString("vehicleLastLat", String.valueOf(latLng.latitude), PrefEnum.Login);
                            infoPref.putString("vehicleLastLng", String.valueOf(latLng.longitude), PrefEnum.Login);
                            infoPref.putString("shareString", "TravelPulse \n" + vehRegNo + " (" + vehRegNo + ") \n" + "Speed: " + speed + " km/hr | Ignition: " + vehIgnition + " | AC: " + vehAc + "\nDateTime: " + trackerDateTime + " \n" + uri,PrefEnum.Login);

                            if (lastLatLng==null){
                                lastLatLng=latLng;
                                finalLatLngList.add(latLng);
                                currentLatLngList.add(latLng);
                                setPanLatLng=latLng;
                                Log.d(TAG, "onResponse:0 "+ realtimeData);

                                changeCarMarker(vehIgnition, speed, network, info,lastLatLng);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));

                                lineOptions = new PolylineOptions();
                                lineOptions.width(6);
                                lineOptions.color(Color.parseColor("#00ff00"));
                                lineOptions.add(latLng);
                            }
                            else {
                                    changeCarMarker(vehIgnition, speed, network, info,lastLatLng);
                                    if (!lastLatLng.equals(latLng)) {
                                        finalLatLngList.add(latLng);
                                        currentLatLngList.add(latLng);
                                        Log.d(TAG, "onResponse:1 "+ realtimeData);
                                        Log.d(TAG, "onResponse:old1 "+lastLatLng);
                                        if (Integer.parseInt(speed)>6)
                                            hereRouteMatchRequest(finalLatLngList);
                                        mapMsgLayout.setVisibility(View.GONE);
                                        mapSymolTxt.setVisibility(View.GONE);
                                        mapMessage.setVisibility(View.GONE);
                                        carDetTxt.setVisibility(View.VISIBLE);
                                        unRefreshCount=0;
                                    }else {
                                        unRefreshCount++;
                                        if (unRefreshCount>16){
                                            changeCarMarker(vehIgnition, speed, network, info,lastLatLng);
                                        }
                                    }
                                    lastLatLng=latLng;
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<GeofenceResponse> call, Throwable t) {
                Toast.makeText(FragmentHomeMap.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                // Utils.stopProgressDialog();
                // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

            }
        });
    }

    private void changeCarMarker(String vehIgnition, String speed, String network, InfoWindowData info,LatLng lastPostion) {
        mapMsgLayout.setVisibility(View.GONE);
        mapMessage.setVisibility(View.GONE);
        mapSymolTxt.setVisibility(View.GONE);
        if (isUserReturn){
            if (greenMarker!=null){
                greenMarker.remove();
            }else {
                MarkerOptions options = new MarkerOptions();
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                options.position(lastPostion);
                if (greenMarker == null) {
                    greenMarker = mMap.addMarker(options);
                    greenMarker.setTag(info);
                    greenMarker.showInfoWindow();
                }
            }
            isUserReturn=false;
        }
        if (animMarker!=null) {
            lastPostion = animMarker.getPosition();
            animMarker.remove();
        }
        animMarkerOption = new MarkerOptions();
            if (network.equalsIgnoreCase("1") && vehIgnition.equalsIgnoreCase("0") && speed.equalsIgnoreCase("0")){
                if (vehicleTypeId.equalsIgnoreCase("8"))
                    animMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.bike_topview_red)));
                else
                    animMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon_red)));

            }else if (network.equalsIgnoreCase("1") && vehIgnition.equalsIgnoreCase("1") && speed.equalsIgnoreCase("0")){
                if (vehicleTypeId.equalsIgnoreCase("8"))
                    animMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.bike_topview_orange)));
                else
                    animMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon_orage)));
            }
            else if (network.equalsIgnoreCase("0")) {
                if (vehicleTypeId.equalsIgnoreCase("8"))
                    animMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.bike_topview_black)));
                else
                    animMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon)));
                mapMsgLayout.setVisibility(View.VISIBLE);
                mapMessage.setVisibility(View.VISIBLE);
                mapSymolTxt.setVisibility(View.VISIBLE);
                mapSymolTxt.setText(R.string.location);
                mapSymolTxt.setTextColor(Color.parseColor("#D2CC24"));
                mapMessage.setText("We are not receiving live data for the vehicle.\n" +
                        "The map shows its last known location.\n" +
                        "Tracking will resume as soon as we get data.");

            } else {
                if (vehicleTypeId.equalsIgnoreCase("8"))
                    animMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.bike_topview_green)));
                else
                    animMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_car_icon_green)));
            }
            animMarkerOption.position(lastPostion);
            animMarker = mMap.addMarker(animMarkerOption);
            animMarker.setTag(info);
            animMarker.setFlat(true);
            //animMarker.setRotation(180);
            animMarker.hideInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPostion, mMap.getCameraPosition().zoom));
    }

    public void GetAllFenceOnMap(String usertype_id, final String user_id, final String is_first_req, final String vehicle_id) {
        RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
        Call<GeofenceResponse> call = objRetrofitInterface.fnGetRealTimeDataDevice(auth, apiKey, usertype_id, user_id, is_first_req, vehicle_id, player_id, devcie_id);
        call.enqueue(new Callback<GeofenceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<GeofenceResponse> call, Response<GeofenceResponse> response) {
                returnedData = response.body();
                if (response.isSuccessful() && data!=null){
                    if (returnedData.getStatus()!=null && returnedData.getStatus().equalsIgnoreCase("success")){
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        if (returnedData != null) {
                            try {
                                realtimeData = returnedData.getRealtimeData();
                                if (returnedData.getGeo_fence_details() != null) {
                                    geofence = returnedData.getGeo_fence_details();
                                    if (geofence.size() > 0) {
                                        if (!draw_geofence) {
                                            fn_getAllGeoFence(geofence);
                                        }
                                    }
                                }
                            } catch (NullPointerException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<GeofenceResponse> call, Throwable t) {
                // Utils.stopProgressDialog();
                // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

            }
        });
    }

    public void fn_getAllGeoFence(ArrayList<GeoFenceModel> geofence) {
        try {
            if (geofence.size() > 0) {
                // etSearch.setVisibility(View.VISIBLE);
                draw_geofence = true;
                for (int i = 0; i < geofence.size(); i++) {

                    GetCircleString = geofence.get(i).getGeoFenceLatLangs();
                    String DrawType = geofence.get(i).getOverlayType();
                    if (DrawType.equalsIgnoreCase("circle")) {
                        JSONArray jsonarray = new JSONArray(GetCircleString);
                        JSONObject jj = jsonarray.getJSONObject(0);
                        JSONObject kk = jj.getJSONObject("center");

                        String circlelat = kk.getString("lat");
                        String circlelong = kk.getString("lng");
                        String CircleRadius = jj.getString("radius");
                        LatLng drawlatlong = new LatLng(Double.valueOf(circlelat), Double.valueOf(circlelong));
                        CircleOptions circleoptions = new CircleOptions().strokeWidth(2).strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"));
                        InfoWindowData infonew = new InfoWindowData();
                        infonew.setLocation(geofence.get(i).getGeoFenceName());
                        customInfoWindowMap customInfoWindow = new customInfoWindowMap(ctx);
                        mMap.setInfoWindowAdapter(customInfoWindow);
                        mMap.addMarker(new MarkerOptions().position(drawlatlong).title(geofence.get(i).getGeoFenceName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon))).setTag(infonew);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drawlatlong, 18.0f));
                        Circle circle = mMap.addCircle(circleoptions.center(drawlatlong).radius(Double.valueOf(CircleRadius)));
                    } else if (DrawType.equalsIgnoreCase("polygon")) {

                        List<LatLng> coordinates = new ArrayList<>();
                        LatLng latlng;
                        String lat = "", lng = "";

                        try {
                            JSONArray jsonArr = new JSONArray(GetCircleString);

                            for (int polypoint = 0; polypoint < jsonArr.length(); polypoint++) {
                                JSONObject jsonObj = jsonArr.getJSONObject(polypoint);

                                System.out.println(jsonObj);
                                lat = jsonObj.getString("lat");
                                if (jsonObj.has("lng")) {
                                    lng = jsonObj.getString("lng");

                                } else {

                                    lng = jsonObj.getString("lang");

                                }
                                coordinates.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        InfoWindowData infonew = new InfoWindowData();
                        infonew.setLocation(geofence.get(i).getGeoFenceName());
                        customInfoWindowMap customInfoWindow = new customInfoWindowMap(ctx);
                        mMap.setInfoWindowAdapter(customInfoWindow);
                        mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon))).setTag(infonew);

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));

                        if (coordinates.size() > 0) {
                            PolygonOptions polygonOptions = new PolygonOptions();
                            polygonOptions.fillColor(Color.argb(60, 0, 0, 255));
                            polygonOptions.strokeColor(Color.argb(60, 0, 0, 255));
                            polygonOptions.strokeWidth(3);
                            polygonOptions.addAll(coordinates);
                            Polygon polygon = mMap.addPolygon(polygonOptions);
                        }
                    }
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }


    private void hereRouteMatchRequest(List<LatLng> latLngList){
        String latLongs = "LATITUDE,LONGITUDE"+"\n";
        for (int i = 0; i <latLngList.size() ; i++) {
            LatLng latLng = latLngList.get(i);
            latLongs+=latLng.latitude+","+latLng.longitude+"\n";
        }
        //Log.d(TAG, "hereRouteMatchRequest: "+latLongs);
        String base64LatLngs = Base64.encodeToString(latLongs.getBytes(), android.util.Base64.DEFAULT);
        String url = "https://rme.api.here.com/2/matchroute.json?app_id=8jmFsrdU1zCTiuB1oYr5&app_code=FQmm3YCKNLpGTTHcqXhHsQ&routemode=car&file="+base64LatLngs;
        //Call<RouteResponse> call = ApiClient.getClient().create(RetrofitInterface.class).getLatLangFromHere("application/binary",/*"8jmFsrdU1zCTiuB1oYr5","FQmm3YCKNLpGTTHcqXhHsQ","car",*/latLongs);
        Call<RouteResponse> call = ApiClient.getClient().create(RetrofitInterface.class).getLatLangFromHere(/*"application/binary",*/url);
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                Log.d(TAG, "onResponse:request  "+bodyToString(call.request().body()));
                if (response.isSuccessful() && response.body()!=null) {
                    List<RouteLink> routes = response.body().getRouteLinks();
                    if (routes != null && routes.size() > 0) {
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        ArrayList<LatLng> latLngCopyList = new ArrayList<>();
                        for (int i = 0; i < routes.size(); i++) {
                            RouteLink link = routes.get(i);
                            if (link.getShape() != null) {
                                String[] shape = link.getShape().split(" ");
                                for (int j = 0; j < shape.length; j = j + 2) {
                                    latLngs.add(new LatLng(Double.parseDouble(shape[j]), Double.parseDouble(shape[j + 1])));
                                }
                            }
                        }
                        latLngCopyList.addAll(latLngs);
                        Log.d(TAG, "onResponse: " + latLngs);
                        //if (lastLatLngArryLength>latLngs.size())
                        if (oldLatLngList.size() > 5) {
                            for (int i = 0; i < 5; i++) {
                                LatLng latLng = oldLatLngList.get(oldLatLngList.size() - 1);
                                polylines.remove(latLng);
                                oldLatLngList.remove(oldLatLngList.size() - 1);
                            }

                        }
                        latLngs.removeAll(oldLatLngList);
                        if (latLngs.size() > 1) {
                            animateCarOnMap(latLngs);
                            //rotationCar(latLngs);
                            oldLatLngList = latLngCopyList;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: 22");
            }
        });
    }



    private void animateCarOnMap(final List<LatLng> latLngs) {
        count=0;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (latLngs.size()>5) {
            for (int j = 0; j < latLngs.size() - 4; j++) {
                LatLng latLng = latLngs.get(j);
                builder.include(latLng);
            }
        }
        else{
            for (LatLng latLng : latLngs) {
                builder.include(latLng);
            }
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        mMap.animateCamera(mCameraUpdate);

        polylineOption = new PolylineOptions();
        polylineOption.color(Color.GREEN);
        polylineOption.width(5);
        polylineOption.startCap(new SquareCap());
        polylineOption.endCap(new SquareCap());
        polylineOption.jointType(ROUND);
        polylineOption.addAll(latLngs);

        animMarker.setPosition(setPanLatLng);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000/*latLngs.size()*18*/);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (count<latLngs.size()-1) {
                    LatLng newPos = latLngs.get(count+1);
                    setPanLatLng = newPos;
                    animMarker.setPosition(newPos);
                    animMarker.setAnchor(0.5f, 0.5f);
                    animMarker.setRotation(getBearing(latLngs.get(count), newPos));
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                            (new CameraPosition.Builder().target(newPos).zoom(mMap.getCameraPosition().zoom).build()));
                }
                ++count;
            }
        });

        Log.d(TAG, "animateCarOnMap: "+latLngs.size()+"   "+finalLatLngList.size() +"   "+ valueAnimator.getDuration());
        if (valueAnimator.isStarted()){
            valueAnimator.setStartDelay(valueAnimator.getDuration()/4);
            valueAnimator.start();
        }
        else {
            valueAnimator.start();
        }
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}