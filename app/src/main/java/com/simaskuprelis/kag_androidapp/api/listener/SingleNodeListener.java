package com.simaskuprelis.kag_androidapp.api.listener;

import com.simaskuprelis.kag_androidapp.entity.Node;

public interface SingleNodeListener {
    void onLoad(Node node);

    void onFail(Exception e);
}
