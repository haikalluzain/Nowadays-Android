package com.example.haikalfluzain.nowadays.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.adapter.TodayAdapter;
import com.example.haikalfluzain.nowadays.base.BaseActivity;
import com.example.haikalfluzain.nowadays.fragment.AttendanceFragment;
import com.example.haikalfluzain.nowadays.fragment.EventFragment;
import com.example.haikalfluzain.nowadays.fragment.TodayFragment;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.example.haikalfluzain.nowadays.model.Today;
import com.example.haikalfluzain.nowadays.presenter.TodayPresenter;
import com.example.haikalfluzain.nowadays.view.TodayView;

import java.util.List;

public class Main extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefManager = new SharedPrefManager(this);

        if (!sharedPrefManager.getSpLogin()){
            startActivity(new Intent(Main.this, Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TodayFragment()).commit();
        MenuItem menuItem = bottomNav.getMenu().getItem(1);
        menuItem.setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_event:
                            selectedFragment = new EventFragment();
                            break;
                        case R.id.nav_today:
                            selectedFragment = new TodayFragment();
                            break;
                        case R.id.nav_attendance:
                            selectedFragment = new AttendanceFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
