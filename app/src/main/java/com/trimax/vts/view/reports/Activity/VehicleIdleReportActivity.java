package com.trimax.vts.view.reports.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class VehicleIdleReportActivity extends AppCompatActivity implements PositionClickListener,DatePickerDialog.OnDateSetListener,
        ClickListner, ReportCallback {
    Context context;
    Calendar selectedDate ;

    TextView FromDate, ToDate;

    private int checkedItemSummary_type = 0;
    private int checkedItemReportType = 0;
    String ReportType = "", ChartType = "D", Chartypen = "";
    int SelectedMonth = 0,SelectedYear=0;
    TextView clnFrmDate, xvehiclegroup, clnToDate, xvehicl, xalert;
    EditText FromDatetxt, ToDatetxt, vehiclegroupValue, vehicleValue, alertValue, reportValue, chartValue, txtFromMonthValue;
    Button viewReport;
    String ReportValue;

    Calendar backOnedatecalendar;


    String user_type_id = "";
    String  VehicleGroup = "";
    PositionClickListener positionClickListener;
    LinearLayout DateLinearLayout, MonthLinearLayout, vehicleLinearLayout;
    ClickListner cl;
    String id = "", record_id="";
    TextInputLayout frmDateLay, frmtimeLay, toDateLay, toTimeLay;

    SharedPreferences sharedpreference, sharedpreferencenew;
    ArrayList<NotificationPojo> arrayList_type;
    ArrayList<VehiclePojo> arrayList_vehicle;
    ArrayList<String> vehicle_report_type;
    ArrayList<String> vehicle_chart_type;
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
    EditText edtDepDate, edtReturnDate;
    private AlertTypeAdapter generateconnectionreporttwotype;

    private HashMap<String, Boolean> vehicleGroupHashmap;
    private HashMap<String, Boolean> vehiclesHashmap;
    private HashMap<String, Boolean> alertHashmap;
    private String TAG = VehicleIdleReportActivity.class.getSimpleName();
    private ReportCustomDialog vehicleGroupDialog, vehiclesDialog, alertDialog;
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_stop_summary_report);
        infoPref = new TravelpulseInfoPref(this);
        record_id = infoPref.getString("record_id", PrefEnum.OneSignal);

        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_action_bar_color)));

        initHashmaps();
        sharedpreference = getSharedPreferences(Constants.app_preference,
                Context.MODE_PRIVATE);
        cc = new CommonClass(context);
        font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        progressbar_login = (ProgressBar) findViewById(R.id.progressbar_login);
        /*toDateLay = (RelativeLayout) findViewById(R.id.toDateLayID);*/
        viewReport = (Button) findViewById(R.id.viewReportBtn);
        selectedDate = Calendar.getInstance();

        vehicleLinearLayout = (LinearLayout) findViewById(R.id.vehicleLinearLayout);

        vehiclegroupValue = (EditText) findViewById(R.id.vehiclegroupValue);
       /* if(vehiclegroupValue.getText().toString().equalsIgnoreCase("")){
           vehicleLinearLayout.setVisibility(View.GONE);
        }else{
            vehicleLinearLayout.setVisibility(View.VISIBLE);

        }*/
        reportValue = (EditText) findViewById(R.id.reportValue);
        chartValue = (EditText) findViewById(R.id.chartValue);
        txtFromMonthValue = (EditText) findViewById(R.id.txtFromMonthValue);
        DateLinearLayout = (LinearLayout) findViewById(R.id.DateLinearLayout);
        MonthLinearLayout = (LinearLayout) findViewById(R.id.MonthLinearLayout);
        chartValue.setText("Daily");
        chartValue.setFocusable(false);
        reportValue.setText("Summarized");
        if (reportValue.getText().toString().equalsIgnoreCase("Vehicle Details")) {

            chartValue.setText("Daily");
            checkedItemReportType=0;

        }

        if (chartValue.getText().toString().equalsIgnoreCase("Daily")) {
            DateLinearLayout.setVisibility(View.VISIBLE);
            MonthLinearLayout.setVisibility(View.GONE);
        } else {
            MonthLinearLayout.setVisibility(View.VISIBLE);
            DateLinearLayout.setVisibility(View.GONE);
        }
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
        backOnedatecalendar=Calendar.getInstance();
        backOnedatecalendar.add(Calendar.DAY_OF_MONTH,-1);
        int dayo = backOnedatecalendar.get(Calendar.DAY_OF_MONTH);
        int montho = backOnedatecalendar.get(Calendar.MONTH) + 1;
        int yearo = backOnedatecalendar.get(Calendar.YEAR);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int year = selectedDate.get(Calendar.YEAR);
        int hour = selectedDate.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDate.get(Calendar.MINUTE);
        txtFromMonthValue.setText(Constant.getMonth(month) + " " + year);
        SelectedMonth= month -1;
        SelectedYear= year;
        arrayList_type = new ArrayList<>();
        arrayList_vehicle = new ArrayList<>();
        arrayList_vehicle_group = new ArrayList<>();
        positionClickListener = (PositionClickListener) this;
        cl = (ClickListner) this;
        vehicle_report_type = new ArrayList<String>();
        vehicle_report_type.add("Summarized");
        vehicle_report_type.add("Vehiclewise");
        vehicle_report_type.add("Vehicledetail");
        vehicle_chart_type = new ArrayList<String>();
        vehicle_chart_type.add("Monthly");
        vehicle_chart_type.add("Daily");

        arrayList_vehicle = new ArrayList<>();
        current_vehicle_status_dashboard_AllVehicle(user_type_id, id);

        new AsyncTaskToObtainTypeForDropDown().execute();
        new AsyncTaskToObtainAllDetailsForDropDown().execute();
        // new AsyncTaskToObtainGroupWiseVehicleForDropDown().execute();
       /* generateconnectionreporttwotype = new AlertTypeAdapter(context,R.layout.custom_row,arrayList_type);
        autoTextViewCustomthree.setThreshold(1);
        autoTextViewCustomthree.setAdapter(generateconnectionreporttwotype);
        autoTextViewCustomthree.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
*/


        xvehiclegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(VehicleGroupCount) != null && !String.valueOf(VehicleGroupCount).isEmpty()) {
                    VehicleGroupCount = 0;
                    vehiclegroupValue.setText("0");
                    xvehiclegroup.setVisibility(View.GONE);
                    //edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                }
            }
        });
       /* tvClearvehiclegroup.setVisibility(View.GONE);
        tvClearvehiclegroup.setTypeface(font_awesome);
        tvClearvehiclegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strVehicleGroup = edtvehiclegroupp.getText().toString();
                if (strVehicleGroup != null && !strVehicleGroup.isEmpty()) {
                    strVehicleGroup = "";
                    edtvehiclegroupp.setText(strVehicleGroup);
                    tvClearvehiclegroup.setVisibility(View.GONE);
                    //edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                }
            }
        });
        tvClearReturnIcon.setVisibility(View.GONE);
        tvClearReturnIcon.setTypeface(font_awesome);
        tvClearReturnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strReturnDate = edtReturnDate.getText().toString();
                if (strReturnDate != null && !strReturnDate.isEmpty()) {
                    strReturnDate = "";
                    edtReturnDate.setText(strReturnDate);
                    tvClearReturnIcon.setVisibility(View.GONE);
                    //edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                }
            }
        });
        edtDepDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().isEmpty()) {
                    tvClearfromIcon.setVisibility(View.VISIBLE);
                } else {
                    tvClearfromIcon.setVisibility(View.GONE);
                }
            }
        });
        edtvehiclegroupp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().isEmpty()) {
                    tvClearvehiclegroup.setVisibility(View.VISIBLE);
                } else {
                    tvClearvehiclegroup.setVisibility(View.GONE);
                }
            }
        });
        edtReturnDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().isEmpty()) {
                    tvClearReturnIcon.setVisibility(View.VISIBLE);
                } else {
                    tvClearReturnIcon.setVisibility(View.GONE);
                }
            }
        });*/
        FromDatetxt = (EditText) findViewById(R.id.txtFromValue);
        FromDatetxt.setText(dayo + "-" + montho + "-"
                + yearo);
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
      /*  edtDepDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Intent intent = new Intent(context, CalendarTabbedActivity.class);

                Bundle extras = new Bundle();
                extras.putString("call_from", "InterCityActivity");
                extras.putString("departure_date", edtDepDate.getText().toString());
                extras.putString("return_date", edtReturnDate.getText().toString());
                extras.putBoolean("is_return", false);
                intent.putExtras(extras);
                startActivityForResult(intent, 1);*//*
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
                                    FromDate.setText(day1 + "-" + month1 + "-"
                                            + year1);
                                    strDepartureDate=day1 + "-" + month1 + "-"
                                            + year1;
                                    edtDepDate.setText(day1 + "-" + month1 + "-"
                                            + year1);
                                    edtDepDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                                }
                            }
                        }, year, month, day);

                dp.show();

            }
        });

        edtReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* strDepartureDate = edtDepDate.getText().toString();
                if (strDepartureDate != null && !strDepartureDate.isEmpty()) {
                    Intent intent = new Intent(context, CalendarTabbedActivity.class);

                    Bundle extras = new Bundle();
                    extras.putString("call_from", "InterCityActivity");
                    extras.putString("departure_date", strDepartureDate);
                    extras.putString("return_date", strReturnDate);
                    extras.putBoolean("is_return", true);

                    intent.putExtras(extras);
                    startActivityForResult(intent, 1);
                }*//*

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
                                    ToDate.setText(day1 + "-" + month1 + "-"
                                            + year1);
                                    strReturnDate=day1 + "-" + month1 + "-"
                                            + year1;
                                    edtReturnDate.setText(day1 + "-" + month1 + "-"
                                            + year1);
                                    edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                                }

                            }
                        }, year, month, day);
                dp.show();
            }
        });
*/

       /* FromTime = (TextView) findViewById(R.id.timeFromValue);
        FromTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                TimePickerDialog dp = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                fromDateHour = String.valueOf(hourOfDay);
                                fromDateMinute = String.valueOf(minute);
                                FromTime.setText(fromDateHour + ":" + fromDateMinute);
                            }
                        }, hour, minute, true);
                dp.show();
            }
        });

        ToTime = (TextView) findViewById(R.id.timeToValue);
        ToTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                TimePickerDialog dp = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                toDateHour = String.valueOf(hourOfDay);
                                toDateMinute = String.valueOf(minute);
                                ToTime.setText(toDateHour + ":" + toDateMinute);
                            }
                        }, hour, minute, true);
                dp.show();
            }
        });*/
        //fn_getAllViewGroup(user_type_id,id);
        //Log.e("vehicle_group size"+arrayList_vehicle_group.size(),"");

        reportValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose Report Type");

