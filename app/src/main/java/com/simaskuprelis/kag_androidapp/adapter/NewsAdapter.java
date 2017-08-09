package com.simaskuprelis.kag_androidapp.adapter;

import android.support.v7.widget.RecyclerView;
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
    private List<NewsListItem> mItems;
    private RequestManager mRequestManager;

    static class ViewHolder0 extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView mText;

        ViewHolder0(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text)
        TextView mTitle;
        @BindView(R.id.body_text)
        TextView mBody;
        @BindView(R.id.date_created)
        TextView mDate;
        @BindView(R.id.image)
        ImageView mImage;

        ViewHolder1(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public NewsAdapter(List<NewsListItem> items, RequestManager rm) {
        mItems = items;
        mRequestManager = rm;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());

        if (viewType == NewsListItem.TYPE_IMPORTANT)
            return new ViewHolder0(li.inflate(R.layout.list_item_news_important, parent, false));

        if (viewType == NewsListItem.TYPE_REGULAR)
            return new ViewHolder1(li.inflate(R.layout.list_item_news, parent, false));

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();
        if (type == NewsListItem.TYPE_IMPORTANT) {
            ViewHolder0 vh = (ViewHolder0) holder;
            ImportantNewsItem item = (ImportantNewsItem) mItems.get(position);

            CharSequence text = Utils.parseHtml(item.getText());
            vh.mText.setText(text);
        } else if (type == NewsListItem.TYPE_REGULAR) {
            ViewHolder1 vh = (ViewHolder1) holder;
            final NewsItem item = (NewsItem) mItems.get(position);

            String url = item.getPhotoUrl();
            vh.mImage.setVisibility(url.isEmpty() ? View.GONE : View.VISIBLE);
            if (!url.isEmpty()) {
                mRequestManager.load(url)
                        .apply(new RequestOptions().centerCrop())
                        .into(vh.mImage);
            } else {
                mRequestManager.clear(vh.mImage);
            }

            vh.mTitle.setText(item.getTitle());
            vh.mDate.setText(item.getCreated());
            vh.mBody.setText(Utils.parseHtml(item.getText()).toString());

            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(item);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
