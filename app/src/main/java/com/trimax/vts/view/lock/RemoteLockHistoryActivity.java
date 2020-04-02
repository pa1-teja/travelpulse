package com.trimax.vts.view.lock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.trimax.vts.view.R;

import java.util.ArrayList;
import java.util.List;

public class RemoteLockHistoryActivity extends AppCompatActivity {
    private static final String TAG = "RemoteLockHistoryActivi";
    private RecyclerView rv_requests;
    private LockRequestAdapter adapter;
    private List<RequestModel> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_lock_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("MH-12 0A1234 - History");

        requests = new ArrayList<>();
        requests.add(new RequestModel("Lock","12:20:30","Processing",""));
        requests.add(new RequestModel("Unlock","12:20:30","Success",""));
        requests.add(new RequestModel("Lock","12:20:30","Success",""));
        requests.add(new RequestModel("Unlock","12:20:30","Success",""));
        requests.add(new RequestModel("Unlock","12:20:30","Fail",""));
        requests.add(new RequestModel("Lock","12:20:30","Success",""));
        requests.add(new RequestModel("Unlock","12:20:30","Success",""));
        requests.add(new RequestModel("Unlock","12:20:30","Fail",""));

        rv_requests = findViewById(R.id.rv_requests);
        rv_requests.setHasFixedSize(true);
        rv_requests.setItemAnimator(new DefaultItemAnimator());
        rv_requests.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LockRequestAdapter(requests);
        rv_requests.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
