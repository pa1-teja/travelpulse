package com.trimax.vts.view.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.trimax.vts.receiver.AlarmReceiver;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.R;
import com.trimax.vts.view.model.NotificationSettingModel;
import com.trimax.vts.view.notifications.adapter.NotificationSettingAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NotificationSettingActi";
    private Chip cp_devices,cp_os,cp_third_party_apps;
    private SearchView searchView;
    private List<NotificationSettingModel> settings;
    private NotificationSettingAdapter adapter;
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Notification Settings");

        infoPref = new TravelpulseInfoPref(this);
        settings = new ArrayList<>();
        settings = NotificationSettingProvider.getOSWiseSettings();

        Switch auto_start = findViewById(R.id.auto_start);
        RecyclerView rv_settings = findViewById(R.id.rv_settings);
        rv_settings.setLayoutManager(new LinearLayoutManager(this));
        rv_settings.setHasFixedSize(true);
        adapter = new NotificationSettingAdapter(settings,this);
        rv_settings.setAdapter(adapter);

        ChipGroup chip_group = findViewById(R.id.chip_group);
        cp_devices = findViewById(R.id.cp_devices);
        cp_third_party_apps = findViewById(R.id.cp_third_party_apps);
        cp_os = findViewById(R.id.cp_os);
        cp_os.setTextColor(Color.WHITE);
        cp_os.setOnClickListener(this);
        cp_devices.setOnClickListener(this);
        cp_third_party_apps.setOnClickListener(this);
        auto_start.setOnCheckedChangeListener(null);
        auto_start.setChecked(infoPref.getBoolean(infoPref.APP_ALARM,PrefEnum.Login));

        auto_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    scheduleAlarm();
                }else {
                    infoPref.putBoolean(infoPref.APP_ALARM,false, PrefEnum.Login);
                    cancelAlarm();
                }
            }
        });
        chip_group.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                if (i == R.id.cp_devices){
                    settings = NotificationSettingProvider.getDeviceWiseSettings();
                    adapter.addSettings(settings);
                }else if (i==R.id.cp_os){
                    settings = NotificationSettingProvider.getOSWiseSettings();
                    adapter.addSettings(settings);
                }else {
                    settings = NotificationSettingProvider.getAppWiseSettings();
                    adapter.addSettings(settings);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search by android os");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length()>0){
                    ArrayList<NotificationSettingModel> filterList = new ArrayList<>();
                    for (int i = 0; i <settings.size() ; i++) {
                        NotificationSettingModel model = settings.get(i);
                        if (model.getTitle().toLowerCase().contains(s.toLowerCase()))
                            filterList.add(model);
                        adapter.addSettings(filterList);
                    }
                }else {
                    adapter.addSettings(settings);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void changeCheckedChip(int id){
        if (id == R.id.cp_devices){
            cp_devices.setChecked(true);
            cp_devices.setTextColor(Color.WHITE);
            cp_os.setTextColor(Color.BLACK);
            cp_third_party_apps.setTextColor(Color.BLACK);
            cp_os.setChecked(false);
            cp_third_party_apps.setChecked(false);
            searchView.setQueryHint("Search by device");
        }else if (id == R.id.cp_os){
            cp_devices.setChecked(false);
            cp_os.setTextColor(Color.WHITE);
            cp_devices.setTextColor(Color.BLACK);
            cp_third_party_apps.setTextColor(Color.BLACK);
            cp_os.setChecked(true);
            cp_third_party_apps.setChecked(false);
            searchView.setQueryHint("Search by Android OS");
        }else {
            cp_devices.setChecked(false);
            cp_os.setChecked(false);
            cp_third_party_apps.setTextColor(Color.WHITE);
            cp_devices.setTextColor(Color.BLACK);
            cp_os.setTextColor(Color.BLACK);
            cp_third_party_apps.setChecked(true);
            searchView.setQueryHint("Search by app");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_setting){
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }else {
            //Chip selection
            changeCheckedChip(view.getId());
        }
    }

    private void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        infoPref.putBoolean(infoPref.APP_ALARM,true, PrefEnum.Login);
        assert alarm != null;
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, (20*60000), pIntent);
        Log.d(TAG, "scheduleAlarm: "+infoPref.getBoolean(infoPref.APP_ALARM,PrefEnum.Login));
        //Send notification from device
        Utils.scheduleJob(this);
    }

    private void cancelAlarm(){
        JobScheduler mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int JOB_ID=101;
        assert mScheduler != null;
        mScheduler.cancel(JOB_ID);
        Intent cancelIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, cancelIntent, 0);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert am != null;
        am.cancel(cancelPendingIntent);
    }
}
