package com.trimax.vts.view.reports.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.Constant;
import com.trimax.vts.interfaces.ClickListner;
import com.trimax.vts.interfaces.PositionClickListener;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.reports.Adapter.AlertTypeAdapter;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import com.trimax.vts.view.reports.Callback.ReportCallback;
import com.trimax.vts.view.reports.DialogFragment.ReportCustomDialog;
import com.trimax.vts.model.NotificationPojo;
import com.trimax.vts.model.VehiclePojo;
import com.trimax.vts.model.VehicleViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class VehicleSummaryReportActivityNew extends AppCompatActivity implements PositionClickListener,
        ClickListner, ReportCallback {

    Context context;
    TextView FromDate, ToDate;
    Calendar backdatecalendar;
    Calendar backOnedatecalendar;

    TextView clnFrmDate, xvehiclegroup, clnToDate, xvehicl, xalert;
    EditText FromDatetxt, ToDatetxt, vehiclegroupValue, vehicleValue, alertValue;
    Button viewReport;
    Calendar selectedDate ;
    String user_type_id = "";
    String  VehicleGroup = "";
    PositionClickListener positionClickListener;
    ClickListner cl;
    String id = "",record_id="";
    TextInputLayout frmDateLay, frmtimeLay, toDateLay, toTimeLay;

    SharedPreferences sharedpreference, sharedpreferencenew;
    ArrayList<NotificationPojo> arrayList_type;
    ArrayList<VehiclePojo> arrayList_vehicle;
    ArrayList<VehicleViewGroup> arrayList_vehicle_group;

    String FromDateString, ToDateString, NewFromDateString, NewToDateString;
    private MultiAutoCompleteTextView  autoTextViewCustomthree;

    int VehicleGroupCount = 0, VehicleCount = 0, AlertCount = 0;

    CommonClass cc;
    String day1, year1, month1, Currentdate, fromDate;
    Typeface font_awesome;
    String indicatorVal = "", alert;
    ProgressBar progressbar_login;
    String strDepartureDate = "", strReturnDate = "";
    EditText edtDepDate, edtReturnDate, edtvehiclegroupp;
    private AlertTypeAdapter generateconnectionreporttwotype;

    private HashMap<String, Boolean> vehicleGroupHashmap;
    private HashMap<String, Boolean> vehiclesHashmap;
    private HashMap<String, Boolean> alertHashmap;
    private String TAG = VehicleSummaryReportActivityNew.class.getSimpleName();
    private ReportCustomDialog vehicleGroupDialog, vehiclesDialog, alertDialog;
    private TravelpulseInfoPref infoPref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_summary_report_new);
        context = this;
        infoPref = new TravelpulseInfoPref(this);
        record_id = infoPref.getString("record_id", PrefEnum.OneSignal);
        clearStaticDataFromConstants();

        initHashmaps();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.road_pulse_action_bar_color)));
        sharedpreference = getSharedPreferences(Constants.app_preference,
                Context.MODE_PRIVATE);
        cc = new CommonClass(context);
        font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        progressbar_login = (ProgressBar) findViewById(R.id.progressbar_login);
        /*toDateLay = (RelativeLayout) findViewById(R.id.toDateLayID);*/
        viewReport = (Button) findViewById(R.id.viewReportBtn);
        vehiclegroupValue = (EditText) findViewById(R.id.vehiclegroupValue);
        vehicleValue = (EditText) findViewById(R.id.vehicleValue);
        alertValue = (EditText) findViewById(R.id.alertValue);

        frmDateLay = (TextInputLayout) findViewById(R.id.dateLayID);
        toDateLay = (TextInputLayout) findViewById(R.id.todateLayID);
        frmtimeLay = (TextInputLayout) findViewById(R.id.timeLayID);
        toTimeLay = (TextInputLayout) findViewById(R.id.toTimeLayID);

        clnFrmDate = (TextView) findViewById(R.id.xfrmdate);
        xalert = (TextView) findViewById(R.id.xalert);
        xvehicl = (TextView) findViewById(R.id.xvehicl);
        xvehiclegroup = (TextView) findViewById(R.id.xvehiclegroup);
        xalert.setTypeface(font_awesome);
        xvehicl.setTypeface(font_awesome);
        xvehiclegroup.setTypeface(font_awesome);
        xalert.setVisibility(View.GONE);
        xvehicl.setVisibility(View.GONE);
        xvehiclegroup.setVisibility(View.GONE);

        clnFrmDate.setTypeface(font_awesome);
        clnFrmDate.setVisibility(View.GONE);
        clnToDate = (TextView) findViewById(R.id.xtodate);
        clnToDate.setTypeface(font_awesome);
        clnToDate.setVisibility(View.GONE);
        frmDateLay.setTypeface(font_awesome);
        frmDateLay.setHint(getString(R.string.fa_calendar_plus_o) + "  From Date");

        toDateLay.setTypeface(font_awesome);
        toDateLay.setHint(getString(R.string.fa_calendar_plus_o) + "  To Date");

        Currentdate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        sharedpreferencenew = context.getSharedPreferences(Constants.app_preference_login,
                Context.MODE_PRIVATE);
        user_type_id = sharedpreferencenew.getString("user_type_id", "");
        id = sharedpreferencenew.getString("id", "");
        VehicleGroup = sharedpreferencenew.getString("AlertViewgroup", "");
        selectedDate = Calendar.getInstance();
        backdatecalendar=Calendar.getInstance();
        backdatecalendar.add(Calendar.DAY_OF_MONTH,-7);
        backOnedatecalendar=Calendar.getInstance();
        backOnedatecalendar.add(Calendar.DAY_OF_MONTH,-1);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int year = selectedDate.get(Calendar.YEAR);
        int hour = selectedDate.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDate.get(Calendar.MINUTE);
        int dayb = backdatecalendar.get(Calendar.DAY_OF_MONTH);
        int monthb = backdatecalendar.get(Calendar.MONTH) + 1;
        int yearb = backdatecalendar.get(Calendar.YEAR);
        int dayo = backOnedatecalendar.get(Calendar.DAY_OF_MONTH);
        int montho = backOnedatecalendar.get(Calendar.MONTH) + 1;
        int yearo = backOnedatecalendar.get(Calendar.YEAR);
        arrayList_type = new ArrayList<>();
        arrayList_vehicle = new ArrayList<>();
        arrayList_vehicle_group = new ArrayList<>();
        positionClickListener = (PositionClickListener) this;
        cl = (ClickListner) this;
        current_vehicle_status_dashboard_AllVehicle(user_type_id, id);
        new AsyncTaskToObtainTypeForDropDown().execute();

        new AsyncTaskToObtainAllDetailsForDropDown().execute();
        //new AsyncTaskToObtainGroupWiseVehicleForDropDown().execute();



        xvehiclegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(VehicleGroupCount) != null && !String.valueOf(VehicleGroupCount).isEmpty()) {
                    VehicleGroupCount = 0;
                    vehiclegroupValue.setText("Please select Vehicle Group");
                    xvehiclegroup.setVisibility(View.GONE);
                    //edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                }
            }
        });
        xvehicl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(VehicleCount) != null && !String.valueOf(VehicleCount).isEmpty()) {
                    VehicleGroupCount = 0;
                    vehicleValue.setText(R.string.selectVehicle);
                    xvehicl.setVisibility(View.GONE);
                    //edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                }
            }
        });
        xalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(AlertCount) != null && !String.valueOf(AlertCount).isEmpty()) {
                    AlertCount = 0;
                    alertValue.setText("Please Select Alert");
                    xalert.setVisibility(View.GONE);
                    //edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                }
            }
        });


        FromDatetxt = (EditText) findViewById(R.id.txtFromValue);
       /* FromDatetxt.setText(day + "-" + month + "-"
                + year);*/
        FromDatetxt.setText(dayb + "-" + monthb + "-"
                + yearb);
        FromDatetxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DatePickerDialog dp = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view,
                                                  int selectedYear, int selectedMonth,
                                                  int selectedDay) {

                                year1 = String.valueOf(selectedYear);
                                month1 = (selectedMonth + 1) <= 9 ? "0"
                                        + (selectedMonth + 1) : String
                                        .valueOf(selectedMonth + 1);
                                day1 = selectedDay <= 9 ? "0"
                                        + selectedDay : String
                                        .valueOf(selectedDay);
                                fromDate = day1.concat("-").concat(month1).concat("-").concat(year1);
                                if (isValidMaintenanceDate(Currentdate, fromDate)) {
                                    Toast.makeText(context, "From date should not be after current date", Toast.LENGTH_LONG).show();
                                } else {
                                    FromDatetxt.setText(day1 + "-" + month1 + "-"
                                            + year1);
                                    clnFrmDate.setVisibility(View.VISIBLE);
                                    selectedDate.set(selectedYear,selectedMonth,selectedDay);


                                }
                            }
                        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));

                dp.getDatePicker().setMaxDate(System.currentTimeMillis());

                dp.show();
            }
        });

        clnFrmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FromDatetxt.setText("");
                clnFrmDate.setVisibility(View.GONE);
            }
        });
        ToDatetxt = (EditText) findViewById(R.id.txtToValue);
        ToDatetxt.setText(dayo + "-" + montho + "-"
                + yearo);
        ToDatetxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DatePickerDialog dp = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view,
                                                  int selectedYear, int selectedMonth,
                                                  int selectedDay) {

                                year1 = String.valueOf(selectedYear);
                                month1 = (selectedMonth + 1) <= 9 ? "0"
                                        + (selectedMonth + 1) : String
                                        .valueOf(selectedMonth + 1);
                                day1 = selectedDay <= 9 ? "0"
                                        + selectedDay : String
                                        .valueOf(selectedDay);

                                String toDate = day1.concat("-").concat(month1).concat("-").concat(year1);
                                if (isValidMaintenanceDate(Currentdate, toDate)) {
                                    Toast.makeText(context, "To date should not be after current date", Toast.LENGTH_LONG).show();
                                } else {
                                    ToDatetxt.setText(day1 + "-" + month1 + "-"
                                            + year1);
                                    clnToDate.setVisibility(View.VISIBLE);
                                    selectedDate.set(selectedYear,selectedMonth,selectedDay);


                                }

                            }
                        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));
                dp.getDatePicker().setMaxDate(System.currentTimeMillis());

                dp.show();


            }
        });

        clnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDatetxt.setText("");
                clnToDate.setVisibility(View.GONE);
            }
        });


        vehiclegroupValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList_vehicle_group.size() > 0) {
                    vehicleGroupDialog.show(getFragmentManager(), Constants.IReport.VEHICLE_GROUP);
                } else {
                    Toast.makeText(context, "Please wait while loading ", Toast.LENGTH_LONG).show();
                }

            }
        });
        vehicleValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList_vehicle.size() > 0) {
                    vehiclesDialog.show(getFragmentManager(), Constants.IReport.VEHICLES);
                } else {
                    Toast.makeText(context, "Please wait while loading", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show(getFragmentManager(), Constants.IReport.ALERT);
            }
        });

        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indicatorVal = "";


                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");

                FromDateString = FromDatetxt.getText().toString();
                ToDateString = ToDatetxt.getText().toString();

                Date FromDate = null;
                try {
                    FromDate = inFormat.parse(FromDateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Date ToDate = null;
                try {
                    ToDate = inFormat.parse(ToDateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                final Calendar calendr = Calendar.getInstance();
                calendr.add(Calendar.MONTH, -1);

                if (FromDate != null) {
                    //diff=todayDate.getTime()-FromDate.getTime();
                }
                try {
                    NewFromDateString = outFormat.format(FromDate);
                } catch (NullPointerException e) {
                    NewFromDateString = null;
                }
                try {
                    NewToDateString = outFormat.format(ToDate);
                } catch (NullPointerException e) {
                    NewToDateString = null;
                }

                if (FromDate == null || ToDate == null) {

                    cc.CommonDialog(context, "notification_click", "", "Select From date and To date.", "Ok", "", false);

                } else if (FromDate.compareTo(ToDate) > 0) {
                    cc.CommonDialog(context, "notification_click", "", "To Date should be after From Date", "Ok", "", false);

                } else if (CalculateDay(NewFromDateString, NewToDateString)) {
                    cc.CommonDialog(context, "notification_click", "", "Maximum Duration between From Date and To Date is 7 Days", "Ok", "", false);

                }

                // Vehicle_group_type=autoTextViewCustomone.getText().toString().trim();
                else if (vehicleValue.getText().toString().equalsIgnoreCase("")) {

                    cc.CommonDialog(context, "notification_click", "", "Please Choose Vehicle", "Ok", "", false);

                }
                else {
                    /*getAlertReport(user_type_id, id, NewFromDateString, NewToDateString,
                            VehicleGroup,
                            sharedpreferencenew.getString("AlertGroupWiseVehicle", ""),
                            sharedpreferencenew.getString("AlertType", ""));*/
                    if (VehicleCount == 0) {
                        cc.CommonDialog(context, "notification_click", "", "Please Select Vehicle", "Ok", "", false);


                    }else if(vehicleValue.getText().toString().equalsIgnoreCase("Please Select Vehicle")){
                        cc.CommonDialog(context, "notification_click", "", "Please Select Vehicle", "Ok", "", false);
                    }

                    else {
                        viewReport.setClickable(false);
                        viewReport.setEnabled(false);
                        viewReport.setBackgroundResource(R.drawable.button_boarder_disable);

                        getAlertReport(user_type_id, id, NewFromDateString, NewToDateString,
                                getSelectedVehicleGroupIds(),
                                getSelectedVehicleIds(),
                                getSelectedAlertTypeIds());
                    }
                    //getAlertReport(user_type_id,id,NewFromDateString,NewToDateString,Vehicle_group,SendVehicle,Sendstatus);
                }
            }
        });

    }

    public void getAlertReport(String usertype_id, final String user_id, final String fromdate,
                               final String todate, final String vehicle_group, final String vehicle, final String status) {
        try {

            Log.d(TAG, "usertype_id: " + usertype_id);
            Log.d(TAG, "user_id: " + user_id);
            Log.d(TAG, "fromdate: " + fromdate);
            Log.d(TAG, "todate: " + todate);
            Log.d(TAG, "vehicle_group: " + vehicle_group);
            Log.d(TAG, "vehicle: " + vehicle);
            Log.d(TAG, "status: " + status);

            progressbar_login.setVisibility(View.VISIBLE);

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.getGetVehiclesummaryreport(auth, apiKey, usertype_id, user_id, fromdate, todate, vehicle_group, vehicle);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();
                        progressbar_login.setVisibility(View.GONE);
                        viewReport.setClickable(true);
                        viewReport.setEnabled(true);
                        viewReport.setBackgroundResource(R.drawable.button_boarder);

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code: " + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                Log.e(TAG, "Response strResponse: " + strResponse);
                                JSONObject myObject = new JSONObject(strResponse);

                                String strStatus = myObject.getString("status");

                                if (strStatus.trim().equals("success")) {
                                    try {
                                        if (strStatus != null) {

                                            //Toast.makeText(context,"Report Successfully Send To Your Registered Email ID"+syncresponse,Toast.LENGTH_SHORT).show();
                                            new AlertDialog.Builder(VehicleSummaryReportActivityNew.this)
                                                    .setMessage("Report successfully sent to your registered email id ")
                                                    .setCancelable(false)
                                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }

                                                    }).show();
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        progressbar_login.setVisibility(View.GONE);

                                    }
                                } else {
                                    try {
                                        if(myObject.has("data")) {
                                            String msg = "";
                                            JSONObject jsonObject = myObject.optJSONObject("data");
                                            Iterator<String> keys = jsonObject.keys();
                                            while (keys.hasNext()) {
                                                String key = keys.next();
                                                String value = msg + jsonObject.getString(key);
                                                msg = value+"\n";
                                                Log.v("category key", key);
                                                Log.v("category value", value);
                                            }
                                            new AlertDialog.Builder(context)
                                                    .setMessage(msg)
                                                    .setCancelable(false)
                                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }

                                                    }).show();
                                        }else {
                                            new AlertDialog.Builder(context)
                                                    .setMessage(myObject.optString("msg"))
                                                    .setCancelable(false)
                                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }

                                                    }).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case 500:

                                Toast.makeText(context, "Please Try Again", Toast.LENGTH_LONG).show();


                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressbar_login.setVisibility(View.GONE);
                        Toast.makeText(context, "Server not responding please try again", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");
                    progressbar_login.setVisibility(View.GONE);

                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "Api fail");
            progressbar_login.setVisibility(View.GONE);

        }
    }

    public void current_vehicle_status_dashboard(final String viewgroup) {
        try {

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.getGropWiseVehicle(auth, apiKey, viewgroup);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();

                                // Toast.makeText(ctx,"onresponse "+strResponse,Toast.LENGTH_SHORT).show();
                                //googleMap.setPadding(0, 0, 0, 80);


                                try {

                                    JSONObject jsonObject = new JSONObject(strResponse);
                                    String status = jsonObject.getString("status");

                                    if (status.equalsIgnoreCase("success")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                VehiclePojo vehiclePojo = new VehiclePojo();

                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String vehicle_id = jsonObject1.getString("id");
                                                String vehicle_no = jsonObject1.getString("vehicle_no");

                                            /*if (arraylist_position.contains(i)) {
                                                vehiclePojo.setSelected(true);
                                            } else {
                                                vehiclePojo.setSelected(false);
                                            }*/


                                              /*if (Constant.arraylist_position.contains(i)) {
                                                  vehiclePojo.setSelected(true);
                                              } else {
                                                  vehiclePojo.setSelected(false);
                                              }*/


                                                vehiclePojo.setVehicle_id(vehicle_id);
                                                vehiclePojo.setVehicle_no(vehicle_no);


                                                Constant.arrayList_vehicle.add(vehiclePojo);
                                                arrayList_vehicle.add(vehiclePojo);

                                                Log.e(TAG,"size_ofarraylist"+ Constant.arrayList_vehicle.size());


                                            }

                                            setVehiclesHashmap();
                                        } else {

                                            Toast.makeText(context, "No Vehicle Found For This ViewGroup", Toast.LENGTH_LONG).show();
                                        }


                                    }


/*
                                    if (arrayList_vehicle.size()>0){
                                        generateconnectionreporttwo = new NotificationFilterAdapter(context, arrayList_vehicle);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                        recyclerView_vehicle.setLayoutManager(mLayoutManager);
                                        recyclerView_vehicle.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                                        recycllerView_vehicle.setAdapter(generateconnectionreporttwo);
                                        generateconnectionreporttwo.notifyDataSetChanged();
                                    }
*/


                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                        }


