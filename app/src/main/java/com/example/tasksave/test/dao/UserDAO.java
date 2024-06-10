package com.example.tasksave.test.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tasksave.test.conexaoSQLite.Conexao;

public class UserDAO {

    private Conexao con;
    private SQLiteDatabase db;

    public UserDAO(Context context) {
        con = new Conexao(context);
        db = con.getWritableDatabase();

    }

//    public long inserir(User user) {
//
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put("username", user.getUsername());
//        contentValues.put("password", user.getPassword());
//        contentValues.put("email", user.getEmail());
//
//        return db.insert("user", null, contentValues);
//
//    }
//
//    public List<User> ListarNome() {
//
//        List<User> listausername = new ArrayList<User>();
//
//        Cursor cursor = db.rawQuery("SELECT * FROM user;", null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range")
//                String username = cursor.getString(cursor.getColumnIndex("username"));
//                @SuppressLint("Range")
//                String password = cursor.getString(cursor.getColumnIndex("password"));
//                @SuppressLint("Range")
//                String email = cursor.getString(cursor.getColumnIndex("password"));
//
//                listausername.add(new User(username, password, email));
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//
//        return listausername;
//    }

    public boolean checkUser(String username) {

        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean authenticateUser(String username, String password) {

        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username = ? AND password = ?", new String[]{username, password});
        boolean authenticated = cursor.getCount() > 0;
        cursor.close();
        return authenticated;

    }


}
