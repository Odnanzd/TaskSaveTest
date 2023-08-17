package com.example.tasksave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class activity_splash_screen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(activity_splash_screen.this, activity_fingerprint.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}