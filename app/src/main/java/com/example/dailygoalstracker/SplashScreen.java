package com.example.dailygoalstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentuser== null) {
                    startActivity(new Intent(SplashScreen.this, LoginPageActivity.class));
                }
                else{
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));

                }

                finish();

            }
        },1500);
    }
}