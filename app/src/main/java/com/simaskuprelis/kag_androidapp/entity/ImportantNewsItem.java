package com.simaskuprelis.kag_androidapp.entity;


import com.squareup.moshi.Json;

public class ImportantNewsItem extends NewsListItem {
    private static final int ACTIVE = 1;

    @Json(name = "text")
    private String mText;
    @Json(name = "active")
    private int mActive;
    @Json(name = "updated_at")
    private String mUpdated;

    public String getText() {
        return mText;
    }

    public boolean isActive() {
        return mActive == ACTIVE;
    }

    public String getUpdated() {
        return mUpdated;
    }

    @Override
    public int getType() {
        return TYPE_IMPORTANT;
    }
}
