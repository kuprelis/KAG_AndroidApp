package com.simaskuprelis.kag_androidapp.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.simaskuprelis.kag_androidapp.Utils;
import com.squareup.moshi.Json;

public class NewsItem extends NewsListItem implements Parcelable {

    @Json(name = "title")
    private String title;
    @Json(name = "category")
    private int category;
    @Json(name = "primarytext")
    private String text;
    @Json(name = "maintext")
    private String bonusText;
    @Json(name = "visable")
    private int visible;
    @Json(name = "fotourl")
    private String photoUrl;
    @Json(name = "created_at")
    private String created;
    @Json(name = "updated_at")
    private String updated;

    public String getTitle() {
        return title;
    }

    public int getCategory() {
        return category;
    }

    public String getText() {
        return text + bonusText;
    }

    public boolean isVisible() {
        return visible == 1;
    }

    public String getPhotoUrl() {
        return Utils.absUrl(photoUrl);
    }

    public String getCreated() {
        return created.substring(0, created.length() - 3);
    }

    public String getUpdated() {
        return updated.substring(0, updated.length() - 3);
    }

    @Override
    public int getType() {
        return TYPE_REGULAR;
    }

    protected NewsItem(Parcel in) {
        title = in.readString();
        category = in.readInt();
        text = in.readString();
        bonusText = in.readString();
        visible = in.readInt();
        photoUrl = in.readString();
        created = in.readString();
        updated = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(category);
        dest.writeString(text);
        dest.writeString(bonusText);
        dest.writeInt(visible);
        dest.writeString(photoUrl);
        dest.writeString(created);
        dest.writeString(updated);
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
