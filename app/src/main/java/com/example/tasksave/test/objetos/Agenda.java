package com.example.tasksave.test.objetos;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDataAgendaString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dataHoraAgenda.format(formatter);

        return formattedDateTime;

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
    public void setDataAgendaString(LocalDateTime dataAgendaString) {
        if (dataAgendaString != null) {
            this.dataHoraAgenda = dataAgendaString;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDataAgendaFimString() {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dataHoraAgendaInsert.format(formatter);

            return formattedDateTime;

    }
    @SuppressLint("NewApi")
    public void setDataAgendaFimString(String dataAgendaFimString) {
        if (dataAgendaFimString != null) {
            this.dataHoraAgendaFim = LocalDateTime.parse(dataAgendaFimString);
        } else {
            this.dataHoraAgendaFim = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDataAgendaInsertString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dataHoraAgendaInsert.format(formatter);

            return formattedDateTime;

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

    public void setRepetirLembrete(boolean repetirLembrete) {
        this.repetirLembrete = repetirLembrete;
    }

    public int getRepetirModo() {
        return repetirModo;
    }
    public void setRepetirModo(int repetirModo) {
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

        // Configura o Calendar com os valores do LocalDateTime
        calendar.set(Calendar.YEAR, dataHoraAgenda.getYear());
        calendar.set(Calendar.MONTH, dataHoraAgenda.getMonthValue() - 1); // Mês no Calendar é 0-based
        calendar.set(Calendar.DAY_OF_MONTH, dataHoraAgenda.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, dataHoraAgenda.getHour());
        calendar.set(Calendar.MINUTE, dataHoraAgenda.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0); // Configura os milissegundos para 0

        return calendar.getTimeInMillis();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Calendar getCalendarTime() {

        ZonedDateTime zonedDateTime = dataHoraAgenda.atZone(ZoneId.systemDefault());

        Instant instant = zonedDateTime.toInstant();

        Date date = Date.from(instant);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        return calendar;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return
                lembrete == agenda.lembrete &&
                finalizado == agenda.finalizado &&
                agendaAtraso == agenda.agendaAtraso &&
                repetirLembrete == agenda.repetirLembrete &&
                repetirModo == agenda.repetirModo &&
                notificouTarefa == agenda.isNotificado() &&
                Objects.equals(nomeAgenda, agenda.nomeAgenda) &&
                Objects.equals(descriçãoAgenda, agenda.descriçãoAgenda) &&
                Objects.equals(dataHoraAgenda, agenda.dataHoraAgenda) &&
                Objects.equals(dataHoraAgendaFim, agenda.dataHoraAgendaFim) &&
                Objects.equals(dataHoraAgendaInsert, agenda.dataHoraAgendaInsert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeAgenda, descriçãoAgenda, dataHoraAgenda, lembrete, finalizado, dataHoraAgendaFim,
                dataHoraAgendaInsert,
                agendaAtraso, repetirLembrete, repetirModo, notificouTarefa);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getHoraAgenda() {

        return dataHoraAgenda.getHour();


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getMinutoAgenda() {
        return dataHoraAgenda.getMinute();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getLocalDateAgenda () {

        LocalDate localDateAgenda = dataHoraAgenda.toLocalDate();
        return localDateAgenda;
    }
}

