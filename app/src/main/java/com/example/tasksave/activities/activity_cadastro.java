package com.example.tasksave.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class activity_cadastro extends AppCompatActivity {

    EditText editTextUsuario;
    EditText editTextEmail;
    EditText editTextSenha;
    EditText editTextSenha2;
    Button buttonCadastro;

    TextView textViewLogin;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    TextView textViewButton;
    String USUARIOID;
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

        boolean usercadastrado = usuarioDAOMYsql.emailJaCadastrado(emailCadastro);

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

        } else if (usercadastrado) {

            String msg_error2 = "Esse e-mail já está cadastrado.";

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

        }
    }

    public void CadastrarUserMYSQL(String usuarioCadastro, String emailCadastro, String senhaCadastro) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {

                User user = new User();
                user.setNome_usuario(usuarioCadastro);
                user.setEmail_usuario(emailCadastro);
                user.setSenha_usuario(senhaCadastro);


                usuarioDAOMYsql usuarioDAOMYsql = new usuarioDAOMYsql();

                boolean sucesso = usuarioDAOMYsql.CadastraUsuarioAWS(user);


                if (sucesso) {
                    // Sucesso na autenticação
                    str = "Faça o login para continuar.";
                    runOnUiThread(() -> {
                        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity_cadastro.this, activity_login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    });
                } else {
                    // Falha na autenticação
                    str2 = "Erro ao autenticar";
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        textViewButton.setVisibility(View.VISIBLE);
                        frameLayout.setClickable(true);
                        Snackbar snackbar = Snackbar.make(this.getCurrentFocus(), str2, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    });
                }
            } catch (Exception e) {
                Log.d("ERRO SQL AUT", "ERRO SQL" + e);
            }
        });
    }


    private void CadastrarUserFirebase(String usuario, String email, String senha, String senha2, View view) {

        UserDAO userDAO = new UserDAO(this);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    SalvarDadosUser();
                    SharedPreferences prefs = getSharedPreferences("ArquivoPrimeiroAcesso", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("PrimeiroAcesso", true);
                    editor.commit();
//                    User user = new User(editTextUsuario.getText().toString(), editTextSenha.getText().toString(), editTextEmail.getText().toString());
//                    long id = userDAO.inserir(user);
                    Intent intent = new Intent(activity_cadastro.this, activity_login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    Toast.makeText(activity_cadastro.this, "Faça o login para continuar ", Toast.LENGTH_LONG).show();
                } else {
                    String erro;
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Senha deve conter no minímo 6 caracteres";
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                        progressBar.setVisibility(View.GONE);
                        textViewButton.setVisibility(View.VISIBLE);
                        frameLayout.setClickable(true);
                        editTextSenha.setTextColor(Color.RED);
                        editTextSenha2.setTextColor(Color.RED);
                        editTextEmail.setTextColor(getResources().getColor(R.color.grey6));

                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta conta já foi cadastrada";
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                        progressBar.setVisibility(View.GONE);
                        textViewButton.setVisibility(View.VISIBLE);
                        frameLayout.setClickable(true);
                        editTextEmail.setTextColor(Color.RED);

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail inválido";
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                        progressBar.setVisibility(View.GONE);
                        textViewButton.setVisibility(View.VISIBLE);
                        frameLayout.setClickable(true);
                        editTextEmail.setTextColor(Color.RED);

                    } catch (Exception e) {
                        erro = "Erro inesperado";
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                        progressBar.setVisibility(View.GONE);
                        textViewButton.setVisibility(View.VISIBLE);
                        frameLayout.setClickable(true);
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }
            }
        });

    }

    private void SalvarDadosUser() {

        String usuarioText = editTextUsuario.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("usuario", usuarioText);

        USUARIOID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuários").
                document(USUARIOID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar user.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DB_ERROR", "Erro ao salvar User" + e.toString());
            }
        });
    }
}