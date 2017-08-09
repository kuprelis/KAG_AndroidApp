package com.simaskuprelis.kag_androidapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.title_text)
    TextView mTitle;
    @BindView(R.id.date_created)
    TextView mCreated;
    @BindView(R.id.date_updated)
    TextView mUpdated;
    @BindView(R.id.body_text)
    WebView mBody;

    private NewsItem mItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);

        mItem = getIntent().getParcelableExtra(EXTRA_ARTICLE);

        String imgUrl = mItem.getPhotoUrl();
        if (imgUrl.isEmpty()) {
            mImage.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(imgUrl).into(mImage);
        }

        mTitle.setText(mItem.getTitle());

        mBody.setBackgroundColor(Color.TRANSPARENT);
        String html =
                "<style>" +
                "body{margin: 0; padding: 0}" +
                "div{margin: 0 !important; padding: 0 !important}" +
                "img{display: inline; height: auto; max-width: 100%;}" +
                "</style>" +
                mItem.getText() + mItem.getBonusText();
        mBody.loadDataWithBaseURL(Utils.BASE_URL, html, "text/html", null, null);

        String created = mItem.getCreated();
        String updated = mItem.getUpdated();
        mCreated.setText(created);
        if (created.equals(updated)) {
            mUpdated.setVisibility(View.GONE);
        } else {
            mUpdated.setText(getString(R.string.updated) + " " + updated);
        }
    }
}
