package com.example.tasksave.test.objetos;

import java.time.LocalDate;
import java.util.Date;

public class AgendaAWS {

    private int id_tarefa;
    private String nome_tarefa;
    private String descricao_tarefa;
    private boolean lembrete_tarefa;
    private boolean repetir_tarefa;
    private int repetir_modo_tarefa;
    private LocalDate data_tarefa;
    private int hora_tarefa;
    private int minuto_tarefa;
    private LocalDate data_tarefa_fim;
    private int hora_tarefa_fim;
    private int minuto_tarefa_fim;
    private LocalDate data_tarefa_insert;
    private int hora_tarefa_insert;
    private int minuto_tarefa_insert;
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

    public String getData_tarefaString() {
        if (data_tarefa != null) {
            return data_tarefa.toString();
        } else {
            return null;
        }
    }

    public void setData_tarefa(LocalDate data_tarefa) {
        this.data_tarefa = data_tarefa;
    }

    public int getHora_tarefa() {
        return hora_tarefa;
    }

    public void setHora_tarefa(int hora_tarefa) {
        this.hora_tarefa = hora_tarefa;
    }

    public int getMinuto_tarefa() {
        return minuto_tarefa;
    }

    public void setMinuto_tarefa(int minuto_tarefa) {
        this.minuto_tarefa = minuto_tarefa;
    }

    public String getData_tarefa_fimString() {
        if (data_tarefa_fim != null) {
            return data_tarefa_fim.toString();
        } else {
            return null;
        }
    }

    public void setData_tarefa_fim(LocalDate data_tarefa_fim) {
        this.data_tarefa_fim = data_tarefa_fim;
    }

    public int getHora_tarefa_fim() {
        return hora_tarefa_fim;
    }

    public void setHora_tarefa_fim(int hora_tarefa_fim) {
        this.hora_tarefa_fim = hora_tarefa_fim;
    }

    public int getMinuto_tarefa_fim() {
        return minuto_tarefa_fim;
    }

    public void setMinuto_tarefa_fim(int minuto_tarefa_fim) {
        this.minuto_tarefa_fim = minuto_tarefa_fim;
    }

    public String getData_tarefa_insertString() {
        if (data_tarefa_insert != null) {
            return data_tarefa_insert.toString();
        } else {
            return null;
        }
    }

    public void setData_tarefa_insert(LocalDate data_tarefa_insert) {
        this.data_tarefa_insert = data_tarefa_insert;
    }

    public int getHora_tarefa_insert() {
        return hora_tarefa_insert;
    }

    public void setHora_tarefa_insert(int hora_tarefa_insert) {
        this.hora_tarefa_insert = hora_tarefa_insert;
    }

    public int getMinuto_tarefa_insert() {
        return minuto_tarefa_insert;
    }

    public void setMinuto_tarefa_insert(int minuto_tarefa_insert) {
        this.minuto_tarefa_insert = minuto_tarefa_insert;
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
