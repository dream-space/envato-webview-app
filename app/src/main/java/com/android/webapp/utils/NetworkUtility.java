package com.android.webapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

public final class NetworkUtility {
    private NetworkUtility() {
    }

    public static boolean isOnline(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static int getType(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null ? networkInfo.getType() : -1;
    }

    public static String getTypeName(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null ? networkInfo.getTypeName() : null;
    }
}
