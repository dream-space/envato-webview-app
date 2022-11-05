package com.android.webapp;
import com.android.webapp.activity.ActivityNotification;
import com.android.webapp.model.InterstitialMode;
import com.android.webapp.model.LoadingMode;
import com.android.webapp.data.ToolbarTitleMode;
import com.android.webapp.model.DrawerMenuItem;

import dreamspace.ads.sdk.data.AdNetworkType;

public class AppConfig {

    // menu and navigation -------------------------------------------------------------------------

    public static int DEFAULT_MENU_ID = 100;

    public static DrawerMenuItem[] DRAWER_MENU = {
            new DrawerMenuItem(100, R.drawable.ic_home, "Home", "https://demo.dream-space.web.id/webview/"),
            new DrawerMenuItem(200, R.drawable.ic_produts, "Product", "https://codecanyon.net/user/dream_space/portfolio?order_by=sales"),
            new DrawerMenuItem(300, R.drawable.ic_offline, "Offline", "file:///android_asset/offline.html"),
            new DrawerMenuItem(400, R.drawable.ic_video, "Videos", "https://youtube.com")
    };

    public static String DRAWER_SUBMENU_TITLE = "Others";

    public static DrawerMenuItem[] DRAWER_SUBMENU = {
            new DrawerMenuItem(500, R.drawable.ic_notifications, "Notification", ActivityNotification.class),
            new DrawerMenuItem(600, R.drawable.ic_home, "Rate App", "https://play.google.com/store/apps/developer?id=Dream+Space"),
            new DrawerMenuItem(700, R.drawable.ic_privacy, "Privacy", "https://dream-space.web.id/privacy-policy"),
            new DrawerMenuItem(800, R.drawable.ic_about, "About", "file:///android_asset/about.html")
    };

    public static boolean SHOW_DRAWER_NAVIGATION = true;

    // toolbar and loading -------------------------------------------------------------------------

    public static LoadingMode LOADING_MODE = LoadingMode.ALL;

    public static boolean TOOLBAR = true;
    public static boolean TOOLBAR_REFRESH_BUTTON = true;
    public static ToolbarTitleMode TOOLBAR_TITLE_MODE = ToolbarTitleMode.DRAWER_TITLE_MENU;

    // https://www.google.com/search?q=hex+color+picker
    public static String TOOLBAR_COLOR = "#6200EE";

    public static String TOOLBAR_TEXT_ICON_COLOR = "#FFFFFF";

    public static boolean SYSTEM_BAR_LIGHT = false;

    public static boolean EXIT_CONFIRMATION = true;

    // web and links -------------------------------------------------------------------------------

    public static String WEB_USER_AGENT = "";

    public static boolean WEB_GEOLOCATION = true;

    public static String[] DOWNLOAD_FILE_EXT = {
            ".*zip$", ".*rar$", ".*pdf$",
            ".*mp3$", ".*wav$",
            ".*mp4$", ".*mpg$",
            ".*drive.google.com.*file.*",
    };

    public static boolean OPEN_ALL_LINKS_EXTERNALLY = false;

    public static String[] LINKS_OPEN_EXTERNALLY = {
            "target=external",
            "play.google.com/store",
            "wa.me",
            "t.me"
    };

    public static String[] LINKS_OPEN_INTERNALLY = {
            "target=internal"
    };

    // notification --------------------------------------------------------------------------------

    public static String ONE_SIGNAL_APP_ID = "6543b161-3f62-40c1-9652-e30d39b32628";

    // ads networks --------------------------------------------------------------------------------

    public static boolean AD_ENABLE = true;
    public static boolean ENABLE_BANNER = true;
    public static boolean ENABLE_INTERSTITIAL = true;
    public static InterstitialMode SHOW_INTERSTITIAL_WHEN = InterstitialMode.URL_LOAD;

    public static boolean ENABLE_GDPR = true;
    public static boolean LEGACY_GDPR = false;

    public static AdNetworkType AD_NETWORK = AdNetworkType.ADMOB;
    public static int AD_INTERSTITIAL_INTERVAL = 10;

    public static String PRIVACY_POLICY_URL = "http://dream-space.web.id/privacy-policy";

    public static String AD_ADMOB_PUBLISHER_ID = "pub-3239677920600357";
    public static String AD_ADMOB_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
    public static String AD_ADMOB_INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

    public static String AD_FAN_BANNER_UNIT_ID = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String AD_FAN_INTERSTITIAL_UNIT_ID = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";

    public static String AD_IRONSOURCE_APP_KEY = "172a53645";
    public static String AD_IRONSOURCE_BANNER_UNIT_ID = "DefaultBanner";
    public static String AD_IRONSOURCE_INTERSTITIAL_UNIT_ID = "DefaultInterstitial";

    public static String AD_UNITY_GAME_ID = "4648853";
    public static String AD_UNITY_BANNER_UNIT_ID = "Banner_Android";
    public static String AD_UNITY_INTERSTITIAL_UNIT_ID = "Interstitial_Android";

}
