package com.simaskuprelis.kag_androidapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.Lesson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private List<Lesson> mLessons;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.node_name)
        TextView mName;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public LessonAdapter(List<Lesson> lessons, Context c) {
        mLessons = lessons;
        mContext = c;
    }

    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_node, parent, false));
    }

    @Override
    public void onBindViewHolder(LessonAdapter.ViewHolder holder, int position) {
        Lesson l = mLessons.get(position);
        String day;
        switch (l.getDay()) {
            case 1: day = mContext.getString(R.string.monday); break;
            case 2: day = mContext.getString(R.string.tuesday); break;
            case 3: day = mContext.getString(R.string.wednesday); break;
            case 4: day = mContext.getString(R.string.thursday); break;
            case 5: day = mContext.getString(R.string.friday); break;
            default: day = ""; break;
        }
        StringBuilder sb = new StringBuilder()
                .append(day)
                .append(", ")
                .append(l.getNumber())
                .append(" pamoka");
        // TODO get normal names someday
        if (l.getRoom() != null) sb.append(", ").append(l.getRoom());
        holder.mName.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return mLessons.size();
    }
}
