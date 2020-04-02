package com.trimax.vts.view.document;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.trimax.vts.interfaces.DocumentUpload;
import com.trimax.vts.sharedpref.TravelpulseInfoPref;
import com.trimax.vts.view.master.activity.Activity_Vehicle_Master;
import com.trimax.vts.view.master.activity.BaseActivity;
import com.trimax.vts.view.master.model.documents.UploadDocument;
import com.trimax.vts.view.master.model.documents.UploadDocumentListResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;
import static com.trimax.vts.utils.CommonClass.loaddocument;

public class Activity_Document extends BaseActivity implements DocumentUpload{

    RecyclerView recycler_view_upload_document;
    private Adapter_DocumentList_Master adapter;
    public String fileName;
    Context context;
    File file;
    Intent intent;
    DocumentUpload documentUpload;
    private String TAG = Activity_Vehicle_Master.class.getSimpleName();
    private String vehicleId;
    private SearchView searchView;
    Dialog pDialog;
    String[] documents = {"Insurance", "PUC", "RC", "Other"};
    List<UploadDocument> documentMasterModels = new ArrayList<UploadDocument>();
    private TravelpulseInfoPref infoPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        infoPref = new TravelpulseInfoPref(this);
        inti();
        setListener();
        intent = getIntent();
        documentUpload = this;
        vehicleId = intent.getStringExtra(Constants.IVehicle.VEHICLE_ID);
        infoPref.getLoginEditor().putString("doc_vid",vehicleId);

         for(int i=0;i< documents.length;i++){
             documentMasterModels.add(new UploadDocument(documents[i]));
         }
        loadAllUploadedDocument();
    }

    private void setListener() {
        search(searchView);
    }

    private void inti() {
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recycler_view_upload_document=findViewById(R.id.recycler_view_upload_document);
        setTitle("Vehicle Documents");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CardView card = (CardView) findViewById(R.id.card_view_vehicle_master);
        TextView txtRetry = (TextView) findViewById(R.id.txtRetryVehicleMaster);
        searchView = findViewById(R.id.search_view_vehicles);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
                return false;
            }
        });
    }


    private void loadAllUploadedDocument(){
        progressDialogStart(this, "Please Wait...");
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<UploadDocumentListResponse> call = objRetrofitInterface.fnGetAllDocumentList(auth, apiKey, vehicleId);

            call.enqueue(new Callback<UploadDocumentListResponse>() {
                @Override
                public void onResponse(Call<UploadDocumentListResponse> call, Response<UploadDocumentListResponse> response) {

                    try {
                        progressDialogStop();
                        Log.e(TAG, "Response " + response);

                        List<UploadDocument> documentlist = response.body().getData();
                        if (documentlist.size() > 0) {
                            for(int i =0 ;i<documentlist.size();i++){

                                UploadDocument model = documentlist.get(i);

                                switch (model.getAtributeId().toLowerCase()){

                                    case "insurance" :
                                        documentMasterModels.get(0).UpdateModelValue(model.getId(),model.getVehicleId(),model.getOtherName(),model.getDescription(),model.getExpiryDate(),
                                                model.getFileName(),model.getFilePath(),model.getInsuranceCompany(),
                                                model.getInsurancePolicyNo(),model.getInsuranceAmount(),model.getInsuranceCompanyName());
                                        break;

                                    case "puc" :
                                        documentMasterModels.get(1).UpdateModelValue(model.getId(),model.getVehicleId(),model.getOtherName(),model.getDescription(),model.getExpiryDate(),
                                                model.getFileName(),model.getFilePath(),model.getInsuranceCompany(),
                                                model.getInsurancePolicyNo(),model.getInsuranceAmount(),model.getInsuranceCompanyName());
                                        break;

                                        case "rc" :
                                        documentMasterModels.get(2).UpdateModelValue(model.getId(),model.getVehicleId(),model.getOtherName(),model.getDescription(),model.getExpiryDate(),
                                                model.getFileName(),model.getFilePath(),model.getInsuranceCompany(),
                                                model.getInsurancePolicyNo(),model.getInsuranceAmount(),model.getInsuranceCompanyName());
                                        break;

                                    case "other" :
                                        documentMasterModels.get(3).UpdateModelValue(model.getId(),model.getVehicleId(),model.getOtherName(),model.getDescription(),model.getExpiryDate(),
                                                model.getFileName(),model.getFilePath(),model.getInsuranceCompany(),
                                                model.getInsurancePolicyNo(),model.getInsuranceAmount(),model.getInsuranceCompanyName());
                                        break;
                                }

                            }

                        }

                        adapter = new Adapter_DocumentList_Master(Activity_Document.this, (ArrayList<UploadDocument>) documentMasterModels, documentUpload);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Activity_Document.this);
                        recycler_view_upload_document.setLayoutManager(mLayoutManager);
                        recycler_view_upload_document.setHasFixedSize(true);
                        recycler_view_upload_document.setItemAnimator(new DefaultItemAnimator());
                        recycler_view_upload_document.setAdapter(adapter);
                        recycler_view_upload_document.setNestedScrollingEnabled(false);

                    } catch(Exception ex) {
                        progressDialogStop();
                        Log.e(TAG, "This is an error: "+ex.getMessage());
                    }
                }

                @Override
                public void onFailure (Call <UploadDocumentListResponse> call, Throwable t){
                    Log.e(TAG, "Responce " + call + t);
                    progressDialogStop();
                }
            });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!loaddocument) {
            loadAllUploadedDocument();
        }
    }

    @Override
    public void uploadedFile(String s,String UpfileName) {
        fileName=UpfileName;
        loaddocument=true;
        new DownloadFileFromURL().execute(s);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, File> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            pDialog = new ProgressDialog(context);
            ((ProgressDialog) pDialog).setMessage("Loading... Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected File doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory() + "/tp" + "/"+fileName.replace(" ","");
                System.out.println("Downloading");
                URL url = new URL(f_url[0].replace(" ","%20"));
                URLConnection conection = url.openConnection();
                conection.connect();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(root);
                byte[] data = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // writing data to file
                    output.write(data, 0, count);
                }
                file = new File(root);
                output.flush();
                output.close();
                input.close();
            }
            catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file_url) {
            System.out.println("Downloaded");
            try {
                openFile(context,file_url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
        }

    }

    public void openFile(Context context, File url) throws IOException {
        // Create URI
        Uri uri = null;
        intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName() + ".provider", url);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        else{
            uri = Uri.fromFile(url);
        }

        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(intent);

    }

}
