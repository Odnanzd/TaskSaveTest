package com.example.tasksave.test.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tasksave.R;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;

public class ActivityConfg extends AppCompatActivity {
    private ImageView imageViewBack;
    private LinearLayout linearLayout, linearLayoutSecPriv, linearLayoutSobre, linearLayoutNotifica,
            linearLayoutAparencia, linearLayoutPainelADMTodo, linearLayoutPainelADM;
    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {

        Intent intent = new Intent(ActivityConfg.this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        imageViewBack = findViewById(R.id.imageView4);
        linearLayout = findViewById(R.id.linearLayout2);
        linearLayoutSecPriv = findViewById(R.id.linearLayoutSeg);
        linearLayoutSobre = findViewById(R.id.linearLayoutSobre);
        linearLayoutNotifica = findViewById(R.id.linearLayoutNot);
        linearLayoutAparencia = findViewById(R.id.linearLayoutAparencia);
        linearLayoutPainelADM = findViewById(R.id.linearLayoutPainelADM);
        linearLayoutPainelADMTodo = findViewById(R.id.linearLayoutPainelADMTodo);

        permissaoUsuario();

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ActivityConfg.this, ActivityMain.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityConfg.this, ActivityConfgPerfil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        linearLayoutSecPriv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityConfg.this, ActivityConfgSecPriv.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        linearLayoutSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityConfg.this, ActivityConfgSobre.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        linearLayoutNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityConfg.this, ActivityConfgNotificacao.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        linearLayoutAparencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityConfg.this, ActivityConfgAparencia.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

    }
    public void permissaoUsuario() {

        SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityConfg.this);

        if (sharedPreferencesUsuario.getUsuarioCargo()==2) {
            linearLayoutPainelADMTodo.setVisibility(View.VISIBLE);
        }
    }
}