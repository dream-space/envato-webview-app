package com.android.webapp.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.Toolbar;

import com.android.webapp.AppConfig;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewUtility {

    public static void configureToolbar(Activity act, AppBarLayout appBarLayout, Toolbar toolbar) {
        appBarLayout.setVisibility(AppConfig.TOOLBAR ? View.VISIBLE : View.GONE);
        setSystemBarColor(act, Color.parseColor(AppConfig.SYSTEM_BAR_COLOR));
        if (AppConfig.SYSTEM_BAR_LIGHT) setSystemBarLight(act);
        toolbar.setBackgroundColor(Color.parseColor(AppConfig.TOOLBAR_COLOR));
        toolbar.setTitleTextColor(Color.parseColor(AppConfig.TOOLBAR_TEXT_ICON_COLOR));
    }

    public static void configureNavigation(Activity act, BottomNavigationView navigationView) {
        navigationView.setVisibility(AppConfig.SHOW_BOTTOM_NAVIGATION ? View.VISIBLE : View.GONE);
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

}
