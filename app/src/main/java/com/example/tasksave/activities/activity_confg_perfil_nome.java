package com.example.tasksave.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.tasksave.R;

public class activity_confg_perfil_nome extends AppCompatActivity {


    FrameLayout frameLayout;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(activity_confg_perfil_nome.this, activity_confg_perfil.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_perfil_nome);

        frameLayout = findViewById(R.id.framelayout1);
        frameLayout.setClickable(false);


    }
}