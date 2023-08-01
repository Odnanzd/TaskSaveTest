package com.example.tasksave;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

        return db.insert("agenda", null, contentValues);

    }


}
