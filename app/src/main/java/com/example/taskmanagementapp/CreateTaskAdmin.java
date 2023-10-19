package com.example.taskmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CreateTaskAdmin extends AppCompatActivity {

    TextView task_description;
    Spinner spinner;
    Button  btn_task_assign;
    DatePicker datePicker;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_admin);

        task_description = findViewById(R.id.task_description);
        spinner = findViewById(R.id.spinner);
        datePicker = findViewById(R.id.datePicker);
        btn_task_assign = findViewById(R.id.btn_task_assign);

        btn_task_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("adminTasks");
                String taskDescription = task_description.getText().toString().trim();
                String selectedItem = spinner.getSelectedItem().toString().trim();
                int dueDate = datePicker.getYear();

                if(taskDescription.isEmpty() || selectedItem.isEmpty()){
                    Toast.makeText(CreateTaskAdmin.this, "all need to be field", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    AdminTaskModel model = new AdminTaskModel(taskDescription,selectedItem,dueDate);
                    reference.child(taskDescription).setValue(model);
                    Toast.makeText(CreateTaskAdmin.this, "Succesfully Assigned", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateTaskAdmin.this,MainActivity.class);
                    intent.putExtra("taskDescription",taskDescription);
                    intent.putExtra("selectedItem",selectedItem);
                    intent.putExtra("dueDate",dueDate);

                    startActivity(intent);
                    finish();

                }

            }
        });

    }
}