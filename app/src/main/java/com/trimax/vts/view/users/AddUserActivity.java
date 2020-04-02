package com.trimax.vts.view.users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.trimax.vts.view.R;
import com.trimax.vts.view.databinding.ActivityAddUserBinding;
import com.trimax.vts.view.login.LoginViewModel;
import com.trimax.vts.view.model.UserModel;
import com.trimax.vts.view.vehicle.VTSSetVehicalActivity;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddUserActivity";
    private ActivityAddUserBinding userBinding;
    private GoogleApiClient mGoogleApiClient;
    private final int REQ_CODE=2000, REQ_NUMBER_PICK=2001;
    private String vehicleNo="",vehicleId="",relation="family";
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Authorized User");
        userBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        userBinding.cpDriver.setOnClickListener(this);
        userBinding.cpFriend.setOnClickListener(this);
        userBinding.cpFamily.setOnClickListener(this);
        userBinding.tieVehicle.setOnClickListener(this);
        userBinding.tieMobile.setOnClickListener(this);
        userBinding.btnSave.setOnClickListener(this);

        viewModel.getLoading().observe(this, isLoading->{
            if (isLoading)
                userBinding.pbProgress.setVisibility(View.VISIBLE);
            else
                userBinding.pbProgress.setVisibility(View.GONE);

        });
    }

    public void getHintPhoneNumber() {
        HintRequest hintRequest = new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), REQ_NUMBER_PICK, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void changeCheckedChip(int id){
        if (id == R.id.cp_family){
            userBinding.cpFamily.setChecked(true);
            userBinding.cpFriend.setChecked(false);
            userBinding.cpDriver.setChecked(false);
            relation="family";
        }else if (id == R.id.cp_friend){
            userBinding.cpFamily.setChecked(false);
            userBinding.cpFriend.setChecked(true);
            userBinding.cpDriver.setChecked(false);
            relation="friend";
        }else {
            userBinding.cpFamily.setChecked(false);
            userBinding.cpFriend.setChecked(false);
            userBinding.cpDriver.setChecked(true);
            relation="driver";
        }
    }

    private boolean validate(String name, String mobile, String relation, String vehicleNo) {
        return name.isEmpty() || mobile.length()!=10 || relation.isEmpty() || vehicleNo.isEmpty();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_save){
            String name = userBinding.tieName.getText().toString();
            String mobile = userBinding.tieMobile.getText().toString();
            if (!validate(name,mobile,relation,vehicleNo)){
                viewModel.saveSecondaryUser(new UserModel(name,mobile,vehicleNo,relation));
            }else {
                Toast.makeText(this, "Required all fields", Toast.LENGTH_SHORT).show();
            }
        }else if (view.getId()==R.id.tie_vehicle){
            startActivityForResult(new Intent(this, VTSSetVehicalActivity.class).putExtra("Call","add_user"),REQ_CODE);
        }else if (view.getId()==R.id.tie_mobile){
            getHintPhoneNumber();
        }
        else {
            changeCheckedChip(view.getId());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_CODE && resultCode==RESULT_OK && data!=null){
            vehicleNo = data.getStringExtra("vehicleNo");
            vehicleId = data.getStringExtra("vehicleId");
            if (vehicleNo!=null && vehicleId!=null)
                userBinding.tieVehicle.setText(vehicleNo);
        }else if (requestCode==REQ_NUMBER_PICK && resultCode==RESULT_OK && data!=null){
            Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
            assert credential != null;
            userBinding.tieMobile.setText(credential.getId().substring(3));
            userBinding.tieName.setText(credential.getName());
        }
    }
}
