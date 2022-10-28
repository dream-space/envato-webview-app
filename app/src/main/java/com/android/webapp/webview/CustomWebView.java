package com.android.webapp.webview;

import android.content.Context;
import android.util.AttributeSet;

public class CustomWebView extends VideoEnabledWebView {
    private OnScrollListener mOnScrollListener = null;

    public interface OnScrollListener {
        void onScrollChanged(CustomWebView customWebView, int x, int y, int oldx, int oldy);
    }

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }
}
