package com.trimax.vts.view.maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.helper.MapAreaManager;
import com.trimax.vts.helper.MapAreaMeasure;
import com.trimax.vts.helper.MapAreaWrapper;
import com.trimax.vts.interfaces.CircleManagerListener;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.maps.adapters.MyInfoWindowAdapter;
import com.trimax.vts.view.model.GeofenceResponse;
import com.trimax.vts.view.model.replay.RealtimeData;
import com.trimax.vts.view.vehicle.VTSSetVehicalActivity;
import com.trimax.vts.view.model.RealtimeDatum;
import com.trimax.vts.model.GeoFence;
import com.trimax.vts.model.GeoFenceStatus;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class GeoFencingNew extends AppCompatActivity implements OnMapReadyCallback {

    CommonClass cc;
    SharedPreferences sharedpreferencenew;
    String ClickFenceName="";
    Context context;
    CustomMapFragment supportMapFragment;
    GoogleMap mMap;
    String DrawCircleRadius="";
    private List<MapAreaWrapper> areas = new ArrayList<MapAreaWrapper>(1);
    private CircleManagerListener circleListener;

    LatLng center;
    float zoom=0;
    LinearLayout headerLayout;
    String PolygaonName;
    GeoFencingNew a;
    private Polygon polygon;
    String user_type_id = "", id = "";
    public TextView Nogeofence;
    int DrawcirclePosition=-1;
    public double Radius= 0.0;
    public double CircleCenterLatitute=0.0;
    public double CircleCenterLongitute=0.0;
    public String GetCircleString="";
    LatLng setPanLatLng;
    public String Vehicle_id="";
    private List<Marker> markerList = new ArrayList<>();
    private List<LatLng> points = new ArrayList<>();
    ListView GeofenceListview;
    Typeface font_awesome;
    LatLng latLng;
    String circlelat ="";
    String circlelong ="";
    String CircleRadius="";
    Double TrackerLat = null, TrackerLong = null;
    public int zoo_level=18;
    String click_VehicleValue = "",Vehicle_value="";
    String jsonStrigCircle ="";
    Button save;
    TextView setPanButton,vehicle_no,saveMapIconGeof, saveAllGeo, cancelAllGeo, car,headerIndicator, addGeoMap, cancelGeoMap, autoFenceGeoMap, ellipseIcon;
    Integer i;
    RealtimeData returnedData;
    RealtimeData latlnglistfortrack;
    ArrayList<GeoFence> allgeofenceByVehicle;
    private AdapterGetGeoFenceByVehicle getgeofencebyAdapter;
    boolean IsPolygoanClick=false,IsCircleClick=false,IsUpdatePolyClick=false,IsUpdatecircle=false;
    ProgressDialog dialog;
    String GeoFenceID="";
    String polyRequest="";
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofencingnew);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_red_color)));
        infoPref = new TravelpulseInfoPref(this);
        context = this;
        cc = new CommonClass(context);
        a = new GeoFencingNew();
        font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        sharedpreferencenew = context.getSharedPreferences(Constants.app_preference_login, Context.MODE_PRIVATE);
        supportMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap));
        supportMapFragment.getMapAsync(this);
        headerLayout =  findViewById(R.id.HeaderLayout);
        GeofenceListview = findViewById(R.id.vtslistviewGeof);
        Nogeofence = findViewById(R.id.Nogeofence);
        headerIndicator =  findViewById(R.id.txtHeaderIndicator);
        save =  findViewById(R.id.save);
        saveAllGeo =  findViewById(R.id.saveAllGeo);
        saveMapIconGeof=  findViewById(R.id.saveMapIconGeof);
        vehicle_no=  findViewById(R.id.vehicle_no);
        setPanButton =  findViewById(R.id.setPanId);
        car =  findViewById(R.id.vtsCarIcon);
        allgeofenceByVehicle = new ArrayList<>();
        cancelAllGeo =  findViewById(R.id.cancelAllGeo);
        addGeoMap =  findViewById(R.id.addMapIconGeof);
        cancelGeoMap =  findViewById(R.id.cancelMapIconGeof);
        autoFenceGeoMap =  findViewById(R.id.autoFenceMapIconGeof);
        saveAllGeo.setTypeface(font_awesome);
        addGeoMap.setTypeface(font_awesome);
        cancelGeoMap.setTypeface(font_awesome);
        cancelAllGeo.setTypeface(font_awesome);
        car.setTypeface(font_awesome);
        setPanButton.setTypeface(font_awesome);
        autoFenceGeoMap.setTypeface(font_awesome);
        saveMapIconGeof.setTypeface(font_awesome);
        click_VehicleValue = infoPref.getString("vid", PrefEnum.Login);
        user_type_id = infoPref.getString("user_type_id", PrefEnum.Login);
        id = infoPref.getString("id", PrefEnum.Login);

        // Real_Time_Tracking_function_for_on_Replay_FOR_start_point(user_type_id, id, "y", click_VehicleValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_geofence, menu);
        menu.findItem(R.id.menu_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(GeoFencingNew.this,AutoCompleteAddressActivity.class));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (infoPref.isKeyContains(infoPref.GEOFENCE_PLACE, PrefEnum.Login)){
            LatLng latLng = new LatLng(Double.parseDouble(infoPref.getString(infoPref.GEOFENCE_LAT,PrefEnum.Login)),Double.parseDouble(infoPref.getString(infoPref.GEOFENCE_LNG,PrefEnum.Login)));
            String place = infoPref.getString(infoPref.GEOFENCE_PLACE,PrefEnum.Login)+"\n"+infoPref.getString(infoPref.GEOFENCE_ADDRESS,PrefEnum.Login);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(place);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            if (mMap!=null) {
                Marker markerreplayMapClick = mMap.addMarker(markerOptions);
                //markerreplayMapClick.setAlpha(0.0f);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(19.0f));
            }
            infoPref.remove(infoPref.GEOFENCE_PLACE,PrefEnum.Login);
            infoPref.remove(infoPref.GEOFENCE_ADDRESS,PrefEnum.Login);
            infoPref.remove(infoPref.GEOFENCE_LAT,PrefEnum.Login);
            infoPref.remove(infoPref.GEOFENCE_LNG,PrefEnum.Login);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setPadding(0, 0, 0, 80);
        center = mMap.getCameraPosition().target;
        zoom = mMap.getCameraPosition().zoom;
        click_VehicleValue = infoPref.getString("vid", PrefEnum.Login);
        user_type_id = infoPref.getString("user_type_id", PrefEnum.Login);
        id = infoPref.getString("id", PrefEnum.Login);
        Real_Time_Tracking_function_for_on_Replay_FOR_start_point(user_type_id, id, "y", click_VehicleValue,infoPref.getString("GT_PLAYER_ID",PrefEnum.OneSignal),infoPref.getString("record_id",PrefEnum.OneSignal));

        setPanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  setPanButton.setVisibility(View.GONE);
                if (setPanLatLng != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(setPanLatLng));
                } else {
                    Toast.makeText(context, R.string.last_loc_not_found, Toast.LENGTH_SHORT).show();
                }
                //setPan = true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(IsPolygoanClick==true){
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon)));

                    marker.setTag(latLng);
                    markerList.add(marker);
                    points.add(latLng);
                    IsCircleClick=false;
                    Toast.makeText(context, "Start Drawing Fence", Toast.LENGTH_LONG).show();
                    drawPolygon(points);
                }

                else if(IsCircleClick==true){
                    IsPolygoanClick=false;
                    Toast.makeText(context, "Start Drawing Fence", Toast.LENGTH_LONG).show();
                    setupMap();
                }

            }
        });
        addGeoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Start Drawing Polygon Fence Click On Map", Toast.LENGTH_LONG).show();

                IsPolygoanClick=true;
                IsCircleClick=false;
                saveMapIconGeof.setVisibility(View.VISIBLE);
                cancelGeoMap.setVisibility(View.VISIBLE);
                addGeoMap.setVisibility(View.GONE);
                autoFenceGeoMap.setVisibility(View.GONE);
            }
        });

        saveMapIconGeof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMapIconGeof.setVisibility(View.GONE);
                cancelGeoMap.setVisibility(View.GONE);
                addGeoMap.setVisibility(View.VISIBLE);
                autoFenceGeoMap.setVisibility(View.VISIBLE);

                if (IsUpdatePolyClick) {
                    try {

                        JSONArray jsonArray = new JSONArray();

                        for (int i = 0; i < points.size(); i++) {
                            JSONObject js = new JSONObject();
                            js.put("lat", points.get(i).latitude);
                            js.put("lng", points.get(i).longitude);
                            jsonArray.put(js);
                        }

                        polyRequest = jsonArray.toString();
                        Log.d("polystring", polyRequest);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    fn_UpdateGeoFenceReal(user_type_id, id, GeoFenceID, String.valueOf(center.latitude), String.valueOf(center.longitude), String.valueOf(zoom), polyRequest, "polygon", ClickFenceName, "1");
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                } else if (IsUpdatecircle) {
                    fn_UpdateGeoFenceReal(user_type_id, id, GeoFenceID, String.valueOf(center.latitude), String.valueOf(center.longitude), String.valueOf(zoom), jsonStrigCircle, "circle", ClickFenceName, "1");
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                } else {
                    final AlertDialog dialogBuilder = new AlertDialog.Builder(GeoFencingNew.this).create();
                    LayoutInflater inflater = GeoFencingNew.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.customdailogforgeofence, null);

                    final EditText editText =  dialogView.findViewById(R.id.edt_comment);
                    TextView button1 =  dialogView.findViewById(R.id.buttonSubmit);
                    TextView button2 =  dialogView.findViewById(R.id.buttonCancel);
                    final TextView Textview =  dialogView.findViewById(R.id.textView);
                    Textview.setVisibility(View.GONE);
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            dialogBuilder.dismiss();
                        }
                    });
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PolygaonName = editText.getText().toString();
                            if (PolygaonName.equalsIgnoreCase("")) {

                                Toast.makeText(context, "Please Enter Fence Name", Toast.LENGTH_LONG).show();
                            }
                            else {
                                if (IsPolygoanClick) {
                                    if (points.size() <= 0) {
                                        Toast.makeText(context, "Please Mark Geo Fence On Map", Toast.LENGTH_LONG).show();

                                    } else {

                                        try {
                                            JSONArray jsonArray = new JSONArray();

                                            for (int i = 0; i < points.size(); i++) {
                                                JSONObject js = new JSONObject();
                                                js.put("lat", points.get(i).latitude);
                                                js.put("lng", points.get(i).longitude);
                                                jsonArray.put(js);
                                            }
                                            polyRequest = jsonArray.toString();
                                            Log.d("polystring", polyRequest);
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                        click_VehicleValue = infoPref.getString("vid", PrefEnum.Login);
                                        user_type_id = infoPref.getString("user_type_id", PrefEnum.Login);
                                        id = infoPref.getString("id", PrefEnum.Login);
                                        fn_SaveGeoFence(user_type_id, id, click_VehicleValue, String.valueOf(center.latitude), String.valueOf(center.longitude), String.valueOf(zoom), polyRequest, "polygon", PolygaonName, "1");
                                        // fn_getAllGeoFence("189");
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    }
                                }
                                else if (IsCircleClick) {
                                    if (jsonStrigCircle.equalsIgnoreCase("")) {
                                        Toast.makeText(context, "Please Mark Geo Fence On Map", Toast.LENGTH_LONG).show();

                                    } else {
                                        click_VehicleValue = infoPref.getString("vid", PrefEnum.Login);
                                        user_type_id = infoPref.getString("user_type_id", PrefEnum.Login);
                                        id = infoPref.getString("id", PrefEnum.Login);
                                        fn_SaveGeoFence(user_type_id, id, click_VehicleValue, String.valueOf(center.latitude), String.valueOf(center.longitude), String.valueOf(zoom), jsonStrigCircle, "circle", PolygaonName, "1");
                                        // fn_getAllGeoFence("189");
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    }
                                }
                                dialogBuilder.dismiss();
                            }
                        }

                    });

                    dialogBuilder.setView(dialogView);
                    dialogBuilder.show();
                }
            }
        });

        cancelGeoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                IsCircleClick=false;
                IsPolygoanClick=false;
                addGeoMap.setVisibility(View.VISIBLE);
                autoFenceGeoMap.setVisibility(View.VISIBLE);
                cancelGeoMap.setVisibility(View.GONE);
                saveMapIconGeof.setVisibility(View.GONE);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        autoFenceGeoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsCircleClick=true;
                IsPolygoanClick=false;
                saveMapIconGeof.setVisibility(View.VISIBLE);
                cancelGeoMap.setVisibility(View.VISIBLE);
                addGeoMap.setVisibility(View.GONE);
                autoFenceGeoMap.setVisibility(View.GONE);
                showAlertDialog(context,"GeoFence"," Please Long Click on Map To Draw Circle GeoFence .\n" +
                        " \n" +
                        " You can move GeoFence by Clicking on Center Icon Of Circle.\n" +
                        " \n" +
                        " You Can Resize GeoFence By Clicking on Side Icon Of Circle.","ok","");
                //Toast.makeText(context, "Start Drawing Circle Fence On Long Click Of Map", Toast.LENGTH_LONG).show();

                setupMap();
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                if (IsUpdatecircle) {
                    MarkerMoveResultWithCircle result = onMarkerMoved(marker);
                    switch (result.markerMoveResult) {
                        case minRadius: {
                            circleListener.onMinRadius(result.draggableCircle);
                            break;
                        }
                        case maxRadius: {
                            circleListener.onMaxRadius(result.draggableCircle);
                            break;
                        }
                        case radiusChange: {
                            circleListener.onResizeCircleStart(result.draggableCircle);
                            break;
                        }
                        case moved: {
                            circleListener.onMoveCircleStart(result.draggableCircle);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                if(IsUpdatecircle){
                    MarkerMoveResultWithCircle result = onMarkerMoved(marker);
                    switch (result.markerMoveResult) {
                        case minRadius: {
                            circleListener.onMinRadius(result.draggableCircle);
                            break;
                        }
                        case maxRadius: {
                            circleListener.onMaxRadius(result.draggableCircle);
                            break;
                        }
                        default:
                            break;
                    }
                }else {

                    updateMarkerLocation(marker, false);
                }
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if(IsUpdatecircle) {
                    MarkerMoveResultWithCircle result = onMarkerMoved(marker);
                    switch (result.markerMoveResult) {
                        case minRadius: {
                            circleListener.onMinRadius(result.draggableCircle);
                            break;
                        }
                        case maxRadius: {
                            circleListener.onMaxRadius(result.draggableCircle);
                            break;
                        }
                        case radiusChange: {
                            circleListener.onResizeCircleEnd(result.draggableCircle);
                            break;
                        }
                        case moved: {
                            circleListener.onMoveCircleEnd(result.draggableCircle);
                            break;
                        }
                        default:
                            break;
                    }
                }else {

                    updateMarkerLocation(marker, true);
                }
            }
        });
    }

    public void maxminMapClicked(View view) {
        RelativeLayout vtsF =  findViewById(R.id.relvtsgoogleMapGeof);
        ImageView maxminimg =  view.findViewById(R.id.maxminMapIconGeof);
        if (maxminimg.getTag().equals("min")) {
            if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                headerLayout.setVisibility(view.GONE);
                vtsF.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
            }
            maxminimg.setImageResource(R.drawable.minimize);
            maxminimg.setTag("max");
        } else {
            if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                headerLayout.setVisibility(view.GONE);
                vtsF.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.6f));
            }
            maxminimg.setImageResource(R.drawable.maximize);
            maxminimg.setTag("min");
        }
    }

    private void drawPolygon(List<LatLng> latLngList) {
        IsCircleClick=false;
        if (polygon != null) {
            polygon.remove();
        }
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(Color.argb(60, 0, 0, 255));
        polygonOptions.strokeColor(Color.argb(60, 0, 0, 255));
        polygonOptions.strokeWidth(3);
        polygonOptions.addAll(latLngList);
        polygon = mMap.addPolygon(polygonOptions);

    }

    private void  updateMarkerLocation(Marker marker, boolean calculate) {
        latLng = (LatLng) marker.getTag();
        int position = points.indexOf(latLng);
        points.set(position, marker.getPosition());
        marker.setTag(marker.getPosition());
        if (IsPolygoanClick == true) {
            drawPolygon(points);
        } else if (IsCircleClick == true) {
            setupMap();
        } else if (IsUpdatePolyClick == true) {
            drawPolygon(points);
        }
    }


    private void setupMap() {
        mMap.setOnMapClickListener(null);
        new MapAreaManager(mMap, 2, Color.BLUE, Color.HSVToColor(70, new float[]{1, 1, 200}), //styling

                R.drawable.movecir, R.drawable.resizecir, //custom drawables for move and resize icons

                0.5f, 0.5f, 0.5f, 0.5f, //sets anchor point of move / resize drawable in the middle

                new MapAreaMeasure(90, MapAreaMeasure.Unit.pixels), //circles will start with 100 pixels (independent of zoom level)

                new CircleManagerListener() { //listener for all circle events

                    @Override
                    public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {
                        CircleCenterLatitute = draggableCircle.getCenter().latitude;
                        CircleCenterLongitute = draggableCircle.getCenter().longitude;
                        Radius = draggableCircle.getRadius();
                        fnGetCircleRequestJson(CircleCenterLatitute, CircleCenterLongitute, Radius);
                        //Toast.makeText(GeoFencingNew.this, "do something on drag end circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCreateCircle(MapAreaWrapper draggableCircle) {
                        CircleCenterLatitute = draggableCircle.getCenter().latitude;
                        CircleCenterLongitute = draggableCircle.getCenter().longitude;
                        Radius = draggableCircle.getRadius();
                        fnGetCircleRequestJson(CircleCenterLatitute, CircleCenterLongitute, Radius);

                        //Toast.makeText(GeoFencingNew.this, "do something on crate circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMoveCircleEnd(MapAreaWrapper draggableCircle) {
                        CircleCenterLatitute = draggableCircle.getCenter().latitude;
                        CircleCenterLongitute = draggableCircle.getCenter().longitude;
                        Radius = draggableCircle.getRadius();
                        fnGetCircleRequestJson(CircleCenterLatitute, CircleCenterLongitute, Radius);

                        //Toast.makeText(GeoFencingNew.this, "do something on moved circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
                        CircleCenterLatitute = draggableCircle.getCenter().latitude;
                        CircleCenterLongitute = draggableCircle.getCenter().longitude;
                        Radius = draggableCircle.getRadius();
                        fnGetCircleRequestJson(CircleCenterLatitute, CircleCenterLongitute, Radius);
                        //Toast.makeText(GeoFencingNew.this, "do something on move circle start: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
                        CircleCenterLatitute = draggableCircle.getCenter().latitude;
                        CircleCenterLongitute = draggableCircle.getCenter().longitude;
                        Radius = draggableCircle.getRadius();
                        fnGetCircleRequestJson(CircleCenterLatitute, CircleCenterLongitute, Radius);

                        //Toast.makeText(GeoFencingNew.this, "do something on resize circle start: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMinRadius(MapAreaWrapper draggableCircle) {
                        CircleCenterLatitute = draggableCircle.getCenter().latitude;
                        CircleCenterLongitute = draggableCircle.getCenter().longitude;
                        Radius = draggableCircle.getRadius();
                        fnGetCircleRequestJson(CircleCenterLatitute, CircleCenterLongitute, Radius);
                        //Toast.makeText(GeoFencingNew.this, "do something on min radius: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMaxRadius(MapAreaWrapper draggableCircle) {
                        CircleCenterLatitute = draggableCircle.getCenter().latitude;
                        CircleCenterLongitute = draggableCircle.getCenter().longitude;
                        Radius = draggableCircle.getRadius();
                        fnGetCircleRequestJson(CircleCenterLatitute, CircleCenterLongitute, Radius);
                        //Toast.makeText(GeoFencingNew.this, "do something on max radius: " + draggableCircle, Toast.LENGTH_LONG).show();
                    }
                });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TrackerLat, TrackerLong), zoo_level));
    }

    private void UpdateCircleMap(LatLng circlelocation,double initRadiusMetersFinal) {
        IsUpdatecircle = true;
        IsCircleClick=false;
        IsPolygoanClick=false;
        IsUpdatePolyClick=false;
        MapAreaWrapper circle = new MapAreaWrapper(mMap, circlelocation, initRadiusMetersFinal, 2, Color.BLUE, Color.HSVToColor(70, new float[]{1, 1, 200}), 1, 1000,
                R.drawable.movecir, R.drawable.resizecir, 0.5f, 0.5f, 0.5f, 0.5f);
        if (areas.isEmpty()) {
            areas.add(circle);
        }

        circleListener = new CircleManagerListener() {
            @Override
            public void onCreateCircle(MapAreaWrapper draggableCircle) {
                CircleCenterLatitute=draggableCircle.getCenter().latitude;
                CircleCenterLongitute=draggableCircle.getCenter().longitude;
                Radius=draggableCircle.getRadius();
                fnGetCircleRequestJson(CircleCenterLatitute,CircleCenterLongitute,Radius);
            }

            @Override
            public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {
                CircleCenterLatitute=draggableCircle.getCenter().latitude;
                CircleCenterLongitute=draggableCircle.getCenter().longitude;
                Radius=draggableCircle.getRadius();
                fnGetCircleRequestJson(CircleCenterLatitute,CircleCenterLongitute,Radius);
            }

            @Override
            public void onMoveCircleEnd(MapAreaWrapper draggableCircle) {
                CircleCenterLatitute=draggableCircle.getCenter().latitude;
                CircleCenterLongitute=draggableCircle.getCenter().longitude;
                Radius=draggableCircle.getRadius();
                fnGetCircleRequestJson(CircleCenterLatitute,CircleCenterLongitute,Radius);
            }

            @Override
            public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
                CircleCenterLatitute=draggableCircle.getCenter().latitude;
                CircleCenterLongitute=draggableCircle.getCenter().longitude;
                Radius=draggableCircle.getRadius();
                fnGetCircleRequestJson(CircleCenterLatitute,CircleCenterLongitute,Radius);
            }

            @Override
            public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
                CircleCenterLatitute=draggableCircle.getCenter().latitude;
                CircleCenterLongitute=draggableCircle.getCenter().longitude;
                Radius=draggableCircle.getRadius();
                fnGetCircleRequestJson(CircleCenterLatitute,CircleCenterLongitute,Radius);
            }

            @Override
            public void onMinRadius(MapAreaWrapper draggableCircle) {
                CircleCenterLatitute=draggableCircle.getCenter().latitude;
                CircleCenterLongitute=draggableCircle.getCenter().longitude;
                Radius=draggableCircle.getRadius();
                fnGetCircleRequestJson(CircleCenterLatitute,CircleCenterLongitute,Radius);
            }

            @Override
            public void onMaxRadius(MapAreaWrapper draggableCircle) {
                CircleCenterLatitute=draggableCircle.getCenter().latitude;
                CircleCenterLongitute=draggableCircle.getCenter().longitude;
                Radius=draggableCircle.getRadius();
                fnGetCircleRequestJson(CircleCenterLatitute,CircleCenterLongitute,Radius);
            }
        };
