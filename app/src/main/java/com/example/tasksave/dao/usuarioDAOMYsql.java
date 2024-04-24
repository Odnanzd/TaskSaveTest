package com.example.tasksave.dao;

import android.util.Log;

import com.example.tasksave.conexaoMYSQL.ConnectionClass;
import com.example.tasksave.objetos.User;

import java.sql.Connection;
import java.sql.DriverManager;
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
    public boolean CadastraUsuarioAWS(User user) {

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {
            String sql = "INSERT INTO usuario (nome_usuario, email_usuario, senha_usuario) VALUES (?, ?, ?)";

            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getNome_usuario());
            pstm.setString(2, user.getEmail_usuario());
            pstm.setString(3, user.getSenha_usuario());

            pstm.executeUpdate();
            return true;

        }catch (SQLException e) {
            e.printStackTrace();
            Log.d("Usuario cadastro", "ERRO AO CADASTRAR" + e);
            return false;
        }
    }
    public boolean emailJaCadastrado(String email) {

        boolean jaCadastrado = false;

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {
            String sql = "SELECT COUNT(*) AS total FROM usuario WHERE email_usuario = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int total = resultSet.getInt("total");
                jaCadastrado = (total > 0);
            }
        } catch (SQLException e) {
            // Tratar exceção, se necessário
            e.printStackTrace();
        }

        return jaCadastrado;
    }


}
