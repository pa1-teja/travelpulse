package com.trimax.vts.view.document;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.trimax.vts.interfaces.DocumentUpload;
import com.trimax.vts.view.master.model.documents.UploadDocument;
import com.trimax.vts.view.R;

import java.util.ArrayList;

import static com.trimax.vts.utils.CommonClass.loaddocument;

public class Adapter_DocumentList_Master extends RecyclerView.Adapter<Adapter_DocumentList_Master.VehicleMasterViewHolder>
        implements Filterable ,DocumentUpload {

    private Context context;
    private ArrayList<UploadDocument> vehicleMasterModels;
    private ArrayList<UploadDocument> filteredVehicleMasterModels;
    private String TAG = Adapter_DocumentList_Master.class.getSimpleName();
    DocumentUpload documentUpload;

    public Adapter_DocumentList_Master(Context context, ArrayList<UploadDocument> vehicleMasterModels, DocumentUpload documentUpload) {
        this.context = context;
        this.vehicleMasterModels = vehicleMasterModels;
        filteredVehicleMasterModels = vehicleMasterModels;
        this.documentUpload = documentUpload;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                Log.d(TAG, "charSequence: "+charString);

                if (charString.isEmpty()) {
                    filteredVehicleMasterModels = vehicleMasterModels;
                } else {
                    ArrayList<UploadDocument> filteredList = new ArrayList<>();

                    for (int i = 0; i< filteredVehicleMasterModels.size(); i++) {
                        //FILTER
                        if (filteredVehicleMasterModels.get(i).getAtributeId().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(filteredVehicleMasterModels.get(i));
                        }
                    }

                    filteredVehicleMasterModels = filteredList;
                    Log.d(TAG, "filteredList: "+filteredList.size());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredVehicleMasterModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredVehicleMasterModels = (ArrayList<UploadDocument>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public VehicleMasterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_item_upload_document, parent, false);
        Log.d("Set infalter",""+v);
        return new VehicleMasterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleMasterViewHolder holder, int position) {

      final UploadDocument model  = filteredVehicleMasterModels.get(position);

        try {
            if (model.getAtributeId().equalsIgnoreCase("puc")) {
                holder.document_name.setText("PUC");
                holder.company_name.setVisibility(View.GONE);
            } else if (model.getAtributeId().equalsIgnoreCase("insurance")) {
                holder.document_name.setText("Insurance");
            } else if (model.getAtributeId().equalsIgnoreCase("rc")) {
                holder.document_name.setText("RC");
                holder.company_name.setVisibility(View.GONE);
            } else if (model.getAtributeId().equalsIgnoreCase("other")) {
                holder.document_name.setText("Other");
                holder.company_name.setVisibility(View.GONE);
            }

            if (model.getFileName() == null) {
                holder.edit_document.setVisibility(View.GONE);
                holder.upload_success.setVisibility(View.GONE);
                holder.iv_upload_document.setVisibility(View.VISIBLE);
                holder.policy_no.setText("Document Not Uploaded");
            }
            else {
                if(model.getFileName().equalsIgnoreCase("")){
                    holder.edit_document.setVisibility(View.GONE);
                    holder.upload_success.setVisibility(View.GONE);
                    holder.iv_upload_document.setVisibility(View.VISIBLE);
                    holder.policy_no.setText("Document Not Uploaded");
                }
                else{
                    holder.edit_document.setVisibility(View.VISIBLE);
                    holder.upload_success.setVisibility(View.VISIBLE);
                    holder.iv_upload_document.setVisibility(View.GONE);
                    SpannableString content = new SpannableString(model.getFileName());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    content.setSpan(new ForegroundColorSpan(Color.BLUE), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.policy_no.setText(content);
                }
            }

            if(model.checkModelValue(model.getDescription(),model.getExpiryDate(),model.getFileName(),model.getFilePath()
            ,model.getInsuranceCompany(),model.getInsurancePolicyNo(),model.getInsuranceAmount(),model.getInsuranceCompanyName())){

                holder.edit_document.setVisibility(View.VISIBLE);
                holder.upload_success.setVisibility(View.VISIBLE);
                holder.iv_upload_document.setVisibility(View.GONE);
                SpannableString content = new SpannableString(model.getFileName());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                content.setSpan(new ForegroundColorSpan(Color.BLUE), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.policy_no.setText(content);
            }else{
                holder.edit_document.setVisibility(View.GONE);
                holder.upload_success.setVisibility(View.GONE);
                holder.iv_upload_document.setVisibility(View.VISIBLE);
                holder.policy_no.setText("Document Not Uploaded");
            }


            if (model.getExpiryDate() == null ) {
                holder.exp_date.setText("Exp Date :" +"No Details Found");
            }
            else {
                if(model.getExpiryDate().equalsIgnoreCase("")){
                    holder.exp_date.setText("Exp Date :" +"No Details Found");
                }else {
                    holder.exp_date.setText("Exp Date :" + model.getExpiryDate());
                }
            }
            if (model.getInsuranceCompanyName() == null ) {
                holder.company_name.setText("Company :"+"No Details Found");
            } else {

                if(model.getInsuranceCompanyName().equalsIgnoreCase("")){
                    holder.company_name.setText("Company :"+"No Details Found");
                }else {
                    holder.company_name.setText(model.getInsuranceCompanyName());
                }

            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }



        holder.edit_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaddocument=false;
                Intent intent = new Intent(context, Activity_UplaodDocument.class);
                intent.putExtra("vid", model.getVehicleId());
                intent.putExtra("doc",model);
                if (model.getFilePath()!=null && TextUtils.isEmpty(model.getFilePath()))
                    intent.putExtra("edit",true);
                else
                    intent.putExtra("edit",true);
                ((AppCompatActivity)context).startActivity(intent);
            }
        });
        holder.iv_upload_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_UplaodDocument.class);
                intent.putExtra("vid", model.getVehicleId());
                intent.putExtra("doc",model);
                if (model.getFilePath()!=null && TextUtils.isEmpty(model.getFilePath()))
                    intent.putExtra("add",true);
                else
                    intent.putExtra("add",false);
                ((AppCompatActivity)context).startActivity(intent);
            }
        });

        holder.policy_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentUpload.uploadedFile(model.getFilePath(),model.getFileName());

            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredVehicleMasterModels.size();
    }

    @Override
    public void uploadedFile(String s ,String t) {
    }

    class VehicleMasterViewHolder extends RecyclerView.ViewHolder {
        TextView document_name, exp_date,policy_no,company_name;
        ImageView upload_success, edit_document,iv_upload_document;

        public VehicleMasterViewHolder(View itemView) {
            super(itemView);
            exp_date = itemView.findViewById(R.id.exp_date);
            document_name = itemView.findViewById(R.id.document_name);
            upload_success = itemView.findViewById(R.id.upload_success);
            edit_document = itemView.findViewById(R.id.edit_document);
            iv_upload_document=itemView.findViewById(R.id.iv_upload_document);
            policy_no = itemView.findViewById(R.id.policy_no);
            company_name=itemView.findViewById(R.id.company_name);
        }
    }

}
