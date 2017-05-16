package com.simaskuprelis.kag_androidapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.TimetableAdapter;
import com.simaskuprelis.kag_androidapp.entity.Group;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TimetableFragment extends Fragment {

    private static final String KEY_DAY = "day";
    private static final String KEY_TIMES = "times";
    private static final String KEY_GROUPS = "groups";

    @BindView(R.id.timetable)
    RecyclerView mTimetable;

    private int mDay;
    private List<Group> mGroups;
    private List<Integer> mTimes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mDay = args.getInt(KEY_DAY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, v);

        mTimetable.setLayoutManager(new LinearLayoutManager(getContext()));
        mTimetable.setAdapter(new TimetableAdapter());

        return v;
    }

    public static TimetableFragment newInstance(int day, List<Group> groups, List<Integer> times) {
        Bundle args = new Bundle();
        args.putInt(KEY_DAY, day);
        TimetableFragment fragment = new TimetableFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
