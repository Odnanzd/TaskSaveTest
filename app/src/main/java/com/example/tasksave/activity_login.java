package com.example.tasksave;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_login extends AppCompatActivity {
    public EditText input_Nome;
    private TextView text_view_user;
    private Button button_login;

    SQLiteDatabase database;

    @Override
    public void onBackPressed() {
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_Nome = findViewById(R.id.inputNome);
        text_view_user = findViewById(R.id.text_view_user);
        button_login = findViewById(R.id.button_login);
        input_Nome.requestFocus();

//Método de entrada, no ActivityLogin, a mensagem de boas vindas irá alterar para o nome que o usuário colocar.
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_Nome.getText().toString().equals("")) {

                    Toast.makeText(activity_login.this, "Favor, inserir um usuário", Toast.LENGTH_SHORT).show();

                } else {
                    SharedPreferences prefs = getSharedPreferences("arquivoSalvar", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("primeiroAcesso", true);
                    editor.commit();
                    InserirUser();
                    Intent intentMain = new Intent(activity_login.this, activity_main.class);
                    startActivity(intentMain);
                }

            }

        });
        }
        public void InserirUser() {

            try {


                User user = new User(input_Nome.getText().toString());

                UserDAO userDAO = new UserDAO(this);
                long id = userDAO.inserir(user);

                Toast.makeText(this, "Usuário salvo. ID: " + id, Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        }



