package com.example.taskmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditUserTasks extends AppCompatActivity {

    private EditText editTextTaskTitle, editTextTaskDescription;
    private TextView textViewDueDate;
    private DatePicker datePicker;
    private Button btnUpdateTask;
    private FirebaseFirestore db;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_tasks);

        taskId = getIntent().getStringExtra("taskId");

        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        datePicker = findViewById(R.id.datePicker);
        btnUpdateTask = findViewById(R.id.btnUpdateTask);

        db = FirebaseFirestore.getInstance();
        loadTaskDetails();

        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle update button click
                updateTask();
            }
        });
    }

    private void loadTaskDetails() {
        // Load existing task details from Firestore based on taskId
        DocumentReference taskRef = db.collection("Tasks").document(taskId);
        taskRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Task details found, populate UI fields
                            String taskTitle = documentSnapshot.getString("title");
                            String taskDescription = documentSnapshot.getString("description");
                            long dueDate = documentSnapshot.getLong("dueDate");

                            // Display task details in the UI
                            editTextTaskTitle.setText(taskTitle);
                            editTextTaskDescription.setText(taskDescription);

                            // Format and display due date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            String formattedDate = dateFormat.format(new Date(dueDate));
                            textViewDueDate.setText("Due Date: " + formattedDate);
                        } else {
                            // Task details not found
                            Toast.makeText(EditUserTasks.this, "Task details not found", Toast.LENGTH_SHORT).show();
                            finish(); // Close this activity if details not found
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(EditUserTasks.this, "Failed to load task details", Toast.LENGTH_SHORT).show();
                        finish(); // Close this activity in case of failure
                    }
                });
    }

    private void updateTask() {
        String updatedTitle = editTextTaskTitle.getText().toString().trim();
        String updatedDescription = editTextTaskDescription.getText().toString().trim();

        // Get the selected date from DatePicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        // Create a Calendar instance to set the due date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        long dueDateInMillis = calendar.getTimeInMillis();

        // Update the task in Firestore
        DocumentReference taskRef = db.collection("Tasks").document(taskId);
        taskRef.update("title", updatedTitle, "description", updatedDescription, "dueDate", dueDateInMillis)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Task updated successfully
                        Toast.makeText(EditUserTasks.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close this activity after updating
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(EditUserTasks.this, "Failed to update task", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}