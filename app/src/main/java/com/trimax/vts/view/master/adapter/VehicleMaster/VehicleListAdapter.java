package com.trimax.vts.view.master.adapter.VehicleMaster;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trimax.vts.view.R;
import com.trimax.vts.model.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VehicleListAdapter extends BaseAdapter {
    Context context;
    List<Vehicle> sampleData=null;
    ArrayList<Vehicle> arraylist;

    int trackedvehicleId;


    public VehicleListAdapter(Context context, List<Vehicle> sampleData, int trackedvehId) {
        this.context = context;
        this.sampleData = sampleData;
        this.arraylist = new ArrayList<>();
        arraylist.addAll(sampleData);
        trackedvehicleId = trackedvehId;
        int selectedIndex = -1;
    }

    public void addVehicle(List<Vehicle> sampleData){
        this.sampleData=sampleData;
        notifyDataSetChanged();
    }

    public void setSelectedIndex(int ind) {
        trackedvehicleId = ind;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView CarIcon,txtName,CheckBoxIcon;
    }

    public int getCount() {

        return sampleData.size();
    }

    public Object getItem(int position) {
        return sampleData.get(position);
    }

    public long getItemId(int position) {
        return sampleData.indexOf(getItem(position));
    }

    public View getView(final int position, View rowView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.vehicle_list_item, null);
            holder = new ViewHolder();
            holder.CarIcon=(TextView)rowView.findViewById(R.id.txtCarIcon);
            holder.CheckBoxIcon=(TextView)rowView.findViewById(R.id.txtCheckBoxIcon);
            rowView.setTag(holder);
        }
        else {
            holder = (ViewHolder) rowView.getTag();
        }
        Typeface font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        holder.CarIcon.setTypeface(font_awesome);
        holder.CheckBoxIcon.setTypeface(font_awesome);

        if(trackedvehicleId != -1 && position == trackedvehicleId) {
            holder.CheckBoxIcon.setVisibility(holder.CheckBoxIcon.VISIBLE);
        }
        else {
            holder.CheckBoxIcon.setVisibility(holder.CheckBoxIcon.INVISIBLE);
        }
        TextView txtReg = (TextView) rowView.findViewById(R.id.txtRegNo);
        if(sampleData.get(position).getVehicleNo() != null){
            txtReg.setText(sampleData.get(position).getVehicleNo());
        }
        else{
            txtReg.setText("");
        }

        TextView txtMake = (TextView) rowView.findViewById(R.id.txtMake);
        txtMake.setVisibility(View.GONE);
        TextView txtmodel = (TextView) rowView.findViewById(R.id.txtModel);
        txtmodel.setVisibility(View.GONE);
        return rowView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sampleData.clear();
        if (charText.length() == 0) {
            sampleData.addAll(arraylist);
        } else {
            for ( Vehicle st : arraylist) {
                if (st.getVehicleNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    sampleData.add(st);
                }
            }
        }
        notifyDataSetChanged();
    }
}
