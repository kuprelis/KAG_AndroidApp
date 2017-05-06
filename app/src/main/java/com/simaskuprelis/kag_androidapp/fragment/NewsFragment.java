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

import com.simaskuprelis.kag_androidapp.NewsApi;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.NewsAdapter;
import com.simaskuprelis.kag_androidapp.object.NewsItem;
import com.squareup.moshi.Moshi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class NewsFragment extends Fragment {
    // TODO get from firebase
    private static final String BASE_URL = "http://www.azuolynogimnazija.lt/json/";
    private static final String TAG = "NewsFragment";

    @BindView(R.id.news_list)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, v);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Moshi moshi = new Moshi.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
        NewsApi api = retrofit.create(NewsApi.class);
        Call<List<NewsItem>> call = api.getNews();
        call.enqueue(new Callback<List<NewsItem>>() {
            @Override
            public void onResponse(Call<List<NewsItem>> call, Response<List<NewsItem>> response) {
                mRecyclerView.setAdapter(new NewsAdapter(response.body()));
            }

            @Override
            public void onFailure(Call<List<NewsItem>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        return v;
    }

    public void reset() {
        mRecyclerView.scrollToPosition(0);
    }
}
