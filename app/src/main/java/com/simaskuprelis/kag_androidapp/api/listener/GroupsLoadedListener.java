package com.simaskuprelis.kag_androidapp.api.listener;


import com.simaskuprelis.kag_androidapp.entity.Group;

import java.util.Map;

public interface GroupsLoadedListener {
    void onGroupsLoaded(Map<String, Group> groups);

    void onGroupsLoadingFailed(Exception exception);
}
