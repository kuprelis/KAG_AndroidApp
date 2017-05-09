package com.simaskuprelis.kag_androidapp.api;


import com.simaskuprelis.kag_androidapp.entity.ImportantNewsItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("news")
    Call<NewsResponse> getNews(@Query("page") Integer page, @Query("perpage") Integer perPage);

    @GET("svarbu")
    Call<ImportantNewsItem> getImportantNews();
}
