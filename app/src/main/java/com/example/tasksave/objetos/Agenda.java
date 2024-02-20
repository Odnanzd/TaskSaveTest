package com.example.tasksave.objetos;

import android.annotation.SuppressLint;

import java.time.LocalDate;

public class Agenda {
    static long id;
    private String nomeAgenda;
    private String descriçãoAgenda;
    private LocalDate dataAgenda;
    private int horaAgenda;

    private int minutoAgenda;
    private boolean lembrete;
    private boolean finalizado;
    private LocalDate dataAgendaFim;
    private int horaAgendaFim;

    private int minutoAgendaFim;
    private LocalDate dataAgendaInsert;
    private int horaAgendaInsert;
    private int minutoAgendaInsert;
    private boolean agendaAtraso;

    private boolean repetirLembrete;

    private int repetirModo;




    public Agenda(long id, String nomeAgenda, String descriçãoAgenda, LocalDate dataAgenda,
                  int horaAgenda, int minutoAgenda, boolean lembrete, boolean finalizado,
                  LocalDate dataAgendaFim, int horaAgendaFim, int minutoAgendaFim,
                  LocalDate dataAgendaInsert, int horaAgendaInsert, int minutoAgendaInsert, boolean agendaAtraso, boolean repetirLembrete, int repetirModo) {

        this.id = id;
        this.nomeAgenda = nomeAgenda;
        this.descriçãoAgenda = descriçãoAgenda;
        this.dataAgenda = dataAgenda;
        this.horaAgenda = horaAgenda;
        this.minutoAgenda = minutoAgenda;
        this.lembrete = lembrete;
        this.finalizado = finalizado;
        this.horaAgendaFim = horaAgendaFim;
        this.minutoAgendaFim = minutoAgendaFim;
        this.dataAgendaFim = dataAgendaFim;
        this.dataAgendaInsert = dataAgendaInsert;
        this.horaAgendaInsert = horaAgendaInsert;
        this.minutoAgendaInsert = minutoAgendaInsert;
        this.agendaAtraso = agendaAtraso;
        this.repetirLembrete = repetirLembrete;
        this.repetirModo = repetirModo;

    }

    public String getNomeAgenda() {
        return nomeAgenda;
    }

    public String getDescriçãoAgenda() {
        return descriçãoAgenda;
    }

    public String getDataAgendaString() {
        if (dataAgenda != null) {
            return dataAgenda.toString();
        } else {
            return null;
        }
    }

    public int getHoraAgenda() {
        return horaAgenda;
    }

    public void setNomeAgenda(String nomeAgenda) {
        this.nomeAgenda = nomeAgenda;
    }

    public void setDescriçãoAgenda(String descriçãoAgenda) {
        this.descriçãoAgenda = descriçãoAgenda;
    }

    @SuppressLint("NewApi")
    public void setDataAgendaString(String dataAgendaString) {
        if (dataAgendaString != null) {
            this.dataAgenda = LocalDate.parse(dataAgendaString);
        } else {
            this.dataAgenda = null;
        }
    }

    public void setHoraAgenda(int horaAgenda) {
        this.horaAgenda = horaAgenda;
    }

    public int getMinutoAgenda() {
        return minutoAgenda;
    }

    public void setMinutoAgenda(int minutoAgenda) {
        this.minutoAgenda = minutoAgenda;
    }

    public boolean getLembrete() {
        return lembrete;
    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        Agenda.id = id;
    }
    public boolean getFinalizado() {
        return finalizado;
    }
    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
    public String getDataAgendaFimString() {
        if (dataAgendaFim != null) {
            return dataAgendaFim.toString();
        } else {
            return null;
        }
    }
    @SuppressLint("NewApi")
    public void setDataAgendaFimString(String dataAgendaFimString) {
        if (dataAgendaFimString != null) {
            this.dataAgendaFim = LocalDate.parse(dataAgendaFimString);
        } else {
            this.dataAgendaFim = null;
        }
    }

    public int getHoraAgendaFim() {
        return horaAgendaFim;
    }
    public void setHoraAgendaFim(int horaAgendaFim) {
        this.horaAgendaFim = horaAgendaFim;
    }
    public int getMinutoAgendaFim() {
        return minutoAgendaFim;
    }

    public void setMinutoAgendaFim(int minutoAgendaFim) {
        this.minutoAgendaFim = minutoAgendaFim;
    }
    public String getDataAgendaInsertString() {
        if (dataAgendaInsert != null) {
            return dataAgendaInsert.toString();
        } else {
            return null;
        }
    }
    @SuppressLint("NewApi")
    public void setDataAgendaInsertString(String dataAgendaInsertString) {
        if (dataAgendaInsertString != null) {
            this.dataAgendaInsert = LocalDate.parse(dataAgendaInsertString);
        } else {
            this.dataAgendaInsert = null;
        }
    }
    public int getHoraAgendaInsert() {
        return horaAgendaInsert;
    }
    public void setHoraAgendaInsert(int horaAgendaInsert) {
        this.horaAgendaInsert = horaAgendaInsert;
    }
    public int getMinutoAgendaInsert() {
        return minutoAgendaInsert;
    }

    public void setMinutoAgendaInsert(int minutoAgendaInsert) {
        this.minutoAgendaInsert = minutoAgendaInsert;
    }
    public boolean getAgendaAtraso() {
        return agendaAtraso;
    }
    public void setAgendaAtraso(boolean agendaAtraso) {
        this.agendaAtraso = agendaAtraso;
    }

    public boolean getRepetirLembrete() {
        return repetirLembrete;
    }

    public void setRepetirLembrete() {
        this.repetirLembrete = repetirLembrete;
    }

    public int getRepetirModo() {
        return repetirModo;
    }
    public void setRepetirModo() {
        this.repetirModo = repetirModo;
    }

}
