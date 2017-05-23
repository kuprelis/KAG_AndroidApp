package com.simaskuprelis.kag_androidapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.fragment.TimetableFragment;

import java.util.List;

public class TimetablePagerAdapter extends FragmentStatePagerAdapter {

    private static final int ITEM_COUNT = 5;

    private List<Group> mGroups;
    private Context mContext;

    public TimetablePagerAdapter(FragmentManager fm, List<Group> groups, Context c) {
        super(fm);
        mGroups = groups;
        mContext = c;
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
            case 0: return mContext.getString(R.string.mon_short);
            case 1: return mContext.getString(R.string.tue_short);
            case 2: return mContext.getString(R.string.wed_short);
            case 3: return mContext.getString(R.string.thu_short);
            case 4: return mContext.getString(R.string.fri_short);
            default: return "";
        }
    }
}
