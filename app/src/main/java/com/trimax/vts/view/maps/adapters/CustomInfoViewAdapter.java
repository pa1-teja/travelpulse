package com.trimax.vts.view.maps.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.trimax.vts.view.R;
import com.trimax.vts.model.InfoWindowData;

public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    View view;
    LayoutInflater inflater;

    public CustomInfoViewAdapter(Context c) {
        context = c;
    }

    @Override public View getInfoWindow(Marker marker) {
        try {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.info_window_reply, null);
            Log.e("on C","");

            LinearLayout parentLayout = (LinearLayout)view.findViewById(R.id.layout_info);
            InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

                if ((infoWindowData.getNotiType() != null)) {

                    String Notification_Data=infoWindowData.getNotiType();
                    String [] Notification_Data_array=Notification_Data.split("#");
                    final TextView[] myTextViews = new TextView[Notification_Data_array.length]; // create an empty array;

                    for (int i = 0; i < Notification_Data_array.length; i++){
                        TextView rowTextView = new TextView(context);
                        rowTextView.setTextSize(11.0f);
                        rowTextView.setGravity(Gravity.LEFT);
                        rowTextView.setPadding(5,5,5,5);
                        rowTextView.setText("  "+Notification_Data_array[i]);
                         myTextViews[i] = rowTextView;
                        View v = new View(context);
                        v.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                2
                        ));
                        v.setBackgroundColor(Color.parseColor("#000000"));
                        // Add the text view to the parent layout
                        parentLayout.addView(rowTextView);
                        parentLayout.addView(v);
                    }
                }

            if ((infoWindowData.getLocation() != null)) {
                TextView rowTextVieww = new TextView(context);
                rowTextVieww.setText("  "+infoWindowData.getLocation());
                rowTextVieww.setTextSize(13.0f);
                rowTextVieww.setTypeface(rowTextVieww.getTypeface(), Typeface.BOLD);

                // Add the text view to the parent layout
                parentLayout.addView(rowTextVieww);
            }
        }catch (NullPointerException e) {
            e.printStackTrace();

        }

        return view;
    }

    @Override public View getInfoContents(Marker marker) {
        return null;
    }
}
