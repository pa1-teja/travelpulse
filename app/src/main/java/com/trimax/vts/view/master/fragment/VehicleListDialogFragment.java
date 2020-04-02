package com.trimax.vts.view.master.fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.trimax.vts.view.master.adapter.VehicleGroup.GroupVehiclesListAdapter;
import com.trimax.vts.view.master.callbacks.AddGroupCallback;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import java.util.HashMap;

public class VehicleListDialogFragment extends DialogFragment{

    private Context context;
    private String from;
    private HashMap<String, Boolean> hashMap;
    private AddGroupCallback callback;

    @SuppressLint("ValidFragment")
    public VehicleListDialogFragment(Context context, String from, HashMap<String, Boolean> hashmap, AddGroupCallback callback) {
        this.context = context;
        this.from = from;
        this.hashMap = hashmap;
        this.callback = callback;
    }

    public VehicleListDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.partial_custom_dialog, null);

        RelativeLayout relativeLayout = view.findViewById(R.id.rl_search_view_select_all);
        relativeLayout.setVisibility(View.GONE);

        ListView listView = view.findViewById(R.id.listView_dialog_items);

        Button btnOk = view.findViewById(R.id.dialogok);
        Button btnCancel = view.findViewById(R.id.dialogcancel);

        GroupVehiclesListAdapter adapter = new GroupVehiclesListAdapter(context, from, hashMap, callback);
        listView.setAdapter(adapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.trim().equalsIgnoreCase(Constants.IVehicle.VEHICLE_LIST)) {
//                    ((AddVehicleGroupActivity) context).updateVehicleList();
                    callback.onOkClick(true);
                }else {
//                    ((AddVehicleGroupActivity) context).updateUsersList();
                    callback.onOkClick(false);
                }
                dismiss();
            }
        });
        return view;
    }
}
