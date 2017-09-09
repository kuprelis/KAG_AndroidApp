package com.simaskuprelis.kag_androidapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Lesson;
import com.simaskuprelis.kag_androidapp.fragment.TimetableFragment;

import java.util.List;

public class TimetablePagerAdapter extends FragmentStatePagerAdapter {

    private static final int ITEM_COUNT = 5;

    private List<Group> groups;
    private List<Integer> times;
    private Context context;

    public TimetablePagerAdapter(FragmentManager fm, List<Group> groups, List<Integer> times, Context c) {
        super(fm);
        this.groups = groups;
        this.times = times;
        context = c;
    }

    @Override
    public Fragment getItem(int position) {
        SparseArray<Group> groups = new SparseArray<>();
        for (Group g : this.groups) {
            for (Lesson l : g.getLessons()) {
                if (l.getDay() == position + 1) groups.append(l.getNum(), g);
            }
        }
        return TimetableFragment.newInstance(groups, times);
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return context.getString(R.string.mon_short);
            case 1: return context.getString(R.string.tue_short);
            case 2: return context.getString(R.string.wed_short);
            case 3: return context.getString(R.string.thu_short);
            case 4: return context.getString(R.string.fri_short);
            default: return "";
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
