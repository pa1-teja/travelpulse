package com.trimax.vts.view.maps.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trimax.vts.view.R;
import com.trimax.vts.view.model.AutoSuggestModel;

import java.util.List;

public class AutoSuggestAdapter extends RecyclerView.Adapter<AutoSuggestAdapter.AutoSuggestHolder> {
    private List<AutoSuggestModel> suggestModels;
    private View.OnClickListener clickListener;

    public AutoSuggestAdapter(List<AutoSuggestModel> suggestModels, View.OnClickListener clickListener) {
        this.suggestModels = suggestModels;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AutoSuggestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AutoSuggestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_autosuggest_place_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AutoSuggestHolder holder, int position) {
        AutoSuggestModel model = suggestModels.get(position);
        holder.tv_title.setText(model.getTitle());
        holder.tv_address.setText(model.getAddress().replace("<br/>"," "));
        holder.btn_select.setTag(model);
        holder.btn_select.setOnClickListener(clickListener);
    }

    public void addAddresses(List<AutoSuggestModel> suggestModels){
        this.suggestModels=suggestModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return suggestModels.size();
    }

    class AutoSuggestHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_address;
        ImageView btn_select;
        public AutoSuggestHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_address = itemView.findViewById(R.id.tv_address);
            btn_select = itemView.findViewById(R.id.iv_select);
        }
    }
}
