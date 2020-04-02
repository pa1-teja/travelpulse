package com.trimax.vts.view.provider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.trimax.vts.helper.OnSwipeTouchListener;
import com.trimax.vts.services.AppLocationService;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.helper.GooglePlaceAreaTextChangeListener;
import com.trimax.vts.model.CurrLocation;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import com.trimax.vts.webservices.GetAllProvidersWs;

public class ProviderADVSearchActivity extends AppCompatActivity {
	Context context;
	TextView close,clear;
	EditText /*etArea,*/etRadius,etETA,etProvider,etCost,ProviderName;
	AutoCompleteTextView aedtArea;
	Button btnSearch;
	String ServiceCode,addressWs;
	CommonClass cc;
	Typeface type;
	public static int REQUEST_CODE = 5;
	SharedPreferences sharedpreference;
	String flag="";
	SharedPreferences.Editor editor;
	public int searchRadiusWs;
	LatLng latLng;
	CheckBox checkBox;
	Double currlatUser,currlngUser;
	Location tempLocation;
	AppLocationService appLocationService;
	LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provider_advsearch);
		context=this;

		getSupportActionBar().hide();

		sharedpreference=context.getSharedPreferences(Constants.app_preference, context.MODE_PRIVATE);
	    editor = sharedpreference.edit();

		type = Typeface.createFromAsset(context.getAssets(),"fontawesome-webfont.ttf");



		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		flag = extras.getString(Constants.IVASService.ADV_SEARCH_FLAG);
		if(flag != null) {
			if (flag.equals(Constants.IVASService.DIRECT_CALL)) {
				Toast.makeText(context, "Your vehicle's last location not found please select area.", Toast.LENGTH_LONG).show();
			}
		}

		ServiceCode = extras.getString(Constants.IVASService.SERVICE_CODE);
		cc = new CommonClass(context);
		close=(TextView) findViewById(R.id.close);
		clear=(TextView) findViewById(R.id.txtClearText);
		
		close.setTypeface(type);
		clear.setTypeface(type);
		
		aedtArea = (AutoCompleteTextView) findViewById(R.id.aedtArea);

		addressWs = sharedpreference.getString(Constants.IVASService.ADDRESS_WS, null);
		searchRadiusWs=Integer.parseInt(sharedpreference.getString(Constants.IVASService.SEARCH_RADIUS_WS,"0"));

		if(addressWs!=null){
			aedtArea.setText(addressWs);
		}

		etRadius=(EditText) findViewById(R.id.ETRadius);
		if(searchRadiusWs!=0)
		{
			etRadius.setText(Integer.toString(searchRadiusWs));
		}
		/*if(PassedSR!=0){
			etRadius.setText(Integer.toString(PassedSR));
		}*/
		etETA=(EditText) findViewById(R.id.ETETA);	
		etProvider=(EditText) findViewById(R.id.ETProvider);
		etCost=(EditText) findViewById(R.id.ETCost);
		ProviderName=(EditText) findViewById(R.id.ETProvider);
		
		btnSearch=(Button) findViewById(R.id.searchbtn);
		
		aedtArea.addTextChangedListener(new GooglePlaceAreaTextChangeListener(context, aedtArea));

		checkBox= (CheckBox) findViewById(R.id.checkboxLocatn);
		checkBox.setOnClickListener(new View.OnClickListener() {

		   @Override
	    public void onClick(View v) {

			   if (checkBox.isChecked()) {
				   aedtArea.setEnabled(false);
				   locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
				   checkPlayServices();

				   if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					   // GPS Provider disabled
					   showSettingsAlert("GPS", ServiceCode);
				   }
				   appLocationService = new AppLocationService(context);
				   tempLocation = appLocationService.getBestLocation();
				   if(tempLocation!=null)
				   {
					   currlatUser = tempLocation.getLatitude();
					   currlngUser = tempLocation.getLongitude();
				   }

			   }else
			   {
				   aedtArea.setEnabled(true);
			   }
		   }
        });


		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				aedtArea.setText("");
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String area = aedtArea.getText().toString();
				String radius = etRadius.getText().toString();
				String eta = etETA.getText().toString();
				String cost = etCost.getText().toString();
				String providername = ProviderName.getText().toString();

				CurrLocation CL = new CurrLocation();
				CL.setSearchRadius(etRadius.getText().length() == 0 ? null : Integer.parseInt(radius));

				if(!checkBox.isChecked()) {

					if (area != null && area.length() > 0) {
						CL.setArea(area);

						latLng = cc.getLatLngFromAddress(context, area);

						if (latLng != null) {
							CL.setLatitude(latLng.latitude);
							CL.setLongitude(latLng.longitude);
						}
					}
				}
				else
				{
					CL.setLatitude(currlatUser);
					CL.setLongitude(currlngUser);
				}
				CL.setServiceCode(ServiceCode);
				CL.setUseLocation(true);

				if (eta.length() > 0 && !eta.equals("")) {
					CL.setEta(Integer.parseInt(eta));
				}

				if (cost != null && cost.length() > 0) {
					CL.setCost(Integer.parseInt(cost));
				}

				if (providername != null && providername.length() > 0) {
					CL.setOutletname(providername);
				}

				Gson gson = new Gson();
				String value = gson.toJson(CL);

				String url = context.getString(R.string.action_url);
				url = url.concat(context.getString(R.string.action_getallservices));

				if(!checkBox.isChecked())
				{
				if (eta.length() > 0 || cost.length() > 0 || radius.length() > 0) {
					if (area.length() == 0 && providername.length() == 0) {
						Toast.makeText(context, "Please enter area", Toast.LENGTH_LONG).show();
					}
					else {

						if ((area != null && area.length() > 0) || (radius != null && radius.length() > 0) || (eta != null && eta.length() > 0) || (cost != null && cost.length() > 0) || (providername != null && providername.length() > 0))
						{


							if (cc.isConnected(context)) {
								if (area.length() != 0 && latLng == null) {
									cc.DisplayToast(context, "Please enter valid area", "bottom");

								} else {
									new GetAllProvidersWs(context, url, null, null,
                                            Constants.IVASService.ADVANCED_SEARCH, flag)
                                            .execute(value, radius);
								}

							} else {
								cc.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
							}
						} else {
							cc.DisplayToast(context, "Please enter atleast one search criteria", "bottom");
						}

					}
				}
				else if(providername.length() > 0||area.length() > 0) {

					if (cc.isConnected(context)) {
						if (area.length() != 0 && latLng == null) {
							cc.DisplayToast(context, "Please enter valid area", "bottom");

						} else {
							new GetAllProvidersWs(context, url, null, null,
									Constants.IVASService.ADVANCED_SEARCH, flag).execute(value, radius);
						}

					} else {
						cc.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
					}
				}
			}
				else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
					Toast.makeText(context, "Turn on Location", Toast.LENGTH_SHORT).show();
				}
				else if (tempLocation==null)
					{
						Toast.makeText(context, "Could not determine current location", Toast.LENGTH_SHORT).show();
					}
					else {

						if (cc.isConnected(context)) {
							new GetAllProvidersWs(context, url, null, null, Constants.IVASService.ADVANCED_SEARCH, flag).execute(value, radius);
						} else {
							cc.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
						}
					}
				}

		});


		View transv = findViewById(R.id.transparentview);

		transv.setOnTouchListener(new OnSwipeTouchListener(context) {
			public void onSwipeBottom () {
					finish();
					overridePendingTransition(R.anim.slide_stay, R.anim.slide_out);
			    }
			});
	
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.slide_stay,R.anim.slide_out);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.provider_advsearch, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showSettingsAlert(String provider, final String _serviceCode) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		alertDialog.setTitle(provider + " SETTINGS");

		alertDialog.setMessage(provider
				+ " is not enabled! Want to go to settings menu?");

		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						context.startActivity(intent);
						dialog.cancel();
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						//FuelandTireRepairCommon(_serviceCode,"network");
						checkBox.setChecked(false);
					}
				});

		alertDialog.show();
	}
	private  boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode,this,1000).show();
			} else {
				Toast.makeText(context, "This device is not supported.",
						Toast.LENGTH_LONG).show();
			}
			return false;
		}
		return true;
	}
}
