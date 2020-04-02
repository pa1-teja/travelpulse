package com.trimax.vts.view.complaints;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.R;
import com.trimax.vts.view.complaints.models.TicketModel;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintHolder> {
    private static final String TAG = "ComplaintAdapter";
    private List<TicketModel> complaints;
    private View.OnClickListener clickListener;

    public ComplaintAdapter(List<TicketModel> complaints,View.OnClickListener clickListener) {
        this.complaints = complaints;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComplaintHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintHolder holder, int position) {
        TicketModel model = complaints.get(position);
        holder.tv_ticket_no.setText(model.getTicketNo());
        holder.tv_issue_date.setText(Utils.getDateTime(model.getCreatedOn()));
        holder.tv_issue_description.setText(model.getProblemFaced());
        if (model.getSolution()!=null && !model.getSolution().isEmpty()) {
            holder.tv_solution.setVisibility(View.VISIBLE);
            holder.tv_issue_solution.setVisibility(View.VISIBLE);
            holder.tv_issue_solution.setText( model.getSolution());
        }
        else {
            holder.tv_solution.setVisibility(View.GONE);
            holder.tv_issue_solution.setVisibility(View.GONE);
        }
        holder.tv_status_at.setText(model.getStatus());
        if (model.getStatus().equalsIgnoreCase("close"))
            holder.tv_status_at.setTextColor(Color.parseColor("#228329"));
        else
            holder.tv_status_at.setTextColor(Color.parseColor("#EF4C37"));

        holder.iv_reply.setTag(model);
        holder.iv_reply.setOnClickListener(clickListener);
        holder.iv_details.setTag(model);
        holder.iv_details.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    public void addComplaints(List<TicketModel> complaints){
        this.complaints=complaints;
        notifyDataSetChanged();
    }

    class ComplaintHolder extends RecyclerView.ViewHolder {
        TextView tv_ticket_no,tv_issue_date,tv_issue_description,tv_issue_solution,tv_solution;
        ImageView iv_reply,iv_details;
        MaterialButton tv_status_at;

        public ComplaintHolder(@NonNull View itemView) {
            super(itemView);
            tv_ticket_no = itemView.findViewById(R.id.tv_ticket_no);
            tv_issue_date = itemView.findViewById(R.id.tv_issue_date);
            tv_issue_description = itemView.findViewById(R.id.tv_issue_description);
            tv_status_at = itemView.findViewById(R.id.tv_status_at);
            tv_issue_solution = itemView.findViewById(R.id.tv_issue_solution);
            tv_solution = itemView.findViewById(R.id.tv_solution);
            iv_reply = itemView.findViewById(R.id.iv_reply);
            iv_details = itemView.findViewById(R.id.iv_details);
        }
    }
}
