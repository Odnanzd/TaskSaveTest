package com.example.tasksave;

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




    public Agenda(long id, String nomeAgenda, String descriçãoAgenda, LocalDate dataAgenda,
                  int horaAgenda, int minutoAgenda, boolean lembrete, boolean finalizado,
                  LocalDate dataAgendaFim, int horaAgendaFim, int minutoAgendaFim ) {

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
    public void setDataAgendaFimString(String dataAgendaString) {
        if (dataAgendaString != null) {
            this.dataAgendaFim = LocalDate.parse(dataAgendaString);
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

}
