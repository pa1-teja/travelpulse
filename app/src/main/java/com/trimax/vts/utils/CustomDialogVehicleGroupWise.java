package com.trimax.vts.utils;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.trimax.vts.interfaces.ClickListner;
import com.trimax.vts.interfaces.DialogItemClickListener;
import com.trimax.vts.interfaces.PositionClickListener;
import com.trimax.vts.adapters.Adapter_ConcessionType;
import com.trimax.vts.adapters.Adapter_GetVehicleGroupWise;
import com.trimax.vts.view.R;
import com.trimax.vts.model.VehiclePojo;
import com.trimax.vts.model.VehicleViewGroup;

import java.util.ArrayList;
import java.util.List;


public class CustomDialogVehicleGroupWise extends DialogFragment implements PositionClickListener {
    private static final String TAG = "CustomDialogVehicleGrou";
    private Context context;
    private String dialogtitle;
    private List<?> dataList;
    ClickListner listener=null;
    CheckBox selectAllCb;
    boolean selectAllvalue=false;
    Button ok, cancel;
    private ListView listViewDialogItem;
    private DialogItemClickListener dialogItemClickListener;
    private SearchView searchViewCustomdialog;
    PositionClickListener positionClickListener;
    private Adapter_GetVehicleGroupWise VehicleGroupWise;
    private Adapter_ConcessionType viewgroupadapter;


