package com.example.taskmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class admin extends AppCompatActivity {

    Button addTaskButton, assignTaskButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addTaskButton = findViewById(R.id.addTaskButton);
        assignTaskButton = findViewById(R.id.assignTaskButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to a new screen to add a task
                startActivity(new Intent(admin.this, CreateTaskAdmin.class));
            }
        });
    }


    public void logoutAdmin(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }


}