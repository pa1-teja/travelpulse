package com.trimax.vts.view.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.R;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class ForgetPasswordActivityNew extends AppCompatActivity  {

    TextInputEditText textInputEditText;
    Button button;
    TextView textView_back;
    String strMessage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ///initialing the data...
        textInputEditText = (TextInputEditText) findViewById(R.id.edittext_email_forgetpassword);
        button = (Button) findViewById(R.id.button_forgetpassword);
        textView_back = (TextView) findViewById(R.id.textview_back_forgetpassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isInternetAvailable(ForgetPasswordActivityNew.this)) {
                    if (textInputEditText.getText().toString().equals("") || (!textInputEditText.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))) {
                        Utils.showToast(ForgetPasswordActivityNew.this, "Please Enter Valid Email Address");
                    } else {
                        fnforgotpassowrd(textInputEditText.getText().toString());
                    }
                } else {
                    Utils.showToast(ForgetPasswordActivityNew.this, "Please check Internet connection");
                }
            }
        });

        textView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgetPasswordActivityNew.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }


    public void fnforgotpassowrd(String strUserName) {
            Utils.showProgressDialog(ForgetPasswordActivityNew.this, "please wait");

            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call=objRetrofitInterface.fnForgotPassword(auth,apiKey,strUserName);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Utils.stopProgressDialog();

                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e("","Responce code"+strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                //CommonClass.DisplayToast(context,strResponse,"center");
                                JSONObject myObject = new JSONObject(strResponse);
                                String strStatus = myObject.getString("status");
                                strMessage = myObject.getString("msg");
                                if (strStatus.trim().equals("success"))
                                {
                                    new AlertDialog.Builder(ForgetPasswordActivityNew.this)
                                            .setMessage(strMessage)
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent=new Intent(ForgetPasswordActivityNew.this,LoginActivity.class);
                                                    startActivity(intent);
                                                }

                                            }).show();

                                }
                                else {
                                    new AlertDialog.Builder(ForgetPasswordActivityNew.this)
                                            .setMessage(strMessage)
                                            .setCancelable(true)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }

                                            }).show();
                                    Log.e("Failure",strMessage);
                                    Utils.stopProgressDialog();
                                }
                                break;
                            case 400:
                                break;
                            case 500:
                                Log.e("response code 500","Internal server error");
                                break;
                        }

                    } catch (Exception ex)
                    {

                        ex.printStackTrace();
                        new AlertDialog.Builder(ForgetPasswordActivityNew.this)
                                .setMessage("Please Try Agaim")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }

                                }).show();
                        Utils.stopProgressDialog();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {


                }
            });
    }

}

