package com.example.tasksave;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    public void onReceive(Context context, Intent intent) {
        // Coloque aqui a lógica para verificar as tarefas e mostrar notificações


        AgendaDAO agendaDAO = new AgendaDAO(context);
        Calendar calendar = Calendar.getInstance();
        int horasFim = calendar.get(Calendar.HOUR_OF_DAY);
        int minutosFim = calendar.get(Calendar.MINUTE);
        LocalDate dataAtual = LocalDate.now();


        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AgendamentoService.enqueueWork(context, intent);

        } else {
            AgendamentoService.enqueueWork(context, intent);
        }
//
//        if (intent.getAction() != null && intent.getAction().equals("ACTION_CONCLUIR")) {
//
//            long tarefaId = intent.getLongExtra("tarefaId", -1);
//            boolean tarefaFinalizado = intent.getBooleanExtra("tarefaFinalizado", false);
//            Log.d("AlarmReceiver", "Tarefa com status: " + tarefaFinalizado);
//
//            if (!tarefaFinalizado) {
//                // Atualizar o status da tarefa no banco de dados
//                boolean finalizado = agendaDAO.AtualizarStatus(tarefaId, 1, dataAtual, horasFim, minutosFim);
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//                notificationManager.cancel((int) tarefaId);
//                Log.d("AlarmReceiver", "Tarefa marcada como concluída: " + finalizado);
//            }
//        }
//    }
    }
}

