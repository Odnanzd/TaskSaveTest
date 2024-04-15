package com.example.tasksave.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.tasksave.R;
import com.example.tasksave.dao.UserDAO;
import com.google.android.material.snackbar.Snackbar;

public class activity_login extends AppCompatActivity {
    public EditText input_Nome;
    public EditText input_Password;
    private Button button_login;
    CheckBox checkBox;
    private Button button_cadastro;

    @Override
    public void onBackPressed() {

    }

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_Nome = findViewById(R.id.editTextEmail);
        button_login = findViewById(R.id.buttonLogin);
        input_Password = findViewById(R.id.editTextSenha);
        button_cadastro = findViewById(R.id.buttonLogin2);
//        input_Nome.requestFocus();
        checkBox = findViewById(R.id.checkBox2);
        WindowAdjuster.assistActivity(this);

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
                    InserirUser(view);
                }

            }

        });
        button_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this, activity_cadastro.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        }
        public void InserirUser(View view) {

            try {
                UserDAO userDAO = new UserDAO(this);
                String username = input_Nome.getText().toString();
                String password = input_Password.getText().toString();
                boolean checarUser = userDAO.checkUser(username);

                if(!checarUser) {

                    String msg_error2 = "Usuário não existe.";
                    Snackbar snackbar = Snackbar.make(view,msg_error2,Snackbar.LENGTH_SHORT );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }
                boolean validarAcesso = userDAO.authenticateUser(username, password);

                Log.d("Verificação validar Acesso", "validarAcesso" +   validarAcesso);

                if(validarAcesso) {

                    if(checkBox.isChecked()) {

                        SharedPreferences prefs = getSharedPreferences("arquivoSalvarSenha", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("SalvarSenha", true);
                        editor.commit();
                    }
                    SharedPreferences prefs = getSharedPreferences("ArquivoPrimeiroAcesso", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("PrimeiroAcesso", true);
                    editor.commit();
                   Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT);
                    Intent intentMain = new Intent(activity_login.this, activity_main.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentMain);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                } else {
                    String msg_error2 = "Senha incorreta.";
                    Snackbar snackbar = Snackbar.make(view,msg_error2,Snackbar.LENGTH_SHORT );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        }



