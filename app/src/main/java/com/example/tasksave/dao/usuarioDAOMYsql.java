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

    public ResultSet autenticaUsuarioAWS(User user) {

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

            try {
                String sql = "select * from usuario where email_usuario = ? and senha_usuario = ?";
                PreparedStatement pstm = conn.prepareStatement(sql);
                pstm.setString(1, user.getEmail_usuario());
                pstm.setString(2, user.getSenha_usuario());

                ResultSet rs = pstm.executeQuery();
                return rs;

            }catch (SQLException e) {
                e.printStackTrace();
                Log.d("Usuario autenticacao", "ERRO AO AUTENTICAR" + e);
                return null;
            }
        }


    }

