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

import com.example.tasksave.test.activities.activity_agenda;
import com.example.tasksave.test.baseadapter.CustomAdapter;
import com.example.tasksave.test.conexaoSQLite.Conexao;
import com.example.tasksave.test.objetos.Agenda;

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
                        -1, -1, 0, repetirLembretebl, repetirModo, notificouTarefa));
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

        return rowsUpdated > 0;
    }
    public String verificaTarefaPendente() {

        Cursor cursor = db.rawQuery("SELECT agendaAtraso FROM agenda WHERE agendaAtraso = 1;", null);

        String contador = String.valueOf(cursor.getCount());

        return contador;

    }

    //LISTAR AGENDA PENDENTE

    public void listarAgendaPendentes(ArrayList<Long> listaIDs, ArrayList<Integer> repetirModoLembrete2, Context context, ListView listView) {


        List<Agenda> listaagenda = new ArrayList<Agenda>();
        listaIDs.clear();
        repetirModoLembrete2.clear();

        Cursor cursor = db.rawQuery("SELECT * FROM agenda WHERE finalizado = 0 AND agendaAtraso = 1;", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                long ID = cursor.getLong(cursor.getColumnIndex("id"));
                @SuppressLint("Range")
                String titulo = cursor.getString(cursor.getColumnIndex("nomeTarefa"));
                @SuppressLint("Range")
                String descricao = cursor.getString(cursor.getColumnIndex("descricaoTarefa"));
                @SuppressLint("Range")
                String dataagenda = cursor.getString(cursor.getColumnIndex("dataAgenda"));
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


                @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate localdataagendaFim = LocalDate.parse(dataagendaFim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate localdataagendaInsert = LocalDate.parse(dataAgendaInsert, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                listaagenda.add(new Agenda(ID, titulo, descricao, localdataagenda, horaagenda, minutoagenda,
                        lembrete, finalizado, localdataagendaFim, horaAgendaFim, minutoAgendaFim, localdataagendaInsert,
                        horaAgendaInsert, minutoAgendaInsert, agendaAtrasoDB, repetirLembrete, repetirLembreteModo, notificouTarefa));
                listaIDs.add(ID);
                repetirModoLembrete2.add(repetirLembreteModo);

            } while (cursor.moveToNext());
        }

        cursor.close();

        // Convertendo a lista de objetos Agenda em arrays separados para o CustomAdapter
        String[] titulos = new String[listaagenda.size()];
        String[] descricoes = new String[listaagenda.size()];
        String[] datas = new String[listaagenda.size()];
        String[] horas = new String[listaagenda.size()];
        boolean[] lembretes = new boolean[listaagenda.size()];
        long[] ids = new long[listaagenda.size()];
        boolean[] repetirLembrete = new boolean[listaagenda.size()];
        int[] repetirModoLembrete = new int[listaagenda.size()];
        boolean[] notificouTarefa = new boolean[listaagenda.size()];

        for (int i = 0; i < listaagenda.size(); i++) {
            ids[i] = listaIDs.get(i);
            titulos[i] = listaagenda.get(i).getNomeAgenda();
            descricoes[i] = listaagenda.get(i).getDescriçãoAgenda();
            datas[i] = listaagenda.get(i).getDataAgendaString();
            int hora = listaagenda.get(i).getHoraAgenda();
            int minuto = listaagenda.get(i).getMinutoAgenda();
            String horaFormatada = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto);
            horas[i] = horaFormatada;
            lembretes[i] = listaagenda.get(i).getLembrete();
            repetirLembrete[i] = listaagenda.get(i).getRepetirLembrete();
            repetirModoLembrete[i] = repetirModoLembrete2.get(i);
            notificouTarefa[i] = listaagenda.get(i).isNotificado();
        }

        // Configurando o CustomAdapter para a ListView
        CustomAdapter customAdapter = new CustomAdapter(context, listaIDs, titulos, descricoes, datas, horas,
                lembretes, repetirLembrete, repetirModoLembrete2, notificouTarefa);
        listView.setAdapter(customAdapter);


    }

    public void listarAgendaAtraso(ArrayList<Long> listaIDs, ArrayList<Integer> repetirModoLembrete2, Context context, ListView listView) {


        List<Agenda> listaagenda = new ArrayList<Agenda>();
        listaIDs.clear();
        repetirModoLembrete2.clear();

        Cursor cursor = db.rawQuery("SELECT * FROM agenda WHERE finalizado = 0 AND agendaAtraso = 2;", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                long ID = cursor.getLong(cursor.getColumnIndex("id"));
                @SuppressLint("Range")
                String titulo = cursor.getString(cursor.getColumnIndex("nomeTarefa"));
                @SuppressLint("Range")
                String descricao = cursor.getString(cursor.getColumnIndex("descricaoTarefa"));
                @SuppressLint("Range")
                String dataagenda = cursor.getString(cursor.getColumnIndex("dataAgenda"));
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


                @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate localdataagendaFim = LocalDate.parse(dataagendaFim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate localdataagendaInsert = LocalDate.parse(dataAgendaInsert, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                listaagenda.add(new Agenda(ID, titulo, descricao, localdataagenda, horaagenda, minutoagenda,
                        lembrete, finalizado, localdataagendaFim, horaAgendaFim, minutoAgendaFim, localdataagendaInsert,
                        horaAgendaInsert, minutoAgendaInsert, agendaAtrasoDB, repetirLembrete, repetirLembreteModo, notificouTarefa));
                listaIDs.add(ID);
                repetirModoLembrete2.add(repetirLembreteModo);

            } while (cursor.moveToNext());
        }

        cursor.close();

        // Convertendo a lista de objetos Agenda em arrays separados para o CustomAdapter
        String[] titulos = new String[listaagenda.size()];
        String[] descricoes = new String[listaagenda.size()];
        String[] datas = new String[listaagenda.size()];
        String[] horas = new String[listaagenda.size()];
        boolean[] lembretes = new boolean[listaagenda.size()];
        long[] ids = new long[listaagenda.size()];
        boolean[] repetirLembrete = new boolean[listaagenda.size()];
        int[] repetirModoLembrete = new int[listaagenda.size()];
        boolean[] notificouTarefa = new boolean[listaagenda.size()];

        for (int i = 0; i < listaagenda.size(); i++) {
            ids[i] = listaIDs.get(i);
            titulos[i] = listaagenda.get(i).getNomeAgenda();
            descricoes[i] = listaagenda.get(i).getDescriçãoAgenda();
            datas[i] = listaagenda.get(i).getDataAgendaString();
            int hora = listaagenda.get(i).getHoraAgenda();
            int minuto = listaagenda.get(i).getMinutoAgenda();
            String horaFormatada = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto);
            horas[i] = horaFormatada;
            lembretes[i] = listaagenda.get(i).getLembrete();
            repetirLembrete[i] = listaagenda.get(i).getRepetirLembrete();
            repetirModoLembrete[i] = repetirModoLembrete2.get(i);
            notificouTarefa[i] = listaagenda.get(i).isNotificado();
        }

        // Configurando o CustomAdapter para a ListView
        CustomAdapter customAdapter = new CustomAdapter(context, listaIDs, titulos, descricoes, datas, horas,
                lembretes, repetirLembrete, repetirModoLembrete2, notificouTarefa);
        listView.setAdapter(customAdapter);


    }



    }


