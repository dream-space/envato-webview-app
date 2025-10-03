package com.webview.space.utils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.webview.space.AppConfig;
import com.webview.space.model.InterstitialMode;

import dreamspace.ads.sdk.data.AdNetworkType;

public class AppConfigExt {

    public static boolean synced = false;

    /* --------------- DONE EDIT CODE BELOW ------------------------------------------------------ */

    public static void setFromRemoteConfig(FirebaseRemoteConfig remote) {

        if (!remote.getString("WEB_USER_AGENT").isEmpty()) {
            AppConfig.WEB_USER_AGENT = remote.getString("WEB_USER_AGENT");
        }

        /* notification configuration */

        if (!remote.getString("ONE_SIGNAL_APP_ID").isEmpty()) {
            AppConfig.ONE_SIGNAL_APP_ID = remote.getString("ONE_SIGNAL_APP_ID");
        }

        /* ads configuration */

        if (!remote.getString("AD_ENABLE").isEmpty()) {
            AppConfig.AD_ENABLE = Boolean.parseBoolean(remote.getString("AD_ENABLE"));
        }

        if (!remote.getString("ENABLE_BANNER").isEmpty()) {
            AppConfig.ENABLE_BANNER = Boolean.parseBoolean(remote.getString("ENABLE_BANNER"));
        }

        if (!remote.getString("ENABLE_INTERSTITIAL").isEmpty()) {
            AppConfig.ENABLE_INTERSTITIAL = Boolean.parseBoolean(remote.getString("ENABLE_INTERSTITIAL"));
        }

        if (!remote.getString("ENABLE_SPLASH_OPEN_APP").isEmpty()) {
            AppConfig.ENABLE_SPLASH_OPEN_APP = Boolean.parseBoolean(remote.getString("ENABLE_SPLASH_OPEN_APP"));
        }

        if (!remote.getString("ENABLE_GLOBAL_OPEN_APP").isEmpty()) {
            AppConfig.ENABLE_GLOBAL_OPEN_APP = Boolean.parseBoolean(remote.getString("ENABLE_GLOBAL_OPEN_APP"));
        }

        if (!remote.getString("AD_REPLACE_UNSUPPORTED_OPEN_APP_WITH_INTERSTITIAL_ON_SPLASH").isEmpty()) {
            AppConfig.AD_REPLACE_UNSUPPORTED_OPEN_APP_WITH_INTERSTITIAL_ON_SPLASH = Boolean.parseBoolean(remote.getString("AD_REPLACE_UNSUPPORTED_OPEN_APP_WITH_INTERSTITIAL_ON_SPLASH"));
        }

        if (!remote.getString("SHOW_INTERSTITIAL_WHEN").isEmpty()) {
            try {
                AppConfig.SHOW_INTERSTITIAL_WHEN = InterstitialMode.valueOf(remote.getString("SHOW_INTERSTITIAL_WHEN"));
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("ENABLE_GDPR").isEmpty()) {
            AppConfig.ENABLE_GDPR = Boolean.parseBoolean(remote.getString("ENABLE_GDPR"));
        }

        if (!remote.getString("SHOW_UMP").isEmpty()) {
            AppConfig.SHOW_UMP = Boolean.parseBoolean(remote.getString("SHOW_UMP"));
        }

        if (!remote.getString("RETRY_FROM_START_MAX").isEmpty()) {
            try {
                AppConfig.RETRY_FROM_START_MAX = (int) remote.getLong("SHOW_INTERSTITIAL_WHEN");
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("LIMIT_TIME_OPEN_APP_LOADING").isEmpty()) {
            try {
                AppConfig.LIMIT_TIME_OPEN_APP_LOADING = (int) remote.getLong("LIMIT_TIME_OPEN_APP_LOADING");
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("AD_INTERS_INTERVAL").isEmpty()) {
            try {
                AppConfig.AD_INTERS_INTERVAL = (int) remote.getLong("AD_INTERS_INTERVAL");
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("AD_NETWORKS").isEmpty()) {

            String[] arr = remote.getString("AD_NETWORKS").split(",");
            AdNetworkType[] adNetworkTypes = new AdNetworkType[arr.length];
            for (int i = 0; i < arr.length; i++) {
                try {
                    adNetworkTypes[i] = AdNetworkType.valueOf(arr[i].trim());
                } catch (Exception ignore) { }
            }
            AppConfig.AD_NETWORKS = adNetworkTypes;
        }

        // AdMob
        if (!remote.getString("AD_ADMOB_PUBLISHER_ID").isEmpty()) {
            AppConfig.AD_ADMOB_PUBLISHER_ID = remote.getString("AD_ADMOB_PUBLISHER_ID");
        }
        if (!remote.getString("AD_ADMOB_BANNER_UNIT_ID").isEmpty()) {
            AppConfig.AD_ADMOB_BANNER_UNIT_ID = remote.getString("AD_ADMOB_BANNER_UNIT_ID");
        }
        if (!remote.getString("AD_ADMOB_INTERSTITIAL_UNIT_ID").isEmpty()) {
            AppConfig.AD_ADMOB_INTERSTITIAL_UNIT_ID = remote.getString("AD_ADMOB_INTERSTITIAL_UNIT_ID");
        }
        if (!remote.getString("AD_ADMOB_REWARDED_UNIT_ID").isEmpty()) {
            AppConfig.AD_ADMOB_REWARDED_UNIT_ID = remote.getString("AD_ADMOB_REWARDED_UNIT_ID");
        }
        if (!remote.getString("AD_ADMOB_OPEN_APP_UNIT_ID").isEmpty()) {
            AppConfig.AD_ADMOB_OPEN_APP_UNIT_ID = remote.getString("AD_ADMOB_OPEN_APP_UNIT_ID");
        }

        // Google Ad Manager
        if (!remote.getString("AD_MANAGER_BANNER_UNIT_ID").isEmpty()) {
            AppConfig.AD_MANAGER_BANNER_UNIT_ID = remote.getString("AD_MANAGER_BANNER_UNIT_ID");
        }
        if (!remote.getString("AD_MANAGER_INTERSTITIAL_UNIT_ID").isEmpty()) {
            AppConfig.AD_MANAGER_INTERSTITIAL_UNIT_ID = remote.getString("AD_MANAGER_INTERSTITIAL_UNIT_ID");
        }
        if (!remote.getString("AD_MANAGER_REWARDED_UNIT_ID").isEmpty()) {
            AppConfig.AD_MANAGER_REWARDED_UNIT_ID = remote.getString("AD_MANAGER_REWARDED_UNIT_ID");
        }
        if (!remote.getString("AD_MANAGER_OPEN_APP_UNIT_ID").isEmpty()) {
            AppConfig.AD_MANAGER_OPEN_APP_UNIT_ID = remote.getString("AD_MANAGER_OPEN_APP_UNIT_ID");
        }

        // Facebook Audience Network (FAN)
        if (!remote.getString("AD_FAN_BANNER_UNIT_ID").isEmpty()) {
            AppConfig.AD_FAN_BANNER_UNIT_ID = remote.getString("AD_FAN_BANNER_UNIT_ID");
        }
        if (!remote.getString("AD_FAN_INTERSTITIAL_UNIT_ID").isEmpty()) {
            AppConfig.AD_FAN_INTERSTITIAL_UNIT_ID = remote.getString("AD_FAN_INTERSTITIAL_UNIT_ID");
        }
        if (!remote.getString("AD_FAN_REWARDED_UNIT_ID").isEmpty()) {
            AppConfig.AD_FAN_REWARDED_UNIT_ID = remote.getString("AD_FAN_REWARDED_UNIT_ID");
        }

        // IronSource
        if (!remote.getString("AD_IRONSOURCE_APP_KEY").isEmpty()) {
            AppConfig.AD_IRONSOURCE_APP_KEY = remote.getString("AD_IRONSOURCE_APP_KEY");
        }
        if (!remote.getString("AD_IRONSOURCE_BANNER_UNIT_ID").isEmpty()) {
            AppConfig.AD_IRONSOURCE_BANNER_UNIT_ID = remote.getString("AD_IRONSOURCE_BANNER_UNIT_ID");
        }
        if (!remote.getString("AD_IRONSOURCE_REWARDED_UNIT_ID").isEmpty()) {
            AppConfig.AD_IRONSOURCE_REWARDED_UNIT_ID = remote.getString("AD_IRONSOURCE_REWARDED_UNIT_ID");
        }
        if (!remote.getString("AD_IRONSOURCE_INTERSTITIAL_UNIT_ID").isEmpty()) {
            AppConfig.AD_IRONSOURCE_INTERSTITIAL_UNIT_ID = remote.getString("AD_IRONSOURCE_INTERSTITIAL_UNIT_ID");
        }

        synced = true;
    }
}
