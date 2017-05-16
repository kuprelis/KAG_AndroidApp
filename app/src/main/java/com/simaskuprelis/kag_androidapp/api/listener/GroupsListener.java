package com.simaskuprelis.kag_androidapp.api.listener;

import com.simaskuprelis.kag_androidapp.entity.Group;

import java.util.List;

public interface GroupsListener {
    void onLoad(List<Group> groups);

    void onFail(Exception e);
}
