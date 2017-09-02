package com.simaskuprelis.kag_androidapp.entity;

import android.os.Parcelable;
import android.os.Parcel;

public class Lesson extends NodeListItem implements Parcelable {
    private int day;
    private int num;
    private String room;

    @SuppressWarnings("unused")
    public Lesson() {}

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int number) {
        num = number;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public int getType() {
        return NodeListItem.TYPE_LESSON;
    }

    protected Lesson(Parcel in) {
        day = in.readInt();
        num = in.readInt();
        room = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day);
        dest.writeInt(num);
        dest.writeString(room);
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
