package com.example.tasksave.test.objetos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AgendaAWS {

    private int id_tarefa;
    private String nome_tarefa;
    private String descricao_tarefa;
    private boolean lembrete_tarefa;
    private boolean repetir_tarefa;
    private int repetir_modo_tarefa;
    private LocalDateTime data_hora_tarefa;
    private LocalDateTime data_hora_tarefa_fim;
    private LocalDateTime data_hora_tarefa_insert;
    private int atraso_tarefa;
    private boolean finalizado_tarefa;
    private boolean notificou_tarefa;
    private int usuario_id;

    public int getId_tarefa() {
        return id_tarefa;
    }

    public void setId_tarefa(int id_tarefa) {
        this.id_tarefa = id_tarefa;
    }

    public String getNome_tarefa() {
        return nome_tarefa;
    }

    public void setNome_tarefa(String nome_tarefa) {
        this.nome_tarefa = nome_tarefa;
    }

    public String getDescricao_tarefa() {
        return descricao_tarefa;
    }

    public void setDescricao_tarefa(String descricao_tarefa) {
        this.descricao_tarefa = descricao_tarefa;
    }

    public boolean getLembrete_tarefa() {
        return lembrete_tarefa;
    }

    public void setLembrete_tarefa(boolean lembrete_tarefa) {
        this.lembrete_tarefa = lembrete_tarefa;
    }

    public boolean getRepetir_tarefa() {
        return repetir_tarefa;
    }

    public void setRepetir_tarefa(boolean repetir_tarefa) {
        this.repetir_tarefa = repetir_tarefa;
    }

    public int getRepetir_modo_tarefa() {
        return repetir_modo_tarefa;
    }

    public void setRepetir_modo_tarefa(int repetir_modo_tarefa) {
        this.repetir_modo_tarefa = repetir_modo_tarefa;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Timestamp getData_tarefaTimestamp() {
        if (data_hora_tarefa != null) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");

            String formattedDateTime = data_hora_tarefa.format(formatter);


            return Timestamp.valueOf(formattedDateTime);

        } else {
            return null;
        }
    }

    public void setData_tarefa(LocalDateTime data_hora_tarefa) {

        if (data_hora_tarefa!=null) {
            this.data_hora_tarefa = data_hora_tarefa;
        }else {
            this.data_hora_tarefa = null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Timestamp getData_tarefa_fimTimestamp() {
        if (data_hora_tarefa_fim != null) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");

            String formattedDateTime = data_hora_tarefa_fim.format(formatter);

            return Timestamp.valueOf(formattedDateTime);

        } else {
            return null;
        }
    }

    public void setData_tarefa_fim(LocalDateTime data_hora_tarefa_fim) {

        if (data_hora_tarefa_fim!=null) {
            this.data_hora_tarefa_fim = data_hora_tarefa_fim;
        }else {
            this.data_hora_tarefa_fim = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Timestamp getData_tarefa_insertTimestamp() {
        if (data_hora_tarefa_insert != null) {
            // Converter LocalDateTime para Timestamp

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");

            String formattedDateTime = data_hora_tarefa_insert.format(formatter);

            return Timestamp.valueOf(formattedDateTime);

        } else {
            return null;
        }
    }

    public void setData_tarefa_insert(LocalDateTime data_hora_tarefa_insert) {
        if (data_hora_tarefa_insert!=null) {
            this.data_hora_tarefa_insert = data_hora_tarefa_insert;
        }else {
            this.data_hora_tarefa_insert = null;
        }
    }

    public int getAtraso_tarefa() {
        return atraso_tarefa;
    }

    public void setAtraso_tarefa(int atraso_tarefa) {
        this.atraso_tarefa = atraso_tarefa;
    }

    public boolean getFinalizado_tarefa() {
        return finalizado_tarefa;
    }

    public void setFinalizado_tarefa(boolean finalizado_tarefa) {
        this.finalizado_tarefa = finalizado_tarefa;
    }

    public boolean getNotificou_tarefa() {
        return notificou_tarefa;
    }

    public void setNotificou_tarefa(boolean notificou_tarefa) {
        this.notificou_tarefa = notificou_tarefa;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }
}
