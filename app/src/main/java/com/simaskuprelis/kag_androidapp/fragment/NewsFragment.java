package com.simaskuprelis.kag_androidapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.activity.ArticleActivity;
import com.simaskuprelis.kag_androidapp.api.NewsApi;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.NewsAdapter;
import com.simaskuprelis.kag_androidapp.entity.NewsResponse;
import com.simaskuprelis.kag_androidapp.entity.ImportantNewsItem;
import com.simaskuprelis.kag_androidapp.entity.NewsItem;
import com.simaskuprelis.kag_androidapp.entity.NewsListItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    RecyclerView recycler;
    @BindView(R.id.loading_indicator)
    ProgressBar progressBar;

    private int callbacksReceived;
    private int page;
    private boolean itemsAvailable;
    private boolean loading;
    private List<NewsListItem> items;
    private NewsApi newsApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page = 1;
        itemsAvailable = true;
        loading = false;
        newsApi = Utils.getApi();
        items = new ArrayList<>();
        callbacksReceived = 0;

        Call<ImportantNewsItem> importantCall = newsApi.getImportantNews();
        importantCall.enqueue(new Callback<ImportantNewsItem>() {
            @Override
            public void onResponse(@NonNull Call<ImportantNewsItem> call, @NonNull Response<ImportantNewsItem> response) {
                callbacksReceived++;
                ImportantNewsItem item = response.body();
                if (item.isActive()) items.add(0, response.body());
                setupAdapter();
            }

            @Override
            public void onFailure(@NonNull Call<ImportantNewsItem> call, @NonNull Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
            }
        });

        Call<NewsResponse> newsCall = newsApi.getNews(page, null);
        newsCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                callbacksReceived++;
                NewsResponse data = response.body();
                for (NewsItem item : data.getItems()) {
                    if (item.isVisible()) items.add(item);
                }
                itemsAvailable = data.getCurrentPage() != data.getLastPage();
                page++;
                setupAdapter();
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
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
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fa = FirebaseAnalytics.getInstance(getContext());
        fa.setCurrentScreen(getActivity(), getClass().getSimpleName(), null);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setupAdapter() {
        if (recycler == null || recycler.getAdapter() != null || getActivity() == null ||
                callbacksReceived != 2) return;

        progressBar.setVisibility(View.GONE);

        NewsAdapter adapter = new NewsAdapter(items, Glide.with(this));
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        DividerItemDecoration did = new DividerItemDecoration(getContext(), llm.getOrientation());
        recycler.setLayoutManager(llm);
        recycler.addItemDecoration(did);
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llm.findLastVisibleItemPosition() == items.size() - 1) addItems();
            }
        });
    }

    private void addItems() {
        if (!itemsAvailable || loading) return;
        loading = true;

        Call<NewsResponse> call = newsApi.getNews(page, null);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                NewsResponse data = response.body();
                for (NewsItem item : data.getItems()) {
                    if (item.isVisible()) items.add(item);
                }
                itemsAvailable = data.getCurrentPage() != data.getLastPage();
                page++;
                recycler.getAdapter().notifyDataSetChanged();
                loading = false;
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
                loading = false;
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
        recycler.smoothScrollToPosition(0);
    }
}
