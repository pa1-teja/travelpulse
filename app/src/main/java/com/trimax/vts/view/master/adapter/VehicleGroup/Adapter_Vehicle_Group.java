package com.trimax.vts.view.master.adapter.VehicleGroup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.trimax.vts.view.master.activity.Activity_Vehical_Group;
import com.trimax.vts.view.master.activity.AddVehicleGroupActivity;
import com.trimax.vts.view.master.model.VehicleGroup.VehicleGroupModel;
import com.trimax.vts.utils.Utils;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;


/**
 * Created by Abdul on 5/3/2018.
 */

public class Adapter_Vehicle_Group extends BaseAdapter implements Filterable {
    // Declare Variables

    Context context;
    LayoutInflater inflater;
    public ArrayList<VehicleGroupModel> userLists;
    ArrayList<VehicleGroupModel> userfilter;
    View itemView;
    CustomUserFilter filter;
    String iddelete;
    Utils utils;
    ProgressDialog pdialog;
    private String TAG = Adapter_Vehicle_Group.class.getSimpleName();
    private String cust_id, usertype_id;


    public Adapter_Vehicle_Group(Context context, ArrayList<VehicleGroupModel> organisationDataGetArrayList) {
        this.context = context;
        this.userLists = organisationDataGetArrayList;
        this.userfilter = organisationDataGetArrayList;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
        cust_id = sharedPreferences.getString("id", "");
        usertype_id = sharedPreferences.getString(context.getString(R.string.user_type_id), "");
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

        final TextView titlename_vehiclegroup, tvCreatedOn, tvUpdatedOn;
        ImageView edit_vehiclegroup, delete_vehiclegroup;
        String Id;
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.vehiclegroup_design, parent, false);

        }

        titlename_vehiclegroup = (TextView) convertView.findViewById(R.id.titlename_vehiclegroup);
        edit_vehiclegroup = (ImageView) convertView.findViewById(R.id.edit_vehiclegroup);
        delete_vehiclegroup = (ImageView) convertView.findViewById(R.id.delete_vehiclegroup);
        tvCreatedOn = convertView.findViewById(R.id.tv_grp_created_on);
        tvUpdatedOn = convertView.findViewById(R.id.tv_grp_updated_on);
        // inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        titlename_vehiclegroup.setText(userLists.get(position).getTitle());
        tvCreatedOn.setText(userLists.get(position).getCreatedOn());
        tvUpdatedOn.setText(userLists.get(position).getUpdatedOn());

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


               // Utils.showToast(context, "edit click");
                updateGroup(userLists.get(position));
            }
        });

        delete_vehiclegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Do you want to delete ?");
                alertbox.setTitle("Delete Group");
                //  alertbox.setIcon(R.drawable.trn_03);
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pdialog = ProgressDialog.show(context, "", "Loading...", true);
                        deleteGroup(userLists.get(position).getId());
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
                // Toast.makeText(context,u((AppCom((AppCompatActivity)context)p((AppCompatActivity)context)atActivity)context)serLists.get(position).getId1(),Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }


    private void updateGroup(final VehicleGroupModel groupModel){
        Intent intent = new Intent(context, AddVehicleGroupActivity.class);
        intent.putExtra(Constants.IVehicle.GROUP_ID, groupModel.getId());
        intent.putExtra(Constants.IVehicle.TTILE, groupModel.getTitle());
        intent.putExtra(Constants.IVehicle.ACTION,"update");
        ((AppCompatActivity)context).startActivity(intent);
        ((AppCompatActivity)context).finish();
    }



    public void deleteGroup(final String groupId) {
        try {

            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.fnRemoveGroup(auth, apiKey, usertype_id, cust_id, groupId);

            Log.d(TAG, "strId: " + cust_id);
            Log.d(TAG, "strUserId: " + usertype_id);
            Log.d(TAG, "groupId: " + groupId);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (pdialog.isShowing())
                            pdialog.dismiss();
                        Log.e(TAG, "Response " + response);
                        ResponseBody responseBody = response.body();
                        Log.e(TAG, "Response body: " + responseBody.toString());
                        int strResponceCode = response.code();
                        Log.e(TAG, "Responce code" + strResponceCode);
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                Log.e(TAG, "Responce strResponse " + strResponse);
                                JSONObject jsonObject = new JSONObject(strResponse);

                                String msg = "";
                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                    if (jsonObject.has("data"))
                                        msg = jsonObject.optString("data");
                                    else if (jsonObject.has("msg"))
                                        msg = jsonObject.optString("msg");
                                    else msg = "Server not responding.";
                                    ((Activity_Vehical_Group)context).updateList(groupId);
                                } else {
                                    if (jsonObject.has("data"))
                                        msg = jsonObject.optString("data");
                                    else if (jsonObject.has("msg"))
                                        msg = jsonObject.optString("msg");
                                    else msg = "Server not responding.";
                                }
                                new AlertDialog.Builder(context)
                                        .setMessage(msg)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();


                                break;
                            case 400:
                                // Utils.stopProgressDialog();
                                break;
                        }

                    } catch (Exception ex) {
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                        if (pdialog.isShowing())
                            pdialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Responce " + call + t);
                    if (pdialog.isShowing())
                        pdialog.dismiss();
                    new AlertDialog.Builder(context)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "Api fail: " + ex.getMessage());
            if (pdialog.isShowing())
                pdialog.dismiss();
        }

    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomUserFilter(userfilter, this);
        }
        return filter;
    }


    public class CustomUserFilter extends Filter {
        ArrayList<VehicleGroupModel> filterlistcf;
        Adapter_Vehicle_Group adapter;

        public CustomUserFilter(ArrayList<VehicleGroupModel> filterlistcf, Adapter_Vehicle_Group adapter) {
            this.filterlistcf = filterlistcf;
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            //VALIDATION
            if (constraint != null && constraint.length() > 0) {
                // CHANGE TO UPPER CASE FOR CONSISTANCY
                constraint = constraint.toString().toUpperCase();

                ArrayList<VehicleGroupModel> filteredvisitors = new ArrayList<>();

                //LOOP THRU FILTER LIST
                for (int i = 0; i < filterlistcf.size(); i++) {
                    //FILTER
                    if (filterlistcf.get(i).getTitle().toUpperCase().contains(constraint)) {
                        filteredvisitors.add(filterlistcf.get(i));
                    }
                }

                results.count = filteredvisitors.size();
                results.values = filteredvisitors;
            } else {
                results.count = filterlistcf.size();
                results.values = filterlistcf;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.userLists = (ArrayList<VehicleGroupModel>) results.values;
            adapter.notifyDataSetChanged();
        }
    }
}
