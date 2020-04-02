package com.trimax.vts.view.provider;

import java.io.Serializable;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.services.AppLocationService;
import com.trimax.vts.view.provider.ProviderListLocationActivity;
import com.trimax.vts.view.R;
import com.trimax.vts.model.ProviderDetails;
import com.trimax.vts.utils.Constants;

public class ProviderListAdapter extends BaseAdapter
{

	ArrayList<ProviderDetails> result;
    Context context;
    private LayoutInflater inflater = null;
    String ServiceCode,Address;
	int SearchRadius;
	Double currlat;
	Double currlng;
	CommonClass cc;
	String calculatedEta,getTime;
	Double trackerLatitiude,trackerLongitude;

    public ProviderListAdapter(Context ctx, ArrayList<ProviderDetails> providerDetails, String serviceCode,int searchRadius,String address,Double TrackerLatitiude,Double TrackerLongitude) {
        result = providerDetails;
        context = ctx;
        cc = new CommonClass(context);
        ServiceCode = serviceCode;
		SearchRadius=searchRadius;
		trackerLatitiude=TrackerLatitiude;
		trackerLongitude=TrackerLongitude;

		Address=address;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AppLocationService appLocationService = new AppLocationService(context);	
		Location tempLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
		/*if(tempLocation!=null) {
			currlat = tempLocation.getLatitude();
			currlng = tempLocation.getLongitude();
		}
		else{
			currlat=null;
			currlng=null;
		}*/
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return result!=null ? result.size() : 0;
    }


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		  View rowView = inflater.inflate(R.layout.provider_list_item, parent, false);   
		  
		  Typeface font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
		  
		    TextView CallIcon=(TextView) rowView.findViewById(R.id.txtCallIcon);
		    TextView Location=(TextView) rowView.findViewById(R.id.txtLocation);
		    TextView Rupee=(TextView) rowView.findViewById(R.id.txtRupeeIcon);
		   TextView Mob= (TextView) rowView.findViewById(R.id.txtMob);
		 TextView Name = (TextView) rowView.findViewById(R.id.txtName);
		 TextView Address = (TextView) rowView.findViewById(R.id.txtAddress);
		 TextView Eta = (TextView) rowView.findViewById(R.id.txtEta);
		 TextView Cost= (TextView) rowView.findViewById(R.id.txtCost);
		TextView CostText= (TextView) rowView.findViewById(R.id.txtCostText);
		TextView ETAText= (TextView) rowView.findViewById(R.id.txtEtaText);

		   CallIcon.setTypeface(font_awesome);
		    Location.setTypeface(font_awesome);
		    Rupee.setTypeface(font_awesome);
		//getTime = cc.getTimeDistanceOnRoad(trackerLatitiude, trackerLongitude, result.get(position).getLatitude(), result.get(position).getLongitude());

		if(result.get(position).isCallCenterControl())
		{
			if(result.get(position).getOutletName()!= null){
				Name.setText(result.get(position).getOutletName());
			}else{
				Name.setText("");
			}

			/*if (getTime!=null) {
				Eta.setText(getTime);
			} else {
				Eta.setText("");
			}*/
			if (result.get(position).getCalculatedETA() != null) {
				Eta.setText(result.get(position).getCalculatedETA());
			} else {
				Eta.setText("");
			}
			CallIcon.setVisibility(View.GONE);
			Rupee.setVisibility(View.GONE);
			Mob.setVisibility(View.GONE);
			Address.setVisibility(View.GONE);
			Cost.setVisibility(View.GONE);
			CostText.setVisibility(View.GONE);
			ETAText.setText("Proximity: ");

		}
else {
			if (result.get(position).getOutletName() != null) {
				Name.setText(result.get(position).getOutletName());
			} else {
				Name.setText("");
			}


			if (result.get(position).getAddress() != null) {
				Address.setText(result.get(position).getAddress());
			} else {
				Address.setText("");
			}


/*
	        if(getTime!= null){
	        	Eta.setText(getTime);
	        }else{
	        	Eta.setText("");
	        }*/

			if (result.get(position).getCalculatedETA() != null) {
				Eta.setText(result.get(position).getCalculatedETA());
			} else {
				Eta.setText("");
			}

			if (result.get(position).getRates() != null) {
				Cost.setText(result.get(position).getRates().toString());
			} else {
				Cost.setText("");
			}

			if (result.get(position).getContactPersonTelNo() != null) {
				Mob.setText(" " + result.get(position).getContactPersonTelNo());
			} else {
				Mob.setText("");
			}
		}
	        
