package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Lesson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private static final int ITEM_COUNT = 8;

    private List<Group> mGroups;
    private List<Integer> mTimes;
    private int mDay;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lesson_number)
        TextView mNumber;
        @BindView(R.id.lesson_name)
        TextView mName;
        @BindView(R.id.start_time)
        TextView mTime;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public TimetableAdapter() {
    }

    public TimetableAdapter(List<Group> groups, List<Integer> times, int day) {
        mGroups = groups;
        mTimes = times;
        mDay = day;
    }

    @Override
    public TimetableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_timetable, parent, false));
    }

    @Override
    public void onBindViewHolder(TimetableAdapter.ViewHolder holder, int position) {
        holder.mNumber.setText(Integer.toString(position + 1));
        int startTime = mTimes.get(position * 2);
        holder.mTime.setText(String.format("%d:%d", startTime / 60, startTime % 60));
        for (Group g : mGroups) {
            for (Lesson l : g.getLessons()) {
                if (l.getDay() == mDay && l.getNumber() == position + 1) {
                    holder.mName.setText(g.getName());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
