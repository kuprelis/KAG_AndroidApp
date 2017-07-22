package com.simaskuprelis.kag_androidapp;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;

import com.simaskuprelis.kag_androidapp.api.NewsApi;
import com.simaskuprelis.kag_androidapp.receiver.ImportantNewsReceiver;
import com.squareup.moshi.Moshi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Utils {
    public static final String BASE_URL = "http://www.azuolynogimnazija.lt";
    public static final String JSON_URL = "http://www.azuolynogimnazija.lt/json/";

    public static CharSequence parseHtml(String html) {
        Document doc = Jsoup.parse(html, BASE_URL);
        Elements elements = doc.select("a");
        for (Element e : elements) {
            String absUrl = e.attr("abs:href");
            if (!absUrl.isEmpty()) e.attr("href", absUrl);
        }
        html = doc.html();

        Spanned s = Html.fromHtml(html);

        int i = s.length() - 1;
        while (i >= 0) {
            char c = s.charAt(i);
            if (!Character.isWhitespace(c) && !Character.isSpaceChar(c)) break;
            i--;
        }
        return s.subSequence(0, i+1);
    }

    public static NewsApi getApi() {
        Moshi moshi = new Moshi.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JSON_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
        return retrofit.create(NewsApi.class);
    }

    public static void updatePollState(Context c) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        boolean on = sp.getBoolean(c.getString(R.string.pref_notify_important), true);

        Intent i = new Intent(ImportantNewsReceiver.ACTION_POLL_IMPORTANT);
        PendingIntent pi = PendingIntent.getBroadcast(c, 0, i, PendingIntent.FLAG_NO_CREATE);
        if (on) {
            if (pi == null) pi = PendingIntent.getBroadcast(c, 0, i, 0);
            int interval = Integer.valueOf(sp.getString(c.getString(R.string.pref_poll_interval), "1"));
            am.cancel(pi);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    interval * 60 * 60 * 1000, pi);
        } else  {
            if (pi != null) {
                am.cancel(pi);
                pi.cancel();
            }
        }
    }

    public static void setupRecycler(RecyclerView rv, Context c, RecyclerView.Adapter adapter) {
        if (c == null) return;
        LinearLayoutManager llm = new LinearLayoutManager(c);
        rv.setLayoutManager(llm);
        DividerItemDecoration did = new DividerItemDecoration(c, llm.getOrientation());
        rv.addItemDecoration(did);
        rv.setAdapter(adapter);
    }
}
