package com.android.webapp.data;

import android.app.Application;

public class ThisApp extends Application {

    private static ThisApp mInstance;

    public static synchronized ThisApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }
}