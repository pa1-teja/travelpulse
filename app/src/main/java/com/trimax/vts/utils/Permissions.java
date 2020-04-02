package com.trimax.vts.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.trimax.vts.ApplicationM;
import com.trimax.vts.view.BuildConfig;
import com.trimax.vts.view.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



/**
 * Permissions check and request helper
 */
public class Permissions {
    private static final String TAG = Permissions.class.getName();
    private static final int REQUEST_CODE = (Permissions.class.hashCode() & 0xffff);
    private static final Map<String, Boolean> permissionsResults = new HashMap<>();

    // Permissions names
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String WAKE_LOCK = "android.permission.WAKE_LOCK";


    public static final String Location = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String Location1 = "android.permission.ACCESS_FINE_LOCATION";


    public static String[] PERMISSIONS = new String[]{
            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE,
            READ_PHONE_STATE,
           Location,
           Location1,
            WAKE_LOCK
    };

    /**
     * Checks for permission
     **/
    public static synchronized boolean isGranted(@NonNull Context context, @NonNull String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        Boolean result = permissionsResults.get(permission);
        if (result == null) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            result = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            permissionsResults.put(permission, result);
        }
        return result;
    }

    /**
     * Checks for permissions and notifies the user if they aren't granted
     **/
    public static synchronized void notifyIfNotGranted(Context context) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String permission : PERMISSIONS) {
            if (!isGranted(context, permission)) {
                if (count > 0) {
                    sb.append("\n");
                }
                String info = getPermissionInfo(context, permission);
                sb.append(info);
                sb.append(";");
                count++;
            }
        }

        if (count > 0) {
            int duration;
            String message = context.getString(R.string.app_name) + " ";
            if (count == 1) {
                duration = Toast.LENGTH_SHORT;
          //      message += context.getString(R.string.needs_permission) + ":\n" + sb.toString();
            } else {
                duration = Toast.LENGTH_LONG;
          //      message += context.getString(R.string.needs_permissions) + ":\n" + sb.toString();
            }
          //  Utils.showToast(context, message, duration);
        }
    }

    /**
     * Checks for permission and notifies if it isn't granted
     **/
    public static synchronized boolean notifyIfNotGranted(@NonNull Context context, @NonNull String permission) {
        if (!isGranted(context, permission)) {
            notify(context, permission);
            return true;
        }
        return false;
    }

    /**
     * Returns information string about permission
     **/
    @Nullable
    private static String getPermissionInfo(@NonNull Context context, @NonNull String permission) {
        context = context.getApplicationContext();
        PackageManager pm = context.getPackageManager();
        PermissionInfo info = null;
        try {
            info = pm.getPermissionInfo(permission, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException ex) {
            Log.w(TAG, ex);
        }

        if (info != null) {
            CharSequence label = info.loadLabel(pm);
            if (label == null) {
                label = info.nonLocalizedLabel;
            }
            return label.toString();
        }

        return null;
    }

    /**
     * Notifies the user if permission isn't granted
     **/
    private static void notify(@NonNull Context context, @NonNull String permission) {
        String info = getPermissionInfo(context, permission);
        if (info != null) {
           // String message = context.getString(R.string.app_name) + " " +
            //        context.getString(R.string.needs_permission) + ":\n" + info + ";";
          //  Utils.showToast(context, message, Toast.LENGTH_SHORT);
        }
    }

    /**
     * Checks for permissions and shows a dialog for permission granting
     **/
    public static void checkAndRequest( Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new LinkedList<>();
            for (String permission : PERMISSIONS) {
                if (!isGranted(context, permission)) {
                    permissions.add(permission);
                }
            }
            if (permissions.size() > 0) {
                String[] array = permissions.toArray(new String[permissions.size()]);
                ActivityCompat.requestPermissions(context, array, REQUEST_CODE);
            }
        }
    }

    /**
     * Resets permissions results cache
     **/
    public static synchronized void invalidateCache() {
        permissionsResults.clear();
    }

    /**
     * Saves the results of permission granting request
     **/
    public static synchronized void onRequestPermissionsResult(int requestCode,
                                                               @NonNull String[] permissions,
                                                               @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE &&
                permissions.length == grantResults.length) {
            for (int i = 0; i < permissions.length; i++) {
                boolean result = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                permissionsResults.put(permissions[i], result);
            }
        }
    }



    public  static void startPowerSaverIntent(final Context context) {
        SharedPreferences settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        boolean skipMessage = settings.getBoolean("skipProtectedAppCheck", false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            boolean foundCorrectIntent = false;
            for (final Intent intent : Permissions.POWERMANAGER_INTENTS) {
                if (isCallable(context, intent)) {
                    foundCorrectIntent = true;
                    final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(context);
                    dontShowAgain.setText("Do not show again");
                    dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            editor.putBoolean("skipProtectedAppCheck", isChecked);
                            editor.apply();
                        }
                    });

                    new AlertDialog.Builder(context)
                            .setTitle(Build.MANUFACTURER + " Protected Apps")
                            .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", context.getString(R.string.app_name)))
                            .setView(dontShowAgain)
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                    break;
                }
            }
            if (!foundCorrectIntent) {
                editor.putBoolean("skipProtectedAppCheck", true);
                editor.apply();
            }
        }
    }


    private static boolean isCallable(Context context, Intent intent) {
        try {
            if (intent == null || context == null) {
                return false;
            } else {
                List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                return list.size() > 0;
            }
        } catch (Exception ignored) {
            return false;
        }
    }


    public static final List<Intent> POWERMANAGER_INTENTS = Arrays.asList(
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerSaverModeActivity")),

            new Intent().setComponent(new ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerConsumptionActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")).setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).setData(Uri.parse("package:"+ ApplicationM.getContext().getPackageName())) : null,
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"))
                    .setData(android.net.Uri.parse("mobilemanager://function/entry/AutoStart")),
            new Intent().setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.SHOW_APPSEC")).addCategory(Intent.CATEGORY_DEFAULT).putExtra("packageName", BuildConfig.APPLICATION_ID)
    );
}
