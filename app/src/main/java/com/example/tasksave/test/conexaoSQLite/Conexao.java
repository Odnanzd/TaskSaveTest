package com.example.tasksave.test.conexaoSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {

    private static final String nome = "banco.db";
    private static final int versao = 1;

    public Conexao(Context context) {
        super(context, nome, null, versao);
    }
        @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE agenda (id integer, nomeTarefa varchar(50), descricaoTarefa varchar(50)," +
                " dataAgenda TEXT, horaAgenda integer, minutoAgenda integer, lembretedefinido integer, finalizado integer, dataAgendaFim TEXT," +
                "horaAgendaFim integer, minutoAgendaFim integer, dataAgendaInsert TEXT, horaAgendaInsert integer, minutoAgendaInsert integer, " +
                "agendaAtraso integer, repetirLembrete integer, repetirModo integer, notificouTarefa integer)");
        }

        @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

}


