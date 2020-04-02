package com.trimax.vts.view.complaints;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.complaints.models.CommentModel;
import com.trimax.vts.view.complaints.models.CommentResponse;
import com.trimax.vts.view.complaints.models.DataSaveResponse;
import com.trimax.vts.view.complaints.models.TicketModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CommentActivity";
    private RecyclerView rv_issues;
    private TextView tv_ticket_no;
    private ImageView iv_back;
    private CardView cv_comment;
    private CheckBox cb_close_issue;
    private TextInputEditText tie_comment;
    private String ticketId="",customerId="";
    private TravelpulseInfoPref infoPref;
    private TicketModel model;
    private CommentAdapter adapter;
    private List<CommentModel> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        if (getSupportActionBar()!=null)
            getSupportActionBar().hide();
        infoPref = new TravelpulseInfoPref(this);
        tv_ticket_no = findViewById(R.id.tv_ticket_no);
        iv_back = findViewById(R.id.iv_back);
        rv_issues = findViewById(R.id.rv_issues);
        tie_comment = findViewById(R.id.tie_comment);
        cb_close_issue = findViewById(R.id.cb_close_issue);
        cv_comment = findViewById(R.id.cv_comment);

        rv_issues.setLayoutManager(new LinearLayoutManager(this));
        rv_issues.setHasFixedSize(true);
        rv_issues.setItemAnimator(new DefaultItemAnimator());

        adapter = new CommentAdapter(comments);
        rv_issues.setAdapter(adapter);

        model = (TicketModel) getIntent().getSerializableExtra("ticket");
        boolean isComment= getIntent().getBooleanExtra("isComment",false);
        if (!isComment)
            cv_comment.setVisibility(View.GONE);
        if (model!=null){
            ticketId=model.getId();
            tv_ticket_no.setText(model.getTicketNo());
        }
        customerId = infoPref.getString("id", PrefEnum.Login);

        iv_back.setOnClickListener(this);
        tie_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (tie_comment.getRight() - tie_comment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        String comment = tie_comment.getText().toString();
                        boolean status = cb_close_issue.isChecked();
                        if (!TextUtils.isEmpty(comment)){
                            saveComment(comment,status?"close":"reopen");
                        }else {
                            tie_comment.setError("Required.");
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void saveComment(String comment,String status){
        Call<DataSaveResponse> call = RepositryInstance.getTicketRepository().saveTicketComment(customerId,ticketId,comment,status,"");
        call.enqueue(new Callback<DataSaveResponse>() {
            @Override
            public void onResponse(Call<DataSaveResponse> call, Response<DataSaveResponse> response) {
                if (response.isSuccessful()){
                    DataSaveResponse data = response.body();
                    getComments(ticketId);
                    tie_comment.setText("");
                    Toast.makeText(CommentActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataSaveResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(CommentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getComments(ticketId);
    }

    private void getComments(String ticketId){
        Call<CommentResponse> call = RepositryInstance.getTicketRepository().getTicketComments(ticketId);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()){
                    CommentResponse data = response.body();
                    if (data.getStatus().equalsIgnoreCase("success")){
                        comments = data.getData();
                       /* for (int i = 0; i <comments.size() ; i++) {
                            comments.get(i).setMobile(infoPref.getString("mobile_number", PrefEnum.Login));
                        }*/
                        adapter.addComments(comments);
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Toast.makeText(CommentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
