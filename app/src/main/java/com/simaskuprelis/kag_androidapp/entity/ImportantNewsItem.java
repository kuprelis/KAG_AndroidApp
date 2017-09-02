package com.simaskuprelis.kag_androidapp.entity;


import com.squareup.moshi.Json;

public class ImportantNewsItem extends NewsListItem {

    @Json(name = "text")
    private String text;
    @Json(name = "active")
    private int active;
    @Json(name = "updated_at")
    private String updated;

    public String getText() {
        return text;
    }

    public boolean isActive() {
        return active == 1;
    }

    public String getUpdated() {
        return updated;
    }

    @Override
    public int getType() {
        return TYPE_IMPORTANT;
    }
}
