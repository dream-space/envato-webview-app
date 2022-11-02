package com.android.webapp;
import com.android.webapp.data.LoadingMode;
import static com.android.webapp.data.LoadingMode.*;

public class AppConfig {

    public static final String HOME_URL = "https://mock.robotemplates.com/webviewapp/home.html";
    public static final LoadingMode LOADING_MODE = ALL;
    public static final boolean TOOLBAR_REFRESH = true;

    public static final boolean SHOW_OPTION_MENU = true;
    public static final String OPTION_URL = "https://mock.robotemplates.com/webviewapp/home.html";

    public static final boolean SHOW_BOTTOM_NAVIGATION = true;

    public static final boolean TOOLBAR = true;
    public static final String TOOLBAR_COLOR = "#FF6200EE";
    public static final boolean TOOLBAR_WEB_TITLE = false;
    public static final String TOOLBAR_TEXT_ICON_COLOR = "#FFFFFF";

    public static final boolean SYSTEM_BAR_LIGHT = false;
    public static final String SYSTEM_BAR_COLOR = "#FF3700B3";

    public static final boolean WEB_GEOLOCATION = true;

    public static final String[] DOWNLOAD_FILE_TYPES = {
            ".*zip$", ".*rar$", ".*pdf$", ".*doc$", ".*xls$",
            ".*mp3$", ".*wma$", ".*ogg$", ".*m4a$", ".*wav$",
            ".*avi$", ".*mov$", ".*mp4$", ".*mpg$", ".*3gp$",
            ".*drive.google.com.*file.*",
            ".*dropbox.com/s/.*"
    };


}
