package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class activity_login extends AppCompatActivity {
    public EditText input_Nome;
    public EditText input_Password;
    private Button button_login;


    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_Nome = findViewById(R.id.inputNome);
        button_login = findViewById(R.id.button_login);
        input_Password = findViewById(R.id.input_Password);
        input_Nome.requestFocus();

        InputFilter noSpaceFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        input_Nome.setFilters(new InputFilter[]{noSpaceFilter});
        input_Password.setFilters(new InputFilter[]{noSpaceFilter});

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_Nome.getText().toString().equals("") || input_Password.getText().toString().equals("")) {

                    String msg_error2 = "Os campos não podem ser vazios.";
                    Snackbar snackbar = Snackbar.make(view,msg_error2,Snackbar.LENGTH_SHORT );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

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



