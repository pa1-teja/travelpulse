package com.trimax.vts.view.master.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
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
import com.trimax.vts.interfaces.DocumentUpload;
import com.trimax.vts.view.master.activity.Activity_Drivers;
import com.trimax.vts.view.master.model.driver.DriverModel;
import com.trimax.vts.view.master.activity.AddDriverActivity;
import com.trimax.vts.view.master.model.Modal_getDetails;
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

public class Adapter_Driver extends RecyclerView.Adapter<Adapter_Driver.MyViewHolder> implements Filterable, DocumentUpload
{
    private ArrayList<DriverModel> imageParametersList;
    private Context context;
    private Gson gson;
    private Layout layout;
    private ArrayList<DriverModel> mFilteredList;
    private String TAG = Adapter_Driver.class.getSimpleName();
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    DocumentUpload dp;
    String fileN,Des;


    public Adapter_Driver(ArrayList<DriverModel> imageParametersList, Context context, int id,DocumentUpload dp) {
        this.imageParametersList = imageParametersList;
        this.context = context;
        this.mFilteredList = imageParametersList;
        sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
        this.dp=dp;
    }

    @Override
    public Adapter_Driver.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_driver, parent, false);
        Log.d("Set infalter",""+v);
        return new Adapter_Driver.MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final Adapter_Driver.MyViewHolder holder, final int position) {

        final DriverModel pr = mFilteredList.get(position);
        Log.e("TAG", "onBindViewHolder: position is " + position + " " + pr.getName());
        holder.txtheaderName.setText(Html.fromHtml( pr.getName().trim()));
        if(pr !=null&&!pr.getMobileNo().trim().equalsIgnoreCase("")) {
            holder.ll_mobile.setVisibility(View.VISIBLE);
            holder.txtContactNumer.setText(": "+Html.fromHtml(pr.getMobileNo().trim()));
        }else{
            holder.ll_mobile.setVisibility(View.GONE);
        }
        String strLicanceNumber = (String) pr.getLicenseNumber();

        String strLicanceExpiry = (String) pr.getLicenseExpiry();
        if(pr.getFile_name() !=null&&!pr.getFile_name().trim().equalsIgnoreCase("")) {

            SpannableString content = new SpannableString(pr.getFile_name());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            content.setSpan(new ForegroundColorSpan(Color.parseColor("#206296")), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.download_file.setText(": "+content);
            fileN = pr.getFile_name();
        }else{
            holder.download_file.setText(": Not uploaded");
            fileN = "";
        }

        if(pr.getDescription() !=null&&!pr.getDescription().trim().equalsIgnoreCase("")) {

            Des = pr.getDescription();
        }else{

            Des = "";
        }


        if (strLicanceNumber==null ) {
            holder.txtlicenseNumber.setText(": N/A");//N/A
        }else if(strLicanceNumber.equalsIgnoreCase("null")) {
            holder.txtlicenseNumber.setText(": "+strLicanceNumber); }
         else {holder.txtlicenseNumber.setText(": "+strLicanceNumber);}

        if (strLicanceExpiry == null ) {
            holder.txtlicenseExpiry.setText(": N/A");//N/A
        }else if(strLicanceExpiry.equalsIgnoreCase("null")) {
            holder.txtlicenseExpiry.setText(": "+strLicanceNumber); }

        else{holder.txtlicenseExpiry.setText(": "+strLicanceExpiry);}

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddDriverActivity.class);
                intent.putExtra(Constants.IDriver.ACTION, Constants.IDriver.ACTION_EDIT_DRIVER_DETAILS);
                intent.putExtra(Constants.IDriver.DRIVER_ID, pr.getId().trim());
                intent.putExtra(Constants.IDriver.NAME, pr.getName().trim());
                intent.putExtra(Constants.IDriver.LIECENSE_NO, ""+pr.getLicenseNumber());
                intent.putExtra(Constants.IDriver.LIECENSE_EXPIRY, ""+pr.getLicenseExpiry());
                intent.putExtra(Constants.IDriver.MOBILE_NUMBER, pr.getMobileNo().trim());
                intent.putExtra(Constants.IDriver.FILE_NAME, holder.download_file.getText().toString());
                intent.putExtra(Constants.IDriver.DESCRIPTION, Des);
                ((AppCompatActivity)context).startActivity(intent);
                ((AppCompatActivity)context).finish();
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemoveDriverDialog(pr);
            }
        });


        holder.download_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dp.uploadedFile(pr.getFile_path(),pr.getFile_name());

            }
        });
    }


    private void openRemoveDriverDialog(final DriverModel driverModel){
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Delete Driver")
                .setMessage("Do you really want to delete this driver?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removeDriver(driverModel);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    private void removeDriver(final DriverModel driverModel){
        progressDialogStart(context, "Please wait...");
        try{
            auth = auth.replace("\n", "");
            String strUserId = sharedPreferences.getString("user_type_id", "");
            RetrofitInterface retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = retrofitInterface.fnRemoveDriver(auth, apiKey,
                    driverModel.getCustomerId(),
                    strUserId,
                    driverModel.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    try
                    {
                        progressDialogStop();
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG,"Response code: "+strResponceCode);
                        Log.e(TAG,"Response body: "+responseBody.toString());
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                Log.e(TAG,"Response strResponse: "+strResponse);
                                JSONObject obj = new JSONObject(strResponse);
                                String msg = obj.optString("msg");
                                if (obj.optString("status").trim().equalsIgnoreCase("success")){
                                    if (obj.has("data"))
                                        msg = obj.optString("data");
                                    else if (obj.has("msg"))
                                        msg = obj.optString("msg");
                                    else msg = "Server not responding.";
                                    ((Activity_Drivers)context).updateList(driverModel.getId());
                                }else {
                                    if (obj.has("data"))
                                        msg = obj.optString("data");
                                    else if (obj.has("msg"))
                                        msg = obj.optString("msg");
                                    else msg = "Server not responding.";
                                }

                                new AlertDialog.Builder(context)
                                        .setMessage(msg)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                                break;
                            case 400:
                                break;
                        }
                    } catch (Exception ex) {
                        progressDialogStop();
                        Log.e(TAG,"This is an error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialogStop();
                    new androidx.appcompat.app.AlertDialog.Builder(context)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
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


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    /*public void fnFilterList(ArrayList<DriverModel>filterList) {
        imageParametersList = filterList;
        notifyDataSetChanged();
    }*/

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = imageParametersList;
                } else {
                    ArrayList<DriverModel> filteredList = new ArrayList<>();

                    for (int i = 0; i< mFilteredList.size(); i++) {
                        //FILTER
                        if (mFilteredList.get(i).getName().toUpperCase().contains(charString.toUpperCase())) {
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
                mFilteredList = (ArrayList<DriverModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void uploadedFile(String s, String filename) {

    }

    class  MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtheaderName,txtdate,txtContactNumer,txtlicenseNumber,txtlicenseExpiry,
                download_file,txtlocation;
        private ImageView imgEdit,imgDelete;
        LinearLayout ll_mobile;

        MyViewHolder(View itemView) {
            super(itemView);
            txtheaderName=(TextView) itemView.findViewById(R.id.txtheaderName);
            imgEdit  = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            txtContactNumer = (TextView) itemView.findViewById(R.id.txtContactNumer);
            txtlicenseNumber = (TextView) itemView.findViewById(R.id.txtlicenseNumber);
            download_file = (TextView) itemView.findViewById(R.id.download_file);
            txtlicenseExpiry = (TextView) itemView.findViewById(R.id.txtlicenseExpiry);
            ll_mobile = itemView.findViewById(R.id.ll_mobile);
        }

    }

    public ArrayList<DriverModel> onSaveInstanceState() {
        int size = getItemCount();
        ArrayList<DriverModel> items = new ArrayList<DriverModel>(size);
        for(int i=0;i<size;i++){
            items.add(imageParametersList.get(i));
        }
        return items;
    }

    public void onRestoreInstanceState(ArrayList<Modal_getDetails> items) {
        //clear();
        addAll(items);
    }

}