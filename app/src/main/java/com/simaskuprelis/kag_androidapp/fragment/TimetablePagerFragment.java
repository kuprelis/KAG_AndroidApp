package com.simaskuprelis.kag_androidapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.TimetablePagerAdapter;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.listener.SingleGroupListener;
import com.simaskuprelis.kag_androidapp.api.listener.SingleNodeListener;
import com.simaskuprelis.kag_androidapp.api.listener.TimesListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimetablePagerFragment extends Fragment {

    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.pager_tabs)
    TabLayout mTabs;
    @BindView(R.id.edit_fab)
    FloatingActionButton mEditFab;

    private List<Integer> mTimes;
    private List<Group> mGroups;
    private boolean mTimesLoaded, mGroupsLoaded;
    private String mUserId = "18a_kups"; // TODO get from prefs

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTimesLoaded = false;
        mGroupsLoaded = false;
        FirebaseDatabaseApi.getTimes(new TimesListener() {
            @Override
            public void onLoad(List<Integer> times) {
                mTimes = times;
                mTimesLoaded = true;
                setupAdapter();
            }

            @Override
            public void onFail(Exception e) {
            }
        });
        FirebaseDatabaseApi.getNode(new SingleNodeListener() {
            @Override
            public void onLoad(Node node) {
                mGroups = new ArrayList<>();
                final List<String> ids = node.getGroups();
                for (String id : ids) {
                    FirebaseDatabaseApi.getGroup(new SingleGroupListener() {
                        @Override
                        public void onLoad(Group group) {
                            mGroups.add(group);
                            if (mGroups.size() == ids.size()) {
                                mGroupsLoaded = true;
                                setupAdapter();
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                        }
                    }, id);
                }
            }

            @Override
            public void onFail(Exception e) {
            }
        }, mUserId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable_pager, container, false);
        ButterKnife.bind(this, v);

        setupAdapter();
        mEditFab.setVisibility(View.GONE);

        return v;
    }

    private void setupAdapter() {
        if (mPager == null || mPager.getAdapter() != null) return;
        if (!mTimesLoaded || !mGroupsLoaded) return;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mPager.setAdapter(new TimetablePagerAdapter(fm, mTimes, mGroups));
        mTabs.setupWithViewPager(mPager);
    }
}
