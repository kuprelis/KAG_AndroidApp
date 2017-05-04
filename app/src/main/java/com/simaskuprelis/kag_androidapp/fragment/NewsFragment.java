package com.simaskuprelis.kag_androidapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.simaskuprelis.kag_androidapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment {
    // TODO get from firebase
    private static final String PAGE_URL = "http://www.azuolynogimnazija.lt/";

    @BindView(R.id.web_view)
    WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, v);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(PAGE_URL);
        return v;
    }
}
