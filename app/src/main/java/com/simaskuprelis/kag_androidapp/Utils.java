package com.simaskuprelis.kag_androidapp;


import android.text.Html;
import android.text.Spanned;

import com.google.firebase.database.FirebaseDatabase;
import com.simaskuprelis.kag_androidapp.api.NewsApi;
import com.squareup.moshi.Moshi;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Utils {
    private static final String BASE_URL = "http://www.azuolynogimnazija.lt/json/";
    private static boolean sDatabaseSetup = false;

    public static void setupDatabase() {
        if (sDatabaseSetup) return;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        sDatabaseSetup = true;
    }

    public static CharSequence parseHtml(String html) {
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
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
        return retrofit.create(NewsApi.class);
    }
}
