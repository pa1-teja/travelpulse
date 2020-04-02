package com.trimax.vts.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.trimax.vts.view.databinding.ActivityLoginBinding;
import com.trimax.vts.view.login.LoginViewModel;
import com.trimax.vts.view.users.UserListActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding loginActivityBinding;
    private LoginViewModel viewModel;

    String mobile="",otp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        loginActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginActivityBinding.tvGetCode.setOnClickListener(this);
        loginActivityBinding.btnLogin.setOnClickListener(this);

        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading) {
                startActivity(new Intent(this, UserListActivity.class));
                loginActivityBinding.pbProgress.setVisibility(View.VISIBLE);
            }else {
                loginActivityBinding.pbProgress.setVisibility(View.GONE);
            }
        });


        loginActivityBinding.tieMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()==10)
                    loginActivityBinding.tvGetCode.setVisibility(View.VISIBLE);
                else {
                    loginActivityBinding.tieMobile.setError("Invalid mobile number");
                    loginActivityBinding.tvGetCode.setVisibility(View.GONE);
                }
            }
        });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_get_code:
                mobile = loginActivityBinding.tieMobile.getText().toString();
                //sendOTP(mobile);
                loginActivityBinding.tilVerificationCode.setVisibility(View.VISIBLE);
                loginActivityBinding.cbAutoVerification.setVisibility(View.VISIBLE);
                loginActivityBinding.btnLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_login:
                otp = loginActivityBinding.tieOtp.getText().toString();
                verifyOtp(mobile,otp);
                break;
        }
    }

    private void verifyOtp(String mobile, String otp) {
        if (mobile.length()==10 && otp.length()==6){
            viewModel.verifyOTP(mobile,otp);
        }else {
            Toast.makeText(this, "Invalid mobile and OTP.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendOTP(String mobile) {
        viewModel.sendOTP(mobile);
    }
}
