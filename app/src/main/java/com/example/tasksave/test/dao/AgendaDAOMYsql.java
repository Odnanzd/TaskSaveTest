package com.example.tasksave.test.dao;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.example.tasksave.test.conexaoMYSQL.ConnectionClass;
import com.example.tasksave.test.objetos.AgendaAWS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AgendaDAOMYsql {


    Connection conn;

    public int salvaTarefaAWS(AgendaAWS agendaAWS) {

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        int generatedId = -1;

        try {
            String sql = "INSERT INTO tarefa_usuario (nome_tarefa, descricao_tarefa, lembrete_tarefa," +
                    " repetir_tarefa, repetir_modo_tarefa, data_tarefa, hora_tarefa, minuto_tarefa, " +
                    "data_farefa_fim, hora_tarefa_fim, minuto_tarefa_fim, data_tarefa_insert, hora_tarefa_insert," +
                    " minuto_tarefa_insert, atraso_tarefa, finalizado_tarefa, notificou_tarefa, usuario_id) VALUES" +
                    " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, agendaAWS.getNome_tarefa());
            pstm.setString(2, agendaAWS.getDescricao_tarefa());
            pstm.setBoolean(3, agendaAWS.getLembrete_tarefa());
            pstm.setBoolean(4, agendaAWS.getRepetir_tarefa());
            pstm.setInt(5, agendaAWS.getRepetir_modo_tarefa());

            if (agendaAWS.getData_tarefaString() != null) {
                pstm.setDate(6, java.sql.Date.valueOf(agendaAWS.getData_tarefaString()));
            } else {
                pstm.setNull(6, java.sql.Types.DATE);
            }

            pstm.setInt(7, agendaAWS.getHora_tarefa());
            pstm.setInt(8, agendaAWS.getMinuto_tarefa());

            if (agendaAWS.getData_tarefa_fimString() != null) {
                pstm.setDate(9, java.sql.Date.valueOf(agendaAWS.getData_tarefa_fimString()));
            } else {
                pstm.setNull(9, java.sql.Types.DATE);
            }


            pstm.setInt(10, agendaAWS.getHora_tarefa_fim());
            pstm.setInt(11, agendaAWS.getMinuto_tarefa_fim());

            if (agendaAWS.getData_tarefa_insertString() != null) {
                pstm.setDate(12, java.sql.Date.valueOf(agendaAWS.getData_tarefa_insertString()));
            } else {
                pstm.setNull(9, java.sql.Types.DATE);
            }


            pstm.setInt(13, agendaAWS.getHora_tarefa_insert());
            pstm.setInt(14, agendaAWS.getMinuto_tarefa_insert());
            pstm.setInt(15, agendaAWS.getAtraso_tarefa());
            pstm.setBoolean(16, agendaAWS.getFinalizado_tarefa());
            pstm.setBoolean(17, agendaAWS.getNotificou_tarefa());
            pstm.setInt(18, agendaAWS.getUsuario_id());

            int affectedRows = pstm.executeUpdate();


            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("Erro ao cadastrar Tarefa", "ERRO AO CADASTRAR" + e);
        }

        return generatedId;
    }

    public boolean deleteTasks(ArrayList<Long> taskIds, int idUsuario) {

        String sql = "DELETE FROM tarefa_usuario WHERE id_tarefa = ?";

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        if(taskIds.isEmpty()) {
            Log.d("LISTA VAZIA", "LISTA VAZIA");
        }else {
            for(Long id : taskIds) {
                Log.d(TAG, "Task ID: " + id);
            }
        }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Long id : taskIds) {
//                    stmt.setInt(1, idUsuario);
                    stmt.setLong(1, id);
                    stmt.addBatch();
                }
                stmt.executeBatch();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

}
