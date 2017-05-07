package com.simaskuprelis.kag_androidapp.entity;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class Node {
    private static final int TEACHER = 1;
    private static final int ROOM = 2;
    private static final int STUDENT = 3;

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
}
