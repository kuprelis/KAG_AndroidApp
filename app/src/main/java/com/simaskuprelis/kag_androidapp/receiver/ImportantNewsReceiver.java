package com.simaskuprelis.kag_androidapp.receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.activity.MainActivity;
import com.simaskuprelis.kag_androidapp.api.NewsApi;
import com.simaskuprelis.kag_androidapp.entity.ImportantNewsItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportantNewsReceiver extends BroadcastReceiver {

    private static final String TAG = "ImportantNewsReceiver";
    private static final String PREF_LAST_UPDATE = "lastUpdate";

    public static final String ACTION_POLL_IMPORTANT = "com.simaskuprelis.kag_androidapp.POLL_IMPORTANT";

    @Override
    public void onReceive(final Context context, Intent intent) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final String last_update = sp.getString(PREF_LAST_UPDATE, null);
        NewsApi api = Utils.getApi();
        api.getImportantNews().enqueue(new Callback<ImportantNewsItem>() {
            @Override
            public void onResponse(Call<ImportantNewsItem> call, Response<ImportantNewsItem> response) {
                ImportantNewsItem item = response.body();
                if (!item.isActive()) return;
                String update = item.getUpdated();
                if (last_update == null || last_update.compareTo(update) < 0) {
                    sp.edit().putString(PREF_LAST_UPDATE, update).apply();

                    String text = Utils.parseHtml(item.getText()).toString();
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra(MainActivity.EXTRA_TAB, MainActivity.TAB_NEWS);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

                    NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(context.getString(R.string.important_news))
                            .setContentText(text)
                            .setSound(sound)
                            .setColor(context.getResources().getColor(R.color.notification))
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        nb.setVisibility(Notification.VISIBILITY_PUBLIC);
                    }

                    NotificationManager nm = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(0, nb.build());
                }
            }

            @Override
            public void onFailure(Call<ImportantNewsItem> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
            }
        });
    }
}
