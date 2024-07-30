package com.example.tasksave.test.objetos;

import android.annotation.SuppressLint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

public class Agenda {
    private int id;
    private String nomeAgenda;
    private String descriçãoAgenda;
    private LocalDateTime dataHoraAgenda;
    private boolean lembrete;
    private boolean finalizado;
    private LocalDateTime dataHoraAgendaFim;
    private LocalDateTime dataHoraAgendaInsert;
    private int agendaAtraso;

    private boolean repetirLembrete;

    private int repetirModo;
    private boolean notificouTarefa;


    public Agenda(int id, String nomeAgenda, String descriçãoAgenda, LocalDateTime dataHoraAgenda, boolean lembrete, boolean finalizado,
                  LocalDateTime dataHoraAgendaFim, LocalDateTime dataHoraAgendaInsert, int agendaAtraso, boolean repetirLembrete,
                  int repetirModo, boolean notificouTarefa) {

        this.id = id;
        this.nomeAgenda = nomeAgenda;
        this.descriçãoAgenda = descriçãoAgenda;
        this.dataHoraAgenda = dataHoraAgenda;
        this.lembrete = lembrete;
        this.finalizado = finalizado;
        this.dataHoraAgendaFim = dataHoraAgendaFim;
        this.dataHoraAgendaInsert = dataHoraAgendaInsert;
        this.agendaAtraso = agendaAtraso;
        this.repetirLembrete = repetirLembrete;
        this.repetirModo = repetirModo;
        this.notificouTarefa = notificouTarefa;

    }

    public String getNomeAgenda() {
        return nomeAgenda;
    }

    public String getDescriçãoAgenda() {
        return descriçãoAgenda;
    }

    public String getDataAgendaString() {

        return dataHoraAgenda.toString();
    }
    public LocalDateTime getDate() {
        return dataHoraAgenda;
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
            this.dataHoraAgenda = LocalDateTime.parse(dataAgendaString);
        } else {
            this.dataHoraAgenda = null;
        }
    }

    public boolean getLembrete() {
        return lembrete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getFinalizado() {
        return finalizado;
    }
    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
    public String getDataAgendaFimString() {

            return dataHoraAgendaFim.toString();

    }
    @SuppressLint("NewApi")
    public void setDataAgendaFimString(String dataAgendaFimString) {
        if (dataAgendaFimString != null) {
            this.dataHoraAgendaFim = LocalDateTime.parse(dataAgendaFimString);
        } else {
            this.dataHoraAgendaFim = null;
        }
    }

    public String getDataAgendaInsertString() {

            return dataHoraAgendaInsert.toString();

    }
    @SuppressLint("NewApi")
    public void setDataAgendaInsertString(String dataAgendaInsertString) {
        if (dataAgendaInsertString != null) {
            this.dataHoraAgendaInsert = LocalDateTime.parse(dataAgendaInsertString);
        } else {
            this.dataHoraAgendaInsert = null;
        }
    }

    public int getAgendaAtraso() {
        return agendaAtraso;
    }
    public void setAgendaAtraso(int agendaAtraso) {
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
    public boolean isNotificado() {
        return notificouTarefa;
    }

    public void setNotificado(boolean notificouTarefa) {
        this.notificouTarefa = notificouTarefa;
    }
    @SuppressLint("NewApi")
    public long getDateTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, dataAgenda.getYear());
        calendar.set(Calendar.MONTH, dataAgenda.getMonthValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dataAgenda.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, horaAgenda);
        calendar.set(Calendar.MINUTE, minutoAgenda);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return horaAgenda == agenda.horaAgenda &&
                minutoAgenda == agenda.minutoAgenda &&
                lembrete == agenda.lembrete &&
                finalizado == agenda.finalizado &&
                horaAgendaFim == agenda.horaAgendaFim &&
                minutoAgendaFim == agenda.minutoAgendaFim &&
                horaAgendaInsert == agenda.horaAgendaInsert &&
                minutoAgendaInsert == agenda.minutoAgendaInsert &&
                agendaAtraso == agenda.agendaAtraso &&
                repetirLembrete == agenda.repetirLembrete &&
                repetirModo == agenda.repetirModo &&
                notificouTarefa == agenda.isNotificado() &&
                Objects.equals(nomeAgenda, agenda.nomeAgenda) &&
                Objects.equals(descriçãoAgenda, agenda.descriçãoAgenda) &&
                Objects.equals(dataAgenda, agenda.dataAgenda) &&
                Objects.equals(dataAgendaFim, agenda.dataAgendaFim) &&
                Objects.equals(dataAgendaInsert, agenda.dataAgendaInsert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeAgenda, descriçãoAgenda, dataAgenda, horaAgenda, minutoAgenda, lembrete, finalizado, dataAgendaFim, horaAgendaFim, minutoAgendaFim, dataAgendaInsert, horaAgendaInsert, minutoAgendaInsert, agendaAtraso, repetirLembrete, repetirModo, notificouTarefa);
    }
}

