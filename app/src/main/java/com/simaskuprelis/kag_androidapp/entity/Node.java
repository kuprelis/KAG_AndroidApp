package com.simaskuprelis.kag_androidapp.entity;

import android.support.annotation.NonNull;

import java.util.List;

public class Node implements Comparable<Node> {
    public static final int TEACHER = 1;
    public static final int ROOM = 2;
    public static final int STUDENT = 3;

    private String mId;
    private int mCategory;
    private List<String> mGroups;
    private String mName;

    public List<String> getGroups() {
        return mGroups;
    }

    public void setGroups(List<String> groups) {
        mGroups = groups;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Override
    public int compareTo(@NonNull Node o) {
        if (getCategory() != o.getCategory()) return getCategory() - o.getCategory();
        return getName().compareToIgnoreCase(o.getName());
    }
}
