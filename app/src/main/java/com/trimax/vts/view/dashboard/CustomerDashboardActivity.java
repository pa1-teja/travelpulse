package com.trimax.vts.view.dashboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.takusemba.spotlight.Target;
import com.takusemba.spotlight.shape.RoundedRectangle;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.model.notifications.NotificationListData;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.about.AboutUS;
import com.trimax.vts.view.complaints.CustomerQueriesActivity;
import com.trimax.vts.view.faq.FaqActivity;
import com.trimax.vts.view.lock.RemoteLockActivity;
import com.trimax.vts.view.notifications.NotificationActivity;
import com.trimax.vts.view.notifications.NotificationSettingActivity;
import com.trimax.vts.view.provider.ProviderListActivity;
import com.trimax.vts.view.R;
import com.trimax.vts.view.login.UserProfileActivity;
import com.trimax.vts.view.login.ChangePasswordActivity;
import com.trimax.vts.view.login.LoginActivity;
import com.trimax.vts.view.maps.FragmentHomeMap;
import com.trimax.vts.view.maps.GeoFencingNew;
import com.trimax.vts.view.maps.RealTimeTrackingActivity;
import com.trimax.vts.view.maps.ReplayTrackingActivity;
import com.trimax.vts.view.nearby.NearByPlacesActivity;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.view.master.activity.Activity_Drivers;
import com.trimax.vts.view.master.activity.Activity_FleetSubUsers;
import com.trimax.vts.view.master.activity.Activity_Vehical_Group;
import com.trimax.vts.view.master.activity.Activity_Vehicle_Master;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.notifications.adapter.NotificationListAdapterMain;
import com.trimax.vts.database.DatabaseClass;
import com.trimax.vts.drawer.Category;
import com.trimax.vts.drawer.SubCategory;
import com.trimax.vts.view.maps.CustomMapFragment;

import com.trimax.vts.model.MenuModel;
import com.trimax.vts.model.notifications.Datum;
import com.trimax.vts.model.notifications.BadgeView;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.utils.Permissions;
import com.trimax.vts.view.reports.ReportsActivity;

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

public class CustomerDashboardActivity extends AppCompatActivity implements OnMapReadyCallback, OSSubscriptionObserver, InstallStateUpdatedListener {
    private static final String TAG = "CustomerDashboardActivi";
    NotificationListAdapterMain adapter;
    private DrawerLayout mDrawerLayout;
    DatabaseClass db;
    ArrayList<MenuModel> menumodel = new ArrayList<>();
    String playerId = "", deviceId ="",token="", deviceiid="",user_type_id="",id="", FirstName, LastName, UserAccessType;
    NotificationListData notificationListdata;
    ArrayList<Datum> listnew = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;
    Typeface font_awesome,type;
    TextView userName, showProfile, bellIcon, shareIcon, profileText_tv, refreshText,refreshToast;
    SwipeRefreshLayout swipeRefresh;
    Activity context;
    CommonClass cc;
    int previousGroup;
    ListView lv;
    BadgeView badge;

    Boolean doubleBackToExitPressedOnce = false;
    ProgressDialog dialogg;
    private ArrayList<Category> category_name = new ArrayList<Category>();
    private ArrayList<ArrayList<SubCategory>> subcategory_name = new ArrayList<ArrayList<SubCategory>>();
    private ArrayList<Integer> subCatCount = new ArrayList<Integer>();

    private ArrayList<SubCategory> subproduct,subreport, submaster, subvas, subsetting;

    FragmentHomeMap FM = new FragmentHomeMap();
    String vehiclechangeflag = "";
    GoogleMap mMap;
    RelativeLayout relativelayoutMain, mapLayout;
    CustomMapFragment supportMapFragment;
    int dividerHeight;
    Handler handler;
    View customMarkerView;
    String[] msgToDisplay;
    ImageView profile_Img_View;

    private TravelpulseInfoPref infoPref;
    private AppUpdateManager appUpdateManager;
    private Calendar calendar;
    private List<Target> targets = new ArrayList<>();
    private int spotLightIndex=1;

