package com.simaskuprelis.kag_androidapp.entity;


public abstract class NodeListItem {
    public static final int TYPE_TEACHER = 0;
    public static final int TYPE_ROOM = 1;
    public static final int TYPE_STUDENT = 2;
    public static final int TYPE_LESSON = 3;
    public static final int TYPE_CATEGORY = 4;

    abstract public int getType();
}
