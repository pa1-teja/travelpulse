package com.trimax.vts.view.nearby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.trimax.vts.view.R;
import com.trimax.vts.view.nearby.models.InfoWindow;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private View.OnClickListener clickListener;

    public CustomInfoWindowGoogleMap(Context context) {
        this.context = context;
        //this.clickListener =
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_info_window_view,null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        //tv_title.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        TextView tv_address = view.findViewById(R.id.tv_address);
        TextView tv_distance = view.findViewById(R.id.tv_distance);

        InfoWindow info = (InfoWindow) marker.getTag();
        if (info!=null){
            if (info.getAddress().isEmpty())
                tv_address.setVisibility(View.GONE);
            else
                tv_address.setVisibility(View.VISIBLE);
            if (info.getDistance().isEmpty())
                tv_distance.setVisibility(View.GONE);
            else
                tv_distance.setVisibility(View.VISIBLE);
            tv_title.setText(info.getName());
            tv_address.setText(info.getAddress().replace("<br/>",", "));
            tv_distance.setText(info.getDistance());
        }
        return view;
    }
}
