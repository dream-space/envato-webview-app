package com.android.webapp.data;

import android.app.Application;

import com.android.webapp.advertise.AdNetworkHelper;

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
    }
}