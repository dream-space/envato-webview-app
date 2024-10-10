package com.webview.space.advertise;

import android.app.Activity;

import com.webview.space.AppConfig;
import com.webview.space.R;

import dreamspace.ads.sdk.AdConfig;
import dreamspace.ads.sdk.AdNetwork;
import dreamspace.ads.sdk.gdpr.GDPR;
import dreamspace.ads.sdk.gdpr.LegacyGDPR;
import dreamspace.ads.sdk.listener.AdOpenListener;
import dreamspace.ads.sdk.listener.AdRewardedListener;

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
        gdpr.updateGDPRConsentStatus();
    }

    public static void initConfig() {
        AdConfig.ad_enable = AppConfig.AD_ENABLE;
        AdConfig.ad_networks = AppConfig.AD_NETWORKS;
        AdConfig.retry_from_start_max = AppConfig.RETRY_FROM_START_MAX;
        AdConfig.enable_gdpr = AppConfig.ENABLE_GDPR;

        AdConfig.ad_replace_unsupported_open_app_with_interstitial_on_splash = AppConfig.AD_REPLACE_UNSUPPORTED_OPEN_APP_WITH_INTERSTITIAL_ON_SPLASH;
        AdConfig.ad_inters_interval = AppConfig.AD_INTERS_INTERVAL;
        AdConfig.ad_enable_open_app = AppConfig.ENABLE_GLOBAL_OPEN_APP;
        AdConfig.limit_time_open_app_loading = AppConfig.LIMIT_TIME_OPEN_APP_LOADING;
        AdConfig.debug_mode = false;

        AdConfig.ad_admob_publisher_id = AppConfig.AD_ADMOB_PUBLISHER_ID;
        AdConfig.ad_admob_banner_unit_id = AppConfig.AD_ADMOB_BANNER_UNIT_ID;
        AdConfig.ad_admob_interstitial_unit_id = AppConfig.AD_ADMOB_INTERSTITIAL_UNIT_ID;
        AdConfig.ad_admob_rewarded_unit_id = AppConfig.AD_ADMOB_REWARDED_UNIT_ID;
        AdConfig.ad_admob_open_app_unit_id = AppConfig.AD_ADMOB_OPEN_APP_UNIT_ID;

        AdConfig.ad_manager_banner_unit_id = AppConfig.AD_MANAGER_BANNER_UNIT_ID;
        AdConfig.ad_manager_interstitial_unit_id = AppConfig.AD_MANAGER_INTERSTITIAL_UNIT_ID;
        AdConfig.ad_manager_rewarded_unit_id = AppConfig.AD_MANAGER_REWARDED_UNIT_ID;
        AdConfig.ad_manager_open_app_unit_id = AppConfig.AD_MANAGER_OPEN_APP_UNIT_ID;

        AdConfig.ad_fan_banner_unit_id = AppConfig.AD_FAN_BANNER_UNIT_ID;
        AdConfig.ad_fan_interstitial_unit_id = AppConfig.AD_FAN_INTERSTITIAL_UNIT_ID;
        AdConfig.ad_fan_rewarded_unit_id = AppConfig.AD_FAN_REWARDED_UNIT_ID;

        AdConfig.ad_ironsource_app_key = AppConfig.AD_IRONSOURCE_APP_KEY;
        AdConfig.ad_ironsource_banner_unit_id = AppConfig.AD_IRONSOURCE_BANNER_UNIT_ID;
        AdConfig.ad_ironsource_rewarded_unit_id = AppConfig.AD_IRONSOURCE_REWARDED_UNIT_ID;
        AdConfig.ad_ironsource_interstitial_unit_id = AppConfig.AD_IRONSOURCE_INTERSTITIAL_UNIT_ID;
    }

    public void init() {
        AdNetworkHelper.initConfig();
        adNetwork.init();
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

    public void loadRewardedAd(boolean enable, AdRewardedListener listener) {
        adNetwork.loadRewardedAd(enable, listener);
    }

    public boolean showRewardedAd(boolean enable, AdRewardedListener listener) {
        return adNetwork.showRewardedAd(enable, listener);
    }

    public void loadAndShowOpenAppAd(Activity activity, boolean enable, AdOpenListener listener) {
        adNetwork.loadAndShowOpenAppAd(activity, enable, listener);
    }

    public void destroyAndDetachBanner() {
        adNetwork.destroyAndDetachBanner();
    }

    public void loadShowUMPConsentForm() {

    }

}
