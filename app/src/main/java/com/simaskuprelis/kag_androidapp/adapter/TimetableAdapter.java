package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.Group;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private static final int ITEM_COUNT = 8;

    private SparseArray<Group> mGroups;
    private List<Integer> mTimes;

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

    public TimetableAdapter(SparseArray<Group> groups, List<Integer> times) {
        mGroups = groups;
        mTimes = times;
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
        holder.mTime.setText(String.format("%02d:%02d", startTime / 60, startTime % 60));
        Group g = mGroups.get(position + 1);
        if (g != null) holder.mName.setText(g.getName());
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
