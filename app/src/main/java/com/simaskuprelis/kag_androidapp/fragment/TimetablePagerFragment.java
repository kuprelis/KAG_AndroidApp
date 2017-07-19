package com.simaskuprelis.kag_androidapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.activity.NodePickActivity;
import com.simaskuprelis.kag_androidapp.adapter.TimetablePagerAdapter;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.listener.GroupsListener;
import com.simaskuprelis.kag_androidapp.api.listener.NodesListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetablePagerFragment extends Fragment {
    private static final int REQUEST_USER_ID = 0;

    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.pager_tabs)
    TabLayout mTabs;
    @BindView(R.id.edit_fab)
    FloatingActionButton mEditFab;

    private List<Group> mGroups;
    private String mUserId;
    private int mTodayPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        mUserId = sp.getString(getString(R.string.pref_user_id), null);
        loadData();

        Calendar cal = Calendar.getInstance();
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.TUESDAY: mTodayPage = 1; break;
            case Calendar.WEDNESDAY: mTodayPage = 2; break;
            case Calendar.THURSDAY: mTodayPage = 3; break;
            case Calendar.FRIDAY: mTodayPage = 4; break;
            default: mTodayPage = 0; break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable_pager, container, false);
        ButterKnife.bind(this, v);

        if (mPager.getAdapter() == null) setupAdapter();
        mEditFab.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_timetable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_pick_node) {
            Intent i = new Intent(getContext(), NodePickActivity.class);
            startActivityForResult(i, REQUEST_USER_ID);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_USER_ID) {
            mUserId = data.getStringExtra(NodePickActivity.EXTRA_USER_ID);
            loadData();
        }
    }

    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.app_name);
        super.onDetach();
    }

    private void loadData() {
        FirebaseDatabaseApi.getNodes(Collections.singletonList(mUserId), new NodesListener() {
            @Override
            public void onLoad(List<Node> nodes) {
                Node n = nodes.get(0);
                Activity activity = getActivity();
                if (activity != null) activity.setTitle(n.getName());
                FirebaseDatabaseApi.getGroups(n.getGroups(), new GroupsListener() {
                    @Override
                    public void onLoad(List<Group> groups) {
                        mGroups = groups;
                        setupAdapter();
                    }

                    @Override
                    public void onFail(Exception e) {
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    private void setupAdapter() {
        if (getActivity() == null || mGroups == null || mPager == null) return;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mPager.setAdapter(new TimetablePagerAdapter(fm, mGroups, getContext()));
        mTabs.setupWithViewPager(mPager);
        mPager.setCurrentItem(mTodayPage, false);
    }

    public void reset() {
        mPager.setCurrentItem(mTodayPage);
    }
}
