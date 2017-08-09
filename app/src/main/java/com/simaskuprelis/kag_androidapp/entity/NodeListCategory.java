package com.simaskuprelis.kag_androidapp.entity;


import android.content.Context;

import com.simaskuprelis.kag_androidapp.R;

public class NodeListCategory extends NodeListItem {
    private int mCategory;

    public NodeListCategory(int category) {
        mCategory = category;
    }

    public String getName(Context c) {
        switch (mCategory) {
            case NodeListItem.TYPE_LESSON: return c.getString(R.string.lessons);
            case NodeListItem.TYPE_ROOM: return c.getString(R.string.room);
            case NodeListItem.TYPE_STUDENT: return c.getString(R.string.students);
            case NodeListItem.TYPE_TEACHER: return c.getString(R.string.teacher);
            default: return "";
        }
    }

    @Override
    public int getType() {
        return TYPE_CATEGORY;
    }
}
