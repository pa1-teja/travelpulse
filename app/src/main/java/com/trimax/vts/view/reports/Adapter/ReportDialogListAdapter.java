package com.trimax.vts.view.reports.Adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import com.trimax.vts.view.reports.Callback.ReportCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportDialogListAdapter extends ArrayAdapter implements Filterable {

    private Context context;
    private String from;
    private ArrayList<String> list = new ArrayList<>(), filteredList = new ArrayList<>(), finalList = new ArrayList<>();
    private HashMap<String, Boolean> hashmap;
    private ReportCallback callback;
    private String TAG = ReportDialogListAdapter.class.getSimpleName();

    public ReportDialogListAdapter(@NonNull Context context, String from, HashMap<String, Boolean> hashmap,
                                   ReportCallback callback) {
        super(context, 0);
        this.context = context;
        this.from = from;
        this.hashmap = hashmap;
        this.callback = callback;
        for (String s : hashmap.keySet()) {
            list.add(s);
            filteredList.add(s);
        }
        finalList.addAll(list);
    }

    @Override
    public int getCount() {
        /*if (filteredList.size() == 0)
            return hashmap.size();
        else*/
            return list.size();
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_concession_type, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.textview_concession_type);
        CheckBox ticket_selected = (CheckBox) view.findViewById(R.id.checkbox_);

        textView.setText(list.get(position));

        ticket_selected.setChecked(hashmap.get(list.get(position)));

        ticket_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callback.updateHashmap(from, list.get(position), isChecked);
                if (!isChecked) {
                    callback.onButtonClick(from, Constants.IReport.NONE);
                }
            }
        });


        return view;
    }

    public void performSelectAll(boolean isAllSelected){
        try{
            Log.d(TAG, "performSelectAll: "+isAllSelected);
            if (isAllSelected) {
                for (String s : hashmap.keySet()) {
                    hashmap.put(s, true);
                }
            }else {
                for (String s : hashmap.keySet()) {
                    hashmap.put(s, false);
                }
            }
            this.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                Log.d(TAG, "charString: "+charString);

                filteredList = finalList;

                if (charString.isEmpty()) {
                    filteredList = finalList;
                } else {
                    ArrayList<String> filterLi = new ArrayList<>();
                    for (int i = 0; i< filteredList.size(); i++) {
                        //FILTER
                        if (filteredList.get(i).toUpperCase().contains(charString.toUpperCase()) ||
                                filteredList.get(i).toUpperCase().contains(charString.toUpperCase())) {
                            filterLi.add(filteredList.get(i));
                        }
                    }
                    filteredList = filterLi;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
