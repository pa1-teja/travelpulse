package com.trimax.vts.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.trimax.vts.utils.Constant;
import com.trimax.vts.interfaces.PositionClickListener;
import com.trimax.vts.view.R;
import com.trimax.vts.model.VehiclePojo;
import com.trimax.vts.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.trimax.vts.utils.Constant.arraylist_alertTypeTitle_vehicle;


/**
 * Created by shwetad on 7/8/17.
 */

public class Adapter_GetVehicleGroupWise extends BaseAdapter implements Filterable
{
    private Context context;
    String resultone = "";
    String result = "";
    private List<VehiclePojo> m_concessionMasterList;
    private List<VehiclePojo> listToBeDisplayed;
    SharedPreferences sharedpreference;

    int positionn=0;
    PositionClickListener positionClickListener;
    VehiclePojo generateconnectionreport;
    private  FilterClass filterClass;
    public boolean isSelectedAll = false;
    public Adapter_GetVehicleGroupWise(Context context, List<VehiclePojo> m_concessionMasterList,
                                       PositionClickListener positionClickListener)
    {
        this.context=context;
        this.m_concessionMasterList=m_concessionMasterList;
        this.listToBeDisplayed=m_concessionMasterList;
        this.positionClickListener = positionClickListener;
        sharedpreference = context.getSharedPreferences(Constants.app_preference_login,
                Context.MODE_PRIVATE);

    }

    @Override
    public int getCount() {
        return listToBeDisplayed!=null ?listToBeDisplayed.size():0 ;
    }

    @Override
    public Object getItem(int i) {
        return listToBeDisplayed.get(i).getVehicle_id()+":"+listToBeDisplayed.get(i).getVehicle_no();
    }

    public String getConcessionName(int i)
    {
        return listToBeDisplayed.get(i).getVehicle_no();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.item_concession_type,null);
        }
        positionn= position;
        final TextView textVieww = (TextView) view.findViewById(R.id.textview_concession_type);
        CheckBox ticket_selected = (CheckBox) view.findViewById(R.id.checkbox_);

        textVieww.setText(listToBeDisplayed.get(position).getVehicle_no());
        generateconnectionreport = m_concessionMasterList.get(position);
        ticket_selected.setChecked(generateconnectionreport.isSelected());
        ticket_selected.setEnabled(true);
        ticket_selected.setClickable(true);
        ticket_selected.setTag(listToBeDisplayed.get(position));
        ticket_selected.setSelected(listToBeDisplayed.get(position).isSelected());
        ticket_selected.setChecked(listToBeDisplayed.get(position).isSelected());
        ticket_selected.setOnCheckedChangeListener(null);
        ticket_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                VehiclePojo contact = (VehiclePojo) cb.getTag();

                if (cb.isChecked()==true) {
                    contact.setSelected(true);
                    listToBeDisplayed.get(position).setSelected(cb.isChecked());
                    Log.e("position_000", String.valueOf(positionn));

                    if (Constant.selectedVehiclePositionsArray .contains(position)){

                    }else {

                        Constant.selectedVehiclePositionsArray .add(position);
                        arraylist_alertTypeTitle_vehicle .add(generateconnectionreport.getVehicle_no());
                    }

                    positionClickListener.itemClicked(1, Constant.selectedVehiclePositionsArray ,true);

                    Log.e( "0000: ", String.valueOf(Constant.selectedVehiclePositionsArray ));
                    Log.e( "0000: ", String.valueOf(Constant.arraylist_alertTypeTitle_vehicle ));

                    Log.e( "size0000: ", String.valueOf(Constant.selectedVehiclePositionsArray .size()));


                }
                else {
                    contact.setSelected(false);
                    listToBeDisplayed.get(position).setSelected(cb.isChecked());
                    positionClickListener.itemselectAll(false);
                    for (Integer assurance : Constant.selectedVehiclePositionsArray ) {
                        if (assurance.equals(position))  {
                            Constant.selectedVehiclePositionsArray .remove(assurance);
                            arraylist_alertTypeTitle_vehicle .remove(generateconnectionreport.getVehicle_no());
                            //You can exit the loop as you find a reference
                            break;
                        }
                    }
                    notifyDataSetChanged();

                    Log.e( "1111: ", String.valueOf(Constant.selectedVehiclePositionsArray ));
                    Log.e( "size1111: ", String.valueOf(Constant.selectedVehiclePositionsArray .size()));

                    positionClickListener.itemClicked(2, Constant.selectedVehiclePositionsArray ,true);


                }

            }
        });
        ticket_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //new....................

                Collections.sort(Constant.selectedVehicleStringArray);
