package com.realshovanshah.locateme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.realshovanshah.locateme.models.User;

public class RegisterActivity extends AppCompatActivity {

    EditText email_r, fname_r, phone_r, pass_r, confirm_pass_r;
    Button signup, loginBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_r= findViewById(R.id.email_r);
        fname_r= findViewById(R.id.fname_r);
        phone_r= findViewById(R.id.number_r);
        pass_r= findViewById(R.id.pass_r);
        confirm_pass_r= findViewById(R.id.confirm_pass_r);

        loginBtn = findViewById(R.id.login_btn);
        signup= findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();

        db= FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginActivity();
            }
        });

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
        final String email = email_r.getText().toString().trim();
        final String fname = fname_r.getText().toString().trim();
        final String phone = phone_r.getText().toString().trim();
        final String pass = pass_r.getText().toString().trim();
        String confirm_pass = confirm_pass_r.getText().toString().trim();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass) && TextUtils.isEmpty(fname) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(confirm_pass)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fname)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirm_pass)) {
            Toast.makeText(this, "Confirm password field is empty", Toast.LENGTH_SHORT).show();
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
                                String userId = FirebaseAuth.getInstance().getUid();
                                String username = email.substring(0, email.indexOf("@"));
                                DocumentReference dbUsers = db.collection("users").document(userId);
                                User user = new User(userId, username, email, fname, phone);

                                dbUsers.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Successfully signed up.", Toast.LENGTH_SHORT).show();
                                            loginActivity();
                                        }else{
                                            Toast.makeText(RegisterActivity.this, "Failed to create an account. Please try again.", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Whoops! Something went wrong.", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }
    }

    private void loginActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}