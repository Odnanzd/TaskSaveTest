package com.example.tasksave;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {

    private Conexao con;
    private SQLiteDatabase db;

    public UserDAO(Context context) {
        con = new Conexao(context);
        db = con.getWritableDatabase();

    }

    public long inserir(User user) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("username", user.getUsername());

        return db.insert()

    }

}
