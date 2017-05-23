package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.fragment.TimetableFragment;

import java.util.List;

public class TimetablePagerAdapter extends FragmentStatePagerAdapter {

    private static final int ITEM_COUNT = 5;

    private List<Group> mGroups;

    public TimetablePagerAdapter(FragmentManager fm, List<Group> groups) {
        super(fm);
        mGroups = groups;
    }

    @Override
    public Fragment getItem(int position) {
        return TimetableFragment.newInstance(mGroups, position + 1);
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
