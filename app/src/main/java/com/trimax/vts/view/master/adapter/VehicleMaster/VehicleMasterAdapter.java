package com.trimax.vts.view.master.adapter.VehicleMaster;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.trimax.vts.view.master.model.VehicleMaster.VehicleMasterModel;
import com.trimax.vts.view.R;

import java.util.ArrayList;

public class VehicleMasterAdapter extends RecyclerView.Adapter<VehicleMasterAdapter.VehicleMasterViewHolder> implements Filterable {

    private ArrayList<VehicleMasterModel> vehicleMasterModels;
    private ArrayList<VehicleMasterModel> filteredVehicleMasterModels;
    private String TAG = VehicleMasterAdapter.class.getSimpleName();
    private View.OnClickListener clickListener;
    private CompoundButton.OnCheckedChangeListener checklistner;

    public VehicleMasterAdapter(View.OnClickListener clickListener, ArrayList<VehicleMasterModel> vehicleMasterModels, CompoundButton.OnCheckedChangeListener checklistner) {
        this.vehicleMasterModels = vehicleMasterModels;
        filteredVehicleMasterModels = vehicleMasterModels;
        this.clickListener = clickListener;
        this.checklistner = checklistner;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                Log.d(TAG, "charSequence: "+charString);

                if (charString.isEmpty()) {
                    filteredVehicleMasterModels = vehicleMasterModels;
                } else {
                    ArrayList<VehicleMasterModel> filteredList = new ArrayList<>();

                    for (int i = 0; i< filteredVehicleMasterModels.size(); i++) {
                        //FILTER
                        if (filteredVehicleMasterModels.get(i).getVehicleNo().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(filteredVehicleMasterModels.get(i));
                        }
                    }

                    filteredVehicleMasterModels = filteredList;
                    Log.d(TAG, "filteredList: "+filteredList.size());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredVehicleMasterModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredVehicleMasterModels = (ArrayList<VehicleMasterModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public VehicleMasterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_master, parent, false);
        Log.d("Set infalter",""+v);
        return new VehicleMasterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleMasterViewHolder holder, int position) {
        final VehicleMasterModel model = filteredVehicleMasterModels.get(position);
        holder.tvModelName.setText(String.format("%s %s", model.getMake() == null ? " " : model.getMake(), model.getModel() == null ? " " : model.getModel()));
        holder.tvVehicleNo.setText(model.getVehicleNo()==null?"":model.getVehicleNo());
        holder.parking_mode.setOnCheckedChangeListener(null);
        if (model.getParking_mode()!=null) {
            holder.parking_mode.setChecked(model.getParking_mode().equalsIgnoreCase("1"));
        }else {
            holder.parking_mode.setChecked(false);
        }
        holder.iv_option.setTag(model);
        holder.iv_option.setOnClickListener(clickListener);
        holder.parking_mode.setTag(model);
        holder.parking_mode.setOnCheckedChangeListener(checklistner);

    }

    public void addVehicles(ArrayList<VehicleMasterModel> vehicleMasterModels){
        this.vehicleMasterModels=vehicleMasterModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredVehicleMasterModels.size();
    }

    class VehicleMasterViewHolder extends RecyclerView.ViewHolder {
        TextView tvVehicleNo, tvModelName;
        ImageView iv_option;
        Switch parking_mode;

        public VehicleMasterViewHolder(View itemView) {
            super(itemView);
            tvModelName = itemView.findViewById(R.id.tv_vehicle_model_name);
            tvVehicleNo = itemView.findViewById(R.id.tv_vehicle_no);
            iv_option = itemView.findViewById(R.id.iv_option);
            parking_mode = (Switch) itemView.findViewById(R.id.parking_mode);
        }
    }


}
