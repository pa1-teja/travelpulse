package com.trimax.vts.view.notifications.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.trimax.vts.view.R;
import com.trimax.vts.view.model.NotificationSettingModel;

import java.util.List;

public class NotificationSettingAdapter extends RecyclerView.Adapter<NotificationSettingAdapter.SettingHolder>{
    private List<NotificationSettingModel> settings;
    private View.OnClickListener clickListener;

    public NotificationSettingAdapter(List<NotificationSettingModel> settings, View.OnClickListener clickListener) {
        this.settings = settings;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SettingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_setting_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SettingHolder holder, int position) {
        NotificationSettingModel model = settings.get(position);
        holder.tv_title.setText(model.getTitle());
        if (TextUtils.isEmpty(model.getContent()))
            holder.tv_info.setVisibility(View.GONE);
        else
            holder.tv_info.setVisibility(View.VISIBLE);
        holder.tv_info.setText(Html.fromHtml(model.getContent()));
        holder.tv_setting.setText(Html.fromHtml(model.getSetting()));
        holder.btn_setting.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    public void addSettings(List<NotificationSettingModel> settings){
        this.settings=settings;
        notifyDataSetChanged();
    }
    class SettingHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_info,tv_setting;
        MaterialButton btn_setting;

        public SettingHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_info = itemView.findViewById(R.id.tv_info);
            tv_setting = itemView.findViewById(R.id.tv_setting);
            btn_setting = itemView.findViewById(R.id.btn_setting);
        }
    }
}
