package com.simaskuprelis.kag_androidapp.entity;

import android.os.Parcelable;
import android.os.Parcel;

public class Lesson extends NodeListItem implements Parcelable {
    private int mDay;
    private int mNum;
    private String mRoom;

    @SuppressWarnings("unused")
    public Lesson() {}

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int number) {
        mNum = number;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        mRoom = room;
    }

    @Override
    public int getType() {
        return NodeListItem.TYPE_LESSON;
    }

    protected Lesson(Parcel in) {
        mDay = in.readInt();
        mNum = in.readInt();
        mRoom = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDay);
        dest.writeInt(mNum);
        dest.writeString(mRoom);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Lesson> CREATOR = new Parcelable.Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };
}
