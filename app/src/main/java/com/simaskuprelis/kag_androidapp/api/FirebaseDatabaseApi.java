package com.simaskuprelis.kag_androidapp.api;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simaskuprelis.kag_androidapp.api.listener.GroupsListener;
import com.simaskuprelis.kag_androidapp.api.listener.NodesListener;
import com.simaskuprelis.kag_androidapp.api.listener.TimesListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseDatabaseApi {
    private static final String TAG = "FirebaseDatabaseApi";

    private static final String NODES = "nodes";
    private static final String GROUPS = "groups";
    private static final String TIMES = "times";

    public static void getNodes(@Nullable final List<String> ids, final NodesListener listener) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference(NODES);
                final List<Node> nodes = new ArrayList<>();

                if (ids != null) for (String id : ids) {
                    dr.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Node n = dataSnapshot.getValue(Node.class);
                            nodes.add(n);
                            if (nodes.size() == ids.size()) {
                                Collections.sort(nodes);
                                listener.onLoad(nodes);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            FirebaseCrash.logcat(Log.ERROR, TAG, databaseError.toString());
                            listener.onFail(databaseError.toException());
                        }
                    });
                } else {
                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Node n = ds.getValue(Node.class);
                                nodes.add(n);
                            }
                            Collections.sort(nodes);
                            listener.onLoad(nodes);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            FirebaseCrash.logcat(Log.ERROR, TAG, databaseError.toString());
                            listener.onFail(databaseError.toException());
                        }
                    });
                }
            }
        });
    }

    public static void getGroups(@Nullable final List<String> ids, final GroupsListener listener) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference(GROUPS);
                final List<Group> groups = new ArrayList<>();

                if (ids != null) for (String id : ids) {
                    dr.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Group g = dataSnapshot.getValue(Group.class);
                            groups.add(g);
                            if (groups.size() == ids.size()) {
                                listener.onLoad(groups);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            FirebaseCrash.logcat(Log.ERROR, TAG, databaseError.toString());
                            listener.onFail(databaseError.toException());
                        }
                    });
                } else {
                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Group g = ds.getValue(Group.class);
                                groups.add(g);
                            }
                            listener.onLoad(groups);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            FirebaseCrash.logcat(Log.ERROR, TAG, databaseError.toString());
                            listener.onFail(databaseError.toException());
                        }
                    });
                }
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
                        FirebaseCrash.logcat(Log.ERROR, TAG, databaseError.toString());
                        listener.onFail(databaseError.toException());
                    }
                });
    }
}
