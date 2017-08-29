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
import com.simaskuprelis.kag_androidapp.entity.NewsItem;
import com.simaskuprelis.kag_androidapp.entity.NewsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsReceiver extends BroadcastReceiver {
    private static final String TAG = "NewsReceiver";

    public static final String ACTION_POLL_IMPORTANT = "com.simaskuprelis.kag_androidapp.POLL_IMPORTANT";

    @Override
    public void onReceive(final Context context, Intent intent) {
        NewsApi api = Utils.getApi();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyImportant = context.getString(R.string.pref_notify_important);
        String keyNews = context.getString(R.string.pref_notify_news);

        if (sp.getBoolean(keyImportant, true)) {
            api.getImportantNews().enqueue(new Callback<ImportantNewsItem>() {
                @Override
                public void onResponse(Call<ImportantNewsItem> call, Response<ImportantNewsItem> response) {
                    ImportantNewsItem item = response.body();
                    if (!item.isActive()) return;

                    String key = context.getString(R.string.pref_last_important);
                    if (isNew(context, key, item.getUpdated())) {
                        String title = context.getString(R.string.important_news);
                        String text = Utils.parseHtml(item.getText()).toString();
                        sendNotification(context, title, text);
                    }
                }

                @Override
                public void onFailure(Call<ImportantNewsItem> call, Throwable t) {
                    FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
                }
            });
        }

        if (sp.getBoolean(keyNews, false)) {
            api.getNews(1, 1).enqueue(new Callback<NewsResponse>() {
                @Override
                public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                    NewsResponse nr = response.body();
                    NewsItem item = nr.getItems().get(0);
                    if (!item.isVisible()) return;

                    String key = context.getString(R.string.pref_last_news);
                    if (isNew(context, key, item.getCreated())) {
                        String title = context.getString(R.string.news);
                        String text = Utils.parseHtml(item.getText()).toString();
                        sendNotification(context, title, text);
                    }
                }

                @Override
                public void onFailure(Call<NewsResponse> call, Throwable t) {
                    FirebaseCrash.logcat(Log.ERROR, TAG, t.toString());
                }
            });
        }
    }

    private boolean isNew(Context c, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        String previous = sp.getString(key, null);
        if (!value.equals(previous)) sp.edit().putString(key, value).apply();
        return previous == null || previous.compareTo(value) < 0;
    }

    private void sendNotification(Context c, String title, String body) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int color = c.getResources().getColor(R.color.notification);

        Intent i = new Intent(c, MainActivity.class);
        i.putExtra(MainActivity.EXTRA_TAB, MainActivity.TAB_NEWS);
        PendingIntent pi = PendingIntent.getActivity(c, 0, i, 0);

        NotificationCompat.Style style = new NotificationCompat.BigTextStyle().bigText(body);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(c)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(sound)
                .setColor(color)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setStyle(style);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nb.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(title, 0, nb.build());
    }
}
