package com.trimax.vts.view.nearby;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trimax.vts.view.R;
import com.trimax.vts.view.nearby.models.InfoWindow;

import java.util.List;

public class NearByPlacesAdapter extends RecyclerView.Adapter<NearByPlacesAdapter.PlaceHolder> {

    private List<InfoWindow> infoWindows;
    private View.OnClickListener clickListener;

    public NearByPlacesAdapter(List<InfoWindow> infoWindows, View.OnClickListener clickListener) {
        this.infoWindows = infoWindows;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PlaceHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_window_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int i) {
        InfoWindow model = infoWindows.get(i);
        holder.tv_title.setText(model.getName());
        holder.tv_address.setText(model.getAddress().replace("<br/>",", "));
        holder.tv_distance.setText(model.getDistance());
        holder.tv_title.setTag(model);
        holder.tv_title.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return infoWindows.size();
    }

    public void addInfoWindow(List<InfoWindow> infoWindows){
        this.infoWindows = infoWindows;
        notifyDataSetChanged();
    }
    class PlaceHolder extends RecyclerView.ViewHolder{
        TextView tv_title ,tv_address, tv_distance ;
        LinearLayout ll_container;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_distance = itemView.findViewById(R.id.tv_distance);
            ll_container = itemView.findViewById(R.id.ll_container);
        }
    }
}
