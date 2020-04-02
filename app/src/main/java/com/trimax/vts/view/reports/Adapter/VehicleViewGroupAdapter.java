package com.trimax.vts.view.reports.Adapter;

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
import com.trimax.vts.model.VehicleViewGroup;
import com.trimax.vts.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.trimax.vts.utils.Constant.arraylist_alertTypeTitle;

/**
 * Created by keyurp on 8/4/17.
 */


public class VehicleViewGroupAdapter extends RecyclerView.Adapter<VehicleViewGroupAdapter.MyViewHolder> {

    PositionClickListener positionClickListener;
    private List<VehicleViewGroup> reportList;
    private List<VehicleViewGroup> reportListFiltered;
    ClickListner listner;
    String result = "";
    private Context context;
    public boolean isSelectedAll = false;
    VehicleViewGroup generateconnectionreport;
    private boolean onBind;
    SharedPreferences sharedpreference;
    String resultone = "";
    ClickListner clickListner;

    public VehicleViewGroupAdapter(Context activity, List<VehicleViewGroup> connectionList, PositionClickListener positionClickListener, ClickListner cl) {
        this.context = activity;
        this.reportList = connectionList;
        isSelectedAll = false;
        this.reportListFiltered = connectionList;
        int selectedIndex = -1;
        this.positionClickListener = positionClickListener;
         clickListner=cl;
        sharedpreference = context.getSharedPreferences(Constants.app_preference_login,
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
        holder.route_id.setText(generateconnectionreport.getId());
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
                VehicleViewGroup contact = (VehicleViewGroup) cb.getTag();

                if (cb.isChecked()) {
                    contact.setSelected(true);
                    reportList.get(position).setSelected(cb.isChecked());
                    Log.e("position_000", String.valueOf(position));

                    if (Constant.arraylist_position.contains(position)){

                    }else {

                        Constant.arraylist_position.add(position);
                        arraylist_alertTypeTitle.add(generateconnectionreport.getTitle());
                        clickListner.Onclickclr(arraylist_alertTypeTitle.size(),"group",context);
                    }

                    positionClickListener.itemClicked(1, Constant.arraylist_position,true);



                }
                else {
                    contact.setSelected(false);
                    reportList.get(position).setSelected(cb.isChecked());
                    for (Integer assurance : Constant.arraylist_position) {
                        if (assurance.equals(position))  {
                            Constant.arraylist_position.remove(assurance);
                            arraylist_alertTypeTitle.remove(generateconnectionreport.getTitle());
                            clickListner.Onclickclr(arraylist_alertTypeTitle.size(),"group",context);
                            //You can exit the loop as you find a reference
                            break;
                        }
                    }
                    notifyDataSetChanged();

                    Log.e( "1111: ", String.valueOf(Constant.arraylist_position));
                    Log.e( "size1111: ", String.valueOf(Constant.arraylist_position.size()));

                    positionClickListener.itemClicked(2, Constant.arraylist_position,true);


                }

            }
        });




        holder.ticket_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //new....................

                Collections.sort(Constant.selectedStrings);
//                Collections.sort(Constant.arraylist_position);

                if (isChecked) {

                    if (Constant.selectedStrings.contains(holder.route_id.getText().toString())) {

                    } else {
                        Constant.selectedStrings.add(holder.route_id.getText().toString());
                        Collections.sort(Constant.selectedStrings);
                        resultone = convertToString(Constant.selectedStrings);
                        Log.e("single_arraylist_size", String.valueOf(Constant.selectedStrings.size()));
                        Log.e( "single_string: ", resultone);
                    }

                } else {

                    Constant.selectedStrings.remove(holder.route_id.getText().toString());
                    Collections.sort(Constant.selectedStrings);

                }
                sharedpreference.edit().remove("selectall").commit();
                sharedpreference.edit().remove("selectone").commit();
                SharedPreferences.Editor editor = sharedpreference.edit();
                editor.putString("selectone", resultone);
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
            Constant.first=false;
            Constant.selectedStrings.clear();
            //   Toast.makeText(context, "check---"+this.reportList.size(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < reportList.size(); i++) {
                if (Constant.selectedStrings.contains(reportList.get(i).getId())) {

                } else {

                    Constant.selectedStrings.add(reportList.get(i).getId());
                    result = convertToString(Constant.selectedStrings);

                    Log.e("selectall_arraylist_size", String.valueOf(Constant.selectedStrings.size()));
                    Log.e("selectAll: ", result);


                }
            }
            SharedPreferences.Editor editor = sharedpreference.edit();
            editor.putString("selectall", result);
            editor.commit();


            //    Toast.makeText(context, "check---"+Constant.selectedStrings, Toast.LENGTH_LONG).show();


            notifyDataSetChanged();

        } else if (true_or_false == false) {
            Log.e("onClickSelectAll", "no");
            // listner.OnClick(0);
            isSelectedAll = false;
            Constant.selectedStrings.clear();
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
