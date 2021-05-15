package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {

    public String username;
    public static String email;

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

        try {
            username = getIntent().getStringExtra("username");
            email = getIntent().getStringExtra("email");
        } catch (Exception e) {
            username = "not working";
        }
        BottomNavigationView eNav = findViewById(R.id.home_nav);
        eNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(CoursesFragment.newInstance("", ""));

    }

    private long pressedTime;

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            resetPref();
            super.onBackPressed();
            finish();
        } else {
            String close = getResources().getString(R.string.close_app);
            Toast.makeText(getBaseContext(), close, Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        final int profile_id = R.id.profile;
        final int settings_id = R.id.settings;
        final int courses_id = R.id.courses;
        final int leaderbd_id = R.id.leaderboard;
        switch (item.getItemId()) {
            case courses_id:
                openFragment(CoursesFragment.newInstance("", ""));
                return true;
            case leaderbd_id:
                openFragment(LeaderboardFragment.newInstance("", ""));
                return true;
            case profile_id:
                openFragment(ProfileFragment.newInstance(username, ""));
                return true;
            case settings_id:
                openFragment(SettingsFragment.newInstance(email, username));
                return true;
        }
        return false;
    };

    private void resetPref() {
        getSharedPreferences(GetStartedPage.PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(GetStartedPage.PREF_USERNAME, null)
                .apply();
    }
}