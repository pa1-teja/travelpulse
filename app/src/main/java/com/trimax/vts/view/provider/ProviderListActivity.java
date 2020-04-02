package com.trimax.vts.view.provider;

import java.util.ArrayList;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.model.CurrLocation;
import com.trimax.vts.model.ProviderDetails;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import com.trimax.vts.webservices.GetAllProvidersWs;


public class ProviderListActivity extends AppCompatActivity
{

	ListView listview;
	TextView LocationAddress;
	Context context;
	String ServiceCode,CurrUserLat,CurrUserLong,Address;
	List<Address> addresses = null;
	public String area=null;
	public int searchRadius;
	SharedPreferences sharedpreference;
	CommonClass cc;
	Integer userId;
	String flagAdvSearch;
	SharedPreferences.Editor editor;
	Double TrackerLatitude,TrackerLongitude;


	private ArrayList<ProviderDetails> providerDetailsList= new ArrayList<ProviderDetails>();
	private int PLAY_SERVICES_RESOLUTION_REQUEST; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider_list_layout);

		 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setHomeButtonEnabled(true);
		//getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_blue_color)));
		 
		context = this;
		cc=new CommonClass(context);

		listview= findViewById(R.id.ProviderList);

		sharedpreference=context.getSharedPreferences(Constants.app_preference, MODE_PRIVATE);
		editor = sharedpreference.edit();

		//String firstName=sharedpreference.getString("FirstName", "");

		CurrUserLat=sharedpreference.getString(Constants.IVASService.CUR_USER_LATTITUDE, null);
		CurrUserLong=sharedpreference.getString(Constants.IVASService.CUR_USER_LONGITUDE, null);
		userId = sharedpreference.getInt(Constants.IVASService.ID, 0);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		ServiceCode=extras.getString(Constants.IVASService.SERVICE_CODE, null);

		ImageView serviceLogo= findViewById(R.id.imgLocation);
		ActionBar bar = getSupportActionBar();
		//bar.setBackgroundDrawable(getResources().getDrawable(R.color.blue_color));
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
		if(ServiceCode.equals(Constants.IVASService.FUEL)){
			//bar.setBackgroundDrawable(getResources().getDrawable(R.color.yellow_color));
			bar.setTitle("Fuel Delivery");
			serviceLogo.setImageResource(R.drawable.menu_fuel_delivery);
		}
		else if(ServiceCode.equals(Constants.IVASService.TIRE_REPAIR)){
			//bar.setBackgroundDrawable(getResources().getDrawable(R.color.green_color));
			bar.setTitle("Flat Tyre");
			serviceLogo.setImageResource(R.drawable.menu_flat_tyre);
		}
		else if(ServiceCode.equals(Constants.IVASService.CAR_TOWING)){
			//bar.setBackgroundDrawable(getResources().getDrawable(R.color.purple_color));
			bar.setTitle("Vehicle Towing");
			serviceLogo.setImageResource(R.drawable.menu_vehicle_towing);
		}
		else if(ServiceCode.equals(Constants.IVASService.CAR_WORKSHOP)){
			//bar.setBackgroundDrawable(getResources().getDrawable(R.color.orange_color));
			bar.setTitle("Car Workshop");
			serviceLogo.setImageResource(R.drawable.repair);
		}
		else if(ServiceCode.equals("BLOODBANK")){
			//bar.setBackgroundDrawable(getResources().getDrawable(R.color.blood_color));
			bar.setTitle("Blood Bank");
			serviceLogo.setImageResource(R.drawable.safety);
		}
		else if(ServiceCode.equals("HOSPITAL")){
			//bar.setBackgroundDrawable(getResources().getDrawable(R.color.hospital_color));
			bar.setTitle("Hospital");
			serviceLogo.setImageResource(R.drawable.hospital);
		}
		else if(ServiceCode.equals("AMBULANCE")){
			//bar.setBackgroundDrawable(getResources().getDrawable(R.color.ambulance_color));
			bar.setTitle("Ambulance");
			serviceLogo.setImageResource(R.drawable.ambulance);
		}
		else if(ServiceCode.equals("BATTERYJUMP")){
			bar.setTitle("Battery Jump Start");
			serviceLogo.setImageResource(R.drawable.menu_batteryjump);
			//Toast.makeText(context,"Coming Soon",Toast.LENGTH_SHORT).show();

		}

		LocationAddress= findViewById(R.id.txtLocationAddress);
		LocationAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bundle bundle=new Bundle();
				Intent i = new Intent(context, ProviderADVSearchActivity.class);
				bundle.putString(Constants.IVASService.SERVICE_CODE, ServiceCode);
				bundle.putString(Constants.IVASService.AREA, area);
				bundle.putInt(Constants.IVASService.SEARCH_RADIUS, searchRadius);
			//	bundle.putString("CurrUserLattitude", CurrUserLat);
			//	bundle.putString("CurrUserLongitude", CurrUserLong);
				i.putExtras(bundle);
				startActivityForResult(i, ProviderADVSearchActivity.REQUEST_CODE);
				//finish();
				overridePendingTransition(R.anim.slide_in, R.anim.slide_stay);
				
			}
			});


		flagAdvSearch=extras.getString("flagAdvSearch",null);

        Log.d(GetAllProvidersWs.class.getSimpleName(), "flagAdvSearch: " + flagAdvSearch);

		if(flagAdvSearch!=null && flagAdvSearch.equals("directCall")) {
			providerDetailsList = (ArrayList<ProviderDetails>) extras.getSerializable("providerDetails");
			Address = extras.getString("Address", null);
			searchRadius = Integer.parseInt(extras.getString("radius", "0"));
			TrackerLatitude=Double.parseDouble(extras.getString("TrackerLatitude", "0"));
			TrackerLongitude=Double.parseDouble(extras.getString("TrackerLongitude", "0"));

			listview.setAdapter(new ProviderListAdapter(context, providerDetailsList, ServiceCode, searchRadius, Address,TrackerLatitude,TrackerLongitude));
			LocationAddress.setText(Address+ " (Search Radius : " + searchRadius + " Km)\nClick here to modify search");
		}
		else {


			CurrLocation CL = new CurrLocation();
			CL.setLatitude(null);
			CL.setLongitude(null);
			CL.setArea(null);
			CL.setServiceCode(ServiceCode);
			CL.setUserId(userId);
			CL.setUseLocation(false);


			Gson gson = new Gson();
			String value = gson.toJson(CL);

			String url = context.getString(R.string.action_url);
			url = url.concat(context.getString(R.string.action_getallservices));

			if (cc.isConnected(context)) {
				try {
					new GetAllProvidersWs(context, url, listview, LocationAddress, "normal_search", "")
							.execute(value, "");
				}catch (Exception e){
					Log.d(GetAllProvidersWs.class.getSimpleName(), "Error: "+e.getMessage());
				}
			} else {
				CommonClass.DisplayToast(context, context.getString(R.string.network_error_message), "bottom");
			}
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.menu_search, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		editor.remove("AddressWs");
		editor.remove("SearchRadiusWs");
		editor.commit();
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_search) {
			Bundle bundle=new Bundle();
			Intent i = new Intent(context, ProviderADVSearchActivity.class);
			bundle.putString("serviceCode", ServiceCode);
			bundle.putString("area", area);
			bundle.putInt("searchRadius", searchRadius);
			//bundle.putString("CurrUserLattitude", CurrUserLat);
		//	bundle.putString("CurrUserLongitude", CurrUserLong);
			i.putExtras(bundle);
			startActivityForResult(i, ProviderADVSearchActivity.REQUEST_CODE);
			//finish();
			overridePendingTransition(R.anim.slide_in, R.anim.slide_stay);
			return true;
			// return super.onOptionsItemSelected(item);
		}
		onBackPressed();
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d(GetAllProvidersWs.class.getSimpleName(), "requestCode "+requestCode);
		Log.d(GetAllProvidersWs.class.getSimpleName(), "resultCode "+resultCode);
		//Log.d(GetAllProvidersWs.class.getSimpleName(), "data "+data.getDataString());

		if (requestCode == ProviderADVSearchActivity.REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle extras = data.getExtras();
				ServiceCode = extras.getString("serviceCode", "");
				area = extras.getString("area", "");
				TrackerLatitude=Double.parseDouble(extras.getString("TrackerLatitude", "0"));
				TrackerLongitude=Double.parseDouble(extras.getString("TrackerLongitude", "0"));
				searchRadius = Integer.parseInt(extras.getString("radius", "0"));

				providerDetailsList = (ArrayList<ProviderDetails>) extras.getSerializable("providerDetails");
				//CurrUserLat=extras.getString("CurrUserLattitude", null);
				//CurrUserLong=extras.getString("CurrUserLongitude", null);
				Address=extras.getString("Address",null);
			/*	for(int i=0;i<providerDetailsList.size();i++)
				{
					if(providerDetailsList.get(i).getLatitude()==0.0||providerDetailsList.get(i).getLongitude()==0.0)
					{
						Intent intent =new Intent(context,ProviderADVSearchActivity.class);
						context.startActivity(intent);
					}
					else
					{*/
						listview.setAdapter(new ProviderListAdapter(context, providerDetailsList,
								ServiceCode, searchRadius,Address,TrackerLatitude,TrackerLongitude));

						String addr = "";
						if(area != null && area.length() > 0) {
							addr = area != null ? "Your Location : " + area : "";
						}
						LocationAddress.setText(Address+ " (Search Radius : "+searchRadius+" Km)\nClick here to modify search");
					//}
				//}
			}
		}
	}


	}