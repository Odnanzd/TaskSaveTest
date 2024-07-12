package com.example.tasksave.test.dao;

import android.util.Log;

import com.example.tasksave.test.conexaoMYSQL.ConnectionClass;
import com.example.tasksave.test.objetos.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAOMYsql {

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
    public int cargoUsuarioAWS(String email) {

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {
            String sql = "SELECT id FROM cargo_usuario cu JOIN usuario u ON u.cargo_id = cu.id WHERE u.email_usuario = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Se houver pelo menos uma linha no resultado
                return resultSet.getInt("id"); // Recupere o nome do usuário da coluna "nome_usuario"
            } else {
                // Se não houver linha correspondente aos parâmetros fornecidos
                return 0; // Ou outra indicação de que nenhum usuário foi encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }




    public double getVersionAPP() {
        double versaoAppDB = 0.0;
        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();
        try {

            String query = "SELECT versao FROM versaoapp WHERE id = 1";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                versaoAppDB = resultSet.getDouble("versao");
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return versaoAppDB;
    }
    public String getTextoVersaoAPP() {

        String textoVersaoAPP = "";

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {

            String query = "SELECT texto_versao FROM versaoapp WHERE id = 1";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                textoVersaoAPP = resultSet.getString("texto_versao");
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return textoVersaoAPP;
    }
    public String getTexto1APP() {

        String texto1 = "";

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {

            String query = "SELECT texto_1 FROM versaoapp WHERE id = 1";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                texto1 = resultSet.getString("texto_1");
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return texto1;
    }
    public String getTexto2APP() {

        String texto2 = "";

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {

            String query = "SELECT texto_2 FROM versaoapp WHERE id = 1";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                texto2 = resultSet.getString("texto_2");
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return texto2;
    }
    public String getTexto3APP() {

        String texto3 = "";

        ConnectionClass connectionClass = new ConnectionClass();
        conn = connectionClass.CONN();

        try {

            String query = "SELECT texto_3 FROM versaoapp WHERE id = 1";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                texto3 = resultSet.getString("texto_3");
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return texto3;
    }

}



