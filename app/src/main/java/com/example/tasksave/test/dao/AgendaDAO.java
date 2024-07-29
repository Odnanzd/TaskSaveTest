package com.example.tasksave.test.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.example.tasksave.test.baseadapter.CustomAdapter;
import com.example.tasksave.test.conexaoSQLite.Conexao;
import com.example.tasksave.test.objetos.Agenda;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AgendaDAO {

    private Conexao con;
    private SQLiteDatabase db;

    public AgendaDAO(Context context) {

        con = new Conexao(context);
        db = con.getWritableDatabase();

    }

    public long inserir(Agenda agenda) {

        String[] columns = {"id"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(agenda.getId())};

        Cursor cursor = db.query("agenda", columns, selection, selectionArgs, null, null, null);

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
        contentValues.put("id", agenda.getId());

        long result;
        if (cursor.moveToFirst()) {
            // Registro já existe, então atualize
            result = db.update("agenda", contentValues, selection, selectionArgs);
            Log.d("IF CURSOR MOVE TO FIRST IF 1", "IF 1");
        } else {
            // Registro não existe, então insira
            result = db.insert("agenda", null, contentValues);
            Log.d("IF CURSOR MOVE TO FIRST IF 2", "IF 2");
        }

        cursor.close();
        return result;
    }

    public boolean atualizarTitDesc(long id, String novoTitulo, String novaDescricao) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("nomeTarefa", novoTitulo);
        contentValues.put("descricaoTarefa", novaDescricao);
        contentValues.put("agendaAtraso", 0);
        contentValues.put("lembretedefinido", false);
        contentValues.put("repetirLembrete", false);
        contentValues.put("repetirModo", 0);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }
    public boolean atualizarAll(long id, String tituloTarefa, String descTarefa, LocalDate localDateData,
                                int horaAgenda, int minutoAgenda, int lembrete, int repetir, int repetirModo, int agendaAtraso ) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("nomeTarefa", tituloTarefa);
        contentValues.put("descricaoTarefa", descTarefa);
        contentValues.put("dataAgenda", String.valueOf(localDateData));
        contentValues.put("horaAgenda", horaAgenda);
        contentValues.put("minutoAgenda", minutoAgenda);
        contentValues.put("agendaAtraso", agendaAtraso);
        contentValues.put("lembretedefinido", lembrete);
        contentValues.put("repetirLembrete", repetir);
        contentValues.put("repetirModo", repetirModo);

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
                int id = cursor.getInt(cursor.getColumnIndex("id"));
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
                        -1, -1, 0, repetirLembretebl, repetirModo, notificouTarefa));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return tarefasComLembrete;
    }

    public ArrayList<Long> idTarefasLembrete() {

        ArrayList<Long> ids = new ArrayList<>();

        String[] colunas = {"id", "lembretedefinido"};

        String whereClause = "lembretedefinido = ?";
        String[] whereArgs = {"1"}; // Lembrete ativado

        Cursor cursor = db.query("agenda", colunas, whereClause, whereArgs, null, null, null);


        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                ids.add(id);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return ids;
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
    public boolean atualizarTarefaPendente(long id, int status) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("agendaAtraso", status);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }
    public boolean atualizarTarefaAtraso(long id, int status) {

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


        if (rowsUpdated>0) {
            return true;
        }
        return false;
    }
    public String verificaTarefaPendente() {

        Cursor cursor = db.rawQuery("SELECT agendaAtraso FROM agenda WHERE agendaAtraso = 1;", null);

        String contador = String.valueOf(cursor.getCount());

        return contador;

    }



    public boolean hasTwoOrMoreTasks(){

        Cursor cursor = db.rawQuery("SELECT * FROM agenda WHERE finalizado = 0;", null);
        if(cursor.getCount()>1) {
            return true;
        }else {
            return false;
        }

    }
    public void excluiTabelaAgenda() {

        try {
            String sql = "DELETE FROM agenda";
            db.execSQL(sql);
            Log.d("DatabaseHelper", "Tabela " + "agenda" + " excluída com sucesso.");
        } finally {
            db.close();
        }
    }


    }


