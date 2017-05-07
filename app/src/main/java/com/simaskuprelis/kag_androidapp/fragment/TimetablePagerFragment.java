package com.simaskuprelis.kag_androidapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimetablePagerFragment extends Fragment {
    private static final String KEY_DAY = "day";

    private int mDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDay = getArguments().getInt(KEY_DAY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static TimetablePagerFragment newInstance(int day) {
        Bundle args = new Bundle();
        args.putInt(KEY_DAY, day);
        TimetablePagerFragment fragment = new TimetablePagerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
