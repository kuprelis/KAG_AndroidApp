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

import com.simaskuprelis.kag_androidapp.api.NewsApi;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.NewsAdapter;
import com.simaskuprelis.kag_androidapp.api.NewsResponse;
import com.simaskuprelis.kag_androidapp.entity.NewsItem;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class NewsFragment extends Fragment {
    private static final String BASE_URL = "http://www.azuolynogimnazija.lt/json/";
    private static final String TAG = "NewsFragment";

    @BindView(R.id.news_list)
    RecyclerView mRecyclerView;

    private int mPage;
    private boolean mItemsAvailable;
    private boolean mLoading;
    private List<NewsItem> mNewsItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = 1;
        mItemsAvailable = true;
        mLoading = false;
        mNewsItems = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, v);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new NewsAdapter(mNewsItems));
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

    private void addItems() {
        if (!mItemsAvailable || mLoading) return;

        Moshi moshi = new Moshi.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
        NewsApi api = retrofit.create(NewsApi.class);
        Call<NewsResponse> call = api.getNews(mPage, null);
        mLoading = true;
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                NewsResponse data = response.body();
                Log.d(TAG, String.format("first: %d, last: %d", data.getFirst(), data.getLast()));
                mNewsItems.addAll(data.getItems());
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mItemsAvailable = data.getCurrentPage() != data.getLastPage();
                mPage++;
                mLoading = false;
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                mLoading = false;
            }
        });
    }

    public void reset() {
        mRecyclerView.scrollToPosition(0);
    }
}
