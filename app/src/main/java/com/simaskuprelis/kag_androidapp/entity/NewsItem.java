package com.simaskuprelis.kag_androidapp.entity;


import com.squareup.moshi.Json;

public class NewsItem {
    // TODO add decoder
    // TODO remove id if not needed
    @Json(name = "id")
    private int mId;
    @Json(name = "title")
    private String mTitle;
    @Json(name = "category")
    private int mCategory;
    @Json(name = "primarytext")
    private String mText;
    @Json(name = "maintext")
    private String mBonusText;
    @Json(name = "visable")
    private boolean mVisible;
    @Json(name = "fotourl")
    private String mPhotoUrl;
    //@Json(name = "created_at")
    //private Date mCreated;
    //@Json(name = "updated_at")
    //private Date mUpdated;

    public int getId() {
        return mId;
    }

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

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public boolean isVisible() {
        return mVisible;
    }
}
