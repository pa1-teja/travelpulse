package com.trimax.vts.view.notifications;

import com.trimax.vts.view.model.NotificationSettingModel;

import java.util.ArrayList;

public class NotificationSettingProvider {

    public static ArrayList<NotificationSettingModel> getOSWiseSettings(){
        ArrayList<NotificationSettingModel> settings = new ArrayList<>();
        NotificationSettingModel model = new NotificationSettingModel("Android version 6.0.1+","For devices running Android version 6.0.1 and newer, check that your battery optimisation is not preventing <strong>Travelpulse</strong> from sending you notifications:<br/>");
        model.setSetting("1. Go to <strong>Settings.</strong><br/>" +
                "2. Select <strong>Battery</strong> option.<br/>" +
                "3. Tap the <strong>3 dot icon</strong> and choose <strong>Battery Optimization.</strong><br/>" +
                "4. Find <strong>Travelpulse </strong>in the list.<br/>" +
                "5. Tap <strong>Travelpulse</strong>, then tap <strong>Don’t optimize.</strong><br/>");
        settings.add(model);

        model = new NotificationSettingModel("Android version 7+","");
        model.setSetting("1.Go to <strong>Settings.</strong><br/>" +
                "2. Select <strong>Apps</strong>.<br/>" +
                "3. Find and select <strong>Travelpulse</strong>.<br/>" +
                "4. Select <strong>Battery</strong>, then tap <strong>Battery Optimisation</strong>.<br/>" +
                "5. From here, select the <strong>Apps not optimised</strong> option and change it to <strong>All apps</strong>.<br/>" +
                "6. Find <strong>Travelpulse</strong> in this list and set to <strong>Don’t optimise</strong>.");
        settings.add(model);

        model = new NotificationSettingModel("Android version 9","For devices running Android version 9, make sure the Adaptive Battery feature is not preventing Travelpulse from sending you notifications:<br/>");
        model.setSetting("1. From the app drawer, tap <strong>Settings</strong>.<br/>" +
                "2. Select <strong>Battery</strong>.<br/>" +
                "3. Choose <strong>Adaptive Battery</strong>.<br/>" +
                "4. Tap <strong>Restricted apps</strong> and make sure Travelpulse is not listed.");
        settings.add(model);
        return settings;
    }


    public static ArrayList<NotificationSettingModel> getAppWiseSettings(){
        ArrayList<NotificationSettingModel> settings = new ArrayList<>();
        NotificationSettingModel model = new NotificationSettingModel("Clean Master","This third-party app manages battery saving and RAM.<br/>");
        model.setSetting("1. Open the app and select <strong>Tools</strong> tab.<br/>" +
                "2. Select the<strong> Notification Cleaner.</strong><br/>" +
                "3. Tap the&nbsp;<strong>Setting icon.</strong><br/>" +
                "4. Disable</strong> this feature for Travelpulse only or for all notifications.");
        settings.add(model);

        model = new NotificationSettingModel("Security Master","This third-party security app could be blocking Travelpulse from sending notifications:<br/>");
        model.setSetting("1. Open the app and select <strong>Notification</strong> <strong>Cleaner.</strong><br/>" +
                "2. Tap the</span> Setting.</strong><br/>" +
                "3. Ensure that <strong>Travelpulse</strong> is <strong>unticked</strong> and listed under <strong>Apps – Notifications Allowed</strong>.<br/>" +
                "4. Note:</strong> In some cases, you may need to uninstall the app in order to receive mobile notifications.");
        settings.add(model);

        return settings;
    }


