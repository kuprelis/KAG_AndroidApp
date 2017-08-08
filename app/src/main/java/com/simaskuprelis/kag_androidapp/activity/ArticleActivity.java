package com.simaskuprelis.kag_androidapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    TextView mBody;

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
            Glide.with(this).load(mItem.getPhotoUrl()).into(mImage);
        }

        mTitle.setText(mItem.getTitle());

        String body = mItem.getText() + mItem.getBonusText();
        mBody.setText(Utils.parseHtml(body));

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
