package com.example.tasksave.test.dao;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.tasksave.test.conexaoMYSQL.ConnectionClass;
import com.example.tasksave.test.objetos.Agenda;
import com.example.tasksave.test.objetos.AgendaAWS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

            if(agendaAWS.getLembrete_tarefa()) {

                pstm.setDate(6, java.sql.Date.valueOf(agendaAWS.getData_tarefaString()));
                pstm.setInt(7, agendaAWS.getHora_tarefa());
                pstm.setInt(8, agendaAWS.getMinuto_tarefa());

                pstm.setNull(9, java.sql.Types.DATE);
                pstm.setNull(10, java.sql.Types.INTEGER);
                pstm.setNull(11, java.sql.Types.INTEGER);

            }else {

                pstm.setNull(6, java.sql.Types.DATE);
                pstm.setNull(7, java.sql.Types.INTEGER);
                pstm.setNull(8, java.sql.Types.INTEGER);

                pstm.setNull(9, java.sql.Types.DATE);
                pstm.setNull(10, java.sql.Types.INTEGER);
                pstm.setNull(11, java.sql.Types.INTEGER);

            }



            pstm.setString(1, agendaAWS.getNome_tarefa());
            pstm.setString(2, agendaAWS.getDescricao_tarefa());
            pstm.setBoolean(3, agendaAWS.getLembrete_tarefa());
            pstm.setBoolean(4, agendaAWS.getRepetir_tarefa());
            pstm.setInt(5, agendaAWS.getRepetir_modo_tarefa());

//            if (agendaAWS.getData_tarefaString() != null) {
//                pstm.setDate(6, java.sql.Date.valueOf(agendaAWS.getData_tarefaString()));
//            } else {
//                pstm.setNull(6, java.sql.Types.DATE);
//            }
//            pstm.setInt(7, agendaAWS.getHora_tarefa());
//            pstm.setInt(8, agendaAWS.getMinuto_tarefa());

//            if (agendaAWS.getData_tarefa_fimString() != null) {
//                pstm.setDate(9, java.sql.Date.valueOf(agendaAWS.getData_tarefa_fimString()));
//            } else {
//                pstm.setNull(9, java.sql.Types.DATE);
//            }


//            pstm.setInt(10, agendaAWS.getHora_tarefa_fim());
//            pstm.setInt(11, agendaAWS.getMinuto_tarefa_fim());

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

        String sql = "DELETE FROM tarefa_usuario WHERE id_tarefa = ? AND usuario_id = ?";

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Long id : taskIds) {
                    stmt.setLong(1, id);
                    stmt.setInt(2, idUsuario);
                    stmt.addBatch();
                }
                stmt.executeBatch();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Agenda> tarefasUsuario(int idUsuario) {


        String sql = "SELECT * FROM tarefa_usuario WHERE usuario_id = ? AND finalizado_tarefa = 0";

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        List<Agenda> tarefasUsuario = new ArrayList<>();


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idUsuario);

                try(ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {

                        LocalDate localDateTarefanow = null;
                        LocalDate localDateFimNow = null;
                        LocalDate localDateInsertNow = null;


                        Date dataTarefaDT = rs.getDate("data_tarefa");
                        if (dataTarefaDT !=null ) {

                            String dataTarefaString = dataTarefaDT.toString();
                            localDateTarefanow = LocalDate.parse(dataTarefaString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        }

                        Date dataTarefaFim = rs.getDate("data_farefa_fim");
                        if (dataTarefaFim !=null) {

                            String dataTarefaFimString = dataTarefaFim.toString();
                            localDateFimNow = LocalDate.parse(dataTarefaFimString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        }

                        Date dataTarefaInsert = rs.getDate("data_tarefa_insert");

                        if(dataTarefaInsert!=null) {

                            String dataTarefaInsertString = dataTarefaInsert.toString();
                            localDateInsertNow = LocalDate.parse(dataTarefaInsertString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        }

                        tarefasUsuario.add(new Agenda(rs.getInt("id_tarefa"),
                        rs.getString("nome_tarefa"),rs.getString("descricao_tarefa"),
                        localDateTarefanow, rs.getInt("hora_tarefa"), rs.getInt("minuto_tarefa"),
                        rs.getBoolean("lembrete_tarefa"),
                        rs.getBoolean("finalizado_tarefa"), localDateFimNow,
                        rs.getInt("hora_tarefa_fim"), rs.getInt("minuto_tarefa_fim"),
                        localDateInsertNow,
                        rs.getInt("hora_tarefa_insert"), rs.getInt("minuto_tarefa_insert"),
                        rs.getInt("atraso_tarefa"), rs.getBoolean("repetir_tarefa"),
                        rs.getInt("repetir_modo_tarefa"), rs.getBoolean("notificou_tarefa")));

                    }
                }


        } catch (SQLException e) {
           e.printStackTrace();
        }

        return tarefasUsuario;
    }

}
