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
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;
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
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetablePagerFragment extends Fragment {
    private static final int REQUEST_NODE_ID = 0;
    private static final int REQUEST_GROUP_NODE_ID = 1;

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.pager_tabs)
    TabLayout tabs;
    @BindView(R.id.loading_indicator)
    ProgressBar progressBar;

    private List<Group> groups;
    private List<Integer> times;
    private int page;
    private int callbacksReceived;

    // Pair structure: {id, name}
    private Pair<String, String> current;
    private Stack<Pair<String, String>> history;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        callbacksReceived = 0;

        FirebaseDatabaseApi.getTimes(new FirebaseListener<List<Integer>>() {
            @Override
            public void onLoad(List<Integer> list) {
                callbacksReceived++;
                times = list;
                setupAdapter();
            }

            @Override
            public void onFail(Exception e) {
            }
        });

        history = new Stack<>();
        page = getTodayIndex();

        setNode(getDefaultNode(), false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable_pager, container, false);
        ButterKnife.bind(this, v);

        tabs.setupWithViewPager(pager);
        if (pager.getAdapter() == null) setupAdapter();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fa = FirebaseAnalytics.getInstance(getContext());
        String name = getClass().getSimpleName();
        fa.setCurrentScreen(getActivity(), name, name);
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
                return true;

            case R.id.menu_default_node:
                setNode(getDefaultNode(), true);
                return true;

            case R.id.menu_back:
                if (!history.empty()) setNode(history.pop(), false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_NODE_ID) {
            Node n = data.getParcelableExtra(NodePickActivity.RESULT_NODE);
            setNode(new Pair<>(n.getId(), n.getName()), true);

        } else if (requestCode == REQUEST_GROUP_NODE_ID) {
            Node n = data.getParcelableExtra(GroupActivity.RESULT_GROUP_NODE);
            setNode(new Pair<>(n.getId(), n.getName()), true);
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

    private void setNode(Pair<String, String> node, boolean saveCurrent) {
        if (current != null && current.equals(node)) return;

        if (pager != null) {
            page = pager.getCurrentItem();
            pager.setAdapter(null);
        }
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        if (saveCurrent && current != null) history.add(current);
        current = node;

        getActivity().setTitle(node.second);

        FirebaseDatabaseApi.getNodeGroups(node.first, new FirebaseListener<List<Group>>() {
            @Override
            public void onLoad(List<Group> list) {
                callbacksReceived++;
                groups = list;
                setupAdapter();
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    private void setupAdapter() {
        if (getActivity() == null || pager == null || callbacksReceived < 2) return;

        progressBar.setVisibility(View.GONE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        pager.setAdapter(new TimetablePagerAdapter(fm, groups, times, getContext()));
        pager.setCurrentItem(page, false);
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

    private Pair<String, String> getDefaultNode() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String id = sp.getString(getString(R.string.pref_user_id), null);
        String name = sp.getString(getString(R.string.pref_user_name), null);
        return new Pair<>(id, name);
    }

    public void reset() {
        page = getTodayIndex();
        pager.setCurrentItem(page);
    }
}
