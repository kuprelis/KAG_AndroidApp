package com.simaskuprelis.kag_androidapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Group implements Parcelable {
    private String mName;
    private List<String> mNodes;
    private List<Lesson> mLessons;

    @SuppressWarnings("unused")
    public Group() {}

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<String> getNodes() {
        return mNodes;
    }

    public void setNodes(List<String> nodes) {
        mNodes = nodes;
    }

    public List<Lesson> getLessons() {
        return mLessons;
    }

    public void setLessons(List<Lesson> lessons) {
        mLessons = lessons;
    }

    protected Group(Parcel in) {
        mName = in.readString();
        if (in.readByte() == 0x01) {
            mNodes = new ArrayList<>();
            in.readList(mNodes, String.class.getClassLoader());
        } else {
            mNodes = null;
        }
        if (in.readByte() == 0x01) {
            mLessons = new ArrayList<>();
            in.readList(mLessons, Lesson.class.getClassLoader());
        } else {
            mLessons = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        if (mNodes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mNodes);
        }
        if (mLessons == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mLessons);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
