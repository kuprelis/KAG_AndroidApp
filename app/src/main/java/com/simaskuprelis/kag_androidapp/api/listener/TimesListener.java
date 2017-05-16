package com.simaskuprelis.kag_androidapp.api.listener;

import java.util.List;

public interface TimesListener {
    void onLoad(List<Integer> times);

    void onFail(Exception e);
}
