package com.example.tasksave.test.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tasksave.R;
import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.servicesreceiver.AlarmScheduler;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesConfg;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;

import java.util.ArrayList;

public class ActivityConfgPerfil extends AppCompatActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityConfgPerfil.this, ActivityConfg.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nomeExibicao();
    }

    LinearLayout linearLayout, linearLayoutSair, linearLayoutMail;
    TextView textView;
    ImageView imageViewback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_perfil);
        linearLayout = findViewById(R.id.linearLayout2);
        linearLayoutSair = findViewById(R.id.linearLayoutSair);
        linearLayoutMail = findViewById(R.id.linearLayoutEmail);

        textView = findViewById(R.id.TextViewNomeCompleto);
        imageViewback = findViewById(R.id.imageView4);

        nomeExibicao();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityConfgPerfil.this, ActivityConfgPerfilNome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityConfgPerfil.this, ActivityConfg.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        linearLayoutSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attFingerprintPositivo();
            }
        });

        linearLayoutMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityConfgPerfil.this, ActivityConfgPerfilEmail.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }
    public void nomeExibicao() {

        SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityConfgPerfil.this);
        String valorNome = sharedPreferencesUsuario.getUsuarioLogin();

        textView.setText(valorNome);

    }
    public void attFingerprintPositivo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityConfgPerfil.this);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setMessage("Deseja confirmar as alterações e sair? ");
        builder.setNegativeButton("Não", (dialog, which) -> {

        });
        builder.setPositiveButton("Sim", (dialog, which) -> {

            SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(ActivityConfgPerfil.this);
            sharedPreferencesConfg.clearShareds();

            AgendaDAO agendaDAO = new AgendaDAO(ActivityConfgPerfil.this);
            ArrayList<Long> ids = agendaDAO.idTarefasLembrete();

            for(long id : ids) {
                int idInt = (int) id;
                AlarmScheduler.cancelAlarm(ActivityConfgPerfil.this, idInt);
            }
            agendaDAO.excluiTabelaAgenda();

            Intent intent = new Intent(ActivityConfgPerfil.this, ActivityLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}