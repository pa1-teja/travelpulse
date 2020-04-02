package com.trimax.vts.view.reports.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import com.trimax.vts.view.reports.Adapter.ReportDialogListAdapter;
import com.trimax.vts.view.reports.Callback.ReportCallback;

import java.util.HashMap;

public class ReportCustomDialog extends DialogFragment implements ReportCallback{

    private Context context;
    private String from;
    private HashMap<String, Boolean> hashMap;
    private ReportCallback callback;
    private CheckBox selectAllCb;
    private boolean flag = true;
    private String TAG = ReportCustomDialog.class.getSimpleName();

    @SuppressLint("ValidFragment")
    public ReportCustomDialog(Context context, String from, HashMap<String, Boolean> hashmap, ReportCallback callback) {
        this.context = context;
        this.from = from;
        this.hashMap = hashmap;
        this.callback = callback;
    }

    public ReportCustomDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.partial_custom_dialog_report, null);

        final ListView listViewDialogItem = (ListView) view.findViewById(R.id.listView_dialog_items);
        final SearchView searchViewCustomdialog = (SearchView) view.findViewById(R.id.search_view_custom_dialog);
        selectAllCb = (CheckBox) view.findViewById(R.id.select_all);
        Button ok = (Button) view.findViewById(R.id.dialogok);
        Button cancel = (Button) view.findViewById(R.id.dialogcancel);

        String dialogTitleToBeSet = "<font color=\"#ff0000\"><bold>"
                + "\t"
                + from
                + "</bold></font>";
        getDialog().setTitle(Html.fromHtml(dialogTitleToBeSet));

        final ReportDialogListAdapter reportDialogListAdapter = new ReportDialogListAdapter(context, from, hashMap, this);
        listViewDialogItem.setAdapter(reportDialogListAdapter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callback.onButtonClick(from, Constants.IReport.OK_SELECTION);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectAllCb.setChecked(false);
                //callback.onButtonClick(from.trim(), Constants.IReport.CANCEL_SELECTION);
                //Toast.makeText(getActivity(), "All filter data are cleared", Toast.LENGTH_SHORT).show();

                dismiss();
            }
        });

        selectAllCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "selectAll flag: "+flag);
                if (flag) {
                    Log.d(TAG, "selectAll selected: "+b);
                    if (b) {
                        callback.onButtonClick(from, Constants.IReport.ALL_SELECTION);
                    } else {
                        callback.onButtonClick(from, Constants.IReport.CANCEL_SELECTION);
                    }
                    reportDialogListAdapter.performSelectAll(b);
                    reportDialogListAdapter.notifyDataSetChanged();
                }//else flag = true;

            }

        });

        searchViewCustomdialog.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                reportDialogListAdapter.getFilter().filter(s);
                reportDialogListAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return view;
    }


    public void updateSelectTab(){
        Log.d(TAG, "before flag: "+flag);
        if (flag)
            flag = false;
//        else flag = true;

        Log.d(TAG, "mid flag: "+flag);

        if (selectAllCb.isChecked())
            selectAllCb.setChecked(false);//here check is performed , and we don't want to perform any action so this flag is there

        flag = true;
        Log.d(TAG, "Selection: "+ selectAllCb.isChecked()+" "+ selectAllCb.isSelected());
        Log.d(TAG, "after flag: "+flag);
    }

    private boolean isAllSelected(){
        for (String s : hashMap.keySet()){
            if (!hashMap.get(s))
                return false;
        }
        return true;
    }

    @Override
    public void updateHashmap(String nameOfSelection, String key, Boolean value) {
        hashMap.put(key,value);
        callback.updateHashmap(nameOfSelection, key, value);
    }

    @Override
    public void onButtonClick(String nameOfSelection, String buttonSelection) {
        if (buttonSelection.trim().equalsIgnoreCase( Constants.IReport.NONE)){
            updateSelectTab();
        }else {
            callback.onButtonClick(nameOfSelection, buttonSelection);
        }
    }
}