//                Collections.sort(Constant.selectedVehiclePositionsArray );

                if (isChecked) {


                    if (Constant.selectedVehicleStringArray.contains(listToBeDisplayed.get(position).getVehicle_id())) {

                    } else {
                        Constant.selectedVehicleStringArray.add(listToBeDisplayed.get(position).getVehicle_id());
                        Collections.sort(Constant.selectedVehicleStringArray);

                        resultone = convertToString(Constant.selectedVehicleStringArray);

                        Log.e("single_arraylist_size", String.valueOf(Constant.selectedVehicleStringArray.size()));
                        Log.e( "single_string: ", resultone);

                    }

                } else {

                    positionClickListener.itemselectAll(false);
                    Constant.selectedVehicleStringArray.remove(listToBeDisplayed.get(position).getVehicle_id());
                    Collections.sort(Constant.selectedVehicleStringArray);

                }
               sharedpreference.edit().remove("AlertGroupWiseVehicle").commit();

                SharedPreferences.Editor editor = sharedpreference.edit();
                editor.putString("AlertGroupWiseVehicle",resultone);
                editor.commit();


            }
        });


        return view;
    }

    @Override
    public Filter getFilter() {
        if(filterClass==null)
        {
            filterClass=new FilterClass();
        }
        return filterClass;
    }


    private  class FilterClass extends Filter
    {

        @SuppressLint("LongLogTag")
        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            final FilterResults filterResults=new FilterResults();
            //  new Thread(new Runnable() {
            //    @Override
            //   public void run() {

            List<VehiclePojo> filteredList =new ArrayList<>();
            if(constraint!=null && constraint.length()>0)
            {
                for(int i=0;i<m_concessionMasterList.size();i++)
                {
                    //Log.d("Constraind Entered ",constraint.toString());
                    //Log.d("Concession Master List Item At Position"+i,m_concessionMasterList.get(i).getConcessionName());
                    if(m_concessionMasterList.get(i).getVehicle_no().startsWith(constraint.toString().toUpperCase()))
                    {
                        filteredList.add(m_concessionMasterList.get(i));
                        Log.d("Added data In Filter Array List",m_concessionMasterList.get(i).getVehicle_no());

                    }
                }
                Log.d("Filtered ArrayList Size","/t"+filteredList.size());
                filterResults.count=filteredList.size();
                filterResults.values=filteredList;
            }
            else
            {
                filterResults.count=m_concessionMasterList.size();
                filterResults.values=m_concessionMasterList;
            }
            // Set on UI Thread
                    /*((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Notify the List that the DataSet has changed...
                            notifyDataSetChanged();
                        }
                    });
*/

            //  }
            // }).start();


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if(filterResults.count>0) {
                listToBeDisplayed = (List<VehiclePojo>) filterResults.values;
                notifyDataSetChanged();
            }
            else
                notifyDataSetInvalidated();
        }
    }

    @SuppressLint("LongLogTag")
    public void selectAll(boolean true_or_false) {
        Log.e("onClickSelectAll", "yes");
        if (true_or_false == true) {
            //listner.OnClick(0);

            isSelectedAll = true;
            Constant.first=false;
            Constant.selectedVehicleStringArray.clear();
            //   Toast.makeText(context, "check---"+this.reportList.size(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < listToBeDisplayed.size(); i++) {
                if (Constant.selectedVehicleStringArray.contains(listToBeDisplayed.get(i).getVehicle_id())) {

                } else {

                    Constant.selectedVehicleStringArray.add(listToBeDisplayed.get(i).getVehicle_id());
                    result = convertToString(Constant.selectedVehicleStringArray);

                    Log.e("selectall_arraylist_size", String.valueOf(Constant.selectedVehicleStringArray.size()));
                    Log.e("selectAll: ", result);


                }
            }

            sharedpreference.edit().remove("AlertGroupWiseVehicle").commit();
            SharedPreferences.Editor editor = sharedpreference.edit();
            editor.putString("AlertGroupWiseVehicle",result);
            editor.commit();

            //    Toast.makeText(context, "check---"+Constant.selectedVehicleStringArray, Toast.LENGTH_LONG).show();


            notifyDataSetChanged();

        } else if (true_or_false == false) {
            Log.e("onClickSelectAll", "no");
            // listner.OnClick(0);
            isSelectedAll = false;
            Constant.selectedVehicleStringArray.clear();
            //  Toast.makeText(context, ""+Constant.selectedVehicleStringArray, Toast.LENGTH_LONG).show();
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
