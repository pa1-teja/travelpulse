package com.trimax.vts.view.nearby;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trimax.vts.view.R;
import com.trimax.vts.view.nearby.models.PlaceServiceModel;

import java.util.List;

public class NearByPlaceServicesAdapter extends RecyclerView.Adapter<NearByPlaceServicesAdapter.PlaceHolder> {

    private List<PlaceServiceModel> serviceModels;
    private View.OnClickListener clickListener;

    public NearByPlaceServicesAdapter(List<PlaceServiceModel> serviceModels, View.OnClickListener clickListener) {
        this.serviceModels = serviceModels;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PlaceHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_places_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int i) {
        PlaceServiceModel model = serviceModels.get(i);
        holder.tv_title.setText(model.getService());
        holder.iv_icon.setImageResource(model.getImgRes());
        holder.iv_icon.setTag(model);
        holder.iv_icon.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    class PlaceHolder extends RecyclerView.ViewHolder{
        ImageView iv_icon;
        TextView tv_title;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
