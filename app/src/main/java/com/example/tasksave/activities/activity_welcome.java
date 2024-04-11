package com.example.tasksave.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tasksave.R;

public class activity_welcome extends AppCompatActivity {

    Button buttonEntrar;
    Button buttonCadastrar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonEntrar = findViewById(R.id.buttonEntrar);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_welcome.this, activity_login.class);
                startActivity(intent);
            }
        });
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_welcome.this, activity_cadastro.class);
                startActivity(intent);
            }
        });

    }
}