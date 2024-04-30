package com.example.tasksave.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tasksave.R;

public class activity_confg_perfil extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity_confg_perfil.this, activity_config.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nomeExibicao();
    }

    LinearLayout linearLayout;
    TextView textView;
    ImageView imageViewback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_perfil);
        linearLayout = findViewById(R.id.linearLayout2);
        textView = findViewById(R.id.TextViewNomeCompleto);
        imageViewback = findViewById(R.id.imageViewBack);

        nomeExibicao();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_confg_perfil.this, activity_confg_perfil_nome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_confg_perfil.this, activity_config.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }
    public void nomeExibicao() {

        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvarUser", Context.MODE_PRIVATE);
        String valorNome = sharedPrefs2.getString("userString", "");

        textView.setText(valorNome);

    }
}