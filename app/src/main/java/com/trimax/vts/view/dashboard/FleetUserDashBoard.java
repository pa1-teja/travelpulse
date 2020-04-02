package com.trimax.vts.view.dashboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.Target;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.shape.RoundedRectangle;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.about.AboutUS;
import com.trimax.vts.view.faq.FaqActivity;
import com.trimax.vts.view.lock.RemoteLockActivity;
import com.trimax.vts.view.notifications.NotificationActivity;
import com.trimax.vts.view.notifications.NotificationSettingActivity;
import com.trimax.vts.view.provider.ProviderListActivity;
import com.trimax.vts.view.R;
import com.trimax.vts.view.login.UserProfileActivity;
import com.trimax.vts.view.complaints.CustomerQueriesActivity;
import com.trimax.vts.view.reports.ReportsActivity;
import com.trimax.vts.view.vehicle.VehicleStatusActivity;
import com.trimax.vts.view.login.ChangePasswordActivity;
import com.trimax.vts.view.login.LoginActivity;
import com.trimax.vts.view.maps.FragmentHomeMap;
import com.trimax.vts.view.maps.GeoFencingNew;
import com.trimax.vts.view.maps.RealTimeTrackingActivity;
import com.trimax.vts.view.maps.ReplayTrackingActivity;
import com.trimax.vts.view.master.activity.Activity_Drivers;
import com.trimax.vts.view.master.activity.Activity_FleetSubUsers;
import com.trimax.vts.view.master.activity.Activity_Vehical_Group;
import com.trimax.vts.view.master.activity.Activity_Vehicle_Master;
import com.trimax.vts.view.master.model.FleetUserData;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.database.DatabaseClass;
import com.trimax.vts.drawer.Category;
import com.trimax.vts.drawer.SubCategory;
import com.trimax.vts.view.maps.CustomMapFragment;
import com.trimax.vts.view.maps.adapters.MyCustomAdapterForItems;
import com.trimax.vts.view.model.LatLngBean;
import com.trimax.vts.view.model.StringClusterItem;
import com.trimax.vts.model.InfoWindowData;
import com.trimax.vts.model.MenuModel;
import com.trimax.vts.model.notifications.BadgeView;
import com.trimax.vts.view.nearby.NearByPlacesActivity;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.utils.Permissions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;
import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

