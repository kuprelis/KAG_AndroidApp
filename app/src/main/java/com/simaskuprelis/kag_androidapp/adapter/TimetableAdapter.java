package com.simaskuprelis.kag_androidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.activity.GroupActivity;
import com.simaskuprelis.kag_androidapp.entity.Group;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private static final int ITEM_COUNT = 8;

    private SparseArray<Group> mGroups;
    private List<Integer> mTimes;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lesson_number)
        TextView mNumber;
        @BindView(R.id.lesson_name)
        TextView mName;
        @BindView(R.id.start_time)
        TextView mStartTime;
        @BindView(R.id.end_time)
        TextView mEndTime;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public TimetableAdapter(SparseArray<Group> groups, List<Integer> times, Context c) {
        mGroups = groups;
        mTimes = times;
        mContext = c;
    }

    @Override
    public TimetableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_timetable, parent, false));
    }

    @Override
    public void onBindViewHolder(TimetableAdapter.ViewHolder holder, int position) {
        holder.mNumber.setText(Integer.toString(position + 1));
        int start = mTimes.get(position * 2);
        int end = mTimes.get(position * 2 + 1);
        holder.mStartTime.setText(formatTime(start));
        holder.mEndTime.setText(formatTime(end));
        final Group g = mGroups.get(position + 1);
        if (g != null) holder.mName.setText(g.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, GroupActivity.class);
                i.putExtra(GroupActivity.EXTRA_GROUP, g);
                mContext.startActivity(i);
            }
        });
    }

    private String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
