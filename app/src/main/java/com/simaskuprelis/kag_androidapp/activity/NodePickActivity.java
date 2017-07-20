package com.simaskuprelis.kag_androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.adapter.NodeAdapter;
import com.simaskuprelis.kag_androidapp.adapter.NodeClickListener;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.listener.NodesListener;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NodePickActivity extends AppCompatActivity {

    public static final String RESULT_NODE_ID = "com.simaskuprelis.kag_androidapp.user_id";

    @BindView(R.id.node_list)
    RecyclerView mNodeList;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    private List<Node> mNodes;
    private List<Node> mAdapterNodes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        setTitle(R.string.select_node);

        FirebaseDatabaseApi.getNodes(null, new NodesListener() {
            @Override
            public void onLoad(List<Node> nodes) {
                mNodes = new ArrayList<>(nodes);
                mAdapterNodes = nodes;
                setupAdapter();
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    private void setupAdapter() {
        mLoadingIndicator.setVisibility(View.GONE);
        NodeAdapter adapter = new NodeAdapter(mAdapterNodes, new NodeClickListener() {
            @Override
            public void onClick(Node n) {
                sendResult(n.getId());
            }
        });
        Utils.setupRecycler(mNodeList, this, adapter);
    }

    private void sendResult(String id) {
        Intent i = new Intent();
        i.putExtra(RESULT_NODE_ID, id);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_onboarding, menu);

        MenuItem searchItem = menu.findItem(R.id.node_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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

    private void filterItems(String query) {
        if (mNodes == null) return;
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mAdapterNodes.clear();
        query = query.toLowerCase();
        if (query.isEmpty()) {
            mAdapterNodes.addAll(mNodes);
        } else for (Node n : mNodes) {
            String name = n.getName().toLowerCase();
            if (name.contains(query)) mAdapterNodes.add(n);
        }
        mLoadingIndicator.setVisibility(View.GONE);
        mNodeList.getAdapter().notifyDataSetChanged();
    }
}
