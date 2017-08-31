package com.simaskuprelis.kag_androidapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.CategoryDecoration;
import com.simaskuprelis.kag_androidapp.adapter.MultiCatAdapter;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.FirebaseListener;
import com.simaskuprelis.kag_androidapp.entity.Node;
import com.simaskuprelis.kag_androidapp.entity.NodeListItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NodePickActivity extends AppCompatActivity {

    public static final String EXTRA_UP_NAV = "com.simaskuprelis.kag_androidapp.up_nav";
    public static final String RESULT_NODE = "com.simaskuprelis.kag_androidapp.node";

    @BindView(R.id.node_list)
    RecyclerView mNodeList;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    private List<NodeListItem> mNodes;
    private List<NodeListItem> mAdapterNodes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        Intent i = getIntent();
        boolean enableUp = !i.hasExtra(EXTRA_UP_NAV) || i.getExtras().getBoolean(EXTRA_UP_NAV, true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enableUp);

        setTitle(R.string.select_node);

        final Context c = this;
        FirebaseDatabaseApi.getAllNodes(new FirebaseListener<List<Node>>() {
            @Override
            public void onLoad(List<Node> obj) {
                mNodes = new ArrayList<NodeListItem>(obj);
                mAdapterNodes = new ArrayList<NodeListItem>(obj);

                mLoadingIndicator.setVisibility(View.GONE);
                LinearLayoutManager llm = new LinearLayoutManager(c);
                mNodeList.setLayoutManager(llm);
                CategoryDecoration cd = new CategoryDecoration(c);
                mNodeList.addItemDecoration(cd);
                mNodeList.setAdapter(new MultiCatAdapter(mAdapterNodes));
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
        i.putExtra(RESULT_NODE, n);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_onboarding, menu);

        MenuItem searchItem = menu.findItem(R.id.node_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterItems(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterItems(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void filterItems(String query) {
        if (mNodes == null) return;
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mAdapterNodes.clear();
        query = query.toLowerCase();
        if (query.isEmpty()) {
            mAdapterNodes.addAll(mNodes);
        } else for (NodeListItem nli : mNodes) {
            Node n = (Node) nli;
            String name = n.getName().toLowerCase();
            if (name.contains(query)) mAdapterNodes.add(nli);
        }
        mLoadingIndicator.setVisibility(View.GONE);
        MultiCatAdapter adapter = (MultiCatAdapter) mNodeList.getAdapter();
        adapter.notifyDataChange();
    }
}
