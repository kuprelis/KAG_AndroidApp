package com.simaskuprelis.kag_androidapp.activity;

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
import com.simaskuprelis.kag_androidapp.fragment.TimetablePagerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bottom_nav)
    BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
                // TODO remove test code
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment old = fm.findFragmentById(R.id.fragment_container);
                if (old != null) ft.remove(old);
                if (f != null) ft.add(R.id.fragment_container, f);
                ft.commit();

                return true;
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
}
