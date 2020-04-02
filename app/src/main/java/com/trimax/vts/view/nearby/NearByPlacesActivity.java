package com.trimax.vts.view.nearby;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.BuildConfig;
import com.trimax.vts.view.R;
import com.trimax.vts.view.vehicle.VTSSetVehicalActivity;
import com.trimax.vts.view.nearby.models.InfoWindow;
import com.trimax.vts.view.nearby.models.PlaceServiceModel;
import com.trimax.vts.view.nearby.models.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class NearByPlacesActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final String TAG = "NearByPlacesActivity";

    private SupportMapFragment mapFragment;
    private RecyclerView rv_places,rv_list_place;
    private ProgressBar pb_progress;
    private FloatingActionButton fab_my_location,fab_vehicle_location;

    private Location mLastKnownLocation;
    private GoogleMap mMap;
    private ClipboardManager clipboard;
    private Menu menu;
    private Marker currentLocMarkar;
    private boolean isLocationUpdate,isListView,isMyLocation=true;
    private boolean mLocationPermissionGranted;
    private BottomSheetBehavior bottomSheetBehavior;
    private HashMap<String, HashMap<String,List<Result>>> vehiclePlacesMap;
    private List<InfoWindow> placeResult;
    private NearByPlacesAdapter placesAdapter;
    private double vehicleLat,vehicleLng;
    private String vehicleNo="";
    private LocationManager locationManager;
    private PlaceServiceModel placeServiceModel;

    private AndroidXMapFragment hereMapFragment;
    private Map hereMap;
    public List<DiscoveryResult> searchResultList;
    public List<PlaceLink> searchPlaceList;

    private HashMap<String,List<PlaceLink>> herePlacesMap;
    private HashMap<String,List<PlaceLink>> hereVehicleMap;
    private HashMap<String, HashMap<String,List<PlaceLink>>> hereVehiclePlacesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        vehiclePlacesMap = new HashMap<>();
        placeResult = new ArrayList<>();

        herePlacesMap = new HashMap<>();
        hereVehicleMap = new HashMap<>();
        hereVehiclePlacesMap = new HashMap<>();

        placeResult = new ArrayList<>();

        hereMapFragment = (AndroidXMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);

        if(hereMapFragment != null) {
            hereMapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(
                        OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        // now the map is ready to be used
                        hereMap = hereMapFragment.getMap();
                        hereMap.setCenter(new GeoCoordinate(28.644800, 77.216721), Map.Animation.NONE);
                        hereMap.setZoomLevel(16.0);
                    } else {
                        System.out.println("ERROR: Cannot initialize AndroidXMapFragment");
                    }
                }
            });
            hereMapFragment.getView().setVisibility(View.GONE);
        }

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showLocationEnableDialog();
        }

        TextView tv_explore = findViewById(R.id.tv_explore);
        fab_my_location = findViewById(R.id.fab_my_location);
        fab_vehicle_location = findViewById(R.id.fab_vehicle_location);
        rv_list_place = findViewById(R.id.rv_list_place);
        pb_progress = findViewById(R.id.pb_progress);
        LinearLayout ll_bottom_sheet = findViewById(R.id.ll_bottom_sheet);
        rv_places = findViewById(R.id.rv_places);
        tv_explore.setOnClickListener(this);
        fab_my_location.setOnClickListener(this);
        fab_vehicle_location.setOnClickListener(this);

        rv_list_place.setHasFixedSize(true);
        rv_list_place.setItemAnimator(new DefaultItemAnimator());
        rv_list_place.setLayoutManager(new LinearLayoutManager(this));
        placesAdapter = new NearByPlacesAdapter(placeResult,this);
        rv_list_place.setAdapter(placesAdapter);

        bottomSheetBehavior = BottomSheetBehavior.from(ll_bottom_sheet);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setupBottomSheetData();
    }

    private void showLocationEnableDialog() {
        new AlertDialog.Builder(this)
                .setMessage("To continue, let your device turn on location using Google's location service. Please select high accuracy location.")
                .setTitle("Enable Location")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent,2000);
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.near_by_places,menu);
        this.menu=menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        else if (item.getItemId()==R.id.action_map){
            menu.findItem(R.id.action_map).setVisible(false);
            menu.findItem(R.id.action_list).setVisible(true);
            rv_list_place.setVisibility(View.GONE);
            mapFragment.getView().setVisibility(View.VISIBLE);
            fab_vehicle_location.show();
            fab_my_location.show();
            isListView=false;
        }
        else if (item.getItemId()==R.id.action_list){
            if (placeResult.size()>0) {
                menu.findItem(R.id.action_list).setVisible(false);
                menu.findItem(R.id.action_map).setVisible(true);
                rv_list_place.setVisibility(View.VISIBLE);
                mapFragment.getView().setVisibility(View.GONE);
                fab_vehicle_location.hide();
                fab_my_location.hide();
                placesAdapter.addInfoWindow(placeResult);
                isListView=true;
            }else{
                Toast.makeText(this, "Select explore option and retry.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBottomSheetData() {
        List<PlaceServiceModel> serviceModels = new ArrayList<>();
        serviceModels.add(new PlaceServiceModel("Hospital",R.drawable.places_background_circle,R.drawable.ic_local_hospital,"hospital",R.drawable.ic_local_hospital_red));
        serviceModels.add(new PlaceServiceModel("Petrol Pump",R.drawable.places_background_circle,R.drawable.ic_local_gas_station,"gas_station",R.drawable.ic_local_gas_station_red));
        serviceModels.add(new PlaceServiceModel("Fire Station",R.drawable.places_background_circle,R.drawable.fire_station,"fire_station",R.drawable.fire_station_red));
        serviceModels.add(new PlaceServiceModel("Police Station",R.drawable.places_background_circle,R.drawable.police_station,"police",R.drawable.police_station_red));
        serviceModels.add(new PlaceServiceModel("Garage",R.drawable.places_background_circle,R.drawable.garage,"car_repair",R.drawable.garage_red));
        serviceModels.add(new PlaceServiceModel("Medical Stores",R.drawable.places_background_circle,R.drawable.ic_local_hospital,"pharmacy",R.drawable.ic_local_hospital_red));

        rv_places.setLayoutManager(new GridLayoutManager(this,3));
        rv_places.setHasFixedSize(true);
        rv_places.setItemAnimator(new DefaultItemAnimator());
        NearByPlaceServicesAdapter adapter = new NearByPlaceServicesAdapter(serviceModels, this);
        rv_places.setAdapter(adapter);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        currentLocMarkar = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(28.644800, 77.216721))
                .title("Initial Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.644800, 77.216721), 5.0f));
        currentLocMarkar.setAlpha(0.0f);
        getLocationPermission();
        getDeviceLocation();
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                InfoWindow info = (InfoWindow) marker.getTag();
                if (info!=null){
                    ClipData clip = ClipData.newPlainText(info.getName(), info.getName()+"\n"+info.getAddress()+"\n"+info.getDistance());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(NearByPlacesActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDeviceLocation() {
        final LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLastKnownLocation=location;
                        Log.d(TAG, "onLocationResult: "+location);
                    }
                }

                if (mLastKnownLocation!=null && currentLocMarkar!=null && !isLocationUpdate){
                    currentLocMarkar.remove();
                    currentLocMarkar = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                            .title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 14.0f));

                    if (hereMap!=null) {
                        hereMap.setCenter(new GeoCoordinate(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), Map.Animation.NONE);
                    }
                    isLocationUpdate=true;
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLastKnownLocation=location;
                Log.d(TAG, "onSuccess: "+location);
            }
        });
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            mLocationPermissionGranted = false;
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_explore){
            if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            else
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else if(v.getId()==R.id.tv_title){
            InfoWindow info = (InfoWindow) v.getTag();
            ClipData clip = ClipData.newPlainText(info.getName(), info.getName()+"\n"+info.getAddress()+"\n"+info.getDistance());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!!", Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.fab_vehicle_location){
            startActivityForResult(new Intent(this, VTSSetVehicalActivity.class).putExtra("Call","nearByPlace"),1000);
            isMyLocation=false;
        }else if(v.getId()==R.id.fab_my_location){
            isMyLocation=true;
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showLocationEnableDialog();
            }else {
                getLocationPermission();
                getDeviceLocation();
            }

            if (mLastKnownLocation!=null && currentLocMarkar!=null/* && !isLocationUpdate*/){
                mMap.clear();
                currentLocMarkar.remove();
                currentLocMarkar = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                        .title("Your Location"));
                InfoWindow info = new InfoWindow("", " Your Location", "", "");
                currentLocMarkar.setTag(info);
                currentLocMarkar.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 12.0f));
                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getApplicationContext());
                mMap.setInfoWindowAdapter(customInfoWindow);
                isLocationUpdate=true;
            }
        }
        else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            placeServiceModel = (PlaceServiceModel) v.getTag();
            if (isMyLocation){
                pb_progress.setVisibility(View.VISIBLE);
                if (herePlacesMap.containsKey(placeServiceModel.getType())){
                    addPlacesMarkars(herePlacesMap.get(placeServiceModel.getType()), placeServiceModel);
                }else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fetchNearByLocations(placeServiceModel);
                        }
                    },5000);
                }
            }else {
                pb_progress.setVisibility(View.VISIBLE);
                if (vehiclePlacesMap.containsKey(vehicleNo)){
                    HashMap<String,List<PlaceLink>> places = hereVehiclePlacesMap.get(vehicleNo);
                    if (places!=null) {
                        if (places.containsKey(placeServiceModel.getType())) {
                            addPlacesMarkars(places.get(placeServiceModel.getType()), placeServiceModel);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fetchNearByLocations(placeServiceModel);
                                }
                            },5000);
                        }
                    }
                }else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fetchNearByLocations(placeServiceModel);
                        }
                    },5000);
                }
            }
        }
    }

    private void fetchNearByLocations(final PlaceServiceModel model) {
        if (isMyLocation && mLastKnownLocation!=null){
            hereMap.setCenter(new GeoCoordinate(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), Map.Animation.NONE);
        }else {
            hereMap.setCenter(new GeoCoordinate(vehicleLat,vehicleLng), Map.Animation.NONE);
        }
        DiscoveryRequest searchRequest = new SearchRequest(model.getService())
                .setSearchArea(hereMap.getCenter(),5000);
        //.setCollectionSize(10);
        searchRequest.execute(resultListener);

    }

    private void addPlacesMarkars(List<PlaceLink> results, PlaceServiceModel model) {
        pb_progress.setVisibility(View.GONE);
        setTitle(model.getService()+"s");
        placeResult.clear();
        mMap.clear();
        if (mLastKnownLocation!=null && currentLocMarkar!=null ){
            currentLocMarkar.remove(); // remove marker
            currentLocMarkar = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())));
            InfoWindow info = new InfoWindow("", "Vehicle Location", "", "");
            currentLocMarkar.setTag(info);
            if (!isMyLocation) {
                currentLocMarkar.remove();
                currentLocMarkar = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(vehicleLat, vehicleLng)));
                currentLocMarkar.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_car_icon_blue)));
                info = new InfoWindow("", "Vehicle No. "+ vehicleNo, "", "");
                currentLocMarkar.setTag(info);
            }
            currentLocMarkar.showInfoWindow();
            if (isMyLocation) {
                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                        .radius(5000)
                        .strokeWidth(2)
                        .strokeColor(Color.parseColor("#2196F3"))
                        .fillColor(Color.parseColor("#500084d3")));
            }else {
                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(vehicleLat, vehicleLng))
                        .radius(5000)
                        .strokeWidth(2)
                        .strokeColor(Color.parseColor("#2196F3"))
                        .fillColor(Color.parseColor("#500084d3")));
            }
        }
        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getApplicationContext());
        mMap.setInfoWindowAdapter(customInfoWindow);

        for (int i = 0; i < results.size(); i++) {
            PlaceLink result = results.get(i);
            GeoCoordinate loc = result.getPosition();
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(loc.getLatitude());
            location.setLongitude(loc.getLongitude());
            Location toLocation = new Location(LocationManager.GPS_PROVIDER);
            if (!isMyLocation) {
                toLocation.setLatitude(vehicleLat);
                toLocation.setLongitude(vehicleLng);
            }
            else
                toLocation =mLastKnownLocation;
            String distance = String.valueOf(result.getDistance()/1000)/*String.valueOf(location.distanceTo(toLocation) / 1000).substring(0, 5)*/;
            Log.d(TAG, "onResponse: " + distance + " KM");
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .title(result.getTitle());
            InfoWindow info = new InfoWindow("", result.getTitle(), result.getVicinity(), distance + " km away.");
            placeResult.add(info);
            Marker marker = mMap.addMarker(options);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(model.getMarkerRes())));
            marker.setTag(info);
            marker.showInfoWindow();
            if (i == 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 12.0f));
            }
        }
        if (isListView)
            placesAdapter.addInfoWindow(placeResult);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000 && resultCode==RESULT_OK && data!=null){
            vehicleNo = data.getStringExtra("vehicleNo");
            String vehicleId = data.getStringExtra("vehicleId");
            String lat = data.getStringExtra("lat");
            String lang = data.getStringExtra("lag");
            if (lat!=null && lang!=null) {
                if (!lat.isEmpty() && !lang.isEmpty()) {
                    vehicleLat = Double.parseDouble(lat);
                    vehicleLng = Double.parseDouble(lang);
                    currentLocMarkar.remove();
                    currentLocMarkar = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(vehicleLat, vehicleLng)));
                    currentLocMarkar.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_car_icon_blue)));
                    InfoWindow info = new InfoWindow("", "Vehicle No: " + vehicleNo, "", "");
                    currentLocMarkar.setTag(info);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(vehicleLat, vehicleLng), 12.0f));
                    currentLocMarkar.showInfoWindow();
                    CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getApplicationContext());
                    mMap.setInfoWindowAdapter(customInfoWindow);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        }
        else if (requestCode==2000){
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showLocationEnableDialog();
            }else {
                getLocationPermission();
                getDeviceLocation();
            }
        }
    }

    public Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void logPlacesApiError(String err){
        auth = auth.replace("\n", "");
        RetrofitInterface obj = ApiClient.getClient().create(RetrofitInterface.class);
        Call<ResponseBody> call = obj.uploadNearByPlacesApiError(auth,apiKey,err,"Places","android", BuildConfig.VERSION_NAME);
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

    private ResultListener<DiscoveryResultPage> resultListener = new ResultListener<DiscoveryResultPage>(){

        @Override
        public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode errorCode) {

            if (errorCode ==ErrorCode.NONE){
                searchPlaceList = discoveryResultPage.getPlaceLinks();
                searchResultList = discoveryResultPage.getItems();
                Log.d(TAG, "onCompleted: "+searchResultList.size()+"  "+searchPlaceList.size());
                addPlacesMarkars(searchPlaceList,placeServiceModel);
                for(PlaceLink placeLink : searchPlaceList){
                    Log.d(TAG, "onCompleted: "+placeLink.getTitle());
                    Log.d(TAG, "onCompleted: "+placeLink.getVicinity());
                    Log.d(TAG, "onCompleted: "+placeLink.getDistance());
                    Log.d(TAG, "onCompleted: "+placeLink.getPosition().getLatitude()+"  "+placeLink.getPosition().getLongitude());
                }

                if (isMyLocation)
                    herePlacesMap.put(placeServiceModel.getType(), searchPlaceList);
                else {
                    hereVehicleMap.put(placeServiceModel.getType(), searchPlaceList);
                    hereVehiclePlacesMap.put(vehicleNo, hereVehicleMap);
                }

            }else {
                logPlacesApiError(errorCode.toString());
                Toast.makeText(NearByPlacesActivity.this, errorCode.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
