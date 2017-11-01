package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.entity.ImportantNewsItem;
import com.simaskuprelis.kag_androidapp.entity.NewsItem;
import com.simaskuprelis.kag_androidapp.entity.NewsListItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsListItem> items;
    private RequestManager requestManager;

    static class ImportantHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView info;

        ImportantHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            info.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    static class NewsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text)
        TextView title;
        @BindView(R.id.body_text)
        TextView body;
        @BindView(R.id.date_created)
        TextView date;
        @BindView(R.id.image)
        ImageView image;

        NewsHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public NewsAdapter(List<NewsListItem> items, RequestManager rm) {
        this.items = items;
        requestManager = rm;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());

        if (viewType == NewsListItem.TYPE_IMPORTANT)
            return new ImportantHolder(li.inflate(R.layout.list_item_news_important, parent, false));

        if (viewType == NewsListItem.TYPE_REGULAR)
            return new NewsHolder(li.inflate(R.layout.list_item_news, parent, false));

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();
        if (type == NewsListItem.TYPE_IMPORTANT) {
            ImportantHolder ih = (ImportantHolder) holder;
            ImportantNewsItem item = (ImportantNewsItem) items.get(position);

            ih.info.setText(Utils.parseHtml(item.getText(), true));
        } else if (type == NewsListItem.TYPE_REGULAR) {
            NewsHolder nh = (NewsHolder) holder;
            final NewsItem item = (NewsItem) items.get(position);

            String url = item.getPhotoUrl();
            nh.image.setVisibility(url.isEmpty() ? View.GONE : View.VISIBLE);
            if (!url.isEmpty()) {
                requestManager.load(url)
                        .apply(new RequestOptions().centerCrop())
                        .into(nh.image);
            } else {
                requestManager.clear(nh.image);
            }

            nh.title.setText(item.getTitle());
            nh.date.setText(item.getCreated());
            nh.body.setText(Utils.parseHtml(item.getText(), false).toString());

            nh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(item);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
