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
}
