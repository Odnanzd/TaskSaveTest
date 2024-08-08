package com.example.tasksave.test.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tasksave.test.conexaoSQLite.Conexao;

import java.util.ArrayList;

public class AlarmeDAO {

    private Conexao con;
    private SQLiteDatabase db;

    public AlarmeDAO(Context context) {

        con = new Conexao(context);
        db = con.getWritableDatabase();

    }

    public long inserirAlarmeID(int idTarefa) {

        ContentValues values = new ContentValues();
        values.put("idTarefa", idTarefa );
        return db.insert("alarme", null, values);
    }

    public ArrayList<Integer> alarmesID() {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT id FROM alarme";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                ids.add(id);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return ids;

    }

    public ArrayList<Long> idsAlarmes (ArrayList<Long> ids) {

        ArrayList<Long> idsAlarmes = new ArrayList<>();

        String[] colunas = {"id"};
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(ids)};

        Cursor cursor = db.query("agenda", colunas, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                ids.add(id);
            } while (cursor.moveToNext());

        }
        cursor.close();

        return idsAlarmes;

    }
    public long atualizaIdTarefa(int idAntigo, int idnovo) {

//        String[] columns = {"idTarefa"};
        String selection = "idTarefa = ?";
        String[] selectionArgs = {String.valueOf(idAntigo)};

        ContentValues contentValues = new ContentValues();
        contentValues.put("idTarefa", idnovo);
        Log.d("AtualizaIdTarefa", "teste idNOVO: "+idnovo);

        return db.update("alarme", contentValues, selection, selectionArgs);


    }
    public int getLastIdTarefa() {
        int idTarefa = -1;

        // Primeiro, obtenha o último ROWID inserido
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        if (cursor.moveToFirst()) {
            int lastInsertId = cursor.getInt(0);
            Log.d("AtualizaIdTarefa", "teste idNOVO: "+lastInsertId);

            // Agora, use esse ID para recuperar o valor de idTarefa
            Cursor tarefaCursor = db.rawQuery("SELECT idTarefa FROM alarme WHERE id = ?", new String[]{String.valueOf(lastInsertId)});
            if (tarefaCursor.moveToFirst()) {
                idTarefa = tarefaCursor.getInt(0);
            }
            tarefaCursor.close();
        }
        cursor.close();

        return idTarefa;
    }
}
