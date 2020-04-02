package com.trimax.vts.view.master.adapter.VehicleGroup;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.trimax.vts.view.master.callbacks.AddGroupCallback;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupVehiclesListAdapter extends ArrayAdapter {

    private Context context;
    private String from;
    private ArrayList<String> list = new ArrayList<>();
    private HashMap<String, Boolean> hashmap;
    private AddGroupCallback callback;

    public GroupVehiclesListAdapter(@NonNull Context context, String from, HashMap<String, Boolean> hashmap, AddGroupCallback callback) {
        super(context, 0);
        this.context = context;
        this.from = from;
        this.hashmap = hashmap;
        this.callback = callback;
//        if (from.trim().equalsIgnoreCase(Constants.IVehicle.VEHICLE_LIST)) {
        list.addAll(hashmap.keySet());
        /*} else {
            for (String s : AddVehicleGroupActivity.usersHashMap.keySet()) {
                list.add(s);
            }
        }*/
    }

    @Override
    public int getCount() {
       /* if (from.trim().equalsIgnoreCase(Constants.IVehicle.VEHICLE_LIST))
            return AddVehicleGroupActivity.vehiclesHashMap.size();
        else
            return AddVehicleGroupActivity.usersHashMap.size();*/
       return hashmap.size();
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_concession_type, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.textview_concession_type);
        CheckBox ticket_selected = (CheckBox) view.findViewById(R.id.checkbox_);

        textView.setText(list.get(position));

        ticket_selected.setChecked(hashmap.get(list.get(position)));
        /*if (from.trim().equalsIgnoreCase(Constants.IVehicle.VEHICLE_LIST))
            ticket_selected.setChecked(AddVehicleGroupActivity.vehiclesHashMap.get(list.get(position)));
        else
            ticket_selected.setChecked(AddVehicleGroupActivity.usersHashMap.get(list.get(position)));
*/
        ticket_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (from.trim().equalsIgnoreCase(Constants.IVehicle.VEHICLE_LIST)) {
//                    AddVehicleGroupActivity.vehiclesHashMap.put(list.get(position), isChecked);
                    callback.updateVehiclesHashmap(list.get(position), isChecked);
                } else {
//                    AddVehicleGroupActivity.usersHashMap.put(list.get(position), isChecked);
                    callback.updateUsersHashmap(list.get(position), isChecked);
                }
            }
        });


        return view;
    }
}
