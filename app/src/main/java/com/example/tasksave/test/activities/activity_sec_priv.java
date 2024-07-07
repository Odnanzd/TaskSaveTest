package com.example.tasksave.test.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasksave.R;

public class activity_sec_priv extends AppCompatActivity {


    private ImageView imageViewBack;
    private LinearLayout linearLayoutBiometria, linearLayoutSenha, linearLayoutPermissao;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(activity_sec_priv.this, activity_config.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_priv);

        imageViewBack = findViewById(R.id.imageView4);
        linearLayoutBiometria = findViewById(R.id.linearLayoutSeg);
        linearLayoutPermissao = findViewById(R.id.linearLayoutNot);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_sec_priv.this, activity_config.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        linearLayoutBiometria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_sec_priv.this, activity_sec_fingerprint.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        linearLayoutPermissao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_sec_priv.this, activity_sec_permissao.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }
}