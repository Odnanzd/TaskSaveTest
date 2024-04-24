package com.example.tasksave.conexaoMYSQL;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class ConnectionClass {
    protected static String db = "tasksave";
    protected static String ip = "tasksaveaws.cnuguuiogphj.us-east-1.rds.amazonaws.com";
    protected static String port = "3306";
    protected static String username = "odnan";
    protected static String password = "tasksave2024";

    public Connection CONN() {
        Connection conn = null;
        try{
//            Class.forName("com.mysql.jdbc.Driver");
//            String connectionString = "jdbc:mysql://"+ip + ":" +port +"/" +db;
            String url = "jdbc:mysql://tasksaveaws.cnuguuiogphj.us-east-1.rds.amazonaws.com:3306/tasksave?user=odnan&password=tasksave2024";
            conn = DriverManager.getConnection(url);
        }catch (Exception e) {
            Log.e("ERROSSS", Objects.requireNonNull(e.getMessage()));
        }
        return conn;
    }


}
