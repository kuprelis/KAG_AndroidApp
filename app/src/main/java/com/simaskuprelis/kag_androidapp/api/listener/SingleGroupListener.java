package com.simaskuprelis.kag_androidapp.api.listener;

import com.simaskuprelis.kag_androidapp.entity.Group;

public interface SingleGroupListener {
    void onLoad(Group group);

    void onFail(Exception e);
}
