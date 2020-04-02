package com.trimax.vts.webservices;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.provider.ProviderListAdapter;

import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.provider.ProviderADVSearchActivity;
import com.trimax.vts.view.provider.ProviderListActivity;
import com.trimax.vts.view.R;
import com.trimax.vts.view.vas.VASFinalProviderListModel;
import com.trimax.vts.view.vas.VASProviderModel;
import com.trimax.vts.view.vas.VASProviderModelResponse;
import com.trimax.vts.model.CurrLocation;
import com.trimax.vts.model.ProviderDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class GetAllProvidersWs extends AsyncTask<String, String, String> {

    String URL;
    Context context;
    String ServiceCode = "", searchradius = "", area = "";
    String jsonResponse;
    ProgressDialog dialog;
    String callType;
    CommonClass commonClass;
    ListView listview;
    TextView LocationAddress;
    String flagAdvSearch;
    SharedPreferences sharedpreference,sharedPreferencess;
    private String TAG = GetAllProvidersWs.class.getSimpleName();

    public GetAllProvidersWs(Context ctx, String URL, ListView lv, TextView locAddr, String callType, String flagAdvSearch) {
        this.URL = URL;
        context = ctx;
        this.callType = callType;
        this.listview = lv;
        this.LocationAddress = locAddr;
        this.flagAdvSearch = flagAdvSearch;
        commonClass = new CommonClass(ctx);
//		getAllProveidersNewApiCalling();
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        CurrLocation CL = new CurrLocation();
            Gson gson = new Gson();
            Type listType = new TypeToken<CurrLocation>() {}.getType();
            CL = gson.fromJson(params[0], listType);
            ServiceCode = CL.getServiceCode();
            Log.d(TAG, "ServiceCode: " + ServiceCode);

//            jsonResponse =

            getAllProveidersNewApiCalling(CL);

        return jsonResponse;
    }


    String strId = "", strUserId = "",
            newArea = "", newOutletName = "", newServiceCode = "", newCost = "",
            newETA = "", newLat = "", newLong = "", newProviderId = "",
            newSearchRadius = "", newUseLocation = "";

    private void setNewData(CurrLocation curLoc) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
            strId = sharedPreferences.getString("id", "");
            strUserId = sharedPreferences.getString(context.getString(R.string.user_type_id), "");

            if (curLoc != null) {
                Log.d(TAG, "CurLoc toString: " + curLoc.toString() + "getArea: " + curLoc.getArea());
                Log.d(TAG, "CurLoc getOutletname: " + curLoc.getOutletname());
                Log.d(TAG, "CurLoc getServiceCode: " + curLoc.getServiceCode());
                Log.d(TAG, "CurLoc getCost: " + curLoc.getCost());
                Log.d(TAG, "CurLoc getEta: " + curLoc.getEta());
                Log.d(TAG, "CurLoc getLatitude: " + curLoc.getLatitude());
                Log.d(TAG, "CurLoc getLongitude: " + curLoc.getLongitude());
                Log.d(TAG, "CurLoc getProviderId: " + curLoc.getProviderId());
                Log.d(TAG, "CurLoc getSearchRadius: " + curLoc.getSearchRadius());
                Log.d(TAG, "CurLoc getUseLocation: " + curLoc.getUseLocation());
                Log.d(TAG, "CurLoc getUserId: " + curLoc.getUserId());
                //{"serviceCode":"TIREREPAIR","useLocation":false,"userId":0}
                //
                if (curLoc.getArea() != null)
                    newArea = curLoc.getArea();

                if (curLoc.getOutletname() != null)
                    newOutletName = curLoc.getOutletname();

                if (curLoc.getServiceCode() != null)
                    newServiceCode = curLoc.getServiceCode();

                if (curLoc.getCost() != null)
                    newCost = curLoc.getCost() + "";

                if (curLoc.getEta() != null)
                    newETA = curLoc.getEta() + "";

                if (curLoc.getLatitude() != null)
                    newLat = curLoc.getLatitude() + "";

                if (curLoc.getLongitude() != null)
                    newLong = curLoc.getLongitude() + "";

                if (curLoc.getProviderId() != null)
                    newProviderId = curLoc.getProviderId() + "";

                if (curLoc.getSearchRadius() != null)
                    newSearchRadius = curLoc.getSearchRadius() + "";

                if (curLoc.getUseLocation() != null) {
                    if (curLoc.getUseLocation())
                        newUseLocation = "Y";
                    else newUseLocation = "N";
                }

            } else Log.d(TAG, "curLoc is null...");
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private boolean isAllDataEmpty() {
        boolean result = true;
        if (!newArea.trim().equalsIgnoreCase(""))
            result = false;
        else if (!newOutletName.trim().equalsIgnoreCase(""))
            result = false;
        else if (!newCost.trim().equalsIgnoreCase(""))
            result = false;
        /*else if (!newServiceCode.trim().equalsIgnoreCase(""))
            result = false;*///service code will never be empty
        else if (!newETA.trim().equalsIgnoreCase(""))
            result = false;
        else if (!newLat.trim().equalsIgnoreCase(""))
            result = false;
        else if (!newLong.trim().equalsIgnoreCase(""))
            result = false;
        else if (!newProviderId.trim().equalsIgnoreCase(""))
            result = false;
        else if (!newSearchRadius.trim().equalsIgnoreCase(""))
            result = false;
        /*else if (!newUseLocation.trim().equalsIgnoreCase(""))
            result = false;*/
        else result = true;

        return result;
    }

    private void getAllProveidersNewApiCalling(CurrLocation curLoc) {
//        final String[] result = {""};
//        String radius = "";
        setNewData(curLoc);
        sharedPreferencess = context.getSharedPreferences("login", MODE_PRIVATE);

        RetrofitInterface retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
        Call<VASProviderModelResponse> call;
        if (isAllDataEmpty()) {
            Log.d(TAG, "Serach type: Normal");
            call = retrofitInterface.fnGetVASProvidersWithNormalSearch(auth, apiKey,
                    sharedPreferencess.getString("id",""),//strId,//
                    sharedPreferencess.getString("user_type_id",""),
                    Constants.IVASService.NORMAL_SEARCH_TYPE,
                    newServiceCode);
        } else {
            Log.d(TAG, "Serach type: Advanced");
            call = retrofitInterface.fnGetVASProvidersWithAdvancedSearch(auth, apiKey,
                    sharedPreferencess.getString("id",""),
                    sharedPreferencess.getString("user_type_id",""),
                    Constants.IVASService.ADVANCE_SEARCH_TYPE,
                    newServiceCode,
                    newLat,
                    newLong,
                    newUseLocation,
                    newArea,
                    newSearchRadius,
                    newETA,
                    newOutletName,
                    newCost);
        }

        final String message = "No data \nServer is not responding.";

        call.enqueue(new Callback<VASProviderModelResponse>() {
            @Override
            public void onResponse(Call<VASProviderModelResponse> call, final Response<VASProviderModelResponse> response) {
                Log.d(TAG, "Status: " + response.body().getStatus());
                Log.d(TAG, "Data: " + response.body().getData());
                try {
                    if (response.body().getStatus().trim().equalsIgnoreCase("success")) {
                        Log.d(TAG, "ServiceCode: " + response.body().getData().getServiceCode());
                        setAllProviders(response.body().getData(), "1", "");
                    } else {
                        new AlertDialog.Builder(context)
                                .setMessage(message)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setAllProviders(response.body().getData(), "0", message);
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                } catch (Exception e) {
                    Log.d(TAG, "string body error: " + e.getMessage());
                    setAllProviders(response.body().getData(), "0", message);
                }
            }

            @Override
            public void onFailure(Call<VASProviderModelResponse> call, Throwable t) {
                Log.d(TAG, "Error code: " + call.toString());
                try{
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                }catch (Exception e){
                    Log.d(TAG, e.getMessage());
                }
                new AlertDialog.Builder(context)
                        .setMessage("Server not responding.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
//                setAllProviders(null, "0", message);
            }
        });
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

//        onPostExecutionImpl(result);
    }

    public class returnJson {
        public String status;
        public int searchRadius;
        public ArrayList<ProviderDetails> providerlistrespnslist;
        public String message;
        public Double latitude;
        public Double longitude;
        public String address;
    }


    private ArrayList<ProviderDetails> getProviderDetailsList(List<VASFinalProviderListModel> models){
        ArrayList<ProviderDetails> providerDetailsList = new ArrayList<>();
        if (models != null) {
            for (VASFinalProviderListModel obj : models) {
                boolean isCallCenter = false;
                if (obj.getCallCenterControl().trim().equalsIgnoreCase("Y"))
                    isCallCenter = true;

                ProviderDetails providerDetails = new ProviderDetails(
                        getExactIntergerValue(obj.getPid()),
                        getExactIntergerValue(obj.getPid()),
                        obj.getOutletName(),
                        obj.getAddress(),
                        getExactIntergerValue(obj.getSid()),//areaId
                        getExactIntergerValue(obj.getSid()),//cityId
                        getExactIntergerValue(obj.getPin()),
                        getExactDoubleValue(obj.getLatitude()),
                        getExactDoubleValue(obj.getLongitude()),
                        obj.getGroupServiceCode(),//regnNo
                        obj.getContactPersonName(),
                        ""+obj.getDuration(),//calculatedETA
                        obj.getContactPersonTelNo(),
                        obj.getContactPersonMobile(),
                        obj.getContactPersonEmailId(),
                        getExactIntergerValue(obj.getTriamxPreferenceRating()),
                        getExactIntergerValue(obj.getUserServiceRating()),//referralPointesEarned
                        getExactIntergerValue(obj.getResponseTime()),
                        getExactIntergerValue(obj.getRates()),
                        isCallCenter
                );
                providerDetailsList.add(providerDetails);
            }
        }
        Log.d(TAG, "providerDetailsList: "+providerDetailsList);
        return providerDetailsList;
    }


    private int getExactIntergerValue(String val){
        int i = 0;
        if (val != null){
            if (!val.trim().equalsIgnoreCase(""))
                i = Integer.parseInt(val);
        }
        Log.d(TAG, "String val: "+val+ " int val: "+i);
        return i;
    }

    private double getExactDoubleValue(String val){
        double i = 0;
        if (val != null){
            if (!val.trim().equalsIgnoreCase(""))
                i = Double.parseDouble(val);
        }
        Log.d(TAG, "String val: "+val+ " double val: "+i);
        return i;
    }


    /* New Method using retrfit api */
    private void setAllProviders(VASProviderModel model, String status, String message) {
        try {
            try{
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }catch (Exception e){
                Log.d(TAG, e.getMessage());
            }
            if (model != null) {


                if (status.equals("1")) {
                    Log.d(TAG, "Model ProviderList: "+model.getFinalProviderList());
                    ArrayList<ProviderDetails> providerlistrespnslist = getProviderDetailsList(model.getFinalProviderList());

                    Log.d(TAG, "provider lise size: " + providerlistrespnslist.size());

                    if (model.getArea() == null) {
                        model.setArea(" ");
                    }
                    sharedpreference = context.getSharedPreferences(Constants.app_preference, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreference.edit();
                    editor.putString("AddressWs", model.getArea());
                    editor.putString("SearchRadiusWs", (model.getSearchRadius()));
                    editor.commit();

                    Log.d(TAG, "callType: " + callType);
                    if (callType.equals("normal_search")) {
                        Bundle bundle = new Bundle();
                        Log.d(TAG, "model.getLongitude(): " + model.getLongitude());
                        Log.d(TAG, "model.getLatitude(): " + model.getLatitude());
                        if (model.getLongitude() == null && model.getLatitude() == null) {
                            Intent i = new Intent(context, ProviderADVSearchActivity.class);
                            Bundle bundleADVsearch = new Bundle();
                            bundleADVsearch.putSerializable("ADVSearchFlag", "DirectCall");
                            bundleADVsearch.putString("serviceCode", ServiceCode);

                            i.putExtras(bundleADVsearch);
                            ((Activity) context).finish();
                            context.startActivity(i);
                        } else {
                            listview.setAdapter(new ProviderListAdapter(context,
                                    providerlistrespnslist, model.getServiceCode(),
                                    Integer.parseInt(model.getSearchRadius()), model.getArea(),
                                    Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude())));

                            ProviderListActivity PLA = (ProviderListActivity) context;
                            PLA.area = area;
                            PLA.searchRadius = Integer.parseInt(model.getSearchRadius());
                            LocationAddress.setText(model.getArea() +
                                    " (Search Radius : " + model.getSearchRadius() + " Km)\nClick here to modify search");
                        }
                    } else if (callType.equals("advanced_search")) {

                        if (flagAdvSearch != null && flagAdvSearch.equals("DirectCall")) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("providerDetails", providerlistrespnslist);
                            bundle.putString("Address", model.getArea());
                            bundle.putString("flagAdvSearch", "directCall");
                            bundle.putString("TrackerLatitude", (model.getLatitude()));
                            bundle.putString("TrackerLongitude", (model.getLongitude()));
                            bundle.putString("radius", (model.getSearchRadius()));
                            bundle.putString("serviceCode", ServiceCode);
                            Intent i = new Intent(context, ProviderListActivity.class);
                            i.putExtras(bundle);
                            ((Activity) context).finish();
                            context.startActivity(i);

                        } else {
                            //	editor.putString("AddressWs",model.address);
                            //	editor.commit();
                            Bundle bundle = new Bundle();
                            bundle.putString("serviceCode", ServiceCode);
                            bundle.putString("area", area);
                            if (searchradius.equals("")) {
                                bundle.putString("radius", (model.getSearchRadius()));
                            } else {
                                bundle.putString("radius", searchradius);
                            }
                            //bundle.putString("CurrUserLattitude", Double.toString(CurrUserLat));
                            //	bundle.putString("CurrUserLongitude", Double.toString(CurrUserLong));
                            bundle.putSerializable("providerDetails", providerlistrespnslist);
                            bundle.putString("Address", model.getArea());
                            bundle.putString("TrackerLatitude", (model.getLatitude()));
                            bundle.putString("TrackerLongitude", (model.getLongitude()));
                            Intent resultIntent = new Intent();
                            resultIntent.putExtras(bundle);
                            ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                            ((Activity) context).finish();
                        }
                    }
                } else if (status.equals("0")) {
                    commonClass.DisplayToast(context, message, "bottom");
                }
            } else {

                commonClass.DisplayToast(context, context.getString(R.string.unknown_error_message), "bottom");
            }
        } catch (Exception e) {
            Log.d(TAG, "setAllProviders:error: " + e.getMessage());
        }
    }
}

