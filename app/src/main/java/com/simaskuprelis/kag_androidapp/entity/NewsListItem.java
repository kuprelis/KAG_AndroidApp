package com.simaskuprelis.kag_androidapp.entity;


public abstract class NewsListItem {
    public static final int TYPE_IMPORTANT = 0;
    public static final int TYPE_REGULAR = 1;

    abstract public int getType();
}
