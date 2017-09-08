package com.simaskuprelis.kag_androidapp.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.database.FirebaseDatabase;
import com.simaskuprelis.kag_androidapp.R;


public class MyApp extends Application {
    public static final String CHANNEL_IMPORTANT = "important";
    public static final String CHANNEL_NEWS = "news";

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        fd.setPersistenceEnabled(true);
        fd.getReference().keepSynced(true);

        registerActivityLifecycleCallbacks(new LifecycleManager());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) setupChannels();
    }

    @TargetApi(26)
    private void setupChannels() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String nameImportant = getString(R.string.important_news);
        String nameNews = getString(R.string.news);
        NotificationChannel nci = new NotificationChannel(
                CHANNEL_IMPORTANT, nameImportant, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationChannel ncn = new NotificationChannel(
                CHANNEL_NEWS, nameNews, NotificationManager.IMPORTANCE_DEFAULT);

        nm.createNotificationChannel(nci);
        nm.createNotificationChannel(ncn);
    }
}
