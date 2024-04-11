package com.example.tasksave.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasksave.R;
import com.example.tasksave.dao.UserDAO;
import com.example.tasksave.objetos.User;
import com.google.android.material.snackbar.Snackbar;

public class activity_cadastro extends AppCompatActivity {

    EditText editTextUsuario;
    EditText editTextEmail;
    EditText editTextSenha;
    EditText editTextSenha2;
    Button buttonCadastro;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

    editTextUsuario = findViewById(R.id.editTextUsuarioCadastro);
    editTextEmail = findViewById(R.id.editTextEmailCadastro);
    editTextSenha = findViewById(R.id.editTextSenhaCadastro);
    editTextSenha2 = findViewById(R.id.editTextSenhaCadastro2);

    buttonCadastro = findViewById(R.id.buttonCadastro);

    buttonCadastro.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inserirUser(v);
            }
    });

    }

    public void inserirUser(View view) {

        if(editTextUsuario.getText().toString().equals("")||editTextEmail.getText().toString().equals("")||
                editTextSenha.getText().toString().equals("") || editTextSenha2.getText().toString().equals("")) {
            String msg_error2 = "Os campos não podem ser vazios.";
            Snackbar snackbar = Snackbar.make(view,msg_error2,Snackbar.LENGTH_SHORT );
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();

        } else if(editTextSenha.getText().toString().equals(editTextSenha2.getText().toString())) {

            UserDAO userDAO = new UserDAO(this);

            SharedPreferences prefs = getSharedPreferences("ArquivoPrimeiroAcesso", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("PrimeiroAcesso", true);
            editor.commit();
            User user = new User(editTextUsuario.getText().toString(), editTextSenha.getText().toString(), editTextEmail.getText().toString());
            long id = userDAO.inserir(user);
            Intent intent = new Intent(activity_cadastro.this, activity_login.class);
            startActivity(intent);
            Toast.makeText(this, "Faça o login para continuar " + id, Toast.LENGTH_LONG).show();

        }
    }
}