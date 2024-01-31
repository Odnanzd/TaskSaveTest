package com.example.tasksave;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class AgendamentoService extends JobIntentService {
    private PowerManager.WakeLock wakeLock;

    private Conexao con;
    private SQLiteDatabase db;

    // Identificador único para o serviço
    private static final int JOB_ID = 1001;

    // Método estático para enfileirar o trabalho no serviço
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, AgendamentoService.class, JOB_ID, work);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Obter um WakeLock para manter o serviço em execução
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AgendamentoService:WakeLock");
        wakeLock.acquire();

        // Iniciar a tarefa de verificação com o Handler
        verificarTarefasEExibirNotificacoes(this);

        // Liberar o WakeLock ao concluir o trabalho
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

            Calendar calendar = Calendar.getInstance();
            int horasFim = calendar.get(Calendar.HOUR_OF_DAY);
            int minutosFim = calendar.get(Calendar.MINUTE);

            Log.d("VerificacaoTarefa", "Data da tarefa: " + dataTarefa + " Hora da tarefa: " + horaTarefa);
            Log.d("VerificacaoTarefa", "Data atual: " + dataAtual + " Hora atual: " + horaAtual);

            long tarefaId = tarefa.getId();

            Intent intentConcluir = new Intent(context, AlarmReceiver.class);
            intentConcluir.setAction("ACTION_CONCLUIR");
            intentConcluir.putExtra("tarefaId", tarefa.getId()); // Adicione o ID da tarefa como extra

            PendingIntent pendingIntentConcluir = PendingIntent.getBroadcast(
                    context,
                    0,
                    intentConcluir,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            if (dataTarefa.isEqual(dataAtual) && horaTarefa.equals(horaAtual)) {

                mostrarNotificacao(context, "TaskSave - " + tarefa.getNomeAgenda(), tarefa.getDescriçãoAgenda(), pendingIntentConcluir);
//                boolean finalizado = agendaDAO.AtualizarStatus(tarefaId, 1, dataAtual, horasFim, minutosFim);

                }
            }
        }
    @SuppressLint("MissingPermission")


    private void mostrarNotificacao(Context context, String titulo, String descricao, PendingIntent pendingIntentConcluir) {
        int notificationId = (int) System.currentTimeMillis();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.logotipoicon)
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_background, "Concluir", pendingIntentConcluir )
                ;

        // Intent para abrir a atividade ao tocar na notificação (ajuste conforme sua necessidade)
        Intent intent = new Intent(context, activity_agenda.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Construir o gerenciador de notificações e exibir a notificação
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
    }