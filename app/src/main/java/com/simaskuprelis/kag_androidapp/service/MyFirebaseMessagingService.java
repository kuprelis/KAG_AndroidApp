package com.simaskuprelis.kag_androidapp.service;

import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.net.Uri;
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

        NotificationCompat.Builder nb = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(rmn.getBody())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(getResources().getColor(R.color.accent));

        if (rmn.getSound() != null) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            nb.setSound(uri);
        }

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, nb.build());
    }
}
