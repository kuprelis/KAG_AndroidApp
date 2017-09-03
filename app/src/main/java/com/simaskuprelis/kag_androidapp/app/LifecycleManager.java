package com.simaskuprelis.kag_androidapp.app;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class LifecycleManager implements Application.ActivityLifecycleCallbacks {
    private FirebaseDatabase database;
    private int numActivities;

    public LifecycleManager() {
        database = FirebaseDatabase.getInstance();
        numActivities = 0;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numActivities == 0) {
            database.goOnline();
        }
        numActivities++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        numActivities--;
        if (numActivities == 0) {
            database.goOffline();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
