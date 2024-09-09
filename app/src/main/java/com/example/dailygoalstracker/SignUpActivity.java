package com.example.dailygoalstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button signUpBtn;
    private ProgressBar progressBar;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);
        signUpBtn = findViewById(R.id.signup_button);
        loginText = findViewById(R.id.loginText);

        signUpBtn.setOnClickListener(v -> signUpActivity());
        loginText.setOnClickListener(v -> finish());
    }

    private void signUpActivity() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email, password);
        if (!isValidated) {
            return;
        }
        signUpActivityInFirebase(email, password);
    }

    private void signUpActivityInFirebase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            Utility.showToast(SignUpActivity.this, "Your account is created, check email for verification.");

                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            Utility.showToast(SignUpActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            signUpBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            signUpBtn.setVisibility(View.VISIBLE);
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
