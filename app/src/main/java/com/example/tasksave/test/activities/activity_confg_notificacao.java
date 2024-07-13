package com.example.tasksave.test.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tasksave.R;
import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.objetos.Agenda;
import com.example.tasksave.test.servicesreceiver.AlarmScheduler;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesConfg;

import java.util.List;

public class activity_confg_notificacao extends AppCompatActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(activity_confg_notificacao.this, activity_config.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent2);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private LinearLayout linearLayoutNotSilen;
    private Switch aSwitch;
    private ImageView imageViewBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_notificacao);

        linearLayoutNotSilen = findViewById(R.id.linearLayoutNot);
        aSwitch = findViewById(R.id.switchNot);
        imageViewBack = findViewById(R.id.imageView4);

        verificaNotifica();

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(activity_confg_notificacao.this, activity_config.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        linearLayoutNotSilen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aSwitch.toggle();

                if(aSwitch.isChecked()) {

                    removerLembreteTarefa();
                    SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(activity_confg_notificacao.this);
                    sharedPreferencesConfg.armazenaNotifica(true);

                    Toast.makeText(getBaseContext(), "Notificações silenciadas", Toast.LENGTH_LONG).show();

                } else {

                    SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(activity_confg_notificacao.this);
                    sharedPreferencesConfg.armazenaNotifica(false);
                    reagendaLembreteTarefa(activity_confg_notificacao.this);

                    Toast.makeText(getBaseContext(), "Notificações ativadas", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void verificaNotifica() {


        SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(activity_confg_notificacao.this);
        boolean notifica = sharedPreferencesConfg.getNotifica();

        if(!notifica) {
            aSwitch.setChecked(false);
        }else {
            aSwitch.setChecked(true);
        }
    }
    public void removerLembreteTarefa() {

        AgendaDAO agendaDAO = new AgendaDAO(activity_confg_notificacao.this);
        @SuppressLint({"NewApi", "LocalSuppress"})
        List<Agenda> agendasComLembrete = agendaDAO.obterTarefasComLembreteAtivado();

        for (Agenda tarefa : agendasComLembrete) {

         AlarmScheduler.cancelAlarm(activity_confg_notificacao.this, tarefa.getId());

        }
    }
    public void reagendaLembreteTarefa(Context context) {
        AgendaDAO agendaDAO = new AgendaDAO(activity_confg_notificacao.this);
        @SuppressLint({"NewApi", "LocalSuppress"})
        List<Agenda> agendasComLembrete = agendaDAO.obterTarefasComLembreteAtivado();

        for (Agenda tarefa : agendasComLembrete) {
            if (!tarefa.getFinalizado()) {
                AlarmScheduler.scheduleAlarm(
                        context,
                        tarefa.getDateTimeInMillis(),
                        tarefa.getNomeAgenda(),
                        tarefa.getDescriçãoAgenda(),
                        tarefa.getRepetirModo(),
                        tarefa.getId(),
                        tarefa.getDate()
                );
            }
        }
    }
}