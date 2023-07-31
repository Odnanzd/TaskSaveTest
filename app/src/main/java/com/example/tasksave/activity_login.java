package com.example.tasksave;

import android.annotation.SuppressLint;
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
    public EditText input_Password;
    private TextView text_view_user;
    private Button button_login;

    SQLiteDatabase database;

    @Override
    public void onBackPressed() {
    }
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_Nome = findViewById(R.id.inputNome);
        text_view_user = findViewById(R.id.text_view_user);
        button_login = findViewById(R.id.button_login);
        input_Password = findViewById(R.id.input_Password);
        input_Nome.requestFocus();

//Método de entrada, no ActivityLogin, a mensagem de boas vindas irá alterar para o nome que o usuário colocar.
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_Nome.getText().toString().equals("") || input_Password.getText().toString().equals("")) {

                    Toast.makeText(activity_login.this, "Os campos não podem ser vazios.", Toast.LENGTH_LONG).show();

                } else {
                    SharedPreferences prefs = getSharedPreferences("arquivoSalvar", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("primeiroAcesso", true);
                    editor.commit();
                    InserirUser();
                    Intent intentMain = new Intent(activity_login.this, activity_main.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentMain);
                }

            }

        });
        }
        public void InserirUser() {

            try {

                User user = new User(input_Nome.getText().toString(), input_Password.getText().toString());

                UserDAO userDAO = new UserDAO(this);
                long id = userDAO.inserir(user);

                Toast.makeText(this, "Usuário salvo. ID: " + id, Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        }



