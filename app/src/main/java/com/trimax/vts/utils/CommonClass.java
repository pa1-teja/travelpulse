package com.trimax.vts.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.trimax.vts.ApplicationM;
import com.trimax.vts.database.DatabaseClass;
import com.trimax.vts.view.BuildConfig;
import com.trimax.vts.view.R;

import okhttp3.RequestBody;
import okio.Buffer;

import static com.trimax.vts.api.ApiClient.BASE_URL;

public class CommonClass {
    public static String creds = String.format("%s:%s", "android@roadpulse.in", "api@123");
    public static String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
    public static String apiKey = BuildConfig.DEBUG?"abcdefghijklmn":"abcdefghijklmn";
    public static String VehicleValue="";
    public static boolean loaddocument=false;
    public static String VehicleValueReplay="";
    public static MediaPlayer mediaPlayer=null;
    Context ctx;
    Typeface font_awesome;

    public CommonClass(Context ctx) {
        this.ctx = ctx;
        font_awesome = Typeface.createFromAsset(ctx.getAssets(), "fontawesome-webfont.ttf");
    }

    public static MediaPlayer getMediaPlayer() {
        if(mediaPlayer==null){
            mediaPlayer=  MediaPlayer.create(ApplicationM.getContext(), R.raw.dangerousspeed);
        }
        return mediaPlayer;
    }

    public boolean isConnected(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) {
                return true;
            }

            NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting()) {
                return true;
            }

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    public ArrayList<String> copyDB(Context ctx, String DB_PATH, String DB_NAME, String NewDBVersion) {
        ArrayList<String> result = new ArrayList<String>();

        try {
            InputStream fileToRead = ctx.getAssets().open(DB_NAME);
            File directory = new File(DB_PATH);

            if (!directory.exists()) {
                System.out.println("directory Dont Exist");
                directory.mkdir();
            }

            String outputFileName = DB_PATH + DB_NAME;
            File databaseFile = new File(outputFileName);
            DatabaseClass db = new DatabaseClass(ctx);
            db.DatabaseVersion_CreateTable();
            db.RulesPermission_CreateTable();
            // Do nothing if file exists already
            if (databaseFile.exists()) {
                System.out.println("DB Already Exist");
                //DatabaseClass db = new DatabaseClass(ctx);
                db.DatabaseVersion_CreateTable();
                db.RulesPermission_CreateTable();
                SQLiteDatabase myDB = null;
                String OldDBVersion = "";

                try {
                    myDB = ctx.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                    String query = "SELECT Version FROM DatabaseVersion";
                    Cursor cursor = myDB.rawQuery(query, null);

                    if (cursor.getCount() == 1) {
                        cursor.moveToFirst();
                        OldDBVersion = cursor.getString(cursor.getColumnIndex("Version"));
                    }
                    cursor.close();

                    if (!OldDBVersion.equals("")) {
                        int res = compareDBVersions(OldDBVersion, NewDBVersion);

                        if (res == -1) {
                            result.add("yes");
                            result.add(OldDBVersion);
                        } else {
                            result.add("no");
                        }
                    } else {
                        result.add("no");
                    }
                } catch (Exception e) {
                    Log.e("DBHelper", "Cannot open database" + e.toString());
                    result.add("no");
                } finally {
                    if (myDB != null) {
                        myDB.close();
                    }
                }
                return result;
            }

            OutputStream fileToWrite = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileToRead.read(buffer)) > 0) {
                fileToWrite.write(buffer, 0, length);
            }

            fileToWrite.flush();
            fileToWrite.close();
            fileToRead.close();
            System.out.println("Copied DB");
        } catch (IOException e) {
            new CommonClass(ctx).alertComman(ctx, "Sorry",
                    "Something Went Wrong!!");
        }
        result.add("no");
        return result;
    }

    public int compareDBVersions(String version1, String version2) {
        String[] arr1 = version1.split("\\.");
        String[] arr2 = version2.split("\\.");

        int i = 0;
        while (i < arr1.length || i < arr2.length) {
            if (i < arr1.length && i < arr2.length) {
                if (Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
                    return -1; //new version
                } else if (Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
                    return 1; //old version
                }
            } else if (i < arr1.length) {
                if (Integer.parseInt(arr1[i]) != 0) {
                    return 1;
                }
            } else if (i < arr2.length) {
                if (Integer.parseInt(arr2[i]) != 0) {
                    return -1;
                }
            }

            i++;
        }

        return 0; //same version
    }

    public void alertComman(Context _ctx, String alerttitle, String alertmessage) {
        new AlertDialog.Builder(_ctx)
                .setTitle(alerttitle)
                .setMessage(alertmessage)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                            }
                        })

                .show();
    }

    public static String getDevice_Id(Context ctx) {
        String DeviceId = "";
        try {
            TelephonyManager tManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            DeviceId = tManager.getDeviceId();
        }catch (SecurityException e){
            Crashlytics.logException(e);
            e.printStackTrace();
        }

        return DeviceId;
    }

    public void showSnackbar(View view, String msg){ Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }


    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
    public static void DisplayToast(final Context ctx, final String toastText, final String gravity) {

        ((Activity) ctx).runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(ctx, toastText, Toast.LENGTH_SHORT);

                if (gravity.toLowerCase().equals("center")) {
                    toast.setGravity(Gravity.CENTER, 0, 0);
                }

                toast.show();
            }
        });
    }

    public void CommonDialog(final Context ctx, final String CallType, String Title, String Message, String positiveBtnLabel, String negativeBtnLabel, Boolean Both) {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(ctx);
        confirmDialog.setTitle(Title);
        confirmDialog.setMessage(Message);
        confirmDialog.setCancelable(false);
        confirmDialog.setPositiveButton(positiveBtnLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                positiveButtonClick(ctx, CallType.toLowerCase(), dialog, id);
            }
        });

        if (Both) {
            confirmDialog.setNegativeButton(negativeBtnLabel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }
        AlertDialog alertDialog = confirmDialog.create();
        alertDialog.show();
    }

    public void positiveButtonClick(Context ctx, String CallType, DialogInterface dialog, int id) {
        dialog.cancel();
    }

    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public LatLng getLatLngFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            return latLng;
        } catch (Exception ex) {
            return null;
        }
    }

    public String getTimeDistanceOnRoad(double latitude, double longitude,
                                        double prelatitute, double prelongitude, String flag) {
        String result_in_min = "";
        String result_in_km = "";
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        String[] tag = {"text"};

        try {
            String url = "http://maps.google.com/maps/api/directions/json?origin="
                    + latitude + "," + longitude + "&destination=" + prelatitute
                    + "," + prelongitude + "&sensor=false&units=metric";
            URL url1 = new URL(url);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url1.openConnection();


            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            final JSONObject json = new JSONObject(data);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);

            JSONArray newTempARr = routes.getJSONArray("legs");
            JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
            JSONObject timeOb = newDisTimeOb.getJSONObject("duration");
            result_in_min = timeOb.getString("text");
            result_in_km = distOb.getString("text");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag.equals("Time")) {
            return result_in_min;
        } else {
            return result_in_km;
        }
    }


    public   static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }


}
