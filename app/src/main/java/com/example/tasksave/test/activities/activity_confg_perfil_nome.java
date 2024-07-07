package com.example.tasksave.test.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.tasksave.R;
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class activity_confg_perfil_nome extends AppCompatActivity {


    FrameLayout frameLayout;
    EditText editTextNomeCompleto;
    ProgressBar progressBar;
    TextView textView, textView2;
    ImageView imageViewback;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(activity_confg_perfil_nome.this, activity_confg_perfil.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_perfil_nome);

        editTextNomeCompleto = findViewById(R.id.editTextNomeCompleto);
        frameLayout = findViewById(R.id.framelayout1);
        progressBar = findViewById(R.id.progressbar1);
        textView = findViewById(R.id.textviewbutton);

        textView2 = findViewById(R.id.textViewPerfil2);
        imageViewback = findViewById(R.id.imageView4);

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvarUser", Context.MODE_PRIVATE);
        String sharedPrd = sharedPrefs.getString("userString", "");

        editTextNomeCompleto.setHint(sharedPrd);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextNomeCompleto.getText().toString().isEmpty()) {

                    esconderTeclado();
                    String msg_error2 = "O campo está vazio.";
                    Snackbar snackbar = Snackbar.make(v, msg_error2, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }else {

                    String nomeCompletoeditText = editTextNomeCompleto.getText().toString();
                    textView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    frameLayout.setClickable(false);

                    atualizarNome(nomeCompletoeditText);
                }

            }
        });
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_confg_perfil_nome.this, activity_confg_perfil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

    }

    public void atualizarNome(String nomeCompleto) {


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            try {

                SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvarLoginEmail", Context.MODE_PRIVATE);
                String valorEmail = sharedPrefs2.getString("arquivo_Email", "");

                UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();

                int id_usuario = usuarioDAOMYsql.idUsarioAWS(valorEmail);

                User user = new User();
                user.setId_usuario(id_usuario);
                user.setNome_usuario(nomeCompleto);

                boolean sucesso = usuarioDAOMYsql.atualizarNomeCompletoAWS(user);

                if (sucesso) {
                    runOnUiThread(() -> {

                        SharedPreferences prefs5 = getSharedPreferences("arquivoSalvarUser", MODE_PRIVATE);
                        SharedPreferences.Editor editor5 = prefs5.edit();
                        editor5.putString("userString",nomeCompleto) ;
                        editor5.apply();

                        Toast.makeText(getApplicationContext(), "Nome alterado", Toast.LENGTH_SHORT).show();
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        frameLayout.setClickable(true);
                        finish();

                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Erro ao alterar o nome", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                Log.d("ERRO SQL AUT", "ERRO SQL" + e);
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
}