package com.simaskuprelis.kag_androidapp;


import android.text.Html;
import android.text.Spanned;

public class Utils {
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
}
