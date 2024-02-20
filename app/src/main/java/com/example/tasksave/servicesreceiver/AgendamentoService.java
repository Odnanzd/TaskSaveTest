package com.example.tasksave.servicesreceiver;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tasksave.R;
import com.example.tasksave.activities.activity_agenda;
import com.example.tasksave.dao.AgendaDAO;
import com.example.tasksave.objetos.Agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class AgendamentoService extends JobIntentService {
    private PowerManager.WakeLock wakeLock;

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
        verificarTarefasEExibirNotificacoes(this, intent);

        // Liberar o WakeLock ao concluir o trabalho
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void verificarTarefasEExibirNotificacoes(Context context, Intent intent) {

        AgendaDAO agendaDAO = new AgendaDAO(context);
        List<Agenda> tarefasComLembrete = agendaDAO.obterTarefasComLembreteAtivado();

        for (Agenda tarefa : tarefasComLembrete) {
            LocalDate dataTarefa = LocalDate.parse(tarefa.getDataAgendaString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime horaTarefa = LocalTime.of(tarefa.getHoraAgenda(), tarefa.getMinutoAgenda());
            LocalDate dataAtual = LocalDate.now();
            LocalTime horaAtual = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);


            Calendar calendar = Calendar.getInstance();


            long tarefaId = tarefa.getId();
            boolean tarefaFinalizado = tarefa.getFinalizado();
            boolean repetirLembrete = tarefa.getRepetirLembrete();
            int repetirLembreteModo = tarefa.getRepetirModo();

            if (repetirLembrete && dataTarefa.isBefore(dataAtual)) {
                switch (repetirLembreteModo) {
                    case 1: // Todo dia
                        dataTarefa = dataTarefa.plusDays(1);
                        break;
                    case 2: // Toda semana
                        dataTarefa = dataTarefa.plusWeeks(1);
                        break;
                    case 3: // Todo mês
                        dataTarefa = dataTarefa.plusMonths(1);
                        break;
                    case 4: // Todo ano
                        dataTarefa = dataTarefa.plusYears(1);
                        break;
                }
            }

            Log.d("VerificacaoTarefa", "Data da tarefa: " + dataTarefa + " Hora da tarefa: " + horaTarefa);
            Log.d("VerificacaoTarefa", "Data atual: " + dataAtual + " Hora atual: " + horaAtual);

            if (dataTarefa.isEqual(dataAtual) && horaTarefa.equals(horaAtual) && !tarefaFinalizado) {

                Intent intentConcluir = new Intent(context, AlarmReceiver.class);
                intentConcluir.setAction("ACTION_CONCLUIR");
                intentConcluir.putExtra("tarefaId", tarefa.getId());// Adicione o ID da tarefa como extra
                intentConcluir.putExtra("tarefaFinalizado", tarefa.getFinalizado());

                int notificationId = (int) tarefaId;

                PendingIntent pendingIntentConcluir = PendingIntent.getBroadcast(
                        context,
                        notificationId,
                        intentConcluir,
                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                mostrarNotificacao(context, tarefa.getNomeAgenda(),
                        tarefa.getDescriçãoAgenda(), pendingIntentConcluir, notificationId);

                }
            if (intent.getAction() != null && intent.getAction().equals("ACTION_CONCLUIR")) {
                processarAcaoConcluir(context, intent);
            }
        }

            }

    private void processarAcaoConcluir(Context context, Intent intent) {

        AgendaDAO agendaDAO = new AgendaDAO(context);
        Calendar calendar = Calendar.getInstance();
        int horasFim = calendar.get(Calendar.HOUR_OF_DAY);
        int minutosFim = calendar.get(Calendar.MINUTE);
        @SuppressLint({"NewApi", "LocalSuppress"})
        LocalDate dataAtual = LocalDate.now();

        long tarefaId = intent.getLongExtra("tarefaId", -1);
        boolean tarefaFinalizado = intent.getBooleanExtra("tarefaFinalizado", false);
        Log.d("AgendamentoService", "Tarefa com status: " + tarefaFinalizado);

        if (!tarefaFinalizado) {
            // Atualizar o status da tarefa no banco de dados
            boolean finalizado = agendaDAO.AtualizarStatus(tarefaId, 1, dataAtual, horasFim, minutosFim);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel((int) tarefaId);

            Log.d("AgendamentoService", "Tarefa marcada como concluída: " + finalizado);
        }
    }
    @SuppressLint("MissingPermission")


    private void mostrarNotificacao(Context context, String titulo, String descricao, PendingIntent pendingIntentConcluir, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.rocket)
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_background, "Concluir", pendingIntentConcluir);

        // Intent para abrir a atividade ao tocar na notificação (ajuste conforme sua necessidade)
        Intent intent = new Intent(context, activity_agenda.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Construir o gerenciador de notificações e exibir a notificação
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
    }