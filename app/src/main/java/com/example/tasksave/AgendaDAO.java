package com.example.tasksave;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AgendaDAO {

        private Conexao con;
        private SQLiteDatabase db;

        public AgendaDAO(Context context) {

            con = new Conexao(context);
            db = con.getWritableDatabase();

        }




}
