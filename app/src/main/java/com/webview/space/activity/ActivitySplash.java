package com.webview.space.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.webview.space.AppConfig;
import com.webview.space.advertise.AdNetworkHelper;
import com.webview.space.databinding.ActivitySplashBinding;

public class ActivitySplash extends AppCompatActivity {

    private ActivitySplashBinding binding;

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
                startActivityMain();
            }
        });
        // start
        mAnimatorSet.start();
    }

    private void startActivityMain() {
        // init ads
        AdNetworkHelper adNetworkHelper = new AdNetworkHelper(this);
        adNetworkHelper.init();
        // init open ads for admob
        adNetworkHelper.loadAndShowOpenAppAd(this, AppConfig.ENABLE_SPLASH_OPEN_APP, () -> {
            new Handler(getMainLooper()).postDelayed(() -> {
                Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                startActivity(i);
                finish();
            }, 1000);
        });
    }
}