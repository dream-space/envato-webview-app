package com.webview.space;
import com.webview.space.activity.ActivityNotification;
import com.webview.space.model.InterstitialMode;
import com.webview.space.model.LoadingMode;
import com.webview.space.data.ToolbarTitleMode;
import com.webview.space.model.DrawerMenuItem;
import com.webview.space.utils.AppConfigExt;

import dreamspace.ads.sdk.data.AdNetworkType;

public class AppConfig extends AppConfigExt {

    // splash screen -----------------------------------------------------------------------------

    /* set true when you want show splash screen at start app */
    public static boolean SPLASH_SCREEN = true;

    // remote config -----------------------------------------------------------------------------

    /* set true when you want enable remote config for ads */
    public static boolean USE_REMOTE_CONFIG = true;


    // menu and navigation -------------------------------------------------------------------------

    /* Default menu id will show at first launch Activity Main,
    this value to DRAWER_MENU and DRAWER_SUBMENU */
    public static int DEFAULT_MENU_ID = 100;

    /* List of top drawer menu, this list menu will show at the top drawer menu,
    * we have two type constructor for DrawerMenuItem
    * Link value     : ( 1. your menu id , 2. your icon , 3. menu title , 4. menu link )
    * Activity value : ( 1. your menu id , 2. your icon , 3. menu title , 4. activity class )\ */
    public static DrawerMenuItem[] DRAWER_TOP_MENU = {
            new DrawerMenuItem(100, R.drawable.ic_home, "Home", "https://demo.dream-space.web.id/webview/"),
            new DrawerMenuItem(200, R.drawable.ic_produts, "Product", "https://codecanyon.net/user/dream_space/portfolio?order_by=sales"),
            new DrawerMenuItem(300, R.drawable.ic_offline, "Offline", "file:///android_asset/offline.html"),
            new DrawerMenuItem(400, R.drawable.ic_video, "Videos", "https://youtube.com"),
            new DrawerMenuItem(500, R.drawable.ic_open_in_new, "Input Url", "#INPUT")
    };

    /* menu top and bottom separator */
    public static String DRAWER_SUBMENU_TITLE = "Others";

    /* List of bottom drawer menu, This list menu will show at the bottom drawer menu */
    public static DrawerMenuItem[] DRAWER_SUBMENU = {
            new DrawerMenuItem(600, R.drawable.ic_notifications, "Notification", ActivityNotification.class),
            new DrawerMenuItem(700, R.drawable.ic_home, "Rate App", "https://play.google.com/store/apps/developer?id=Dream+Space"),
            new DrawerMenuItem(800, R.drawable.ic_privacy, "Privacy", "https://dream-space.web.id/privacy-policy"),
            new DrawerMenuItem(900, R.drawable.ic_about, "About", "file:///android_asset/about.html")
    };

    /* true for enabling drawer menu */
    public static boolean SHOW_DRAWER_NAVIGATION = true;


    // toolbar and loading -------------------------------------------------------------------------

    /* Configuration for Loading mode when webView load
    * DISABLE for not show any loading,
    * SWIPE_ONLY for show swipe refresh loading only,
    * TOP_BAR_ONLY for showing to bar loading only,
    * ALL for showing all mode loading (top bar and swipe refresh) */
    public static LoadingMode LOADING_MODE = LoadingMode.ALL;

    /* true for showing toolbar view */
    public static boolean TOOLBAR = true;

    /* true for showing toolbar refresh button at toolbar */
    public static boolean TOOLBAR_REFRESH_BUTTON = true;

    /* Configuration for toolbar title
     * DISABLE for hide toolbar title,
     * APP_NAME for show app name in toolbar,
     * DRAWER_TITLE_MENU for show title from drawer menu title,
     * FROM_WEB for showing title get from webpage title */
    public static ToolbarTitleMode TOOLBAR_TITLE_MODE = ToolbarTitleMode.DRAWER_TITLE_MENU;

    /* Configuration for toolbar color, this is hex color value
     * https://www.google.com/search?q=hex+color+picker */
    public static String TOOLBAR_COLOR = "#6200EE";

    /* Configuration for toolbar icon and text color, this is hex color value */
    public static String TOOLBAR_TEXT_ICON_COLOR = "#FFFFFF";

    /* true to make icon at system bar become dark */
    public static boolean SYSTEM_BAR_LIGHT = false;

    /* true for enabling exit confirmation when back button is pressed */
    public static boolean EXIT_CONFIRMATION = true;


    // web and links -------------------------------------------------------------------------------
    /* set user agent for the web view,
    * leave this value empty if you do not want to set user agent */
    public static String WEB_USER_AGENT = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";

    /* true for enabling web geolocation */
    public static boolean WEB_GEOLOCATION = true;

    /* list of file extensions for download,
    * use regular expression for this value,
    * make this empty for disable download manager */
    public static String[] DOWNLOAD_FILE_EXT = {
            ".*zip$", ".*rar$", ".*pdf$",
            ".*mp3$", ".*wav$",
            ".*mp4$", ".*mpg$",
            ".*drive.google.com.*file.*",
    };

