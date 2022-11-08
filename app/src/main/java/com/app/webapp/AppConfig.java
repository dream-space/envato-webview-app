package com.app.webapp;
import com.app.webapp.R;
import com.app.webapp.activity.ActivityNotification;
import com.app.webapp.model.InterstitialMode;
import com.app.webapp.model.LoadingMode;
import com.app.webapp.data.ToolbarTitleMode;
import com.app.webapp.model.DrawerMenuItem;

import dreamspace.ads.sdk.data.AdNetworkType;

public class AppConfig {

    // splash screen -----------------------------------------------------------------------------

    /* set true when you want show splash screen at start app */
    public static boolean SPLASH_SCREEN = true;


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
            new DrawerMenuItem(400, R.drawable.ic_video, "Videos", "https://youtube.com")
    };

    /* menu top and bottom separator */
    public static String DRAWER_SUBMENU_TITLE = "Others";

    /* List of bottom drawer menu, This list menu will show at the bottom drawer menu */
    public static DrawerMenuItem[] DRAWER_SUBMENU = {
            new DrawerMenuItem(500, R.drawable.ic_notifications, "Notification", ActivityNotification.class),
            new DrawerMenuItem(600, R.drawable.ic_home, "Rate App", "https://play.google.com/store/apps/developer?id=Dream+Space"),
            new DrawerMenuItem(700, R.drawable.ic_privacy, "Privacy", "https://dream-space.web.id/privacy-policy"),
            new DrawerMenuItem(800, R.drawable.ic_about, "About", "file:///android_asset/about.html")
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
    public static String WEB_USER_AGENT = "";

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

    /* Mode when interstitial try to show
    * DRAWER_MENU_CLICK : for try show interstitial when one of drawer menu clicked
    * URL_LOAD          : for try show interstitial when url load*/
    public static InterstitialMode SHOW_INTERSTITIAL_WHEN = InterstitialMode.URL_LOAD;

    public static boolean ENABLE_GDPR = true;
    public static boolean LEGACY_GDPR = false;

    /* Ad networks selection,
     * Available ad networks ADMOB, FAN, UNITY, IRONSOURCE, APPLOVIN */
    public static AdNetworkType AD_NETWORK = AdNetworkType.ADMOB;

    /* show interstitial after several action, this value for action counter */
    public static int AD_INTERSTITIAL_INTERVAL = 10;

    public static String PRIVACY_POLICY_URL = "http://dream-space.web.id/privacy-policy";

    /* ----- Value below is unit id for ad networks, the name is pretty clear for unit ID  ------*/

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