    @SuppressWarnings("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_main);
        supportMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap));
        supportMapFragment.getMapAsync(this);
        context = this;
        infoPref = new TravelpulseInfoPref(this);
        Utils.getRecordId(infoPref.getString("deviceid", PrefEnum.OneSignal), "IMEI", infoPref.getString("token", PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),infoPref);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Home");

        cc = new CommonClass(context);
        db = new DatabaseClass(context);

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

        type = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        handler = new Handler();
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)){
            Permissions.checkAndRequest(CustomerDashboardActivity.this);
        }

         Intent ii = getIntent();
         menumodel= (ArrayList<MenuModel>) ii.getSerializableExtra("menumodel");
        if (menumodel!=null && menumodel.size() > 0) {

            String query = "insert into permissions(menu_seq,menu_status) values";
            for (int i = 0; i < menumodel.size(); i++) {
                MenuModel menu = menumodel.get(i);
                if (i == menumodel.size() - 1)
                    query += "('" + menu.getPosition() + "','" + menu.getStatus() + "')";
                else
                    query += "('" + menu.getPosition() + "','" + menu.getStatus() + "'),";
            }
            db.insertMenus(context,query);
        }
        else{
            new AlertDialog.Builder(this)
                    .setMessage("Internet Connectivity is Slow Please Try Again")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }

        menumodel= db.getMenuPermission();
        user_type_id =infoPref.getString("user_type_id", PrefEnum.Login);
        id =infoPref.getString("id",PrefEnum.Login);

        customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.svg_icon_layout, null);

        lv = findViewById(R.id.homelistview);

        FirstName = infoPref.getString("first_name", PrefEnum.Login);
        LastName = infoPref.getString("last_name", PrefEnum.Login);
        UserAccessType = infoPref.getString("user_type_id", PrefEnum.Login);

        relativelayoutMain = findViewById(R.id.relativeLayoutMain);
        swipeRefresh = findViewById(R.id.swipe_refresh_layout);
        swipeRefresh.setEnabled(true);
        refreshText = findViewById(R.id.refreshtxtID);
        refreshToast = findViewById(R.id.refreshtoastID);
        refreshText.setTypeface(type);
        refreshText.setText("swipe  down to  refresh ");
        mapLayout = findViewById(R.id.mapid);
        swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        dividerHeight = (int) (getResources().getDimension(R.dimen.listview_divider) / getResources().getDisplayMetrics().density);

        refreshToast.setVisibility(View.VISIBLE);
        refreshToast.setText("Loading alerts please wait...");

        if (!cc.isConnected(context)) {
            showNoInternetMsg();
        }
        else {
            loadList();
        }
        getCatData();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ExpandableListView mDrawerList = findViewById(R.id.list_slidermenu);
        mDrawerList.setGroupIndicator(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();
        mDrawerLayout.setDrawerListener(toggle);

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View header = mInflater.inflate(R.layout.navigationdrawer_header, null);
        font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        shareIcon = findViewById(R.id.vtsShareIcon);
        bellIcon = header.findViewById(R.id.BellIcon);
        bellIcon.setTypeface(type);
        shareIcon.setTypeface(type);
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

        profile_Img_View = header.findViewById(R.id.profile_img_view);
        profile_Img_View.setVisibility(View.GONE);
        mDrawerList.addHeaderView(header);

        mDrawerList.setAdapter(new ExpandableListViewAdapter(CustomerDashboardActivity.this, category_name, subcategory_name, subCatCount));

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
                    startActivity(new Intent(CustomerDashboardActivity.this, RemoteLockActivity.class));
                }
                if(catgroup.getCat_code()==9){
                    startActivity(new Intent(CustomerDashboardActivity.this, FaqActivity.class));
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_troubleshoot_url))));
                }
                if(catgroup.getCat_code()==7){
                    startActivity(new Intent(CustomerDashboardActivity.this, NearByPlacesActivity.class));
                }

                if(catgroup.getCat_code()==11){
                    startActivity(new Intent(CustomerDashboardActivity.this, CustomerQueriesActivity.class));
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
                                i.putExtras(b);
                                startActivity(i);
                                break;
                        }

                        break;
                    case "Reports":
                        SubCategory sbreport = subreport.get(childPosition);
                          switch (sbreport.getSubcat_code()) {
                              case "3.0":
                                  startActivity(new Intent(CustomerDashboardActivity.this, ReportsActivity.class));
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
                                   Intent i = new Intent(CustomerDashboardActivity.this, Activity_Vehicle_Master.class);
                                   startActivity(i);
                                   break;

                               case "4.2":
                                    i = new Intent(CustomerDashboardActivity.this, Activity_Drivers.class);
                                   startActivity(i);
                                   break;

                               case "4.3":
                                    i = new Intent(CustomerDashboardActivity.this, Activity_Vehical_Group.class);
                                   startActivity(i);
                                   break;

                               case "4.4":
                                    i = new Intent(CustomerDashboardActivity.this, Activity_FleetSubUsers.class);
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
                                   intent = new Intent(CustomerDashboardActivity.this, ChangePasswordActivity.class);
                                  startActivity(intent);
                                  break;

                              case "6.3":
                                   intent = new Intent(CustomerDashboardActivity.this, AboutUS.class);
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
        showProfile = findViewById(R.id.OpenProfile);
        showProfile.setVisibility(View.GONE);
        showProfile.setTypeface(type);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        supportMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap));
        supportMapFragment.getMapAsync(this);
        GridView gridview = findViewById(R.id.homegridview);

        gridview.setVisibility(View.GONE);
        mapLayout.setVisibility(View.VISIBLE);
        relativelayoutMain.setVisibility(View.VISIBLE);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cc.isConnected(context)) {
                    loadList();
                }
                else {
                    if (dialogg != null) {
                        dialogg.dismiss();
                    }
                    CommonClass.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
                    lv.setVisibility(View.VISIBLE);
                    swipeRefresh.setEnabled(true);
                    swipeRefresh.setRefreshing(false);
                    msgToDisplay = new String[]{context.getString(R.string.refresh_network_error_message) + ". Please connect to the Internet and swipe down to refresh "};
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.notification_row_layout, R.id.textView1, msgToDisplay);
                    lv.setAdapter(adapter);
                    lv.setDividerHeight(0);
                    refreshToast.setVisibility(View.GONE);
                    refreshText.setVisibility(View.GONE);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefresh.setRefreshing(false);
                            CommonClass.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
                        }
                    }, 3000);
                }
            }
        });

        fnlogout(infoPref.getString("record_id", PrefEnum.OneSignal), infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal),
                infoPref.getString("token", PrefEnum.OneSignal), infoPref.getString("user_type_id", PrefEnum.Login),
                infoPref.getString("id", PrefEnum.Login), "A",false);

        if(!infoPref.getBoolean("is_notification",PrefEnum.Login))
            setNotificationPref();

        //showSpotLight();
    }

    private void showNoInternetMsg() {
        if (dialogg != null) {
            dialogg.dismiss();
        }
        lv.setVisibility(View.VISIBLE);
        msgToDisplay = new String[]{context.getString(R.string.refresh_network_error_message) + ". Please connect to the Internet and swipe down to refresh "};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.notification_row_layout, R.id.textView1, msgToDisplay);
        lv.setAdapter(adapter);
        lv.setDividerHeight(0);
        refreshToast.setVisibility(View.GONE);
        refreshText.setVisibility(View.GONE);
    }


    public void loadList() {
        if (cc.isConnected(context)) {
            user_type_id =infoPref.getString("user_type_id",PrefEnum.Login);
            NotificationList(user_type_id,id,lv, playerId, deviceId);
        }
        else {
            showNoInternetMsg();
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
        final float scale = context.getResources().getDisplayMetrics().density;
        RelativeLayout vtsF = findViewById(R.id.mapid);
        ImageView maxminimg = view.findViewById(R.id.maxminMapIcon);
        if (maxminimg.getTag().equals("min")) {
            if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                vtsF.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (750 * scale + 0.5f)));
            }
            maxminimg.setImageResource(R.drawable.minimize);
            maxminimg.setTag("max");
        } else {
            if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                vtsF.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (350 * scale + 0.5f)));
            }
            maxminimg.setImageResource(R.drawable.maximize);
            maxminimg.setTag("min");
        }
    }


    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

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
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void getCatData() {
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

         /*Category lock = new Category();
        lock.setCat_code(8);
        lock.setCat_name("Remote Lock");
        lock.setImageid(R.drawable.ic_explore);
        category_name.add(lock);
        ArrayList<SubCategory> lockCat = new ArrayList<>();
        subcategory_name.add(lockCat);
        subCatCount.add(lockCat.size());*/

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

        loadList();

        FM.markerPoints = null;
        FM.pointRepeated = false;
        FM.wholeData = null;

        FirstName = infoPref.getString("first_name", PrefEnum.Login);
        LastName = infoPref.getString("last_name", PrefEnum.Login);
        userName.setText(FirstName + " " + LastName);
        profileText_tv.setText(FirstName.toUpperCase().charAt(0) + "" + LastName.toUpperCase().charAt(0)); //added by pankaj

        if (!cc.isConnected(context)) {
            CommonClass.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
            lv.setVisibility(View.VISIBLE);
            swipeRefresh.setEnabled(true);
            swipeRefresh.isRefreshing();
            msgToDisplay = new String[]{context.getString(R.string.refresh_network_error_message) + ". Please connect to the Internet and swipe down to refresh "};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.notification_row_layout, R.id.textView1, msgToDisplay);
            lv.setAdapter(adapter);
            lv.setDividerHeight(0);
            refreshToast.setVisibility(View.GONE);
            refreshText.setVisibility(View.GONE);
        }
        vehiclechangeflag = infoPref.getString("vehiclechangeflag", PrefEnum.App);
        if (vehiclechangeflag.equals("vehicle_changed")) {
            supportMapFragment.getMapAsync(this);
            if(mMap!=null) {
                mMap.clear();
            }
            FM.markerPoints = null;
            infoPref.remove("vehiclechangeflag",PrefEnum.App);
        }
        if(mMap!=null) {
            mMap.clear();
        }
        infoPref.putString("livetrack","y",PrefEnum.Login);
        supportMapFragment.getMapAsync(this);
        FM.startTimer(supportMapFragment, context);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) { //replace this with actual function which returns if the drawer is open
            mDrawerLayout.closeDrawer(GravityCompat.START);     // replace this with actual function which closes drawer
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

    public void FuelandTireRepairCommon(String ServiceCode, String networkcall) {
        Bundle bundle = new Bundle();
        bundle.putString("serviceCode", ServiceCode);
        Intent i = new Intent(context, ProviderListActivity.class);
        i.putExtras(bundle);
        context.startActivity(i);
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
        final int scale= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        googleMap.setPadding(0, 0, 0, scale);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        infoPref.putString("livetrack","y",PrefEnum.Login);
        loadList();
        FM.Index(context, supportMapFragment, customMarkerView);
    }

    public void NotificationList(String usertype_id, final String user_id, final ListView lv,final String p_id,final String d_id ) {
            Call<NotificationListData> call = RepositryInstance.getNotificationRepository().getNotifications(usertype_id, user_id,p_id,d_id);
            call.enqueue(new Callback<NotificationListData>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<NotificationListData> call, Response<NotificationListData> response) {
                    if (response.isSuccessful()){
                        swipeRefresh.setRefreshing(false);

                        if (response.body().getStatus().equalsIgnoreCase("success")){
                            listnew.clear();
                            notificationListdata = response.body();
                            if (notificationListdata!=null && notificationListdata.getData()!=null) {
                                listnew.addAll(notificationListdata.getData());
                                adapter = new NotificationListAdapterMain(context, listnew);
                                lv.setDividerHeight(0);
                                lv.setAdapter(adapter);
                                refreshToast.setVisibility(View.GONE);
                            }else {
                                refreshToast.setText("No Notification Found");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationListData> call, Throwable t) {

                }
            });
    }


    private void fnlogout(String strdeviveid,String pid,String token,String strUserName, final String strusertypeid,String strStatus, boolean isLogout) {
        if (isLogout) {
            dialogg = new ProgressDialog(context);
            dialogg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogg.setCancelable(false);
            dialogg.setMessage("Please Wait...");
            dialogg.show();
        }
            Call<ResponseBody> call= RepositryInstance.getNotificationRepository().changeUserStatus(/*auth,apiKey,*/strdeviveid,pid,token,strUserName,strusertypeid,strStatus);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        infoPref.putBoolean("is_notification",false,PrefEnum.Login);
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("","Responce code"+strResponceCode);
                        if(dialogg!=null){
                            dialogg.dismiss();
                        }
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);
                                String strStatus = myObject.getString("status");
                                if (isLogout) {
                                    infoPref.remove("vid", PrefEnum.Login);
                                    infoPref.remove("vidgeo", PrefEnum.Login);
                                    infoPref.remove("livetrack", PrefEnum.Login);
                                    infoPref.putString("logout", "y", PrefEnum.Login);
                                    if (strStatus.equalsIgnoreCase("success")) {
                                        db.deletedata();
                                        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context, LoginActivity.class);
                                        startActivity(i);
                                    }
                                    finish();
                                }
                                if(strStatus.equalsIgnoreCase("failure")){
                                    String msg = myObject.getString("Msg");
                                    if(msg.equalsIgnoreCase("device_not_exists")) {
                                        infoPref.getLoginEditor().clear().commit();
                                    }
                                    if(msg.equalsIgnoreCase("user_not_exists")){
                                        infoPref.getLoginEditor().clear().commit();
                                    }
                                    startActivity(new Intent(CustomerDashboardActivity.this, LoginActivity.class));
                                }

                                break;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
    }

    public  void getvalue(){
        if (TextUtils.isEmpty(infoPref.getString("deviceiid",PrefEnum.OneSignal)))
            deviceiid = CommonClass.getDevice_Id(this);
        else
            deviceiid = infoPref.getString("deviceiid",PrefEnum.OneSignal);
        playerId =infoPref.getString("GT_PLAYER_ID", PrefEnum.OneSignal);
        token=infoPref.getString("token", PrefEnum.OneSignal);
        deviceId =infoPref.getString("record_id", PrefEnum.OneSignal);
        }

    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() && stateChanges.getTo().getSubscribed()) {
            stateChanges.getTo().getUserId();
            infoPref.putString("token",  stateChanges.getTo().getPushToken(),PrefEnum.OneSignal);
            infoPref.putString("GT_PLAYER_ID", stateChanges.getTo().getUserId(),PrefEnum.OneSignal);
            getvalue();
        }
    }
    
    private void setNotificationPref(){
        dialogg = new ProgressDialog(context);
        dialogg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogg.setCancelable(false);
        dialogg.setMessage("Please Wait...");
        dialogg.show();
        Call<ResponseBody> call=RepositryInstance.getNotificationRepository().setNotificationPrefOnLogin(user_type_id, playerId, deviceId,id);
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

    private void checkUpdate(){
        calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(final AppUpdateInfo result) {
                if (result.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    infoPref.putLong(infoPref.APP_UPDATE_TIME,calendar.getTimeInMillis(),PrefEnum.Login);
                    try {
                        appUpdateManager.registerListener(CustomerDashboardActivity.this);
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE, CustomerDashboardActivity.this, 2000);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }else if (result.installStatus() == InstallStatus.DOWNLOADED){
                    Log.d(TAG, "onSuccess: 666");
                    showUpdateSnackbar();
                }else {
                    Log.d(TAG, "onSuccess: 555");
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
                if(mMap!=null){
                    mMap.clear();
                }
                FM.markerPoints = null;
                FM.pointRepeated = false;
                FM.wholeData = null;

                infoPref.putString("livetrack","y",PrefEnum.Login);
                FM.Index(context, supportMapFragment, customMarkerView);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appUpdateManager.unregisterListener(this);
    }
}
