package com.example.tasksave.test.workers;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tasksave.test.dao.AgendaDAOMYsql;
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.AgendaAWS;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;

import java.time.LocalDateTime;

public class SaveTaskWorker extends Worker {

    public SaveTaskWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        // Aqui você deve inserir a lógica para salvar a tarefa no banco de dados
        AgendaDAOMYsql agendaDAOMYsql = new AgendaDAOMYsql();

        SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(getApplicationContext());
        String emailShared = sharedPreferencesUsuario.getEmailLogin();

        UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
        int id_usuario = usuarioDAOMYsql.idUsarioAWS(emailShared);

        AgendaAWS agendaAWS = new AgendaAWS();
        agendaAWS.setNome_tarefa(getInputData().getString("nomeTarefa"));
        agendaAWS.setDescricao_tarefa(getInputData().getString("descTarefa"));
        agendaAWS.setLembrete_tarefa(getInputData().getBoolean("lembreteTarefa", false));
        agendaAWS.setRepetir_tarefa(getInputData().getBoolean("repetirLembrete", false));
        agendaAWS.setRepetir_modo_tarefa(getInputData().getInt("repetirModoLembrete", 0));

        // Verificar se as strings não são nulas antes de parsear
        String dataHoraTarefaStr = getInputData().getString("dataHoraTarefa");
        if (dataHoraTarefaStr != null) {
            agendaAWS.setData_tarefa(LocalDateTime.parse(dataHoraTarefaStr));
        }

        String dataHoraTarefaFimStr = getInputData().getString("dataHoraTarefaFim");
        if (dataHoraTarefaFimStr != null) {
            agendaAWS.setData_tarefa_fim(LocalDateTime.parse(dataHoraTarefaFimStr));
        }

        String dataHoraTarefaInsertStr = getInputData().getString("dataHoraTarefaInsert");
        if (dataHoraTarefaInsertStr != null) {
            agendaAWS.setData_tarefa_insert(LocalDateTime.parse(dataHoraTarefaInsertStr));
        }

        agendaAWS.setAtraso_tarefa(getInputData().getInt("atrasoTarefa", 0));
        agendaAWS.setFinalizado_tarefa(getInputData().getBoolean("finalizadoTarefa", false));
        agendaAWS.setNotificou_tarefa(getInputData().getBoolean("notificouTarefa", false));
        agendaAWS.setUsuario_id(id_usuario);

        int tarefaID = agendaDAOMYsql.salvaTarefaAWS(agendaAWS);

        // Verificar se a tarefa foi salva com sucesso
        if (tarefaID != -1) {
            Log.d("TESTE WORKERS", "TESTE POSITIVO");
            return Result.success();
        } else {
            Log.d("TESTE WORKERS", "TESTE NEGATIVO");
            return Result.failure();
        }
    }
}
