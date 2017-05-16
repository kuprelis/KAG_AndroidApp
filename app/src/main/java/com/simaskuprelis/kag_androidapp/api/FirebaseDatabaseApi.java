package com.simaskuprelis.kag_androidapp.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simaskuprelis.kag_androidapp.api.listener.GroupsListener;
import com.simaskuprelis.kag_androidapp.api.listener.NodesListener;
import com.simaskuprelis.kag_androidapp.api.listener.SingleGroupListener;
import com.simaskuprelis.kag_androidapp.api.listener.SingleNodeListener;
import com.simaskuprelis.kag_androidapp.api.listener.TimesListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseDatabaseApi {
    private static final String NODES = "nodes";
    private static final String GROUPS = "groups";
    private static final String TIMES = "times";

    public static void getNode(final SingleNodeListener listener, @NonNull String id) {
        FirebaseDatabase.getInstance().getReference(NODES).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot ds = (DataSnapshot) dataSnapshot.getChildren().iterator();
                        listener.onLoad(ds.getValue(Node.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Exception e = databaseError.toException();
                        FirebaseCrash.report(e);
                        listener.onFail(e);
                    }
                });
    }

    public static void getNodes(final NodesListener listener, @Nullable final Integer category) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference(NODES)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<Node> items = new ArrayList<>();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Node n = ds.getValue(Node.class);
                                    if (category == null || n.getCategory() == category) {
                                        items.add(n);
                                    }
                                }
                                Collections.sort(items);
                                listener.onLoad(items);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Exception e = databaseError.toException();
                                FirebaseCrash.report(e);
                                listener.onFail(e);
                            }
                        });
            }
        });
    }

    public static void getGroup(final SingleGroupListener listener, @NonNull String id) {
        FirebaseDatabase.getInstance().getReference(GROUPS).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot ds = (DataSnapshot) dataSnapshot.getChildren().iterator();
                        listener.onLoad(ds.getValue(Group.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Exception e = databaseError.toException();
                        FirebaseCrash.report(e);
                        listener.onFail(e);
                    }
                });
    }

    public static void getGroups(final GroupsListener listener, @NonNull final String name) {
        FirebaseDatabase.getInstance().getReference(GROUPS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Group> items = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Group g = ds.getValue(Group.class);
                            if (g.getName().equals(name)) items.add(g);
                        }
                        listener.onLoad(items);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Exception e = databaseError.toException();
                        FirebaseCrash.report(e);
                        listener.onFail(e);
                    }
                });
    }

    public static void getTimes(final TimesListener listener) {
        FirebaseDatabase.getInstance().getReference(TIMES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Integer> items = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            items.add(ds.getValue(Integer.class));
                        }
                        listener.onLoad(items);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Exception e = databaseError.toException();
                        FirebaseCrash.report(e);
                        listener.onFail(e);
                    }
                });
    }
}
