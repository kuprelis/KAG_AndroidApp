package com.simaskuprelis.kag_androidapp;


import com.simaskuprelis.kag_androidapp.object.NewsItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApi {
    @GET("news")
    Call<List<NewsItem>> getNews();
}
