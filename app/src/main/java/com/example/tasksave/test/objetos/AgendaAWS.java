package com.example.tasksave.test.objetos;

import java.time.LocalDateTime;

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

    public String getData_tarefaString() {
            return data_hora_tarefa.toString();

    }

    public void setData_tarefa(LocalDateTime data_hora_tarefa) {
        this.data_hora_tarefa = data_hora_tarefa;
    }

    public String getData_tarefa_fimString() {

            return data_hora_tarefa_fim.toString();

    }

    public void setData_tarefa_fim(LocalDateTime data_hora_tarefa_fim) {
        this.data_hora_tarefa_fim = data_hora_tarefa_fim;
    }

    public String getData_tarefa_insertString() {

            return data_hora_tarefa_insert.toString();

    }

    public void setData_tarefa_insert(LocalDateTime data_hora_tarefa_insert) {
        this.data_hora_tarefa_insert = data_hora_tarefa_insert;
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
