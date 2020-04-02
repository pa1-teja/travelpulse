package com.trimax.vts.view.login;

import org.json.JSONObject;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class ChangePasswordActivity extends AppCompatActivity {
	TextInputEditText password,newpassword,confirmpassword;
	Button update;
	String firstName;
	ProgressBar progressbar_login;
	Context context;
	CommonClass commonClass;
	String userID="";
	TravelpulseInfoPref infoPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_layout);

		infoPref = new TravelpulseInfoPref(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		context=this;

		userID = infoPref.getString("id", PrefEnum.Login);
		commonClass = new CommonClass(context);
		
		
		password= findViewById(R.id.etPassword);
		newpassword= findViewById(R.id.etNewPassword);
		confirmpassword= findViewById(R.id.etCofirmPassword);
		update=(Button) findViewById(R.id.btUpdate);
        progressbar_login=(ProgressBar)findViewById(R.id.progressbar_login);

        update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String flag="true";
				firstName = infoPref.getString("FirstName", PrefEnum.Login);
				String passwordS=password.getText().toString();
				String newpasswordS=newpassword.getText().toString();
		        String confirmpasswordS=confirmpassword.getText().toString();

				if (!isValidPassword(passwordS)) {
		        	flag="false";
		        	password.setError("Enter Valid password");
				}
		        if (!isValidPassword(newpasswordS)) {
		        	flag="false";
		        	newpassword.setError("Enter Valid password");
				}

				if (isValidconPassword(newpasswordS, confirmpasswordS)) {
					flag="false";
					confirmpassword.setError("password not matching");
				}
				if (newpasswordS.length()<6) {
					flag="false";
					confirmpassword.setError("password is too short  atleast 6 character in length");
				}
				if (confirmpasswordS.length()<6) {
					flag="false";
					confirmpassword.setError("password is too short  atleast 6 character in length");
				}
				if (!isValidconPassword(passwordS, newpasswordS)) {
					flag="false";
					newpassword.setError("Old and new password can not be same");
				}

		        if(flag.equals("true")){
					if (commonClass.isConnected(context)) {
						current_vehicle_status_dashboard(infoPref.getString("id",PrefEnum.Login),passwordS,newpasswordS,confirmpasswordS);
					} else {
						commonClass.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
					}
		        }
	     }

		});
	
}

	
	private boolean isValidPassword(String pass) {
		if (TextUtils.isEmpty(pass)) {
			return false;
		}
		return true;
	}

	private boolean isValidconPassword(String conPassword, String pass) {
		if (pass.equals(conPassword)) {
			return false;
		}
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}

	public void current_vehicle_status_dashboard(String usertype_id, final String old,final String newp,final String cnew) {
			progressbar_login.setVisibility(View.VISIBLE);
			RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
			Call<ResponseBody> call=objRetrofitInterface.chnagePassword(auth,apiKey,usertype_id,old,newp,cnew);
			call.enqueue(new Callback<ResponseBody>() {
				@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
				@Override
				public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
					try {
                        progressbar_login.setVisibility(View.INVISIBLE);
						ResponseBody responseBody = response.body();
						int strResponceCode = response.code();
						Log.e("", "Response code" + strResponceCode);
						switch (strResponceCode) {
							case 200:
								assert responseBody != null;
								String strResponse = responseBody.string();
								JSONObject myObject = new JSONObject(strResponse);

								String strStatus = myObject.getString("status");
								String strmsg = myObject.getString("msg");

								if (strStatus.trim().equals("success")) {
										Toast.makeText(context,strmsg,Toast.LENGTH_SHORT).show();
										infoPref.putString("logout","y",PrefEnum.Login);
										Intent ii =new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                        startActivity(ii);
								}
								else{
									Toast.makeText(context,strmsg,Toast.LENGTH_SHORT).show();
								}
						}
					}catch (Exception e){
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Call<ResponseBody> call, Throwable t) {


				}
			});

	}

}