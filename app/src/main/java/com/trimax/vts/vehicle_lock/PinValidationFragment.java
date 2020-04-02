package com.trimax.vts.vehicle_lock;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.factory.RepositryInstance;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinValidationFragment extends DialogFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView counterText, msgOtpSent;
    Button mResendOTP;
    TextInputEditText tvOTP, tvTPin, tvReenterTPin;
    private CountDownTimer countDownTimer;
    ProgressDialog progressBar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userID = "1";
    private String userIDType =  "5";
    private String mobileDeviceID = "1";

    public PinValidationFragment() {
        // Required empty public constructor
    }

    public static PinValidationFragment newInstance(String param1, String param2) {
        PinValidationFragment fragment = new PinValidationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        TravelpulseInfoPref infoPref = new TravelpulseInfoPref(Objects.requireNonNull(getActivity()));
        userIDType = infoPref.getString("user_type_id", PrefEnum.Login);
        userID = infoPref.getString("id", PrefEnum.Login);
        mobileDeviceID = infoPref.getString("record_id", PrefEnum.OneSignal);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        countDownTimer = new CountDownTimer(61000, 1000) {
            public void onTick(long millisUntilFinished) {
                long count = millisUntilFinished / 1000;
                counterText.setText("" + (count+1));
                if(count < 25) {
                    try {
                        counterText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    } catch (Exception e) {

                    }
                }
            }

            public void onFinish() {
                counterText.setText("");
                msgOtpSent.setVisibility(View.GONE);
                counterText.setVisibility(View.GONE);
                mResendOTP.setVisibility(View.VISIBLE);
                mResendOTP.setText("Resend");
                PinValidationFragment.this.onDestroy();
            }
        };

        progressBar = new ProgressDialog(getActivity(), R.style.MySpinnerTheme);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_validation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.close).setOnClickListener(this);
        view.findViewById(R.id.btn_save).setOnClickListener(this);
        tvOTP = view.findViewById(R.id.otp);
        tvTPin = view.findViewById(R.id.tie_password);
        tvReenterTPin = view.findViewById(R.id.tie_cnfm_password);
        msgOtpSent = view.findViewById(R.id.msg_otp_sent);
        counterText = view.findViewById(R.id.counter);
        mResendOTP = view.findViewById(R.id.resend);
        mResendOTP.setOnClickListener(this);
        msgOtpSent.setVisibility(View.GONE);
        // sendOTP();
        // startTimer();
    }

    public void sendOTP() {
        progressBar.show();
        Call<ResponseSentOTP> call = RepositryInstance.getImmobilizationRepository().sendOTP(userID, userIDType, mobileDeviceID);
        call.enqueue(new Callback<ResponseSentOTP>() {
            @Override
            public void onResponse(Call<ResponseSentOTP> call, Response<ResponseSentOTP> response) {
                if (response.isSuccessful()) {
                    progressBar.dismiss();
                    if (Objects.requireNonNull(response.body()).getStatus().equalsIgnoreCase("success")) {
                        counterText.setVisibility(View.VISIBLE);
                        msgOtpSent.setVisibility(View.VISIBLE);
                        mResendOTP.setVisibility(View.GONE);
                        countDownTimer.start();
                        Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        progressBar.dismiss();
                        String msg = Objects.requireNonNull(response.body()).getMsg()+ " "+Objects.requireNonNull(response.body()).getResponseDate().getMobileDeviceID();
                        showAlertDialog(Objects.requireNonNull(response.body()).getStatus() + "..", msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSentOTP> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                progressBar.dismiss();
                showAlertDialog("Sorry!!", t.getMessage());
            }
        });

    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case  R.id.btn_save:
                generateTPin();
                break;
            case R.id.resend:
                sendOTP();
                break;
            case R.id.close:
                dismiss();
                break;
        }
    }

    private void generateTPin() {
        progressBar.show();
        Call<ResponseSetTPin> call = RepositryInstance.getImmobilizationRepository().setTPin(userID, userIDType, mobileDeviceID, tvTPin.getText().toString(), tvReenterTPin.getText().toString(),
                tvOTP.getText().toString());
        call.enqueue(new Callback<ResponseSetTPin>() {
            @Override
            public void onResponse(Call<ResponseSetTPin> call, Response<ResponseSetTPin> response) {
                if (response.isSuccessful()) {

                    String msg = Objects.requireNonNull(response.body()).getMsg();

                    progressBar.dismiss();
                    if (!Objects.requireNonNull(response.body()).getStatus().equalsIgnoreCase("success")) {
                        msg = Objects.requireNonNull(response.body()).getMsg()+ " "+Objects.requireNonNull(response.body()).getResponseDate().getCnfTPin();
                    }
                    showAlertDialog(Objects.requireNonNull(response.body()).getStatus() + "..", msg);
                }
            }

            @Override
            public void onFailure(Call<ResponseSetTPin> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                progressBar.dismiss();
                showAlertDialog("Sorry!!", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    private void showAlertDialog(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(title.toUpperCase())
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(title.equalsIgnoreCase("success")) {
                            PinValidationFragment.this.dismiss();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
