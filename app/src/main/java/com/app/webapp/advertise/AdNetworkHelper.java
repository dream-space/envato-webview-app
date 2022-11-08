package com.app.webapp.advertise;

import android.app.Activity;
import android.content.Context;

import com.app.webapp.AppConfig;
import com.app.webapp.R;

import dreamspace.ads.sdk.AdConfig;
import dreamspace.ads.sdk.AdNetwork;
import dreamspace.ads.sdk.gdpr.GDPR;
import dreamspace.ads.sdk.gdpr.LegacyGDPR;
import dreamspace.ads.sdk.listener.AdBannerListener;

public class AdNetworkHelper {

    private Activity activity;
    private AdNetwork adNetwork;
    private LegacyGDPR legacyGDPR;
    private GDPR gdpr;

    public AdNetworkHelper(Activity activity) {
        this.activity = activity;
        adNetwork = new AdNetwork(activity);
        legacyGDPR = new LegacyGDPR(activity);
        gdpr = new GDPR(activity);
    }

    public void updateConsentStatus() {
        if (!AppConfig.AD_ENABLE || !AppConfig.ENABLE_GDPR) return;
        if (AppConfig.LEGACY_GDPR) {
            legacyGDPR.updateLegacyGDPRConsentStatus(AppConfig.AD_ADMOB_PUBLISHER_ID, AppConfig.PRIVACY_POLICY_URL);
        } else {
            gdpr.updateGDPRConsentStatus();
        }
    }

    public static void init(Context context) {
        AdConfig.ad_enable = AppConfig.AD_ENABLE;
        AdConfig.debug_mode = true;
        AdConfig.enable_gdpr = true;
        AdConfig.ad_network = AppConfig.AD_NETWORK;
        AdConfig.ad_inters_interval = AppConfig.AD_INTERSTITIAL_INTERVAL;

        AdConfig.ad_admob_publisher_id = AppConfig.AD_ADMOB_PUBLISHER_ID;
        AdConfig.ad_admob_banner_unit_id = AppConfig.AD_ADMOB_BANNER_UNIT_ID;
        AdConfig.ad_admob_interstitial_unit_id = AppConfig.AD_ADMOB_INTERSTITIAL_UNIT_ID;

        AdConfig.ad_fan_banner_unit_id = AppConfig.AD_FAN_BANNER_UNIT_ID;
        AdConfig.ad_fan_interstitial_unit_id = AppConfig.AD_FAN_INTERSTITIAL_UNIT_ID;

        AdConfig.ad_ironsource_app_key = AppConfig.AD_IRONSOURCE_APP_KEY;
        AdConfig.ad_ironsource_banner_unit_id = AppConfig.AD_IRONSOURCE_BANNER_UNIT_ID;
        AdConfig.ad_ironsource_interstitial_unit_id = AppConfig.AD_IRONSOURCE_INTERSTITIAL_UNIT_ID;

        AdConfig.ad_unity_game_id = AppConfig.AD_UNITY_GAME_ID;
        AdConfig.ad_unity_banner_unit_id = AppConfig.AD_UNITY_BANNER_UNIT_ID;
        AdConfig.ad_unity_interstitial_unit_id = AppConfig.AD_UNITY_INTERSTITIAL_UNIT_ID;

        AdNetwork.init(context);
    }

    public void loadBannerAd(boolean enable, AdBannerListener listener) {
        adNetwork.loadBannerAd(enable, activity.findViewById(R.id.ad_container), listener);
    }

    public void loadBannerAd(boolean enable) {
        adNetwork.loadBannerAd(enable, activity.findViewById(R.id.ad_container));
    }

    public void loadInterstitialAd(boolean enable) {
        adNetwork.loadInterstitialAd(enable);
    }

    public boolean showInterstitialAd(boolean enable) {
        return adNetwork.showInterstitialAd(enable);
    }

}
