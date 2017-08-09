package com.simaskuprelis.kag_androidapp.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.simaskuprelis.kag_androidapp.Utils;
import com.squareup.moshi.Json;

public class NewsItem extends NewsListItem implements Parcelable {

    @Json(name = "title")
    private String mTitle;
    @Json(name = "category")
    private int mCategory;
    @Json(name = "primarytext")
    private String mText;
    @Json(name = "maintext")
    private String mBonusText;
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

    public String getPhotoUrl() {
        return Utils.absUrl(mPhotoUrl);
    }

    public String getCreated() {
        return mCreated.substring(0, mCreated.length() - 3);
    }

    public String getUpdated() {
        return mUpdated.substring(0, mUpdated.length() - 3);
    }

    @Override
    public int getType() {
        return TYPE_REGULAR;
    }

    protected NewsItem(Parcel in) {
        mTitle = in.readString();
        mCategory = in.readInt();
        mText = in.readString();
        mBonusText = in.readString();
        mPhotoUrl = in.readString();
        mCreated = in.readString();
        mUpdated = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeInt(mCategory);
        dest.writeString(mText);
        dest.writeString(mBonusText);
        dest.writeString(mPhotoUrl);
        dest.writeString(mCreated);
        dest.writeString(mUpdated);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };
}
