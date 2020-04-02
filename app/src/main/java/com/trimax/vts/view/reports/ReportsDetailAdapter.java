package com.trimax.vts.view.reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.R;

import java.time.Duration;
import java.util.List;

public class ReportsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ReportsDetailAdapter";
    private final int TYPE_MOVING=0;
    private final int TYPE_NON_MOVING=1;
    private List<ReportDetailModel> reportDetailModels;

    public ReportsDetailAdapter(List<ReportDetailModel> reportDetailModels) {
        this.reportDetailModels = reportDetailModels;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==TYPE_MOVING)
            return new ReportMovingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_timeline_moving,parent,false));
        else
            return new ReportIdleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_timeline,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReportDetailModel model = reportDetailModels.get(position);
        if (getItemViewType(position)==TYPE_MOVING){
            ((ReportMovingHolder) holder).tv_status.setText("M");
            ((ReportMovingHolder) holder).tv_distance.setText("Distance:  "+model.getDistance());
            ((ReportMovingHolder) holder).tv_status.setBackgroundResource(R.drawable.shape_circle_green);
            ((ReportMovingHolder) holder).view_status.setBackgroundResource(R.drawable.bg_green_gradient);
            ((ReportMovingHolder) holder).tv_duration.setText("Duration:  "+model.getDurationHoursMin());
            ((ReportMovingHolder) holder).tv_address.setText(model.getStartPointLocation());
            ((ReportMovingHolder) holder).tv_to_address.setText(model.getEndPointLocation());
            ((ReportMovingHolder) holder).tv_title.setText(Utils.getTimeInAMPM(model.getStartDate()) +"        TO        "+Utils.getTimeInAMPM(model.getEndDate()));
        }else {
            ((ReportIdleHolder) holder).tv_duration.setText("Duration:  "+model.getDurationHoursMin());
            ((ReportIdleHolder) holder).tv_address.setText(model.getStartPointLocation());
            ((ReportIdleHolder) holder).tv_title.setText(Utils.getTimeInAMPM(model.getStartDate()) +"        TO         "+Utils.getTimeInAMPM(model.getEndDate()));
            if (model.getVehicleStatus().equalsIgnoreCase("stop")) {
                ((ReportIdleHolder) holder).tv_status.setText("S");
                ((ReportIdleHolder) holder).tv_status.setBackgroundResource(R.drawable.shape_circle_red);
                ((ReportIdleHolder) holder).view_status.setBackgroundResource(R.drawable.bg_red_gradient);
            }else {
                ((ReportIdleHolder) holder).tv_status.setText("I");
                ((ReportIdleHolder) holder).tv_status.setBackgroundResource(R.drawable.shape_circle_orange);
                ((ReportIdleHolder) holder).view_status.setBackgroundResource(R.drawable.bg_orange_gradient);
            }
        }
    }


    @Override
    public int getItemCount() {
        return reportDetailModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        ReportDetailModel model = reportDetailModels.get(position);
        if (model.getVehicleStatus().equalsIgnoreCase("run"))
            return TYPE_MOVING;
        else
            return TYPE_NON_MOVING;
    }

    public void add(List<ReportDetailModel> reportDetailModels){
        this.reportDetailModels=reportDetailModels;
        notifyDataSetChanged();
    }

    class ReportMovingHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_address,tv_to_address,tv_duration,tv_status,tv_distance;
        View view_status;

        public ReportMovingHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_to_address = itemView.findViewById(R.id.tv_to_address);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            view_status = itemView.findViewById(R.id.view_status);
            tv_distance = itemView.findViewById(R.id.tv_distance);
        }
    }

    class ReportIdleHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_address,tv_duration,tv_status;
        View view_status;

        public ReportIdleHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            view_status = itemView.findViewById(R.id.view_status);
        }
    }
}
