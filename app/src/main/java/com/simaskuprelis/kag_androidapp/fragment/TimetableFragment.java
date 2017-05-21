package com.simaskuprelis.kag_androidapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.TimetableAdapter;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.listener.TimesListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Lesson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TimetableFragment extends Fragment {

    private static final String KEY_GROUPS = "groups";

    @BindView(R.id.timetable)
    RecyclerView mTimetable;

    private SparseArray<Group> mGroups;
    private List<Integer> mTimes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabaseApi.getTimes(new TimesListener() {
            @Override
            public void onLoad(List<Integer> times) {
                mTimes = times;
                setupAdapter();
            }

            @Override
            public void onFail(Exception e) {
            }
        });

        Bundle args = getArguments();
        List<Group> groups = args.getParcelableArrayList(KEY_GROUPS);
        mGroups = new SparseArray<>();
        for (Group g : groups) {
            for (Lesson l : g.getLessons()) {
                mGroups.append(l.getNumber(), g);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, v);

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (mTimes == null || mTimetable == null || mTimetable.getAdapter() != null) return;
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mTimetable.setLayoutManager(llm);
        DividerItemDecoration did = new DividerItemDecoration(getContext(), llm.getOrientation());
        mTimetable.addItemDecoration(did);
        mTimetable.setAdapter(new TimetableAdapter(mGroups, mTimes, getContext()));
    }

    public static TimetableFragment newInstance(List<Group> groups) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_GROUPS, new ArrayList<>(groups));
        TimetableFragment fragment = new TimetableFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
