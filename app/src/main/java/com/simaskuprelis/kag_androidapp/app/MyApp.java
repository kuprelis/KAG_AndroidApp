package com.simaskuprelis.kag_androidapp.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
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
        Utils.updateSubscriptions(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notifications);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel ch = new NotificationChannel(getString(R.string.main_channel), name, importance);
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(ch);
        }
    }
}
