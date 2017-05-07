package com.simaskuprelis.kag_androidapp.api;

import android.support.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;
import com.simaskuprelis.kag_androidapp.api.listener.ApiReferenceListener;
import com.simaskuprelis.kag_androidapp.api.listener.GroupsLoadedListener;
import com.simaskuprelis.kag_androidapp.api.listener.NodesLoadedListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.lang.ref.WeakReference;
import java.util.Map;

public class FirebaseRealtimeApi {

    public static void getNodes(NodesLoadedListener listener, @Nullable final Integer category) {
        FirebaseDatabase.getInstance().getReference("nodes")
                .addListenerForSingleValueEvent(
                        new ApiReferenceListener<Node, NodesLoadedListener>(Node.class,
                                new WeakReference<>(listener)) {

                            @Override
                            public void onFailed(NodesLoadedListener listener, Exception ex) {
                                listener.onNodesLoadingFailed(ex);
                            }

                            @Override
                            public void onLoaded(Map<String, Node> items, NodesLoadedListener listener) {
                                listener.onNodesLoaded(items);
                            }

                            @Override
                            public boolean where(Node item) {
                                return category == null || category.equals(item.getCategory());
                            }
                        });
    }

    public static void getGroups(GroupsLoadedListener listener) {
        FirebaseDatabase.getInstance().getReference("groups")
                .addListenerForSingleValueEvent(
                        new ApiReferenceListener<Group, GroupsLoadedListener>(Group.class,
                                new WeakReference<>(listener)) {

                            @Override
                            public void onFailed(GroupsLoadedListener listener, Exception ex) {
                                listener.onGroupsLoadingFailed(ex);
                            }

                            @Override
                            public void onLoaded(Map<String, Group> items, GroupsLoadedListener listener) {
                                listener.onGroupsLoaded(items);
                            }
                        });
    }
}
