package com.example.tasksave.test.objetos;

public class User {
    private int id_usuario;

    private String nome_usuario;
    private String email_usuario;

    private String senha_usuario;
    private int cargo_usuario;

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public String getSenha_usuario() {
        return senha_usuario;
    }

    public void setSenha_usuario(String senha_usuario) {
        this.senha_usuario = senha_usuario;
    }

    public int getCargo_usuario() {
        return cargo_usuario;
    }

    public void setCargo_usuario(int cargo_usuario) {
        this.cargo_usuario = cargo_usuario;
    }
}
