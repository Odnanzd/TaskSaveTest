package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

        return db.insert("user", null, contentValues);

    }

    public List<User> ListarNome() {

        List<User> listausername = new ArrayList<User>();

        Cursor cursor = db.rawQuery("SELECT * FROM user", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                String username = cursor.getString(cursor.getColumnIndex("username"));
                listausername.add(new User(username));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listausername;
    }
}