	        TextView ProviderLoc= (TextView) rowView.findViewById(R.id.txtLocation);
	        
			ProviderLoc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle bundle=new Bundle();
					bundle.putSerializable("mapproviderDetails", result);
					bundle.putInt("mapproviderposition", position);
					bundle.putString("TrackerLatitude", Double.toString(trackerLatitiude));
					bundle.putString("TrackerLongitude", Double.toString(trackerLongitude));
					Intent intent=new Intent(context,ProviderListLocationActivity.class);
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			});
	       
			Button bookNowBtn= (Button) rowView.findViewById(R.id.btBookNow);
			if(ServiceCode.equals("FUEL") || ServiceCode.equals("TIREREPAIR")){
				bookNowBtn.setOnClickListener(new BookNowOnClickListener(position,result.get(position).getCalculatedETA()));
			}
			else
			{
				bookNowBtn.setVisibility(bookNowBtn.INVISIBLE);
			}
		
		return rowView;
	}
	
	public class BookNowOnClickListener implements OnClickListener
	{
		int _position;
		String _eta;
		public BookNowOnClickListener(int _position,String _eta) {
			// TODO Auto-generated constructor stub
			this._position=_position;
			this._eta=_eta;
		}

		@Override
		public void onClick(View v) {
			AppLocationService appLocationService = new AppLocationService(context);	
			Location currLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
			if(currLocation!=null) {
				 currlat = currLocation.getLatitude();
				 currlng = currLocation.getLongitude();
			}
			else
			{
				currlat=null;
				currlng=null;
			}


			bookNowInputParam BNIP = new bookNowInputParam();
		    BNIP.setLatitude(trackerLatitiude);
			BNIP.setLongitude(trackerLongitude);
			BNIP.setArea(Address);
				BNIP.setServiceCode(ServiceCode);
			BNIP.setProviderId(result.get(_position).getId());
			BNIP.setSearchRadius(SearchRadius);
			BNIP.setMobileLatitude(currlat);
			BNIP.setMobileLongitude(currlng);
			BNIP.setCallCenterControl(result.get(_position).isCallCenterControl());
			BNIP.setETA(null);
			String cost=(result.get(_position).getRates()).toString();
			BNIP.setCost(cost);
			/*TextView Eta1 = (TextView) v.findViewById(R.id.txtEta);
			String currEta=Eta1.getText().toString();*/
			
			//String time=getTimeArray[_position];

			
			SharedPreferences sharedpreference = context.getSharedPreferences(Constants.app_preference,
					Context.MODE_PRIVATE);

			Integer userId = sharedpreference.getInt("id", 0);
			BNIP.setUserId(userId);
			String currimei=sharedpreference.getString("imei", null);
			BNIP.setImeiNo(currimei);
		}
		
	}

	public class bookNowInputParam implements Serializable{
		private Double latitude;
		private Double longitude;
		private String area;
		private String serviceCode;
		private int providerId;
		private int userId;
		private String imeiNo;
		private String eta;
		private Double mobileLatitude;
		private Double mobileLongitude;
		private int searchRadius;
		private String cost;
		private boolean callCenterControl;

		public bookNowInputParam() {
		}

		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public String getServiceCode() {
			return serviceCode;
		}
		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}
		public int getProviderId() {
			return providerId;
		}
		public void setProviderId(int providerId) {
			this.providerId = providerId;
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public String getImeiNo() {
			return imeiNo;
		}
		public void setImeiNo(String imeiNo) {
			this.imeiNo = imeiNo;
		}
		public String getETA() {
			return eta;
		}
		public void setETA(String eTA) {
			eta = eTA;
		}
		public Double getMobileLatitude() {
			return mobileLatitude;
		}
		public void setMobileLatitude(Double mobileLatitude) {
			this.mobileLatitude = mobileLatitude;
		}
		public Double getMobileLongitude() {
			return mobileLongitude;
		}
		public void setMobileLongitude(Double mobileLongitude) {
			this.mobileLongitude = mobileLongitude;
		}
		public int getSearchRadius() {
			return searchRadius;
		}

		public boolean isCallCenterControl() {
			return callCenterControl;
		}
		public void setCallCenterControl(boolean callCenterControl) {
			this.callCenterControl = callCenterControl;
		}

		public void setSearchRadius(int searchRadius) {
			this.searchRadius = searchRadius;
		}

		public String getCost() {
			return cost;
		}

		public void setCost(String cost) {
			this.cost = cost;
		}
	}
		
}
