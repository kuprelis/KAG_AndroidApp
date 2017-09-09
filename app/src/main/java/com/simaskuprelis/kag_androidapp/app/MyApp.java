package com.simaskuprelis.kag_androidapp.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.google.firebase.database.FirebaseDatabase;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        fd.setPersistenceEnabled(true);
        fd.getReference().keepSynced(true);

        registerActivityLifecycleCallbacks(new LifecycleManager());

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        Utils.updatePollState(this);
    }
}
