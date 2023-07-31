package com.example.tasksave;

import java.util.Date;

public class Agenda {

    private String nomeAgenda;
    private String descriçãoAgenda;
    private Date dataAgenda;



    public Agenda(String nomeAgenda, String descriçãoAgenda, Date dataAgenda) {
        this.nomeAgenda = nomeAgenda;
        this.descriçãoAgenda = descriçãoAgenda;
        this.dataAgenda = dataAgenda;
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

    public void setNomeAgenda(String nomeAgenda) {
        this.nomeAgenda = nomeAgenda;
    }

    public void setDescriçãoAgenda(String descriçãoAgenda) {
        this.descriçãoAgenda = descriçãoAgenda;
    }

    public void setDataAgenda(Date dataAgenda) {
        this.dataAgenda = dataAgenda;
    }
}
