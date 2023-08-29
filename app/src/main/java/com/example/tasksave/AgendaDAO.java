package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

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

        List<Agenda> tarefasComLembrete = new ArrayList<>();

        String[] colunas = {
                "id", "nomeTarefa", "descricaoTarefa", "dataAgenda", "horaAgenda", "minutoAgenda", "lembretedefinido"
        };

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

                LocalDate localDataAgenda = LocalDate.parse(dataAgenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                tarefasComLembrete.add(new Agenda(id, titulo, descricao, localDataAgenda, horaAgenda, minutoAgenda, true));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return tarefasComLembrete;
    }
}

