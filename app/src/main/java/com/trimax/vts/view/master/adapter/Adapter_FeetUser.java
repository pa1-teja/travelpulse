package com.trimax.vts.view.master.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trimax.vts.view.master.activity.Activity_FleetSubUsers;
import com.trimax.vts.view.master.activity.AddFleetSubUserActivity;
import com.trimax.vts.view.master.model.FleetSubUsers.FleetSubUsersModel;
import com.trimax.vts.view.master.model.Modal_FleetUser;
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
import static java.util.Collections.addAll;

/**
 * Created by kiranp on 5/3/2018.
 */

public class Adapter_FeetUser extends RecyclerView.Adapter<Adapter_FeetUser.MyViewHolder> implements Filterable
{
    private ArrayList<FleetSubUsersModel> imageParametersList;
    private ArrayList<FleetSubUsersModel> mFilteredList;
    private Context context;
    private Gson gson;
    private Layout layout;
    private String charString="";
    private String TAG = Adapter_FeetUser.class.getSimpleName();
    private ProgressDialog progressDialog;
    private String cust_id, usertype_id;

    public Adapter_FeetUser(ArrayList<FleetSubUsersModel> imageParametersList, Context context, int id) {
        this.imageParametersList = imageParametersList;
        this.context = context;
        this.mFilteredList = imageParametersList;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
        cust_id = sharedPreferences.getString("id", "");
        usertype_id = sharedPreferences.getString(context.getString(R.string.user_type_id), "");
    }


    @Override
    public Adapter_FeetUser.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_driver, parent, false);
        Log.d("Set infalter",""+v);
        return new Adapter_FeetUser.MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final Adapter_FeetUser.MyViewHolder holder, final int position) {
        final FleetSubUsersModel pr = mFilteredList.get(position);
        holder.ll_document.setVisibility(View.GONE);
        holder.txtheaderName.setText("  "+pr.getFirstName() + " " + pr.getLastName());
        holder.tv_status.setText("  Status     :");
        if (pr.getStatus().equals("1")) {
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(" Active");
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#61BC47")), 1, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txtContactNumer.setText(spanString);
        }else {
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(" DeActive");
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#206296")), 1, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txtContactNumer.setText(" DeActive");
        }
        holder.tvLabel1.setText("  Email       :");
        holder.tvLabel2.setText("  Mobile     :");
        holder.txtlicenseNumber.setText(pr.getEmail());
        holder.txtlicenseExpiry.setText(" "+pr.getMobileNo());


        if (pr.getFirstName().contains(charString)) {
            int startPos = pr.getFirstName().indexOf(charString);
            int endPos = startPos + charString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.txtlicenseNumber.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txtlicenseNumber.setText(" "+spanString);
            //notifyDataSetChanged();
        }
/*
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Do you want to delete ?");
                alertbox.setTitle("Delete This Subuser");
                //  alertbox.setIcon(R.drawable.trn_03);
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialogStart(context, "Please wait...");
                        deleteUser(pr.getId());
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

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddFleetSubUserActivity.class);
                intent.putExtra(Constants.ISubUser.FIRST_NAME, pr.getFirstName());
                intent.putExtra(Constants.ISubUser.LAST_NAME, pr.getLastName());
                intent.putExtra(Constants.ISubUser.MOBILE_NUMBER, pr.getMobileNo());
                intent.putExtra(Constants.ISubUser.EMAIL, pr.getEmail());
                intent.putExtra(Constants.ISubUser.USER_ID, pr.getId());
                intent.putExtra(Constants.ISubUser.ACTION, Constants.ISubUser.UPDATE);
                ((AppCompatActivity)context).startActivity(intent);
                ((AppCompatActivity)context).finish();
            }
        });
    }


    public  void progressDialogStop() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
                progressDialog = null;
            }
        }
    }

    public  void progressDialogStart(Context cont, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(cont);//R.style.AlertDialog_Theme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing())
            {
                progressDialog.show();

            }
        }
    }



    private void deleteUser(final String subUserId){
        try {

            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = objRetrofitInterface.fnRemoveSubUser(auth, apiKey, usertype_id, cust_id, subUserId);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        progressDialogStop();
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
                                    ((Activity_FleetSubUsers)context).updateList(subUserId);
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
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Responce " + call + t);
                    progressDialogStop();
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
            progressDialogStop();
        }
    }


    private void editUser(){

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public void fnFilterList(ArrayList<FleetSubUsersModel> filterList) {
        imageParametersList = filterList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = imageParametersList;
                } else {
                    ArrayList<FleetSubUsersModel> filteredList = new ArrayList<>();
                    for (int i = 0; i< mFilteredList.size(); i++) {
                        //FILTER
                        if (mFilteredList.get(i).getFirstName().toUpperCase().contains(charString.toUpperCase()) ||
                                mFilteredList.get(i).getLastName().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(mFilteredList.get(i));
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<FleetSubUsersModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    class  MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtheaderName,txtContactNumer,txtlicenseNumber,txtlicenseExpiry,tv_status;
        private ImageView imgEdit,imgDelete;
        private LinearLayout ll_document;
        private TextView tvLabel1, tvLabel2;

        MyViewHolder(View itemView) {
            super(itemView);
            txtheaderName=(TextView) itemView.findViewById(R.id.txtheaderName);
            imgEdit  = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            txtContactNumer = (TextView) itemView.findViewById(R.id.txtContactNumer);
            txtlicenseNumber = (TextView) itemView.findViewById(R.id.txtlicenseNumber);
            txtlicenseExpiry = (TextView) itemView.findViewById(R.id.txtlicenseExpiry);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tvLabel1 = itemView.findViewById(R.id.tv_label_1);
            tvLabel2 = itemView.findViewById(R.id.tv_label_2);
            ll_document = itemView.findViewById(R.id.ll_document);
        }

    }

    public ArrayList<FleetSubUsersModel> onSaveInstanceState() {
        int size = getItemCount();
        ArrayList<FleetSubUsersModel> items = new ArrayList<>(size);
        for(int i=0;i<size;i++){
            items.add(imageParametersList.get(i));
        }
        return items;
    }

    public void onRestoreInstanceState(ArrayList<Modal_FleetUser> items) {
        //clear();
        addAll(items);
    }
}
