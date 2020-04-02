package com.trimax.vts.view.complaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.complaints.models.TicketModel;
import com.trimax.vts.view.complaints.models.TicketsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerQueriesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CustomerQueriesActivity";
    private FloatingActionButton fab_add_query;
    private RecyclerView rv_complaints;
    private ProgressBar pb_progress;
    private List<TicketModel> complaints = new ArrayList<>();
    private ComplaintAdapter adapter;
    private TravelpulseInfoPref infoPref;
    private String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_queries);
        setTitle("My Complaints");
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        infoPref = new TravelpulseInfoPref(this);
        userId = infoPref.getString("id",PrefEnum.Login);

        fab_add_query = findViewById(R.id.fab_add_query);
        pb_progress = findViewById(R.id.pb_progress);
        rv_complaints = findViewById(R.id.rv_complaints);

        rv_complaints.setLayoutManager(new LinearLayoutManager(this));
        rv_complaints.setHasFixedSize(true);
        rv_complaints.setItemAnimator(new DefaultItemAnimator());
        adapter = new ComplaintAdapter(complaints,this);
        rv_complaints.setAdapter(adapter);
        fab_add_query.setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchComplaints();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_query:
                startActivity(new Intent(this,AddQueryActivity.class));
                break;
            case R.id.iv_reply:
                TicketModel model = (TicketModel) view.getTag();
                startActivity(new Intent(this,CommentActivity.class).putExtra("ticket",model).putExtra("isComment",true));
                break;
            case R.id.iv_details:
                model = (TicketModel) view.getTag();
                startActivity(new Intent(this,CommentActivity.class).putExtra("ticket",model));
                break;
        }
    }

    private void fetchComplaints() {
        pb_progress.setVisibility(View.VISIBLE);
        Call<TicketsResponse> call = RepositryInstance.getTicketRepository().getTickets(userId);
        call.enqueue(new Callback<TicketsResponse>() {
            @Override
            public void onResponse(Call<TicketsResponse> call, Response<TicketsResponse> response) {
                pb_progress.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    TicketsResponse data = response.body();
                    if (data.getData()!=null){
                        complaints = data.getData();
                        adapter.addComplaints(complaints);
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketsResponse> call, Throwable t) {
                pb_progress.setVisibility(View.GONE);
                Toast.makeText(CustomerQueriesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
