package com.trimax.vts.view.master.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.utils.Constants;
import com.trimax.vts.view.R;
import com.trimax.vts.utils.FileCompressor;
import com.trimax.vts.utils.Permissions;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

public class AddDriverActivity extends AppCompatActivity implements View.OnClickListener {

    private String strId, strUserId;
    private ProgressDialog progressDialog;
    private String TAG = AddDriverActivity.class.getSimpleName();
    private TextInputEditText etName, etLiecenseNo, etLiecenseExpiry, etMobileNo,edt_des;
    private Button btnAddDriver;
    private String driverId = "";
    private String action = Constants.IDriver.ACTION_ADD_DRIVER;
    private String year1;
    private String month1;
    private String day1;
    private Calendar calendar;
    long currenttime;
    private static int RESULT_LOAD_IMAGE = 1;
    public ImageView attachment_doc;
    public TextView  succes_attach;
    boolean uploadflag=false;
    File TempFile;
    FileCompressor mCompressor;
    RequestBody requestBody;
    MultipartBody.Part fileToUpload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        closeKeyBoard(this);
        mCompressor= new FileCompressor(this);
        initView();
        uploadflag=false;
        setListner();
        loadDefaultData();
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        action = intent.getStringExtra(Constants.IDriver.ACTION);

        if(action.equalsIgnoreCase( Constants.IDriver.ACTION_EDIT_DRIVER_DETAILS)){
            getSupportActionBar().setTitle(R.string.editdriver);
        }else if(action.equalsIgnoreCase( Constants.IDriver.ACTION_ADD_DRIVER)){
            getSupportActionBar().setTitle(R.string.Adddriver);

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.road_pulse_action_bar_color)));
        etName = findViewById(R.id.et_driver_name);
        etLiecenseNo = findViewById(R.id.et_liecense_no);
        etLiecenseExpiry = findViewById(R.id.et_liecense_expiry);
        etMobileNo = findViewById(R.id.et_mobile_no);
        edt_des = (findViewById(R.id.et_description));
        attachment_doc = (findViewById(R.id.attachment_doc));
        btnAddDriver = findViewById(R.id.btn_add_driver);
        succes_attach = findViewById(R.id.succes_attach);

    }

    private void setListner() {
        btnAddDriver.setOnClickListener(this);
        etLiecenseExpiry.setOnClickListener(this);
        attachment_doc.setOnClickListener(this);
    }

    private void loadDefaultData() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        strId = sharedPreferences.getString("id", "");
        strUserId = sharedPreferences.getString(getString(R.string.user_type_id), "");
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.IDriver.ACTION)) {
            action = intent.getStringExtra(Constants.IDriver.ACTION);
            if (intent.hasExtra(Constants.IDriver.NAME)) {
                String name = intent.getStringExtra(Constants.IDriver.NAME);
                String liecenseNo = intent.getStringExtra(Constants.IDriver.LIECENSE_NO);
                String liecenseExpiry = intent.getStringExtra(Constants.IDriver.LIECENSE_EXPIRY);
                String mobileNo = intent.getStringExtra(Constants.IDriver.MOBILE_NUMBER);
                driverId = intent.getStringExtra(Constants.IDriver.DRIVER_ID);
                String des = intent.getStringExtra(Constants.IDriver.DESCRIPTION);
                String fileName = intent.getStringExtra(Constants.IDriver.FILE_NAME);

                btnAddDriver.setText(Constants.IDriver.UPDATE);
                etName.setText(name);

                if (liecenseNo ==null ) {
                    etLiecenseNo.setText("");//N/A
                }else if(liecenseNo.equalsIgnoreCase("null")) {
                    etLiecenseNo.setText("");
                }else{
                    etLiecenseNo.setText(liecenseNo);
                }

                if (liecenseExpiry ==null ) {
                    etLiecenseExpiry.setText("");//N/A
                }else if(liecenseExpiry.equalsIgnoreCase("null")) {
                    etLiecenseExpiry.setText("");
                }else {
                    etLiecenseExpiry.setText(liecenseExpiry);
                }

                if (mobileNo ==null ) {
                    etMobileNo.setText("");//N/A
                }else if(mobileNo.equalsIgnoreCase("null")) {
                    etMobileNo.setText("");
                }
                else {
                    etMobileNo.setText(mobileNo);
                }
                if (des ==null ) {
                    edt_des.setText("");//N/A
                }else if(des.equalsIgnoreCase("null")) {
                    edt_des.setText("");
                }
                else {
                    edt_des.setText(des);
                }
                if (fileName ==null ) {
                    succes_attach.setText("");//N/A
                }else if(fileName.equalsIgnoreCase(" ")) {
                    succes_attach.setText("");
                }
                else {
                    succes_attach.setVisibility(View.VISIBLE);
                    succes_attach.setText(fileName);
                }

            }
        }
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        currenttime = calendar.getTimeInMillis();
    }


    private void addDriver() {
        progressDialogStart(AddDriverActivity.this, "Please wait...");
        try {

            if (!uploadflag){

                requestBody = null;
                fileToUpload = null;

            }
            else{
                requestBody = RequestBody.create(MediaType.parse("*/*"), TempFile);
                fileToUpload = MultipartBody.Part.createFormData("files_up", TempFile.getName(), requestBody);

            }

            /*strId,strUserId*/
            RequestBody User_id = RequestBody.create(MediaType.parse("text/plain"), strId);
            RequestBody USer_type_id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), etName.getText().toString());
            RequestBody LiecenseNo = RequestBody.create(MediaType.parse("text/plain"), etLiecenseNo.getText().toString());
            RequestBody liecenseExpiry = RequestBody.create(MediaType.parse("text/plain"), etLiecenseExpiry.getText().toString());
            RequestBody MobileNo = RequestBody.create(MediaType.parse("text/plain"),etMobileNo.getText().toString());
            RequestBody des = RequestBody.create(MediaType.parse("text/plain"), edt_des.getText().toString());


            auth = auth.replace("\n", "");
            RetrofitInterface retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = retrofitInterface.fnAddDriver(auth, apiKey,
                    User_id,
                    USer_type_id,
                    name,
                    LiecenseNo,
                    liecenseExpiry,//.getText().toString()
                    MobileNo,des,fileToUpload);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        progressDialogStop();
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code: " + strResponceCode);
                        Log.e(TAG, "Response body: " + responseBody.toString());
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                Log.e(TAG, "Response strResponse: " + strResponse);
                                JSONObject jsonObject = new JSONObject(strResponse);
                                String msg = "";
                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                    if (jsonObject.has("data"))
                                        msg = jsonObject.optString("data");
                                    else if (jsonObject.has("msg"))
                                        msg = jsonObject.optString("msg");
                                    else msg = "Server not responding.";
                                    new AlertDialog.Builder(AddDriverActivity.this)
                                            .setMessage(msg)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(AddDriverActivity.this, Activity_Drivers.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                                else {
                                    if(jsonObject.has("data")) {

                                        JSONObject jsonObjectt = jsonObject.optJSONObject("data");
                                        Iterator<String> keys = jsonObjectt.keys();
                                        while (keys.hasNext()) {
                                            String key = keys.next();
                                            String value = msg + jsonObjectt.getString(key);
                                            msg = value+"\n";
                                            Log.v("category key", key);
                                            Log.v("category value", value);
                                        }
                                        new androidx.appcompat.app.AlertDialog.Builder(AddDriverActivity.this)
                                                .setMessage(msg)
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }

                                                }).show();
                                    }
                                    else {
                                        new androidx.appcompat.app.AlertDialog.Builder(AddDriverActivity.this)
                                                .setMessage(jsonObject.optString("msg"))
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }

                                                }).show();
                                    }

                                }

                                break;
                            case 400:
                                Toast.makeText(AddDriverActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception ex) {
                        progressDialogStop();
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                        Toast.makeText(AddDriverActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialogStop();
                    new androidx.appcompat.app.AlertDialog.Builder(AddDriverActivity.this)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
//                    Toast.makeText(AddDriverActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(AddDriverActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
        }
    }


    private void editDriver() {
        progressDialogStart(AddDriverActivity.this, "Please wait...");
        try {



            if (!uploadflag){

                requestBody = null;
                fileToUpload = null;

            }
            else{
                requestBody = RequestBody.create(MediaType.parse("*/*"), TempFile);
                fileToUpload = MultipartBody.Part.createFormData("files_up", TempFile.getName(), requestBody);

            }

            /*strId,strUserId*/
            RequestBody User_id = RequestBody.create(MediaType.parse("text/plain"), strId);
            RequestBody USer_type_id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), etName.getText().toString());
            RequestBody LiecenseNo = RequestBody.create(MediaType.parse("text/plain"), etLiecenseNo.getText().toString());
            RequestBody liecenseExpiry = RequestBody.create(MediaType.parse("text/plain"), etLiecenseExpiry.getText().toString());
            RequestBody MobileNo = RequestBody.create(MediaType.parse("text/plain"),etMobileNo.getText().toString());
            RequestBody des = RequestBody.create(MediaType.parse("text/plain"), edt_des.getText().toString());
            RequestBody driverid = RequestBody.create(MediaType.parse("text/plain"), driverId);



            auth = auth.replace("\n", "");
            RetrofitInterface retrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<ResponseBody> call = retrofitInterface.fnUpdateDriver(auth, apiKey,
                    User_id,
                    USer_type_id,
                    name,
                    LiecenseNo,
                    liecenseExpiry,//.getText().toString()
                    MobileNo,driverid,des,fileToUpload);


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        progressDialogStop();
                        ResponseBody responseBody = response.body();
                        int strResponceCode = response.code();
                        Log.e(TAG, "Response code: " + strResponceCode);
                        Log.e(TAG, "Response body: " + responseBody.toString());
                        switch (strResponceCode) {
                            case 200:
                                assert responseBody != null;
                                String strResponse = responseBody.string();
                                Log.e(TAG, "Response strResponse: " + strResponse);
                                JSONObject jsonObject = new JSONObject(strResponse);
                                String msg = "";
                                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                                    if (jsonObject.has("data"))
                                        msg = jsonObject.optString("data");
                                    else if (jsonObject.has("msg"))
                                        msg = jsonObject.optString("msg");
                                    else msg = "Server not responding.";
                                } else {
                                    if (jsonObject.has("data"))
                                        msg = jsonObject.optString("data");
                                    else if (jsonObject.has("msg"))
                                        msg = jsonObject.optString("msg");
                                    else msg = "Server not responding.";
                                }
                                new AlertDialog.Builder(AddDriverActivity.this)
                                        .setMessage(msg)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(AddDriverActivity.this, Activity_Drivers.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                                break;
                            case 400:
                                Log.d(TAG, "response code: "+400);
                                Toast.makeText(AddDriverActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception ex) {
                        progressDialogStop();
                        Toast.makeText(AddDriverActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "This is an error: " + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialogStop();
                    new AlertDialog.Builder(AddDriverActivity.this)
                            .setMessage("Server not responding.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(AddDriverActivity.this, Activity_Drivers.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
//                    Toast.makeText(AddDriverActivity.this, "Error occur.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(AddDriverActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
        }
    }


    public void progressDialogStop() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
                progressDialog = null;
            }
        }
    }

    public void progressDialogStart(Context cont, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(cont);//R.style.AlertDialog_Theme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();

            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == btnAddDriver) {
            if (isValid()) {
                if (action.trim().equalsIgnoreCase(Constants.IDriver.ACTION_EDIT_DRIVER_DETAILS))

                    editDriver();
                else
                    addDriver();
            } else
                Toast.makeText(AddDriverActivity.this, "Please enter all details..", Toast.LENGTH_SHORT).show();
        }
        if (v == etLiecenseExpiry){
            DatePickerDialog dp = new DatePickerDialog(AddDriverActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view,
                                              int selectedYear, int selectedMonth,
                                              int selectedDay) {
                            year1=String.valueOf(selectedYear);
                            month1=String.valueOf(selectedMonth+1);
                            day1=String.valueOf(selectedDay);
                            etLiecenseExpiry.setText(day1 + "-" + month1 + "-"
                                    + year1);
                            calendar.set(selectedYear,selectedMonth,selectedDay);
                        }

                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));

            dp.getDatePicker().setMinDate(currenttime);

            dp.show();
        }
        if(v == attachment_doc){

            if (ContextCompat.checkSelfPermission(AddDriverActivity.this, Permissions.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Permissions.WRITE_EXTERNAL_STORAGE)){
                    ActivityCompat.requestPermissions(this,Permissions.PERMISSIONS,0);

                }
                else{
                    ActivityCompat.requestPermissions(this,Permissions.PERMISSIONS,0);

                }

            }else {


                uploadflag = true;
                /*Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
                Intent intent = new Intent();
                intent.setType("*/*");
                String[] mimetype = {"image/*", "application/pdf"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetype);
                //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //startActivityForResult(intent, RESULT_LOAD_IMAGE);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.perform_action_with)), RESULT_LOAD_IMAGE);
            }
        }
    }

    private boolean isValid() {
        boolean isValid = false;
        char mobile_no=1;
        if(!etMobileNo.getText().toString().equalsIgnoreCase("")){
             mobile_no=(etMobileNo.getText().toString().charAt(0));
        }

        if (etName.getText().toString().trim().length() == 0) {
            isValid = false;
            etName.setError("Please enter name.");
        }else if(String.valueOf(mobile_no).equalsIgnoreCase("0")){
            isValid = false;
            etMobileNo.setError("Please enter valid  mobile number");
        }
        /*else if (etLiecenseNo.getText().toString().trim().length() == 0) {
            isValid = false;
            etLiecenseNo.setError("Please enter liecense number.");
        }else if (etLiecenseNo.getText().toString().trim().length() != 10) {
            isValid = false;
            etLiecenseNo.setError("Please enter valid liecense number");
        }else if (liecenseExpiry.trim().length() == 0) {
            isValid = false;
            etLiecenseExpiry.setError("Please enter select liecense expiry date.");
        } else if (etMobileNo.getText().toString().trim().length() == 0) {
            isValid = false;
            etMobileNo.setError("Please enter mobile number.");
        }else if (etMobileNo.getText().toString().trim().length() != 10) {
            isValid = false;
            etMobileNo.setError("Please enter valid mobile number.");
        }*/ else {
            etName.setError(null);
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "called onBackPressed");
        Intent intent = new Intent(AddDriverActivity.this, Activity_Drivers.class);
        startActivity(intent);
        finish();
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


    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            try {
                if (selectedImage.toString().endsWith(".pdf")) {

                    String filename=selectedImage.getPath().substring(selectedImage.getPath().lastIndexOf("/")+1);
                    File directory = new File((Environment.getExternalStorageDirectory()).toString() + "/tp");
                    /// TempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+document.getFileName()+".pdf");
                    //TempFile = new File(directory, String.valueOf((Calendar.getInstance().getTimeInMillis())).substring(8) + "_" + document.getVehicleId() + "_" + document.getAtributeId() + ".pdf");
                    TempFile = new File(directory, filename );
                    openPath(selectedImage);
                  //  succes_attach.setVisibility(View.VISIBLE);
                    //succes_attach.setText(filename+" Attached Successfully");

                }
                else if(selectedImage.toString().contains("content://com.google.android.apps.docs.storage")){

                    Toast.makeText(AddDriverActivity.this,"Couldn't Upload file from Google Drive",Toast.LENGTH_LONG).show();
                }
                else  {
                    try {

                        MimeTypeMap m = MimeTypeMap.getSingleton();
                        String imagename=  getFileName(selectedImage);

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        TempFile = mCompressor.compressToFile(saveImage(bitmap,imagename));

                        if(TempFile.length()>3145728 ){

                            Toast.makeText(this,"File Size Limit Exceed",Toast.LENGTH_LONG).show();
                        }else {

                            succes_attach.setVisibility(View.VISIBLE);
                            succes_attach.setText(imagename+" Attached Successfully");
                        }

                        //attach_link.setText(imagename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }catch (NullPointerException ex){
                ex.printStackTrace();
                Toast.makeText(AddDriverActivity.this,"File Format Not Supported",Toast.LENGTH_LONG).show();

            }
        }
    }


    public String getFileName(Uri uri) {
        String result = null; if (uri.getScheme().equals("content"))
        { Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close(); }
        } if (result == null) {
            result = uri.getPath(); int cut = result.lastIndexOf('/'); if (cut != -1) { result = result.substring(cut + 1); } } return result; }


    public void openPath(Uri uri){
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            //Convert your stream to data here

            copyInputStreamToFile(is,TempFile);
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            Log.e("tempfile"+TempFile.length(),"");

            if(TempFile.length()>3145728 ){

                Toast.makeText(this,"File Size Limit Exceed",Toast.LENGTH_LONG).show();
            }else {

                succes_attach.setVisibility(View.VISIBLE);
                succes_attach.setText(TempFile.getName()+" Attached Successfully");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }


    public File saveImage(Bitmap bitmap,String filename){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File directory = new File ((Environment.getExternalStorageDirectory()).toString() + "/tp");
        if (!directory.exists())
            directory.mkdirs();
        try {
            //File f = new File(directory, String.valueOf((Calendar.getInstance().getTimeInMillis())).substring(8) +"_"+document.getVehicleId()+"_"+document.getAtributeId()+ ".jpg");
            File f = new File(directory, filename);

            f.createNewFile();
            if(f.exists()) {
                FileOutputStream fo =  new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                //scan file
                //MediaScannerConnection.scanFile(this, arrayOf(f.getPath()), arrayOf("image/png"), null)
                fo.close();
            }
            return f;
        }
        catch (IOException e1){
            e1.printStackTrace();
        }
        return null;
    }


    protected void closeKeyBoard(Activity context) {
        View view =  context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
