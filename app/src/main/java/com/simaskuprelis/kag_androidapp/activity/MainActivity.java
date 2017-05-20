package com.simaskuprelis.kag_androidapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.fragment.NewsFragment;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.fragment.TimetablePagerFragment;
import com.simaskuprelis.kag_androidapp.receiver.ImportantNewsReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final int TAB_TIMETABLE = 0;
    public static final int TAB_NEWS = 1;
    public static final int TAB_SETTINGS = 2;
    public static final String EXTRA_TAB = "com.simaskuprelis.kag_androidapp.tab";

    private static final int REQUEST_ID = 0;


    @BindView(R.id.bottom_nav)
    BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Utils.setupDatabase();

        Intent i = new Intent(ImportantNewsReceiver.ACTION_POLL_IMPORTANT);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_NO_CREATE);
        if (pi == null) {
            pi = PendingIntent.getBroadcast(this, 0, i, 0);
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            // TODO get interval from settings
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    AlarmManager.INTERVAL_HOUR, pi);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains(TimetablePagerFragment.PREF_USER_ID)) {
            initialize();
        } else {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivityForResult(intent, REQUEST_ID);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_ID) {
            String id = data.getStringExtra(OnboardingActivity.EXTRA_USER_ID);
            if (id != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit()
                        .putString(TimetablePagerFragment.PREF_USER_ID, id)
                        .apply();
                initialize();
            }
        }
    }

    private void initialize() {
        int tab = getIntent().getIntExtra(EXTRA_TAB, TAB_TIMETABLE);
        switch (tab) {
            case TAB_TIMETABLE:
                putFragment(new TimetablePagerFragment());
                mBottomNav.setSelectedItemId(R.id.tab_my_schedule);
                break;
            case TAB_NEWS:
                putFragment(new NewsFragment());
                mBottomNav.setSelectedItemId(R.id.tab_news);
                break;
            case TAB_SETTINGS:
                break;
        }

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                switch (item.getItemId()) {
                    case R.id.tab_my_schedule:
                        f = new TimetablePagerFragment();
                        break;
                    case R.id.tab_news:
                        f = new NewsFragment();
                        break;
                    case R.id.tab_settings:
                        break;
                }
                putFragment(f);
                return f != null;
            }
        });

        mBottomNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
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

    private void putFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment old = fm.findFragmentById(R.id.fragment_container);
        if (old != null) ft.remove(old);
        if (f != null) ft.add(R.id.fragment_container, f);
        ft.commit();
    }
}
