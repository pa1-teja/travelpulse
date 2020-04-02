package com.trimax.vts.view.reports;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.R;

import java.util.List;

public class AlertReportAdapter extends RecyclerView.Adapter<AlertReportAdapter.ReportHolder> {
    private static final String TAG = "AlertReportAdapter";
    private List<AlertReportModel> reportModels;

    public AlertReportAdapter(List<AlertReportModel> reportModels) {
        this.reportModels = reportModels;
    }

    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReportHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert_report_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {
        AlertReportModel model = reportModels.get(position);
        holder.tv_address.setText(model.getLocation());
        holder.tv_title.setText(model.getTitle());
        Log.d(TAG, "onBindViewHolder: "+model.getTitle());
        holder.tv_time.setText(Utils.getTimeInAMPM(model.getDateTime()));
        holder.tv_status.setText(getAlertShortForm(model.getTitle()));
        holder.tv_message.setText(model.getMsg());
    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

    public void addReports(List<AlertReportModel> reportModels){
        this.reportModels = reportModels;
        notifyDataSetChanged();
    }

    class ReportHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_address,tv_time,tv_status,tv_message;
        public ReportHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }

    private String getAlertShortForm(String alert){
        String shortForm="";
        switch (alert){
            case "Harsh Braking":
                shortForm="HB";
                break;
            case "Harsh Acceleration":
                shortForm="HA";
                break;
            case "Dangerous Speed":
                shortForm="DS";
                break;
            case "Ac Idle 10+10":
                shortForm="AI";
                break;
        }
        return shortForm;
    }
}
