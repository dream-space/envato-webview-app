package com.android.webapp.utils;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.webapp.AppConfig;
import com.android.webapp.BuildConfig;
import com.android.webapp.R;
import com.android.webapp.data.ThisApp;
import com.android.webapp.data.ToolbarTitleMode;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

public class Tools {

    public static void configureToolbar(AppCompatActivity act, AppBarLayout appBarLayout, Toolbar toolbar) {
        ActionBar bar = act.getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(AppConfig.TOOLBAR_TITLE_MODE != ToolbarTitleMode.DISABLE);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayHomeAsUpEnabled(AppConfig.SHOW_DRAWER_NAVIGATION);
        bar.setHomeButtonEnabled(AppConfig.SHOW_DRAWER_NAVIGATION);

        if (!AppConfig.TOOLBAR) bar.hide();
        if (AppConfig.SYSTEM_BAR_LIGHT) setSystemBarLight(act);
        toolbar.setBackgroundColor(Color.parseColor(AppConfig.TOOLBAR_COLOR));
        toolbar.setTitleTextColor(Color.parseColor(AppConfig.TOOLBAR_TEXT_ICON_COLOR));
        appBarLayout.setBackgroundColor(Color.parseColor(AppConfig.TOOLBAR_COLOR));
    }

    public static void configureNavigation(Activity act, View navigationView) {
        navigationView.setVisibility(AppConfig.SHOW_DRAWER_NAVIGATION ? View.VISIBLE : View.GONE);
    }

    private static void setSystemBarColor(Activity act, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    private static void setSystemBarLight(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = act.findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static void changeNavigateionIconColor(Toolbar toolbar, @ColorInt int color) {
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.mutate();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public static void changeMenuIconColor(Menu menu, @ColorInt int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable == null) continue;
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static boolean startIntentActivity(Context context, String url) {
        if (url != null && url.startsWith("mailto:")) {
            MailTo mailTo = MailTo.parse(url);
            startEmailActivity(context, mailTo.getTo(), mailTo.getSubject(), mailTo.getBody());
            return true;
        } else if (url != null && url.startsWith("tel:")) {
            startCallActivity(context, url);
            return true;
        } else if (url != null && url.startsWith("sms:")) {
            startSmsActivity(context, url);
            return true;
        } else if (url != null && url.startsWith("geo:")) {
            startMapSearchActivity(context, url);
            return true;
        } else if (url != null && url.startsWith("fb://")) {
            startWebActivity(context, url);
            return true;
        } else if (url != null && url.startsWith("twitter://")) {
            startWebActivity(context, url);
            return true;
        } else if (url != null && url.startsWith("whatsapp://")) {
            startWebActivity(context, url);
            return true;
        } else {
            return false;
        }
    }

    public static void startWebActivity(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // can't start activity
            e.printStackTrace();
        }
    }

    public static void startEmailActivity(Context context, String email, String subject, String text) {
        try {
            String uri = "mailto:" + email;
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // can't start activity
            e.printStackTrace();
        }
    }

    public static void startCallActivity(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // can't start activity
            e.printStackTrace();
        }
    }

    public static void startSmsActivity(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // can't start activity
            e.printStackTrace();
        }
    }

    public static void startMapSearchActivity(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // can't start activity
            e.printStackTrace();
        }
    }

    public static void startShareActivity(Context context, String subject, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // can't start activity
            e.printStackTrace();
        }
    }

    public static void startStoreActivity(Context context) {
        try {
            String uri = "https://play.google.com/store/apps/details?id=" + ThisApp.getInstance().getPackageName();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // can't start activity
            e.printStackTrace();
        }
    }

    public static boolean isOnline(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static void downloadFile(@NonNull Context context, @NonNull String url, @NonNull String suggestedFilename, @Nullable String mimeType, @Nullable String userAgent) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setTitle(suggestedFilename);
        request.setDescription(context.getString(R.string.main_downloading));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, suggestedFilename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();

        if (mimeType != null) {
            request.setMimeType(mimeType);
        }

        if (userAgent != null) {
            request.addRequestHeader("User-Agent", userAgent);
        }

        String cookies = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookies);

        // start download
        DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }

    public static boolean isDownloadableFile(String url) {
        url = url.toLowerCase();

        for (String type : AppConfig.DOWNLOAD_FILE_EXT) {
            if (url.matches(type)) return true;
        }

        return false;
    }

    public static String getFileName(String url) {
        int index = url.indexOf("?");
        if (index > -1) {
            url = url.substring(0, index);
        }
        url = url.toLowerCase();

        index = url.lastIndexOf("/");
        if (index > -1) {
            return url.substring(index + 1);
        } else {
            return Long.toString(System.currentTimeMillis());
        }
    }

    private static AppUpdateManager appUpdateManager;
    private static InstallStateUpdatedListener installStateUpdatedListener;

    public static void checkGooglePlayUpdateStopListener() {
        if (appUpdateManager != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    public static void checkGooglePlayUpdate(Activity activity) {
        if (BuildConfig.DEBUG) return;
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), R.string.update_ready, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.install, view -> {
                    if (appUpdateManager != null) appUpdateManager.completeUpdate();
                });
                snackbar.show();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (appUpdateManager != null && installStateUpdatedListener != null) {
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            } else {

            }
        };

        appUpdateManager = AppUpdateManagerFactory.create(activity);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateManager.registerListener(installStateUpdatedListener);
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, activity, 200);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
