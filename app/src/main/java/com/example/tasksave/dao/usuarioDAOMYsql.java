package com.example.tasksave.dao;

import android.util.Log;

import com.example.tasksave.conexaoMYSQL.ConnectionClass;
import com.example.tasksave.objetos.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class usuarioDAOMYsql {

    Connection conn;
    String erro;
    public ResultSet autenticaUsuarioAWS(User user) {


        conn = new ConnectionClass().CONN();


        try {
            String sql="SELECT * FROM usuario WHERE email_usuario = ? AND senha_usuario = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getEmail_usuario());
            pstm.setString(2, user.getSenha_usuario());

            ResultSet rs = pstm.executeQuery();
            return rs;

        }catch (SQLException e) {
            Log.d("Usuario autenticacao", "ERRO AO AUTENTICAR" + e);
            return null;
        }
    }


}
