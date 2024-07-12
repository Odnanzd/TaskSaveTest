package com.example.tasksave.test.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    LinearLayout linearLayout, linearLayoutSair;
    TextView textView;
    ImageView imageViewback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_perfil);
        linearLayout = findViewById(R.id.linearLayout2);
        linearLayoutSair = findViewById(R.id.linearLayoutSair);

        textView = findViewById(R.id.TextViewNomeCompleto);
        imageViewback = findViewById(R.id.imageView4);

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
        linearLayoutSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void nomeExibicao() {

        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvarUser", Context.MODE_PRIVATE);
        String valorNome = sharedPrefs2.getString("userString", "");

        textView.setText(valorNome);

    }
    public void attFingerprintPositivo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity_confg_perfil.this);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setMessage("Deseja confirmar as alterações e sair? ");
        builder.setNegativeButton("Não", (dialog, which) -> {

        });
        builder.setPositiveButton("Sim", (dialog, which) -> {

            SharedPreferences sharedPreferences1 = getSharedPreferences("ArquivoTextoAPP", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.clear();
            editor1.apply();



        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}