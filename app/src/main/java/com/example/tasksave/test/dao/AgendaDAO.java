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
import com.example.tasksave.test.objetos.AgendaAWS;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AgendaDAO {

    private Conexao con;
    private SQLiteDatabase db;

    public AgendaDAO(Context context) {

        con = new Conexao(context);
        db = con.getWritableDatabase();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long inserir(Agenda agenda) {

        String[] columns = {"id"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(agenda.getId())};

        Cursor cursor = db.query("agenda", columns, selection, selectionArgs, null, null, null);

        ContentValues contentValues = new ContentValues();

        if(agenda.getLembrete()) {

            contentValues.put("dataHoraAgenda", agenda.getDataAgendaString());

            contentValues.putNull("dataHoraAgendaFim");

        }else {

            contentValues.putNull("dataHoraAgenda");

            contentValues.putNull("dataHoraAgendaFim");


        }

        contentValues.put("nomeTarefa", agenda.getNomeAgenda());
        contentValues.put("descricaoTarefa", agenda.getDescriçãoAgenda());
//        contentValues.put("dataAgenda", agenda.getDataAgendaString());
//        contentValues.put("horaAgenda", agenda.getHoraAgenda());
//        contentValues.put("minutoAgenda", agenda.getMinutoAgenda());
        contentValues.put("lembretedefinido", agenda.getLembrete());
        contentValues.put("finalizado", agenda.getFinalizado());
//        contentValues.put("dataAgendaFim", agenda.getDataAgendaFimString());
//        contentValues.put("horaAgendaFim", agenda.getHoraAgendaFim());
//        contentValues.put("minutoAgendaFim", agenda.getMinutoAgendaFim());
        contentValues.put("dataHoraAgendaInsert", agenda.getDataAgendaInsertString());
        contentValues.put("agendaAtraso", agenda.getAgendaAtraso());
        contentValues.put("repetirLembrete", agenda.getRepetirLembrete());
        contentValues.put("repetirModo", agenda.getRepetirModo());
        contentValues.put("notificouTarefa", agenda.isNotificado());
        contentValues.put("id", agenda.getId());

        long result;
        if (cursor.moveToFirst()) {
            // Registro já existe, então atualize
            result = db.update("agenda", contentValues, selection, selectionArgs);

        } else {
            // Registro não existe, então insira
            result = db.insert("agenda", null, contentValues);

        }

        cursor.close();
        return result;
    }

    public boolean atualizarTitDesc(Agenda agenda) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("nomeTarefa", agenda.getNomeAgenda());
        contentValues.put("descricaoTarefa", agenda.getDescriçãoAgenda());
        contentValues.put("agendaAtraso", 0);
        contentValues.put("lembretedefinido", false);
        contentValues.put("repetirLembrete", false);
        contentValues.put("repetirModo", 0);
        contentValues.put("notificouTarefa", 0);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(agenda.getId())};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean atualizarAll(Agenda agenda) {

        ContentValues contentValues = new ContentValues();

        if(agenda.getLembrete()) {
            contentValues.put("dataHoraAgenda", String.valueOf(agenda.getDataAgendaString()));
        }else {
            contentValues.putNull("dataHoraAgenda");
        }

        contentValues.put("nomeTarefa", agenda.getNomeAgenda());
        contentValues.put("descricaoTarefa", agenda.getDescriçãoAgenda());
        contentValues.put("agendaAtraso", agenda.getAgendaAtraso());
        contentValues.put("lembretedefinido", agenda.getLembrete());
        contentValues.put("repetirLembrete", agenda.getRepetirLembrete());
        contentValues.put("repetirModo", agenda.getRepetirModo());

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(agenda.getId())};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);

        return rowsUpdated > 0;
    }

    public boolean Excluir(long id) {

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsUpdated = db.delete("agenda", whereClause, whereArgs);

        return rowsUpdated > 0;
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public List<Agenda> obterTarefasComLembreteAtivado() {
//
//        LocalDate dataAtual = LocalDate.now();
//        List<Agenda> tarefasComLembrete = new ArrayList<>();
//
//        String[] colunas = {
//                "id", "nomeTarefa", "descricaoTarefa", "dataAgenda", "horaAgenda", "minutoAgenda",
//                "lembretedefinido", "finalizado", "repetirLembrete", "repetirModo", "notificouTarefa"};
//
//        String whereClause = "lembretedefinido = ?";
//        String[] whereArgs = {"1"}; // Lembrete ativado
//
//        Cursor cursor = db.query("agenda", colunas, whereClause, whereArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range")
//                int id = cursor.getInt(cursor.getColumnIndex("id"));
//                @SuppressLint("Range")
//                String titulo = cursor.getString(cursor.getColumnIndex("nomeTarefa"));
//                @SuppressLint("Range")
//                String descricao = cursor.getString(cursor.getColumnIndex("descricaoTarefa"));
//                @SuppressLint("Range")
//                String dataAgenda = cursor.getString(cursor.getColumnIndex("dataAgenda"));
//                @SuppressLint("Range")
//                int horaAgenda = cursor.getInt(cursor.getColumnIndex("horaAgenda"));
//                @SuppressLint("Range")
//                int minutoAgenda = cursor.getInt(cursor.getColumnIndex("minutoAgenda"));
//                @SuppressLint("Range")
//                int finalizado = cursor.getInt(cursor.getColumnIndex("finalizado"));
//                boolean finalizadobl = (finalizado != 0);
//                @SuppressLint("Range")
//                int repetirLembrete = cursor.getInt(cursor.getColumnIndex("repetirLembrete"));
//                boolean repetirLembretebl = (repetirLembrete != 0);
//                @SuppressLint("Range")
//                int repetirModo = cursor.getInt(cursor.getColumnIndex("repetirModo"));
//                @SuppressLint("Range")
//                int notificouTarefaDB = cursor.getInt(cursor.getColumnIndex("notificouTarefa"));
//                boolean notificouTarefa = (notificouTarefaDB != 0);
//
//
//                LocalDate localDataAgenda = LocalDate.parse(dataAgenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//                tarefasComLembrete.add(new Agenda(id, titulo, descricao, localDataAgenda, horaAgenda,
//                        minutoAgenda, true, finalizadobl, dataAtual, -1, -1, dataAtual,
//                        -1, -1, 0, repetirLembretebl, repetirModo, notificouTarefa));
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//
//        return tarefasComLembrete;
//    }

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean atualizarDataTarefa(long id, LocalDateTime dataHoraTarefa) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dataHoraTarefa.format(formatter);

        ContentValues contentValues = new ContentValues();
        contentValues.put("dataHoraAgenda", String.valueOf(formattedDateTime));

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
    @SuppressLint("Range")
    public String dataTarefaInsert(int id) {

        String[] colunas = {"id", "dataHoraAgendaInsert"};

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        Cursor cursor = db.query("agenda", colunas, whereClause, whereArgs, null, null, null);

        String data = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) { // Move the cursor to the first row
                    data = cursor.getString(cursor.getColumnIndex("dataHoraAgendaInsert"));
                }
            } finally {
                cursor.close(); // Ensure the cursor is closed to avoid memory leaks
            }
        }

        return data;
    }



    public boolean hasTwoOrMoreTasks(){

        Cursor cursor = db.rawQuery("SELECT * FROM agenda WHERE finalizado = 0;", null);
        if(cursor.getCount()>1) {
            return true;
        }else {
            return false;
        }

    }
    public void excluiTabelas() {

        try {
            String sql = "DELETE FROM agenda";
            String sql2 = "DELETE FROM alarme";
            db.execSQL(sql);
            db.execSQL(sql2);
            Log.d("DatabaseHelper", "Tabela " + "agenda" + " excluída com sucesso.");
        } finally {
            db.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Agenda> tarefasAgenda() {

        List<Agenda> tarefasComLembrete = new ArrayList<>();

        LocalDate localdataagenda=null;
        LocalDate localdataagendaFim =null;

        String sql = "SELECT * FROM agenda WHERE finalizado = 0";


        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                    @SuppressLint("Range")
                    int ID = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range")
                    String titulo = cursor.getString(cursor.getColumnIndex("nomeTarefa"));
                    @SuppressLint("Range")
                    String descricao = cursor.getString(cursor.getColumnIndex("descricaoTarefa"));
                    @SuppressLint("Range")
                    String dataagenda = cursor.getString(cursor.getColumnIndex("dataAgenda"));
                if (dataagenda!=null) {
                    localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                    @SuppressLint("Range")
                    int horaagenda = cursor.getInt(cursor.getColumnIndex("horaAgenda"));
                    @SuppressLint("Range")
                    int minutoagenda = cursor.getInt(cursor.getColumnIndex("minutoAgenda"));
                    @SuppressLint("Range")
                    int lembreteDB = cursor.getInt(cursor.getColumnIndex("lembretedefinido"));
                    boolean lembrete = (lembreteDB != 0);
                    @SuppressLint("Range")
                    int finalizadoDB = cursor.getInt(cursor.getColumnIndex("finalizado"));
                    boolean finalizado = (finalizadoDB != 0);
                    @SuppressLint("Range")
                    String dataagendaFim = cursor.getString(cursor.getColumnIndex("dataAgendaFim"));
                if (dataagendaFim!=null) {
                    localdataagendaFim = LocalDate.parse(dataagendaFim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                    @SuppressLint("Range")
                    int horaAgendaFim = cursor.getInt(cursor.getColumnIndex("horaAgendaFim"));
                    @SuppressLint("Range")
                    int minutoAgendaFim = cursor.getInt(cursor.getColumnIndex("minutoAgendaFim"));
                    @SuppressLint("Range")
                    String dataAgendaInsert = cursor.getString(cursor.getColumnIndex("dataAgendaInsert"));
                    @SuppressLint("Range")
                    int horaAgendaInsert = cursor.getInt(cursor.getColumnIndex("horaAgendaInsert"));
                    @SuppressLint("Range")
                    int minutoAgendaInsert = cursor.getInt(cursor.getColumnIndex("minutoAgendaInsert"));
                    @SuppressLint("Range")
                    int agendaAtrasoDB = cursor.getInt(cursor.getColumnIndex("agendaAtraso"));
                    @SuppressLint("Range")
                    int repetirLembreteDB = cursor.getInt(cursor.getColumnIndex("repetirLembrete"));
                    boolean repetirLembrete = (repetirLembreteDB != 0);
                    @SuppressLint("Range")
                    int repetirLembreteModo = cursor.getInt(cursor.getColumnIndex("repetirModo"));
                    @SuppressLint("Range")
                    int notificouTarefaDB = cursor.getInt(cursor.getColumnIndex("notificouTarefa"));
                    boolean notificouTarefa = (notificouTarefaDB != 0);


//                    LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                    LocalDate localdataagendaFim = LocalDate.parse(dataagendaFim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate localdataagendaInsert = LocalDate.parse(dataAgendaInsert, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

//                    tarefasComLembrete.add(new Agenda(ID, titulo, descricao, localdataagenda, horaagenda, minutoagenda,
//                            lembrete, finalizado, localdataagendaFim, horaAgendaFim, minutoAgendaFim, localdataagendaInsert,
//                            horaAgendaInsert, minutoAgendaInsert, agendaAtrasoDB, repetirLembrete, repetirLembreteModo, notificouTarefa));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return tarefasComLembrete;
    }

    @SuppressLint("Range")
    public int ultimaTarefa() {

        String query = "SELECT * FROM agenda ORDER BY id DESC LIMIT 1";
        int idTarefa = 0;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            idTarefa = cursor.getInt(cursor.getColumnIndex("id"));
        }

        return idTarefa;

    }
    public boolean atualizaID(int idAntigo, int novoId) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", novoId);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idAntigo)};

        int rowsUpdated = db.update("agenda", contentValues, whereClause, whereArgs);


        if (rowsUpdated>0) {
            return true;
        }
        return false;

    }



    }


