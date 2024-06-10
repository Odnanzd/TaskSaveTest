package com.example.tasksave.test.dao;

import android.util.Log;

import com.example.tasksave.test.conexaoMYSQL.ConnectionClass;
import com.example.tasksave.test.objetos.User;

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

        } catch (SQLException e) {
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

        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("Usuario cadastro", "ERRO AO CADASTRAR" + e);
            return false;
        }
    }

    public ResultSet emailJaCadastradoAWS(String email) {

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {
            String sql = "SELECT email_usuario FROM usuario WHERE email_usuario = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String usuarioCadastrado(String email, String senha) {
        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {
            String sql = "SELECT nome_usuario FROM usuario WHERE email_usuario = ? AND senha_usuario = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, senha);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Se houver pelo menos uma linha no resultado
                return resultSet.getString("nome_usuario"); // Recupere o nome do usuário da coluna "nome_usuario"
            } else {
                // Se não houver linha correspondente aos parâmetros fornecidos
                return null; // Ou outra indicação de que nenhum usuário foi encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int idUsarioAWS(String email) {

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {
            String sql = "SELECT id_usuario FROM usuario WHERE email_usuario = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Se houver pelo menos uma linha no resultado
                return resultSet.getInt("id_usuario"); // Recupere o nome do usuário da coluna "nome_usuario"
            } else {
                // Se não houver linha correspondente aos parâmetros fornecidos
                return 0; // Ou outra indicação de que nenhum usuário foi encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public boolean atualizarNomeCompletoAWS(User user) {

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {
            String sql = "UPDATE usuario SET nome_usuario = ? WHERE id_usuario = ?";

            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getNome_usuario());
            pstm.setInt(2, user.getId_usuario());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("Usuario cadastro", "ERRO AO CADASTRAR" + e);
            return false;
        }
    }
}



