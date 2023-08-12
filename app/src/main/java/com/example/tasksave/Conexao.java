package com.example.tasksave;

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

        sqLiteDatabase.execSQL("CREATE TABLE user (id integer primary key autoincrement, username varchar(50), password varchar(50))");
        sqLiteDatabase.execSQL("CREATE TABLE agenda (id integer primary key autoincrement, nomeTarefa varchar(50), descricaoTarefa varchar(50)," +
                " dataAgenda TEXT, horaAgenda integer, minutoAgenda integer, lembretedefinido integer )");
        }

        @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

}


