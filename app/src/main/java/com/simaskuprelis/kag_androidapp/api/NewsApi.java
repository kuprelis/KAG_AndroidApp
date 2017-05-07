package com.simaskuprelis.kag_androidapp.api;


import com.simaskuprelis.kag_androidapp.entity.NewsItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApi {
    @GET("news")
    Call<List<NewsItem>> getNews();
}
