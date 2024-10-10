package com.webview.space.data;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.webview.space.AppConfig;
import com.webview.space.advertise.AdNetworkHelper;
import com.webview.space.notification.NotificationHelper;
import com.webview.space.utils.RemoteConfigHelper;

public class ThisApp extends Application {

    private static ThisApp mInstance;

    public static synchronized ThisApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        RemoteConfigHelper.getInstance().init();

        // Init Notification One Signal
        NotificationHelper.init(this);
    }

}