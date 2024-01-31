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

        if (intent.getAction() != null && intent.getAction().equals("ACTION_CONCLUIR")) {

            long tarefaId = intent.getLongExtra("tarefaId", -1);

            if (tarefaId != -1) {
                // Atualizar o status da tarefa no banco de dados
                boolean finalizado = agendaDAO.AtualizarStatus(tarefaId, 1, dataAtual, horasFim, minutosFim);
            }
        }
    }
}
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void verificarTarefasEExibirNotificacoes(Context context) {
//        AgendaDAO agendaDAO = new AgendaDAO(context);
//        List<Agenda> tarefasComLembrete = agendaDAO.obterTarefasComLembreteAtivado();
//
//        for (Agenda tarefa : tarefasComLembrete) {
//            LocalDate dataTarefa = LocalDate.parse(tarefa.getDataAgendaString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            LocalTime horaTarefa = LocalTime.of(tarefa.getHoraAgenda(), tarefa.getMinutoAgenda());
//            LocalDate dataAtual = LocalDate.now();
//            LocalTime horaAtual = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
//            Log.d("VerificacaoTarefa", "Data da tarefa: " + dataTarefa + " Hora da tarefa: " + horaTarefa);
//            Log.d("VerificacaoTarefa", "Data atual: " + dataAtual + " Hora atual: " + horaAtual);
//
//            if (dataTarefa.isEqual(dataAtual) && horaTarefa.equals(horaAtual)) {
//                mostrarNotificacao(context, tarefa.getNomeAgenda(), tarefa.getDescriçãoAgenda());
//            }
//        }
//    }
//    @SuppressLint("MissingPermission")
//
//    private void mostrarNotificacao(Context context, String titulo, String descricao) {
//        int notificationId = (int) System.currentTimeMillis();
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle(titulo)
//                .setContentText(descricao)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        // Intent para abrir a atividade ao tocar na notificação (ajuste conforme sua necessidade)
//        Intent intent = new Intent(context, activity_agenda.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//        builder.setContentIntent(pendingIntent);
//
//        // Construir o gerenciador de notificações e exibir a notificação
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(notificationId, builder.build());
//    }
//}
//
//
//
//
//
