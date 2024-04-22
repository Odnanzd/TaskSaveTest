package com.example.tasksave;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class ConnectionClass {
    protected static String db = "tasksave";
    protected static String ip = "10.0.2.2";
    protected static String port = "3306";
    protected static String username = "root";
    protected static String password = "tasksave@2024";

    public Connection CONN() {
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String connectionString = "jdbc:mysql://"+ip + ":" +port +"/" +db;
            conn = DriverManager.getConnection(connectionString, username, password);
        }catch (Exception e) {
            Log.e("ERRO", Objects.requireNonNull(e.getMessage()));
        }
        return conn;
    }


}
