package com.simaskuprelis.kag_androidapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.simaskuprelis.kag_androidapp.api.listener.GroupsListener;
import com.simaskuprelis.kag_androidapp.api.listener.NodesListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetablePagerFragment extends Fragment {

    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.pager_tabs)
    TabLayout mTabs;
    @BindView(R.id.edit_fab)
    FloatingActionButton mEditFab;

    private List<Group> mGroups;
    private String mUserId = "18a_kups"; // TODO get from prefs

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabaseApi.getNodes(Collections.singletonList(mUserId), new NodesListener() {
            @Override
            public void onLoad(List<Node> nodes) {
                Node n = nodes.get(0);
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
        if (mGroups == null || mPager == null || mPager.getAdapter() != null) return;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mPager.setAdapter(new TimetablePagerAdapter(fm, mGroups));
        mTabs.setupWithViewPager(mPager);
    }
}
