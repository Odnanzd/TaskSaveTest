package com.example.tasksave.test.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tasksave.R;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;

public class ActivityConfgPerfilEmail extends AppCompatActivity {

    private ImageView imageViewBack;
    private TextView textViewMail;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_perfil_email);

        imageViewBack = findViewById(R.id.imageView4);
        textViewMail = findViewById(R.id.textViewEmail);

        selecionaEmail();

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityConfgPerfilEmail.this, ActivityConfgPerfil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

    }
    public void selecionaEmail() {

        SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityConfgPerfilEmail.this);

        textViewMail.setText(sharedPreferencesUsuario.getEmailLogin());

    }
}