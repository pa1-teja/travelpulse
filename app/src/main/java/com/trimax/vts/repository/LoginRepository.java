package com.trimax.vts.repository;

import com.trimax.vts.view.login.model.LoginCheckResponse;
import com.trimax.vts.view.login.model.LoginResponse;
import com.trimax.vts.view.login.OTPResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class LoginRepository {

    public Call<LoginCheckResponse> checkIfLogin(String deviceid, String playerId, String token, final String usertypeid, final String userid){
        return ApiClient.getApiClient().ChechIflogin(deviceid,playerId,token,usertypeid,userid);
    }


    public Call<LoginResponse> doLogin(String username, final String password, final String status){
        return ApiClient.getApiClient().fnLogin("*/*",username,password,status);
    }

    public Call<ResponseBody> forgotPassword(){
        return ApiClient.getClient().create(RetrofitInterface.class).fnForgotPassword("","","");
    }

    public Call<ResponseBody> changePassword(){
        return ApiClient.getClient().create(RetrofitInterface.class).chnagePassword("","","","","","");
    }

    public Call<OTPResponse> sendOTP(String mobile){
        return ApiClient.getClient().create(RetrofitInterface.class).sendOTP(mobile);
    }

    public Call<OTPResponse> verifyOTP(String mobile,String otp){
        return ApiClient.getClient().create(RetrofitInterface.class).verifyOTP(mobile,otp);
    }

}
