 package com.trimax.vts.view.provider;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trimax.vts.model.ProviderDetails;
import com.trimax.vts.view.R;

 public class ProviderListLocationActivity extends AppCompatActivity implements OnMapReadyCallback{
	Context context;
	SupportMapFragment supportMapFragment;
	GoogleMap googleMap;
    Double providerLatitude,providerLongitude;
	Double TrackerLatitude,TrackerLongitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provider_list_location);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.providerListlocMap);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
        ArrayList<ProviderDetails> providerDetailsList = (ArrayList<ProviderDetails>) extras.getSerializable("mapproviderDetails");
		int mapproviderposition=extras.getInt("mapproviderposition");
		TrackerLatitude=Double.parseDouble(extras.getString("TrackerLatitude", "0"));
		TrackerLongitude=Double.parseDouble(extras.getString("TrackerLongitude", "0"));
		//providerDetailsList = (ArrayList<ProviderDetails>) getIntent().getSerializableExtra("mapproviderDetails");
		try {
			 supportMapFragment.getMapAsync(this);
			 googleMap.clear();
			googleMap.setMyLocationEnabled(false);
		}catch (SecurityException e){
			e.printStackTrace();
		}
        for(int i = 0; i< providerDetailsList.size(); i++)
        {
        
	        double lat=0.0;
	        double lng=0.0;
	        
	        lat = providerDetailsList.get(i).getLatitude();
			lng = providerDetailsList.get(i).getLongitude();
			LatLng LL= new LatLng(lat, lng);
			
			MarkerOptions options = new MarkerOptions();
			
	        options.position(LL);
			options.title(providerDetailsList.get(i).getOutletName());
			 if(i==mapproviderposition){
		            //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
		            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
				 providerLatitude= providerDetailsList.get(i).getLatitude();
				 providerLongitude= providerDetailsList.get(i).getLongitude();


		        }
			 else{
				 options.icon(BitmapDescriptorFactory.fromResource(R.drawable.nearmeicon));
			 }
		
		        // Add new marker to the Google Map Android API V2
		        googleMap.addMarker(options);
        }

			LatLng LL = new LatLng(TrackerLatitude, TrackerLongitude);
			MarkerOptions options = new MarkerOptions();
			options.position(LL);
			options.title("Service required here");
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			googleMap.addMarker(options);
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(TrackerLatitude, TrackerLongitude)));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		onBackPressed();
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		googleMap = googleMap;
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.setTrafficEnabled(false);
		googleMap.setIndoorEnabled(false);
		googleMap.setBuildingsEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(true);

	}
}