    public static ArrayList<NotificationSettingModel> getDeviceWiseSettings() {
        ArrayList<NotificationSettingModel> settings = new ArrayList<>();

        /*Asus*/
        NotificationSettingModel model = new NotificationSettingModel("Asus Zenfone (Android 7.0)","");
        model.setSetting("<p><strong>Step 1: Ensure that Travelpulse is set to auto-start</strong></p>" +
                "1. Go to <strong>Settings</strong>.<br/>" +
                "2. Tap&nbsp;<strong>Power Management</strong>.<br/>" +
                "3. Select&nbsp;<strong>Auto-start Manager</strong>.<br/>" +
                "4. Find <strong>Travelpulse</strong> and make sure it’s enabled.<br/>" +
                "<hr />" +
                "<p><strong>Step 2: List Travelpulse as a protected app</strong></p>" +
                "1. Select <strong>Mobile Manager</strong> app.<br/>" +
                "2. Tap&nbsp;<strong>Boost</strong>.<br/>" +
                "3. Tap&nbsp;<strong>Enable Super Boost</strong>, then select&nbsp;<strong>Protected app list</strong>.<br/>" +
                "4. Verify that Travelpulse is on the list.");
        settings.add(model);

        model = new NotificationSettingModel("Asus Zenfone 2","Make sure that Travelpulse is allowed to begin on startup from your Auto-start manager:");
        model.setSetting("1. From the app drawer, tap <strong>Apps.</strong><br/>" +
                "2. Tap <strong>Auto-start manager.</strong><br/>" +
                "3. Toggle Travelpulse to <strong>Allow.</strong>");
        settings.add(model);

        /*Honor*/
        model = new NotificationSettingModel("Huawei Honor 6","");
        model.setSetting("<p><strong>Step 1: List Travelpulse as a protected app</strong></p>" +
                "1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Power Saving. </strong><br/>" +
                "3. Tap<strong> Protected apps.</strong><br/>" +
                "4. Toggle <strong>Travelpulse</strong> to enable.<br/>" +
                "<hr />" +
                "<p><strong>Step 2: Enable Travelpulse in the Notification Manager</strong></p>" +
                "1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Notification Manager.</strong><br/>" +
                "3. Select<strong> Notify </strong>for Travelpulse to receive push notifications.<br/>");
        settings.add(model);

        model = new NotificationSettingModel("Huawei Honor 8","");
        model.setSetting("<p><strong>Step 1: List Travelpulse as a protected app</strong></p>" +
                "1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Advanced Settings</strong>.<br/>" +
                "3. Tap<strong> Battery Manager</strong>.<br/>" +
                "4. Tap<strong> Protected apps&nbsp;</strong>(or&nbsp;<strong>Close apps after screen lock</strong>).<br/>" +
                "5. Toggle <strong>Travelpulse</strong> to enable." +
                "<hr/>" +
                "<p><strong>Step 2: Ignore battery optimisations for Travelpulse</strong></p>" +
                "1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Apps.</strong><br/>" +
                "3. Tap<strong> Advanced.</strong><br/>" +
                "4. Select <strong>Ignore battery optimizations.</strong><br/>" +
                "5. Choose <strong>Travelpulse</strong> and ignore the app.<br/>" +
                "<hr>" +
                "<p><strong>Step 3: Allow notifications from Travelpulse</strong></p>" +
                "1. Go to <strong>Settings.</strong><br/>" +
                "2. Select the<strong> Notification Panel &amp; Status Bar.</strong><br/>" +
                "3. Tap<strong> Notification Center </strong>and find<strong> Travelpulse.</strong><br/>" +
                "4. Ensure that <strong>Allow Notifications </strong>and<strong> Priority Display </strong>are<strong> active</strong>.");
        settings.add(model);

        /*Huawei*/
        model = new NotificationSettingModel("Huawei P8 lite","");
        model.setSetting("<p><strong>Step 1: Enable Travelpulse to run at startup</strong></p>" +
                "1. Go to <strong>Phone Manager.</strong><br/>" +
                "2. Swipe left and tap <strong>Startup Manager.</strong><br/>" +
                "3. Tap <strong>Travelpulse</strong> and ensure that it’s set to run automatically at system startup." +
                "<p><strong>Step 2: Allow notifications from Travelpulse</strong></p>" +
                "1. From the app drawer, tap <strong>Phone Manager.</strong><br/>" +
                "2. Swipe left and tap <strong>Notification Manager.</strong><br/>" +
                "3. Tap <strong>Rules</strong> and ensure that Travelpulse is set up to send notifications to the notification panel. Check if any other settings in this view could be blocking notifications.");
        settings.add(model);

        model = new NotificationSettingModel("Huawei Mate 8 & Huawei Nova Plus","<p>Make sure that Travelpulse is exempt from your device’s battery optimisation features:</p>");
        model.setSetting("1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Advanced Settings.</strong><br/>" +
                "3. Tap<strong> Battery Manager.</strong><br/>" +
                "4. Tap<strong> Protected apps.</strong><br/>" +
                "5. Toggle <strong>Travelpulse</strong> to enable.");
        settings.add(model);

        model = new NotificationSettingModel("Huawei P20 & P30","");
        model.setSetting("1. Go to <strong>Settings</strong>.<br/>" +
                "2. Tap&nbsp;<strong>Apps</strong>.<br/>" +
                "3. Tap&nbsp;<strong>Travelpulse</strong>.<br/>" +
                "4. Select&nbsp;<strong>Power usage</strong>, followed by&nbsp;<strong>App launch</strong>.<br/>" +
                "5. Toggle&nbsp;<strong>Manage automatically</strong> to&nbsp;<strong>Off</strong>.<br/>" +
                "6. Then, toggle&nbsp;<strong>Auto-launch</strong>,&nbsp;<strong>Secondary launch</strong> and&nbsp;<strong>Run in Background</strong> to&nbsp;<strong>On</strong>.");
        settings.add(model);

        /*LeEco/LeTV*/
        model = new NotificationSettingModel("LeEco/LeTV","If you have enabled Lock Screen Cleanup and Ultra Long Standby during Sleep, you must disable them to allow Travelpulse notifications.<br/>");
        model.setSetting("<p><strong>Step 1: Enable Travelpulse to run at startup</strong></p>" +
                "1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Permissions.</strong><br/>" +
                "3. Tap<strong> Manage Auto Launch</strong>. If you’ve enabled other apps to auto-launch, you’ll see <strong>You’ve enabled [x] apps to auto launch</strong>.<br/>" +
                "4. Toggle <strong>Travelpulse</strong> to enable.<br/>" +
                "<p><strong>Step 2: List Travelpulse as a protected app</strong></p>" +
                "1. From the app drawer, tap <strong>Settings.</strong><br/>" +
                "2. Select <strong>Battery</strong>, then tap <strong>Power saving management.</strong><br/>" +
                "3. Select<strong> App Protection. </strong><br/>" +
                "4. Toggle <strong>Travelpulse</strong> to enable.");
        settings.add(model);

        /*Lenovo*/
        model = new NotificationSettingModel("Lenovo","Enable the auto-start setting for the Travelpulse app:<br/>");
        model.setSetting("1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap <strong>Power Manager.</strong><br/>" +
                "3. Select <strong>Background app management.</strong><br/>" +
                "4. Toggle Travelpulse to <strong>Allow auto-start. </strong>");
        settings.add(model);

        /*Oneplus*/
        model = new NotificationSettingModel("OnePlus (Android 7.0 and earlier)","For versions earlier than Android&nbsp;8.0 Oreo, make sure that Travelpulse is set to auto-launch:<br/>");
        model.setSetting("1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Apps.</strong><br/>" +
                "3. Tap the<strong> <em class=\"ts_icon ts_icon_cog_o\">&nbsp;</em>cog icon </strong>in the top right.<br/>" +
                "4. Select<strong> App Autolaunch.</strong><br/>" +
                "5. Toggle&nbsp;<strong>Travelpulse</strong> to enable.");
        settings.add(model);

        model = new NotificationSettingModel("OnePlus (Android 8.0 and later)","");
        model.setSetting("<p><strong>Step 1: Check that app storage is set to clear normally</strong></p>" +
                "1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap <strong>Advanced.</strong><br/>" +
                "3. Select <strong>Recent apps management.</strong><br/>" +
                "4. Ensure that <strong>Normal clear</strong> is enabled." +
                "<p><strong>Step 2: Exclude Travelpulse from battery optimisation</strong></p>" +
                "1. Go to <strong>Settings</strong>.<br/>" +
                "2. Tap&nbsp;<strong>Battery</strong>, then&nbsp;<strong>Battery Optimisation</strong>.<br/>" +
                "3. Tap&nbsp;<strong>Apps not optimised</strong> and select&nbsp;<strong>All apps</strong>.<br/>" +
                "4. Find Travelpulse and make sure it’s set to&nbsp;<strong>Don’t optimise</strong>.");
        settings.add(model);

        model = new NotificationSettingModel("OnePlus 3","Adjust your battery optimisation to allow notifications from Travelpulse when it’s running in the background:<br/>");
        model.setSetting("1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Battery.</strong><br/>" +
                "3. Select<strong> Battery Optimization.</strong><br/>" +
                "4. Tap the <strong>Overflow menu </strong>in the top right of that screen.<br/>" +
                "5. Select<strong> Advanced Optimization.</strong><br/>" +
                "6. Toggle <strong>Travelpulse </strong>to<strong> off.</strong>");
        settings.add(model);

        /*Oppo*/
        model = new NotificationSettingModel("OPPO","");
        model.setSetting("<p><strong>Step 1: List Travelpulse as a protected app</strong></p>" +
                "1. Go to <strong>Settings</strong>.<br/>" +
                "2. Tap&nbsp;<strong>Advanced Settings</strong>.<br/>" +
                "3. Tap<strong>&nbsp;Battery Manager</strong>, then&nbsp;<strong>Protected Apps</strong>.<br/>" +
                "4. Toggle&nbsp;<strong>Travelpulse</strong> to enable protection." +
                "<p><strong>Step 2: Ignore battery optimisation for Travelpulse</strong></p>" +
                "1. Tap on <strong>Settings</strong>.<br/>" +
                "2. Select <strong>Battery</strong>, then tap on <strong>Energy Saver</strong>.<br/>" +
                "3. Find <strong>Travelpulse</strong> and ensure that <strong>Freeze when in Background</strong>, <strong>Abnormal Apps Optimisation</strong> and <strong>Doze</strong> are all disabled." +
                "<p><strong>Step 3: Allow notifications from Travelpulse</strong></p>" +
                "1. Go to <strong>Settings</strong>.<br/>" +
                "2. Tap&nbsp;<strong>Notification Panel &amp; Status Bar</strong>.<br/>" +
                "3. Tap&nbsp;<strong>Notification Centre</strong>.<br/>" +
                "4. Find Travelpulse and activate&nbsp;<strong>allow notifications </strong>and&nbsp;<strong>priority display</strong>." +
                "<p><strong>Step 4: Add Travelpulse to start up manager</strong></p>" +
                "1. Open the <strong>Security&nbsp;</strong>app.<br/>" +
                "2. Select <strong>Privacy Permissions</strong>, then <strong>Start up manager</strong>.<br/>" +
                "3. Find <strong>Travelpulse</strong> and ensure this setting is enabled.");
        settings.add(model);

        /*Samsung*/
        model = new NotificationSettingModel("Samsung","");
        model.setSetting("<p><strong>Step 1: Prioritise notifications from Travelpulse</strong></p>" +
                "1. Go to <strong>Settings</strong>.<br/>" +
                "2. Tap&nbsp;<strong>Apps</strong>.<br/>" +
                "3. Select&nbsp;<strong>Travelpulse</strong>.<br/>" +
                "4. Under&nbsp;<strong >App Setting</strong>, tap&nbsp;<strong >Notifications</strong>.<br/>" +
                "5. Select&nbsp;<strong >Messages &amp; Mentions</strong>( Android 9.0 <strong>Advanced</strong>).<br/>" +
                "6. Select&nbsp;<strong >Do Not Disturb Custom Exception</strong>." +
                "<p><strong>Step 2: Exclude Travelpulse from battery optimisation</strong></p>" +
                "1. Go to <strong>Settings.</strong><br/>" +
                "2. Tap<strong> Device management.</strong><br/>" +
                "3. Select<strong> Battery.</strong><br/>" +
                "4. Tap <strong>Unmonitored apps.</strong><br/>" +
                "5. Add <strong>Travelpulse</strong> to this list.");
        settings.add(model);

        /*Xiaomi*/
        model = new NotificationSettingModel("Xiaomi","");
        model.setSetting("<p><strong> Step 1: check that Travelpulse is given permission to autostart</strong></p>" +
                "1. Go to <strong>Security.</strong><br/>" +
                "2. Tap <strong>Permissions.</strong><br/>" +
                "3. Tap <strong>Autostart.</strong><br/>" +
                "4. Toggle <strong>Travelpulse</strong> to enable.<br/>" +
                "<p><strong>Step 2: ignore battery restrictions for Travelpulse</strong></p>" +
                "1. Go to <strong>Settings</strong>.<br/>" +
                "2. Select <strong>Manage Apps’ Battery Usage</strong>.<br/>" +
                "3. Tap <strong>Apps</strong>.<br/>" +
                "4. Tap <strong>Travelpulse</strong>, then tap <strong>No Restrictions</strong>." +
                "<p><strong>Step 3: Allow notifications from Travelpulse</strong></p>" +
                "1. Go to <strong>Settings</strong>.<br/>" +
                "2. Select <strong>App Notifications</strong>.<br/>" +
                "3. Tap <strong>Travelpulse</strong>.<br/>" +
                "4. Toggle on <strong>Set as priority</strong>.");
        settings.add(model);

        /*Vivo*/
        model = new NotificationSettingModel("Vivo","");
        model.setSetting("<p>Go to <strong>Setting </strong> and select <strong>Battery</strong>, check <strong>Normal mode</strong> then click on <strong>High Background Power Consumption</strong> and then enable TravelPulse.</p>" +
                "<p>Go to <strong>Settings</strong> and click on <strong>More Settings</strong> then <strong>App Manager</strong> and Select TravelPulse and enable Allow notification and Show on Lock Screen</p>" +
                "<p>Go to <strong>Settings</strong> and click on <strong>More Settings</strong> and then <strong>Permission Management</strong>. Click <strong>Autostart</strong> and enable TravelPulse App</p>");
        settings.add(model);

        return settings;
    }
}
