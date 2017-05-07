package com.simaskuprelis.kag_androidapp.entity;

import java.util.List;

public class Group {
    private String mName;
    private List<String> mNodes;
    private List<Lesson> mLessons;

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
}
