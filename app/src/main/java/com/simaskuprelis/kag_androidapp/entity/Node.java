package com.simaskuprelis.kag_androidapp.entity;

import android.support.annotation.NonNull;

public class Node extends NodeListItem implements Comparable<Node> {
    public static final int TEACHER = 1;
    public static final int ROOM = 2;
    public static final int STUDENT = 3;

    private String mId;
    private int mCat;
    private String mName;

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
}
