package com.example.taskmanagementapp;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

public class UserUtility {

    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;

    public void user_login(String email, String password, Context context){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login success, update UI or navigate to next activity
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("userId", userId);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            // Now you can navigate to the next activity or perform other actions
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(context, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            // Finish the current activity
                            ((Activity) context).finish();
                        }
                    }
                });
    }

    public void user_logout(Context context){
        FirebaseAuth.getInstance().signOut();
        context.startActivity(new Intent(context, login.class));
        ((Activity)context).finish();
    }

    public void user_signUp(Context context, String email, String password, String firstname, String lastname){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI or navigate to next activity
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            boolean isAdmin;
                            // You might want to store additional user details in the database at this point
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

                            if(email=="kuber@gmail.com" || email=="jay@gmail.com" || email=="harshil@gmail.com"){
                                isAdmin = true;
                            }else {
                                isAdmin = false;
                            }

                            usersRef.child(userId).setValue(new UserModel(userId, firstname, lastname, email, password, isAdmin));

                            // Now you can navigate to the next activity or perform other actions
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(context.getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }













}
