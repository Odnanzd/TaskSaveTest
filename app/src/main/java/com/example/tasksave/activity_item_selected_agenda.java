package com.example.tasksave;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class activity_item_selected_agenda extends AppCompatActivity {
    TextView descricaoTextView;
    TextView tituloTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected_agenda);
        tituloTextView = findViewById(R.id.titulo_text_view);
        descricaoTextView = findViewById(R.id.descricao_text_view);

        // Recebe os extras da Intent
        String titulo = getIntent().getStringExtra("titulo");
        String descricao = getIntent().getStringExtra("descricao");

        // Exibindo os dados nos TextViews
        tituloTextView.setText(titulo);
        descricaoTextView.setText(descricao);
        }
    }