package com.simaskuprelis.kag_androidapp.api;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseApi {
    private static final String TAG = "FirebaseDatabaseApi";

    private static final String NODE_GROUPS = "node_groups";
    private static final String GROUP_NODES = "group_nodes";
    private static final String NODES = "nodes";
    private static final String GROUPS = "groups";
    private static final String TIMES = "times";

    public static void getNode(String id, final FirebaseListener<Node> listener) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(NODES).child(id);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Node n = dataSnapshot.getValue(Node.class);
                n.setId(dataSnapshot.getKey());
                listener.onLoad(n);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleError(databaseError, listener);
            }
        });
    }

    public static void getNodeGroups(String id, final FirebaseListener<List<Group>> listener) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(NODE_GROUPS).child(id);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Group> groups = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Group g = ds.getValue(Group.class);
                    g.setId(ds.getKey());
                    groups.add(g);
                }
                listener.onLoad(groups);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleError(databaseError, listener);
            }
        });
    }

    public static void getAllNodes(final FirebaseListener<List<Node>> listener) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(NODES);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Node> nodes = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Node n = ds.getValue(Node.class);
                    n.setId(ds.getKey());
                    nodes.add(n);
                }
                listener.onLoad(nodes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleError(databaseError, listener);
            }
        });
    }

    public static void getGroup(String id, final FirebaseListener<Group> listener) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(GROUPS).child(id);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group g = dataSnapshot.getValue(Group.class);
                g.setId(dataSnapshot.getKey());
                listener.onLoad(g);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleError(databaseError, listener);
            }
        });
    }

    public static void getGroupNodes(String id, final FirebaseListener<List<Node>> listener) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(GROUP_NODES).child(id);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Node> nodes = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Node n = ds.getValue(Node.class);
                    n.setId(ds.getKey());
                    nodes.add(n);
                }
                listener.onLoad(nodes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleError(databaseError, listener);
            }
        });
    }

    public static void getTimes(final FirebaseListener<List<Integer>> listener) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(TIMES);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Integer> items = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Integer i = ds.getValue(Integer.class);
                    items.add(i);
                }
                listener.onLoad(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleError(databaseError, listener);
            }
        });
    }

    private static void handleError(DatabaseError e, FirebaseListener l) {
        FirebaseCrash.logcat(Log.ERROR, TAG, e.toString());
        l.onFail(e.toException());
    }
}
