package com.simaskuprelis.kag_androidapp.entity;

import android.view.View;

public class GroupSelectEvent {
    public Group group;
    public View view;

    public GroupSelectEvent(Group g, View v) {
        group = g;
        view = v;
    }
}
