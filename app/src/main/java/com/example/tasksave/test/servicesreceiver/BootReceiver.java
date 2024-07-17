package com.example.tasksave.test.servicesreceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.objetos.Agenda;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AgendaDAO agendaDAO = new AgendaDAO(context);
            @SuppressLint({"NewApi", "LocalSuppress"})
            List<Agenda> agendasComLembrete = agendaDAO.obterTarefasComLembreteAtivado();

            for (Agenda tarefa : agendasComLembrete) {
                if (!tarefa.getFinalizado() && tarefa.getRepetirModo()!=0) {
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
            Log.d("BootReceiver", "Alarmes reagendados após reinicialização.");
        }
        }
    }

