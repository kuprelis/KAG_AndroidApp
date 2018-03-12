package com.simaskuprelis.kag_androidapp.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.activity.MainActivity;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final int DEFAULT_ID = 0;
    private static final int ALERT_ID = 1;
    private static final int NEWS_ID = 2;


    @Override
    public void onMessageReceived(RemoteMessage msg) {
        super.onMessageReceived(msg);

        Map<String, String> data = msg.getData();
        if (!data.containsKey("body")) return;

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(MainActivity.EXTRA_TAB, MainActivity.TAB_NEWS);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        String title = data.containsKey("title") ?
                Utils.parseHtml(data.get("title"), false).toString() : getString(R.string.app_name);
        String text = Utils.parseHtml(data.get("body"), false).toString();

        NotificationCompat.Builder nb =
                new NotificationCompat.Builder(this, getString(R.string.main_channel))
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.notification))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(text));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nb.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        int id;
        String from = msg.getFrom();
        if (from == null) id = DEFAULT_ID;
        else {
            if (from.length() >= 8 && from.substring(0, 8).equals("/topics/")) from = from.substring(8);

            if (from.equals(getString(R.string.pref_alerts))) id = ALERT_ID;
            else if (from.equals(getString(R.string.pref_news))) id = NEWS_ID;
            else id = DEFAULT_ID;
        }

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, nb.build());
    }
}
