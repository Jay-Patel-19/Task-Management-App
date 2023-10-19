package com.example.taskmanagementapp;

import java.util.Date;

public class AdminTaskModel {

    private String taskDescription;
    private String selectedItem;
    private int dueDate;



    public AdminTaskModel(String taskDescription, String selectedItem, int dueDate) {
        this.taskDescription = taskDescription;
        this.selectedItem = selectedItem;
        this.dueDate = dueDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }
}