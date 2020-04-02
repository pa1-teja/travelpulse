package com.trimax.vts.view.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.trimax.vts.utils.Constant;
import com.trimax.vts.interfaces.ClickListner;
import com.trimax.vts.interfaces.PositionClickListener;
import com.trimax.vts.view.R;
import com.trimax.vts.model.NotificationPojo;
import com.trimax.vts.utils.Constants;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by keyurp on 8/4/17.
 */


public class NotificationFilterType extends RecyclerView.Adapter<NotificationFilterType.MyViewHolder> {

    private List<NotificationPojo> reportList;
    private List<NotificationPojo> reportListFiltered;
    ClickListner listner;
    String result="";
    public boolean isSelectedAll = false;
    NotificationPojo generateconnectionreport;
    SharedPreferences sharedpreference;
    String resultone="";
    PositionClickListener positionClickListener;


    public NotificationFilterType(Context activity, List<NotificationPojo> connectionList, PositionClickListener positionClickListener) {
        this.reportList = connectionList;
        isSelectedAll = false;
        this.reportListFiltered = connectionList;
        this.positionClickListener = positionClickListener;
        sharedpreference = activity.getSharedPreferences(Constants.app_preference_login,
                Context.MODE_PRIVATE);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView route_name;
        public TextView route_id;
        public CheckBox ticket_selected;


        public MyViewHolder(View view) {
            super(view);
            route_name = (TextView) view.findViewById(R.id.textview_);
            route_id = (TextView) view.findViewById(R.id.route_id);
            ticket_selected = (CheckBox) view.findViewById(R.id.checkbox_);

        }

    }

    @Override
    public int getItemCount() {
        return reportListFiltered.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_list_item_filter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.v("", "position--" + position);
        generateconnectionreport = reportListFiltered.get(position);
        holder.route_name.setText(generateconnectionreport.getTitle());
        holder.route_id.setText(generateconnectionreport.getNotification_id());
        holder.ticket_selected.setChecked(generateconnectionreport.isSelected());


        holder.ticket_selected.setEnabled(true);
        holder.ticket_selected.setClickable(true);
        holder.ticket_selected.setTag(reportList.get(position));
        holder.ticket_selected.setSelected(reportList.get(position).isSelected());
        holder.ticket_selected.setChecked(reportList.get(position).isSelected());

        //new................
        holder.ticket_selected.setOnCheckedChangeListener(null);
        holder.ticket_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                NotificationPojo contact = (NotificationPojo) cb.getTag();
               // reportList.get(position).setSelected(cb.isChecked());


                if (cb.isChecked()==true) {
                    contact.setSelected(true);
//                    contact.setSelected(cb.isChecked());
                    reportList.get(position).setSelected(cb.isChecked());
//                    reportListFiltered.get(position).setSelected(cb.isChecked());
//                    vehiclePojo.setSelected(cb.isChecked());
                    Log.e("position1", String.valueOf(position));

                    if (Constant.arraylist_position_t.contains(position)){

                    }else {
                        Constant.arraylist_position_t.add(position);
                    }

//                    Constant.arraylist_position_type.add(position);
//
//                    Collections.sort(Constant.arraylist_position_type);

//                    Constant.set.addAll(Constant.arraylist_position);
                    positionClickListener.itemClickedType(1, Constant.arraylist_position_t);

//                    positionClickListener.itemClicked(1,Constant.arraylist_position);


                } else {
                    contact.setSelected(false);
                    reportList.get(position).setSelected(cb.isChecked());
//                    reportListFiltered.get(position).setSelected(cb.isChecked());

                    Log.e("position2", String.valueOf(position));


                    for (Integer assurance : Constant.arraylist_position_t) {
                        if (assurance.equals(position))  {
                            Constant.arraylist_position_t.remove(assurance);
                            //You can exit the loop as you find a reference
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    positionClickListener.itemClickedType(2, Constant.arraylist_position_t);

                }






            }
        });

        holder.ticket_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //new....................

                Collections.sort(Constant.selectedStringstype);

                if (isChecked) {
                    if (Constant.selectedStringstype.contains(holder.route_id.getText().toString())) {

                    } else {
                        Constant.selectedStringstype.add(holder.route_id.getText().toString());
                        Collections.sort(Constant.selectedStringstype);

                         resultone = convertToString(Constant.selectedStringstype);

                        Log.e("xyz",result);

                    }

                } else {
                    Constant.selectedStringstype.remove(holder.route_id.getText().toString());
                    Collections.sort(Constant.selectedStringstype);


                }
                sharedpreference.edit().remove("selectalltype").commit();
                sharedpreference.edit().remove("selectonetype").commit();

                SharedPreferences.Editor editor = sharedpreference.edit();
                editor.putString("selectonetype",resultone);
                editor.commit();

            }
        });


    }

    @SuppressLint("LongLogTag")
    public void selectAll(boolean true_or_false) {
        Log.e("onClickSelectAll", "yes");
        if (true_or_false == true) {
            //listner.OnClick(0);

            isSelectedAll = true;
            Constant.selectedStringstype.clear();
            //   Toast.makeText(context, "check---"+this.reportList.size(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < reportList.size(); i++) {
                if (Constant.selectedStringstype.contains(reportList.get(i).getNotification_id())) {

                } else {

                    Constant.selectedStringstype.add(reportList.get(i).getNotification_id());
                     result = convertToString(Constant.selectedStringstype);

                    Log.e("selectall_arraylist_size", String.valueOf(Constant.selectedStringstype.size()));
                    Log.e("selectAll: ",result );


                }
            }
            sharedpreference.edit().remove("selectalltype").commit();
            SharedPreferences.Editor editor = sharedpreference.edit();
            editor.putString("selectalltype",result);
            editor.commit();


            //    Toast.makeText(context, "check---"+Constant.selectedStrings, Toast.LENGTH_LONG).show();


            notifyDataSetChanged();

        } else if (true_or_false == false) {
            Log.e("onClickSelectAll", "no");
            // listner.OnClick(0);
            isSelectedAll = false;
            Constant.selectedStringstype.clear();
            //  Toast.makeText(context, ""+Constant.selectedStrings, Toast.LENGTH_LONG).show();
            notifyDataSetChanged();

        }

    }


    static String convertToString(ArrayList<String> numbers) {
        StringBuilder builder = new StringBuilder();
        // Append all values in StringBuilder to the StringBuilder.
        for (String number : numbers) {
            builder.append(number);
            builder.append(",");
        }
        // Remove last delimiter with setLength.
        builder.setLength(builder.length() - 1);
        return builder.toString();

    }
}
