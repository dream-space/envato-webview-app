package com.android.webapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.webapp.AppConfig;
import com.android.webapp.R;
import com.android.webapp.data.LoadingMode;
import com.android.webapp.databinding.ActivityMainBinding;
import com.android.webapp.utils.DownloadFileUtility;
import com.android.webapp.utils.NetworkUtility;
import com.android.webapp.utils.PermissionManager;
import com.android.webapp.utils.PermissionRationaleHandler;
import com.android.webapp.utils.ViewUtility;
import com.android.webapp.webview.AdvancedWebView;
import com.android.webapp.webview.VideoEnabledWebChromeClient;
import com.android.webapp.webview.VideoEnabledWebView;

import java.util.Arrays;

public class ActivityMain extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PermissionManager mPermissionManager = new PermissionManager(new PermissionRationaleHandler());
    private String lastDownloadUrl = "";
    private boolean webviewSuccess = true;

    private int mStoredActivityRequestCode;
    private int mStoredActivityResultCode;
    private Intent mStoredActivityResultIntent;

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
        showEmptyState(false, R.drawable.ic_error, "");
        binding.mainWebView.setVisibility(View.INVISIBLE);
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
            showEmptyState(true, R.drawable.ic_offline, getString(R.string.network_offline_msg));
        }
    }

    private void initComponent() {
        ViewUtility.configureNavigation(this, binding.navigation);
        binding.swipeRefreshLayout.setEnabled((AppConfig.LOADING_MODE.equals(LoadingMode.ALL) || AppConfig.LOADING_MODE.equals(LoadingMode.SWIPE_ONLY)));
        binding.progressBar.setEnabled((AppConfig.LOADING_MODE.equals(LoadingMode.ALL) || AppConfig.LOADING_MODE.equals(LoadingMode.TOP_BAR_ONLY)));

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
        binding.mainWebView.setListener(this, webViewListener);

        // on swipe list
        binding.swipeRefreshLayout.setOnRefreshListener(() -> reloadWebView());
    }

    AdvancedWebView.Listener webViewListener = new AdvancedWebView.Listener() {
        @Override
        public void onPageStarted(String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(String url) {

        }

        @Override
        public void onPageError(int errorCode, String description, String failingUrl) {

        }

        @Override
        public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
            mPermissionManager.request(ActivityMain.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionManager.PermissionAction<ActivityMain>() {
                        @Override
                        public void run(@NonNull ActivityMain requestable) {
                            requestable.handleDownloadPermissionGranted(url, suggestedFilename, mimeType, userAgent);
                        }
                    }
            );
        }

        @Override
        public void onExternalPageRequest(String url) {

        }
    };

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

    private void showEmptyState(boolean show, @DrawableRes int icon, String msg) {
        TextView failedText = (TextView) binding.lytFailed.findViewById(R.id.failed_text);
        ImageView failedIcon = (ImageView) binding.lytFailed.findViewById(R.id.icon);
        binding.lytFailed.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.mainWebView.setVisibility(show ? View.GONE : View.VISIBLE);
        (binding.lytFailed.findViewById(R.id.failed_retry)).setOnClickListener(view -> reloadWebView());
        if (!show) return;
        failedText.setText(msg);
        failedIcon.setImageResource(icon);
    }

    private void validateGeolocation() {
        if (PermissionManager.check(this, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).isGranted()) {
            binding.mainWebView.setGeolocationEnabled(true);
        } else {
            binding.mainWebView.setGeolocationEnabled(false);
        }
    }

    private void handleDownloadPermissionGranted(String url, String suggestedFilename, String mimeType, String userAgent) {
        Toast.makeText(this, R.string.main_downloading, Toast.LENGTH_LONG).show();
        DownloadFileUtility.downloadFile(this, url, suggestedFilename, mimeType, userAgent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Arrays.asList(permissions).contains(Manifest.permission.ACCESS_COARSE_LOCATION) || Arrays.asList(permissions).contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
            reloadWebView();
        } else if (Arrays.asList(permissions).contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // handle download file
            webViewListener.onDownloadRequested(lastDownloadUrl, DownloadFileUtility.getFileName(lastDownloadUrl), null, 0, null, null);
        } else if (Arrays.asList(permissions).contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // handle upload file
            if (mStoredActivityResultIntent != null) {
                binding.mainWebView.onActivityResult(mStoredActivityRequestCode, mStoredActivityResultCode, mStoredActivityResultIntent);
                mStoredActivityRequestCode = 0;
                mStoredActivityResultCode = 0;
                mStoredActivityResultIntent = null;
            } else {
                reloadWebView();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // handle upload action
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // check permissions
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionManager.PermissionsResult result = PermissionManager.check(this, permissions);

        if (result.isGranted()) {
            // permitted
            binding.mainWebView.onActivityResult(requestCode, resultCode, intent);
        } else {
            // not permitted
            mStoredActivityRequestCode = requestCode;
            mStoredActivityResultCode = resultCode;
            mStoredActivityResultIntent = intent;
            mPermissionManager.request(ActivityMain.this, Manifest.permission.READ_EXTERNAL_STORAGE, requestable -> {});
        }
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
            if (webviewSuccess) {
                binding.mainWebView.setVisibility(View.VISIBLE);
                CookieSyncManager.getInstance().sync();
                showLoading(false);
                showEmptyState(false, R.drawable.ic_error, "");
                if (AppConfig.TOOLBAR_WEB_TITLE) {
                    binding.toolbar.setTitle(view.getTitle());
                }
            } else {
                binding.mainWebView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
            webviewSuccess = false;
            binding.mainWebView.setVisibility(View.GONE);
            showLoading(false);
            showEmptyState(true, R.drawable.ic_error, getString(R.string.general_error_msg));
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            // forward to deprecated method
            onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webviewSuccess = true;
            if (DownloadFileUtility.isDownloadableFile(url)) {
                lastDownloadUrl = url;
                webViewListener.onDownloadRequested(url, DownloadFileUtility.getFileName(url), null, 0, null, null);
                return true;
            } else if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {

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