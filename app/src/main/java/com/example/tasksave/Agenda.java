package com.example.tasksave;

import java.sql.Time;
import java.util.Date;

public class Agenda {

    private String nomeAgenda;
    private String descriçãoAgenda;
    private Date dataAgenda;
    private Date horaAgenda;



    public Agenda(String nomeAgenda, String descriçãoAgenda) {
        this.nomeAgenda = nomeAgenda;
        this.descriçãoAgenda = descriçãoAgenda;
        this.dataAgenda = dataAgenda;
        this.horaAgenda = horaAgenda;

    }

    public String getNomeAgenda() {
        return nomeAgenda;
    }

    public String getDescriçãoAgenda() {
        return descriçãoAgenda;
    }

    public Date getDataAgenda() {
        return dataAgenda;
    }

    public Date getHoraAgenda() {
        return horaAgenda;
    }

    public void setNomeAgenda(String nomeAgenda) {
        this.nomeAgenda = nomeAgenda;
    }

    public void setDescriçãoAgenda(String descriçãoAgenda) {
        this.descriçãoAgenda = descriçãoAgenda;
    }

    public void setDataAgenda(Date dataAgenda) {
        this.dataAgenda = dataAgenda;
    }

    public void setHoraAgenda(Date horaAgenda) {
        this.horaAgenda = horaAgenda;
    }
}
