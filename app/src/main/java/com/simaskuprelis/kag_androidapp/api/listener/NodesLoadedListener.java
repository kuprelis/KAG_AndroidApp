package com.simaskuprelis.kag_androidapp.api.listener;


import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.Map;

public interface NodesLoadedListener {
    void onNodesLoaded(Map<String, Node> nodes);

    void onNodesLoadingFailed(Exception exception);
}