//                        Log.e("pojo_arraylist", String.valueOf(Constant.arraylist_position.size()));


//                        if (Constant.arrayList_vehicle.size()==Constant.arraylist_position.size()){
//                            selectAllCb.setChecked(true);
//                        }
//                        if (Constant.check2==true) {
//
//                            selectAllCb.setChecked(true);
//                            int size = arrayList_vehicle.size();
//                            for (int i = 0; i < size; i++) {
//                                VehiclePojo dto = arrayList_vehicle.get(i);
//                                dto.setSelected(true);
//                            }
//                            generateconnectionreporttwo.selectAll(true);
//                            generateconnectionreporttwo.notifyDataSetChanged();
//
//
//                        }else{
//                            selectAllCb.setChecked(false);
//
//                            Constant.selectedStrings.clear();
//
//
//                        }
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
            Log.e(TAG, "Api fail");
        }
    }

    public void current_vehicle_status_dashboard_type(String usertype_id, final String user_id) {
        try {

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.getNotificationType(auth, apiKey, usertype_id, user_id);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();

                                // Toast.makeText(ctx,"onresponse "+strResponse,Toast.LENGTH_SHORT).show();
                                //googleMap.setPadding(0, 0, 0, 80);

                                try {
                                    if (strResponse != null) {
                                        //Toast.makeText(context, "on response vehicle list " + strResponse, Toast.LENGTH_SHORT).show();

                                        try {

                                            JSONObject jsonObject = new JSONObject(strResponse);
                                            String status = jsonObject.getString("status");

                                            if (status.equalsIgnoreCase("success")) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    NotificationPojo notificatiobPojo = new NotificationPojo();

                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    String title = jsonObject1.getString("title");
                                                    String notification_id = jsonObject1.getString("id");
                                                    String slug = jsonObject1.getString("slug");

                                                    if (Constant.arraylist_position_t.contains(i)) {
                                                        notificatiobPojo.setSelected(true);
                                                    } else {
                                                        notificatiobPojo.setSelected(false);
                                                    }

                                                    notificatiobPojo.setTitle(title);
                                                    notificatiobPojo.setNotification_id(notification_id);
                                                    notificatiobPojo.setSlug(slug);

                                                    Constant.arrayList_alert_type.add(notificatiobPojo);
                                                    arrayList_type.add(notificatiobPojo);

//                                                    Log.e("size_arrayList_type", String.valueOf(arrayList_type.size()));


                                                }

                                                setAlertHashmap();

                                            } else {

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//                                if (getSharedPreferences("selectall_checked_type", MODE_PRIVATE).getString("selectall_checked_type", null).equalsIgnoreCase("true")) {

//
//
//                                if (Constant.arralistcheck1.size()>0){
//                                    selectAllCbtype.setChecked(true);
//                                    int size = arrayList_type.size();
//                                    for (int i = 0; i < size; i++) {
//                                        NotificationPojo dto = arrayList_type.get(i);
//                                        dto.setSelected(true);
//                                    }
//                                    generateconnectionreporttwotype.selectAll(true);
//                                    generateconnectionreporttwotype.notifyDataSetChanged();
//
//                                }else {
//                                    selectAllCbtype.setChecked(false);
//                                    Constant.selectedStringstype.clear();
//
//                                }

//                                if (Constant.check1==true){
//                                    selectAllCbtype.setChecked(true);
//                                    int size = arrayList_type.size();
//                                    for (int i = 0; i < size; i++) {
//                                        NotificationPojo dto = arrayList_type.get(i);
//                                        dto.setSelected(true);
//                                    }
//                                    generateconnectionreporttwotype.selectAll(true);
//                                    generateconnectionreporttwotype.notifyDataSetChanged();
//
//
//                                }else{
//                                    selectAllCbtype.setChecked(false);
//                                    Constant.selectedStringstype.clear();
//
//                                }
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
            Log.e(TAG, "Api fail");
        }
    }

    public void fn_getAllViewGroup(String usertype_id, final String user_id) {
        try {

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.getAllGroup(auth, apiKey, usertype_id, user_id);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();

                                // Toast.makeText(ctx,"onresponse "+strResponse,Toast.LENGTH_SHORT).show();
                                //googleMap.setPadding(0, 0, 0, 80);

                                try {
                                    if (strResponse != null) {
                                        //Toast.makeText(context, "on response vehicle list " + strResponse, Toast.LENGTH_SHORT).show();

                                        try {

                                            JSONObject jsonObject = new JSONObject(strResponse);
                                            String status = jsonObject.getString("status");

                                            if (status.equalsIgnoreCase("success")) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    VehicleViewGroup vehiclegroupPojo = new VehicleViewGroup();

                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    String title = jsonObject1.getString("title");
                                                    String notification_id = jsonObject1.getString("id");

                                                    vehiclegroupPojo.setTitle(title);
                                                    vehiclegroupPojo.setId(notification_id);

                                                    Constant.arrayList_vehicle_group.add(vehiclegroupPojo);
                                                    arrayList_vehicle_group.add(vehiclegroupPojo);

//                                                    Log.e("size_arrayList_type", String.valueOf(arrayList_type.size()));


                                                 /*   viewgroupadapter = new VehicleViewGroupAdapter(context,R.layout.custom_row,arrayList_vehicle_group);
                                                    autoTextViewCustomone.setThreshold(0);
                                                    autoTextViewCustomone.setAdapter(viewgroupadapter);*/
                                                    // autoTextViewCustomthree.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                                                }

                                                setVehicleGroupHashmap();

                                            } else {

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//                                if (getSharedPreferences("selectall_checked_type", MODE_PRIVATE).getString("selectall_checked_type", null).equalsIgnoreCase("true")) {

//
//
//                                if (Constant.arralistcheck1.size()>0){
//                                    selectAllCbtype.setChecked(true);
//                                    int size = arrayList_type.size();
//                                    for (int i = 0; i < size; i++) {
//                                        NotificationPojo dto = arrayList_type.get(i);
//                                        dto.setSelected(true);
//                                    }
//                                    generateconnectionreporttwotype.selectAll(true);
//                                    generateconnectionreporttwotype.notifyDataSetChanged();
//
//                                }else {
//                                    selectAllCbtype.setChecked(false);
//                                    Constant.selectedStringstype.clear();
//
//                                }

//                                if (Constant.check1==true){
//                                    selectAllCbtype.setChecked(true);
//                                    int size = arrayList_type.size();
//                                    for (int i = 0; i < size; i++) {
//                                        NotificationPojo dto = arrayList_type.get(i);
//                                        dto.setSelected(true);
//                                    }
//                                    generateconnectionreporttwotype.selectAll(true);
//                                    generateconnectionreporttwotype.notifyDataSetChanged();
//
//
//                                }else{
//                                    selectAllCbtype.setChecked(false);
//                                    Constant.selectedStringstype.clear();
//
//                                }
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
            Log.e(TAG, "Api fail");
        }
    }

    public void VehicleList(String usertype_id, final String user_id) {
        try {

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.getNotificationType(auth, apiKey, usertype_id, user_id);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //  Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();

                                // Toast.makeText(ctx,"onresponse "+strResponse,Toast.LENGTH_SHORT).show();
                                //googleMap.setPadding(0, 0, 0, 80);

                                try {
                                    if (strResponse != null) {
                                        //Toast.makeText(context, "on response vehicle list " + strResponse, Toast.LENGTH_SHORT).show();

                                        try {

                                            JSONObject jsonObject = new JSONObject(strResponse);
                                            String status = jsonObject.getString("status");

                                            if (status.equalsIgnoreCase("success")) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    NotificationPojo notificatiobPojo = new NotificationPojo();

                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    String title = jsonObject1.getString("title");
                                                    String notification_id = jsonObject1.getString("id");
                                                    String slug =jsonObject1.getString("slug");

                                                    if (Constant.arraylist_position_t.contains(i)) {
                                                        notificatiobPojo.setSelected(true);
                                                    } else {
                                                        notificatiobPojo.setSelected(false);
                                                    }

                                                    notificatiobPojo.setTitle(title);
                                                    notificatiobPojo.setNotification_id(notification_id);
                                                    notificatiobPojo.setSlug(slug);

                                                    arrayList_type.add(notificatiobPojo);

//                                                    Log.e("size_arrayList_type", String.valueOf(arrayList_type.size()));


                                                    generateconnectionreporttwotype = new AlertTypeAdapter(context, R.layout.custom_row, arrayList_type);
                                                    autoTextViewCustomthree.setThreshold(0);
                                                    autoTextViewCustomthree.setAdapter(generateconnectionreporttwotype);
                                                    autoTextViewCustomthree.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                                                }

                                                setAlertHashmap();
                                            } else {

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


//                                if (getSharedPreferences("selectall_checked_type", MODE_PRIVATE).getString("selectall_checked_type", null).equalsIgnoreCase("true")) {

//
//
//                                if (Constant.arralistcheck1.size()>0){
//                                    selectAllCbtype.setChecked(true);
//                                    int size = arrayList_type.size();
//                                    for (int i = 0; i < size; i++) {
//                                        NotificationPojo dto = arrayList_type.get(i);
//                                        dto.setSelected(true);
//                                    }
//                                    generateconnectionreporttwotype.selectAll(true);
//                                    generateconnectionreporttwotype.notifyDataSetChanged();
//
//                                }else {
//                                    selectAllCbtype.setChecked(false);
//                                    Constant.selectedStringstype.clear();
//
//                                }

//                                if (Constant.check1==true){
//                                    selectAllCbtype.setChecked(true);
//                                    int size = arrayList_type.size();
//                                    for (int i = 0; i < size; i++) {
//                                        NotificationPojo dto = arrayList_type.get(i);
//                                        dto.setSelected(true);
//                                    }
//                                    generateconnectionreporttwotype.selectAll(true);
//                                    generateconnectionreporttwotype.notifyDataSetChanged();
//
//
//                                }else{
//                                    selectAllCbtype.setChecked(false);
//                                    Constant.selectedStringstype.clear();
//
//                                }
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
            Log.e(TAG, "Api fail");
        }
    }

    private boolean isValidMaintenanceDate(String LastMaintenanceDateString, String NextMaintenanceDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date d1;
        Date d2;
        boolean flag = true;
        try {
            d1 = sdf.parse(LastMaintenanceDateString);

            d2 = sdf.parse(NextMaintenanceDateString);


            if (d2.compareTo(d1) == -1 || d2.compareTo(d1) == 0)

                flag = false;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void itemClickedType(int id, ArrayList<Integer> arraylist_position_type) {

    }

    @Override
    public void itemselectAll(boolean value) {

    }

    @Override
    public void itemClicked(int id, ArrayList<Integer> arraylist_position, Boolean flag) {

    }

    @Override
    public void Onclickclr(int count, String type, Context c) {

      /*  TextView txtView = (TextView) ((Activity)c).findViewById(R.id.autoTextViewCustomonee);
        txtView.setText(count)*/
        if (type.equalsIgnoreCase("group")) {
            VehicleGroupCount = count;
            vehiclegroupValue.setText(count + " Vehicle Group Selected");
            xvehiclegroup.setVisibility(View.VISIBLE);
            //vehicleLinearLayout.setVisibility(View.VISIBLE);
            arrayList_vehicle.clear();
            if (count == 0) {
                sharedpreferencenew.edit().remove("AlertViewgroup").commit();

                arrayList_vehicle = new ArrayList<>();
                current_vehicle_status_dashboard_AllVehicle(user_type_id, id);
            } else {
                VehicleGroup = sharedpreferencenew.getString("AlertViewgroup", "");
                arrayList_vehicle = new ArrayList<>();
                current_vehicle_status_dashboard(VehicleGroup);

                //new AsyncTaskToObtainGroupWiseVehicleForDropDown().execute();
            }
        } else if (type.equalsIgnoreCase("vehicle")) {
            VehicleCount = count;
            vehicleValue.setText(count + " Vehicle Selected");
            xvehicl.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("type")) {
            AlertCount = count;
            alertValue.setText(count + " Type Selected");
            xalert.setVisibility(View.VISIBLE);
        }
        // SetCount(count);
    }

   /* @Override
    public void onClickDialogItemListener(int count, String type, ArrayList<String> selectedList) {

    }*/

    private void SetCount(int count) {


    }

    private class AsyncTaskToObtainAllDetailsForDropDown extends AsyncTask<Void, Void, Void> {
//        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Obtaining From Bus Stop Code,Bus Stops, Bus Types And Fare Types And Initializing Fragment Dialog  For Each Of Them//
     /*       selectedFromBusStopCode = DatabaseController.getDatabaseControllerInstance(context).getFromBusStopCode();

            //  m_busStopsList = DatabaseController.getDatabaseControllerInstance(context).getBusStopDetails("DDRE");
            m_busStopsList = DatabaseController.getDatabaseControllerInstance(context).getBusStopDetails(selectedFromBusStopCode);
            m_busTypesList = DatabaseController.getDatabaseControllerInstance(context).getBusTypeDetails();
            m_fareTypesList = DatabaseController.getDatabaseControllerInstance(context).getFareTypeDetails();*/
            fn_getAllViewGroup(user_type_id, id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //  Toast.makeText(ActivityIssueTickets.this,"m_busStopsList==="+m_busStopsList.size(),Toast.LENGTH_SHORT).show();
//            initializeCustomDialogForBusStops();
            // Toast.makeText(ActivityIssueTickets.this,"m_busTypesList=="+m_busTypesList.size(),Toast.LENGTH_SHORT).show();
            //initializeCustomDialogForBusTypes();
            // Toast.makeText(ActivityIssueTickets.this,"m_fareTypesList=="+m_fareTypesList.size(),Toast.LENGTH_SHORT).show();
            //initializeCustomDialogForFareTypes();
        }


    }

    private class AsyncTaskToObtainTypeForDropDown extends AsyncTask<Void, Void, Void> {
//        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Obtaining From Bus Stop Code,Bus Stops, Bus Types And Fare Types And Initializing Fragment Dialog  For Each Of Them//
     /*       selectedFromBusStopCode = DatabaseController.getDatabaseControllerInstance(context).getFromBusStopCode();

            //  m_busStopsList = DatabaseController.getDatabaseControllerInstance(context).getBusStopDetails("DDRE");
            m_busStopsList = DatabaseController.getDatabaseControllerInstance(context).getBusStopDetails(selectedFromBusStopCode);
            m_busTypesList = DatabaseController.getDatabaseControllerInstance(context).getBusTypeDetails();
            m_fareTypesList = DatabaseController.getDatabaseControllerInstance(context).getFareTypeDetails();*/
            current_vehicle_status_dashboard_type(user_type_id, id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //  Toast.makeText(ActivityIssueTickets.this,"m_busStopsList==="+m_busStopsList.size(),Toast.LENGTH_SHORT).show();
//            initializeCustomDialogForType();
            // Toast.makeText(ActivityIssueTickets.this,"m_busTypesList=="+m_busTypesList.size(),Toast.LENGTH_SHORT).show();
            //initializeCustomDialogForBusTypes();
            // Toast.makeText(ActivityIssueTickets.this,"m_fareTypesList=="+m_fareTypesList.size(),Toast.LENGTH_SHORT).show();
            //initializeCustomDialogForFareTypes();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (data != null) {
            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                if (extras.getString("return_date") != null && !extras.getString("return_date").isEmpty()) {
                    strReturnDate = extras.getString("return_date");
                    edtReturnDate.setText(strReturnDate);
                    edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                } else {
                    edtReturnDate.clearFocus();
                }

                if (extras.getString("departure_date") != null && !extras.getString("departure_date").isEmpty()) {
                    strDepartureDate = extras.getString("departure_date");
                    edtDepDate.setText(strDepartureDate);
                    edtDepDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                } else {
                    edtDepDate.clearFocus();
                }
            }
        }
    }


    private void clearStaticDataFromConstants() {
        Constant.selectedGroupStringArray.clear();
        Constant.selectedTypeStringArray.clear();
        Constant.selectedVehicleStringArray.clear();
        Constant.selectedGroupPositionsArray.clear();
        Constant.selectedTypePositionsArray.clear();
        Constant.selectedVehiclePositionsArray.clear();
        Constant.arraylist_position_t_group.clear();
        Constant.arraylist_position_t_vehicle.clear();
        Constant.arraylist_position_t_type.clear();
        Constant.arrayList_vehicle.clear();
        Constant.arrayList_vehicle_group.clear();
        Constant.arrayList_alert_type.clear();
        Constant.arraylist_alertTypeTitle_group.clear();
        Constant.arraylist_alertTypeTitle_type.clear();
        Constant.arraylist_alertTypeTitle_vehicle.clear();
        Constant.selectedStrings.clear();
        Constant.selectedStringstype.clear();
        Constant.arraylist_position.clear();
        Constant.arraylist_alertTypeTitle.clear();
        Constant.arraylist_position_t.clear();
        Constant.arrayListcheck2.clear();
        Constant.arralistcheck1.clear();
        Constant.selectedStringsVehicleGroupWise.clear();
        Constant.selectedStringsVehicleGroupWisePosition.clear();
        Constant.set.clear();
        Constant.stringBuilder = new StringBuilder();
        Constant.check2 = false;
        Constant.check1 = false;
        Constant.first = false;
        Constant.second = false;
        Constant.vehicleflag = false;
        Constant.trackerflag = false;
        Constant.clickOkAlertDialog = false;
        Constant.s = false;
    }

    public void current_vehicle_status_dashboard_AllVehicle(String usertype_id, final String user_id) {
        try {
            Utils.showProgressDialog(VehicleSummaryReportActivityNew.this, "Please Wait");

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.current_vehicle_details(auth, apiKey, usertype_id, user_id,record_id);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();

                                // Toast.makeText(ctx,"onresponse "+strResponse,Toast.LENGTH_SHORT).show();
                                //googleMap.setPadding(0, 0, 0, 80);

                                try {

                                    JSONObject jsonObject = new JSONObject(strResponse);
                                    String status = jsonObject.getString("status");

                                    if (status.equalsIgnoreCase("success")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                VehiclePojo vehiclePojo = new VehiclePojo();

                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String vehicle_id = jsonObject1.getString("vehicle_id");
                                                String vehicle_no = jsonObject1.getString("vehicle_no");

                                            /*if (arraylist_position.contains(i)) {
                                                vehiclePojo.setSelected(true);
                                            } else {
                                                vehiclePojo.setSelected(false);
                                            }*/


                                              /*if (Constant.arraylist_position.contains(i)) {
                                                  vehiclePojo.setSelected(true);
                                              } else {
                                                  vehiclePojo.setSelected(false);
                                              }*/


                                                vehiclePojo.setVehicle_id(vehicle_id);
                                                vehiclePojo.setVehicle_no(vehicle_no);


                                                Constant.arrayList_vehicle.add(vehiclePojo);
                                                arrayList_vehicle.add(vehiclePojo);

                                                Log.e(TAG,"size_ofarraylist"+ Constant.arrayList_vehicle.size());

                                            }

                                            setVehiclesHashmap();
                                        } else {

                                            Toast.makeText(context, "No Vehicle Found For This ViewGroup", Toast.LENGTH_LONG).show();
                                        }


                                    }


/*
                                    if (arrayList_vehicle.size()>0){
                                        generateconnectionreporttwo = new NotificationFilterAdapter(context, arrayList_vehicle);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                        recyclerView_vehicle.setLayoutManager(mLayoutManager);
                                        recyclerView_vehicle.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                                        recycllerView_vehicle.setAdapter(generateconnectionreporttwo);
                                        generateconnectionreporttwo.notifyDataSetChanged();
                                    }
*/


                                } catch (Exception e) {
                                    e.printStackTrace();

                                }


//                                if (getSharedPreferences("selectall_checked_type", MODE_PRIVATE).getString("selectall_checked_type", null).equalsIgnoreCase("true")) {

//
//
//                                if (Constant.arralistcheck1.size()>0){
//                                    selectAllCbtype.setChecked(true);
//                                    int size = arrayList_type.size();
//                                    for (int i = 0; i < size; i++) {
//                                        NotificationPojo dto = arrayList_type.get(i);
//                                        dto.setSelected(true);
//                                    }
//                                    generateconnectionreporttwotype.selectAll(true);
//                                    generateconnectionreporttwotype.notifyDataSetChanged();
//
//                                }else {
//                                    selectAllCbtype.setChecked(false);
//                                    Constant.selectedStringstype.clear();
//
//                                }

//                                if (Constant.check1==true){
//                                    selectAllCbtype.setChecked(true);
//                                    int size = arrayList_type.size();
//                                    for (int i = 0; i < size; i++) {
//                                        NotificationPojo dto = arrayList_type.get(i);
//                                        dto.setSelected(true);
//                                    }
//                                    generateconnectionreporttwotype.selectAll(true);
//                                    generateconnectionreporttwotype.notifyDataSetChanged();
//
//
//                                }else{
//                                    selectAllCbtype.setChecked(false);
//                                    Constant.selectedStringstype.clear();
//
//                                }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                     Utils.stopProgressDialog();
                    // cc.showSnackbar(viewpart,"Something Went Wrong Please Try Again..");

                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "Api fail");
        }
    }



    private void initHashmaps() {
        if (vehicleGroupHashmap == null)
            vehicleGroupHashmap = new HashMap<>();
        if (vehiclesHashmap == null)
            vehiclesHashmap = new HashMap<>();
        if (alertHashmap == null)
            alertHashmap = new HashMap<>();
    }


    private void setVehicleGroupHashmap() {
        for (int i = 0; i < arrayList_vehicle_group.size(); i++) {
            vehicleGroupHashmap.put(arrayList_vehicle_group.get(i).getTitle(), false);
        }
        Log.d(TAG, "setVehicleGroupHashmap: vehicleGroupHashmap size: " + vehicleGroupHashmap.size()
                + " arrayListsize: " + arrayList_vehicle_group.size());


        vehicleGroupDialog = new ReportCustomDialog(VehicleSummaryReportActivityNew.this, Constants.IReport.VEHICLE_GROUP,
                vehicleGroupHashmap, this);
    }

    private void setVehiclesHashmap() {
        for (int i = 0; i < arrayList_vehicle.size(); i++) {
            vehiclesHashmap.put(arrayList_vehicle.get(i).getVehicle_no(), false);
        }
        Log.d(TAG, "setVehicleGroupHashmap: vehiclesHashmap size: " + vehiclesHashmap.size()
                + " arrayListsize: " + arrayList_vehicle.size());

        vehiclesDialog = new ReportCustomDialog(VehicleSummaryReportActivityNew.this, Constants.IReport.VEHICLES,
                vehiclesHashmap, this);
    }

    private void setAlertHashmap() {
        for (int i = 0; i < arrayList_type.size(); i++) {
            alertHashmap.put(arrayList_type.get(i).getTitle(), false);
        }
        Log.d(TAG, "setAlertHashmap: alertHashmap size: " + alertHashmap.size()
                + " arrayListsize: " + arrayList_type.size());

        alertDialog = new ReportCustomDialog(VehicleSummaryReportActivityNew.this, Constants.IReport.ALERT,
                alertHashmap, this);
    }

    private void updateVehicleGroupHashmap(String selection) {
        int count = 0;
        if (selection.trim().equalsIgnoreCase(Constants.IReport.OK_SELECTION)) {
            for (String s : vehicleGroupHashmap.keySet()) {
                if (vehicleGroupHashmap.get(s)) {
                    count += 1;
                }
            }
        } else if (selection.trim().equalsIgnoreCase(Constants.IReport.CANCEL_SELECTION)) {
            for (String s : vehicleGroupHashmap.keySet()) {
                if (vehicleGroupHashmap.get(s)) {
                    vehicleGroupHashmap.put(s, false);
                }
            }
        } else if (selection.trim().equalsIgnoreCase(Constants.IReport.ALL_SELECTION)) {
            for (String s : vehicleGroupHashmap.keySet()) {
                vehicleGroupHashmap.put(s, true);
                count += 1;
            }
        } else Log.d(TAG, "updateVehicelGroupHashmap: different selection: " + selection);

        Log.d(TAG, "vehicleGroupHashmap count: " + count);
        vehiclegroupValue.setText("" + count + " Vehicle Group Selected");
        xvehiclegroup.setVisibility(View.VISIBLE);
        vehiclesHashmap.clear();
        vehicleValue.setText("" + "0" + " Vehicles Selected");
        VehicleGroupCount = count;
        if (count == 0) {
            sharedpreferencenew.edit().remove("AlertViewgroup").commit();
            arrayList_vehicle = new ArrayList<>();
            current_vehicle_status_dashboard_AllVehicle(user_type_id, id);
        } else {
            getSelectedVehicleGroupIds();
//            VehicleGroup = getSelectedVehicleGroupIds().replace("[[]]", "");//sharedpreferencenew.getString("AlertViewgroup", "");
            Log.d(TAG, "VehicleGroup: "+VehicleGroup);
            arrayList_vehicle = new ArrayList<>();
            current_vehicle_status_dashboard(VehicleGroup);
        }
    }

    private void updateVehiclesHashmap(String selection) {
        int count = 0;
        if (selection.trim().equalsIgnoreCase(Constants.IReport.OK_SELECTION)) {
            for (String s : vehiclesHashmap.keySet()) {
                if (vehiclesHashmap.get(s)) {
                    count += 1;
                }
            }
        } else if (selection.trim().equalsIgnoreCase(Constants.IReport.CANCEL_SELECTION)) {
            for (String s : vehiclesHashmap.keySet()) {
                if (vehiclesHashmap.get(s)) {
                    vehiclesHashmap.put(s, false);
                }
            }
        } else if (selection.trim().equalsIgnoreCase(Constants.IReport.ALL_SELECTION)) {
            for (String s : vehiclesHashmap.keySet()) {
                vehiclesHashmap.put(s, true);
                count += 1;
            }
        } else Log.d(TAG, "updatevehiclesHashmap: different selection: " + selection);

        Log.d(TAG, "vehiclesHashmap count: " + count);
        vehicleValue.setText("" + count + " Vehicle Selected");
        xvehicl.setVisibility(View.VISIBLE);
        VehicleCount = count;
    }

    private void updateAlertHashmap(String selection) {
        int count = 0;
        if (selection.trim().equalsIgnoreCase(Constants.IReport.OK_SELECTION)) {
            for (String s : alertHashmap.keySet()) {
                if (alertHashmap.get(s)) {
                    count += 1;
                }
            }
        } else if (selection.trim().equalsIgnoreCase(Constants.IReport.CANCEL_SELECTION)) {
            for (String s : alertHashmap.keySet()) {
                if (alertHashmap.get(s)) {
                    alertHashmap.put(s, false);
                }
            }
        } else if (selection.trim().equalsIgnoreCase(Constants.IReport.ALL_SELECTION)) {
            for (String s : alertHashmap.keySet()) {
                alertHashmap.put(s, true);
                count += 1;
            }
        } else Log.d(TAG, "updateAlertHashmap: different selection: " + selection);

        Log.d(TAG, "alertHashmap count: " + count);
        alertValue.setText("" + count + " Type Selected");
        xalert.setVisibility(View.VISIBLE);
        AlertCount = count;
    }


    @Override
    public void updateHashmap(String nameOfSelection, String key, Boolean value) {
        Log.d(TAG, "nameOfSelection: " + nameOfSelection + " key: " + key + " value: " + value);
        if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLE_GROUP))
            vehicleGroupHashmap.put(key, value);
        else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLES))
            vehiclesHashmap.put(key, value);
        else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.ALERT))
            alertHashmap.put(key, value);
        else Log.d(TAG, "updateHashmap: different selection: " + nameOfSelection);
    }

    @Override
    public void onButtonClick(String nameOfSelection, String buttonSelection) {
        Log.d(TAG, "nameOfSelection: " + nameOfSelection + " buttonSelection: " + buttonSelection);
        switch (buttonSelection) {
            case Constants.IReport.OK_SELECTION:
                if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLE_GROUP))
                    updateVehicleGroupHashmap(Constants.IReport.OK_SELECTION);
                else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLES))
                    updateVehiclesHashmap(Constants.IReport.OK_SELECTION);
                else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.ALERT))
                    updateAlertHashmap(Constants.IReport.OK_SELECTION);
                else Log.d(TAG, "OK_SELECTION: different selection: " + nameOfSelection);
                break;

            case Constants.IReport.CANCEL_SELECTION:
                if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLE_GROUP))
                    updateVehicleGroupHashmap(Constants.IReport.CANCEL_SELECTION);
                else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLES))
                    updateVehiclesHashmap(Constants.IReport.CANCEL_SELECTION);
                else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.ALERT))
                    updateAlertHashmap(Constants.IReport.CANCEL_SELECTION);
                else Log.d(TAG, "CANCEL_SELECTION: different selection: " + nameOfSelection);
                break;

            case Constants.IReport.ALL_SELECTION:
                if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLE_GROUP))
                    updateVehicleGroupHashmap(Constants.IReport.ALL_SELECTION);
                else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLES))
                    updateVehiclesHashmap(Constants.IReport.ALL_SELECTION);
                else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.ALERT))
                    updateAlertHashmap(Constants.IReport.ALL_SELECTION);
                else Log.d(TAG, "ALL_SELECTION: different selection: " + nameOfSelection);
                break;

            /*case Constants.IReport.NONE :
                if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLE_GROUP))
                    vehicleGroupDialog.updateSelectTab();
                else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.VEHICLES))
                    vehiclesDialog.updateSelectTab();
                else if (nameOfSelection.trim().equalsIgnoreCase(Constants.IReport.ALERT))
                    alertDialog.updateSelectTab();
                else Log.d(TAG, "ALL_SELECTION: different selection: " + nameOfSelection);
                break;*/
            default:
                Log.d(TAG, "OnbuttonClick: different button selection: " + buttonSelection);
                break;
        }
    }


    private String getVehicleId(String vehicleNo) {
        for (VehiclePojo vehiclePojo : arrayList_vehicle) {
            if (vehiclePojo.getVehicle_no().trim().equalsIgnoreCase(vehicleNo.trim()))
                return vehiclePojo.getVehicle_id();
        }
        return "";
    }

    private String getAlertTypeId(String title) {
        for (NotificationPojo pojo : arrayList_type) {
            if (pojo.getTitle().trim().equalsIgnoreCase(title.trim()))
                return pojo.getSlug();
        }
        return "";
    }

    private String getVehicleGroupId(String vehicleGrpTitle) {
        for (VehicleViewGroup vehiclePojo : arrayList_vehicle_group) {
            if (vehiclePojo.getTitle().trim().equalsIgnoreCase(vehicleGrpTitle.trim()))
                return vehiclePojo.getId();
        }
        return "";
    }


    private String getSelectedVehicleIds() {
        String vehicleIds = "";

        for (String s : vehiclesHashmap.keySet()) {
            if (vehiclesHashmap.get(s)) {
                if (vehicleIds.trim().equalsIgnoreCase("")) {
                     vehicleIds =  getVehicleId(s);
                    //vehicleIds = "[" + getVehicleId(s);

                } else {
                    vehicleIds = vehicleIds + "," + getVehicleId(s);
                }
            }
        }
       /* if (vehicleIds.trim().equalsIgnoreCase(""))
            vehicleIds = vehicleIds + "[]";
        else
            vehicleIds = vehicleIds + "]";
       */
        Log.d(TAG, "selectedVehicleId: " + vehicleIds);
        return vehicleIds;

    }


    private String getSelectedAlertTypeIds() {
        String vehicleIds = "";

        String[] value = new String[alertHashmap.size()];
        int count = 0;
        Log.d(TAG, "getAlertTypeId: " + Arrays.toString(value));
        for (String s : alertHashmap.keySet()) {
            if (alertHashmap.get(s)) {
                if (vehicleIds.trim().equalsIgnoreCase("")) {
                    //vehicleIds = "[" + getAlertTypeId(s);
                    vehicleIds = getAlertTypeId(s);
                } else {
                    vehicleIds = vehicleIds + "," + getAlertTypeId(s);
                }
                value[count++] = getAlertTypeId(s);
            }
        }
       /* if (vehicleIds.trim().equalsIgnoreCase(""))
            vehicleIds = vehicleIds + "[]";
        else
            vehicleIds = vehicleIds + "]";
*/
        Log.d(TAG, "getAlertTypeId: " + vehicleIds);
        Log.d(TAG, "getAlertTypeId: " + Arrays.toString(value));
        return vehicleIds;
    }


    private String getSelectedVehicleGroupIds() {
        String vehicleIds = "";
        VehicleGroup = "";
        for (String s : vehicleGroupHashmap.keySet()) {
            if (vehicleGroupHashmap.get(s)) {
                if (vehicleIds.trim().equalsIgnoreCase("")) {
                    //vehicleIds = "[" + getVehicleGroupId(s);
                    vehicleIds =  getVehicleGroupId(s);
                    VehicleGroup = getVehicleGroupId(s);
                } else {
                    vehicleIds = vehicleIds + "," + getVehicleGroupId(s);
                    VehicleGroup = VehicleGroup + "," + getVehicleGroupId(s);
                }
            }
        }
      /*  if (vehicleIds.trim().equalsIgnoreCase(""))
            vehicleIds = vehicleIds + "[]";
        else
            vehicleIds = vehicleIds + "]";
*/
        Log.d(TAG, "selectedVehicleGroupId: " + vehicleIds);
        return vehicleIds;
    }


    public boolean CalculateDay(String dateBeforeString ,String dateAfterString ){
        SimpleDateFormat outFormatMonth = new SimpleDateFormat("yyyy-MM-dd");
        boolean daysBetweens =false;
        float daysBetween=0;
        try {
            Date dateBefore = outFormatMonth.parse(dateBeforeString);
            Date dateAfter = outFormatMonth.parse(dateAfterString);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            daysBetween = (difference / (1000 * 60 * 60 * 24));
            if(daysBetween>7.0){
                daysBetweens=true;
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return daysBetweens;

    }
}

