package com.trimax.vts.view.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.trimax.vts.view.R;
import com.trimax.vts.view.model.UserModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {
    private static final String TAG = "UsersAdapter";
    private List<UserModel> users;
    private View.OnClickListener clickListener;

    public UsersAdapter(List<UserModel> users, View.OnClickListener clickListener) {
        this.users = users;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_view,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        UserModel user = users.get(position);
        holder.tv_name.setText(user.getName()+" ("+user.getRelation()+")");
        holder.tv_mobile.setText(user.getMobile());
        holder.tv_vehicle.setText(user.getVehicle());
        holder.tv_status.setText(user.getStatus());

        holder.cp_active.setTag(user);
        holder.cp_active.setOnClickListener(clickListener);
        holder.rl_container.setTag(user);
        holder.rl_container.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addUsers(List<UserModel> users){
        this.users=users;
        notifyDataSetChanged();
    }

    class UserHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_mobile,tv_status,tv_vehicle;
        RelativeLayout rl_container;
        Chip cp_active;

        public UserHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_vehicle = itemView.findViewById(R.id.tv_vehicle);
            tv_status = itemView.findViewById(R.id.tv_status);
            cp_active = itemView.findViewById(R.id.cp_active);
            rl_container = itemView.findViewById(R.id.rl_container);
        }
    }
}
