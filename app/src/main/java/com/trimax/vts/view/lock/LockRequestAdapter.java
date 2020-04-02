package com.trimax.vts.view.lock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trimax.vts.view.R;

import java.util.List;

public class LockRequestAdapter extends RecyclerView.Adapter<LockRequestAdapter.RequestHolder> {
    private List<RequestModel> requests;

    public LockRequestAdapter(List<RequestModel> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_last_request_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        RequestModel model = requests.get(position);
        holder.tv_status.setText(model.getStatus());
        holder.tv_time.setText(model.getTime());
        holder.tv_type.setText(model.getType());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestHolder extends RecyclerView.ViewHolder{
        TextView tv_status,tv_type,tv_time;

        RequestHolder(@NonNull View itemView) {
            super(itemView);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
