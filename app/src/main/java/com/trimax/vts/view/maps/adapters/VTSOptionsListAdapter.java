package com.trimax.vts.view.maps.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trimax.vts.view.R;

public class VTSOptionsListAdapter extends BaseAdapter {

	 private LayoutInflater inflater;
	 private String[] optionHeader;
	 private String[] optiondetail;
	 private Drawable[] optionIndicator;
	 Typeface type;
	 
   public VTSOptionsListAdapter(Context ctx, String[] VTSOptionsHeader, String[] VTSOptionsDetail){
       
       inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       optionHeader = VTSOptionsHeader;
       optiondetail = VTSOptionsDetail;
       //optionIndicator = VTSOptionsIndicator;
       type = Typeface.createFromAsset(ctx.getAssets(), "fontawesome-webfont.ttf");
       
   }
   
   @Override
   public View getView(int position, View convertView, ViewGroup parent){
       View row = inflater.inflate(R.layout.vts_options_list_layout, parent, false);
       TextView body = (TextView) row.findViewById(R.id.vtsoptionheader);
       body.setText(optionHeader[position]);
       TextView time = (TextView) row.findViewById(R.id.vtsoptiondetail);
       time.setText(optiondetail[position]);
       /*if(position < 2){
    	   View indicator = row.findViewById(R.id.vtsoptionindicator);
    	   indicator.setVisibility(indicator.INVISIBLE);
       }*/
       
       return row;
   }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return optionHeader.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
