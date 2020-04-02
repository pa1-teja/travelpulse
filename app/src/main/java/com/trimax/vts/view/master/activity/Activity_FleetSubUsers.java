package com.trimax.vts.view.master.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trimax.vts.view.master.adapter.Adapter_FeetUser;
import com.trimax.vts.view.master.model.FleetSubUsers.FleetSubUsersModel;
import com.trimax.vts.view.master.model.FleetSubUsers.FleetSubUsersModelResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

/**
 * Created by kiranp on 5/11/2018.
 */

public class Activity_FleetSubUsers extends BaseActivity {

    private RecyclerView listView;
    private Adapter_FeetUser feetUserAdapter;
    List<FleetSubUsersModel> fleetSubUsersModels;
    private Context mContext;
    private String TAG = Activity_FleetSubUsers.class.getSimpleName();
    private String strId, strUserId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feeltusers);
        mContext = this;
        InitilizeView();
    }

    private void InitilizeView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.road_pulse_action_bar_color)));


        FloatingActionButton fab = findViewById(R.id.fab);
        listView = findViewById(R.id.ListView);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        strId = sharedPreferences.getString("id", "");
        strUserId = sharedPreferences.getString(getString(R.string.user_type_id), "");

        fleetSubUsersModels = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        listView.setLayoutManager(mLayoutManager);
        listView.setHasFixedSize(true);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setNestedScrollingEnabled(false);

        feetUserAdapter = new Adapter_FeetUser((ArrayList<FleetSubUsersModel>) fleetSubUsersModels, mContext, 0);
        listView.setAdapter(feetUserAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_FleetSubUsers.this, AddFleetSubUserActivity.class);
                intent.putExtra(Constants.ISubUser.ACTION,"add");
                startActivity(intent);
                finish();
            }
        });
        progressDialogStart(this, "Please wait...");
        fnGetDashBoardDetails();
    }

    private void fnGetDashBoardDetails() {
        try {
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<FleetSubUsersModelResponse> call = objRetrofitInterface.fnGetAllFeeltUserDetails(auth, apiKey, strId, strUserId);
            call.enqueue(new Callback<FleetSubUsersModelResponse>() {
                @Override
                public void onResponse(Call<FleetSubUsersModelResponse> call, Response<FleetSubUsersModelResponse> response) {
                    try {
                        progressDialogStop();

                        fleetSubUsersModels = response.body().getData();
                        Log.d(TAG, "fleetSubUsersModels : "+fleetSubUsersModels.size());

                        feetUserAdapter = new Adapter_FeetUser((ArrayList<FleetSubUsersModel>) fleetSubUsersModels, mContext, 0);
                        listView.setAdapter(feetUserAdapter);

                    } catch (Exception ex) {
                        progressDialogStop();
                        Log.e(TAG, "This is an error");
                    }
                }

                @Override
                public void onFailure(Call<FleetSubUsersModelResponse> call, Throwable t) {
                    progressDialogStop();
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "Api fail");
        }
    }

    public static void StartActivity(Context context) {
        context.startActivity(new Intent(context, Activity_FleetSubUsers.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drivers, menu);
        MenuItem item = menu.findItem(R.id.search);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                feetUserAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    public void updateList(String subUserId) {
        for (FleetSubUsersModel model : fleetSubUsersModels) {
            if (model.getId().trim().equalsIgnoreCase(subUserId.trim())) {
                fleetSubUsersModels.remove(model);
                feetUserAdapter.notifyDataSetChanged();
                break;
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
}
