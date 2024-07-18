package com.example.tasksave.test.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tasksave.R;
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.User;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityConfgSecSenha extends AppCompatActivity {

    private EditText editTextSenhaAntiga, editTextSenhaNova1, editTextSenhaNova2;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    private TextView textViewFrame;
    private ImageView imageViewBack;
    private boolean processoAtualiza = false;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if(!processoAtualiza) {
            Intent intent = new Intent(ActivityConfgSecSenha.this, ActivitySecPriv.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }else {
            Toast.makeText(ActivityConfgSecSenha.this, "Por favor, aguarde.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_sec_senha);


        editTextSenhaAntiga = findViewById(R.id.editTextSenhaAntiga);
        editTextSenhaNova1 = findViewById(R.id.editTextSenha2);
        editTextSenhaNova2 = findViewById(R.id.editTextSenha3);
        frameLayout = findViewById(R.id.framelayout1);
        progressBar = findViewById(R.id.progressbar1);
        textViewFrame = findViewById(R.id.textviewbutton);
        imageViewBack = findViewById(R.id.imageView4);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textSenhaAntiga = editTextSenhaAntiga.getText().toString();
                String textSenhaNova = editTextSenhaNova1.getText().toString();
                String textSenhaNova2 = editTextSenhaNova2.getText().toString();

                if (textSenhaAntiga.isEmpty() ||
                        textSenhaNova.isEmpty() ||
                        textSenhaNova2.isEmpty()) {

                    esconderTeclado();
                    String msg_error2 = "O campo está vazio.";
                    Snackbar snackbar = Snackbar.make(v, msg_error2, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                } else if (textSenhaNova.length() < 6) {

                    esconderTeclado();
                    String msg_error2 = "Senha deve conter no minímo 6 caracteres";
                    Snackbar snackbar = Snackbar.make(v, msg_error2, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();


                } else if (!textSenhaNova.equals(textSenhaNova2)) {

                    esconderTeclado();
                    String msg_error2 = "As senhas são diferentes.";
                    Snackbar snackbar = Snackbar.make(v, msg_error2, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                } else {

                    String senhaNova1 = editTextSenhaNova1.getText().toString();

                    textViewFrame.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    frameLayout.setClickable(false);

                    atualizarSenha(senhaNova1, v);
                }

            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!processoAtualiza) {
                    Intent intent = new Intent(ActivityConfgSecSenha.this, ActivitySecPriv.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }else {
                    Toast.makeText(ActivityConfgSecSenha.this, "Por favor, aguarde.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void esconderTeclado() {

        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view2 = getCurrentFocus();
        if (view2 != null) {
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }

    public void atualizarSenha(String senhaNova, View v) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {

                processoAtualiza = true;

                SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityConfgSecSenha.this);
                String valorEmail = sharedPreferencesUsuario.getEmailLogin();

                UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
                String senhaAntiga = usuarioDAOMYsql.senhaUsuarioAWS(valorEmail);

                if (!senhaAntiga.equals(editTextSenhaAntiga.getText().toString())) {
                    runOnUiThread(() -> {
                        textViewFrame.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        frameLayout.setClickable(true);
                        processoAtualiza = false;
                        esconderTeclado();
                        String msg_error2 = "Senha antiga incorreta.";
                        Snackbar snackbar = Snackbar.make(v, msg_error2, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    });
                } else {
                    try {
                        int id_usuario = usuarioDAOMYsql.idUsarioAWS(valorEmail);

                        User user = new User();
                        user.setId_usuario(id_usuario);
                        user.setSenha_usuario(senhaNova);

                        boolean sucesso = usuarioDAOMYsql.atualizarSenhaAWS(user);

                        if (sucesso) {
                            runOnUiThread(() -> {
                                sharedPreferencesUsuario.armazenaSenhaLogin(senhaNova);

                                Toast.makeText(getApplicationContext(), "Senha alterada", Toast.LENGTH_SHORT).show();
                                textViewFrame.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                frameLayout.setClickable(true);
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Erro ao alterar a senha", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (Exception e) {
                        Log.d("ERRO SQL AUT", "ERRO SQL" + e);
                    }
                }
            } catch (Exception e) {
                Log.d("ERRO SQL AUT", "ERRO SQL" + e);
            }
        });
    }
}