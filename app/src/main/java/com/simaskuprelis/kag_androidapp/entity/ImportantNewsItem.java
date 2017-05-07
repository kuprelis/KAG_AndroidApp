package com.simaskuprelis.kag_androidapp.entity;


import java.util.Date;

public class ImportantNewsItem {
    private String mTitle;
    private String mText;
    private boolean mActive;
    //@Json(name = "created_at")
    //private Date mCreated;
    //@Json(name = "updated_at")
    //private Date mUpdated;

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public boolean isActive() {
        return mActive;
    }
}