//        circleListener.onCreateCircle(circle);
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TrackerLat, TrackerLong), zoo_level));
    }

    public void Real_Time_Tracking_function_for_on_Replay_FOR_start_point(String usertype_id, final String user_id, final String is_first_req, final String vehicle_id ,final String Player_id,final String devcie_id) {
        try {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
            dialog.setMessage("Please Wait...");
            dialog.show();
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<GeofenceResponse> call = objRetrofitInterface.fnGetRealTimeDataDevice(auth, apiKey, usertype_id, user_id, is_first_req, vehicle_id,Player_id,devcie_id);

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
                                //googleMap.setPadding(0, 0, 0, 80);
                                if (mMap != null) {
                                    mMap.clear();
                                }
                                if (returnedData!=null){
                                    latlnglistfortrack=returnedData;
                                    TrackerLat = Double.parseDouble(String.valueOf(latlnglistfortrack.getLat()));
                                    TrackerLong = Double.parseDouble(String.valueOf(latlnglistfortrack.getLang()));
                                    String vehicle_type = latlnglistfortrack.getVehicleTypeId();
                                    if (TrackerLat != null && TrackerLong != null) {
                                        MarkerOptions optionsVehAlert = new MarkerOptions();
                                        LatLng LLVehAlert = new LatLng(TrackerLat, TrackerLong);
                                        //setPanLatLng = new LatLng(TrackerLat, TrackerLong);
                                        optionsVehAlert.position(LLVehAlert);
                                        optionsVehAlert.title("Location: "+latlnglistfortrack.getLocation());
                                        optionsVehAlert.snippet("Vehicle No: "+latlnglistfortrack.getVehicleNo()+"\n"+"Driver Name: "+latlnglistfortrack.getDriverName()
                                                +"\n"+"Updated On: "+latlnglistfortrack.getFormattedDateTime());
                                        if (vehicle_type.equalsIgnoreCase("8")) {
                                            optionsVehAlert.icon(bitmapDescriptorFromVector(context, R.drawable.ic_car_icon));

                                        } else {
                                            optionsVehAlert.icon(bitmapDescriptorFromVector(context, R.drawable.ic_car_icon));
                                        }
                                        mMap.addMarker(optionsVehAlert);
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TrackerLat, TrackerLong), zoo_level));
                                        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(context));

                                        SharedPreferences.Editor editor = sharedpreferencenew.edit();
                                        editor.putString("vidGeoFence",latlnglistfortrack.getVehicleNo());
                                        editor.commit();
                                        setPanLatLng = new LatLng(TrackerLat, TrackerLong);

                                        Vehicle_id = latlnglistfortrack.getId();
                                        infoPref.putString("vid", Vehicle_id,PrefEnum.Login);
                                        fn_getAllGeoFence(latlnglistfortrack.getId());
                                        //fn_getAllGeoFence("189");

                                    } else {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                        Toast.makeText(context, "No tracking data found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    /*try {
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String syncresponse = myObject.getString("data");
                                String strStatus = myObject.getString("status");

                                if (strStatus.trim().equals("success")) {
                                    try {
                                        if (strStatus != null) {
                                            // Toast.makeText(ctx,"onresponse "+strResponse,Toast.LENGTH_SHORT).show();
                                            mMap.getUiSettings().setZoomControlsEnabled(true);
                                            //googleMap.setPadding(0, 0, 0, 80);
                                            if (mMap != null) {
                                                mMap.clear();
                                            }
                                            Gson gson = new Gson();

                                            Type listType = new TypeToken<Data>() {}.getType();

                                            returnedData = gson.fromJson(syncresponse, listType);
                                            if (returnedData != null) {
                                                latlnglistfortrack = returnedData.getRealtimeData();
                                                TrackerLat = Double.parseDouble(String.valueOf(latlnglistfortrack.get(0).getLat()));
                                                TrackerLong = Double.parseDouble(String.valueOf(latlnglistfortrack.get(0).getLang()));
                                                String vehicle_type = latlnglistfortrack.get(0).getVehicle_type();
                                                if (TrackerLat != null && TrackerLong != null) {
                                                    MarkerOptions optionsVehAlert = new MarkerOptions();
                                                    LatLng LLVehAlert = new LatLng(TrackerLat, TrackerLong);
                                                    //setPanLatLng = new LatLng(TrackerLat, TrackerLong);
                                                    optionsVehAlert.position(LLVehAlert);
                                                    optionsVehAlert.title("Location: "+latlnglistfortrack.get(0).getLocation());
                                                    optionsVehAlert.snippet("Vehicle No: "+latlnglistfortrack.get(0).getVehicleNo()+"\n"+"Driver Name: "+latlnglistfortrack.get(0).getDriverName()
                                                            +"\n"+"Updated On: "+latlnglistfortrack.get(0).getFormattedDateTime());
                                                    if (vehicle_type.equalsIgnoreCase("8")) {
                                                        optionsVehAlert.icon(bitmapDescriptorFromVector(context, R.drawable.ic_car_icon));

                                                    } else {
                                                        optionsVehAlert.icon(bitmapDescriptorFromVector(context, R.drawable.ic_car_icon));
                                                    }
                                                    mMap.addMarker(optionsVehAlert);
                                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TrackerLat, TrackerLong), zoo_level));
                                                    mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(context));

                                                    SharedPreferences.Editor editor = sharedpreferencenew.edit();
                                                    editor.putString("vidGeoFence",latlnglistfortrack.get(0).getVehicleNo());
                                                    editor.commit();
                                                    setPanLatLng = new LatLng(TrackerLat, TrackerLong);

                                                    Vehicle_id = latlnglistfortrack.get(0).getId();
                                                    sharedpreferencenew.edit().putString("vid", Vehicle_id).commit();
                                                    fn_getAllGeoFence(latlnglistfortrack.get(0).getId());
                                                    //fn_getAllGeoFence("189");

                                                } else {
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    Toast.makeText(context, "No tracking data found", Toast.LENGTH_LONG).show();
                                                }


                                            }

                                        }

                                    } catch (Exception ex) {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                        // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again");
                                        ex.printStackTrace();
                                        cc.DisplayToast(context, "exception", "bottom");
                                    }
                                    break;
                                }
                        }
                    } catch (Exception e) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        e.printStackTrace();
                    }*/
                }

                @Override
                public void onFailure(Call<GeofenceResponse> call, Throwable t) {
                    // Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    public void fn_getAllGeoFence(String Vehicle_id) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        try {

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<GeoFenceStatus> call = objRetrofitInterface.getAllGeoFenceByVehicle(auth, apiKey, Vehicle_id);
            call.enqueue(new Callback<GeoFenceStatus>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<GeoFenceStatus> call, Response<GeoFenceStatus> response) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                try {
                                        if (response.body()!=null) {
                                            allgeofenceByVehicle = response.body().getGeofences();
                                            if (allgeofenceByVehicle.size() > 0) {
                                                //.setVisibility(View.VISIBLE);
                                                getgeofencebyAdapter = new AdapterGetGeoFenceByVehicle(context, allgeofenceByVehicle);
                                                GeofenceListview.setAdapter(getgeofencebyAdapter);
                                                for (int i = 0; i < allgeofenceByVehicle.size(); i++) {

                                                    GetCircleString = allgeofenceByVehicle.get(i).getGeoFenceLatLangs();
                                                    String DrawType = allgeofenceByVehicle.get(i).getOverlayType();
                                                    if (DrawType.equalsIgnoreCase("circle")) {
                                                        JSONArray jsonarray = new JSONArray(GetCircleString);
                                                        JSONObject jj = jsonarray.getJSONObject(0);
                                                        JSONObject kk = jj.getJSONObject("center");

                                                        String circlelat = kk.getString("lat");
                                                        String circlelong = kk.getString("lng");
                                                        String CircleRadius = jj.getString("radius");
                                                        LatLng drawlatlong = new LatLng(Double.valueOf(circlelat), Double.valueOf(circlelong));
                                                        CircleOptions circleoptions = new CircleOptions().strokeWidth(2).strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"));
                                                        mMap.addMarker(new MarkerOptions().position(drawlatlong).title(allgeofenceByVehicle.get(i).getGeoFenceName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon)));
                                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drawlatlong, zoo_level));
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
                                                        mMap.addMarker(new MarkerOptions().position(latlng).title(allgeofenceByVehicle.get(i).getGeoFenceName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon)));

                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));

                                                        if (coordinates.size() > 0) {
                                                            PolygonOptions polygonOptions = new PolygonOptions();
                                                            polygonOptions.fillColor(Color.argb(60, 0, 0, 255));
                                                            polygonOptions.strokeColor(Color.argb(60, 0, 0, 255));
                                                            polygonOptions.strokeWidth(3);
                                                            polygonOptions.addAll(coordinates);
                                                            polygon = mMap.addPolygon(polygonOptions);
                                                        }
                                                    }
                                                }
                                                dialog.dismiss();
                                            } else {
                                                Nogeofence.setVisibility(View.VISIBLE);
                                                if (dialog != null) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        }
                                }catch (NullPointerException | JSONException  ex) {
                                    ex.printStackTrace();
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                }
                                break;
                            case 500:
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                Toast.makeText(GeoFencingNew.this,"Internal Server Error",Toast.LENGTH_LONG).show();
                                break;

                        }
                    }

                @Override
                public void onFailure(Call<GeoFenceStatus> call, Throwable t) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }

                }
            });
        } catch (Exception ex) {
            if (dialog != null) {
                dialog.dismiss();
            }
            Log.e("", "Api fail");
        }
    }

    public void fn_SaveGeoFence(String user_type_id, String user_id, String Vehicle_id, String map_center_lat, String map_center_lang, String map_zoom, String geo_fence_lat_langs
            , String drawtype, String geofencename, String status) {
        try {
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.SaveAllGeoFenceByVehicle(auth, apiKey,
                    user_type_id,user_id, Vehicle_id,map_center_lat,map_center_lang,map_zoom,
                    geo_fence_lat_langs,drawtype,geofencename,status);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String syncresponse = myObject.getString("status");
                                try {
                                    if (syncresponse.equalsIgnoreCase("success")) {

                                        String syncresponsee = myObject.getString("msg");

                                        Toast.makeText(GeoFencingNew.this,syncresponsee,Toast.LENGTH_LONG).show();
                                    }

                                }catch (NullPointerException ex){
                                    ex.printStackTrace();
                                }
                                break;
                            case 500:
                                Toast.makeText(GeoFencingNew.this,"Internal Server Error",Toast.LENGTH_LONG).show();
                                break;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

                }
            });
        } catch (Exception ex) {
            Log.e("", "Api fail");
        }
    }


    public void fn_UpdateGeoFenceReal(String user_type_id, String user_id, String geofenceid, String map_center_lat, String map_center_lang,
                                      String map_zoom, String geo_fence_lat_langs, String drawtype, String geofencename, String status) {
        try {
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.UpdateGeoFenceById(auth, apiKey,
                    user_type_id,user_id, geofenceid,map_center_lat,map_center_lang,map_zoom,
                    geo_fence_lat_langs,drawtype,geofencename,status);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String syncresponse = myObject.getString("status");
                                try {
                                    if (syncresponse.equalsIgnoreCase("success")) {

                                        String syncresponsee = myObject.getString("msg");

                                        Toast.makeText(GeoFencingNew.this,syncresponsee,Toast.LENGTH_LONG).show();
                                    }

                                }catch (NullPointerException ex){
                                    ex.printStackTrace();
                                }
                                break;
                            case 500:
                                Toast.makeText(GeoFencingNew.this,"Internal Server Error",Toast.LENGTH_LONG).show();
                                break;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

                }
            });
        } catch (Exception ex) {
            Log.e("", "Api fail");
        }
    }

    public void fn_UpdateGeoFence(String geo_id, String geo_name) {
        try {
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.UpdateGeoFenceNameByVehicle(auth, apiKey,geo_id,geo_name);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String syncresponse = myObject.getString("status");
                                try {
                                    if (syncresponse.equalsIgnoreCase("success")) {

                                        String syncresponsee = myObject.getString("msg");

                                        Toast.makeText(GeoFencingNew.this,syncresponsee,Toast.LENGTH_LONG).show();
                                        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                                            mMap.clear();
                                        }
                                    }
                                    else {
                                        Toast.makeText(GeoFencingNew.this,"Please Try again",Toast.LENGTH_LONG).show();
                                    }
                                }catch (NullPointerException ex){
                                    ex.printStackTrace();
                                }
                                break;
                            case 500:
                                Toast.makeText(GeoFencingNew.this,"Internal Server Error",Toast.LENGTH_LONG).show();
                                break;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

                }
            });
        } catch (Exception ex) {
            Log.e("", "Api fail");
        }
    }

    public void fn_DeleteGeoFence(String geo_id, String vehicleid) {
        try {
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.DeleteGeoFenceByVehicle(auth, apiKey,geo_id,vehicleid);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String syncresponse = myObject.getString("status");
                                try {
                                    if (syncresponse.equalsIgnoreCase("success")) {

                                        String syncresponsee = myObject.getString("msg");
                                        if (!IsUpdatecircle == true || !IsUpdatePolyClick == true) {
                                            Toast.makeText(GeoFencingNew.this, syncresponsee, Toast.LENGTH_LONG).show();
                                        }

                                    }

                                }catch (NullPointerException ex){
                                    ex.printStackTrace();
                                }
                                break;
                            case 500:
                                Toast.makeText(GeoFencingNew.this,"Internal Server Error",Toast.LENGTH_LONG).show();
                                break;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

                }
            });
        } catch (Exception ex) {
            Log.e("", "Api fail");
        }
    }

    /*public class Data {

        @SerializedName("realtime_data")
        @Expose
        private ArrayList<RealtimeDatum> realtimeData = null;

        public ArrayList<RealtimeDatum> getRealtimeData() {
            return realtimeData;
        }

        public void setRealtimeData(ArrayList<RealtimeDatum> realtimeData) {
            this.realtimeData = realtimeData;
        }

    }*/

    public String fnGetCircleRequestJson(double lat, double longi, double radi) {
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject j = new JSONObject();
            j.put("lat", lat);
            j.put("lng", longi);
            JSONObject jj = new JSONObject();
            jj.put("center", j);
            jj.put("radius", radi);
            jsonarray.put(0, jj);
            jsonStrigCircle = jsonarray.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStrigCircle;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void vtsCarMapClicked(View view) {
        Intent intent = new Intent(context, VTSSetVehicalActivity.class);
        intent.putExtra("Call", "CallFromGeoFence");
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                /* clear map and set UserAccessType */
                supportMapFragment.getMapAsync(this);
                if (mMap != null) {
                    mMap.clear();
                }
                if (cc.isConnected(context)) {
                    click_VehicleValue = infoPref.getString("vid", PrefEnum.Login);
                    Vehicle_value = data.getStringExtra("vid");
                    infoPref.putString("vid",data.getStringExtra("vid"),PrefEnum.Login);
                    Real_Time_Tracking_function_for_on_Replay_FOR_start_point(user_type_id, id, "y", click_VehicleValue, infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal), infoPref.getString("record_id", PrefEnum.OneSignal));
                } else {
                    cc.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
                }
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class AdapterGetGeoFenceByVehicle extends BaseAdapter {
        Context context;
        List<GeoFence> sampleData=null;
        ArrayList<GeoFence> arraylist;
        private int selectedIndex;

        public AdapterGetGeoFenceByVehicle(Context context, List<GeoFence> sampleData) {
            this.context = context;
            this.sampleData = sampleData;
            this.arraylist = new ArrayList<GeoFence>();
            this.arraylist.addAll(sampleData);
            selectedIndex = -1;
        }

        public class ViewHolder {
            TextView CarIcon,txtName,CheckBoxIcon;
            LinearLayout click_layout;
        }

        public int getCount() {

            return sampleData.size();
        }

        public Object getItem(int position) {
            return sampleData.get(position);
        }

        public long getItemId(int position) {
            return sampleData.indexOf(getItem(position));
        }

        public View getView(final int position, View rowView, ViewGroup parent)
        {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (rowView == null) {
                rowView = mInflater.inflate(R.layout.vehicle_list_item_geofence, null);
                holder = new ViewHolder();
                holder.click_layout=rowView.findViewById(R.id.click_layout);
                rowView.setTag(holder);
            }
            else {
                holder = (ViewHolder) rowView.getTag();
            }

            TextView txtReg =  rowView.findViewById(R.id.txtRegNo);
            txtReg.setVisibility(View.GONE);
            if(sampleData.get(position).getVehicleNo() != null){
                txtReg.setText(sampleData.get(position).getVehicleNo());
            }
            else{
                txtReg.setText("");
            }

            TextView txtMake =  rowView.findViewById(R.id.txtMake);
            TextView txtVehName  =  rowView.findViewById(R.id.txtVehName);
            ImageView txteditgeo=rowView.findViewById(R.id.txteditgeo);
            ImageView txtdeletegeo=rowView.findViewById(R.id.txtdeletegeo);
            ImageView txtupdategeo =rowView.findViewById(R.id.txtupdategeo);

            txtVehName.setVisibility(View.GONE);
            if(sampleData.get(position).getGeoFenceName()!= null){
                txtMake.setText(sampleData.get(position).getGeoFenceName());
            }else{
                txtMake.setText("");
            }
            TextView txtmodel =  rowView.findViewById(R.id.txtModel);
            txtmodel.setVisibility(View.GONE);

            txtupdategeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IsUpdatecircle=true;
                    saveMapIconGeof.setVisibility(View.VISIBLE);
                    cancelGeoMap.setVisibility(View.VISIBLE);
                    addGeoMap.setVisibility(View.GONE);
                    autoFenceGeoMap.setVisibility(View.GONE);

                    GeoFenceID=sampleData.get(position).getId();
                    ClickFenceName=sampleData.get(position).getGeoFenceName();

                    String OverlayType =sampleData.get(position).getOverlayType();
                    GetCircleString=sampleData.get(position).getGeoFenceLatLangs();

                    if(OverlayType.equalsIgnoreCase("polygon")){
                        List<LatLng> coordinates = new ArrayList<>();
                        try {
                            IsUpdatePolyClick=true;
                            IsUpdatecircle=false;
                            IsCircleClick=false;
                            LatLng latlng;
                            String lat="",lng="";

                            try {

                                JSONArray jsonArr = new JSONArray(GetCircleString);

                                for (int i = 0; i < jsonArr.length(); i++)
                                {
                                    JSONObject jsonObj = jsonArr.getJSONObject(i);

                                    System.out.println(jsonObj);
                                    lat=jsonObj.getString("lat");
                                    if(jsonObj.has("lng")){
                                        lng=jsonObj.getString("lng");

                                    }else{

                                        lng=jsonObj.getString("lang");

                                    }
                                    coordinates.add(new LatLng(Double.parseDouble(lat),Double.parseDouble(lng)));
                                    latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(latlng)
                                            /*.title(bean.getTitle())
                                            .snippet(bean.getSnippet())*/
                                            .draggable(true)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon)));

                                    marker.setTag(latlng);
                                    markerList.add(marker);
                                    points.add(latlng);
                                }
                                latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                mMap.addMarker(new MarkerOptions().position(latlng).title(allgeofenceByVehicle.get(i).getGeoFenceName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,16));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,16));
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        IsCircleClick=false;
                        IsPolygoanClick=false;
                        //drawPolygon(points);

                    }
                    else if(OverlayType.equalsIgnoreCase("circle")){

                        IsCircleClick=false;
                        IsUpdatePolyClick=false;
                        IsPolygoanClick=false;
                        try {
                            JSONArray jsonarray = new JSONArray(GetCircleString);
                            JSONObject jj = jsonarray.getJSONObject(0);
                            JSONObject kk = jj.getJSONObject("center");

                            circlelat = kk.getString("lat");
                            circlelong = kk.getString("lng");
                            CircleRadius = jj.getString("radius");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        latLng = new LatLng(Double.parseDouble(circlelat),Double.parseDouble(circlelong));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoo_level));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoo_level));
                        UpdateCircleMap(latLng, Double.parseDouble(CircleRadius));
                    }
                }
            });


            txteditgeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog dialogBuilder = new AlertDialog.Builder(GeoFencingNew.this).create();
                    LayoutInflater inflater = GeoFencingNew.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.customdailogforgeofence, null);

                    final EditText editText =  dialogView.findViewById(R.id.edt_comment);
                    TextView button1 =  dialogView.findViewById(R.id.buttonSubmit);
                    TextView button2 =  dialogView.findViewById(R.id.buttonCancel);
                    final TextView Textview =  dialogView.findViewById(R.id.textView);
                    Textview.setVisibility(View.GONE);
                    ClickFenceName=sampleData.get(position).getGeoFenceName();

                    editText.setText(sampleData.get(position).getGeoFenceName());

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            dialogBuilder.dismiss();
                        }
                    });
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // DO SOMETHINGS

                            PolygaonName=editText.getText().toString();
                            if(PolygaonName.equalsIgnoreCase("")){

                                Toast.makeText(context,"Please Enter Geo Fence Name",Toast.LENGTH_LONG).show();
                            }else {
                                fn_UpdateGeoFence(sampleData.get(position).getId(), PolygaonName);

                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);


                            }
                            dialogBuilder.dismiss();
                        }
                    });

                    dialogBuilder.setView(dialogView);
                    dialogBuilder.show();
                }

            });

            txtdeletegeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog dialogBuilder = new AlertDialog.Builder(GeoFencingNew.this).create();
                    LayoutInflater inflater = GeoFencingNew.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.customdailogforgeofence, null);

                    final EditText editText =  dialogView.findViewById(R.id.edt_comment);
                    TextView button1 =  dialogView.findViewById(R.id.buttonSubmit);
                    TextView button2 =  dialogView.findViewById(R.id.buttonCancel);

                    final TextView Textview =  dialogView.findViewById(R.id.textView);
                    Textview.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            dialogBuilder.dismiss();
                        }
                    });
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // DO SOMETHINGS

                            PolygaonName=editText.getText().toString();
                            //fn_DeleteGeoFence(sampleData.get(position).getId(),sampleData.get(position).getVid());
                            click_VehicleValue = infoPref.getString("vid", PrefEnum.Login);
                            user_type_id = infoPref.getString("user_type_id", PrefEnum.Login);
                            id = infoPref.getString("id", PrefEnum.Login);
                            fn_DeleteGeoFence(sampleData.get(position).getId(),click_VehicleValue);
                            sampleData.remove(position);
                            notifyDataSetChanged();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            dialogBuilder.dismiss();
                        }
                    });

                    dialogBuilder.setView(dialogView);
                    dialogBuilder.show();


                }
            });

            holder.click_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DrawcirclePosition=position;
                    String OverlayType =allgeofenceByVehicle.get(DrawcirclePosition).getOverlayType();

                    GetCircleString = allgeofenceByVehicle.get(DrawcirclePosition).getGeoFenceLatLangs();
                    String GeoFenceNAme=allgeofenceByVehicle.get(DrawcirclePosition).getGeoFenceName();

                    if(OverlayType.equalsIgnoreCase("circle")) {
                        try {
                            // JSONObject jsnobject = new JSONObject(GetCircleString);
                            JSONArray jsonarray = new JSONArray(GetCircleString);
                            JSONObject jj =jsonarray.getJSONObject(0);
                            JSONObject kk = jj.getJSONObject("center");
                            String circlelat = kk.getString("lat");
                            String circlelong =kk.getString("lng");
                            if(DrawCircleRadius.equalsIgnoreCase(jj.getString("radius"))){

                            }else {
                                DrawCircleRadius = jj.getString("radius");
                                LatLng drawlatlong = new LatLng(Double.valueOf(circlelat), Double.valueOf(circlelong));
                                CircleOptions circleoptions = new CircleOptions().strokeWidth(2).strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"));
                                mMap.addMarker(new MarkerOptions().position(drawlatlong).title(GeoFenceNAme).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon)));
                                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drawlatlong,18));
                                Circle circle = mMap.addCircle(circleoptions.center(drawlatlong).radius(Double.valueOf(DrawCircleRadius)));
                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circleoptions.getCenter(),getZoomLevel(circle)));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circleoptions.getCenter(), zoo_level));
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else if(OverlayType.equalsIgnoreCase("polygon")){
                        List<LatLng> coordinates = new ArrayList<>();
                        List<LatLng> oldcoordinates = new ArrayList<>();
                        LatLng latlng;
                        String lat="",lng="";

                        try {

                            JSONArray jsonArr = new JSONArray(GetCircleString);


                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jsonObj = jsonArr.getJSONObject(i);

                                System.out.println(jsonObj);
                                lat = jsonObj.getString("lat");
                                if (jsonObj.has("lng")) {
                                    lng = jsonObj.getString("lng");

                                } else {

                                    lng = jsonObj.getString("lang");

                                }
                                coordinates.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                            }
                            latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                            mMap.addMarker(new MarkerOptions().position(latlng).title(allgeofenceByVehicle.get(DrawcirclePosition).getGeoFenceName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluering_icon)));

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(oldcoordinates.size()>0) {
                            if (coordinates.size() > 0) {
                                PolygonOptions polygonOptions = new PolygonOptions();
                                polygonOptions.fillColor(Color.argb(60, 0, 0, 255));
                                polygonOptions.strokeColor(Color.argb(60, 0, 0, 255));
                                polygonOptions.strokeWidth(3);
                                polygonOptions.addAll(coordinates);
                                polygon = mMap.addPolygon(polygonOptions);
                                oldcoordinates = coordinates;
                            }
                        }
                    }

                }
            });

            return rowView;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            sampleData.clear();
            if (charText.length() == 0) {
                sampleData.addAll(arraylist);

            } else {
                for ( GeoFence st : arraylist) {
                    if (st.getVehicleNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                        sampleData.add(st);
                    }else if (st.getGeoFenceName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        sampleData.add(st);
                    }
                    /*else if (st.getMake().toLowerCase(Locale.getDefault()).contains(charText)) {
                        sampleData.add(st);
                    }*/
                }
            }
            notifyDataSetChanged();
        }

    }

    private class MarkerMoveResultWithCircle {
        MapAreaWrapper.MarkerMoveResult markerMoveResult;
        MapAreaWrapper draggableCircle;

        public MarkerMoveResultWithCircle(MapAreaWrapper.MarkerMoveResult markerMoveResult, MapAreaWrapper draggableCircle) {
            this.markerMoveResult = markerMoveResult;
            this.draggableCircle = draggableCircle;
        }
    }


    private MarkerMoveResultWithCircle onMarkerMoved(Marker marker) {
        MapAreaWrapper.MarkerMoveResult result = MapAreaWrapper.MarkerMoveResult.none;
        MapAreaWrapper affectedDraggableCircle = null;

        for (MapAreaWrapper draggableCircle : areas) {
            result = draggableCircle.onMarkerMoved(marker);
            if (result != MapAreaWrapper.MarkerMoveResult.none) {
                affectedDraggableCircle = draggableCircle;
                break;
            }
        }
        return new MarkerMoveResultWithCircle(result, affectedDraggableCircle);
    }


    public void add(MapAreaWrapper draggableCircle) {

        areas.add(draggableCircle);
    }


    public void showAlertDialog(Context context, String title, String message, String posBtnMsg, String negBtnMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(posBtnMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton(negBtnMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /*public void addMarker(Place p){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(p.getLatLng());
        markerOptions.title(p.getName()+"");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.getMaxZoomLevel();
        Marker markerreplayMapClick= mMap.addMarker(markerOptions);
        markerreplayMapClick.setAlpha(0.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19.0f));
    }*/
}
