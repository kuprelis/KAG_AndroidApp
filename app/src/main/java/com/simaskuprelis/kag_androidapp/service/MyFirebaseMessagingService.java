package com.simaskuprelis.kag_androidapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.simaskuprelis.kag_androidapp.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification rmn = remoteMessage.getNotification();
        if (rmn == null) return;

        String title = rmn.getTitle();
        Notification n = new NotificationCompat.Builder(this)
                .setContentTitle(title != null ? title : getString(R.string.app_name))
                .setContentText(rmn.getBody())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(getColor(R.color.colorAccent))
                .build();


        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, n);
    }
}
