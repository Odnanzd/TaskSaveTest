package com.example.tasksave.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tasksave.conexaoSQLite.Conexao;
import com.example.tasksave.objetos.Agenda;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class AgendaDAO {

    private Conexao con;
    private SQLiteDatabase db;

    public AgendaDAO(Context context) {

        con = new Conexao(context);
        db = con.getWritableDatabase();

    }

    public long inserir(Agenda agenda) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("nomeTarefa", agenda.getNomeAgenda());
        contentValues.put("descricaoTarefa", agenda.getDescriçãoAgenda());
        contentValues.put("dataAgenda", agenda.getDataAgendaString());
        contentValues.put("horaAgenda", agenda.getHoraAgenda());
        contentValues.put("minutoAgenda", agenda.getMinutoAgenda());
        contentValues.put("lembretedefinido", agenda.getLembrete());
        contentValues.put("finalizado", agenda.getFinalizado());
        contentValues.put("dataAgendaFim", agenda.getDataAgendaFimString());
        contentValues.put("horaAgendaFim", agenda.getHoraAgendaFim());
        contentValues.put("minutoAgendaFim", agenda.getMinutoAgendaFim());
        contentValues.put("dataAgendaInsert", agenda.getDataAgendaInsertString());
        contentValues.put("horaAgendaInsert", agenda.getHoraAgendaInsert());
        contentValues.put("minutoAgendaInsert", agenda.getMinutoAgendaInsert());
        contentValues.put("agendaAtraso", agenda.getAgendaAtraso());
        contentValues.put("repetirLembrete", agenda.getRepetirLembrete());
        contentValues.put("repetirModo", agenda.getRepetirModo());
        contentValues.put("notificouTarefa", agenda.isNotificado());

        return db.insert("agenda", null, contentValues);

    }

    public boolean Atualizar(long id, String novoTitulo, String novaDescricao) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("nomeTarefa", novoTitulo);
        contentValues.put("descricaoTarefa", novaDescricao);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }

    public boolean Excluir(long id) {

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.delete("agenda", whereClause, whereArgs);

        return rowsUpdated > 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Agenda> obterTarefasComLembreteAtivado() {

        LocalDate dataAtual = LocalDate.now();
        List<Agenda> tarefasComLembrete = new ArrayList<>();

        String[] colunas = {
                "id", "nomeTarefa", "descricaoTarefa", "dataAgenda", "horaAgenda", "minutoAgenda",
                "lembretedefinido", "finalizado", "repetirLembrete", "repetirModo", "notificouTarefa"};

        String whereClause = "lembretedefinido = ?";
        String[] whereArgs = {"1"}; // Lembrete ativado

        Cursor cursor = db.query("agenda", colunas, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                @SuppressLint("Range")
                String titulo = cursor.getString(cursor.getColumnIndex("nomeTarefa"));
                @SuppressLint("Range")
                String descricao = cursor.getString(cursor.getColumnIndex("descricaoTarefa"));
                @SuppressLint("Range")
                String dataAgenda = cursor.getString(cursor.getColumnIndex("dataAgenda"));
                @SuppressLint("Range")
                int horaAgenda = cursor.getInt(cursor.getColumnIndex("horaAgenda"));
                @SuppressLint("Range")
                int minutoAgenda = cursor.getInt(cursor.getColumnIndex("minutoAgenda"));
                @SuppressLint("Range")
                int finalizado = cursor.getInt(cursor.getColumnIndex("finalizado"));
                boolean finalizadobl = (finalizado != 0);
                @SuppressLint("Range")
                int repetirLembrete = cursor.getInt(cursor.getColumnIndex("repetirLembrete"));
                boolean repetirLembretebl = (repetirLembrete != 0);
                @SuppressLint("Range")
                int repetirModo = cursor.getInt(cursor.getColumnIndex("repetirModo"));
                @SuppressLint("Range")
                int notificouTarefaDB = cursor.getInt(cursor.getColumnIndex("notificouTarefa"));
                boolean notificouTarefa = (notificouTarefaDB != 0);


                LocalDate localDataAgenda = LocalDate.parse(dataAgenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                tarefasComLembrete.add(new Agenda(id, titulo, descricao, localDataAgenda, horaAgenda,
                        minutoAgenda, true, finalizadobl, dataAtual, -1, -1, dataAtual,
                        -1, -1, false, repetirLembretebl, repetirModo, notificouTarefa));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return tarefasComLembrete;
    }

    public boolean AtualizarStatusNotificacao(long id, int Status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("notificouTarefa", Status);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }

    public boolean AtualizarStatus(long id, int Status, LocalDate dataAgendaFim, int horaAgendaFim, int minutoAgendaFim) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("finalizado", Status);
        contentValues.put("dataAgendaFim", String.valueOf(dataAgendaFim));
        contentValues.put("horaAgendaFim", horaAgendaFim);
        contentValues.put("minutoAgendaFim", minutoAgendaFim);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }
    public boolean AtualizarStatusAtraso(long id, int status) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("agendaAtraso", status);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }
    public boolean AtualizarDataTarefa(long id, LocalDate dataTarefa) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("dataAgenda", String.valueOf(dataTarefa));

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }






    }


