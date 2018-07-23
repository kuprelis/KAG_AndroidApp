package com.simaskuprelis.kag_androidapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.simaskuprelis.kag_androidapp.fragment.NewsFragment;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.fragment.SettingsFragment;
import com.simaskuprelis.kag_androidapp.fragment.TimetablePagerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final int TAB_TIMETABLE = 0;
    public static final int TAB_NEWS = 1;
    public static final int TAB_SETTINGS = 2;
    public static final String EXTRA_TAB = "com.simaskuprelis.kag_androidapp.tab";
    public static final String EXTRA_RESET = "com.simaskuprelis.kag_androidapp.reset";

    private static final int[] tabId = {R.id.tab_my_schedule, R.id.tab_news, R.id.tab_settings};
    private int currId;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        currId = tabId[getIntent().getIntExtra(EXTRA_TAB, TAB_TIMETABLE)];
        putFragment(fromTabId(currId));
        bottomNav.setSelectedItemId(currId);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                currId = item.getItemId();
                putFragment(fromTabId(currId));
                return true;
            }
        });

        bottomNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.fragment_container);
                if (f == null) return;
                if (f instanceof TimetablePagerFragment) {
                    ((TimetablePagerFragment) f).reset();
                } else if (f instanceof NewsFragment) {
                    ((NewsFragment) f).reset();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int id = tabId[intent.getIntExtra(EXTRA_TAB, TAB_TIMETABLE)];
        boolean reset = intent.getBooleanExtra(EXTRA_RESET, false);
        if (!reset && id == currId) return;
        currId = id;
        bottomNav.setSelectedItemId(currId);
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (f instanceof TimetablePagerFragment) {
            TimetablePagerFragment tpf = (TimetablePagerFragment) f;
            if (tpf.goUpHistory()) return;
        }
        super.onBackPressed();
    }

    private Fragment fromTabId(int id) {
        switch (id) {
            case R.id.tab_my_schedule: return new TimetablePagerFragment();
            case R.id.tab_news: return new NewsFragment();
            case R.id.tab_settings: return new SettingsFragment();
            default: return null;
        }
    }

    private void putFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.commit();
    }
}
