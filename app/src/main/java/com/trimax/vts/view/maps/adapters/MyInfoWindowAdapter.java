package com.trimax.vts.view.maps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.trimax.vts.view.R;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    public MyInfoWindowAdapter(Context context){
        myContentsView = LayoutInflater.from(context).inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView tvTitle = (myContentsView.findViewById(R.id.title));
        tvTitle.setText(marker.getTitle());
        TextView tvSnippet = (myContentsView.findViewById(R.id.snippet));
        tvSnippet.setText(marker.getSnippet());

        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }

}