// add a radio button list

                String[] animals = {"Summarized", "Vehiclewise", "Vehicle Details"};
                final int checkedItem = 0; // cow

                builder.setSingleChoiceItems(animals, checkedItemSummary_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                        // radioButton = (RadioButton) findViewById(which);
                        //  String ss= radioButton.getText().toString();
                        ReportType = "";
                        checkedItemSummary_type = which;
                        //Toast.makeText(context, "click" + which, Toast.LENGTH_LONG).show();
                        switch (which) {
                            case 0: // horse
                                ReportType = "Summarized";
                                break;
                            case 1: // cow
                                ReportType = "Vehiclewise";
                                break;
                            case 2: // camel
                                ReportType = "Vehicle Details";
                                break;


                        }
                    }
                });

// add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                        reportValue.setText(ReportType);
                        if (reportValue.getText().toString().equalsIgnoreCase("Vehicle Details")) {

                            chartValue.setText("Daily");
                            checkedItemReportType=0;
                        }
                        if (chartValue.getText().toString().equalsIgnoreCase("Daily")) {
                            DateLinearLayout.setVisibility(View.VISIBLE);
                            MonthLinearLayout.setVisibility(View.GONE);

                        } else {
                            MonthLinearLayout.setVisibility(View.VISIBLE);
                            DateLinearLayout.setVisibility(View.GONE);
                        }
                        ReportValue=reportValue.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        txtFromMonthValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
                pd.setListener(VehicleIdleReportActivity.this);
                pd.show(getFragmentManager(), "MonthYearPickerDialog");
            }
        });

        chartValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reportValue.getText().toString().equalsIgnoreCase("Vehicle Details")) {
                    ChartType = "D";
                }
                else {
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Choose Summary Type");

// add a radio button list

                    if (ReportType.equalsIgnoreCase("Vehicle Details")) {
                        String[] animals = {"Daily"};
                        int checkedItem = 0; // cow

                        builder.setSingleChoiceItems(animals, checkedItemReportType, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Chartypen = "";
                                ChartType = "";
                                checkedItemReportType = which;
                                switch (which) {
                                    case 0: // horse
                                        Chartypen = "Daily";
                                        ChartType = "D";
                                        break;
                                    case 1: // horse
                                        Chartypen = "Monthly";
                                        ChartType = "M";
                                        break;
                                }
                            }
                        });

