package com.android.webapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.webapp.R;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startActivityMain();
    }

    private void startActivityMain() {
        Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
        startActivity(i);
        finish();
    }
}