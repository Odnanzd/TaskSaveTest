package com.example.tasksave.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasksave.R;
import com.example.tasksave.dao.UserDAO;
import com.example.tasksave.dao.usuarioDAOMYsql;
import com.example.tasksave.objetos.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class activity_cadastro extends AppCompatActivity {

    EditText editTextUsuario;
    EditText editTextEmail;
    EditText editTextSenha;
    EditText editTextSenha2;

    TextView textViewLogin;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    TextView textViewButton;
    String str, str2;

    public void onBackPressed() {

    }

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        WindowAdjuster.assistActivity(this);

        editTextUsuario = findViewById(R.id.editTextUsuarioCadastro);
        editTextEmail = findViewById(R.id.editTextEmailCadastro);
        editTextSenha = findViewById(R.id.editTextSenhaCadastro);
        editTextSenha2 = findViewById(R.id.editTextSenhaCadastro2);

        textViewLogin = findViewById(R.id.textViewLogin);
        frameLayout = findViewById(R.id.framelayout1);
        progressBar = findViewById(R.id.progressbar1);
        textViewButton = findViewById(R.id.textviewbutton);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                textViewButton.setVisibility(View.GONE);
                frameLayout.setClickable(false);
                inserirUser(v);
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_cadastro.this, activity_login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    public void inserirUser(View view) {

        String usuarioCadastro = editTextUsuario.getText().toString();
        String emailCadastro = editTextEmail.getText().toString();
        String senhaCadastro = editTextSenha.getText().toString();
        String senhaCadastro2 = editTextSenha2.getText().toString();

        usuarioDAOMYsql usuarioDAOMYsql = new usuarioDAOMYsql();


        if (usuarioCadastro.isEmpty() || emailCadastro.isEmpty() || senhaCadastro.isEmpty() || senhaCadastro2.isEmpty()) {

            String msg_error2 = "Os campos não podem ser vazios.";

            InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view2 = getCurrentFocus();
            if (view2 != null) {
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }

            Snackbar snackbar = Snackbar.make(view, msg_error2, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            progressBar.setVisibility(View.GONE);
            textViewButton.setVisibility(View.VISIBLE);
            frameLayout.setClickable(true);


        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailCadastro).matches()) {

            String msg_error2 = "Endereço de e-mail inválido.";

            InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view2 = getCurrentFocus();
            if (view2 != null) {
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }

            Snackbar snackbar = Snackbar.make(view, msg_error2, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            progressBar.setVisibility(View.GONE);
            textViewButton.setVisibility(View.VISIBLE);
            frameLayout.setClickable(true);
            editTextEmail.setTextColor(Color.RED);
            editTextSenha.setTextColor(getResources().getColor(R.color.grey6));
            editTextSenha2.setTextColor(getResources().getColor(R.color.grey6));

        } else if (senhaCadastro.length() < 6) {

            String msg_error2 = "Senha deve conter no minímo 6 caracteres";

            InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view2 = getCurrentFocus();
            if (view2 != null) {
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }

            Snackbar snackbar = Snackbar.make(view, msg_error2, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            progressBar.setVisibility(View.GONE);
            textViewButton.setVisibility(View.VISIBLE);
            frameLayout.setClickable(true);
            editTextSenha.setTextColor(Color.RED);
            editTextSenha2.setTextColor(Color.RED);
            editTextEmail.setTextColor(getResources().getColor(R.color.grey6));

        } else if (editTextSenha.getText().toString().equals(editTextSenha2.getText().toString())) {

            CadastrarUserMYSQL(usuarioCadastro, emailCadastro, senhaCadastro);


        } else {
            String msg_error2 = "As senhas devem ser iguais";

            InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view2 = getCurrentFocus();
            if (view2 != null) {
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }

            Snackbar snackbar = Snackbar.make(view, msg_error2, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            progressBar.setVisibility(View.GONE);
            textViewButton.setVisibility(View.VISIBLE);
            frameLayout.setClickable(true);
            editTextSenha.setTextColor(Color.RED);
            editTextSenha2.setTextColor(Color.RED);
            editTextEmail.setTextColor(getResources().getColor(R.color.grey6));

        }
    }

    public void CadastrarUserMYSQL(String usuarioCadastro, String emailCadastro, String senhaCadastro) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            usuarioDAOMYsql usuarioDAOMysql = new usuarioDAOMYsql();
            try (ResultSet resultSet = usuarioDAOMysql.emailJaCadastrado(emailCadastro)) {

                if (resultSet.next()) {
                    // Sucesso na autenticação
                    str2 = "Esse e-mail já está cadastrado.";
                    runOnUiThread(() -> {
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                        progressBar.setVisibility(View.GONE);
                        textViewButton.setVisibility(View.VISIBLE);
                        frameLayout.setClickable(true);
                        editTextEmail.setTextColor(Color.RED);
                        editTextSenha.setTextColor(getResources().getColor(R.color.grey6));
                        editTextSenha2.setTextColor(getResources().getColor(R.color.grey6));

                        Snackbar snackbar = Snackbar.make(this.getCurrentFocus(), str2, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    });
                } else {
                    // Falha na autenticação
                    str2 = "Erro no cadastro.";
                    str = "Faça o login para continuar.";
                    User user = new User();
                    user.setNome_usuario(usuarioCadastro);
                    user.setEmail_usuario(emailCadastro);
                    user.setSenha_usuario(senhaCadastro);
                    boolean sucesso = usuarioDAOMysql.CadastraUsuarioAWS(user);
                    runOnUiThread(() -> {
                        try {

                            if (sucesso) {
                                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity_cadastro.this, activity_login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                View view2 = getCurrentFocus();
                                if (view2 != null) {
                                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                }
                                progressBar.setVisibility(View.GONE);
                                textViewButton.setVisibility(View.VISIBLE);
                                frameLayout.setClickable(true);
                                Snackbar snackbar = Snackbar.make(this.getCurrentFocus(), str2, Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        } catch (Exception e) {
                            Log.d("ERRO SQL CADASTRO", "Erro ao cadastrar usuário: " + e);
                        }
                    });
                }
            } catch (Exception e) {
                Log.d("ERRO SQL AUT", "ERRO SQL" + e);
            }
        });
    }
}