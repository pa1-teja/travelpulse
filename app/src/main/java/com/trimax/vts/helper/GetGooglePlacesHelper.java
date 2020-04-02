package com.trimax.vts.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.view.R;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

public class GetGooglePlacesHelper extends AsyncTask<String, Void, String> {
	
	CommonClass commonClass;
	Context ctx;
	AutoCompleteTextView aedtArea;
	
    @Override
    protected String doInBackground(String... place) {
        // For storing data from web service

    	commonClass = new CommonClass(ctx);
    	String data = "";

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=" + ctx.getString(R.string.google_api_key_places);

        String input="";

        try {
            input = "input=" + URLEncoder.encode(place[0], "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input+"&"+types+"&"+sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

        try{
            // Fetching the data from we service
            data = commonClass.downloadUrl(url);
        }catch(Exception e){
            //Log.d("Background Task",e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(commonClass.isConnected(ctx)) {
	        // Creating ParserTask
	        ParseGooglePlacesHelper parsePlaces = new ParseGooglePlacesHelper();
	        parsePlaces.ctx = ctx;
	        parsePlaces.aedtArea = aedtArea;
	
	        // Starting Parsing the JSON string returned by Web Service
	        parsePlaces.execute(result);
        }
        else {
        	commonClass.DisplayToast(ctx, ctx.getString(R.string.network_error_message), "bottom");
        }
    }
}
