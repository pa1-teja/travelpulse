package com.trimax.vts.view.master.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trimax.vts.view.R;
import com.trimax.vts.view.maps.RealTimeTrackingActivity;
import com.trimax.vts.model.Current_Status;
import com.trimax.vts.utils.Constants;

import java.util.ArrayList;

import static com.trimax.vts.utils.CommonClass.VehicleValue;

/**
 * Created by kiranp on 5/3/2018.
 */

public class VehicleStatusAdapter extends RecyclerView.Adapter<VehicleStatusAdapter.MyViewHolder> implements Filterable
{
    private ArrayList<Current_Status> imageParametersList;
    private Context context;
    private ArrayList<Current_Status> mFilteredList;
    SharedPreferences sharedpreference;
    private final Context mContext;

    public VehicleStatusAdapter(ArrayList<ArrayList<Current_Status>> imageParametersList, Context context, int id) {
        this.imageParametersList = imageParametersList.get(0);
        this.context = context;
        this.mFilteredList = imageParametersList.get(0);
        mContext = context;
        sharedpreference = context.getSharedPreferences(Constants.app_preference_login, Context.MODE_PRIVATE);
    }

    @Override
    public VehicleStatusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_vehicle_status, parent, false);
        Log.d("Set infalter",""+v);
        return new VehicleStatusAdapter.MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final VehicleStatusAdapter.MyViewHolder holder, final int position) {
        final ArrayList<Current_Status> pr = mFilteredList;
        final Current_Status Current_Statuss=mFilteredList.get(position);
        holder.txtvehicleNO.setText(Html.fromHtml( pr.get(position).getVehicleNo().trim()));
        holder.tv_duration.setText(pr.get(position).getStatuUpdatedHuman());
         final String  ss= pr.get(position).getVid().trim();
        if(pr.get(position).getVehicleStatus().equalsIgnoreCase("moving")) {
            holder.imgDelete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_place_green));
            holder.txtVehicleName.setText("Moving");
        }else if(pr.get(position).getVehicleStatus().equalsIgnoreCase("stop")) {
            holder.imgDelete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_place));
            holder.txtVehicleName.setText("Stop");
        }
        else if(pr.get(position).getVehicleStatus().equalsIgnoreCase("idle")) {
            holder.imgDelete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_place_orange));
            holder.txtVehicleName.setText("Idle");
        }
        else if(pr.get(position).getVehicleStatus().equalsIgnoreCase("No Network")) {
            holder.imgDelete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_place_black));
            holder.txtVehicleName.setText("No Network");
        }

      VehicleValue=ss;
          holder.linlayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  sharedpreference = context.getSharedPreferences(Constants.app_preference_login, Context.MODE_PRIVATE);
                  SharedPreferences.Editor editor = sharedpreference.edit();
                  editor.putString("vid",pr.get(position).getVid().trim());
                  editor.putString("livetrack","");
                  editor.commit();
                  VehicleValue=Current_Statuss.getVid();
                  Intent ii = new Intent(context, RealTimeTrackingActivity.class);
                  ii.putExtra("vid",pr.get(position).getVid().trim());
                  context.startActivity(ii);
              }
          });

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = imageParametersList;
                } else {
                    ArrayList<Current_Status> filteredList = new ArrayList<>();

                    for (int i = 0; i< mFilteredList.size(); i++) {
                        //FILTER
                        if (mFilteredList.get(i).getVehicleNo().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(mFilteredList.get(i));
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Current_Status>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class  MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtvehicleNO,txtVehicleName,tv_duration;
        private LinearLayout linlayout;
        private ImageView imgDelete;

        MyViewHolder(View itemView) {
            super(itemView);

            txtvehicleNO = (TextView) itemView.findViewById(R.id.vehicle_no);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            txtVehicleName = (TextView) itemView.findViewById(R.id.vehicle_name);
            imgDelete=(ImageView) itemView.findViewById(R.id.vehicle_location);
            linlayout=(LinearLayout)itemView.findViewById(R.id.linlayout);
        }
    }

}