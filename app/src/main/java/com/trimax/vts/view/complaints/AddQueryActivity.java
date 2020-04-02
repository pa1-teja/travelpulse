package com.trimax.vts.view.complaints;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.BuildConfig;
import com.trimax.vts.view.R;
import com.trimax.vts.view.complaints.models.DataSaveResponse;
import com.trimax.vts.view.complaints.models.TicketType;
import com.trimax.vts.view.complaints.models.TicketTypeResponse;
import com.trimax.vts.view.vehicle.VTSSetVehicalActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQueryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddQueryActivity";
    private Spinner sp_problem;
    private ProgressBar pb_progress;
    private TextInputEditText tie_from,tie_mobile_model,tie_vehicle_no,tie_description,tie_mobile_no;
    private ImageView iv_upload;
    private MaterialButton btn_submit;

    private final NumberFormat format = new DecimalFormat("00");
    private final Calendar cal = Calendar.getInstance();
    private String selectedDate="",vehicleNo="",vehicleId="",mobileNo="",userId="";
    private TravelpulseInfoPref infoPref;
    private List<TicketType> ticketTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_query);
        setTitle("Add Complaint");
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        infoPref = new TravelpulseInfoPref(this);
        mobileNo = infoPref.getString("mobile_number", PrefEnum.Login);
        vehicleNo = infoPref.getString("vno",PrefEnum.Login);
        vehicleId = infoPref.getString("vid",PrefEnum.Login);
        userId = infoPref.getString("id",PrefEnum.Login);
        sp_problem = findViewById(R.id.sp_problem);
        tie_from = findViewById(R.id.tie_from);
        pb_progress = findViewById(R.id.pb_progress);
        tie_mobile_model = findViewById(R.id.tie_mobile_model);
        tie_vehicle_no = findViewById(R.id.tie_vehicle_no);
        tie_description = findViewById(R.id.tie_description);
        tie_mobile_no = findViewById(R.id.tie_mobile_no);
        iv_upload = findViewById(R.id.iv_upload);
        btn_submit = findViewById(R.id.btn_submit);

        tie_mobile_no.setText(mobileNo);
        tie_vehicle_no.setText(vehicleNo);
        tie_from.setOnClickListener(this);
        tie_vehicle_no.setOnClickListener(this);
        iv_upload.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        getTicketTypes();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tie_from:
                showDatePicker();
                break;
            case R.id.tie_vehicle_no:
                startActivityForResult(new Intent(this, VTSSetVehicalActivity.class).putExtra("Call","complaints"),2000);
                break;
            case R.id.iv_upload:

                break;
            case R.id.btn_submit:
                mobileNo = tie_mobile_no.getText().toString();
                vehicleNo = tie_vehicle_no.getText().toString();
                String mobileModel = tie_mobile_model.getText().toString();
                String problem = tie_description.getText().toString();
                validateData(mobileModel,problem);
            break;
        }
    }

    private void validateData(String mobileModel, String problem) {
        if (mobileModel==null || mobileModel.isEmpty())
            tie_mobile_model.setError("Required");
        else if(problem==null || problem.isEmpty())
            tie_description.setError("Required");
        else if (sp_problem.getSelectedItemPosition()==0)
            Toast.makeText(this, "Select issue category", Toast.LENGTH_SHORT).show();
        else if (selectedDate.isEmpty())
            tie_from.setError("Required");
        else if (vehicleNo.isEmpty())
            tie_vehicle_no.setError("Required");
        else {
            String categoryId = ticketTypes.get(sp_problem.getSelectedItemPosition()-1).getId();
            saveTicket(categoryId,mobileModel,problem);
        }
    }

    private void saveTicket(String categoryId, String mobileModel, String problem) {
        pb_progress.setVisibility(View.VISIBLE);
        Call<DataSaveResponse> call = RepositryInstance.getTicketRepository().saveTicket(userId,vehicleId,categoryId,selectedDate,"Android",mobileNo,mobileModel+" ("+Build.MANUFACTURER+","+Build.MODEL+")", BuildConfig.VERSION_NAME,problem, Build.VERSION.RELEASE);
        call.enqueue(new Callback<DataSaveResponse>() {
            @Override
            public void onResponse(Call<DataSaveResponse> call, Response<DataSaveResponse> response) {
                pb_progress.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    DataSaveResponse data = response.body();
                    Toast.makeText(AddQueryActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }

            @Override
            public void onFailure(Call<DataSaveResponse> call, Throwable t) {
                pb_progress.setVisibility(View.GONE);
                Toast.makeText(AddQueryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String strDate=day+"/"+format.format((month+1))+"/"+year;
                selectedDate=format.format(year)+"-"+format.format(month+1)+"-"+format.format(day);
                tie_from.setText(strDate);
            }
        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2000 && resultCode==RESULT_OK){
            if (data!=null) {
                vehicleNo = data.getStringExtra("vehicleNo");
                vehicleId = data.getStringExtra("vehicleId");
                tie_vehicle_no.setText(vehicleNo);
            }
        }
    }

    private void getTicketTypes(){
        Call<TicketTypeResponse> call = RepositryInstance.getTicketRepository().getTicketType();
        call.enqueue(new Callback<TicketTypeResponse>() {
            @Override
            public void onResponse(Call<TicketTypeResponse> call, Response<TicketTypeResponse> response) {
                if (response.isSuccessful()){
                    TicketTypeResponse data = response.body();
                    assert data != null;
                    if (data.getStatus().equalsIgnoreCase("success")){
                        ticketTypes = data.getData();
                        String []types = new String[ticketTypes.size()+1];
                        types[0]="Select Issue Category";
                        for (int i = 0; i <ticketTypes.size() ; i++) {
                            types[i+1]=ticketTypes.get(i).getName();
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddQueryActivity.this,android.R.layout.simple_spinner_item,types);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_problem.setAdapter(arrayAdapter);
                    }else {
                        Toast.makeText(AddQueryActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketTypeResponse> call, Throwable t) {
                Toast.makeText(AddQueryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields(){
        tie_from.setText("");
        tie_vehicle_no.setText("");
        tie_description.setText("");
        finish();
    }
}
