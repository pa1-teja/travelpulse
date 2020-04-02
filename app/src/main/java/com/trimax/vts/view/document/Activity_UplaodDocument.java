package com.trimax.vts.view.document;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.trimax.vts.view.master.activity.BaseActivity;
import com.trimax.vts.view.master.model.documents.UploadDocument;
import com.trimax.vts.view.master.model.documents.UploadDocumentResponse;
import com.trimax.vts.view.master.model.documents.UploadDocumentTypeList;
import com.trimax.vts.view.master.model.documents.UploadDocumentTypeResponse;
import com.trimax.vts.api.ApiClient;
import com.trimax.vts.api.RetrofitInterface;
import com.trimax.vts.view.R;
import com.trimax.vts.utils.FileCompressor;
import com.trimax.vts.utils.Permissions;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;
import static com.trimax.vts.utils.CommonClass.bodyToString;

public class Activity_UplaodDocument extends BaseActivity implements View.OnClickListener {

    public TextInputEditText edt_exp_date, edt_des, edt_policy, edt_amt;
    Calendar selectedDate ;
    long currenttime;
    private String day1, month1, year1,fromDate;
    public ImageView attachment_doc, attachment_pdf;
    public Spinner insurance_co;
    TextView succes_attach,attach_link;
    Activity_UplaodDocument activity = null;
    private static int RESULT_LOAD_IMAGE = 1;
    String cust_id, usertype_id,Vehicle_id,doc_typee="",selectedText="";
    FileCompressor mCompressor;
    boolean uploadflag=false;
    RequestBody requestBody;
    MultipartBody.Part fileToUpload;
    File TempFile;
    String amount_inc="";
    public Button save_doc;
    private UploadDocument document;
    ArrayList<UploadDocumentTypeList> documentTypeMasterModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document_details);
        closeKeyBoard(Activity_UplaodDocument.this);
        activity = this;
        uploadflag=false;
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)){
            Permissions.checkAndRequest(Activity_UplaodDocument.this);
        }
        initview();
    }

    private void fnLoadInsuranceCompany() {
        progressDialogStart(this, "Please Wait...");
        try {
            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<UploadDocumentTypeResponse> call = objRetrofitInterface.fnGetAllInsuCompanyList(auth, apiKey, Vehicle_id);


            call.enqueue(new Callback<UploadDocumentTypeResponse>() {
                @Override
                public void onResponse(Call<UploadDocumentTypeResponse> call, Response<UploadDocumentTypeResponse> response) {
                    try {
                        progressDialogStop();
                         documentTypeMasterModels = response.body().getData();
                        if (documentTypeMasterModels.size() > 0) {
                            ArrayList<String> data = new ArrayList<>();
                            for (int i = 0; i < documentTypeMasterModels.size(); i++) {
                                UploadDocumentTypeList model = documentTypeMasterModels.get(i);
                                data.add(model.getInsuranceCompanyName());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Activity_UplaodDocument.this,android.R.layout.simple_spinner_item,data);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            insurance_co.setAdapter(adapter);
                            for (int i = 0; i < documentTypeMasterModels.size(); i++) {
                                if(document.getInsuranceCompanyName().equalsIgnoreCase(documentTypeMasterModels.get(i).getInsuranceCompanyName())){
                                    insurance_co.setSelection(i);
                                    break;
                                }
                            }

                        }

                    } catch (Exception ex) {
                        progressDialogStop();
                    }
                }

                @Override
                public void onFailure(Call<UploadDocumentTypeResponse> call, Throwable t) {
                    progressDialogStop();
                }
            });
        } catch (Exception ex) {
            progressDialogStop();
        }
    }


    private void initview() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.road_pulse_action_bar_color)));
        selectedDate  = Calendar.getInstance();
        currenttime = selectedDate.getTimeInMillis();
        edt_exp_date = (findViewById(R.id.edt_exp_date));
        edt_des = (findViewById(R.id.edt_des));
        edt_policy = (findViewById(R.id.edt_policy));
        edt_amt = (findViewById(R.id.edt_amt));
        insurance_co = findViewById(R.id.insurance_co);
        save_doc=findViewById(R.id.save_doc);
        attachment_doc = (findViewById(R.id.attachment_doc));
        attachment_pdf = findViewById(R.id.attachment_pdf);
        succes_attach = findViewById(R.id.succes_attach);
        attach_link = findViewById(R.id.attach_link);
        attachment_doc.setOnClickListener(this);
        attachment_pdf.setOnClickListener(this);
        save_doc.setOnClickListener(this);
        edt_exp_date.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        cust_id = sharedPreferences.getString("id", "");
        usertype_id = sharedPreferences.getString(getString(R.string.user_type_id), "");
        Intent ii = getIntent();
        boolean isEdit =ii.getBooleanExtra("edit",false);
        Vehicle_id =sharedPreferences.getString("doc_vid","");
        document = (UploadDocument) ii.getSerializableExtra("doc");
        if (document!=null && document.getAtributeId()!=null){
            switch (document.getAtributeId().toLowerCase()){
                case "rc":
                    doc_typee="rc";
                    if(!document.getFilePath().isEmpty()){
                        succes_attach.setVisibility(View.VISIBLE);
                    }
                    edt_exp_date.setText(document.getExpiryDate()==null?"":document.getExpiryDate());
                    edt_des.setText(document.getDescription()==null?"":document.getDescription());
                    succes_attach.setText(document.getFilePath()==null?"":document.getFileName());

                    if (isEdit) {
                        setTitle("Update RC Document");
                    }
                    else
                        setTitle("Upload RC Document");
                    break;
                case "puc":
                    doc_typee="puc";
                    if(!document.getFilePath().isEmpty()){
                        succes_attach.setVisibility(View.VISIBLE);
                    }
                    edt_exp_date.setText(document.getExpiryDate()==null?"":document.getExpiryDate());
                    edt_des.setText(document.getDescription()==null?"":document.getDescription());
                    succes_attach.setText(document.getFilePath()==null?"":document.getFileName());

                    if (isEdit) {
                        setTitle("Update PUC Document");
                    }
                    else
                        setTitle("Upload PUC Document");
                    break;
                case "other":
                    doc_typee="other";
                    if(!document.getFilePath().isEmpty()){
                        succes_attach.setVisibility(View.VISIBLE);
                    }
                    edt_exp_date.setText(document.getExpiryDate()==null?"":document.getExpiryDate());
                    edt_des.setText(document.getDescription()==null?"":document.getDescription());
                    succes_attach.setText(document.getFilePath()==null?"":document.getFileName());

                    if (isEdit) {
                        setTitle("Update Document");
                    }
                    else
                        setTitle("Upload Document");
                    break;
                case "insurance":
                    if(!document.getFilePath().isEmpty()){
                        succes_attach.setVisibility(View.VISIBLE);
                    }                    fnLoadInsuranceCompany();
                    edt_exp_date.setText(document.getExpiryDate()==null?"":document.getExpiryDate());
                    edt_amt.setText(document.getInsuranceAmount()==null?"":document.getInsuranceAmount());
                    edt_policy.setText(document.getInsurancePolicyNo()==null?"":document.getInsurancePolicyNo());
                    edt_des.setText(document.getDescription()==null?"":document.getDescription());
                    succes_attach.setText(document.getFilePath()==null?"":document.getFileName());

                    doc_typee="insurance";
                    if (isEdit) {
                        setTitle("Update Insurance Document");

                    }
                    else
                        setTitle("Upload Insurance Document");
                    findViewById(R.id.til_policy_no).setVisibility(View.VISIBLE);
                    findViewById(R.id.til_amount).setVisibility(View.VISIBLE);
                    insurance_co.setVisibility(View.VISIBLE);
            }
        }
        mCompressor= new FileCompressor(this);

    }



    @Override
    public void onClick(View view) {

        String PATH = "/data/data/travelpulse/";
        int attachmentChoice = 2;
        switch (view.getId()) {
            case R.id.attachment_doc:
                if (ContextCompat.checkSelfPermission(Activity_UplaodDocument.this, Permissions.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,Permissions.WRITE_EXTERNAL_STORAGE)){
                        ActivityCompat.requestPermissions(this,Permissions.PERMISSIONS,0);
                    }
                    else{
                        ActivityCompat.requestPermissions(this,Permissions.PERMISSIONS,0);
                    }

                }else {
                    uploadflag = true;
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    String[] mimetype = {"image/*", "application/pdf"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetype);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.perform_action_with)), RESULT_LOAD_IMAGE);
                }
                break;

            case R.id.attachment_pdf:
                uploadflag=true;
                Intent intentt = new Intent();
                intentt.setType("*/*");
                intentt.addCategory(Intent.CATEGORY_OPENABLE);
                intentt.setAction(Intent.ACTION_GET_CONTENT);
                intentt.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intentt, getString(R.string.perform_action_with)), attachmentChoice);
                break;

            case R.id.edt_exp_date:
                DatePickerDialog dp = new DatePickerDialog(Activity_UplaodDocument.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view,
                                                  int selectedYear, int selectedMonth,
                                                  int selectedDay) {
                                  year1=String.valueOf(selectedYear);
                                  month1=String.valueOf(selectedMonth+1);
                                  day1=String.valueOf(selectedDay);
                                fromDate = day1.concat("-").concat(month1).concat("-").concat(year1);
                                edt_exp_date.setText(day1 + "-" + month1 + "-"
                                        + year1);
                                selectedDate.set(selectedYear,selectedMonth,selectedDay);
                            }

                        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));

                dp.getDatePicker().setMinDate(currenttime);

                dp.show();
               break;
            case R.id.save_doc:

                if(document.getAtributeId().equalsIgnoreCase("insurance")) {
                    if (edt_exp_date.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Activity_UplaodDocument.this, "Please Enter Expiry Date", Toast.LENGTH_LONG).show();
                    }  else if (edt_amt.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Activity_UplaodDocument.this, "Please Enter Amount", Toast.LENGTH_LONG).show();
                    } else if (edt_policy.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Activity_UplaodDocument.this, "Please Enter Policy Number", Toast.LENGTH_LONG).show();
                    }
                    else if (selectedText.equalsIgnoreCase("0")) {
                        Toast.makeText(Activity_UplaodDocument.this, "Please Select Company Name", Toast.LENGTH_LONG).show();
                    }
                   else if (!edt_amt.getText().toString().isEmpty()&& Integer.parseInt(edt_amt.getText().toString())==0) {
                        Toast.makeText(Activity_UplaodDocument.this, "Please Enter Valid Amount", Toast.LENGTH_LONG).show();
                    }
                    else {
                        fnUploaddocument();
                    }
                }
                else  {
                    if (edt_exp_date.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Activity_UplaodDocument.this, "Please Enter Expiry Date", Toast.LENGTH_LONG).show();
                    }
                    else{
                        fnUploaddocument();
                    }
                }
                break;

            case R.id.attach_link:

                DownloadFromUrl(PATH +document.getFileName());

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
                    TempFile = new File(directory, filename );
                    openPath(selectedImage);
                    succes_attach.setVisibility(View.VISIBLE);
                    succes_attach.setText(filename+" Attached Successfully");

                }
                else if(selectedImage.toString().contains("content://com.google.android.apps.docs.storage")){

                    Toast.makeText(Activity_UplaodDocument.this,"Couldn't Upload file from Google Drive",Toast.LENGTH_LONG).show();
                }
                else  {
                    try {
                        String imagename=  getFileName(selectedImage);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        TempFile = mCompressor.compressToFile(saveImage(bitmap,imagename));
                        succes_attach.setVisibility(View.VISIBLE);
                        succes_attach.setText(imagename+" Attached Successfully");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }catch (NullPointerException ex){
                ex.printStackTrace();
                Toast.makeText(Activity_UplaodDocument.this,"File Format Not Supported",Toast.LENGTH_LONG).show();
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
            copyInputStreamToFile(is,TempFile);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            attach_link.setText(TempFile.getName());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    private void fnUploaddocument() {

        progressDialogStart(this,"Please Wait...");
            if (!uploadflag){
                requestBody = null;
                fileToUpload = null;
            }
            else{
                requestBody = RequestBody.create(MediaType.parse("*/*"), TempFile);
                 fileToUpload = MultipartBody.Part.createFormData("files_up", TempFile.getName(), requestBody);
            }
            selectedText = insurance_co.getSelectedItem()==null?"":insurance_co.getSelectedItem().toString();
            amount_inc=edt_amt.getText().toString();
            if(edt_amt.getText().toString().startsWith("0")){
                amount_inc=edt_amt.getText().toString().substring(1);
            }
            RequestBody User_id = RequestBody.create(MediaType.parse("text/plain"), cust_id);
            RequestBody USer_type_id = RequestBody.create(MediaType.parse("text/plain"), usertype_id);
            RequestBody vehicle_id = RequestBody.create(MediaType.parse("text/plain"), Vehicle_id);
            RequestBody doc_type = RequestBody.create(MediaType.parse("text/plain"), doc_typee);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), edt_des.getText().toString());
            RequestBody amount = RequestBody.create(MediaType.parse("text/plain"),amount_inc);
            RequestBody exp_date = RequestBody.create(MediaType.parse("text/plain"), edt_exp_date.getText().toString());
            RequestBody inc_comp = RequestBody.create(MediaType.parse("text/plain"), selectedText);
            RequestBody policy_no = RequestBody.create(MediaType.parse("text/plain"), edt_policy.getText().toString());

            auth = auth.replace("\n", "");
            RetrofitInterface objRetrofitInterface = ApiClient.getClient().create(RetrofitInterface.class);
            Call<UploadDocumentResponse> call = objRetrofitInterface.fnUploadDocument(auth, apiKey,User_id,USer_type_id, vehicle_id,
                    doc_type, description, amount, exp_date,inc_comp, policy_no,fileToUpload);
            call.enqueue(new Callback<UploadDocumentResponse>() {
                @Override
                public void onResponse(Call<UploadDocumentResponse> call, Response<UploadDocumentResponse> response) {

                    try {
                        progressDialogStop();
                       String req= bodyToString(call.request().body());

                        Log.e("request",req);

                        String documentTypeMasterModels = response.body().getStatus();
                        if(documentTypeMasterModels.equalsIgnoreCase("success")){
                            Log.e("response",documentTypeMasterModels+" "+response.body().getMsg());
                            Toast.makeText(Activity_UplaodDocument.this,response.body().getMsg(),Toast.LENGTH_LONG);
                            new AlertDialog.Builder(Activity_UplaodDocument.this)
                                .setMessage(response.body().getMsg())
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).show();
                        }
                        else {
                            try {
                                if(response.body().getData()!=null) {
                                    String msg = "";
                                    Set<String> keys = response.body().getData().keySet();
                                    for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                                        String key  = it.next();
                                        String value = msg + response.body().getData().get(key);
                                        msg = value+"\n";
                                    }
                                    Toast.makeText(Activity_UplaodDocument.this, msg, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Activity_UplaodDocument.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        progressDialogStop();
                    }
                }

                @Override
                public void onFailure(Call<UploadDocumentResponse> call, Throwable t) {
                    progressDialogStop();
                    t.getMessage();
                    new AlertDialog.Builder(Activity_UplaodDocument.this)
                            .setMessage("Failed to update")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();
                }
            });

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public File saveImage(Bitmap bitmap,String filename){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File directory = new File ((Environment.getExternalStorageDirectory()).toString() + "/tp");
        if (!directory.exists())
            directory.mkdirs();
        try {
            File f = new File(directory, filename);
            f.createNewFile();
            if(f.exists()) {
                FileOutputStream fo =  new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
            }
            return f;
        }
        catch (IOException e1){
            e1.printStackTrace();
        }
        return null;
    }


    public void DownloadFromUrl(String fileName) {
        try {
            URL url = new URL("https://test.roadpulse.net/upload\\/vehicle_docs\\/insurnace_879799_1563406478.png"); //you can write here any link
            File file = new File(fileName);
            Toast.makeText(Activity_UplaodDocument.this,"Starting download......",Toast.LENGTH_LONG).show();
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Toast.makeText(Activity_UplaodDocument.this,"Download Completed ",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(Activity_UplaodDocument.this,"Starting download......",Toast.LENGTH_LONG).show();
        }
    }


    protected void closeKeyBoard(Activity context) {
        View view =  context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
