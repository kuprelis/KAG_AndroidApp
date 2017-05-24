package com.simaskuprelis.kag_androidapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.api.NewsApi;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.NewsAdapter;
import com.simaskuprelis.kag_androidapp.api.NewsResponse;
import com.simaskuprelis.kag_androidapp.entity.ImportantNewsItem;
import com.simaskuprelis.kag_androidapp.entity.NewsItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";

    @BindView(R.id.news_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.important_display)
    LinearLayout mImportantDisplay;
    @BindView(R.id.important_text)
    TextView mImportantText;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    private int mPage;
    private boolean mItemsAvailable;
    private boolean mLoading;
    private List<NewsItem> mNewsItems;
    private NewsApi mNewsApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = 1;
        mItemsAvailable = true;
        mLoading = false;
        mNewsItems = new ArrayList<>();
        mNewsApi = Utils.getApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, v);

        updateImportant();

        Utils.setupRecycler(mRecyclerView, getContext(), new NewsAdapter(mNewsItems));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llm.findLastVisibleItemPosition() == mNewsItems.size() - 1) addItems();
            }
        });
        addItems();

        return v;
    }

    private void updateImportant() {
        Call<ImportantNewsItem> call = mNewsApi.getImportantNews();
        call.enqueue(new Callback<ImportantNewsItem>() {
            @Override
            public void onResponse(Call<ImportantNewsItem> call, Response<ImportantNewsItem> response) {
                ImportantNewsItem item = response.body();
                if (!item.isActive()) return;
                mImportantText.setText(Utils.parseHtml(item.getText()).toString());
                mImportantDisplay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ImportantNewsItem> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
            }
        });
    }

    private void addItems() {
        if (!mItemsAvailable || mLoading) return;

        mLoading = true;
        Call<NewsResponse> call = mNewsApi.getNews(mPage, null);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                mLoadingIndicator.setVisibility(View.GONE);
                NewsResponse data = response.body();
                mNewsItems.addAll(data.getItems());
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mItemsAvailable = data.getCurrentPage() != data.getLastPage();
                mPage++;
                mLoading = false;
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
                mLoading = false;
            }
        });
    }

    public void reset() {
        mRecyclerView.smoothScrollToPosition(0);
    }
}
