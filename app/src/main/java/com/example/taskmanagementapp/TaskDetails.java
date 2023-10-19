package com.example.taskmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDetails extends AppCompatActivity {
    private TextView textViewTaskTitle, textViewTaskDescription, textViewDueDate;
    private Button btnEditTask, btnDeleteTask;
    private FirebaseFirestore db;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");

        Log.d("TaskDetails", "Received taskId: " + taskId);


        textViewTaskTitle = findViewById(R.id.textViewTaskTitle);
        textViewTaskDescription = findViewById(R.id.textViewTaskDescription);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        btnEditTask = findViewById(R.id.btnEditTask);
        btnDeleteTask = findViewById(R.id.btnDeleteTask);

        db = FirebaseFirestore.getInstance();

        loadTaskDetails();

        btnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click (open EditTaskActivity)
                Intent editIntent = new Intent(TaskDetails.this, EditUserTasks.class);
                editIntent.putExtra("taskId", taskId);
                startActivity(editIntent);
            }
        });

        btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
                deleteTask();
            }
        });
    }

    private void loadTaskDetails() {

        Log.d("TaskDetails", "Loading task details for taskId: " + taskId);

        Task<DocumentSnapshot> documentSnapshotTask = db.collection("Tasks").document(taskId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("TaskDetails", "DocumentSnapshot exists: " + documentSnapshot.exists());

                        if (documentSnapshot.exists()) {
                            String taskTitle = documentSnapshot.getString("title");
                            String taskDescription = documentSnapshot.getString("description");
                            long dueDate = documentSnapshot.getLong("dueDate");

                            // Display task details in the UI
                            textViewTaskTitle.setText(taskTitle);
                            textViewTaskDescription.setText(taskDescription);

                            // Format and display due date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            String formattedDate = dateFormat.format(new Date(dueDate));
                            textViewDueDate.setText("Due Date: " + formattedDate);
                        } else {
                            Log.d("TaskDetails", "DocumentSnapshot does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.e("TaskDetails", "Failed to load task details", e);
                        Toast.makeText(TaskDetails.this, "Failed to load task details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteTask() {
        // Delete the task from Firestore
        db.collection("Tasks").document(taskId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Task deleted successfully
                        Toast.makeText(TaskDetails.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                        // Add any other logic after deleting
                        finish(); // Close this activity after deleting
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(TaskDetails.this, "Failed to delete task", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}