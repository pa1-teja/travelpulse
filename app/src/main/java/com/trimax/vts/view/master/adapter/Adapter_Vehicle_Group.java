package com.trimax.vts.view.master.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.trimax.vts.view.master.model.VehicleGroup_model;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.view.R;

import java.util.ArrayList;



/**
 * Created by Abdul on 5/3/2018.
 */

public class Adapter_Vehicle_Group extends BaseAdapter implements Filterable
{
    // Declare Variables

    Context context;
    LayoutInflater inflater;
    public ArrayList<VehicleGroup_model> userLists;
    ArrayList<VehicleGroup_model> userfilter;
    View itemView;
    CustomUserFilter filter;
    String iddelete;
    Utils utils;

    ProgressDialog pdialog;


    public Adapter_Vehicle_Group(Context context,
                                ArrayList<VehicleGroup_model> organisationDataGetArrayList) {
        this.context = context;
        this.userLists = organisationDataGetArrayList;
        this.userfilter= organisationDataGetArrayList;
    }

    @Override
    public int getCount() {
        return userLists.size();
    }

    @Override
    public Object getItem(int position) {
        return userLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables

        final TextView titlename_vehiclegroup;
        ImageView edit_vehiclegroup,delete_vehiclegroup;
        String Id;
        if (inflater==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null)
        {
            convertView=  inflater.inflate(R.layout.vehiclegroup_design, parent, false);

        }

        titlename_vehiclegroup = (TextView) convertView.findViewById(R.id.titlename_vehiclegroup);
        edit_vehiclegroup = (ImageView) convertView.findViewById(R.id.edit_vehiclegroup);
        delete_vehiclegroup = (ImageView) convertView.findViewById(R.id.delete_vehiclegroup);
        // inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        titlename_vehiclegroup.setText(userLists.get(position).getTitle());

//        if (usertype.getText().toString().equals("Owner")){
//
//            delete.setVisibility(View.GONE);
//        }
//        else {
//            delete.setVisibility(View.VISIBLE);
//        }

        edit_vehiclegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context,SetUpUserPostEdit_Activity.class);
//                i.putExtra("id",userLists.get(position).getId1());
//                i.putExtra("d",userLists.get(position).getDesrciption());
//                i.putExtra("n",userLists.get(position).getName());
//                i.putExtra("e",userLists.get(position).getEmail());
//                i.putExtra("m",userLists.get(position).getMobile());
//                i.putExtra("p",userLists.get(position).getPassword());
//
//                context.startActivity(i);


                Utils.showToast(context,"edit click");
            }
        });

        delete_vehiclegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Do you want to delete ?");
                //alertbox.setTitle("Warning");
                //  alertbox.setIcon(R.drawable.trn_03);

//                alertbox.setNeutralButton("OK",
//                        new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface arg0,
//                                                int arg1) {
//
//                            }
//                        });

                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        iddelete= userLists.get(position).getId1();
//                        Delete();
//                        pdialog = ProgressDialog.show(context, "", "Loading...", true);

                    }
                });

                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                alertbox.show();

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context,userLists.get(position).getId1(),Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

//    public void Delete() {
//
//        utils=new Utils(context);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, utils.BASE_URL_SERVER + "api/DeleteUserctrl",
//
//                new Response.Listener<String>() {
//
//                    @Override
//
//                    public void onResponse(String response) {
//
//                        Log.e("onResponse", response);
//
//                        Intent i = new Intent(context,SetUpUserGet_Activity.class);
//                        context.startActivity(i);
//                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
//
//                    }
//
//                },
//
//                new Response.ErrorListener() {
//
//                    @Override
//
//                    public void onErrorResponse(VolleyError error) {
//
//                        pdialog.dismiss();
//                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
//
//                    }
//
//                }) {
//
//            @Override
//
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("ID",iddelete);
//
//                return params;
//
//            }
//
//
//        };
//
//
//        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//        requestQueue.add(stringRequest);
//
//    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter=new CustomUserFilter(userfilter,this);
        }
        return filter;
    }


    public class CustomUserFilter extends Filter {
        ArrayList<VehicleGroup_model> filterlistcf;
        Adapter_Vehicle_Group adapter;

        public CustomUserFilter(ArrayList<VehicleGroup_model> filterlistcf, Adapter_Vehicle_Group adapter) {
            this.filterlistcf = filterlistcf;
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            //VALIDATION
            if (constraint !=null && constraint.length()>0){
                // CHANGE TO UPPER CASE FOR CONSISTANCY
                constraint = constraint.toString().toUpperCase();

                ArrayList<VehicleGroup_model> filteredvisitors = new ArrayList<>();

                //LOOP THRU FILTER LIST
                for (int i = 0; i< filterlistcf.size(); i++){
                    //FILTER
                    if (filterlistcf.get(i).getTitle().toUpperCase().contains(constraint)) {
                        filteredvisitors.add(filterlistcf.get(i));
                    }
                }

                results.count=filteredvisitors.size();
                results.values = filteredvisitors;
            } else
            {
                results.count= filterlistcf.size();
                results.values = filterlistcf;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.userLists= (ArrayList<VehicleGroup_model>) results.values;
            adapter.notifyDataSetChanged();
        }
    }
}
