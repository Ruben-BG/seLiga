package com.ruben.project.seliga.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.ruben.project.seliga.MainActivity;
import com.ruben.project.seliga.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}