package com.simaskuprelis.kag_androidapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.TimetableAdapter;
import com.simaskuprelis.kag_androidapp.decoration.TimetableDecoration;
import com.simaskuprelis.kag_androidapp.entity.Group;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TimetableFragment extends Fragment {

    private static final String KEY_GROUPS = "groups";
    private static final String KEY_TIMES = "times";

    @BindView(R.id.timetable)
    RecyclerView recycler;

    private SparseArray<Group> groups;
    private List<Integer> times;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        groups = args.getSparseParcelableArray(KEY_GROUPS);
        times = args.getIntegerArrayList(KEY_TIMES);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, v);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(llm);
        recycler.addItemDecoration(new TimetableDecoration(getContext()));
        recycler.setAdapter(new TimetableAdapter(groups, times));

        return v;
    }

    public static TimetableFragment newInstance(SparseArray<Group> groups, List<Integer> times) {
        Bundle args = new Bundle();
        args.putSparseParcelableArray(KEY_GROUPS, groups);
        args.putIntegerArrayList(KEY_TIMES, new ArrayList<>(times));
        TimetableFragment fragment = new TimetableFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
