package com.simaskuprelis.kag_androidapp.api.listener;


public interface PreloadListener {
    void onLoad();

    void onFail(Exception e);
}
