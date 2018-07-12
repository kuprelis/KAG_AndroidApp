package com.simaskuprelis.kag_androidapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.entity.NodeListCategory;
import com.simaskuprelis.kag_androidapp.entity.NodeListItem;
import com.simaskuprelis.kag_androidapp.entity.Lesson;
import com.simaskuprelis.kag_androidapp.entity.Node;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MultiCatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NodeListItem> items;

    /**
     * @param items List of items sorted by category
     */
    public MultiCatAdapter(List<NodeListItem> items) {
        this.items = items;
        addCategories();
    }

    private void addCategories() {
        int count = 0;
        for (int i = items.size() - 1; i >= 0; i--) {
            count++;
            int type = items.get(i).getType();
            if (i == 0 || items.get(i - 1).getType() != type) {
                items.add(i, new NodeListCategory(type, count));
                count = 0;
            }
        }
    }

    public void notifyDataChange() {
        addCategories();
        notifyDataSetChanged();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.node_name)
        TextView name;
        @BindView(R.id.node_class)
        TextView classId;

        ItemHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    static class CategoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.category_name)
        TextView name;

        CategoryHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        if (viewType == NodeListItem.TYPE_CATEGORY) {
            return new CategoryHolder(li.inflate(R.layout.list_item_category, parent, false));
        }
        return new ItemHolder(li.inflate(R.layout.list_item_node, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NodeListItem item = items.get(position);
        int type = item.getType();
        if (type == NodeListItem.TYPE_CATEGORY) {
            CategoryHolder ch = (CategoryHolder) holder;

            NodeListCategory nlc = (NodeListCategory) item;
            String text = nlc.getName(holder.itemView.getContext());
            if (nlc.getCount() > 3) text += " • " + nlc.getCount();
            ch.name.setText(text);
        } else if (type == NodeListItem.TYPE_LESSON) {
            ItemHolder ih = (ItemHolder) holder;
            Lesson l = (Lesson) item;

            String text;
            Context c = holder.itemView.getContext();
            switch (l.getDay()) {
                case 1: text = c.getString(R.string.monday); break;
                case 2: text = c.getString(R.string.tuesday); break;
                case 3: text = c.getString(R.string.wednesday); break;
                case 4: text = c.getString(R.string.thursday); break;
                case 5: text = c.getString(R.string.friday); break;
                default: text = ""; break;
            }

            text += ", " + l.getNum();

            if (l.getRoom() != null) text += ", " + l.getRoom();
            ih.name.setText(text);
            ih.classId.setVisibility(View.GONE);
        } else {
            ItemHolder ih = (ItemHolder) holder;
            final Node n = (Node) item;

            ih.name.setText(n.getName());
            if (n.getCat() == Node.STUDENT) {
                String text = n.getClassId();
                if (text != null) {
                    ih.classId.setVisibility(View.VISIBLE);
                    ih.classId.setText(n.getClassId());
                } else {
                    ih.classId.setVisibility(View.GONE);
                }
            } else {
                ih.classId.setVisibility(View.GONE);
            }
            ih.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(n);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
