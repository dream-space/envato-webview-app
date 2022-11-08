package com.app.webapp.data;

import android.app.Application;

import com.app.webapp.advertise.AdNetworkHelper;
import com.app.webapp.notification.NotificationHelper;

public class ThisApp extends Application {

    private static ThisApp mInstance;

    public static synchronized ThisApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // initialize ad network
        AdNetworkHelper.init(this);

        // Init Notification One Signal
        NotificationHelper.init(this);
    }
}