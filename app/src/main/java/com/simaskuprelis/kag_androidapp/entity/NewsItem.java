package com.simaskuprelis.kag_androidapp.entity;


import com.squareup.moshi.Json;

public class NewsItem {
    // TODO add decoder
    @Json(name = "id")
    private int mId;
    @Json(name = "title")
    private String mTitle;
    @Json(name = "category")
    private int mCategory;
    @Json(name = "primarytext")
    private String mTextPrimary;
    @Json(name = "maintext")
    private String mTextMain;
    //@Json(name = "visable")
    //private boolean mVisible;
    @Json(name = "fotourl")
    private String mPhotoUrl;
    //@Json(name = "gallery_id")
    //private int mGalleryId;
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

    public String getTextPrimary() {
        return mTextPrimary;
    }

    public String getTextMain() {
        return mTextMain;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }
}
