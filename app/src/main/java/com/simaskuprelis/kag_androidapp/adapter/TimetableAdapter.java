package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.Group;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private static final int ITEM_COUNT = 8;

    private SparseArray<Group> groups;
    private List<Integer> times;

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

    public TimetableAdapter(SparseArray<Group> groups, List<Integer> times) {
        this.groups = groups;
        this.times = times;
    }

    @Override
    public TimetableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_timetable, parent, false));
    }

    @Override
    public void onBindViewHolder(TimetableAdapter.ViewHolder holder, int position) {
        holder.number.setText(Integer.toString(position + 1));
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
        }
    }

    private String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
