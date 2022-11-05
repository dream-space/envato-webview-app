package com.android.webapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.util.StringUtil;

import com.android.webapp.AppConfig;
import com.android.webapp.R;
import com.android.webapp.advertise.AdNetworkHelper;
import com.android.webapp.data.ToolbarTitleMode;
import com.android.webapp.databinding.ActivityMainBinding;
import com.android.webapp.model.DrawerMenuItem;
import com.android.webapp.model.InterstitialMode;
import com.android.webapp.model.LoadingMode;
import com.android.webapp.utils.PermissionManager;
import com.android.webapp.utils.PermissionRationaleHandler;
import com.android.webapp.utils.Tools;
import com.android.webapp.webview.AdvancedWebView;
import com.android.webapp.webview.VideoEnabledWebChromeClient;
import com.android.webapp.webview.VideoEnabledWebView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActivityMain extends AppCompatActivity {

    public static final String EXTRA_URL = "intent.EXTRA_URL";
    public static final String EXTRA_TITLE = "intent.EXTRA_TITLE";

    public static Intent navigate(Context context) {
        Intent intent = new Intent(context, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent navigate(Context context, String title, String url) {
        Intent intent = new Intent(context, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    private ActivityMainBinding binding;
    private PermissionManager mPermissionManager = new PermissionManager(new PermissionRationaleHandler());
    private String lastDownloadUrl = "";
    private String extraUrl = "", extraTitle = "";
    private boolean webviewSuccess = true;
    private ActionBar actionBar;
    private Map<Integer, DrawerMenuItem> menuMap;

    private int mStoredActivityRequestCode;
    private int mStoredActivityResultCode;
    private Intent mStoredActivityResultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        extraUrl = getIntent().getStringExtra(EXTRA_URL);
        extraTitle = getIntent().getStringExtra(EXTRA_TITLE);

        initComponent();
        initToolbar();
        setupNavigationDrawer();
        prepareAds();

        // load default menu page
        DrawerMenuItem firstMenu = menuMap.get(AppConfig.DEFAULT_MENU_ID);
        if(TextUtils.isEmpty(extraUrl)){
            if (firstMenu != null) onNavigationItemSelected(firstMenu);
        }  else {
            DrawerMenuItem notifItem = new DrawerMenuItem(100, R.drawable.ic_home, firstMenu.title, extraUrl);
            if(!TextUtils.isEmpty(extraTitle)) notifItem.title = extraTitle;
            onNavigationItemSelected(notifItem);
        }
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        actionBar = getSupportActionBar();
        Tools.configureToolbar(this, binding.appBarLayout, binding.toolbar);
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
        webviewSuccess = true;
        showLoading(true);
        binding.mainWebView.loadUrl(url);
        if (AppConfig.SHOW_INTERSTITIAL_WHEN == InterstitialMode.URL_LOAD) {
            showInterstitialAd();
        }
    }

    private void initComponent() {
        Tools.configureNavigation(this, binding.navigation);
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
        // user agent
        if (AppConfig.WEB_USER_AGENT != null && !AppConfig.WEB_USER_AGENT.equals("")) {
            binding.mainWebView.getSettings().setUserAgentString(AppConfig.WEB_USER_AGENT);
        }
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
        binding.mainWebView.requestFocus(View.FOCUS_DOWN);

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

    private void setupNavigationDrawer() {
        Menu menu = binding.navigation.getMenu();
        menuMap = new HashMap<>();
        for (DrawerMenuItem d : AppConfig.DRAWER_MENU) {
            menu.add(0, d.id, Menu.NONE, d.title).setIcon(d.icon);
            menuMap.put(d.id, d);
        }
        SubMenu subMenu = menu.addSubMenu(AppConfig.DRAWER_SUBMENU_TITLE);
        for (DrawerMenuItem d : AppConfig.DRAWER_SUBMENU) {
            subMenu.add(0, d.id, Menu.NONE, d.title).setIcon(d.icon);
            menuMap.put(d.id, d);
        }
        binding.navigation.invalidate();
        // navigation listener
        binding.navigation.setNavigationItemSelectedListener(item -> {
            DrawerMenuItem selectedMenu = menuMap.get(item.getItemId());
            onNavigationItemSelected(selectedMenu);
            return true;
        });

        if (!AppConfig.SHOW_DRAWER_NAVIGATION) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        } else {
            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.drawer_open, R.string.drawer_open);
            binding.drawerLayout.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
            binding.toolbar.setContentInsetStartWithNavigation(0);
        }
    }

    private void onNavigationItemSelected(DrawerMenuItem item) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        if (AppConfig.SHOW_INTERSTITIAL_WHEN == InterstitialMode.DRAWER_MENU_CLICK) {
            showInterstitialAd();
        }

        if(item.activity != null){
            startActivity(new Intent(this, item.activity));
            return;
        }

        loadWebView(item.url);
        if (AppConfig.TOOLBAR_TITLE_MODE == ToolbarTitleMode.DRAWER_TITLE_MENU) {
            actionBar.setTitle(item.title);
        }
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
        Tools.downloadFile(this, url, suggestedFilename, mimeType, userAgent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Arrays.asList(permissions).contains(Manifest.permission.ACCESS_COARSE_LOCATION) || Arrays.asList(permissions).contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
            reloadWebView();
        } else if (Arrays.asList(permissions).contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // handle download file
            webViewListener.onDownloadRequested(lastDownloadUrl, Tools.getFileName(lastDownloadUrl), null, 0, null, null);
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
            mPermissionManager.request(ActivityMain.this, Manifest.permission.READ_EXTERNAL_STORAGE, requestable -> {
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.mainWebView.canGoBack()) {
            binding.mainWebView.goBack();
        } else {
            if (AppConfig.EXIT_CONFIRMATION) {
                doExitApp();
            } else {
                super.onBackPressed();
            }
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
        menu.findItem(R.id.action_refresh).setVisible(AppConfig.TOOLBAR_REFRESH_BUTTON);
        Tools.changeMenuIconColor(menu, Color.parseColor(AppConfig.TOOLBAR_TEXT_ICON_COLOR));
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Tools.checkGooglePlayUpdate(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tools.checkGooglePlayUpdateStopListener();
    }

    private long exitTime = 0;
    public void doExitApp() {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START) && AppConfig.SHOW_DRAWER_NAVIGATION) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(final WebView view, final String url) {
            if (webviewSuccess) {
                binding.mainWebView.setVisibility(View.VISIBLE);
                CookieSyncManager.getInstance().sync();
                showLoading(false);
                showEmptyState(false, R.drawable.ic_error, "");
                if (AppConfig.TOOLBAR_TITLE_MODE == ToolbarTitleMode.FROM_WEB) {
                    actionBar.setTitle(view.getTitle());
                }
            } else {
                binding.mainWebView.setVisibility(View.INVISIBLE);
                if (!Tools.isOnline(ActivityMain.this)) {
                    showLoading(false);
                    Toast.makeText(ActivityMain.this, R.string.network_offline_msg, Toast.LENGTH_SHORT).show();
                    showEmptyState(true, R.drawable.ic_no_internet, getString(R.string.network_offline_msg));
                }
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
            if (Tools.isDownloadableFile(url)) {
                lastDownloadUrl = url;
                webViewListener.onDownloadRequested(url, Tools.getFileName(url), null, 0, null, null);
                return true;
            } else if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {

                boolean external = isLinkExternal(url);
                boolean internal = isLinkInternal(url);
                if (!external && !internal) {
                    external = AppConfig.OPEN_ALL_LINKS_EXTERNALLY;
                }

                // open the link
                if (external) {
                    Tools.startWebActivity(ActivityMain.this, url);
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
                return Tools.startIntentActivity(ActivityMain.this, url);
            }
        }
    }

    private AdNetworkHelper adNetworkHelper;

    private void prepareAds() {
        adNetworkHelper = new AdNetworkHelper(this);
        adNetworkHelper.updateConsentStatus();
        adNetworkHelper.loadBannerAd(AppConfig.ENABLE_BANNER);
        adNetworkHelper.loadInterstitialAd(AppConfig.ENABLE_INTERSTITIAL);
    }

    public void showInterstitialAd() {
        adNetworkHelper.showInterstitialAd(AppConfig.ENABLE_INTERSTITIAL);
    }

    private boolean isLinkExternal(String url) {
        for (String rule : AppConfig.LINKS_OPEN_EXTERNALLY) {
            if (url.contains(rule)) return true;
        }
        return false;
    }

    private boolean isLinkInternal(String url) {
        for (String rule : AppConfig.LINKS_OPEN_INTERNALLY) {
            if (url.contains(rule)) return true;
        }
        return false;
    }
}