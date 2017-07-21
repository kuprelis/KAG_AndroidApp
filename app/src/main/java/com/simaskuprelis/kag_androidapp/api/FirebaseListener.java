package com.simaskuprelis.kag_androidapp.api;

public interface FirebaseListener<T> {
    void onLoad(T obj);

    void onFail(Exception e);
}
