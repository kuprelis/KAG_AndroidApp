package com.simaskuprelis.kag_androidapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;

import com.google.firebase.messaging.FirebaseMessaging;
import com.simaskuprelis.kag_androidapp.api.NewsApi;
import com.squareup.moshi.Moshi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Utils {
    public static final String BASE_URL = "http://www.azuolynogimnazija.lt";
    public static final String JSON_URL = "http://www.azuolynogimnazija.lt/json/";

    public static String absUrl(String url) {
        if (url.length() >= 2 && url.substring(0, 2).equals("..")) {
            return BASE_URL + url.substring(url.indexOf('/'));
        }
        return url;
    }

    public static Spanned parseHtml(String html, boolean fixLinks) {
        if (fixLinks) {
            Pattern tag = Pattern.compile("<a href=\"..");
            Matcher match = tag.matcher(html);
            StringBuilder sb = new StringBuilder();
            int last = 0;
            while (match.find()) {
                sb.append(html.substring(last, match.start() + 9)).append(BASE_URL);
                last = match.end();
            }
            sb.append(html.substring(last));
            html = sb.toString();
        }

        Spanned s;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            s = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        } else {
            s = Html.fromHtml(html);
        }

        int i = s.length() - 1;
        while (i >= 0) {
            char c = s.charAt(i);
            if (!Character.isWhitespace(c) && !Character.isSpaceChar(c)) break;
            i--;
        }
        return (Spanned) s.subSequence(0, i + 1);
    }

    public static NewsApi getApi() {
        Moshi moshi = new Moshi.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JSON_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
        return retrofit.create(NewsApi.class);
    }

    public static void setSubscription(String topic, boolean subscribe) {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        if (subscribe) fm.subscribeToTopic(topic);
        else fm.unsubscribeFromTopic(topic);
    }

    public static void updateSubscriptions(Context c) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);

        String alertKey = c.getString(R.string.pref_alerts);
        String newsKey = c.getString(R.string.pref_news);
        setSubscription(alertKey, sp.getBoolean(alertKey, true));
        setSubscription(newsKey, sp.getBoolean(newsKey, false));
    }
}
