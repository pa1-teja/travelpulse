package com.trimax.vts.helper;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;


public class ParseGooglePlacesHelper extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
	 
    JSONObject jObject;
    Context ctx;
    AutoCompleteTextView aedtArea;

    @Override
    protected List<HashMap<String, String>> doInBackground(String... jsonData) {

        List<HashMap<String, String>> places = null;

        GooglePlaceJSONParser placeJsonParser = new GooglePlaceJSONParser();

        try{
            jObject = new JSONObject(jsonData[0]);

            // Getting the parsed data as a List construct
            places = placeJsonParser.parse(jObject);

        }catch(Exception e){
            //Log.d("Exception",e.toString());
        }
        return places;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> result) {

        String[] from = new String[] { "description"};
        int[] to = new int[] { android.R.id.text1 };

        // Creating a SimpleAdapter for the AutoCompleteTextView
        SimpleAdapter adapter = new SimpleAdapter(ctx, result, android.R.layout.simple_list_item_1, from, to);

        // Setting the adapter
        aedtArea.setAdapter(adapter);
    }
}
