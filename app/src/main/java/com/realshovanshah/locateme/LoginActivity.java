package com.realshovanshah.locateme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    
    EditText email_l, pass_l;
    Button login_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        email_l= findViewById(R.id.email_l);
        pass_l= findViewById(R.id.pass_l);
        
        login_btn= findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String email = email_l.getText().toString().trim();
        String password = pass_l.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        } else if(email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please fill out all the field.", Toast.LENGTH_SHORT).show();
        }else {
            userLogin(email, password);
        }
    }

    private void userLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "LoginSuccessful", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Incorrect Email/Password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}