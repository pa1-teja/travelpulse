package com.trimax.vts.view.faq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trimax.vts.view.R;
import com.trimax.vts.view.databinding.ActivityFaqBinding;

import java.util.ArrayList;
import java.util.List;

public class FaqActivity extends AppCompatActivity {
    private static final String TAG = "FaqActivity";
    private ActivityFaqBinding binding;
    private List<FaqModel> faqs = new ArrayList<>();
    private FaqAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_faq);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("FAQ");

        binding.rvFaqs.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFaqs.setItemAnimator(new DefaultItemAnimator());
        binding.rvFaqs.setHasFixedSize(true);
        adapter = new FaqAdapter(faqs);
        binding.rvFaqs.setAdapter(adapter);
        setFaqs();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setFaqs() {
        faqs.add(new FaqModel("1. Why Travelpulse application keep crashing? ","1. Restart the Travelpulse app. \n2. Clear Travelpulse application cache and app data. \n3. Reboot your mobile. \n4. Reinstall Travelpulse application."));
        faqs.add(new FaqModel("2. Not receiving notification and alarm? ","First make sure notification preference is set, if not set it. \nSelect menu->Manage master -> Vehicle -> click on list icon -> Notification Preference and set preferences."));
        faqs.add(new FaqModel("3. Receiving delayed notifications? ","Some android phones kill application running in background. To fix this problem, you have do some phone wise setting mentioned in notification settings at top right of Home screen."));
        faqs.add(new FaqModel("4. Not getting Parking alarm? ","First make sure Parking mode is enabled for the vehicle. If not enable, then enable it by using below steps \n1. Go to Menu and Select Manage Master.\n2. Select Vehicles from master. \n3. Search vehicle number and enable parking mode."));
        faqs.add(new FaqModel("5. How to enable or disable Parking mode?","To enable Parking mode follow the steps as below:\n1. Go to Menu and Select Manage Master.\n2. Select Vehicles from master. \n3. Search vehicle number and enable or disable Parking mode for particular vehicle."));
        faqs.add(new FaqModel("6. Why my vehicle is in No Network?","Vehicle goes in no network due to LOW/NO SIGNAL or no GPS at specific location. \nIt will back to Network when it gets proper Network and GPS."));
        adapter.addFaqs(faqs);
    }

}
