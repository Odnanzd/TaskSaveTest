package com.example.tasksave.test.servicesreceiver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;

import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.dao.AgendaDAOMYsql;
import com.example.tasksave.test.dao.AlarmeDAO;
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.AgendaAWS;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;

import java.time.LocalDateTime;

public class BackgroundService extends JobIntentService {

    private static final int JOB_ID = 1000;
    private PowerManager.WakeLock wakeLock;
    public static final String ACTION_INSERT = "ACTION_INSERT";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, BackgroundService.class, JOB_ID, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AgendamentoService:WakeLock");
        wakeLock.acquire();

        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_INSERT:
                    handleInsertTask(intent);
                    break;
                case ACTION_UPDATE:
//                    handleUpdateTask(intent);
                    break;
                default:
                    Log.w("BackgroundService", "Unknown action: " + action);
            }
    }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleInsertTask(Intent intent) {

        int idTarefaSQLite = intent.getIntExtra("idTarefaSQLITE", -1);

        String nomeTarefa = intent.getStringExtra("nomeTarefa");
        String descTarefa = intent.getStringExtra("descTarefa");
        boolean lembreteTarefa = intent.getBooleanExtra("lembreteTarefa", false);
        boolean repetirLembrete = intent.getBooleanExtra("repetirLembrete", false);
        int repetirModoLembrete = intent.getIntExtra("repetirModoLembrete", 0);
        String dataHoraTarefaStr = intent.getStringExtra("dataHoraTarefa");
        String dataHoraTarefaFimStr = intent.getStringExtra("dataHoraTarefaFim");
        String dataHoraTarefaInsertStr = intent.getStringExtra("dataHoraTarefaInsert");
        int atrasoTarefa = intent.getIntExtra("atrasoTarefa", 0);
        boolean finalizadoTarefa = intent.getBooleanExtra("finalizadoTarefa", false);
        boolean notificouTarefa = intent.getBooleanExtra("notificouTarefa", false);

        LocalDateTime dataHoraTarefa = dataHoraTarefaStr != null ? LocalDateTime.parse(dataHoraTarefaStr) : null;
        LocalDateTime dataHoraTarefaFim = dataHoraTarefaFimStr != null ? LocalDateTime.parse(dataHoraTarefaFimStr) : null;
        LocalDateTime dataHoraTarefaInsert = dataHoraTarefaInsertStr != null ? LocalDateTime.parse(dataHoraTarefaInsertStr) : null;

        AgendaDAOMYsql agendaDAOMYsql = new AgendaDAOMYsql();
        SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(getApplicationContext());
        String emailShared = sharedPreferencesUsuario.getEmailLogin();

        UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
        int id_usuario = usuarioDAOMYsql.idUsarioAWS(emailShared);

        AgendaAWS agendaAWS = new AgendaAWS();
        agendaAWS.setNome_tarefa(nomeTarefa);
        agendaAWS.setDescricao_tarefa(descTarefa);
        agendaAWS.setLembrete_tarefa(lembreteTarefa);
        agendaAWS.setRepetir_tarefa(repetirLembrete);
        agendaAWS.setRepetir_modo_tarefa(repetirModoLembrete);
        agendaAWS.setData_tarefa(dataHoraTarefa);
        agendaAWS.setData_tarefa_fim(dataHoraTarefaFim);
        agendaAWS.setData_tarefa_insert(dataHoraTarefaInsert);
        agendaAWS.setAtraso_tarefa(atrasoTarefa);
        agendaAWS.setFinalizado_tarefa(finalizadoTarefa);
        agendaAWS.setNotificou_tarefa(notificouTarefa);
        agendaAWS.setUsuario_id(id_usuario);

        int tarefaID = agendaDAOMYsql.salvaTarefaAWS(agendaAWS, idTarefaSQLite);

        if (tarefaID == -1) {

            Log.d("BackgroundService", "Falha ao salvar a tarefa.");

        } else if(tarefaID==-2) {


            Log.d("BackgroundService", "Tarefa já sincronizadas");

            if(lembreteTarefa) {

                AlarmeDAO alarmeDAO = new AlarmeDAO(getApplicationContext());
                int idTarefaProv = alarmeDAO.getLastIdTarefa();
                Log.d("Teste", "teste: "+idTarefaProv);
                alarmeDAO.atualizaIdTarefa(idTarefaProv, tarefaID);

            }

        } else {

            AgendaDAO agendaDAO = new AgendaDAO(getApplicationContext());
            agendaDAO.atualizaID(idTarefaSQLite, tarefaID);

            if(lembreteTarefa) {

                AlarmeDAO alarmeDAO = new AlarmeDAO(getApplicationContext());
                int idTarefaProv = alarmeDAO.getLastIdTarefa();
                Log.d("ID ANTIGO", "teste ID ANTIGO: "+idTarefaProv);
                alarmeDAO.atualizaIdTarefa(idTarefaProv, tarefaID);

            }

        }
    }

}
