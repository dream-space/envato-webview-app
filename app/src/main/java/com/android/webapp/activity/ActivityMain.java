package com.android.webapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.webapp.AppConfig;
import com.android.webapp.R;
import com.android.webapp.databinding.ActivityMainBinding;
import com.android.webapp.utils.NetworkUtility;
import com.android.webapp.utils.PermissionManager;
import com.android.webapp.utils.PermissionRationaleHandler;
import com.android.webapp.utils.ViewUtility;
import com.android.webapp.webview.VideoEnabledWebChromeClient;
import com.android.webapp.webview.VideoEnabledWebView;

import java.util.Arrays;

public class ActivityMain extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PermissionManager mPermissionManager = new PermissionManager(new PermissionRationaleHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initComponent();
        initToolbar();
        loadWebView(AppConfig.HOME_URL);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        ViewUtility.configureToolbar(this, binding.appBarLayout, binding.toolbar);
    }

    private void reloadWebView() {
        showLoading(true);
        showEmptyState(false, "");
        new Handler(this.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                loadWebView(binding.mainWebView.getUrl());
            }
        }, 1000);
    }

    private void loadWebView(String url) {
        if (NetworkUtility.isOnline(this)) {
            showLoading(true);
            binding.mainWebView.loadUrl(url);
        } else {
            showLoading(false);
            Toast.makeText(this, R.string.network_offline_msg, Toast.LENGTH_SHORT).show();
            showEmptyState(true, getResources().getString(R.string.network_offline_msg));
        }
    }

    private void initComponent() {
        ViewUtility.configureNavigation(this, binding.navigation);
        binding.swipeRefreshLayout.setEnabled(AppConfig.SWIPE_REFRESH);
        binding.progressBar.setEnabled(AppConfig.TOP_PROGRESS_BAR);

        // web view settings
        binding.mainWebView.getSettings().setJavaScriptEnabled(true);
        // binding.mainWebView.getSettings().setAppCacheEnabled(true);
        // binding.mainWebView.getSettings().setAppCachePath(this.getCacheDir().getAbsolutePath());
        binding.mainWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        binding.mainWebView.getSettings().setDomStorageEnabled(true);
        binding.mainWebView.getSettings().setDatabaseEnabled(true);
        binding.mainWebView.getSettings().setGeolocationEnabled(true);
        binding.mainWebView.getSettings().setSupportZoom(true);
        binding.mainWebView.getSettings().setBuiltInZoomControls(false);
        validateGeolocation();

        // webview chrome client
        View nonVideoLayout = binding.navigation;
        ViewGroup videoLayout = binding.mainVideoLayout;
        View progressView = getLayoutInflater().inflate(R.layout.placeholder_progress, null);
        VideoEnabledWebChromeClient webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, progressView, (VideoEnabledWebView) binding.mainWebView) {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                binding.progressBar.setProgress(progress);
                if (progress == 100) binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                validateGeolocation();
                // check permissions
                if (AppConfig.WEB_GEOLOCATION) {
                    mPermissionManager.request(ActivityMain.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            (requestable, permissionsResult) -> {
                                binding.mainWebView.setGeolocationEnabled(permissionsResult.isGranted());
                            }
                    );
                }
            }
        };

        webChromeClient.setOnToggledFullscreen(fullscreen -> {
            if (fullscreen) {
                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                getWindow().setAttributes(attrs);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            } else {
                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                getWindow().setAttributes(attrs);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });


        // web view style
        binding.mainWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // fixes scrollbar on Froyo
        binding.mainWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        binding.mainWebView.setWebViewClient(new CustomWebViewClient());
        binding.mainWebView.requestFocus(View.FOCUS_DOWN); // http://android24hours.blogspot.cz/2011/12/android-soft-keyboard-not-showing-on.html

        binding.mainWebView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (binding.mainWebView.getScrollY() == 0) {
                binding.swipeRefreshLayout.setEnabled(true);
            } else {
                binding.swipeRefreshLayout.setEnabled(false);
            }
        });

        binding.mainWebView.setWebChromeClient(webChromeClient);

        // on swipe list
        binding.swipeRefreshLayout.setOnRefreshListener(() -> reloadWebView());
    }

    private void showLoading(boolean loading) {
        swipeProgress(loading);
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.progressBar.setProgress(0);
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            binding.swipeRefreshLayout.setRefreshing(show);
            return;
        }
        binding.swipeRefreshLayout.post(() -> binding.swipeRefreshLayout.setRefreshing(true));
    }

    private void showEmptyState(boolean show, String msg) {
        TextView failedText = (TextView) binding.lytFailed.findViewById(R.id.failed_text);
        failedText.setText(msg);
        binding.lytFailed.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.mainWebView.setVisibility(show ? View.GONE : View.VISIBLE);
        (binding.lytFailed.findViewById(R.id.failed_retry)).setOnClickListener(view -> reloadWebView());
    }

    private void validateGeolocation() {
        if (PermissionManager.check(this, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).isGranted()) {
            binding.mainWebView.setGeolocationEnabled(true);
        } else {
            binding.mainWebView.setGeolocationEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Arrays.asList(permissions).contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            reloadWebView();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (binding.mainWebView.canGoBack()) {
            binding.mainWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // findViewById(R.id.adView).setVisibility(View.GONE);
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // findViewById(R.id.adView).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        menu.findItem(R.id.action_refresh).setVisible(AppConfig.TOOLBAR_REFRESH);
        ViewUtility.changeMenuIconColor(menu, Color.parseColor(AppConfig.TOOLBAR_TEXT_ICON_COLOR));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            reloadWebView();
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(final WebView view, final String url) {
            binding.mainWebView.setVisibility(View.VISIBLE); // hide progress bar with delay to show webview content smoothly
            CookieSyncManager.getInstance().sync(); // save cookies
            showLoading(false);
            if (AppConfig.TOOLBAR_WEB_TITLE) {
                binding.toolbar.setTitle(view.getTitle());
            }
        }

        @Override
        public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
            binding.mainWebView.setVisibility(View.INVISIBLE);
            showLoading(false);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            // forward to deprecated method
            onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            showLoading(false);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {

                // determine for opening the link externally or internally
//                boolean external = isLinkExternal(url);
//                boolean internal = isLinkInternal(url);
//                if (!external && !internal) {
//                    external = WebViewAppConfig.OPEN_LINKS_IN_EXTERNAL_BROWSER;
//                }

                // open the link
                if (false) {
                    // IntentUtility.startWebActivity(getContext(), url);
                    return true;
                } else {
                    showLoading(true);
                    return false;
                }
            } else if (url != null && url.startsWith("file://")) {
                // load url listener
                // ((LoadUrlListener) getActivity()).onLoadUrl(url);
                return false;
            } else {
                // return IntentUtility.startIntentActivity(getContext(), url);
                return false;
            }
        }
    }
}