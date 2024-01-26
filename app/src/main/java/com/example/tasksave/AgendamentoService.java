package com.example.tasksave;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AgendamentoService extends Service {

    private PowerManager.WakeLock wakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            // Obter um WakeLock para manter o serviço em execução, mesmo quando o dispositivo estiver inativo
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AgendamentoService:WakeLock");
            wakeLock.acquire();

            // Iniciar a tarefa de verificação com o Handler
        verificarTarefasEExibirNotificacoes(this);


        return START_STICKY;
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Liberar o WakeLock ao encerrar o serviço
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void verificarTarefasEExibirNotificacoes(Context context) {
        AgendaDAO agendaDAO = new AgendaDAO(context);
        List<Agenda> tarefasComLembrete = agendaDAO.obterTarefasComLembreteAtivado();

        for (Agenda tarefa : tarefasComLembrete) {
            LocalDate dataTarefa = LocalDate.parse(tarefa.getDataAgendaString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime horaTarefa = LocalTime.of(tarefa.getHoraAgenda(), tarefa.getMinutoAgenda());
            LocalDate dataAtual = LocalDate.now();
            LocalTime horaAtual = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
            Log.d("VerificacaoTarefa", "Data da tarefa: " + dataTarefa + " Hora da tarefa: " + horaTarefa);
            Log.d("VerificacaoTarefa", "Data atual: " + dataAtual + " Hora atual: " + horaAtual);

            if (dataTarefa.isEqual(dataAtual) && horaTarefa.equals(horaAtual)) {
                mostrarNotificacao(context, tarefa.getNomeAgenda(), tarefa.getDescriçãoAgenda());
            }
        }
    }
    @SuppressLint("MissingPermission")

    private void mostrarNotificacao(Context context, String titulo, String descricao) {
        int notificationId = (int) System.currentTimeMillis();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Intent para abrir a atividade ao tocar na notificação (ajuste conforme sua necessidade)
        Intent intent = new Intent(context, activity_agenda.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Construir o gerenciador de notificações e exibir a notificação
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
    }