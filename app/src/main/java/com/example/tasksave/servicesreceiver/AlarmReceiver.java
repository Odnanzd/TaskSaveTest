package com.example.tasksave.servicesreceiver;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tasksave.R;
import com.example.tasksave.activities.activity_main;
import com.example.tasksave.dao.AgendaDAO;

import java.time.LocalDate;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        int repeatMode = intent.getIntExtra("repeatMode", 0);
        long id = intent.getLongExtra("idLong", 0);

        Log.d("INTENT", "Titulo: " + title + "Descricao: " + content);

        showNotification(context, title, content);

        // Reagendar o próximo alarme
        Calendar nextAlarm = Calendar.getInstance();
        switch (repeatMode) {
            case 1: // Todo dia
                nextAlarm.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case 2: // Toda semana
                nextAlarm.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case 3: // Todo mês
                nextAlarm.add(Calendar.MONTH, 1);
                break;
        }

        // Reagendar o alarme se repeatMode não for 0
        if (repeatMode != 0) {
            AlarmScheduler.scheduleAlarm(context, nextAlarm, title, content, repeatMode, id);
        }
        AgendaDAO agendaDAO = new AgendaDAO(context);
        agendaDAO.AtualizarStatusNotificacao(id, 1);
    }




    @SuppressLint("MissingPermission")
    private void showNotification(Context context, String titulo, String descricao) {


        Intent intent = new Intent(context, activity_main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.tasksavelogo_notific) // Substitua por um ícone seu
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }

    private void processarAcaoConcluir(Context context, Intent intent) {

        AgendaDAO agendaDAO = new AgendaDAO(context);
        Calendar calendar = Calendar.getInstance();
        int horasFim = calendar.get(Calendar.HOUR_OF_DAY);
        int minutosFim = calendar.get(Calendar.MINUTE);
        @SuppressLint({"NewApi", "LocalSuppress"})
        LocalDate dataAtual = LocalDate.now();

        long idTarefa = intent.getLongExtra("idLong", 0);


            // Atualizar o status da tarefa no banco de dados
            boolean finalizado = agendaDAO.AtualizarStatus(idTarefa, 1, dataAtual, horasFim, minutosFim);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel((int) idTarefa);

            Log.d("AgendamentoService", "Tarefa marcada como concluída: " + finalizado);
    }


}

