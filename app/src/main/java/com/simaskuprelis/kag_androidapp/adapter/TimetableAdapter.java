package com.simaskuprelis.kag_androidapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.Group;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private static final int ITEM_COUNT = 8;

    private SparseArray<Group> groups;
    private List<Integer> times;
    private int day;
    private int highlight;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lesson_number)
        TextView number;
        @BindView(R.id.lesson_name)
        TextView name;
        @BindView(R.id.start_time)
        TextView startTime;
        @BindView(R.id.end_time)
        TextView endTime;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    /**
     * @param day constant from Calendar class
     */
    public TimetableAdapter(SparseArray<Group> groups, List<Integer> times, int day) {
        this.groups = groups;
        this.times = times;
        this.day = day;
        findHighlight();
    }

    @NonNull
    @Override
    public TimetableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_timetable, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableAdapter.ViewHolder holder, int position) {
        holder.number.setText(Integer.toString(position + 1));
        holder.number.setEnabled(position == highlight);
        int start = times.get(position * 2);
        int end = times.get(position * 2 + 1);
        holder.startTime.setText(formatTime(start));
        holder.endTime.setText(formatTime(end));
        final Group g = groups.get(position + 1);
        if (g != null) {
            holder.name.setText(g.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(g);
                }
            });
        } else {
            holder.name.setText(null);
            holder.itemView.setOnClickListener(null);
        }
    }

    private String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    private void findHighlight() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == day) {
            int now = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
            int pos = Collections.binarySearch(times, now);
            if (pos < 0) pos = -pos - 1;
            highlight = pos / 2;
        } else highlight = -1;
    }

    public void update() {
        findHighlight();
        notifyDataSetChanged();
    }
}
