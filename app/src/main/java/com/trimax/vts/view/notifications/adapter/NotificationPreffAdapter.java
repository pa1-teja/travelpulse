package com.trimax.vts.view.notifications.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.trimax.vts.view.master.model.VehicleMaster.NotificationPreference.NotificationPrefModel;
import com.trimax.vts.view.R;

import java.util.List;

public class NotificationPreffAdapter extends RecyclerView.Adapter<NotificationPreffAdapter.NotificationPrefHolder> {

    private List<NotificationPrefModel> notificationPrefModels;


    public NotificationPreffAdapter(List<NotificationPrefModel> notificationPrefModels, View.OnClickListener clickListener) {
        this.notificationPrefModels = notificationPrefModels;
    }



    public void addNotifiation(List<NotificationPrefModel> notificationPrefModels){
        this.notificationPrefModels=notificationPrefModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationPrefHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NotificationPrefHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification_pref,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationPrefHolder holder, final int i) {
        NotificationPrefModel model = notificationPrefModels.get(i);
        if (model.getAlarmApplicable().equalsIgnoreCase("Y"))
            holder.cb_alarm.setVisibility(View.VISIBLE);
        else
            holder.cb_alarm.setVisibility(View.GONE);
        holder.tv_title.setText(model.getTitle());
        if (model.getIsOn().equalsIgnoreCase("1"))
            holder.cb_notification.setChecked(true);
        else
            holder.cb_notification.setChecked(false);
        holder.tv_title.setText(model.getTitle());
        if (model.getIsOn()!=null)
            holder.cb_notification.setChecked(model.getIsOn().equals("1"));
        if (model.getAlarmOn()!=null)
            holder.cb_alarm.setChecked(model.getAlarmOn().equals("1"));

        holder.cb_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    notificationPrefModels.get(i).setIsOn("1");
                else {
                    notificationPrefModels.get(i).setIsOn("0");
                    notificationPrefModels.get(i).setAlarmOn("0");
                    holder.cb_alarm.setChecked(false);
                }
            }
        });

        holder.cb_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationPrefModels.get(i).setAlarmOn("1");
                    holder.cb_notification.setChecked(true);
                    notificationPrefModels.get(i).setIsOn("1");
                }
                else
                    notificationPrefModels.get(i).setAlarmOn("0");
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationPrefModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public String getNotificationData(){
        StringBuilder data= new StringBuilder();
        for (int i = 0; i <notificationPrefModels.size() ; i++) {
            NotificationPrefModel model = notificationPrefModels.get(i);
            data.append(model.getNotificationTypeId()).append(",").append(model.getIsOn()).append(",").append(model.getAlarmOn()).append("|");
        }
        return data.toString();
    }

    class NotificationPrefHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        CheckBox cb_notification,cb_alarm;

        public NotificationPrefHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            cb_notification = itemView.findViewById(R.id.cb_notification);
            cb_alarm = itemView.findViewById(R.id.cb_alarm);
        }
    }
}
