package com.example.tasksave;

import android.annotation.SuppressLint;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

public class Agenda {

    private String nomeAgenda;
    private String descriçãoAgenda;
    private LocalDate dataAgenda;
    private int horaAgenda;

    private int minutoAgenda;



    public Agenda(String nomeAgenda, String descriçãoAgenda, LocalDate dataAgenda, int horaAgenda, int minutoAgenda) {

        this.nomeAgenda = nomeAgenda;
        this.descriçãoAgenda = descriçãoAgenda;
        this.dataAgenda = dataAgenda;
        this.horaAgenda = horaAgenda;
        this.minutoAgenda = minutoAgenda;

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
}