    /* true for open link in external browser, no inside app */
    public static boolean OPEN_ALL_LINKS_EXTERNALLY = false;

    /* rules for opening links in external,
    * if URL link contains this string, it will be open in external browser */
    public static String[] LINKS_OPEN_EXTERNALLY = {
            "target=external",
            "play.google.com/store",
            "wa.me",
            "t.me"
    };

    /* rules for opening links in internal,
     * if URL link contains this string, it will be open in internal browser */
    public static String[] LINKS_OPEN_INTERNALLY = {
            "target=internal"
    };

    // VARIABLE BELOW CAN CHANGE FROM REMOTE CONFIG ------------------------------------------------
    // notification --------------------------------------------------------------------------------

    /* set with your one signal APP ID for enable notification
    * https://documentation.onesignal.com/docs/accounts-and-keys */
    public static String ONE_SIGNAL_APP_ID = "6543b161-3f62-40c1-9652-e30d39b32628";

    // ads networks --------------------------------------------------------------------------------

    /* true for enable ad */
    public static boolean AD_ENABLE = true;

    /* true for enable ad type banner */
    public static boolean ENABLE_BANNER = true;

    /* true for enable ad type interstitial */
    public static boolean ENABLE_INTERSTITIAL = true;

    /* true for enable ad type open app */
    public static boolean ENABLE_SPLASH_OPEN_APP = true;
    public static boolean ENABLE_GLOBAL_OPEN_APP = true;

    /* Mode when interstitial try to show
    * DRAWER_MENU_CLICK : for try show interstitial when one of drawer menu clicked
    * URL_LOAD          : for try show interstitial when url load*/
    public static InterstitialMode SHOW_INTERSTITIAL_WHEN = InterstitialMode.URL_LOAD;

    public static boolean ENABLE_GDPR = true;
    public static boolean SHOW_UMP = true;
    public static boolean LEGACY_GDPR = false;

    /* ad backup flow retry attempt cycle */
    public static Integer RETRY_FROM_START_MAX = 2;

    /* when ad networks not supported open app format, it will replace with interstitial format
     * for placement after plash screen only */
    public static boolean AD_REPLACE_UNSUPPORTED_OPEN_APP_WITH_INTERSTITIAL_ON_SPLASH = true;

    /* maximum load time in second for open app ads */
    public static Integer LIMIT_TIME_OPEN_APP_LOADING = 10;

    /* show interstitial after several action, this value for action counter */
    public static Integer AD_INTERS_INTERVAL = 5;

    /* MULTI Ad network selection,
     * Fill this array to enable ad backup flow, left this empty to use single ad_network above
     * app will try show sequentially from this array
     * example flow ADMOB > FAN > IRONSOURCE
     *
     * OPTION :
     * ADMOB, MANAGER, FAN, IRONSOURCE
     * */
    public static AdNetworkType[] AD_NETWORKS = {
            AdNetworkType.ADMOB,
            AdNetworkType.FAN,
            AdNetworkType.IRONSOURCE,
    };

    /* ----- Value below is unit id for ad networks, the name is pretty clear for unit ID  ------*/

    /* Ad unit for ADMOB */
    public static String AD_ADMOB_PUBLISHER_ID = "pub-3940256099942544";
    public static String AD_ADMOB_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    public static String AD_ADMOB_INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    public static String AD_ADMOB_REWARDED_UNIT_ID = "ca-app-pub-3940256099942544/5354046379";
    public static String AD_ADMOB_OPEN_APP_UNIT_ID = "ca-app-pub-3940256099942544/9257395921";

    /* Ad unit for Google Ad Manager */
    public static String AD_MANAGER_BANNER_UNIT_ID = "/6499/example/banner";
    public static String AD_MANAGER_INTERSTITIAL_UNIT_ID = "/6499/example/interstitial";
    public static String AD_MANAGER_REWARDED_UNIT_ID = "/6499/example/rewarded";
    public static String AD_MANAGER_OPEN_APP_UNIT_ID = "/6499/example/app-open";

    /* Ad unit for FAN */
    public static String AD_FAN_BANNER_UNIT_ID = "YOUR_PLACEMENT_ID";
    public static String AD_FAN_INTERSTITIAL_UNIT_ID = "YOUR_PLACEMENT_ID";
    public static String AD_FAN_REWARDED_UNIT_ID = "YOUR_PLACEMENT_ID";

    /* Ad unit for IRON SOURCE */
    public static String AD_IRONSOURCE_APP_KEY = "170112cfd";
    public static String AD_IRONSOURCE_BANNER_UNIT_ID = "DefaultBanner";
    public static String AD_IRONSOURCE_REWARDED_UNIT_ID = "DefaultRewardedVideo";
    public static String AD_IRONSOURCE_INTERSTITIAL_UNIT_ID = "DefaultInterstitial";

}
