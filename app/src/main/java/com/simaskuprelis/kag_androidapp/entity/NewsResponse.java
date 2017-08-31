package com.simaskuprelis.kag_androidapp.entity;

import com.squareup.moshi.Json;

import java.util.List;

public class NewsResponse {
    @Json(name = "total")
    private int mTotal;
    @Json(name = "per_page")
    private int mPerPage;
    @Json(name = "current_page")
    private int mCurrentPage;
    @Json(name = "last_page")
    private int mLastPage;
    @Json(name = "next_page_url")
    private String mNextUrl;
    @Json(name = "prev_page_url")
    private String mPrevUrl;
    @Json(name = "from")
    private int mFirst;
    @Json(name = "to")
    private int mLast;
    @Json(name = "data")
    private List<NewsItem> mItems;

    public int getTotal() {
        return mTotal;
    }

    public int getPerPage() {
        return mPerPage;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getLastPage() {
        return mLastPage;
    }

    public String getNextUrl() {
        return mNextUrl;
    }

    public String getPrevUrl() {
        return mPrevUrl;
    }

    public int getFirst() {
        return mFirst;
    }

    public int getLast() {
        return mLast;
    }

    public List<NewsItem> getItems() {
        return mItems;
    }
}
