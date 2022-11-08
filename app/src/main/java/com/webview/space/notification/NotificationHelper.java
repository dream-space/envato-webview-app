package com.webview.space.notification;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.webview.space.AppConfig;
import com.webview.space.BuildConfig;
import com.webview.space.activity.ActivityMain;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationHelper {

    public static void init(Context context) {
        oneSignalInit(context);
    }

    public static void oneSignalInit(Context context) {
        if (BuildConfig.DEBUG) {
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        }

        // init one signal with client data
        OneSignal.initWithContext(context);
        OneSignal.setAppId(AppConfig.ONE_SIGNAL_APP_ID);
        OneSignal.sendTag("APP", "WEBVIEW APP");

        // handle when use click notification
        onOneSignalOpenNotification(context);

    }

    public static void onOneSignalOpenNotification(Context context) {
        OneSignal.setNotificationOpenedHandler(result -> {
            OSNotification notification = result.getNotification();

            try {
                String url = null;
                String title = null;

                // notification data
                String launchURL = notification.getLaunchURL();
                JSONObject additionalData = notification.getAdditionalData();

                // get launch url
                if (launchURL != null) url = launchURL;

                // get url from additional data
                if (additionalData != null) {
                    if (additionalData.has("url")) {
                        url = additionalData.getString("url");
                    } else if (additionalData.has("URL")){
                        url = additionalData.getString("URL");
                    }  else if (additionalData.has("launchURL")) {
                        url = additionalData.getString("launchURL");
                    }
                }
                if (!TextUtils.isEmpty(notification.getTitle())) {
                    title = notification.getTitle();
                }

                // start activity
                Intent intent;
                if (url == null) {
                    intent = ActivityMain.navigate(context);
                } else {
                    intent = ActivityMain.navigate(context, title, url);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

    }

}
