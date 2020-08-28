package com.realshovanshah.locateme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.realshovanshah.locateme.R;
import com.realshovanshah.locateme.models.Users;

public class RegisterActivity extends AppCompatActivity {

    EditText username_r, email_r, fname_r, phone_r, pass_r, confirm_pass_r;
    Button signup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username_r= findViewById(R.id.username_r);
        email_r= findViewById(R.id.email_r);
        fname_r= findViewById(R.id.fname_r);
        phone_r= findViewById(R.id.number_r);
        pass_r= findViewById(R.id.pass_r);
        confirm_pass_r= findViewById(R.id.confirm_pass_r);

        signup= findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();

        db= FirebaseFirestore.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signup() {
        final String username = username_r.getText().toString().trim();
        final String email = email_r.getText().toString().trim();
        final String fname = fname_r.getText().toString().trim();
        final String phone = phone_r.getText().toString().trim();
        final String pass = pass_r.getText().toString().trim();
        String confirm_pass = confirm_pass_r.getText().toString().trim();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass) && TextUtils.isEmpty(fname) && TextUtils.isEmpty(username) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(confirm_pass)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(fname)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(confirm_pass)) {
            Toast.makeText(this, "Confirm password field is empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
        } else if (!pass.equals(confirm_pass)) {
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                CollectionReference dbUsers = db.collection("users");
                                Users user = new Users(username, email, fname, phone, pass);

                                dbUsers.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(RegisterActivity.this, "Successfully signed up.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Failed to create an account. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // Sign in success, update UI with the signed-in user's information

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Whoops! Something went wrong.", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }
    }
}