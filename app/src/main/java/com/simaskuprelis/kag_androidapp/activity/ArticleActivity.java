package com.simaskuprelis.kag_androidapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.entity.NewsItem;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ArticleActivity extends AppCompatActivity {
    public static final String EXTRA_ARTICLE = "com.simaskuprelis.kag_androidapp.article";

    private static final String CSS =
            "<style>" +
            "body{margin: 0; padding: 0}" +
            "div{margin: 0 !important; padding: 0 !important}" +
            "img{display: inline; height: auto; max-width: 100%;}" +
            "a{overflow-wrap: break-word;}" +
            "</style>";

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title_text)
    TextView title;
    @BindView(R.id.date_created)
    TextView created;
    @BindView(R.id.date_updated)
    TextView updated;
    @BindView(R.id.body_text)
    WebView body;

    private NewsItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        item = getIntent().getParcelableExtra(EXTRA_ARTICLE);

        String imgUrl = item.getPhotoUrl();
        if (imgUrl.isEmpty()) {
            image.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(imgUrl).into(image);
        }

        title.setText(item.getTitle());

        body.setBackgroundColor(Color.TRANSPARENT);
        body.loadDataWithBaseURL(Utils.BASE_URL, CSS + item.getText(), "text/html", null, null);

        String created = item.getCreated();
        String updated = item.getUpdated();
        this.created.setText(created);
        if (created.equals(updated)) {
            this.updated.setVisibility(View.GONE);
        } else {
            this.updated.setText(getString(R.string.updated, updated));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = NavUtils.getParentActivityIntent(this);
            i.putExtra(MainActivity.EXTRA_TAB, MainActivity.TAB_NEWS);
            NavUtils.navigateUpTo(this, i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
