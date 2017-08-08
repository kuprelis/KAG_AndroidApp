package com.simaskuprelis.kag_androidapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.firebase.crash.FirebaseCrash;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.activity.ArticleActivity;
import com.simaskuprelis.kag_androidapp.api.NewsApi;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.NewsAdapter;
import com.simaskuprelis.kag_androidapp.api.NewsResponse;
import com.simaskuprelis.kag_androidapp.entity.ImportantNewsItem;
import com.simaskuprelis.kag_androidapp.entity.NewsItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    private int mPage;
    private boolean mItemsAvailable;
    private boolean mLoading;
    private List<NewsItem> mNewsItems;
    private ImportantNewsItem mImportant;
    private NewsApi mNewsApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = 1;
        mItemsAvailable = true;
        mLoading = false;
        mNewsApi = Utils.getApi();

        Call<ImportantNewsItem> importantCall = mNewsApi.getImportantNews();
        importantCall.enqueue(new Callback<ImportantNewsItem>() {
            @Override
            public void onResponse(Call<ImportantNewsItem> call, Response<ImportantNewsItem> response) {
                mImportant = response.body();
                setupAdapter();
            }

            @Override
            public void onFailure(Call<ImportantNewsItem> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
            }
        });

        Call<NewsResponse> newsCall = mNewsApi.getNews(mPage, null);
        newsCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                NewsResponse data = response.body();
                mNewsItems = data.getItems();
                mItemsAvailable = data.getCurrentPage() != data.getLastPage();
                mPage++;
                setupAdapter();
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, v);

        setupAdapter();
        
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setupAdapter() {
        if (mRecyclerView == null || mRecyclerView.getAdapter() != null) return;
        if (mImportant == null || mNewsItems == null) return;

        mLoadingIndicator.setVisibility(View.GONE);
        NewsAdapter adapter = new NewsAdapter(mNewsItems, mImportant, Glide.with(this));
        Utils.setupRecycler(mRecyclerView, getContext(), adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llm.findLastVisibleItemPosition() == mNewsItems.size() - 1) addItems();
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
                NewsResponse data = response.body();
                mNewsItems.addAll(data.getItems());
                mItemsAvailable = data.getCurrentPage() != data.getLastPage();
                mPage++;
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mLoading = false;
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
                mLoading = false;
            }
        });
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onNewsSelect(NewsItem ni) {
        Intent i = new Intent(getContext(), ArticleActivity.class);
        i.putExtra(ArticleActivity.EXTRA_ARTICLE, ni);
        startActivity(i);
    }

    public void reset() {
        mRecyclerView.smoothScrollToPosition(0);
    }
}
