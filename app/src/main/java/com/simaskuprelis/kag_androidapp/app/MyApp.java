package com.simaskuprelis.kag_androidapp.app;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        fd.setPersistenceEnabled(true);
        fd.getReference().keepSynced(true);

        registerActivityLifecycleCallbacks(new LifecycleManager());
    }
}
