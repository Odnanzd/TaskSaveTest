package com.example.tasksave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

        return db.insert("agenda", null, contentValues);

    }
    public String VerificarLista(Agenda agenda) {

        Cursor cursor = db.rawQuery("SELECT * FROM agenda;", null);

        String resultado;

        if (cursor.getCount() == 0) {
            resultado = "Nenhum dado encontrado";
        } else {
            resultado = "";
        }
        cursor.close();

        return resultado;

    }



}
