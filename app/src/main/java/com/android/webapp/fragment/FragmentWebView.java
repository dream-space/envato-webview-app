package com.android.webapp.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.webapp.R;
import com.android.webapp.utils.NetworkUtility;
import com.android.webapp.webview.CustomWebView;

import im.delight.android.webview.AdvancedWebView;

public class FragmentWebView extends Fragment {

    private static final String EXTRA_URL = "url";
    private static final String EXTRA_SHARE = "share";

    private View mRootView;
    private CustomWebView mWebView;
    private String mUrl = "about:blank";

    public static FragmentWebView newInstance(String url, String share) {
        FragmentWebView fragment = new FragmentWebView();

        // arguments
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_URL, url);
        arguments.putString(EXTRA_SHARE, share);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handle fragment arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        mWebView = mRootView.findViewById(R.id.main_webview);
        refreshData();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // restore webview state
        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        }

        // setup webview
        initComponent();
    }


    public void refreshData() {
        if (NetworkUtility.isOnline(getActivity())) {
            // show progress popup
            // showProgress(true);

            // load web url
            mWebView.loadUrl(mUrl);
        } else {
            // showProgress(false);
            Toast.makeText(getActivity(), "global network offline", Toast.LENGTH_LONG).show();
        }
    }

    private void handleArguments(Bundle arguments) {
        if (arguments.containsKey(EXTRA_URL)) {
            mUrl = arguments.getString(EXTRA_URL);
        }
        if (arguments.containsKey(EXTRA_SHARE)) {
            //mShare = arguments.getString(ARGUMENT_SHARE);
        }
    }


    private void showContent(final long delay) {
        if (getActivity() != null && mRootView != null) {
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initComponent() {
        // web view settings
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.getSettings().setAppCacheEnabled(true);
        //mWebView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(false);

        // advanced webview settings
        mWebView.setListener(getActivity(), new AdvancedWebView.Listener() {
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

            }

            @Override
            public void onExternalPageRequest(String url) {

            }
        });
        mWebView.setGeolocationEnabled(true);

        // webview style
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // fixes scrollbar on Froyo

        // webview hardware acceleration
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // webview chrome client

        // webview client
        mWebView.setWebViewClient(new MyWebViewClient());

        // webview key listener
        //mWebView.setOnKeyListener(new WebViewOnKeyListener((DrawerStateListener) getActivity()));

        // webview touch listener
        mWebView.requestFocus(View.FOCUS_DOWN); // http://android24hours.blogspot.cz/2011/12/android-soft-keyboard-not-showing-on.html
        //mWebView.setOnTouchListener(new WebViewOnTouchListener());

        // webview scroll listener
        //((RoboWebView) mWebView).setOnScrollListener(new WebViewOnScrollListener()); // not used

        // admob
        // setupBannerView();
    }

    private class MyWebViewClient extends WebViewClient {
        private boolean mSuccess = true;

        @Override
        public void onPageFinished(final WebView view, final String url) {
            if (getActivity() != null && mSuccess) {
                showContent(500); // hide progress bar with delay to show webview content smoothly
                CookieSyncManager.getInstance().sync(); // save cookies
            }
            mSuccess = true;
        }

        @Override
        public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
            if (getActivity() != null) {
                mSuccess = false;
                mWebView.setVisibility(View.INVISIBLE);
                //showProgress(false);
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            // forward to deprecated method
            onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        //
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (DownloadFileUtility.isDownloadableFile(url)) {
//                onDownloadRequested(url, DownloadFileUtility.getFileName(url), null, 0, null, null);
//                return true;
//            } else if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
//                // load url listener
//                ((LoadUrlListener) getActivity()).onLoadUrl(url);
//
//                // determine for opening the link externally or internally
//                boolean external = isLinkExternal(url);
//                boolean internal = isLinkInternal(url);
//                if (!external && !internal) {
//                    external = WebViewAppConfig.OPEN_LINKS_IN_EXTERNAL_BROWSER;
//                }
//
//                // open the link
//                if (external) {
//                    IntentUtility.startWebActivity(getContext(), url);
//                    return true;
//                } else {
//                    showProgress(true);
//                    return false;
//                }
//            } else if (url != null && url.startsWith("file://")) {
//                // load url listener
//                ((LoadUrlListener) getActivity()).onLoadUrl(url);
//                return false;
//            } else {
//                return IntentUtility.startIntentActivity(getContext(), url);
//            }
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // save current instance state
        super.onSaveInstanceState(outState);

        // save web view state
        mWebView.saveState(outState);
    }

}
