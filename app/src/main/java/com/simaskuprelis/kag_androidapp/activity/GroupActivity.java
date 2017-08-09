package com.simaskuprelis.kag_androidapp.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.CategoryDecoration;
import com.simaskuprelis.kag_androidapp.adapter.MultiCatAdapter;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.FirebaseListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.NodeListItem;
import com.simaskuprelis.kag_androidapp.entity.Node;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends AppCompatActivity {
    public static final String EXTRA_GROUP = "com.simaskuprelis.kag_androidapp.group";
    public static final String RESULT_GROUP_NODE_ID = "com.simaskuprelis.kag_androidapp.group_node_id";

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    private List<NodeListItem> mItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Group g = (Group) getIntent().getExtras().get(EXTRA_GROUP);
        if (g == null) return;

        setTitle(g.getName());

        final Context context = this;
        FirebaseDatabaseApi.getGroupNodes(g.getId(), new FirebaseListener<List<Node>>() {
            @Override
            public void onLoad(List<Node> obj) {
                Collections.sort(obj);
                mItems = new ArrayList<>();
                mItems.addAll(g.getLessons());
                mItems.addAll(obj);

                mLoadingIndicator.setVisibility(View.GONE);

                LinearLayoutManager llm = new LinearLayoutManager(context);
                mRecycler.setLayoutManager(llm);
                CategoryDecoration cd = new CategoryDecoration(context);
                mRecycler.addItemDecoration(cd);
                mRecycler.setAdapter(new MultiCatAdapter(mItems));
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onNodeSelect(Node n) {
        Intent i = new Intent();
        i.putExtra(RESULT_GROUP_NODE_ID, n.getId());
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
