package com.android.webapp;
import com.android.webapp.data.LoadingMode;
import com.android.webapp.data.ToolbarTitleMode;
import com.android.webapp.model.DrawerMenuItem;

public class AppConfig {

    public static final int DEFAULT_MENU_ID = 100;

    public static final DrawerMenuItem[] DRAWER_MENU = {
            new DrawerMenuItem(100, R.drawable.ic_home, "Home", "https://demo.dream-space.web.id/webview/"),
            new DrawerMenuItem(200, R.drawable.ic_produts, "Product", "https://codecanyon.net/user/dream_space/portfolio?order_by=sales"),
            new DrawerMenuItem(300, R.drawable.ic_offline, "Offline", "file:///android_asset/about.html"),
            new DrawerMenuItem(400, R.drawable.ic_video, "Videos", "https://youtube.com")
    };

    public static final String DRAWER_SUBMENU_TITLE = "Others";

    public static final DrawerMenuItem[] DRAWER_SUBMENU = {
            new DrawerMenuItem(500, R.drawable.ic_home, "Rate App", "https://play.google.com/store/apps/developer?id=Dream+Space"),
            new DrawerMenuItem(600, R.drawable.ic_privacy, "Privacy", "https://dream-space.web.id/privacy-policy"),
            new DrawerMenuItem(700, R.drawable.ic_about, "About", "file:///android_asset/about.html")
    };

    public static final LoadingMode LOADING_MODE = LoadingMode.ALL;
    public static final boolean SHOW_DRAWER_NAVIGATION = true;

    public static final boolean TOOLBAR = true;
    public static final boolean TOOLBAR_REFRESH_BUTTON = true;
    public static final ToolbarTitleMode TOOLBAR_TITLE_MODE = ToolbarTitleMode.DRAWER_TITLE_MENU;

    // https://www.google.com/search?q=hex+color+picker
    public static final String TOOLBAR_COLOR = "#6200EE";

    public static final String TOOLBAR_TEXT_ICON_COLOR = "#FFFFFF";

    public static final boolean SYSTEM_BAR_LIGHT = false;

    public static final boolean WEB_GEOLOCATION = true;

    public static final String[] DOWNLOAD_FILE_EXT = {
            ".*zip$", ".*rar$", ".*pdf$",
            ".*mp3$", ".*wav$",
            ".*mp4$", ".*mpg$",
            ".*drive.google.com.*file.*",
    };

    public static final boolean OPEN_ALL_LINKS_EXTERNALLY = false;

    public static final String[] LINKS_OPEN_EXTERNALLY = {
            "target=external",
            "play.google.com/store",
            "wa.me",
            "t.me"
    };

    public static final String[] LINKS_OPEN_INTERNALLY = {
            "target=internal"
    };


}
