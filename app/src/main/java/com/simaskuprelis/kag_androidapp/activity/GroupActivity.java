package com.simaskuprelis.kag_androidapp.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.adapter.LessonAdapter;
import com.simaskuprelis.kag_androidapp.adapter.NodeAdapter;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.FirebaseListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Node;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends AppCompatActivity {
    public static final String EXTRA_GROUP = "com.simaskuprelis.kag_androidapp.group";
    public static final String RESULT_GROUP_NODE_ID = "com.simaskuprelis.kag_androidapp.group_node_id";

    @BindView(R.id.room_display)
    LinearLayout mRoomDisplay;
    @BindView(R.id.teacher_list)
    RecyclerView mTeachers;
    @BindView(R.id.room_list)
    RecyclerView mRooms;
    @BindView(R.id.lesson_list)
    RecyclerView mLessons;
    @BindView(R.id.student_list)
    RecyclerView mStudents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Group g = (Group) getIntent().getExtras().get(EXTRA_GROUP);
        if (g == null) return;

        setTitle(g.getName());
        Utils.setupRecycler(mLessons, this, new LessonAdapter(g.getLessons(), this));

        final Context context = this;
        FirebaseDatabaseApi.getGroupNodes(g.getId(), new FirebaseListener<List<Node>>() {
            @Override
            public void onLoad(List<Node> obj) {
                List<Node> teachers = new ArrayList<>();
                List<Node> rooms = new ArrayList<>();
                List<Node> students = new ArrayList<>();

                for (Node n : obj) {
                    switch (n.getCat()) {
                        case Node.TEACHER: teachers.add(n); break;
                        case Node.ROOM: rooms.add(n); break;
                        case Node.STUDENT: students.add(n); break;
                        default: break;
                    }
                }

                Utils.setupRecycler(mTeachers, context, new NodeAdapter(teachers));
                Utils.setupRecycler(mStudents, context, new NodeAdapter(students));
                if (rooms.size() != 0) {
                    Utils.setupRecycler(mRooms, context, new NodeAdapter(rooms));
                } else {
                    mRoomDisplay.setVisibility(View.GONE);
                }
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
        i.putExtra(RESULT_GROUP_NODE_ID, n.getId());
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
