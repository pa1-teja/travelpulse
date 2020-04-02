package com.trimax.vts.view.master.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.trimax.vts.interfaces.DocumentUpload;
import com.trimax.vts.view.master.model.driver.DriverModel;
import com.trimax.vts.view.master.model.driver.DriverModelResponse;
import com.trimax.vts.view.master.adapter.Adapter_Driver;
import com.trimax.vts.view.master.model.Modal_getDetails;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;
import static com.trimax.vts.utils.CommonClass.loaddocument;

/**
 * Created by kiranp on 5/11/2018.
 */

@SuppressLint("Registered")
public class Activity_Drivers extends BaseActivity implements View.OnClickListener, DocumentUpload {

    private RecyclerView listView;
    private Adapter_Driver driverAdapter;
    private Context mContext;
    private String TAG = "DriverFragment";
    private CardView card;
    private ArrayList<DriverModel> driverModels;
    ImageView txtRetry;
    Dialog pDialog;
    Intent intent;
    File file;
    DocumentUpload dp;
    TextView imgNorecordFound;
    public String fileName;
    private String strId, strUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmagment_drivers);
        mContext = this;
        InitilizeView();
        dp=(DocumentUpload)this;
    }

    private void InitilizeView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.road_pulse_action_bar_color)));
        Gson gson = new Gson();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (RecyclerView) findViewById(R.id.ListView);
        card = (CardView) findViewById(R.id.card);
        txtRetry = (ImageView) findViewById(R.id.txtRetryy);
        imgNorecordFound = (TextView) findViewById(R.id.imgNorecordFound);
        txtRetry.setOnClickListener(this);
        imgNorecordFound.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        strId = sharedPreferences.getString("id", "");
        strUserId = sharedPreferences.getString(getString(R.string.user_type_id), "");

        ArrayList<ArrayList<Modal_getDetails.Datum>> list = new ArrayList<ArrayList<Modal_getDetails.Datum>>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Drivers.this, AddDriverActivity.class);
                intent.putExtra(Constants.IDriver.ACTION,Constants.IDriver.ACTION_ADD_DRIVER);
                startActivity(intent);
                finish();
            }
        });

        fnGetDashBoardDetails();
    }

    public void updateList(String driverId){//, boolean isDelete
//        if (isDelete) {
            for (DriverModel model : driverModels) {
                if (model.getId().trim().equalsIgnoreCase(driverId.trim())) {
                    driverModels.remove(model);
                    driverAdapter.notifyDataSetChanged();
                    break;
                }
            }
    }


    private void fnGetDashBoardDetails() {
        progressDialogStart(this, "Please Wait...");
        try {
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<DriverModelResponse> call = objRetrofitInterface.fnGetAllDriverDetails(auth, apiKey, strId, strUserId);
            call.enqueue(new Callback<DriverModelResponse>() {
                @Override
                public void onResponse(Call<DriverModelResponse> call, Response<DriverModelResponse> response) {
                    try {
                        progressDialogStop();
                        Log.e(TAG, "Response " + response);
                        //Utils.stopProgressDialog();
                        driverModels = response.body().getData();

                        Log.e(TAG, "DriverModels: " + driverModels.size());

                        if (driverModels.size() > 0) {
                            driverAdapter = new Adapter_Driver(driverModels, mContext, 0,dp);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            listView.setLayoutManager(mLayoutManager);
                            listView.setHasFixedSize(true);
                            listView.setItemAnimator(new DefaultItemAnimator());
                            listView.setAdapter(driverAdapter);
                            listView.setNestedScrollingEnabled(false);
                        } else {
                            card.setVisibility(View.VISIBLE);
                            imgNorecordFound.setVisibility(View.VISIBLE);

                        }

                } catch(Exception ex)

                {
                    progressDialogStop();
                    Log.e(TAG, "This is an error: "+ex.getMessage());
                }
            }

            @Override
            public void onFailure (Call < DriverModelResponse > call, Throwable t){
                Log.e(TAG, "Responce " + call + t);
                progressDialogStop();
            }
        });
    } catch(Exception ex) {
        progressDialogStop();
        Log.e(TAG, "Api fail: "+ex.getMessage());
    }

}

    public static void StartActivity(Context context) {
        context.startActivity(new Intent(context, Activity_Drivers.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drivers, menu);
        MenuItem item = menu.findItem(R.id.search);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {

                    driverAdapter.getFilter().filter(newText);

                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == txtRetry) {
            fnGetDashBoardDetails();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void uploadedFile(String s, String UpfileName) {

        fileName=UpfileName;
        loaddocument=true;
       new DownloadFileFromURL().execute(s);
    }


    class DownloadFileFromURL extends AsyncTask<String, String, File> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            pDialog = new ProgressDialog(Activity_Drivers.this);
            ((ProgressDialog) pDialog).setMessage("Loading... Please wait...");
            // pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected File doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory() + "/tp" + "/"+fileName.replace(" ","");
                file = new File(root);
                System.out.println("Downloading");
                URL url = new URL(f_url[0].replace(" ","%20"));

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                OutputStream output = new FileOutputStream(root);
                byte[] data = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);


                }


                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            }
            catch (Exception e) {

                Log.e("Error: ", e.getMessage());

            }

            return file;
        }



        /**
         * After completing background task
         * **/
        @Override
        protected void onPostExecute(File file_url) {


            System.out.println("Downloaded");

            try {
                openFile(Activity_Drivers.this,file_url);

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

            // Add in case of if We get Uri from fileProvider.
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        else{
            uri = Uri.fromFile(url);
        }


        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
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
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(intent);

    }

}
