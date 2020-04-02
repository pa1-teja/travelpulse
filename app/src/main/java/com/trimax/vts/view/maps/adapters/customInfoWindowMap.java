package com.trimax.vts.view.maps.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.trimax.vts.view.R;
import com.trimax.vts.model.InfoWindowData;

public class customInfoWindowMap implements GoogleMap.InfoWindowAdapter {

        private Context context;

        public customInfoWindowMap(Activity ctx){
            context = ctx;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = ((Activity)context).getLayoutInflater().inflate(R.layout.infowindowlayoutnew, null);

            TextView name_tv = view.findViewById(R.id.name);
            TextView details_tv = view.findViewById(R.id.details);
            ImageView img = view.findViewById(R.id.pic);
            TextView ac = view.findViewById(R.id.ac);
            TextView date = view.findViewById(R.id.date);

            TextView hotel_tv = view.findViewById(R.id.hotels);
            TextView food_tv = view.findViewById(R.id.food);
            TextView transport_tv = view.findViewById(R.id.transport);
            InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
            hotel_tv.setText(infoWindowData.getLocation()!=null?infoWindowData.getLocation():"");
            return view;
        }
    }


