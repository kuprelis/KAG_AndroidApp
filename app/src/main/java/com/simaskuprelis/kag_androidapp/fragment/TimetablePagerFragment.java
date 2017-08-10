package com.simaskuprelis.kag_androidapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.activity.GroupActivity;
import com.simaskuprelis.kag_androidapp.activity.NodePickActivity;
import com.simaskuprelis.kag_androidapp.adapter.TimetablePagerAdapter;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.FirebaseListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetablePagerFragment extends Fragment {
    private static final int REQUEST_NODE_ID = 0;
    private static final int REQUEST_GROUP_NODE_ID = 1;

    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.pager_tabs)
    TabLayout mTabs;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoading;

    private List<Group> mGroups;
    private List<Integer> mTimes;
    private String mDefaultId;
    private String mDefaultName;
    private int mPage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        FirebaseDatabaseApi.getTimes(new FirebaseListener<List<Integer>>() {
            @Override
            public void onLoad(List<Integer> list) {
                mTimes = list;
                setupAdapter();
            }

            @Override
            public void onFail(Exception e) {
            }
        });

        mPage = getTodayIndex();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        mDefaultId = sp.getString(getString(R.string.pref_user_id), null);
        mDefaultName = sp.getString(getString(R.string.pref_user_name), null);
        loadData(mDefaultId, mDefaultName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable_pager, container, false);
        ButterKnife.bind(this, v);

        mTabs.setupWithViewPager(mPager);
        if (mPager.getAdapter() == null) setupAdapter();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_timetable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pick_node:
                Intent i = new Intent(getContext(), NodePickActivity.class);
                startActivityForResult(i, REQUEST_NODE_ID);
                break;

            case R.id.menu_default_node:
                loadData(mDefaultId, mDefaultName);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_NODE_ID) {
            Node n = data.getParcelableExtra(NodePickActivity.RESULT_NODE);
            loadData(n.getId(), n.getName());
        } else if (requestCode == REQUEST_GROUP_NODE_ID) {
            Node n = data.getParcelableExtra(GroupActivity.RESULT_GROUP_NODE);
            loadData(n.getId(), n.getName());
        }
    }

    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.app_name);
        super.onDetach();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onGroupSelect(Group g) {
        Intent i = new Intent(getContext(), GroupActivity.class);
        i.putExtra(GroupActivity.EXTRA_GROUP, g);
        startActivityForResult(i, REQUEST_GROUP_NODE_ID);
    }

    private void loadData(String id, String name) {
        if (mPager != null) {
            mPage = mPager.getCurrentItem();
            mPager.setAdapter(null);
            mLoading.setVisibility(View.VISIBLE);
        }

        getActivity().setTitle(name);

        FirebaseDatabaseApi.getNodeGroups(id, new FirebaseListener<List<Group>>() {
            @Override
            public void onLoad(List<Group> list) {
                mGroups = list;
                setupAdapter();
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    private void setupAdapter() {
        if (getActivity() == null || mGroups == null || mTimes == null || mPager == null) return;

        mLoading.setVisibility(View.GONE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mPager.setAdapter(new TimetablePagerAdapter(fm, mGroups, mTimes, getContext()));
        mPager.setCurrentItem(mPage, false);
    }

    private int getTodayIndex() {
        Calendar cal = Calendar.getInstance();
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.TUESDAY: return 1;
            case Calendar.WEDNESDAY: return 2;
            case Calendar.THURSDAY: return 3;
            case Calendar.FRIDAY: return 4;
            default: return 0;
        }
    }

    public void reset() {
        mPage = getTodayIndex();
        mPager.setCurrentItem(mPage);
    }
}
