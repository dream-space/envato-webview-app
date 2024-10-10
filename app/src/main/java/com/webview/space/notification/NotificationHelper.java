package com.webview.space.notification;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.onesignal.debug.LogLevel;
import com.onesignal.notifications.INotification;
import com.webview.space.AppConfig;
import com.webview.space.BuildConfig;
import com.webview.space.R;
import com.webview.space.activity.ActivityMain;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationHelper {

    public static void init(Context context) {
        oneSignalInit(context);
    }

    public static void oneSignalInit(Context context) {
        if (BuildConfig.DEBUG) {
            OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        }

        // init one signal with client data
        OneSignal.initWithContext(context, AppConfig.ONE_SIGNAL_APP_ID);
        OneSignal.getUser().addTag("APP", context.getResources().getString(R.string.app_name));

        // handle when use click notification
        onOneSignalOpenNotification(context);

    }

    public static void onOneSignalOpenNotification(Context context) {
        OneSignal.getNotifications().addClickListener(iNotificationClickEvent -> {
            INotification iNotification = iNotificationClickEvent.getNotification();
            JSONObject additionalData = iNotification.getAdditionalData();
            if(additionalData != null){
                try {
                    String url = null;
                    String title = null;

                    // notification data
                    String launchURL = iNotification.getLaunchURL();

                    // get launch url
                    if (launchURL != null) url = launchURL;

                    // get url from additional data
                    if (additionalData.has("url")) {
                        url = additionalData.getString("url");
                    } else if (additionalData.has("URL")){
                        url = additionalData.getString("URL");
                    }  else if (additionalData.has("launchURL")) {
                        url = additionalData.getString("launchURL");
                    }
                    if (!TextUtils.isEmpty(iNotification.getTitle())) {
                        title = iNotification.getTitle();
                    }

                    // start activity
                    Intent intent;
                    if (TextUtils.isEmpty(url)) {
                        intent = ActivityMain.navigate(context);
                    } else {
                        intent = ActivityMain.navigate(context, title, url);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
