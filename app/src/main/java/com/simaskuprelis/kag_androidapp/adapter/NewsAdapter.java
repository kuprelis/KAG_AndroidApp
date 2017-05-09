package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.entity.NewsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<NewsItem> mNewsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text)
        TextView mTitle;
        @BindView(R.id.body_text)
        TextView mBody;
        @BindView(R.id.date_created)
        TextView mDate;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public NewsAdapter(List<NewsItem> news) {
        mNewsList = news;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        NewsItem item = mNewsList.get(position);
        holder.mTitle.setText(Utils.parseHtml(item.getTitle()));
        holder.mDate.setText(item.getCreated());
        holder.mBody.setText(Utils.parseHtml(item.getText()));
        holder.mBody.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
