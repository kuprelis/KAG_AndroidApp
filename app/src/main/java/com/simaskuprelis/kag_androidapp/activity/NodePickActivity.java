package com.simaskuprelis.kag_androidapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.decoration.CategoryDecoration;
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
    RecyclerView recycler;
    @BindView(R.id.loading_indicator)
    ProgressBar progressBar;

    private List<NodeListItem> nodes;
    private List<NodeListItem> adapterNodes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_pick);
        ButterKnife.bind(this);

        Intent i = getIntent();
        boolean enableUp = !i.hasExtra(EXTRA_UP_NAV) || i.getExtras().getBoolean(EXTRA_UP_NAV, true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enableUp);

        setTitle(R.string.select_node);

        final Context c = this;
        FirebaseDatabaseApi.getAllNodes(new FirebaseListener<List<Node>>() {
            @Override
            public void onLoad(List<Node> obj) {
                nodes = new ArrayList<NodeListItem>(obj);
                adapterNodes = new ArrayList<NodeListItem>(obj);

                progressBar.setVisibility(View.GONE);
                LinearLayoutManager llm = new LinearLayoutManager(c);
                recycler.setLayoutManager(llm);
                CategoryDecoration cd = new CategoryDecoration(c);
                recycler.addItemDecoration(cd);
                recycler.setAdapter(new MultiCatAdapter(adapterNodes));
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
        if (nodes == null) return;
        progressBar.setVisibility(View.VISIBLE);
        adapterNodes.clear();
        query = query.toLowerCase();
        if (query.isEmpty()) {
            adapterNodes.addAll(nodes);
        } else for (NodeListItem nli : nodes) {
            Node n = (Node) nli;
            String name = n.getName().toLowerCase();
            if (name.contains(query)) adapterNodes.add(nli);
        }
        progressBar.setVisibility(View.GONE);
        MultiCatAdapter adapter = (MultiCatAdapter) recycler.getAdapter();
        adapter.notifyDataChange();
    }
}
