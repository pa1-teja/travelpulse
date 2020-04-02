package com.trimax.vts.view.login;


import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;

public class UserProfileActivity extends AppCompatActivity{
	private Context context;
	CommonClass cc;
	TextInputEditText firstName,lastName,email,mobno;
	private TravelpulseInfoPref infoPref;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile_layout);
		context = this;
		infoPref = new TravelpulseInfoPref(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		cc = new CommonClass(context);

		firstName =  findViewById(R.id.etFirstName);
		lastName =  findViewById(R.id.etLastName);
		email =  findViewById(R.id.etEmailAddress);
		mobno =  findViewById(R.id.etMobileNo);

		try {
			firstName.setText(infoPref.getString("first_name", PrefEnum.Login));
		} catch (NullPointerException e) {
			firstName.setText("");
		}
		try {
			lastName.setText(infoPref.getString("last_name",PrefEnum.Login));
		} catch (NullPointerException e) {
			lastName.setText("");
		}
		try {
			email.setText(infoPref.getString("emailAddress",PrefEnum.Login));
		} catch (NullPointerException e) {
			email.setText("");
		}
		try {
			mobno.setText(infoPref.getString("mobile_number",PrefEnum.Login));
		} catch (NullPointerException e) {
			mobno.setText("");
		}
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		finish();
		return false;
	}
}
