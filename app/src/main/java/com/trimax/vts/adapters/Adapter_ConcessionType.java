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
import com.trimax.vts.model.VehicleViewGroup;
import com.trimax.vts.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.trimax.vts.utils.Constant.arraylist_alertTypeTitle_group;


/**
 * Created by shwetad on 7/8/17.
 */

public class Adapter_ConcessionType extends BaseAdapter implements Filterable
{
    private static final String TAG = "Adapter_ConcessionType";

    private Context context;
    String resultone = "";
    String result = "";
    private List<VehicleViewGroup> m_concessionMasterList;
    private List<VehicleViewGroup> listToBeDisplayed;
    SharedPreferences sharedpreference;

    int positionn=0;
    PositionClickListener positionClickListener;
    VehicleViewGroup generateconnectionreport;
    private  FilterClass filterClass;
    public boolean isSelectedAll = false;
    public Adapter_ConcessionType(Context context, List<VehicleViewGroup> m_concessionMasterList, PositionClickListener positionClickListener)
    {
        this.context=context;
        this.m_concessionMasterList=m_concessionMasterList;
        this.listToBeDisplayed=m_concessionMasterList;
        this.positionClickListener = positionClickListener;
        sharedpreference = context.getSharedPreferences(Constants.app_preference_login, Context.MODE_PRIVATE);
        Log.d(TAG, "adapter listToBeDisplayed size: "+listToBeDisplayed.size());
    }

    @Override
    public int getCount() {
        return listToBeDisplayed!=null ?listToBeDisplayed.size():0 ;
    }

    @Override
    public Object getItem(int i) {
        return listToBeDisplayed.get(i).getId()+":"+listToBeDisplayed.get(i).getTitle();
    }

    public String getConcessionName(int i)
    {
        return listToBeDisplayed.get(i).getTitle();
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

        Log.d(TAG, "position: "+position+" item "+listToBeDisplayed.get(position).getTitle());

        textVieww.setText(listToBeDisplayed.get(position).getTitle());
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
                VehicleViewGroup contact = (VehicleViewGroup) cb.getTag();

                if (cb.isChecked()==true) {
                    contact.setSelected(true);
                    listToBeDisplayed.get(position).setSelected(cb.isChecked());

                    if (Constant.selectedGroupPositionsArray .contains(position)){

                    }else {

                        Constant.selectedGroupPositionsArray .add(position);
                        arraylist_alertTypeTitle_group .add(generateconnectionreport.getTitle());
                    }

                    positionClickListener.itemClicked(1, Constant.selectedGroupPositionsArray ,true);
                    Log.e("singl_onclick_list_size", String.valueOf(Constant.selectedGroupPositionsArray.size()));
                    Log.e( "single_onclick_string: ", resultone);

                }
                else {
                    contact.setSelected(false);
                    selectAll(false);

                    positionClickListener.itemselectAll(false);


                    listToBeDisplayed.get(position).setSelected(cb.isChecked());
                    for (Integer assurance : Constant.selectedGroupPositionsArray ) {
                        if (assurance.equals(position))  {
                            Constant.selectedGroupPositionsArray .remove(assurance);
                            arraylist_alertTypeTitle_group .remove(generateconnectionreport.getTitle());
                            //You can exit the loop as you find a reference
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    Log.e("singl_onclick_list_size", String.valueOf(Constant.selectedGroupPositionsArray.size()));
                    Log.e( "single_onclick_string: ", resultone);
                    positionClickListener.itemClicked(2, Constant.selectedGroupPositionsArray ,true);


                }

            }
        });
        ticket_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //new....................

                Collections.sort(Constant.selectedGroupStringArray);
//                Collections.sort(Constant.selectedGroupPositionsArray );

                if (isChecked) {


                    if (Constant.selectedGroupStringArray.contains(listToBeDisplayed.get(position).getId())) {

                    } else {
                        Constant.selectedGroupStringArray.add(listToBeDisplayed.get(position).getId());
                        Collections.sort(Constant.selectedGroupStringArray);

                        resultone = convertToString(Constant.selectedGroupStringArray);

                        Log.e("singl_chck_list_size", String.valueOf(Constant.selectedGroupStringArray.size()));
                        Log.e( "single_chck_string: ", resultone);

                    }

                } else {


                    selectAll(false);
                    positionClickListener.itemselectAll(false);
                    Constant.selectedGroupStringArray.remove(listToBeDisplayed.get(position).getId());
                    Collections.sort(Constant.selectedGroupStringArray);
                    Log.e("singl_chck_list_size", String.valueOf(Constant.selectedGroupStringArray.size()));                    Log.e( "single_string: ", resultone);
                }

                sharedpreference.edit().remove("AlertViewgroup").commit();

                SharedPreferences.Editor editor = sharedpreference.edit();
                editor.putString("AlertViewgroup",resultone);
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

            List<VehicleViewGroup> filteredList =new ArrayList<>();
            if(constraint!=null && constraint.length()>0)
            {
                for(int i=0;i<m_concessionMasterList.size();i++)
                {
                    //Log.d("Constraind Entered ",constraint.toString());
                    //Log.d("Concession Master List Item At Position"+i,m_concessionMasterList.get(i).getConcessionName());
                    if(m_concessionMasterList.get(i).getTitle().startsWith(constraint.toString().toUpperCase()))
                    {
                        filteredList.add(m_concessionMasterList.get(i));

                    }
                }

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
                listToBeDisplayed = (List<VehicleViewGroup>) filterResults.values;
                notifyDataSetChanged();
            }
            else
                notifyDataSetInvalidated();
        }
    }

    @SuppressLint("LongLogTag")
    public void selectAll(boolean true_or_false) {

        if (true_or_false == true) {
            //listner.OnClick(0);

            isSelectedAll = true;
            Constant.first=false;
            Constant.selectedGroupStringArray.clear();
            //   Toast.makeText(context, "check---"+this.reportList.size(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < listToBeDisplayed.size(); i++) {
                if (Constant.selectedGroupStringArray.contains(listToBeDisplayed.get(i).getId())) {

                } else {

                    Constant.selectedGroupStringArray.add(listToBeDisplayed.get(i).getId());
                    result = convertToString(Constant.selectedGroupStringArray);

                    Log.e("selectall_arraylist_size", String.valueOf(Constant.selectedGroupStringArray.size()));
                    Log.e("selectAll: ", result);


                }
            }

            sharedpreference.edit().remove("AlertViewgroup").commit();
            SharedPreferences.Editor editor = sharedpreference.edit();
            editor.putString("AlertViewgroup",result);
            editor.commit();

            //    Toast.makeText(context, "check---"+Constant.selectedGroupStringArray, Toast.LENGTH_LONG).show();


            notifyDataSetChanged();

        }
        else if (true_or_false == false) {

            // listner.OnClick(0);
            isSelectedAll = false;
           // Constant.selectedGroupStringArray.clear();
            //  Toast.makeText(context, ""+Constant.selectedGroupStringArray, Toast.LENGTH_LONG).show();
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
