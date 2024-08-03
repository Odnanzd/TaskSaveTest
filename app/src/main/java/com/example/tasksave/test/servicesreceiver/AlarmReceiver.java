package com.example.tasksave.test.servicesreceiver;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tasksave.R;
import com.example.tasksave.test.activities.ActivityMain;
import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.dao.AgendaDAOMYsql;
import com.example.tasksave.test.objetos.AgendaAWS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.d("AlarmReceiver", "Ação recebida: " + action);

        if (action==null) {
            Notificar(intent, context);
        }else if (action.equals("ACTION_CONCLUIR")) {
            processarAcaoConcluir(context, intent);
        }else if(action.equals("ACTION_OK")) {
            processarAcaoOk(context, intent);
        }
    }

    @SuppressLint("NewApi")
    private void Notificar(Intent intent, Context context) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        int repeatMode = intent.getIntExtra("repeatMode", 0);
        int id = intent.getIntExtra("idInt", 0);

        if (title == null || title.isEmpty() || content == null || content.isEmpty()) {

            Log.e("AlarmReceiver", "Notificação com dados incompletos. Título ou descrição estão vazios.");

        }else {


            Intent intentConcluir = new Intent(context, AlarmReceiver.class);
            intentConcluir.setAction("ACTION_CONCLUIR");
            intentConcluir.putExtra("idInt", id);

            PendingIntent pendingIntentConcluir = PendingIntent.getBroadcast(
                    context,
                    id,
                    intentConcluir,
                    PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            Intent intentOk = new Intent(context, AlarmReceiver.class);
            intentOk.setAction("ACTION_OK");
            intentOk.putExtra("idInt", id);

            PendingIntent pendingIntentOk = PendingIntent.getBroadcast(
                    context,
                    id,
                    intentOk,
                    PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            showNotification(context, title, content, pendingIntentConcluir, id, pendingIntentOk);

            String dataEscolhida = intent.getStringExtra("dataIntent");

            LocalDateTime localDateDataEscolhida = LocalDateTime.parse(dataEscolhida);

            LocalDateTime localDateDataEscolhida2 = LocalDateTime.now();


            // Reagendar o próximo alarme
            Calendar nextAlarm = Calendar.getInstance();
            switch (repeatMode) {
                case 1: // Todo dia
                    nextAlarm.add(Calendar.DAY_OF_YEAR, 1);
                    localDateDataEscolhida2 = localDateDataEscolhida.plusDays(1);
                    break;
                case 2: // Toda semana
                    nextAlarm.add(Calendar.WEEK_OF_YEAR, 1);
                    localDateDataEscolhida2 = localDateDataEscolhida.plusWeeks(1);
                    break;
                case 3: // Todo mês
                    nextAlarm.add(Calendar.MONTH, 1);
                    localDateDataEscolhida2 = localDateDataEscolhida.plusMonths(1);
                    break;
                case 4:
                    nextAlarm.add(Calendar.YEAR, 1);
                    localDateDataEscolhida2 = localDateDataEscolhida.plusYears(1);
                    break;
            }

            // Reagendar o alarme se repeatMode não for 0
            if (repeatMode != 0) {

                AgendaDAO agendaDAO = new AgendaDAO(context);
                AlarmScheduler.scheduleAlarm(context, nextAlarm.getTimeInMillis(), title, content, repeatMode, id, localDateDataEscolhida2);
                boolean atualiza = agendaDAO.atualizarDataTarefa(id, localDateDataEscolhida2);

                if (atualiza) {

                    LocalDateTime finalLocalDateDataEscolhida2 = localDateDataEscolhida2;
                    int finalId = id;

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(() -> {

                        try{

                            AgendaAWS agendaAWS = new AgendaAWS();
                            AgendaDAOMYsql agendaDAOMYsql = new AgendaDAOMYsql();
                            agendaAWS.setData_tarefa(finalLocalDateDataEscolhida2);
                            agendaAWS.setId_tarefa(finalId);
                            agendaDAOMYsql.atualizaDataHoraAWS(agendaAWS);

                        }catch (Exception e) {

                            Log.d("ERRO SQL CADASTRO", "Erro ao atualizar: " + e);

                        }

                    });
                }else {
                    Log.d("ERRO SQL CADASTRO", "Erro ao atualizar data, reprogramar tentativa: ");
                }

            }
            AgendaDAO agendaDAO = new AgendaDAO(context);
            agendaDAO.AtualizarStatusNotificacao(id, 1);
        }
    }

    @SuppressLint("MissingPermission")
    private void showNotification(Context context, String titulo, String descricao, PendingIntent pendingIntentConcluir, int notificationId,
                                  PendingIntent pendingIntentOk) {
        Intent intent = new Intent(context, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.tasksavelogo_notific) // Substitua por um ícone seu
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_background, "Concluir", pendingIntentConcluir)
                .addAction(R.drawable.ic_launcher_background, "OK", pendingIntentOk);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }

    private void processarAcaoConcluir(Context context, Intent intent) {
        AgendaDAO agendaDAO = new AgendaDAO(context);
        Calendar calendar = Calendar.getInstance();
        int horasFim = calendar.get(Calendar.HOUR_OF_DAY);
        int minutosFim = calendar.get(Calendar.MINUTE);
        @SuppressLint({"NewApi", "LocalSuppress"})
        LocalDate dataAtual = LocalDate.now();

        long idTarefa = intent.getLongExtra("idInt", 0);

        if (idTarefa != 0) {
            // Atualizar o status da tarefa no banco de dados
            boolean finalizado = agendaDAO.AtualizarStatus(idTarefa, 1, dataAtual, horasFim, minutosFim);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel((int) idTarefa);

            Log.d("AlarmReceiver", "Tarefa marcada como concluída: " + finalizado);
        } else {
            Log.e("AlarmReceiver", "ID da tarefa não encontrado no Intent");
        }
    }

    private void processarAcaoOk(Context context, Intent intent) {
        int idTarefa = intent.getIntExtra("idInt", 0);
        if (idTarefa != 0) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel((int) idTarefa);

            Log.d("AlarmReceiver", "Notificação OK clicada e removida para tarefa ID: " + idTarefa);
        } else {
            Log.e("AlarmReceiver", "ID da tarefa não encontrado no Intent para ação OK");
        }
    }

}

