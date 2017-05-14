package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.simaskuprelis.kag_androidapp.fragment.TimetableFragment;

public class TimetablePagerAdapter extends FragmentPagerAdapter {

    private static final int ITEM_COUNT = 5;

    public TimetablePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TimetableFragment.newInstance(position + 1);
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
