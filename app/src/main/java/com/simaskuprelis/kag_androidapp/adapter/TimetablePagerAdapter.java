package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Lesson;
import com.simaskuprelis.kag_androidapp.fragment.TimetableFragment;

import java.util.ArrayList;
import java.util.List;

public class TimetablePagerAdapter extends FragmentStatePagerAdapter {

    private static final int ITEM_COUNT = 5;

    private List<List<Group>> mGroups;

    public TimetablePagerAdapter(FragmentManager fm, List<Group> groups) {
        super(fm);
        mGroups = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; i++) mGroups.add(new ArrayList<Group>());
        for (Group g : groups) {
            for (Lesson l : g.getLessons()) {
                mGroups.get(l.getDay() - 1).add(g);
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        return TimetableFragment.newInstance(mGroups.get(position));
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "P";
            case 1: return "A";
            case 2: return "T";
            case 3: return "K";
            case 4: return "P";
            default: return "";
        }
    }
}
