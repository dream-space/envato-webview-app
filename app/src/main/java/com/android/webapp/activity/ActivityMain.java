package com.android.webapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.webapp.R;
import com.android.webapp.fragment.FragmentWebView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMain extends AppCompatActivity {

    private BottomNavigationView navigation;
    private Fragment fragmentHome, fragmentOption, fragmentSaved, fragmentSetting;
    private Fragment selectedFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }


    private void initComponent() {
        navigation = findViewById(R.id.navigation);
        fm = getSupportFragmentManager();

        fragmentHome = FragmentWebView.newInstance("https://dream-space.web.id/", "Test 123");
//        fragmentOption = new FragmentCategory();
//        fragmentSaved = new FragmentPage();
//        fragmentSetting = new FragmentLater();

        fm.beginTransaction().add(R.id.frame_content, fragmentHome, getString(R.string.title_home)).commit();
//        fm.beginTransaction().add(R.id.frame_content, fragmentOption, getString(R.string.title_option)).hide(fragmentOption).commit();
//        fm.beginTransaction().add(R.id.frame_content, fragmentSaved, getString(R.string.title_saved)).hide(fragmentSaved).commit();
//        fm.beginTransaction().add(R.id.frame_content, fragmentSetting, getString(R.string.title_setting)).hide(fragmentSetting).commit();
        selectedFragment = fragmentHome;

        navigation.setOnItemSelectedListener(item -> {
            displayFragment(item.getItemId());
            return true;
        });
    }

    public void displayFragment(int id) {
        if (id == R.id.nav_home) {
            fm.beginTransaction().hide(selectedFragment).show(fragmentHome).commit();
            selectedFragment = fragmentHome;
        }
    }
}