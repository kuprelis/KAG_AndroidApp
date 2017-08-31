package com.simaskuprelis.kag_androidapp.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Node extends NodeListItem implements Parcelable, Comparable<Node> {
    public static final int TEACHER = 1;
    public static final int ROOM = 2;
    public static final int STUDENT = 3;

    private String mId;
    private int mCat;
    private String mName;

    @SuppressWarnings("unused")
    public Node() {}

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getCat() {
        return mCat;
    }

    public void setCat(int cat) {
        mCat = cat;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Override
    public int getType() {
        switch (mCat) {
            case TEACHER: return NodeListItem.TYPE_TEACHER;
            case ROOM: return NodeListItem.TYPE_ROOM;
            case STUDENT: return NodeListItem.TYPE_STUDENT;
            default: return -1;
        }
    }

    @Override
    public int compareTo(@NonNull Node o) {
        if (getCat() != o.getCat()) return getCat() - o.getCat();
        return getName().compareToIgnoreCase(o.getName());
    }

    protected Node(Parcel in) {
        mId = in.readString();
        mCat = in.readInt();
        mName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeInt(mCat);
        dest.writeString(mName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Node> CREATOR = new Parcelable.Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };
}
