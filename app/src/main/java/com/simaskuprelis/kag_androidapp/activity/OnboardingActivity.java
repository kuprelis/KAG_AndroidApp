package com.simaskuprelis.kag_androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.NodeAdapter;
import com.simaskuprelis.kag_androidapp.adapter.NodeClickListener;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.listener.NodesListener;
import com.simaskuprelis.kag_androidapp.api.listener.PreloadListener;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OnboardingActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "com.simaskuprelis.kag_androidapp.user_id";

    @BindView(R.id.node_list)
    RecyclerView mNodeList;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    private List<Node> mNodes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        setTitle(R.string.select_node);

        FirebaseDatabaseApi.preload(new PreloadListener() {
            @Override
            public void onLoad() {
                FirebaseDatabaseApi.getNodes(null, new NodesListener() {
                    @Override
                    public void onLoad(List<Node> nodes) {
                        mNodes = nodes;
                        setupAdapter();
                    }

                    @Override
                    public void onFail(Exception e) {
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    private void setupAdapter() {
        mLoadingIndicator.setVisibility(View.GONE);
        mNodeList.setLayoutManager(new LinearLayoutManager(this));
        mNodeList.setAdapter(new NodeAdapter(mNodes, new NodeClickListener() {
            @Override
            public void onClick(Node n) {
                sendResult(n.getId());
            }
        }));
    }

    private void sendResult(String id) {
        Intent i = new Intent();
        i.putExtra(EXTRA_USER_ID, id);
        setResult(RESULT_OK, i);
        finish();
    }
}
