package com.example.tasksave.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tasksave.conexaoMYSQL.ConnectionClass;
import com.example.tasksave.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class activity_welcome extends AppCompatActivity {

    Button buttonEntrar;
    Button buttonCadastrar;
    Button buttonDB;

    ConnectionClass connectionClass;

    Connection con;

    ResultSet rs;

    String name, str, str2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonEntrar = findViewById(R.id.buttonEntrar);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
        buttonDB = findViewById(R.id.button4);

        Connect();
        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_welcome.this, activity_login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_welcome.this, activity_cadastro.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

    }

    public void Connect() {

        ConnectionClass connectionClass = new ConnectionClass();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = connectionClass.CONN();
                if (con == null) {
                    str = "Error in connection with MySQL server";
                } else {
                    str = "Connected with MySQL server";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                // SQL para criar uma tabela de teste
                String sql = "CREATE TABLE IF NOT EXISTS teste (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "nome VARCHAR(255), " +
                        "idade INT)";

                // Executa a query para criar a tabela
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);

                // Tabela criada com sucesso
                str2 = "Tabela criada com sucesso";
            } catch (SQLException e) {
                Log.e("CreateTableThread", "Erro ao criar tabela: " + e.getMessage());
                str2 = "Erro ao criar tabela: " + e.getMessage();
            }

            runOnUiThread(() -> {
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            });
            runOnUiThread(() -> {
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, str2, Toast.LENGTH_SHORT).show();
            });
        });
    }
}

