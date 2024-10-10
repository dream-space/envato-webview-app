package com.webview.space.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.webview.space.AppConfig;
import com.webview.space.data.ThisApp;

public class RemoteConfigHelper {

    private static RemoteConfigHelper mInstance = null;
    private final FirebaseRemoteConfig firebaseRemoteConfig;

    public static synchronized RemoteConfigHelper getInstance() {
        if(mInstance == null){
            mInstance = new RemoteConfigHelper(ThisApp.getInstance());
        }
        return mInstance;
    }

    public static class Listener {
        public void onDisable() {

        }
        public void onComplete(boolean success, FirebaseRemoteConfig firebaseRemoteConfig){

        }
    }

    public RemoteConfigHelper(Context context) {
        FirebaseApp.initializeApp(context);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mInstance = this;
    }

    public void init() {
        if (!AppConfig.USE_REMOTE_CONFIG) return;
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .setFetchTimeoutInSeconds(3)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    public void fetch(Listener listener) {
        boolean connectToInternet = Tools.cekConnection(ThisApp.getInstance());
        if (!AppConfig.USE_REMOTE_CONFIG || !connectToInternet) {
            listener.onDisable();
            return;
        }
        Log.d("REMOTE_CONFIG", "requestRemoteConfig");
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("REMOTE_CONFIG", "SUCCESS");
                AppConfig.setFromRemoteConfig(firebaseRemoteConfig);
                listener.onComplete(true, firebaseRemoteConfig);
            } else {
                Log.d("REMOTE_CONFIG", "FAILED");
                listener.onComplete(false,firebaseRemoteConfig);
            }
        });
    }
}
