package com.example.dailygoalstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginBtn;
    private ProgressBar progressBar;
    private TextView createAccountText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);
        loginBtn = findViewById(R.id.login_button);
        createAccountText = findViewById(R.id.CreateAccount);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPageActivity.this, SignUpActivity.class));
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.d("LoginPageActivity", "Email: " + email);
        Log.d("LoginPageActivity", "Password: " + password);

        if (!validateData(email, password)) {
            return;
        }

        loginInFirebase(email, password);
    }

    private void loginInFirebase(String email, String password) {
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginPageActivity.this, MainActivity.class));
//                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
//                        startActivity(new Intent(LoginPageActivity.this, MainActivity.class));
//                        finish();
//                    } else {
//                        Toast.makeText(LoginPageActivity.this, "Email is not verified", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Toast.makeText(LoginPageActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateData(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length() < 8) {
            passwordEditText.setError("Password is invalid");
            return false;
        }
        return true;
    }
}
