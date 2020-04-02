package com.trimax.vts.view.about;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.trimax.vts.view.BuildConfig;
import com.trimax.vts.view.R;

public class AboutUS extends AppCompatActivity {
    TextView link,version;
    Context context;
    View view1;
    WebView webView;
    ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_layout);
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("About Us");
        link =  findViewById(R.id.linkId);
        version =  findViewById(R.id.version_name_about);
        version.setText("Version "+ BuildConfig.VERSION_NAME);


        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (view1 == null) {

                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Please wait Loading...");
                        progressDialog.show();
                        view1 = LayoutInflater.from(context).inflate(R.layout.about_us_webview, null);
                        webView = (WebView) view1.findViewById(R.id.webview);

                        webView.getSettings().setJavaScriptEnabled(true);

                        webView.setWebViewClient(new WebViewClient() {

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }

                        });
                        webView.loadUrl(context.getString(R.string.privacy_policy_url));
                        dialog = new AlertDialog.Builder(context)
                                .setView(view1)
                                .setCancelable(true)
                                .create();
                        dialog.getWindow().getAttributes().windowAnimations = R.style.up_down_animation;
                        dialog.show();
                        view1 = null;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return  true;
    }
}
