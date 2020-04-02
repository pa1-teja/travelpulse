package com.trimax.vts.view.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trimax.vts.repository.LoginRepository;
import com.trimax.vts.view.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";
    private LoginRepository repository;
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<OTPResponse> otpResponse = new MutableLiveData<>();
    private MutableLiveData<List<UserModel>> secondaryUsers = new MutableLiveData<>();

    public LoginViewModel() {
        repository = new LoginRepository();
    }

    public LiveData<Boolean> getLoading(){
        return loading;
    }

    public LiveData<OTPResponse> getOTPResponse(){
        return otpResponse;
    }

    public void sendOTP(String mobile){
        loading.setValue(true);
        repository.sendOTP(mobile).enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {

            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {

            }
        });
    }

    public void verifyOTP(String mobile, String otp){
        loading.setValue(true);
        repository.verifyOTP(mobile,otp).enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                loading.setValue(false);

            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    public LiveData<List<UserModel>> getUsers(){
        return secondaryUsers;
    }

    public void getSecondaryUsers(){
        loading.setValue(true);
        repository.verifyOTP("","").enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                loading.setValue(false);
                List<UserModel> users = new ArrayList<>();
                for (int i = 0; i <5 ; i++) {
                    users.add(new UserModel("Demo User "+(i+1),"989247562"+i,"MH-04 5847"));
                }
                secondaryUsers.setValue(users);
            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    public void saveSecondaryUser(UserModel user){
        loading.setValue(true);
        repository.verifyOTP(user.getMobile(),user.getName()).enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    public void changeSecondaryUserStatus(UserModel user){
        loading.setValue(true);
        repository.verifyOTP(user.getMobile(),user.getName()).enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                loading.setValue(false);

            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }
}
