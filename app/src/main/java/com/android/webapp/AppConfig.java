package com.android.webapp;
import com.android.webapp.data.LoadingMode;
import com.android.webapp.data.ToolbarTitleMode;
import com.android.webapp.model.DrawerMenuItem;

import static com.android.webapp.data.LoadingMode.*;
import static com.android.webapp.data.ToolbarTitleMode.*;

public class AppConfig {

    public static final int DEFAULT_MENU_ID = 100;

    public static final DrawerMenuItem[] DRAWER_MENU = {
            new DrawerMenuItem(100, R.drawable.ic_home, "Home", "https://demo.dream-space.web.id/webview/"),
            new DrawerMenuItem(200, R.drawable.ic_produts, "Product", "https://codecanyon.net/user/dream_space/portfolio?order_by=sales"),
            new DrawerMenuItem(300, R.drawable.ic_offline, "Offline", "https://google.com"),
            new DrawerMenuItem(400, R.drawable.ic_video, "Videos", "https://youtube.com")
    };

    public static final String DRAWER_SUBMENU_TITLE = "Others";

    public static final DrawerMenuItem[] DRAWER_SUBMENU = {
            new DrawerMenuItem(500, R.drawable.ic_home, "Rate App", "https://play.google.com/store/apps/developer?id=Dream+Space"),
            new DrawerMenuItem(600, R.drawable.ic_privacy, "Privacy Policy", "https://mock.robotemplates.com/webviewapp/home.html"),
            new DrawerMenuItem(700, R.drawable.ic_about, "About", "https://mock.robotemplates.com/webviewapp/home.html")
    };

    public static final LoadingMode LOADING_MODE = ALL;
    public static final boolean TOOLBAR_REFRESH = true;

    public static final boolean SHOW_DRAWER_NAVIGATION = true;

    public static final boolean TOOLBAR = true;
    public static final ToolbarTitleMode TOOLBAR_TITLE_MODE = DRAWER_TITLE_MENU;

    public static final String TOOLBAR_COLOR = "#FF6200EE";

    public static final String TOOLBAR_TEXT_ICON_COLOR = "#FFFFFF";

    public static final boolean SYSTEM_BAR_LIGHT = false;

    public static final boolean WEB_GEOLOCATION = true;

    public static final String[] DOWNLOAD_FILE_TYPES = {
            ".*zip$", ".*rar$", ".*pdf$", ".*doc$", ".*xls$",
            ".*mp3$", ".*wma$", ".*ogg$", ".*m4a$", ".*wav$",
            ".*avi$", ".*mov$", ".*mp4$", ".*mpg$", ".*3gp$",
            ".*drive.google.com.*file.*",
            ".*dropbox.com/s/.*"
    };

    public static final boolean OPEN_LINKS_IN_EXTERNAL_BROWSER = false;

    public static final String[] LINKS_OPENED_IN_EXTERNAL_BROWSER = {
            "target=blank",
            "target=external",
            "play.google.com/store",
            "youtube.com/watch",
            "facebook.com/sharer",
            "twitter.com/share",
            "t.me"
    };

    public static final String[] LINKS_OPENED_IN_INTERNAL_WEBVIEW = {
            "target=webview",
            "target=internal"
    };


}