    @SuppressLint("ValidFragment")
    public CustomDialogVehicleGroupWise(Context context, String title, List<?> dataList, ClickListner clickListner)
    {
        Log.d(TAG, "dataList2 size: "+dataList.size());
        this.context = context;
        dialogtitle = title;
        this.dataList=dataList;
        listener=clickListner;
    }
    public CustomDialogVehicleGroupWise()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.partial_custom_dialog, null);
        listViewDialogItem = (ListView) view.findViewById(R.id.listView_dialog_items);
        searchViewCustomdialog= (SearchView) view.findViewById(R.id.search_view_custom_dialog);
        selectAllCb = (CheckBox) view.findViewById(R.id.select_all);
        ok = (Button) view.findViewById(R.id.dialogok);
        cancel = (Button) view.findViewById(R.id.dialogcancel);
        positionClickListener = (PositionClickListener) this;
        String dialogTitleToBeSet= "<font color=\"#ff0000\"><bold>"
                + "\t"
                + dialogtitle
                + "</bold></font>";
        getDialog().setTitle(Html.fromHtml(dialogTitleToBeSet));
        setAdapterForListView();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.clickOkAlertDialog=true;
                if(selectAllCb.isChecked()){
                    Log.d("ListViewChecked", "vehicle Listsize: "+ Constant.selectedVehicleStringArray.size());
                    listener.Onclickclr(Constant.selectedVehicleStringArray.size(),"vehicle", getActivity());
                }
                else {
                    Log.d("ListViewChecked", "vehicle Listsize: "+ Constant.selectedVehiclePositionsArray.size());
                    //listener.Onclickclr(Constant.selectedVehiclePositionsArray.size(),"vehicle", getActivity());
                    listener.Onclickclr(Constant.selectedVehicleStringArray.size(),"vehicle", getActivity());

                }
                // Toast.makeText(getActivity(),Constant.arraylist_position.size(), Toast.LENGTH_SHORT).show();

                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAllCb.setChecked(false);
                int size = Constant.arrayList_vehicle.size();
                for (int i = 0; i < size; i++) {
                    VehiclePojo dto = Constant.arrayList_vehicle.get(i);
                    dto.setSelected(false);
                    for (Integer assurance : Constant.arraylist_position_t_vehicle ) {
                        if (assurance.equals(i))  {
                            Constant.arraylist_position_t_vehicle .remove(assurance);
                            //You can exit the loop as you find a reference
                            break;
                        }
                    }
                }
                Constant.selectedVehicleStringArray.clear();
                Constant.selectedVehiclePositionsArray.clear();

                VehicleGroupWise.selectAll(false);
                VehicleGroupWise.notifyDataSetChanged();
                Toast.makeText(getActivity(), "All filter data are cleared", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        selectAllCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Constant.arraylist_position_t_vehicle .clear();
                if (selectAllvalue == false) {
                    if (b) {

                        int size = Constant.arrayList_vehicle.size();
                        for (int i = 0; i < size; i++) {
                            VehiclePojo dto = Constant.arrayList_vehicle.get(i);
                            dto.setSelected(true);
                            Constant.arraylist_position_t_vehicle.add(i);

                        }

                        VehicleGroupWise.selectAll(true);
                        VehicleGroupWise.notifyDataSetChanged();

                    } else {
                        int size = Constant.arrayList_vehicle.size();
                        for (int i = 0; i < size; i++) {
                            VehiclePojo dto = Constant.arrayList_vehicle.get(i);
                            dto.setSelected(false);
                            for (Integer assurance : Constant.arraylist_position_t_vehicle) {
                                if (assurance.equals(i)) {
                                    Constant.arraylist_position_t_vehicle.remove(assurance);

                                    //You can exit the loop as you find a reference
                                    break;
                                }
                            }
                        }

                        VehicleGroupWise.selectAll(false);
                        VehicleGroupWise.notifyDataSetChanged();
                    }
                }
                selectAllvalue=false;

            }

        });

        return view;
    }



    public void setOnDialogItemClickListener(DialogItemClickListener dialogItemClickListener)
    {
        this.dialogItemClickListener=dialogItemClickListener;
    }

    public void setAdapterForListView() {
        switch (dialogtitle) {
            case  Constants.dialogMessageToShowBusStops:
                //m_concessionMasterList = DatabaseController.getDatabaseControllerInstance(context).getConcessionTypeDetails(busTypeCode);
//                Utility.showToast(context, " " + dataList.size());
                viewgroupadapter = new Adapter_ConcessionType(context,
                        (List<VehicleViewGroup>)dataList,positionClickListener);
                searchQueryListener(Constants.dialogMessageToShowBusStops);
                listViewDialogItem.setAdapter(viewgroupadapter);
                setOnClickListenerToListView(  Constants.dialogMessageToShowBusStops);
                break;
            case Constants.dialogMessageToShowBusTypes:
                VehicleGroupWise = new Adapter_GetVehicleGroupWise(context, (List<VehiclePojo>) dataList,positionClickListener);
                listViewDialogItem.setAdapter(VehicleGroupWise);
                searchQueryListener(Constants.dialogMessageToShowBusTypes);
                setOnClickListenerToListView(Constants.dialogMessageToShowBusTypes);
                break;
        }
    }


    private void setOnClickListenerToListView(final String dialogtitle) {
        listViewDialogItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(dialogItemClickListener!=null)
                {
                    switch (dialogtitle)
                    {
                        case Constants.dialogMessageToShowBusStops:
                            dialogItemClickListener.onDialogItemClick(viewgroupadapter.getItem(position).toString(),viewgroupadapter.getItem(position).toString());
                            break;
                        case Constants.dialogMessageToShowBusTypes:
                            dialogItemClickListener.onDialogItemClick(VehicleGroupWise.getItem(position).toString(),VehicleGroupWise.getItem(position).toString());
                            break;
                    }

                }
                getDialog().dismiss();
            }
        });
    }


    private void searchQueryListener(final String dialogtitle) {


        searchViewCustomdialog.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                switch (dialogtitle)
                {
                    case Constants.dialogMessageToShowBusStops:
                        viewgroupadapter.getFilter().filter(s);
                        break;
                    case Constants.dialogMessageToShowBusTypes:
                        VehicleGroupWise.getFilter().filter(s);
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public void itemClicked(int id, ArrayList<Integer> arraylist_position, Boolean flag) {

    }

    @Override
    public void itemClickedType(int id, ArrayList<Integer> arraylist_position_t_vehicle) {

    }

    @Override
    public void itemselectAll(boolean value) {
        if(value==false ){
            selectAllvalue=true;
            selectAllCb.setChecked(false);
            if(selectAllCb.isChecked()){
                selectAllvalue=true;
            }else {
                selectAllvalue=false;

            }

        }
    }
}
