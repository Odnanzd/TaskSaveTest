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
                    Toast.makeText(activity_login.this, "Gravado com Sucesso", Toast.LENGTH_SHORT).show();
                    Intent intentMain = new Intent(activity_login.this, activity_main.class);
                    intentMain.putExtra("arqName", input_Nome.getText().toString());
                    startActivity(intentMain);
                    salvarUser();
                }

            }

        });
        }
        public void salvarUser() {
            try {
                database = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS user (name TEXT);");

                ContentValues values = new ContentValues();
                values.put("username", String.valueOf("arqName"));

                // Insira os dados na tabela "agenda"
                database.insert("user", null, values);
                database.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }


