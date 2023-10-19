package com.example.taskmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class admin extends AppCompatActivity {

    Button addTaskButton, assignTaskButton;
    ListView taskListView;

    FirebaseDatabase database;
    DatabaseReference taskReference;
    ArrayAdapter<String> taskAdapter;
    List<String> taskKeys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addTaskButton = findViewById(R.id.addTaskButton);
        assignTaskButton = findViewById(R.id.assignTaskButton);
        taskListView = findViewById(R.id.taskListView);

        database = FirebaseDatabase.getInstance();
        taskReference = database.getReference("adminTasks");

        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        taskListView.setAdapter(taskAdapter);

        taskKeys = new ArrayList<>();

        loadTasks();

        // Handle item click in the ListView
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected task key
                String selectedTaskKey = taskKeys.get(position);

                // Start the TaskDetails activity with the selected task key
                Intent intent = new Intent(admin.this, AdminTaskDetail.class);
                intent.putExtra("taskKey", selectedTaskKey);
                startActivity(intent);
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to a new screen to add a task
                startActivity(new Intent(admin.this, CreateTaskAdmin.class));
            }
        });
    }

    private void loadTasks() {
        taskReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskAdapter.clear();
                taskKeys.clear();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    AdminTaskModel task = taskSnapshot.getValue(AdminTaskModel.class);
                    if (task != null) {
                        taskAdapter.add(task.getTaskDescription());
                        taskKeys.add(taskSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle an error in reading from the database
            }
        });
    }

        public void logoutAdmin(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }

    }
