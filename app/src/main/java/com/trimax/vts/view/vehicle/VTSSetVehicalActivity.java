package com.trimax.vts.view.vehicle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trimax.vts.model.vehicle.Vehicle;
import com.trimax.vts.model.vehicle.VehicleResponse;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;

import com.trimax.vts.utils.PrefEnum;
import com.trimax.vts.view.R;
import com.trimax.vts.view.master.adapter.VehicleMaster.VehicleListAdapter;
import com.trimax.vts.view.maps.GeoFencingNew;
import com.trimax.vts.view.maps.RealTimeTrackingActivity;
import com.trimax.vts.view.maps.ReplayTrackingActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class VTSSetVehicalActivity extends AppCompatActivity {
	private static final String TAG = "VTSSetVehicalActivity";

	Context context;
	ListView lv;
	int lastselected=-1;
	int previousSelected=-1;
	public Button vtssetvehicalbtn;
	private VehicleListAdapter adapter;
	CommonClass commonClass;
	String vehiclechangeflag="";
	public String ClickVehicle="",user_Id="";
	String user_type_id="",CallFrom="";
	Intent resultIntent;
	String  id="",vehicleId="0";
	ProgressBar progressbar_login;
	private EditText etSearch;
	public List<Vehicle> cur_stutus_list;
	private TravelpulseInfoPref infoPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vtsset_vehical);
		if (getSupportActionBar()!=null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		context=this;
		commonClass = new CommonClass(context);
		infoPref = new TravelpulseInfoPref(this);

		cur_stutus_list = new ArrayList<>();
		adapter = new VehicleListAdapter(this,cur_stutus_list,-1);
		etSearch= findViewById(R.id.et_search);
		lv= findViewById(R.id.vtssetvehicallistview);
        vtssetvehicalbtn = findViewById(R.id.vtssetvehicalbtn);
		progressbar_login= findViewById(R.id.progressbar_login);

		lv.setAdapter(adapter);
        user_Id = infoPref.getString("id", PrefEnum.Login);
        vehicleId = infoPref.getString("vid", PrefEnum.Login);
		user_type_id =infoPref.getString("user_type_id",PrefEnum.Login);
		id =infoPref.getString("id",PrefEnum.Login);
        Intent ii = getIntent();
		CallFrom=ii.getExtras().getString("Call");
		if (CallFrom!=null && (CallFrom.equalsIgnoreCase("nearByPlace") || CallFrom.equalsIgnoreCase("remote_lock") || CallFrom.equalsIgnoreCase("add_user") || CallFrom.equalsIgnoreCase("reports") || CallFrom.equalsIgnoreCase("complaints"))){
			vtssetvehicalbtn.setVisibility(View.GONE);
		}

		current_vehicle_status_dashboard(user_type_id,id);

		etSearch.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView check =  view.findViewById(R.id.txtCheckBoxIcon);
				Vehicle model = cur_stutus_list.get(position);
				lastselected=position;
				ClickVehicle=cur_stutus_list.get(lastselected).getId();
				CommonClass.VehicleValueReplay=ClickVehicle;
				infoPref.putString("vid",ClickVehicle,PrefEnum.Login);
				infoPref.putString("vno",model.getVehicleNo(),PrefEnum.Login);
 				check.setVisibility(View.VISIBLE);
				lastselected=position;
				previousSelected=position;
				adapter.setSelectedIndex(position);
				if (CallFrom!=null && (CallFrom.equalsIgnoreCase("nearByPlace") || CallFrom.equalsIgnoreCase("remote_lock") || CallFrom.equalsIgnoreCase("add_user") || CallFrom.equalsIgnoreCase("reports") || CallFrom.equalsIgnoreCase("complaints"))){
					Intent intent = getIntent();
					intent.putExtra("vehicleNo",model.getVehicleNo());
					intent.putExtra("vehicleId",model.getId());
					intent.putExtra("lat",model.getLat());
					intent.putExtra("lag",model.getLang());
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		});


        vtssetvehicalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if(lastselected!=-1) {
					fnForActiveVehicle(user_type_id, id, ClickVehicle);
				}
				else{
            		Toast.makeText(context,"Please Select Vehicle",Toast.LENGTH_LONG).show();
				}
			}
        });

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}

	public void current_vehicle_status_dashboard(String usertype_id, final String user_id) {
			progressbar_login.setVisibility(View.VISIBLE);
			RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
			Call<VehicleResponse> call=objRetrofitInterface.getVehicles(auth,apiKey,usertype_id,user_id);
			call.enqueue(new Callback<VehicleResponse>() {
				@Override
				public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
					Log.d(TAG, "onResponse: "+response.body());
					cur_stutus_list=response.body().getData();
					int active=-1;
					for(int i =0;i<cur_stutus_list.size(); i++){
						if (vehicleId.equalsIgnoreCase(cur_stutus_list.get(i).getVehicleNo())) {
							active=i;
							break;
						}
					}
					adapter = new VehicleListAdapter(context, cur_stutus_list,active);
					lv.setAdapter(adapter);
					progressbar_login.setVisibility(View.GONE);
				}

				@Override
				public void onFailure(Call<VehicleResponse> call, Throwable t) {
					Log.d(TAG, "onFailure: "+t.getMessage());
					Toast.makeText(VTSSetVehicalActivity.this, "Problem occured while loading vehicles.", Toast.LENGTH_SHORT).show();
				}
			});
	}


	public void fnForActiveVehicle(String strUserId,String strId,String vehicleid) {
		progressbar_login.setVisibility(View.VISIBLE);
		try {
			RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
			Call<ResponseBody> call=objRetrofitInterface.fnMarkedVehicle(auth,apiKey, strUserId, strId,vehicleid);
			call.enqueue(new Callback<ResponseBody>()
			{
				@Override
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					try {
						ResponseBody responseBody = response.body();
						int strResponceCode = response.code();
						switch (strResponceCode) {
							case 200:
								assert responseBody != null;
								String strResponse = responseBody.string();
								GsonBuilder builder = new GsonBuilder();
								Gson gson = builder.create();
								JSONObject myObject = new JSONObject(strResponse);
								String syncresponse = myObject.getString("msg");
								String strStatus = myObject.getString("status");
								if(strStatus.equalsIgnoreCase("success")) {
									progressbar_login.setVisibility(View.INVISIBLE);
									vehiclechangeflag="vehicle_changed";
									infoPref.putString("vehiclechangeflag", vehiclechangeflag,PrefEnum.App);

									if(CallFrom.equalsIgnoreCase("CallFromRealTimeTracking")){
										resultIntent = new Intent(VTSSetVehicalActivity.this, RealTimeTrackingActivity.class);

									}else if(CallFrom.equalsIgnoreCase("CallFromReplay")){
										resultIntent = new Intent(VTSSetVehicalActivity.this, ReplayTrackingActivity.class);

									}else if(CallFrom.equalsIgnoreCase("CallFromGeoFence")){
										resultIntent = new Intent(VTSSetVehicalActivity.this, GeoFencingNew.class);
									}

									resultIntent.putExtra("vid",ClickVehicle);
									resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(resultIntent);

								}
								Toast.makeText(context,syncresponse,Toast.LENGTH_LONG).show();
								break;
							case 400:

								break;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						progressbar_login.setVisibility(View.INVISIBLE);
					}
				}

				@Override
				public void onFailure(Call<ResponseBody> call, Throwable t) {
					progressbar_login.setVisibility(View.INVISIBLE);
				}
			});
		}
		catch (Exception ex) {
			ex.printStackTrace();

		}
	}


}
