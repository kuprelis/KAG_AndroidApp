package com.simaskuprelis.kag_androidapp.entity;


import com.squareup.moshi.Json;

public class ImportantNewsItem {
    private static final int ACTIVE = 1;

    @Json(name = "text")
    private String mText;
    @Json(name = "active")
    private int mActive;

    public String getText() {
        return mText;
    }

    public boolean isActive() {
        return mActive == ACTIVE;
    }
}
