package com.simaskuprelis.kag_androidapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
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
    private static final String KEY_DAY = "day";

    @BindView(R.id.timetable)
    RecyclerView mTimetable;

    private SparseArray<Group> mGroups;
    private List<Integer> mTimes;
    private int mDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        List<Group> groups = args.getParcelableArrayList(KEY_GROUPS);
        mDay = args.getInt(KEY_DAY);

        mGroups = new SparseArray<>();
        for (Group g : groups) {
            for (Lesson l : g.getLessons()) {
                if (l.getDay() == mDay) mGroups.append(l.getNumber(), g);
            }
        }

        FirebaseDatabaseApi.getTimes(new TimesListener() {
            @Override
            public void onLoad(List<Integer> times) {
                mTimes = times;
                if (mTimetable != null) {
                    Utils.setupRecycler(mTimetable, getContext(),
                            new TimetableAdapter(mGroups, mTimes, getContext()));
                }
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, v);

        if (mTimes != null && mTimetable.getAdapter() == null) {
            Utils.setupRecycler(mTimetable, getContext(),
                    new TimetableAdapter(mGroups, mTimes, getContext()));
        }

        return v;
    }

    public static TimetableFragment newInstance(List<Group> groups, int day) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_GROUPS, new ArrayList<>(groups));
        args.putInt(KEY_DAY, day);
        TimetableFragment fragment = new TimetableFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
