package com.simaskuprelis.kag_androidapp.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {

    private List<Node> mNodes;
    private NodeClickListener mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.node_name)
        TextView mName;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public NodeAdapter(List<Node> nodes, @Nullable NodeClickListener listener) {
        mNodes = nodes;
        mListener = listener;
    }

    @Override
    public NodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_node, parent, false));
    }

    @Override
    public void onBindViewHolder(NodeAdapter.ViewHolder holder, int position) {
        final Node n = mNodes.get(position);
        holder.mName.setText(n.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onClick(n);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }
}
