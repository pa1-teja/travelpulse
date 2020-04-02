package com.trimax.vts.view.notifications;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.Constant;
import com.trimax.vts.interfaces.PositionClickListener;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;

import com.trimax.vts.model.NotificationPojo;
import com.trimax.vts.model.VehiclePojo;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.notifications.adapter.NotificationFilterAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class FilterDynamicNew extends AppCompatActivity implements  PositionClickListener {
    private static final String TAG = "FilterDynamicNew";
    private Context context;
    String selectedoption = "";
    CommonClass cc;

    ListView mainCategory_lv;
    RecyclerView recyclerView_vehicle;
    SharedPreferences sharedpreference, sharedpreferencenew;
    int vehicle_id = 0;
    boolean CheckVehicle = true;
    boolean CheckVehicleType = false;
    CheckBox selectAllCb, selectAllCbtype;
    Button clearBtn, applyBtn;
    String user_type_id = "";

    ArrayList<NotificationPojo> arrayList_type;
    String id = "", record_id="";
    private NotificationFilterAdapter generateconnectionreporttwo;
    private NotificationFilterType generateconnectionreporttwotype;

    ProgressDialog progress;

    PositionClickListener positionClickListener;
    Set<Integer> hashSet = new HashSet<Integer>();
    private TravelpulseInfoPref infoPref;

    boolean check_listview=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_dynamic);
        infoPref = new TravelpulseInfoPref(this);
        record_id = infoPref.getString("record_id", PrefEnum.OneSignal);
        positionClickListener = (PositionClickListener) this;
        Constant.selectedStrings.clear();
        Constant.selectedStringstype.clear();
        arrayList_type = new ArrayList<>();

        context = this;
        cc = new CommonClass(context);
        getSupportActionBar().setTitle("Filter");
        mainCategory_lv = (ListView) findViewById(R.id.main_filterCategory_lv);
        recyclerView_vehicle = (RecyclerView) findViewById(R.id.sub_filterCategory_lv);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        applyBtn = (Button) findViewById(R.id.applyBtn);
        selectAllCb = (CheckBox) findViewById(R.id.selectAll_cb);
        selectAllCbtype = (CheckBox) findViewById(R.id.selectAlltype_cb);
        sharedpreferencenew = context.getSharedPreferences(Constants.app_preference_login,
                Context.MODE_PRIVATE);
        user_type_id = sharedpreferencenew.getString("user_type_id", "");
        id = sharedpreferencenew.getString("id", "");
        sharedpreferencenew.edit().remove("selectall").commit();
        sharedpreferencenew.edit().remove("selectalltype").commit();
        sharedpreferencenew.edit().remove("ntype").commit();
        sharedpreferencenew.edit().remove("vehicleid").commit();
        sharedpreferencenew.edit().remove("selectone").commit();
        sharedpreferencenew.edit().remove("selectall").commit();
        sharedpreferencenew.edit().remove("selectonetype").commit();

        Constant.arrayList_vehicle.clear();
        final List<String> fliterlist = new ArrayList<String>();
        fliterlist.add("Vehicle");
        fliterlist.add("Tracker");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                fliterlist);

        mainCategory_lv.setAdapter(arrayAdapter);
        selectAllCb.setVisibility(View.VISIBLE);
        selectAllCbtype.setVisibility(View.GONE);

        if (Constant.vehicleflag==true){
            selectAllCb.setChecked(true);
        }

        if (Constant.trackerflag==true){
            selectAllCbtype.setChecked(true);
        }

        current_vehicle_status_dashboard(user_type_id, id);

        mainCategory_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                selectedoption = fliterlist.get(position);
                if (selectedoption.equalsIgnoreCase("Vehicle")) {
                    Constant.arrayList_vehicle.clear();
                    arrayList_type.clear();
                    CheckVehicle=true;
                    CheckVehicleType=false;
                    selectAllCb.setVisibility(View.VISIBLE);
                    selectAllCbtype.setVisibility(View.GONE);
                    current_vehicle_status_dashboard(user_type_id, id);
                    check_listview=false;
                } else {
                    check_listview=true;
                    Constant.arrayList_vehicle.clear();
                    arrayList_type.clear();
                    selectAllCb.setVisibility(View.GONE);
                    selectAllCbtype.setVisibility(View.VISIBLE);
                    CheckVehicleType=true;
                    CheckVehicle=false;
                    current_vehicle_status_dashboard_type(user_type_id, id);
                }
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("send", "");
                if (sharedpreferencenew.getString("selectall", "").equalsIgnoreCase("")) {
                    intent.putExtra("vehicleid", sharedpreferencenew.getString("selectone", ""));
                } else {
                    intent.putExtra("vehicleid", sharedpreferencenew.getString("selectall", ""));
                }
                if (sharedpreferencenew.getString("selectalltype", "").equalsIgnoreCase("")) {
                    intent.putExtra("ntype", sharedpreferencenew.getString("selectonetype", ""));
                } else {
                    intent.putExtra("ntype", sharedpreferencenew.getString("selectalltype", ""));
                }

                intent.putExtra("checkedObject", "");
                intent.putExtra("filterDate", "");
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.right_in_anim, R.anim.right_out_anim);
            }
        });


        selectAllCbtype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constant.arraylist_position_t.clear();
                if (isChecked) {
                    Constant.trackerflag=true;
                    Constant.check1=true;
                    int size = arrayList_type.size();
                    for (int i = 0; i < size; i++) {
                        NotificationPojo dto = arrayList_type.get(i);
                        dto.setSelected(true);
                        Constant.arraylist_position_t.add(i);
                    }
                    generateconnectionreporttwotype.selectAll(true);
                    generateconnectionreporttwotype.notifyDataSetChanged();
                } else {
                    Constant.trackerflag=false;
                    Constant.check1=false;
                    int size = arrayList_type.size();
                    for (int i = 0; i < size; i++) {
                        NotificationPojo dto = arrayList_type.get(i);
                        dto.setSelected(false);

                        for (Integer assurance : Constant.arraylist_position_t) {
                            if (assurance.equals(i))  {
                                Constant.arraylist_position_t.remove(assurance);
                                //You can exit the loop as you find a reference
                                break;
                            }
                        }
                    }
                    generateconnectionreporttwotype.selectAll(false);
                    generateconnectionreporttwotype.notifyDataSetChanged();
                }

            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAllCb.setChecked(false);
                selectAllCbtype.setChecked(false);
                 Constant.arrayList_vehicle.clear();
                 arrayList_type.clear();
                 Constant.arraylist_position_t.clear();
                 Constant.arraylist_position.clear();
                if(CheckVehicle){
                    current_vehicle_status_dashboard(user_type_id, id);
                }
                else if(CheckVehicleType){
                    current_vehicle_status_dashboard_type(user_type_id, id);
                }
                 Toast.makeText(context, "All filter data are cleared", Toast.LENGTH_SHORT).show();
            }
        });

        selectAllCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Constant.arraylist_position.clear();
                if (isChecked == true) {

                    Constant.vehicleflag=true;
                    int size = Constant.arrayList_vehicle.size();
                    for (int i = 0; i < size; i++) {
                        VehiclePojo dto = Constant.arrayList_vehicle.get(i);
                        dto.setSelected(true);
                        Constant.arraylist_position.add(i);
                    }
                    generateconnectionreporttwo.selectAll(true);
                    generateconnectionreporttwo.notifyDataSetChanged();
                    Constant.check2=true;
                } else {
                    Constant.vehicleflag=false;
                    int size = Constant.arrayList_vehicle.size();
                    for (int i = 0; i < size; i++) {
                        VehiclePojo dto = Constant.arrayList_vehicle.get(i);
                        dto.setSelected(false);
                        for (Integer assurance : Constant.arraylist_position) {
                            if (assurance.equals(i))  {
                                Constant.arraylist_position.remove(assurance);
                                //You can exit the loop as you find a reference
                                break;
                            }
                        }
                    }
                    generateconnectionreporttwo.selectAll(false);
                    generateconnectionreporttwo.notifyDataSetChanged();

                    Constant.check2=false;

                }
            }
        });


    }

    public void current_vehicle_status_dashboard(String usertype_id, final String user_id) {
            progress = new ProgressDialog(FilterDynamicNew.this);
            progress.setMessage("Loading Please wait");
            progress.show();

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.current_vehicle_details(auth, apiKey, usertype_id, user_id,record_id);
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
                                    JSONObject jsonObject = new JSONObject(strResponse);
                                    String status = jsonObject.getString("status");

                                    if (status.equalsIgnoreCase("success")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                                        for (int i = 0; i <= jsonArray.length(); i++) {
                                            VehiclePojo vehiclePojo = new VehiclePojo();

                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String vehicle_id = jsonObject1.getString("vehicle_id");
                                            String vehicle_no = jsonObject1.getString("vehicle_no");
                                            if (Constant.arraylist_position.contains(i)) {
                                                vehiclePojo.setSelected(true);
                                            } else {
                                                vehiclePojo.setSelected(false);
                                            }
                                            vehiclePojo.setVehicle_id(vehicle_id);
                                            vehiclePojo.setVehicle_no(vehicle_no);
                                            Constant.arrayList_vehicle.add(vehiclePojo);
                                            Log.e("size_ofarraylist", String.valueOf(Constant.arrayList_vehicle.size()));
                                            generateconnectionreporttwo = new NotificationFilterAdapter(context, Constant.arrayList_vehicle, positionClickListener);
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                            recyclerView_vehicle.setLayoutManager(mLayoutManager);
                                            recyclerView_vehicle.setAdapter(generateconnectionreporttwo);

                                            progress.dismiss();
                                        }
                                    }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getStackTrace());
                }
            });
    }


    public void current_vehicle_status_dashboard_type(String usertype_id, final String user_id) {

            progress = new ProgressDialog(FilterDynamicNew.this);
            progress.setMessage("Loading Please wait");
            progress.show();

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.getNotificationType(auth, apiKey, usertype_id, user_id);
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
                                progress.dismiss();
                                    if (strResponse != null) {
                                            JSONObject jsonObject = new JSONObject(strResponse);
                                            String status = jsonObject.getString("status");

                                            if (status.equalsIgnoreCase("success")) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    NotificationPojo notificatiobPojo = new NotificationPojo();

                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    String title = jsonObject1.getString("title");
                                                    String notification_id = jsonObject1.getString("id");

                                                    if (Constant.arraylist_position_t.contains(i)) {
                                                        notificatiobPojo.setSelected(true);
                                                    } else {
                                                        notificatiobPojo.setSelected(false);
                                                    }

                                                    notificatiobPojo.setTitle(title);
                                                    notificatiobPojo.setNotification_id(notification_id);
                                                    arrayList_type.add(notificatiobPojo);
                                                    progress.dismiss();
                                                    generateconnectionreporttwotype = new NotificationFilterType(context, arrayList_type,positionClickListener);
                                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                                    recyclerView_vehicle.setLayoutManager(mLayoutManager);
                                                    recyclerView_vehicle.setAdapter(generateconnectionreporttwotype);
                                                }
                                            }
                                    }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                }
            });

    }

    @Override
    public void itemClicked(int id,ArrayList<Integer> arrayList,Boolean flag) {

        if (id==1){
           Constant.arraylist_position=arrayList;
        }else {
            Constant.arraylist_position=arrayList;
        }
        hashSet.addAll(Constant.arraylist_position);
    }


    @Override
    public void itemClickedType(int id, ArrayList<Integer> arraylist_position_type) {

        if (id==1){
            Constant.arraylist_position_t=arraylist_position_type;
        }else {
            Constant.arraylist_position_t=arraylist_position_type;
        }
    }

    @Override
    public void itemselectAll(boolean value) {

    }
}
