package com.trimax.vts.view.notifications;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.model.AlertDetails;
import com.trimax.vts.model.AlertsDeleteRequestModel;
import com.trimax.vts.model.CheckedItemModel;
import com.trimax.vts.model.FilterAlertRequestModel;
import com.trimax.vts.model.notifications.Datum;
import com.trimax.vts.model.notifications.NotificationListData;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import com.trimax.vts.view.dashboard.CustomerDashboardActivity;
import com.trimax.vts.view.maps.ReplayTrackingActivity;
import com.trimax.vts.view.notifications.adapter.NotificationListAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class NotificationActivity extends AppCompatActivity {
    Context context;
    ListView listview;
    SharedPreferences sharedpreference,sharedpreferencenew,sharedpreferenceonsignal;
    String  user_type_id="",id="",VehicleValue="";

    NotificationListData notificationListdata;
    ArrayList<AlertDetails> list;
    ArrayList<AlertDetails> list2;
    ArrayList<Datum> listnew = new ArrayList<>();
    TextView refreshToast, refreshBtn;
    String[] selectedAlertTypesVal;
    Integer[] selectedvehicleArray;
    Typeface fontawesome;
    CommonClass cc;
    static Integer d;
    Runnable r;
    boolean loadingMore = false;
    NotificationListAdapter adapter;
    ArrayList<String> list_items = new ArrayList<String>();
    ArrayList<Integer> list_items1 = new ArrayList<Integer>();
    ArrayList<String> filterObject;
    ArrayList<String> selectedList;
    String filterDate;
    String Ntype="",Vehicle_id="";
    String Device_id="",Player_id="";
    String userType = "";
    int count = 0, countNotificationWs = 0;
    String flag = "";
    String valueGson;
    int position = 10;
    int notification_row_count = 50;
    ProgressDialog dialog;
    boolean setNewAlertReceived = false;
    Timer timer;
    Integer alertId;
    SwipeRefreshLayout swiperefresh;
    int[] isselectAllCheck;
    static int dividerHeight;
    ArrayList<CheckedItemModel> checkedObject;
    NotificationManager notifManager;
    TextView dateNotifyerTv;
    // variables for checking dateNotyfire animations --start here
    boolean isListScrolling = false;
    int lastAnimation = -1;
    private static final int SHOW_ANIMATION = 1;  // to prevent running same animation
    private static final int HIDE_ANIMATION = 0;  // back to back

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        context = this;
        cc = new CommonClass(context);

        fontawesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        filterObject = new ArrayList<String>();
        Intent i = getIntent();
        flag = i.getStringExtra("flag");
        notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        listview = (ListView) findViewById(R.id.NotificationList);

        refreshToast = (TextView) findViewById(R.id.refreshtoastID);
        refreshBtn = (TextView) findViewById(R.id.refreshNtxtID);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swiperefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        dividerHeight = (int) (getResources().getDimension(R.dimen.listview_divider) / getResources().getDisplayMetrics().density);
        refreshBtn.setTypeface(fontawesome);
        refreshBtn.setText("  Swipe down to refresh");
        sharedpreference = context.getSharedPreferences(Constants.app_preference, context.MODE_PRIVATE);
        sharedpreferencenew = context.getSharedPreferences(Constants.app_preference_login,
                Context.MODE_PRIVATE);
        sharedpreferenceonsignal=context.getSharedPreferences(Constants.app_preference_OneSignal,
                Context.MODE_PRIVATE);
        user_type_id =sharedpreferencenew.getString("user_type_id","");
        id =sharedpreferencenew.getString("id","");
        Player_id=sharedpreferenceonsignal.getString("GT_PLAYER_ID","");
        Device_id=sharedpreferenceonsignal.getString("record_id","");
        userType = sharedpreference.getString("UserType", "");
        selectedAlertTypesVal = null;
        d = sharedpreference.getInt("id", 0);
        FilterAlertRequestModel filterAlertRequestModel = new FilterAlertRequestModel();
        filterAlertRequestModel.setUser_id(id);
        filterAlertRequestModel.setUser_type_id(user_type_id);
        filterAlertRequestModel.setVehicle_id("");
        filterAlertRequestModel.setNotification_type_id("");
        filterAlertRequestModel.setLimit(String.valueOf(notification_row_count));
        filterAlertRequestModel.setStart("0");

        Gson gsonEW = new Gson();
        valueGson = gsonEW.toJson(filterAlertRequestModel);

        if (cc.isConnected(context)) {

            listview.setVisibility(View.VISIBLE);
            refreshToast.setVisibility(View.GONE);
            NotificationList(user_type_id,id,"","","50","0",listview,"reload",Player_id,Device_id);
            swiperefresh.setEnabled(true);

        } else {
            Toast.makeText(context, R.string.network_error_message, Toast.LENGTH_SHORT).show();
            listview.setVisibility(View.VISIBLE);
            String[] msgToDisplay = new String[]{context.getString(R.string.refresh_network_error_message) + ". Please connect to the Internet and swipe down to refresh "};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context,
                    R.layout.notification_row_layout, R.id.textView1, msgToDisplay);
            listview.setAdapter(adapter1);
            listview.setDividerHeight(0);
        }
        countNotificationWs = 0;


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swiperefresh.setEnabled(true);
                NotificationList(user_type_id,id,"","","50","0",listview,"reload",Player_id,Device_id);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                    }
                }, 3000);

            }
        });


        dateNotifyerTv = (TextView) findViewById(R.id.notifyer_dateTv);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ReplayTrackingActivity.class);
                intent.putExtra("noti_vid",listnew.get(position).getVehicle_id());
                intent.putExtra("noti_lat",listnew.get(position).getVehicle_lat());
                intent.putExtra("noti_lang",listnew.get(position).getVehicleLong());
                intent.putExtra("noti_msg",listnew.get(position).getMsg());
                intent.putExtra("noti_address",listnew.get(position).getLocation());
                intent.putExtra("noti_datetime",listnew.get(position).getDateTime());
                intent.putExtra("noti_speed",listnew.get(position).getSpeed());
                intent.putExtra("noti_ign",listnew.get(position).getIgn());
                intent.putExtra("noti_ac",listnew.get(position).getAc());
                intent.putExtra("replay_notification", "y");
                intent.putExtra("flag", "");
                intent.putExtra("alertId", "");
                intent.putExtra("callFromMain", "");
                intent.putExtra("alrm", "");
                intent.putExtra("callFromMain", "");
                intent.putExtra("alrm_vid", "");
                context.startActivity(intent);


            }

        });

        listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE_MODAL);
        listview.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub

                //listview.clearChoices();
                count = 0;
                loadingMore = false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode args0, Menu args1) {
                MenuInflater inflator = args0.getMenuInflater();
                inflator.inflate(R.menu.context_menu, args1);
                loadingMore = true;
                list_items = new ArrayList<String>();
                list_items1 = new ArrayList<Integer>();
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode args0, MenuItem args1) {
                switch (args1.getItemId()) {
                    case R.id.delete_id_list:
                        new AlertDialog.Builder(context)
                                .setTitle("Delete Confirmation!")
                                .setMessage("Do you really want to delete " + count + " alerts?")
                                .setIconAttribute(android.R.attr.alertDialogIcon)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface _dialog, int whichButton) {
                                        _dialog.cancel();
                                        Integer[] delAlerts = list_items1.toArray(new Integer[list_items1.size()]);
                                        AlertsDeleteRequestModel deleteAlertRequestModel = new AlertsDeleteRequestModel();
                                        deleteAlertRequestModel.setDeleteAll(false);
                                        deleteAlertRequestModel.setUserId(d);
                                        deleteAlertRequestModel.setAlertIds(delAlerts);


                                        countNotificationWs = 0;
                                        count = 0;
                                        args0.finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface _dialog, int whichButton) {
                                        _dialog.cancel();
                                    }
                                }).show();
                        /*for (String msg : list_items) {
                            adapter.remove(msg);
						}
						count = 0;
						args0.finish();*/
                        return true;

                    default:
                        return false;

                }
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                if (checked) {
                    count = count + 1;
                    mode.setTitle(count + " ");
                    //String a=
                    list_items.add(listview.getItemAtPosition(position).toString());
                    list_items1.add(list.get(position).getId());
                } else {
                    count = count - 1;
                    mode.setTitle(count + " ");
                }


            }
        });


        listview.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && lastAnimation != -1) {
                    isListScrolling = false;
                    isNotScrollingForAwhile();
                    return;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                final int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(loadingMore) && visibleItemCount != 0 && lastInScreen > 10) {

                    try {
                        //call="second";
                        alertId = list.get(list.size() - 1).getId();
                    } catch (Exception e) {
                    }
                    //  startTimer();
                    if (!loadingMore) {
                        loadingMore = true;

                        FilterAlertRequestModel filterAlertRequestModel = new FilterAlertRequestModel();
                        filterAlertRequestModel.setStartIndex(lastInScreen);
                        filterAlertRequestModel.setEndIndex(notification_row_count);
                        filterAlertRequestModel.setUserId(d);
                        filterAlertRequestModel.setAlertTypes(selectedAlertTypesVal);
                        filterAlertRequestModel.setVehicleId(selectedvehicleArray);
                        filterAlertRequestModel.setAlertDate(filterDate);
                        if (cc.isConnected(context)) {
                            NotificationList(user_type_id,id,"","","50","0",listview,"reload",Player_id,Device_id);

                            //a.execute(valueEW);
                            lastAnimation = -1;
                            dateNotifyerTv.setVisibility(View.INVISIBLE);
                        } else {
                            cc.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
                        }

                    }
                } else {
                }


                //added by pankaj --- for dateNotifier animation
                isListScrolling = true;
                if (list != null && list.size() > 0 && view.getChildAt(0).getY()<0) {
                    final float approxVisibleHeightPercentage = -0.60f;

                    if (view.getChildAt(0).getY() > view.getChildAt(0).getHeight() * approxVisibleHeightPercentage) {
                        setDateNotyfire(firstVisibleItem);
                    } else if (firstVisibleItem + 1 < list.size() - 1) {
                        setDateNotyfire(firstVisibleItem + 1);
                    } else {
                        setDateNotyfire(firstVisibleItem);
                    }
                    if (!dateNotifyerTv.isShown()
                            && (transYHideAnim.hasStarted() ? transYHideAnim.hasEnded() : true)
                            && lastAnimation != SHOW_ANIMATION) {
                        lastAnimation = SHOW_ANIMATION;
                        dateNotifyerTv.setVisibility(View.VISIBLE);
                        verticalTranslateAnimation(transYShowAnim, dateNotifyerTv); //from top to bottomanimation
                    }
                } //end here ----
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_notification_actions, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        final int _userId = sharedpreference.getInt("id", 0);

        if (id == R.id.action_newalert) {
            FilterAlertRequestModel filterAlertRequestModel = new FilterAlertRequestModel();
            filterAlertRequestModel.setStartIndex(0);
            filterAlertRequestModel.setEndIndex(notification_row_count);
            filterAlertRequestModel.setUserId(d);
            filterAlertRequestModel.setAlertTypes(selectedAlertTypesVal);
            filterAlertRequestModel.setVehicleId(selectedvehicleArray);
            return true;
        } else if (id == R.id.fil_id) {

            Intent intent = new Intent(context, FilterDynamicNew.class);
            intent.putExtra("gson", "");
            intent.putExtra("selectedFilter", selectedList);
            intent.putExtra("selectAll",isselectAllCheck);
            intent.putExtra("checkedObject",checkedObject);
            intent.putExtra("filterDate",filterDate);

            startActivityForResult(intent, 3);
            overridePendingTransition(R.anim.left_in_anim, R.anim.left_out_anim);

        } else if (id == R.id.delete_all_icon) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Confirmation")
                    .setMessage("Do you really want to delete all alerts?")
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface _dialog, int whichButton) {
                            _dialog.cancel();
                            AlertsDeleteRequestModel deleteAlertRequestModel = new AlertsDeleteRequestModel();
                            deleteAlertRequestModel.setDeleteAll(true);
                            deleteAlertRequestModel.setUserId(d);
                            deleteAlertRequestModel.setAlertIds(null);

                            countNotificationWs = 0;
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface _dialog, int whichButton) {
                            _dialog.cancel();
                        }
                    }).show();
        }else if(id == R.id.read_all_id) {
            new AlertDialog.Builder(context)
                    .setTitle("Read Notifications")
                    .setMessage(R.string.readAllAlerts)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertsDeleteRequestModel deleteAlertRequestModel = new AlertsDeleteRequestModel();
                            deleteAlertRequestModel.setDeleteAll(true);
                            deleteAlertRequestModel.setUserId(d);
                            deleteAlertRequestModel.setAlertIds(null);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        } else {
            onBackPressed();

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        count = 0;
        if (flag != null) {
            if (flag.equals("StackNotification")) {
                Intent i = new Intent(context, CustomerDashboardActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            return;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (resultCode == RESULT_OK)

            {
                if (requestCode == 3) {
                    listnew.clear();
                    Ntype = data.getStringExtra("ntype");
                    Vehicle_id = data.getStringExtra("vehicleid");

                    if (cc.isConnected(context)) {
                        NotificationList(user_type_id,id,Vehicle_id,Ntype,"50","0",listview,"reload",Player_id,Device_id);
                    } else {
                        cc.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        context.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
        dateNotifyerTv.setVisibility(View.INVISIBLE);
        lastAnimation = -1;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
        //context.unregisterReceiver(receiver);
        context.unregisterReceiver(mMessageReceiver);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();

        }

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences shrf = context.getSharedPreferences(Constants.app_preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shrf.edit();
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            //Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            String newMessage = "[" + message + "]";
            Type listType = new TypeToken<List<AlertDetails>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            list2 = gson.fromJson(newMessage, listType);
            String lastBatchId = shrf.getString("batchid", "");
            if (!lastBatchId.equals(list2.get(0).getRandomCounter())) {
                setNewAlertReceived = true;
                invalidateOptionsMenu();
            }

            editor.putString("batchid", list2.get(0).getRandomCounter());
            editor.commit();
            //do other stuff here
        }
    };

    private void isNotScrollingForAwhile() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isListScrolling && lastAnimation != HIDE_ANIMATION) {
                    lastAnimation = HIDE_ANIMATION;
//                    verticalTranslateAnimation(dateNotifyerTv, 0, -100); //from bottom to top animation
                    verticalTranslateAnimation(transYHideAnim, dateNotifyerTv); //from top to bottomanimation
                    dateNotifyerTv.setVisibility(View.INVISIBLE);
                } else {
                    isNotScrollingForAwhile();
                }
            }
        }, 2000);
    }

    private void setDateNotyfire(int position) {
        try {

            String days = list.get(position).getTimeStamp();
            if (days.matches(".*\\bdays\\b.*")) {
                days = days.replaceAll("\\D+", "");
                String dateFormat = "dd MMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -(Integer.parseInt(days)));
                dateNotifyerTv.setText(simpleDateFormat.format(cal.getTime()));
            } else if (days.matches(".*\\bday\\b.*")) {
                dateNotifyerTv.setText("Yesterday");
            } else {
                dateNotifyerTv.setText("Today");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            dateNotifyerTv.setVisibility(View.INVISIBLE);
            lastAnimation = -1;
        }

    }

    private TranslateAnimation transYShowAnim = new TranslateAnimation(0, 0, -100, 0);
    private TranslateAnimation transYHideAnim = new TranslateAnimation(0, 0, 0, -100);

    private void verticalTranslateAnimation(TranslateAnimation translateAnimation, View view) {
        translateAnimation.setDuration(300);
        view.setAnimation(translateAnimation);
        translateAnimation.start();
    }


    public void NotificationList(String usertype_id, final String user_id, final String vehicle_id, final String notification_type, final String limit , final String strat, final ListView list, final String relaod,final String p_id,String d_id) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        try {

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = RepositryInstance.getNotificationRepository().getAllNotifications(usertype_id, user_id,vehicle_id, notification_type,limit,strat,p_id,d_id);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("", "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                JSONObject myObject = new JSONObject(strResponse);


                                String strStatus = myObject.getString("status");

                                if (strStatus.trim().equals("success")) {
                                    try {

                                        if (strStatus != null) {
                                            // Toast.makeText(ctx,"onresponse "+strResponse,Toast.LENGTH_SHORT).show();
                                            //googleMap.setPadding(0, 0, 0, 80);

                                            sharedpreferencenew.edit().remove("selectone").commit();
                                            sharedpreferencenew.edit().remove("selectall").commit();
                                            GsonBuilder builder = new GsonBuilder();
                                            Gson gson = builder.create();
                                            notificationListdata = gson.fromJson(strResponse, NotificationListData.class);

                                            listnew.addAll(notificationListdata.getData());

                                            adapter = new NotificationListAdapter(context, listnew);
                                            list.setAdapter(adapter);


                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(context,"No Notification Found",Toast.LENGTH_LONG).show();

                                    listnew.clear();
                                    adapter.notifyDataSetChanged();
                                    sharedpreferencenew.edit().remove("selectone").commit();
                                    sharedpreferencenew.edit().remove("selectall").commit();
                                    Toast.makeText(context,"No Notification Found",Toast.LENGTH_LONG).show();

                                }
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

}

