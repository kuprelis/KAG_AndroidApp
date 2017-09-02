package com.simaskuprelis.kag_androidapp.entity;

import com.squareup.moshi.Json;

import java.util.List;

public class NewsResponse {
    @Json(name = "current_page")
    private int currentPage;
    @Json(name = "last_page")
    private int lastPage;
    @Json(name = "data")
    private List<NewsItem> newsItems;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public List<NewsItem> getItems() {
        return newsItems;
    }
}
