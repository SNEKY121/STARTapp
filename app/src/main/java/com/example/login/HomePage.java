package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        FloatingActionButton eLogout = findViewById(R.id.home_fabLogout);
        BottomNavigationView eNav = findViewById(R.id.home_nav);
        eNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        TextView username = findViewById(R.id.tv_username);
        username.setText(getIntent().getStringExtra("username"));
        openFragment(ProfileFragment.newInstance("", ""));

        eLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, GetStartedPage.class);
            startActivity(intent);
        });
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        final int profile_id = R.id.profile;
        final int settings_id = R.id.settings;
        switch (item.getItemId()) {
            case profile_id:
                openFragment(ProfileFragment.newInstance("", ""));
                return true;
            case settings_id:
                openFragment(SettingsFragment.newInstance("", ""));
                return true;
        }
        return false;
    };
}