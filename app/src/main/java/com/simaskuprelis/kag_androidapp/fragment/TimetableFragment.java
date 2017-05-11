package com.simaskuprelis.kag_androidapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simaskuprelis.kag_androidapp.R;

import butterknife.ButterKnife;


public class TimetableFragment extends Fragment {

    private static final String KEY_DAY = "day";

    private int mDay;

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

        return v;
    }

    public static TimetableFragment newInstance(int day) {
        Bundle args = new Bundle();
        args.putInt(KEY_DAY, day);
        TimetableFragment fragment = new TimetableFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