//@SuppressLint("NewApi")
public class FleetUserDashBoard extends AppCompatActivity implements OnClickListener, OnMapReadyCallback,
        ClusterManager.OnClusterItemInfoWindowClickListener<StringClusterItem>, OSSubscriptionObserver, InstallStateUpdatedListener {
    private static final String TAG = "FleetUserDashBoard";
    private DrawerLayout mDrawerLayout;
    private TextView changeMap, userName,bellIcon, profileText_tv;
    private TextView nonetwork_vehicle, idle_vehicle, moving_vehicle, stopped_vehicle;
    private ImageView maxminimg, maxminMapIcontwo;
    private ProgressDialog dialogg;
    private BadgeView badge;
    private RelativeLayout four_box, first_box, second_box, third_box,rl_dashboard;

    public List<FleetUserData.VehicleDetail> vehicleDetails;
    ArrayList<MenuModel> menumodel = new ArrayList<>();
    private ArrayList<SubCategory> subproduct,subreport,submaster,subvas,subsetting;
    private ArrayList<Category> category_name = new ArrayList<Category>();
    private ArrayList<ArrayList<SubCategory>> subcategory_name = new ArrayList<ArrayList<SubCategory>>();
    private ArrayList<Integer> subCatCount = new ArrayList<Integer>();
    String[] msgToDisplay;

    String vehicleLat = "", vehicleLnag = "", playerId = "";
    String token = "", deviceiid = "",maptType,vehiclechangeflag = "";
    private String FirstName, LastName, cust_id, usertype_id;

    public FleetUserData returnedData;
    public LatLngBean markerlatlong;
    DatabaseClass db;
    private ClusterManager<StringClusterItem> mClusterManager;
    private ActionBarDrawerToggle mDrawerToggle;
    Typeface font_awesome,type;
    private StringClusterItem clickedClusterItem;
    Activity context;
    CommonClass cc;
    int previousGroup;
    private Boolean doubleBackToExitPressedOnce = false;
    FragmentHomeMap FM = new FragmentHomeMap();
    GoogleMap mMap;
    CustomMapFragment supportMapFragment;
    Handler handler;

    private TravelpulseInfoPref infoPref;
    private AppUpdateManager appUpdateManager;
    private Calendar calendar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<Target> targets = new ArrayList<>();
    private int spotLightIndex=1;

    @SuppressWarnings("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_fleet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Home");
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        infoPref = new TravelpulseInfoPref(this);
        //Firebase Analytics
        logAnalyticsEvent();

        appUpdateManager = AppUpdateManagerFactory.create(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calendar = Calendar.getInstance();
                long lastUpdateChecked = infoPref.getLong(infoPref.APP_UPDATE_TIME,PrefEnum.Login);
                if (calendar.getTimeInMillis()>(lastUpdateChecked+(23*60*60000)))
                    checkUpdate();
            }
        },5000);


        Utils.getRecordId(infoPref.getString("deviceid", PrefEnum.OneSignal), "IMEI", infoPref.getString("token" +
                "", PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),infoPref);
        supportMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap));
        supportMapFragment.getMapAsync(this);
        maxminMapClicked();
        rl_dashboard = findViewById(R.id.layout_fourbox);

        maptType = "normal";
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        nonetwork_vehicle = findViewById(R.id.nonetwork_vehicle);
        idle_vehicle = findViewById(R.id.idle_vehicle);
        moving_vehicle = findViewById(R.id.moving_vehicle);
        stopped_vehicle = findViewById(R.id.stopped_vehicle);
        changeMap = findViewById(R.id.mapchangeID);
        maxminMapIcontwo = findViewById(R.id.maxminMapIcontwo);
        changeMap.setTextColor(ContextCompat.getColor(context, R.color.white_color));
        changeMap.setTypeface(font_awesome);

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)){
            Permissions.checkAndRequest(FleetUserDashBoard.this);
        }
        cc = new CommonClass(context);
        db = new DatabaseClass(context);
        type = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        handler = new Handler();

        Intent ii = getIntent();
        menumodel = (ArrayList<MenuModel>) ii.getSerializableExtra("menumodel");
        Log.d("Fleet", "onCreate: "+menumodel.size());
        if (menumodel!=null && menumodel.size() > 0) {
            String query = "insert into permissions(menu_seq,menu_status) values";
            for (int i = 0; i < menumodel.size(); i++) {
                MenuModel menu = menumodel.get(i);
                if (i == menumodel.size() - 1)
                    query += "('" + menu.getPosition() + "','" + menu.getStatus() + "')";
                else
                    query += "('" + menu.getPosition() + "','" + menu.getStatus() + "'),";
            }
            db.insertMenus(context, query);
        }

        menumodel= db.getMenuPermission();

        FirstName = infoPref.getString("first_name", PrefEnum.Login);
        LastName = infoPref.getString("last_name", PrefEnum.Login);
        usertype_id = infoPref.getString("user_type_id", PrefEnum.Login);
        cust_id = infoPref.getString("id", PrefEnum.Login);

        first_box = findViewById(R.id.first_box);
        second_box = findViewById(R.id.second_box);
        third_box = findViewById(R.id.third_box);
        four_box = findViewById(R.id.four_box);

        first_box.setOnClickListener(this);
        second_box.setOnClickListener(this);
        third_box.setOnClickListener(this);
        four_box.setOnClickListener(this);

        if (!cc.isConnected(context)) {
            if (dialogg != null) {
                dialogg.dismiss();
            }
            NoNetworkMsg();
        }

        getvalue();

        maxminMapIcontwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_dashboard.setVisibility(View.VISIBLE);
                maxminimg.setVisibility(View.VISIBLE);
                maxminimg.setBackgroundResource(R.drawable.maximize_black);
                maxminMapIcontwo.setVisibility(View.INVISIBLE);
            }
        });
        getCategoryData();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ExpandableListView mDrawerList = findViewById(R.id.list_slidermenu);
        mDrawerList.setGroupIndicator(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();
        mDrawerLayout.setDrawerListener(toggle);

        View header = LayoutInflater.from(this).inflate(R.layout.navigationdrawer_header, null);
        bellIcon = header.findViewById(R.id.BellIcon);
        bellIcon.setTypeface(type);
        badge = new BadgeView(this, bellIcon);
        badge.setTextSize(8);
        badge.setBadgeBackgroundColor(getResources().getColor(R.color.road_pulse_blue_color));
        badge.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
        bellIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NotificationActivity.class);
                startActivity(i);
            }
        });

        userName = header.findViewById(R.id.uName);
        userName.setText(FirstName + " " + LastName);
        profileText_tv = header.findViewById(R.id.profile_text_tv);
        profileText_tv.setText(FirstName.toUpperCase().charAt(0) + "" + LastName.toUpperCase().charAt(0));
        profileText_tv.setVisibility(View.VISIBLE);

        changeMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    if (maptType.equals("normal")) {
                        changeMap.setTextColor(getResources().getColor(R.color.dark_green));
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        maptType = "hybrid";
                    } else if (maptType.equals("hybrid")) {
                        changeMap.setTextColor(getResources().getColor(R.color.white_color));
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        maptType = "normal";
                    }
                }
            }
        });
        mDrawerList.addHeaderView(header);
        mDrawerList.setAdapter(new ExpandableListViewAdapter(FleetUserDashBoard.this, category_name, subcategory_name, subCatCount));

        mDrawerList.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    if (groupPosition != previousGroup) {
                        parent.collapseGroup(previousGroup);
                    }
                    previousGroup = groupPosition;
                    parent.expandGroup(groupPosition);
                }
                if (groupPosition == 0) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                    if (mMap != null) {
                        mMap.clear();
                    }
                }
                Category catgroup = category_name.get(groupPosition);
                if(catgroup.getCat_name().equalsIgnoreCase("About us")){
                    Intent intent = new Intent(context, AboutUS.class);
                    startActivity(intent);
                }
                if (catgroup.getCat_code()==8){
                    startActivity(new Intent(FleetUserDashBoard.this, RemoteLockActivity.class));
                }
                if(catgroup.getCat_code()==9){
                    startActivity(new Intent(FleetUserDashBoard.this, FaqActivity.class));
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_troubleshoot_url))));
                }
                if(catgroup.getCat_code()==7){
                    startActivity(new Intent(FleetUserDashBoard.this, NearByPlacesActivity.class));
                }

                if(catgroup.getCat_code()==11){
                    startActivity(new Intent(FleetUserDashBoard.this, CustomerQueriesActivity.class));
                }
                if(catgroup.getCat_name().equalsIgnoreCase("Logout")){
                     if (cc.isConnected(context)) {
                         fnlogout(infoPref.getString("record_id", PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),
                                 infoPref.getString("token", PrefEnum.OneSignal), infoPref.getString("user_type_id", PrefEnum.Login),
                                 infoPref.getString("id", PrefEnum.Login), "I",true);
                     } else {
                         CommonClass.DisplayToast(context, getString(R.string.network_error_message), "bottom");
                     }
                }
                parent.smoothScrollToPosition(groupPosition);
                return true;
            }
        });

        mDrawerList.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Category cat= category_name.get(groupPosition);

                switch (cat.getCat_name()){

                    case "Product Features":
                        SubCategory sb = subproduct.get(childPosition);
                        switch (sb.getSubcat_code()){
                            case "2.1":
                                infoPref.putString("livetrack", "y",PrefEnum.Login);
                                Intent intent = new Intent(context, RealTimeTrackingActivity.class);
                                startActivity(intent);
                                break;
                            case "2.2":
                                Intent intentt = new Intent(context, GeoFencingNew.class);
                                intentt.putExtra("callfrom", "home");
                                startActivityForResult(intentt, 1);
                                break;
                            case "2.3":
                                Intent i = new Intent(context, ReplayTrackingActivity.class);
                                Bundle b = new Bundle();
                                b.putString("callFromMain", "Yes");
                                b.putString("alrm","");
                                b.putString("alrm_vid","");
                                i.putExtras(b);
                                startActivity(i);
                                break;
                        }

                        break;
                    case "Reports":
                        SubCategory sbreport = subreport.get(childPosition);

                        switch (sbreport.getSubcat_code()) {
                            case "3.0":
                                startActivity(new Intent(FleetUserDashBoard.this, ReportsActivity.class));
                                break;
                            case "3.1":
                                Intent intent = new Intent(context, com.trimax.vts.view.reports.Activity.CurrentVehicleStatusReportActivityNew.class);
                                intent.putExtra("callfrom", "home");
                                startActivityForResult(intent, 1);
                                break;
                            case "3.2":
                                intent = new Intent(context,  com.trimax.vts.view.reports.Activity.AlertReportActivity.class);
                                intent.putExtra("report", "alert");
                                startActivity(intent);
                                break;
                            case "3.3":
                                intent = new Intent(context,  com.trimax.vts.view.reports.Activity.VehicleStopReportActivity.class);
                                intent.putExtra("report", "stop_report");
                                startActivity(intent);
                                break;
                            case "3.4":
                                intent = new Intent(context,  com.trimax.vts.view.reports.Activity.VehicleIdleReportActivity.class);
                                intent.putExtra("report", "daily_report");
                                startActivity(intent);
                                break;
                            case "3.5":
                                intent = new Intent(context,  com.trimax.vts.view.reports.Activity.VehicleRunReportActivity.class);
                                intent.putExtra("report", "ideal_report");
                                startActivity(intent);
                                break;
                            case "3.6":
                                intent = new Intent(context,  com.trimax.vts.view.reports.Activity.VehicleSummaryReportActivityNew.class);
                                intent.putExtra("report", "Alert_Summary");
                                startActivity(intent);
                                break;
                            case "3.7":
                                intent = new Intent(context,  com.trimax.vts.view.reports.Activity.AlertSummaryReportActivityNew.class);
                                intent.putExtra("report", "Alert_Summary");
                                startActivity(intent);
                                break;

                        }
                        break;
                    case "Manage Masters":
                        SubCategory sbmaster = submaster.get(childPosition);

                        switch (sbmaster.getSubcat_code()){

                            case "4.1":
                                Intent i = new Intent(context, Activity_Vehicle_Master.class);
                                startActivity(i);
                                break;
                            case "4.2":
                                i = new Intent(context, Activity_Drivers.class);
                                startActivity(i);
                                break;
                            case "4.3":
                                i = new Intent(context, Activity_Vehical_Group.class);
                                startActivity(i);
                                break;
                            case "4.4":
                                i = new Intent(context, Activity_FleetSubUsers.class);
                                startActivity(i);
                                break;
                        }

                        break;
                    case "Value Added Services":
                        SubCategory sbvas = subvas.get(childPosition);
                        switch (sbvas.getSubcat_code()){
                            case "5.1":
                                FuelandTireRepairCommon("TIREREPAIR", "");
                                break;
                            case "5.2":
                                FuelandTireRepairCommon("CARTOWING", "");
                                break;
                            case "5.3":
                                FuelandTireRepairCommon("FUEL", "");
                                break;
                            case "5.4":
                                Toast.makeText(context, "Please contact call center to avail this service", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        break;
                    case "Settings":
                        SubCategory sbsetting = subsetting.get(childPosition);
                        switch (sbsetting.getSubcat_code()){
                            case "6.1":
                                Intent intent = new Intent(context, UserProfileActivity.class);
                                startActivity(intent);
                                break;
                            case "6.2":
                                intent = new Intent(context, ChangePasswordActivity.class);
                                startActivity(intent);
                                break;
                            case "6.3":
                                intent = new Intent(context, AboutUS.class);
                                startActivity(intent);
                                break;
                        }

                        break;
                    case "Logout":
                        break;

                }
                mDrawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawer();

        fnlogout(infoPref.getString("record_id", PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),
                infoPref.getString("token", PrefEnum.OneSignal), infoPref.getString("user_type_id", PrefEnum.Login),
                infoPref.getString("id", PrefEnum.Login), "A",false);
        if(!infoPref.getBoolean("is_notification", PrefEnum.Login))
            setNotificationPref();
        //showSpotLight();
    }

    private void NoNetworkMsg() {
        msgToDisplay = new String[]{context.getString(R.string.refresh_network_error_message) + ". Please connect to the Internet and swipe down to refresh "};
    }

    public void vtsShareClicked(View view) {
        SharedPreferences sharedpref = getSharedPreferences(Constants.app_preference, Context.MODE_PRIVATE);
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

    public void maxminMapClicked() {
        maxminimg = findViewById(R.id.zoomInOut);
        maxminimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                maxminMapIcontwo.setVisibility(View.VISIBLE);
                maxminMapIcontwo.setBackgroundResource(R.drawable.amin);
                rl_dashboard.setVisibility(View.INVISIBLE);
                maxminimg.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_call) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:02242931002"));
            startActivity(intent);
            return true;
        }else if(id ==R.id.action_notification){
            startActivity(new Intent(this, NotificationSettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void getCategoryData() {

        for (int i = 0; i < menumodel.size(); i++) {
            MenuModel menu = menumodel.get(i);
            String Position = menu.getPosition();
            if (menu.getStatus() == 1)
                switch (Position.length()) {
                    case 1:
                        switch (Integer.parseInt(Position)) {
                            case 1:
                                Category newcat = new Category();
                                newcat.setCat_name("Home");
                                newcat.setCat_code(1);
                                newcat.setImageid(R.mipmap.ic_launcher_round);
                                if (!category_name.contains(newcat))
                                    category_name.add(newcat);
                                ArrayList<SubCategory> subhome = new ArrayList<>();
                                subcategory_name.add(subhome);
                                subCatCount.add(subhome.size());
                                break;
                        }

                        break;

                    case 3:

                        String[] spittedPosition = Position.replace(".", ",").split(",");
                        switch (spittedPosition[0]) {

                            case "2":
                                Category newcat = new Category();
                                newcat.setCat_name("Product Features");
                                newcat.setImageid(R.drawable.menu_product_feature);
                                newcat.setCat_code(2);
                                if (!category_name.contains(newcat))
                                    category_name.add(newcat);

                                if (subproduct == null) {
                                    subproduct = new ArrayList<>();
                                }

                                break;
                            case "3":
                                newcat = new Category();
                                newcat.setCat_name("Reports");
                                newcat.setImageid(R.drawable.report);

                                newcat.setCat_code(3);
                                if (!category_name.contains(newcat))
                                    category_name.add(newcat);
                                if (subreport == null) {
                                    subreport = new ArrayList<>();
                                }
                                break;
                            case "4":
                                newcat = new Category();
                                newcat.setCat_name("Manage Masters");
                                newcat.setImageid(R.drawable.manage_master);

                                newcat.setCat_code(3);
                                if (!category_name.contains(newcat))
                                    category_name.add(newcat);
                                if (submaster == null) {
                                    submaster = new ArrayList<>();
                                }
                                break;
                            case "5":
                                newcat = new Category();
                                newcat.setCat_name("Value Added Services");
                                newcat.setImageid(R.drawable.menu_value_added_red);

                                newcat.setCat_code(5);
                                if (!category_name.contains(newcat))
                                    category_name.add(newcat);
                                if (subvas == null) {
                                    subvas = new ArrayList<>();
                                }
                                break;

                            case "6":
                                newcat = new Category();
                                newcat.setCat_name("Settings");
                                newcat.setImageid(R.drawable.setting);
                                newcat.setCat_code(6);
                                if (!category_name.contains(newcat))
                                    category_name.add(newcat);
                                if (subsetting == null) {
                                    subsetting = new ArrayList<>();
                                }
                                break;
                        }


                        switch (Position) {

                            case "2.1":
                                SubCategory subCategory = new SubCategory();
                                subCategory.setSubcat_code("2.1");
                                subCategory.setSubcat_name("Real Time Vehicle Tracking");
                                subCategory.setSubcatimageid(R.drawable.menu_realtracking);
                                if (!subproduct.contains(subCategory) && menu.getStatus() == 1) {
                                    subproduct.add(subCategory);
                                }
                                break;
                            case "2.2":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("2.2");
                                subCategory.setSubcat_name("Virtual Boundary");
                                subCategory.setSubcatimageid(R.drawable.menu_virtualboundry);

                                if (!subproduct.contains(subCategory)) {
                                    subproduct.add(subCategory);
                                }
                                break;
                            case "2.3":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("2.3");
                                subCategory.setSubcat_name("Vehicle Movement Replay");
                                subCategory.setSubcatimageid(R.drawable.menu_replay);

                                if (!subproduct.contains(subCategory)) {
                                    subproduct.add(subCategory);
                                }
                                break;

                            case "3.1":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("3.1");
                                subCategory.setSubcat_name("Current Vehicle Status Report");
                                subCategory.setSubcatimageid(R.drawable.report_red);

                                if (!subreport.contains(subCategory)) {
                                    subreport.add(subCategory);
                                }
                                break;
                            case "3.2":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("3.2");
                                subCategory.setSubcat_name("Alert Report");
                                subCategory.setSubcatimageid(R.drawable.alert_report);
                                if (!subreport.contains(subCategory)) {
                                    subreport.add(subCategory);
                                }
                                break;
                            case "3.3":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("3.3");
                                subCategory.setSubcat_name("Vehicle Stop Summary Report");
                                subCategory.setSubcatimageid(R.drawable.report_red);
                                if (!subreport.contains(subCategory)) {
                                    subreport.add(subCategory);
                                }
                                break;
                            case "3.4":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("3.4");
                                subCategory.setSubcat_name("Vehicle Idle Summary Report");
                                subCategory.setSubcatimageid(R.drawable.report_red);
                                if (!subreport.contains(subCategory)) {
                                    subreport.add(subCategory);
                                }
                                break;
                            case "3.5":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("3.5");
                                subCategory.setSubcat_name("Vehicle Run Summary Report");
                                subCategory.setSubcatimageid(R.drawable.report_red);
                                if (!subreport.contains(subCategory)) {
                                    subreport.add(subCategory);
                                }
                                break;
                            case "3.6":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("3.6");
                                subCategory.setSubcat_name("Vehicle Summary Report");
                                subCategory.setSubcatimageid(R.drawable.report_red);
                                if (!subreport.contains(subCategory)) {
                                    subreport.add(subCategory);
                                }
                                break;
                            case "3.7":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("3.7");
                                subCategory.setSubcat_name("Alert Summary Report");
                                subCategory.setSubcatimageid(R.drawable.report_red);
                                if (!subreport.contains(subCategory)) {
                                    subreport.add(subCategory);
                                }
                                break;
                            case "4.1":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("4.1");
                                subCategory.setSubcat_name("Vehicles");
                                subCategory.setSubcatimageid(R.drawable.vehicle);
                                if (!submaster.contains(subCategory)) {
                                    submaster.add(subCategory);
                                }
                                break;
                            case "4.2":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("4.2");
                                subCategory.setSubcat_name("Drivers");
                                subCategory.setSubcatimageid(R.drawable.driver);
                                if (!submaster.contains(subCategory)) {
                                    submaster.add(subCategory);
                                }
                                break;
                            case "4.3":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("4.3");
                                subCategory.setSubcat_name("Vehicles Groups");
                                subCategory.setSubcatimageid(R.drawable.vehicle_group);
                                if (!submaster.contains(subCategory)) {
                                    submaster.add(subCategory);
                                }
                                break;
                            case "4.4":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("4.4");
                                subCategory.setSubcat_name("Fleet subusers");
                                subCategory.setSubcatimageid(R.drawable.fleet_sub_user);
                                if (!submaster.contains(subCategory)) {
                                    submaster.add(subCategory);
                                }
                                break;
                            case "5.1":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("5.1");
                                subCategory.setSubcat_name("Flat Tyre");
                                subCategory.setSubcatimageid(R.drawable.menu_flat_tyre_red);
                                if (!subvas.contains(subCategory)) {
                                    subvas.add(subCategory);
                                }
                                break;

                            case "5.2":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("5.2");
                                subCategory.setSubcat_name("Vehicle Towing");
                                subCategory.setSubcatimageid(R.drawable.menu_vehicle_towing_red);
                                if (!subvas.contains(subCategory)) {
                                    subvas.add(subCategory);
                                }
                                break;
                            case "5.3":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("5.3");
                                subCategory.setSubcat_name("Fuel Delivery");
                                subCategory.setSubcatimageid(R.drawable.menu_fuel_delivery_red);
                                if (!subvas.contains(subCategory)) {
                                    subvas.add(subCategory);
                                }
                                break;
                            case "5.4":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("5.4");
                                subCategory.setSubcat_name("Battery Jump Start");
                                subCategory.setSubcatimageid(R.drawable.menu_batteryjump_red);
                                if (!subvas.contains(subCategory)) {
                                    subvas.add(subCategory);
                                }
                                break;
                            case "6.1":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("6.1");
                                subCategory.setSubcat_name("User Profile");
                                subCategory.setSubcatimageid(R.drawable.user_profile_red);
                                if (!subsetting.contains(subCategory)) {
                                    subsetting.add(subCategory);
                                }
                                break;
                            case "6.2":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("6.2");
                                subCategory.setSubcat_name("Change Password");
                                subCategory.setSubcatimageid(R.drawable.change_pwd);
                                if (!subsetting.contains(subCategory)) {
                                    subsetting.add(subCategory);
                                }
                                break;
                           /* case "6.3":
                                subCategory = new SubCategory();
                                subCategory.setSubcat_code("6.3");
                                subCategory.setSubcat_name("About us");
                                if (!subsetting.contains(subCategory)) {
                                    subsetting.add(subCategory);
                                }
                                break;*/
                        }

                        break;
                }
        }

        if (subproduct != null) {
            subcategory_name.add(subproduct);
            subCatCount.add(subproduct.size());
        }
        if (subreport != null) {
                SubCategory subCategory = new SubCategory();
                subCategory.setSubcat_code("3.0");
                subCategory.setSubcat_name("Vehicle Status Summary Report");
                subCategory.setSubcatimageid(R.drawable.report);

                if (!subreport.contains(subCategory)) {
                    subreport.add(0,subCategory);
                }
            subcategory_name.add(subreport);
            subCatCount.add(subreport.size());
        }
        if (submaster != null) {
            subcategory_name.add(submaster);
            subCatCount.add(submaster.size());
        }
        if (subvas != null) {
            subcategory_name.add(subvas);
            subCatCount.add(subvas.size());
        }

        if(subsetting==null) {
            Category newcat = new Category();
            newcat.setCat_name("Settings");
            newcat.setImageid(R.drawable.setting);
            newcat.setCat_code(6);
            if (!category_name.contains(newcat))
                category_name.add(newcat);

            subsetting = new ArrayList<>();
        }

        if (subsetting != null) {
            subcategory_name.add(subsetting);
            subCatCount.add(subsetting.size());
        }

        Category nearBy = new Category();
        nearBy.setCat_code(7);
        nearBy.setCat_name("Near By Places");
        nearBy.setImageid(R.drawable.ic_explore);
        category_name.add(nearBy);
        ArrayList<SubCategory> subaboutus = new ArrayList<>();
        subcategory_name.add(subaboutus);
        subCatCount.add(subaboutus.size());

        Category lock = new Category();
        lock.setCat_code(8);
        lock.setCat_name("Remote Lock");
        lock.setImageid(R.drawable.ic_explore);
        category_name.add(lock);
        ArrayList<SubCategory> lockCat = new ArrayList<>();
        subcategory_name.add(lockCat);
        subCatCount.add(lockCat.size());

        Category confSetting = new Category();
        confSetting.setCat_code(9);
        confSetting.setCat_name("FAQ");
        confSetting.setImageid(R.drawable.ic_faq);
        category_name.add(confSetting);
        subaboutus = new ArrayList<>();
        subcategory_name.add(subaboutus);
        subCatCount.add(subaboutus.size());


        Category support = new Category();
        support.setCat_code(11);
        support.setCat_name("Help & Support");
        support.setImageid(R.drawable.ic_support);
        category_name.add(support);
        subaboutus = new ArrayList<>();
        subcategory_name.add(subaboutus);
        subCatCount.add(subaboutus.size());

        Category aboutus = new Category();
        aboutus.setCat_code(10);
        aboutus.setCat_name("About us");
        aboutus.setImageid(R.drawable.about_us);
        category_name.add(aboutus);
        subaboutus = new ArrayList<>();
        subcategory_name.add(subaboutus);
        subCatCount.add(subaboutus.size());

        Category logcatt = new Category();
        logcatt.setCat_code(12);
        logcatt.setCat_name("Logout");
        logcatt.setImageid(R.drawable.logout);
        category_name.add(logcatt);
        ArrayList<SubCategory> sublogout = new ArrayList<>();
        subcategory_name.add(sublogout);
        subCatCount.add(sublogout.size());
    }

    @Override
    public void onPause() {
        super.onPause();
        FM.stopTimerTask();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            System.exit(0);
            super.onBackPressed();
            return;
        } else if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) { //replace this with actual function which returns if the drawer is open
            mDrawerLayout.closeDrawer(Gravity.LEFT);     // replace this with actual function which closes drawer
        } else {
            this.doubleBackToExitPressedOnce = true;
            CommonClass.DisplayToast(context, "Click BACK again to EXIT", "bottom");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public  void FuelandTireRepairCommon(String ServiceCode, String networkcall) {
        Bundle bundle = new Bundle();
        bundle.putString("serviceCode", ServiceCode);
        Intent i = new Intent(context, ProviderListActivity.class);
        i.putExtras(bundle);
        context.startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_call_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.644800, 77.216721), 4.0f));

        getvalue();
        current_vehicle_status_dashboard(infoPref.getString("user_type_id", PrefEnum.Login), infoPref.getString("id", PrefEnum.Login));
        changeMap.setTextColor(getResources().getColor(R.color.white_color));
    }

    public void current_vehicle_status_dashboard(String usertype_id, final String user_id) {
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.current_vehicle_status_dashboard(auth, apiKey, usertype_id, user_id);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                                    try {
                                            GsonBuilder builder = new GsonBuilder();
                                            Gson gson = builder.create();
                                            returnedData = gson.fromJson(syncresponse, FleetUserData.class);
                                            vehicleDetails = returnedData.getVehicleDetail();
                                            InfoWindowData info = new InfoWindowData();
                                            for (int i = 0; i < vehicleDetails.size(); i++) {
                                                markerlatlong = new LatLngBean();
                                                vehicleLat = vehicleDetails.get(i).getLat();
                                                vehicleLnag = vehicleDetails.get(i).getLng();
                                                markerlatlong.setLatitude(vehicleLat);
                                                markerlatlong.setLongitude(vehicleLnag);
                                                info.setVehicleNo("Vehicle No:" + vehicleDetails.get(i).getVehicleNo());
                                                info.setLocation("Location:" + vehicleDetails.get(i).getLabel());
                                                info.setSpeed("Speed:" + vehicleDetails.get(i).getSpeed());
                                                info.setIgnition("Ignition" + vehicleDetails.get(i).getIgnition()+"");
                                                info.setAc("AC:" + vehicleDetails.get(i).getAc());
                                                if (vehicleDetails.get(i).getGps()!=null) {
                                                    if (vehicleDetails.get(i).getGps().equalsIgnoreCase("1"))
                                                        info.setGps("GPS");
                                                    else
                                                        info.setGps("GPS");
                                                }else {
                                                    info.setGps("");
                                                }
                                                info.setDate("Update On:" + vehicleDetails.get(i).getUpdatedOn());
                                                info.setDriver("Driver" + vehicleDetails.get(i).getDriverName());
                                                info.setOwner("Owner:" + vehicleDetails.get(i).getOwnerName());
                                                info.setDate("Group:" + vehicleDetails.get(i).getGroupName());
                                                info.setVehicle_type_id(vehicleDetails.get(i).getVehicle_type_id());
                                            }
                                            moving_vehicle.setText(myObject.getJSONObject("data").getString("vehicle_status_moving"));
                                            stopped_vehicle.setText(myObject.getJSONObject("data").getString("vehicle_status_stop"));
                                            idle_vehicle.setText(myObject.getJSONObject("data").getString("vehicle_status_idle"));
                                            nonetwork_vehicle.setText(myObject.getJSONObject("data").getString("vehicle_status_no_network"));

                                            mClusterManager = new ClusterManager<>(FleetUserDashBoard.this, mMap);
                                            mMap.setOnInfoWindowClickListener(mClusterManager);
                                            mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
                                            mMap.setOnMarkerClickListener(mClusterManager);
                                            mMap.setOnCameraIdleListener(mClusterManager);

                                            List<StringClusterItem> clusterItems = new ArrayList<>();

                                            for (int i = 0; i < vehicleDetails.size(); i++) {
                                                vehicleLat = vehicleDetails.get(i).getLat();
                                                vehicleLnag = vehicleDetails.get(i).getLng();

                                                final LatLng latLng = new LatLng(Double.parseDouble(vehicleLat), Double.parseDouble(vehicleLnag));

                                                StringClusterItem ss = new StringClusterItem("", latLng, vehicleDetails.get(i).getVehicleNo(), vehicleDetails.get(i).getUpdatedOn(), vehicleDetails.get(i).getSpeed()
                                                        , vehicleDetails.get(i).getIgnition(),vehicleDetails.get(i).getGps(), vehicleDetails.get(i).getAc(), vehicleDetails.get(i).getNetwork(), vehicleDetails.get(i).getVehicleId(), vehicleDetails.get(i).getGroupName(), vehicleDetails.get(i).getDriverName(), vehicleDetails.get(i).getOwnerName(), vehicleDetails.get(i).getVehicle_type_id());
                                                clusterItems.add(ss);
                                            }
                                            mClusterManager.addItems(clusterItems);

                                            mClusterManager.setRenderer(new MyCustomAdapterForItems(FleetUserDashBoard.this, mMap, mClusterManager,clickedClusterItem));

                                            mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<StringClusterItem>() {
                                                        @Override
                                                        public boolean onClusterClick(Cluster<StringClusterItem> cluster) {
                                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), (float) Math.floor(mMap.getCameraPosition().zoom + 1)), 300, null);
                                                            return true;
                                                        }
                                                    });

                                            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<StringClusterItem>() {
                                                        @Override
                                                        public boolean onClusterItemClick(StringClusterItem clusterItem) {
                                                            clickedClusterItem = clusterItem;
                                                            mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems(context, mMap, mClusterManager,clickedClusterItem));
                                                            return false;
                                                        }
                                                    });

                                            mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<StringClusterItem>() {
                                                        @Override
                                                        public void onClusterItemInfoWindowClick(StringClusterItem stringClusterItem) {
                                                            infoPref.getLoginEditor().putString("vid", stringClusterItem.getVid());
                                                            infoPref.getLoginEditor().putString("livetrack", "");
                                                            Intent i = new Intent(FleetUserDashBoard.this, RealTimeTrackingActivity.class);
                                                            i.putExtra("vid", stringClusterItem.getVid());
                                                            startActivity(i);
                                                        }
                                                    });

                                            mClusterManager.cluster();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                   }
            });
    }

    @Override
    public void onClusterItemInfoWindowClick(StringClusterItem myItem) {
        //Cluster item InfoWindow clicked, set title as action
        infoPref.getLoginEditor().putString("vid", myItem.getSpeed());
        Intent i = new Intent(this, RealTimeTrackingActivity.class);
        i.putExtra("vid", myItem.getSpeed());
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, VehicleStatusActivity.class);
        switch (view.getId()) {
            case R.id.first_box:
                intent.putExtra("status", "stop");
                startActivity(intent);
                break;
            case R.id.second_box:
                intent.putExtra("status", "moving");
                startActivity(intent);
                break;
            case R.id.third_box:
                intent.putExtra("status", "idle");
                startActivity(intent);
                break;
            case R.id.four_box:
                intent.putExtra("status", "nonetwork");
                startActivity(intent);
                break;
        }

    }

    public void getvalue() {
        if (TextUtils.isEmpty(infoPref.getString("deviceid",PrefEnum.OneSignal))) {
            deviceiid = CommonClass.getDevice_Id(this);
            infoPref.putString("deviceid", deviceiid,PrefEnum.OneSignal);
        }else {
            deviceiid = infoPref.getString("deviceid",PrefEnum.OneSignal);
        }
        playerId = infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
        token = infoPref.getString("token", PrefEnum.OneSignal);
    }

    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() && stateChanges.getTo().getSubscribed()) {
            stateChanges.getTo().getUserId();
            infoPref.putString("token", stateChanges.getTo().getPushToken(),PrefEnum.OneSignal);
            infoPref.putString("GT_PLAYER_ID", stateChanges.getTo().getUserId(),PrefEnum.OneSignal);
            getvalue();
        }
        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }

    private void fnlogout(String strdeviveid,String pid,String token,String strUserName, final String strusertypeid,String strStatus, boolean isLogout) {
        if (isLogout) {
            dialogg = new ProgressDialog(context);
            dialogg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogg.setCancelable(false);
            dialogg.setMessage("Please Wait...");
            dialogg.show();
        }
            Call<ResponseBody> call= RepositryInstance.getNotificationRepository().changeUserStatus(strdeviveid,pid,token,strUserName,strusertypeid,strStatus);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        if(dialogg!=null){
                            dialogg.dismiss();
                        }
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String strStatus = myObject.getString("status");

                                if (isLogout){
                                    infoPref.putBoolean("is_notification",false,PrefEnum.Login);
                                    infoPref.remove("vid",PrefEnum.Login);
                                    infoPref.remove("vidgeo",PrefEnum.Login);
                                    infoPref.remove("livetrack",PrefEnum.Login);
                                    infoPref.putString("logout","y",PrefEnum.Login);
                                    if(strStatus.equalsIgnoreCase("success")){
                                        db.deletedata();
                                        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context, LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                                if(strStatus.equalsIgnoreCase("failure")){
                                        String msg = myObject.getString("Msg");
                                        if(msg.equalsIgnoreCase("device_not_exists")){
                                            infoPref.getLoginEditor().clear().commit();
                                            infoPref.getOneSignalEditor().remove("record_id").commit();
                                        }
                                        if(msg.equalsIgnoreCase("user_not_exists")){
                                            infoPref.getLoginEditor().clear().commit();
                                            infoPref.getOneSignalEditor().remove("record_id").commit();
                                        }
                                        startActivity(new Intent(FleetUserDashBoard.this, LoginActivity.class));
                                }
                                break;
                            case 400:

                                break;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(FleetUserDashBoard.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void setNotificationPref(){
        dialogg = new ProgressDialog(context);
        dialogg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogg.setCancelable(false);
        dialogg.setMessage("Please Wait...");
        dialogg.show();
        Call<ResponseBody> call=RepositryInstance.getNotificationRepository().setNotificationPrefOnLogin(usertype_id, playerId,infoPref.getString("record_id",PrefEnum.OneSignal),cust_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                infoPref.putBoolean("is_notification",true,PrefEnum.Login);
                dialogg.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                dialogg.dismiss();
            }
        });

        dialogg.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            showUpdateSnackbar();
                        }
                    }
                });

        FM.markerPoints = null;
        FM.pointRepeated = false;
        FM.wholeData = null;
        FirstName = infoPref.getString("first_name", PrefEnum.Login);
        LastName = infoPref.getString("last_name", PrefEnum.Login);
        userName.setText(FirstName + " " + LastName);
        profileText_tv.setText(FirstName.toUpperCase().charAt(0) + "" + LastName.toUpperCase().charAt(0)); //added by pankaj

        if (!cc.isConnected(context)) {
            CommonClass.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
            NoNetworkMsg();
        }

        vehiclechangeflag = infoPref.getString("vehiclechangeflag", PrefEnum.Login);
        if (vehiclechangeflag.equals("vehicle_changed")) {
            supportMapFragment.getMapAsync(this);
            if (mMap != null) {
                mMap.clear();
            }
            changeMap.setTextColor(getResources().getColor(R.color.white_color));
            FM.markerPoints = null;
            infoPref.getLoginEditor().remove("vehiclechangeflag");
        }
        supportMapFragment.getMapAsync(this);
        if (mMap != null) {
            mMap.clear();
        }
        getvalue();
        current_vehicle_status_dashboard(infoPref.getString("user_type_id", PrefEnum.Login), infoPref.getString("id", PrefEnum.Login));
    }

    private void showSpotLight(){
        FrameLayout frameLayout = new FrameLayout(this);
        View spotLightView = getLayoutInflater().inflate(R.layout.spotlight_view,frameLayout);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Target target = new Target.Builder()
                .setAnchor(width-80,140)
                .setShape(new Circle(100f,1000L,new DecelerateInterpolator(2f)))
                .setOverlay(spotLightView)
                .build();
        targets.add(target);

        target = new Target.Builder()
                .setAnchor(150,1030)
                .setShape(new RoundedRectangle(110f,800f,10f,500L,new DecelerateInterpolator(2f)))
                .setOverlay(spotLightView)
                .build();
        targets.add(target);

        // create spotlight
        Spotlight spotlight = new Spotlight.Builder(this)
                .setTargets(targets)
                .setBackgroundColor(R.color.grey_50)
                .setDuration(1000L)
                .setAnimation(new DecelerateInterpolator(2f))
                .build();
        spotlight.start();

        String messages[] = {"Device wise setting for delay notification.","Open settings for maintaining user settings"};
        Button btn_next = spotLightView.findViewById(R.id.btn_tap);
        TextView tv_message = spotLightView.findViewById(R.id.tv_message);
        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
                spotlight.next();
                if (targets.size()>spotLightIndex)
                    tv_message.setText(messages[spotLightIndex]);
                spotLightIndex++;
            }
        });
    }

    private void checkUpdate(){
        calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(final AppUpdateInfo result) {
                if (result.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    infoPref.putLong(infoPref.APP_UPDATE_TIME,calendar.getTimeInMillis(),PrefEnum.Login);
                    try {
                        appUpdateManager.registerListener(FleetUserDashBoard.this);
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE, FleetUserDashBoard.this, 2000);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }else if (result.installStatus() == InstallStatus.DOWNLOADED){
                    showUpdateSnackbar();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: 11");
            }else if (resultCode==RESULT_CANCELED ){
                Log.d(TAG, "onActivityResult: 22");
            }else if (resultCode==RESULT_IN_APP_UPDATE_FAILED){
                Log.d(TAG, "onActivityResult: 33");
            }
        }
        else if (resultCode != 0) {
            if (requestCode == 1) {
                if (supportMapFragment != null)
                    supportMapFragment.getMapAsync(this);
                mMap.clear();
                FM.markerPoints = null;
                FM.pointRepeated = false;
                FM.wholeData = null;
            }
        }
    }

    @Override
    public void onStateUpdate(InstallState state) {
        if (state.installStatus() == InstallStatus.DOWNLOADED){
            showUpdateSnackbar();
        } else if (state.installStatus() == InstallStatus.INSTALLED){
            if (appUpdateManager != null){
                appUpdateManager.unregisterListener(this);
            }

        } else {
            Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
        }
    }

    void showUpdateSnackbar() {
        Snackbar.make(mDrawerLayout, "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE).setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appUpdateManager != null)
                    appUpdateManager.completeUpdate();
            }
        }).show();
    }

    private void logAnalyticsEvent(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, infoPref.getString("username",PrefEnum.Login));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, infoPref.getString("first_name",PrefEnum.Login));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appUpdateManager.unregisterListener(this);
    }
}