// add OK and Cancel buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user clicked OK
                                chartValue.setText(Chartypen);

                                if (ChartType.equalsIgnoreCase("M")) {
                                    MonthLinearLayout.setVisibility(View.VISIBLE);
                                    DateLinearLayout.setVisibility(View.GONE);
                                } else {
                                    MonthLinearLayout.setVisibility(View.GONE);
                                    DateLinearLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();


                    } else {
                        String[] animals = {"Daily", "Monthly"};
                        int checkedItem = 0; // cow

                        builder.setSingleChoiceItems(animals, checkedItemReportType, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                // radioButton = (RadioButton) findViewById(which);
                                //  String ss= radioButton.getText().toString();
                                Chartypen = "";
                                ChartType = "";
                                checkedItemReportType = which;
                                switch (which) {
                                    case 0: // horse
                                        Chartypen = "Daily";
                                        ChartType = "D";
                                        break;
                                    case 1: // horse
                                        Chartypen = "Monthly";
                                        ChartType = "M";
                                        break;


                                }
                            }
                        });

// add OK and Cancel buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user clicked OK
                                chartValue.setText(Chartypen);

                                if (ChartType.equalsIgnoreCase("M")) {
                                    MonthLinearLayout.setVisibility(View.VISIBLE);
                                    DateLinearLayout.setVisibility(View.GONE);
                                } else {
                                    MonthLinearLayout.setVisibility(View.GONE);
                                    DateLinearLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        });
        xvehicl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(VehicleCount) != null && !String.valueOf(VehicleCount).isEmpty()) {
                    VehicleGroupCount = 0;
                    vehicleValue.setText("Please Select Vehicle");
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


        xvehiclegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(VehicleGroupCount) != null && !String.valueOf(VehicleGroupCount).isEmpty()) {
                    VehicleGroupCount = 0;
                    vehiclegroupValue.setText("0");
                    xvehiclegroup.setVisibility(View.GONE);
                    //edtReturnDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                }
            }
        });
        vehiclegroupValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList_vehicle_group.size() > 0) {
                    vehicleGroupDialog.show(getFragmentManager(), Constants.IReport.VEHICLE_GROUP);
                } else {
                    Toast.makeText(context, "Please wait while loading", Toast.LENGTH_LONG).show();
                }

            }
        });
        vehicleValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList_vehicle.size() > 0) {
                    vehiclesDialog.show(getFragmentManager(), Constants.IReport.VEHICLES);
                } else {
                    Toast.makeText(context, "Please wait while loading ", Toast.LENGTH_LONG).show();
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
                if (chartValue.getText().toString().equalsIgnoreCase("Daily")) {
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

                        cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Select From date and To date.", "Ok", "", false);

                    } else if (FromDate.compareTo(ToDate) > 0) {
                        cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "To Date should be after From Date", "Ok", "", false);

                    }
                    else if (reportValue.getText().toString().equalsIgnoreCase("Summarized")) {
                        viewReport.setClickable(false);
                        viewReport.setEnabled(false);
                        viewReport.setBackgroundResource(R.drawable.button_boarder_disable);

                        getAlertReport(user_type_id, id, NewFromDateString,
                                NewToDateString, reportValue.getText().toString(),
                                ChartType, getSelectedVehicleGroupIds(),
                                getSelectedVehicleIds());
                    }
                    else{

                    if (vehicleValue.getText().toString().equalsIgnoreCase("")) {

                        cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Vehicle", "Ok", "", false);

                    }
                    else {

                    if (VehicleCount == 0) {
                        cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Vehicle", "Ok", "", false);

                    } else if (vehicleValue.getText().toString().equalsIgnoreCase("Please Select Vehicle")) {
                        cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Vehicle", "Ok", "", false);
                    }
                    else {
                        if(reportValue.getText().toString().equalsIgnoreCase("Vehicle Details")){

                            ReportValue="Vehicledetail";
                        }
                        viewReport.setClickable(false);
                        viewReport.setEnabled(false);
                        viewReport.setBackgroundResource(R.drawable.button_boarder_disable);

                        getAlertReport(user_type_id, id, NewFromDateString,
                                NewToDateString,
                                ReportValue, ChartType,
                                getSelectedVehicleGroupIds(),
                                getSelectedVehicleIds());
                    }
                    }
                    }

                }
                else {

                    Calendar cal = Calendar.getInstance();//getting the instance of the Calendar using the factory method

                    int year = cal.get(Calendar.YEAR);//for example we get 2013 here

                    cal.set(SelectedYear, SelectedMonth, 1);
                    Date firstdate = cal.getTime();//here we will get the first day of the year

                    cal.set(SelectedYear, SelectedMonth, 31);//same way as the above we set the end date of the year

                    Date lastdate = cal.getTime();//here we will get the last day of the year
                    SimpleDateFormat outFormatMonth = new SimpleDateFormat("yyyy-MM-dd");
                    NewFromDateString = outFormatMonth.format(firstdate);
                    NewToDateString = outFormatMonth.format(firstdate);

                    System.out.print("the firstdate and lastdate here\n");

                    if (reportValue.getText().toString().equalsIgnoreCase("Summarized")) {
                        if(ChartType.equalsIgnoreCase("M")) {
                            if (txtFromMonthValue.getText().toString().equalsIgnoreCase("")) {
                                cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Month", "Ok", "", false);

                            } else if (txtFromMonthValue.getText().toString().equalsIgnoreCase("Select Month")) {
                                cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Month", "Ok", "", false);

                            }
                            else {
                                viewReport.setClickable(false);
                                viewReport.setEnabled(false);
                                viewReport.setBackgroundResource(R.drawable.button_boarder_disable);
                                getAlertReport(user_type_id, id, NewFromDateString,
                                        NewToDateString, reportValue.getText().toString(),
                                        ChartType, getSelectedVehicleGroupIds(),
                                        getSelectedVehicleIds());
                            }
                        }
                    }
                    else {
                        if (VehicleCount == 0) {
                            cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Vehicle", "Ok", "", false);

                        }
                        else if (vehicleValue.getText().toString().equalsIgnoreCase("Please Select Vehicle")) {
                            cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Vehicle", "Ok", "", false);
                        }
                        else if(ChartType.equalsIgnoreCase("M")){
                            if(txtFromMonthValue.getText().toString().equalsIgnoreCase("")){
                                cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Month", "Ok", "", false);

                            }
                            else if(txtFromMonthValue.getText().toString().equalsIgnoreCase("Select Month")){
                                cc.CommonDialog(VehicleIdleReportActivity.this, "notification_click", "", "Please Select Month", "Ok", "", false);

                            }else{
                                if (reportValue.getText().toString().equalsIgnoreCase("Vehicle Details")) {

                                    ReportValue = "Vehicledetail";
                                }
                                viewReport.setClickable(false);
                                viewReport.setEnabled(false);
                                viewReport.setBackgroundResource(R.drawable.button_boarder_disable);

                                getAlertReport(user_type_id, id, NewFromDateString,
                                        NewToDateString,
                                        ReportValue, ChartType,
                                        getSelectedVehicleGroupIds(),
                                        getSelectedVehicleIds());

                            }
                        }

                        else {
                            if (reportValue.getText().toString().equalsIgnoreCase("Vehicle Details")) {

                                ReportValue = "Vehicledetail";
                            }
                            viewReport.setClickable(false);
                            viewReport.setEnabled(false);
                            viewReport.setBackgroundResource(R.drawable.button_boarder_disable);

                            getAlertReport(user_type_id, id, NewFromDateString,
                                    NewToDateString,
                                    ReportValue, ChartType,
                                    getSelectedVehicleGroupIds(),
                                    getSelectedVehicleIds());
                        }
                    }
                }
            }
        });

    }


    public void getAlertReport(String usertype_id, final String user_id, final String fromdate,
                               final String todate, final String report_type, final String chart_type,
                               final String vehicle_group, final String vehicle) {
        try {

            Log.e(TAG, "usertype_id: " + usertype_id);
            Log.e(TAG, "user_id: " + user_id);
            Log.e(TAG, "fromdate: " + fromdate);
            Log.e(TAG, "todate: " + todate);
            Log.e(TAG, "report_type: " + report_type);
            Log.e(TAG, "chart_type: " + chart_type);
            Log.e(TAG, "vehicle_group: " + vehicle_group);
            Log.e(TAG, "vehicle: " + vehicle);

            progressbar_login.setVisibility(View.VISIBLE);

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.getVehicleIdleReport(auth, apiKey, usertype_id,
                    user_id, fromdate, todate, report_type, chart_type, vehicle_group, vehicle);
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
                        Log.e(TAG, "Response code" + strResponceCode);
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
                                            new AlertDialog.Builder(VehicleIdleReportActivity.this)
                                                    .setMessage("Report Successfully Send To Your Registered Email ID ")
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
            Log.e("", "Api fail");
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
                        Log.e("", "Response code" + strResponceCode);
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


                                                if (Constant.arraylist_position.contains(i)) {
                                                    vehiclePojo.setSelected(true);
                                                } else {
                                                    vehiclePojo.setSelected(false);
                                                }


                                                vehiclePojo.setVehicle_id(vehicle_id);
                                                vehiclePojo.setVehicle_no(vehicle_no);


                                                Constant.arrayList_vehicle.add(vehiclePojo);
                                                arrayList_vehicle.add(vehiclePojo);

                                                Log.e("size_ofarraylist", String.valueOf(Constant.arrayList_vehicle.size()));
                                                //alertvehicleadpater = new AlertVehicleAdapter(context, R.layout.custom_row_vehicle, Constant.arrayList_vehicle);


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


                        Log.e("pojo_arraylist", String.valueOf(Constant.arraylist_position.size()));


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
            Log.e("", "Api fail");
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
                        Log.e("", "Response code" + strResponceCode);
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

                                                    if (Constant.arraylist_position_t.contains(i)) {
                                                        notificatiobPojo.setSelected(true);
                                                    } else {
                                                        notificatiobPojo.setSelected(false);
                                                    }

                                                    notificatiobPojo.setTitle(title);
                                                    notificatiobPojo.setNotification_id(notification_id);
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
            Log.e("", "Api fail");
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
                        Log.e("", "Response code" + strResponceCode);
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
            Log.e("", "Api fail");
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
                        Log.e("", "Response code" + strResponceCode);
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

                                                    if (Constant.arraylist_position_t.contains(i)) {
                                                        notificatiobPojo.setSelected(true);
                                                    } else {
                                                        notificatiobPojo.setSelected(false);
                                                    }

                                                    notificatiobPojo.setTitle(title);
                                                    notificatiobPojo.setNotification_id(notification_id);
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
            Log.e("", "Api fail");
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
            vehicleLinearLayout.setVisibility(View.VISIBLE);
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

        // SetCount(count);
    }

    private void SetCount(int count) {


    }

    private class AsyncTaskToObtainAllDetailsForDropDown extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

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
        private ProgressDialog progressDialog;

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


    public void current_vehicle_status_dashboard_AllVehicle(String usertype_id, final String user_id) {
        try {
            Utils.showProgressDialog(VehicleIdleReportActivity.this, "Please Wait");

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
                        Log.e("", "Response code" + strResponceCode);
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

                                                Log.e("size_ofarraylist", String.valueOf(Constant.arrayList_vehicle.size()));

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
            Log.e("", "Api fail");
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

        vehicleGroupDialog = new ReportCustomDialog(VehicleIdleReportActivity.this, Constants.IReport.VEHICLE_GROUP,
                vehicleGroupHashmap, this);
    }

    private void setVehiclesHashmap() {
        for (int i = 0; i < arrayList_vehicle.size(); i++) {
            vehiclesHashmap.put(arrayList_vehicle.get(i).getVehicle_no(), false);
        }
        Log.d(TAG, "setVehicleGroupHashmap: vehiclesHashmap size: " + vehiclesHashmap.size()
                + " arrayListsize: " + arrayList_vehicle.size());

        vehiclesDialog = new ReportCustomDialog(VehicleIdleReportActivity.this, Constants.IReport.VEHICLES,
                vehiclesHashmap, this);
    }

    private void setAlertHashmap() {
        for (int i = 0; i < arrayList_type.size(); i++) {
            alertHashmap.put(arrayList_type.get(i).getTitle(), false);
        }
        Log.d(TAG, "setAlertHashmap: alertHashmap size: " + alertHashmap.size()
                + " arrayListsize: " + arrayList_type.size());

        alertDialog = new ReportCustomDialog(VehicleIdleReportActivity.this, Constants.IReport.ALERT,
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
            Log.d(TAG, "VehicleGroup: " + VehicleGroup);
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
                    //vehicleIds = "[" + getVehicleId(s);
                    vehicleIds = getVehicleId(s);
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

        /*String[] value = new String[alertHashmap.size()];
        int count = 0;
        Log.d(TAG, "getAlertTypeId: " + value);
        for (String s : alertHashmap.keySet()) {
            if (alertHashmap.get(s)) {
                if (vehicleIds.trim().equalsIgnoreCase("")) {
                    vehicleIds = "[" + getAlertTypeId(s);
                } else {
                    vehicleIds = vehicleIds + "," + getAlertTypeId(s);
                }
                value[count++] = getAlertTypeId(s);
            }
        }
        if (vehicleIds.trim().equalsIgnoreCase(""))
            vehicleIds = vehicleIds + "[]";
        else
            vehicleIds = vehicleIds + "]";

        Log.d(TAG, "getAlertTypeId: " + vehicleIds);
        Log.d(TAG, "getAlertTypeId: " + value.toString());*/
        return vehicleIds;
    }


    private String getSelectedVehicleGroupIds() {
        String vehicleIds = "";
        VehicleGroup = "";
        for (String s : vehicleGroupHashmap.keySet()) {
            if (vehicleGroupHashmap.get(s)) {
                if (vehicleIds.trim().equalsIgnoreCase("")) {
                   // vehicleIds = "[" + getVehicleGroupId(s);
                    vehicleIds = getVehicleGroupId(s);
                    VehicleGroup = getVehicleGroupId(s);
                } else {
                    vehicleIds = vehicleIds + "," + getVehicleGroupId(s);
                    VehicleGroup = VehicleGroup + "," + getVehicleGroupId(s);
                }
            }
        }
       /* if (vehicleIds.trim().equalsIgnoreCase(""))
            vehicleIds = vehicleIds + "[]";
        else
            vehicleIds = vehicleIds + "]";
*/
        Log.d(TAG, "selectedVehicleGroupId: " + vehicleIds);
        return vehicleIds;
    }
    @SuppressLint("ValidFragment")
    class MonthYearPickerDialog extends DialogFragment {

        String[] MONTH = {"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMNBER","DECEMBER"}; //etc

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        private static final int MAX_YEAR = 2099;
        private DatePickerDialog.OnDateSetListener listener;

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            Calendar cal = Calendar.getInstance();

            View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
            final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

            monthPicker.setMinValue(localCalendar.get(Calendar.MONTH));
            monthPicker.setMaxValue(localCalendar.get(Calendar.MONTH) + 1);
            monthPicker.setDisplayedValues(MONTH);
//            monthPicker.setValue(localCalendar.get(Calendar.MONTH));


            int year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(2015);
            yearPicker.setMaxValue(year);
            yearPicker.setValue(year);


            yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int oldvalue, int newvalue) {

                    //Toast.makeText(VehicleIdleReportActivity.this, "old__" + oldvalue, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(VehicleIdleReportActivity.this, "new__" + newvalue, Toast.LENGTH_SHORT).show();

                    if (localCalendar.get(Calendar.YEAR) <= newvalue) {
                        monthPicker.setMinValue(1);
                        monthPicker.setMaxValue(localCalendar.get(Calendar.MONTH) + 1);
                        monthPicker.setDisplayedValues(MONTH);
                    } else {
                        monthPicker.setMinValue(1);
                        monthPicker.setMaxValue(12);
                        monthPicker.setDisplayedValues(MONTH);
                    }

                }
            });



            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MonthYearPickerDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }



    @Override
    public void onDateSet(DatePicker datePicker, int month, int year, int i2) {

      //  Toast.makeText(this, "YEAR-" + year + "___" + "MONTH-" + month, Toast.LENGTH_SHORT).show();
        SelectedYear=month;
        SelectedMonth=year;
        txtFromMonthValue.setText(month + " " + Constant.getMonth(year));

    }


}
