package com.simaskuprelis.kag_androidapp.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.adapter.LessonAdapter;
import com.simaskuprelis.kag_androidapp.adapter.NodeAdapter;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.listener.NodesListener;
import com.simaskuprelis.kag_androidapp.entity.Group;
import com.simaskuprelis.kag_androidapp.entity.Lesson;
import com.simaskuprelis.kag_androidapp.entity.Node;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends AppCompatActivity {
    public static final String EXTRA_GROUP = "com.simaskuprelis.kag_androidapp.group";

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

        final Group g = (Group) getIntent().getExtras().get(EXTRA_GROUP);
        if (g == null) return;

        final Context context = this;
        FirebaseDatabaseApi.getNodes(g.getNodes(), new NodesListener() {
            @Override
            public void onLoad(List<Node> nodes) {
                List<Node> teachers = new ArrayList<>();
                List<Node> rooms = new ArrayList<>();
                List<Node> students = new ArrayList<>();

                for (Node n : nodes) {
                    switch (n.getCategory()) {
                        case 1: teachers.add(n); break;
                        case 2: rooms.add(n); break;
                        case 3: students.add(n); break;
                        default: break;
                    }
                }

                setupRecycler(mTeachers, new NodeAdapter(teachers));
                setupRecycler(mStudents, new NodeAdapter(students));
                setupRecycler(mLessons, new LessonAdapter(g.getLessons(), context));

                if (rooms.size() != 0) setupRecycler(mRooms, new NodeAdapter(rooms));
                else mRoomDisplay.setVisibility(View.GONE);
            }

            @Override
            public void onFail(Exception e) {
            }
        });


    }

    private void setupRecycler(RecyclerView rv, RecyclerView.Adapter adapter) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }
}