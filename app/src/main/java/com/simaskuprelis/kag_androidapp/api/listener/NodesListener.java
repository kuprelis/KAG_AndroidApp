package com.simaskuprelis.kag_androidapp.api.listener;

import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.List;

public interface NodesListener {
    void onLoad(List<Node> nodes);

    void onFail(Exception e);
}
