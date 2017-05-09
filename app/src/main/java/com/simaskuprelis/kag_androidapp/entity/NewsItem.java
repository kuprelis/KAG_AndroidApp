package com.simaskuprelis.kag_androidapp.entity;


import com.squareup.moshi.Json;

public class NewsItem {
    private static final int VISIBLE = 1;

    @Json(name = "title")
    private String mTitle;
    @Json(name = "category")
    private int mCategory;
    @Json(name = "primarytext")
    private String mText;
    @Json(name = "maintext")
    private String mBonusText;
    @Json(name = "visable")
    private int mVisible;
    @Json(name = "fotourl")
    private String mPhotoUrl;
    @Json(name = "created_at")
    private String mCreated;
    @Json(name = "updated_at")
    private String mUpdated;

    public String getTitle() {
        return mTitle;
    }

    public int getCategory() {
        return mCategory;
    }

    public String getText() {
        return mText;
    }

    public String getBonusText() {
        return mBonusText;
    }

    public boolean isVisible() {
        return mVisible == VISIBLE;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public String getCreated() {
        return mCreated;
    }

    public String getUpdated() {
        return mUpdated;
    }
}
