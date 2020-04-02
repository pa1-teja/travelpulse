package com.trimax.vts.view.vehicle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trimax.vts.view.R;
import com.trimax.vts.model.ModelVehiclePerformance;

import java.util.List;

public class VehiclePerformanceAdapter extends RecyclerView.Adapter<VehiclePerformanceAdapter.PerformanceHolder> {
    private static final String TAG = "VehiclePerformanceAdapt";
    private List<ModelVehiclePerformance> performanceModels;
    private View.OnClickListener clickListener;

    public VehiclePerformanceAdapter(List<ModelVehiclePerformance> performanceModels) {
        this.performanceModels = performanceModels;
    }

    @NonNull
    @Override
    public PerformanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PerformanceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_performance_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PerformanceHolder holder, int position) {
        ModelVehiclePerformance model = performanceModels.get(position);
        holder.progressBar.setProgressDrawable(model.getProgressDrawble());
        holder.progressBar.setProgress((Integer.parseInt(model.getNotification_count())+1));
        if (clickListener!=null){
            holder.progressBar.setTag(model);
            holder.progressBar.setOnClickListener(clickListener);
        }

        holder.tv_title.setText(model.getNotification_name());
        holder.tv_score.setText(model.getNotification_count());
    }

    @Override
    public int getItemCount() {
        return performanceModels.size();
    }

    public void addPerformance(List<ModelVehiclePerformance> performanceModels) {
        this.performanceModels = performanceModels;
        notifyDataSetChanged();
    }

    public void addPerformance(List<ModelVehiclePerformance> performanceModels, View.OnClickListener clickListener) {
        this.clickListener=clickListener;
        this.performanceModels = performanceModels;
        notifyDataSetChanged();
    }

    class PerformanceHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_score;
        ProgressBar progressBar;

        public PerformanceHolder(View itemView) {
            super(itemView);
            progressBar=itemView.findViewById(R.id.progressBar);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_score = itemView.findViewById(R.id.tv_score);
        }
    }
}
