package com.webview.space.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.webview.space.AppConfig;
import com.webview.space.R;
import com.webview.space.advertise.AdNetworkHelper;
import com.webview.space.databinding.ActivitySplashBinding;
import com.webview.space.utils.RemoteConfigHelper;
import com.webview.space.utils.Tools;

public class ActivitySplash extends AppCompatActivity {

    private ActivitySplashBinding binding;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!AppConfig.SPLASH_SCREEN) {
            startActivityMain();
            return;
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(binding.icon, "alpha", 0, 1).setDuration(2000),
                ObjectAnimator.ofFloat(binding.icon, "translationY", 100, 0).setDuration(2000)
        );
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                requestRemoteConfig();
            }
        });
        // start
        mAnimatorSet.start();
    }

    private void requestRemoteConfig() {
        boolean connectToInternet = Tools.cekConnection(this);
        if (!connectToInternet) {
            dialogFailedRemoteConfig(getString(R.string.message_failed_config));
            return;
        }
        new Handler(this.getMainLooper()).postDelayed(() -> {
            try {
                if (!ActivitySplash.active && (alertDialog == null || !alertDialog.isShowing())) {
                    Log.d("REMOTE_CONFIG", "Reach limit request time");
                    dialogFailedRemoteConfig(getString(R.string.message_failed_config));
                }
            } catch (Exception e) {

            }
        }, 10000);

        RemoteConfigHelper.getInstance().fetch(new RemoteConfigHelper.Listener() {
            @Override
            public void onDisable() {
                startActivityDelay(false);
            }

            @Override
            public void onComplete(boolean success, FirebaseRemoteConfig firebaseRemoteConfig) {
                super.onComplete(success, firebaseRemoteConfig);
                if(success){
                    AppConfig.setFromRemoteConfig(firebaseRemoteConfig);
                }
                startActivityDelay(true);
            }
        });
    }

    private void startActivityDelay(boolean fast) {
        // init ads
        AdNetworkHelper adNetworkHelper = new AdNetworkHelper(this);
        adNetworkHelper.init();
        // init open ads for admob
        adNetworkHelper.loadAndShowOpenAppAd(this, AppConfig.ENABLE_SPLASH_OPEN_APP, () -> {
            new Handler(getMainLooper()).postDelayed(() -> {
                startActivityMain();
            }, fast ? 500 : 1000);
        });
    }

    private void startActivityMain() {
        Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
        startActivity(i);
        finish();
    }

    public void dialogFailedRemoteConfig(String message) {
        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.failed);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.RETRY, (dialog, which) -> {
            dialog.dismiss();
            requestRemoteConfig();
        });
        alertDialog = builder.show();
    }

    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }
}