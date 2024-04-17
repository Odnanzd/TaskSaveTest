package com.example.tasksave.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasksave.R;
import com.example.tasksave.dao.UserDAO;
import com.example.tasksave.objetos.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class activity_cadastro extends AppCompatActivity {

    EditText editTextUsuario;
    EditText editTextEmail;
    EditText editTextSenha;
    EditText editTextSenha2;
    Button buttonCadastro;

    TextView textViewLogin;

    public void onBackPressed() {

    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

    WindowAdjuster.assistActivity(this);

    editTextUsuario = findViewById(R.id.editTextUsuarioCadastro);
    editTextEmail = findViewById(R.id.editTextEmailCadastro);
    editTextSenha = findViewById(R.id.editTextSenhaCadastro);
    editTextSenha2 = findViewById(R.id.editTextSenhaCadastro2);

    buttonCadastro = findViewById(R.id.buttonCadastro);
    textViewLogin = findViewById(R.id.textViewLogin);

    buttonCadastro.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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

        if(usuarioCadastro.isEmpty() || emailCadastro.isEmpty() || senhaCadastro.isEmpty()|| senhaCadastro2.isEmpty()) {

            String msg_error2 = "Os campos não podem ser vazios.";
            Snackbar snackbar = Snackbar.make(view, msg_error2, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();

        } else if(editTextSenha.getText().toString().equals(editTextSenha2.getText().toString())) {

            CadastrarUserFirebase(usuarioCadastro, emailCadastro, senhaCadastro, senhaCadastro2, view);

        } else {
            String msg_error2 = "As senhas devem ser iguais";
            Snackbar snackbar = Snackbar.make(view,msg_error2,Snackbar.LENGTH_SHORT );
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }
    }

    private void CadastrarUserFirebase(String usuario, String email, String senha, String senha2, View view) {

        UserDAO userDAO = new UserDAO(this);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    SharedPreferences prefs = getSharedPreferences("ArquivoPrimeiroAcesso", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("PrimeiroAcesso", true);
                    editor.commit();
                    User user = new User(editTextUsuario.getText().toString(), editTextSenha.getText().toString(), editTextEmail.getText().toString());
                    long id = userDAO.inserir(user);
                    Intent intent = new Intent(activity_cadastro.this, activity_login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    Toast.makeText(activity_cadastro.this, "Faça o login para continuar ", Toast.LENGTH_LONG).show();
                } else {
                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro="Senha deve conter no minímo 6 caracteres";
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                    }catch (FirebaseAuthUserCollisionException e) {
                        erro="Esta conta já foi cadastrada";
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }

                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        erro="E-mail inválido";
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                    }catch (Exception e) {
                        erro="Erro inesperado";
                        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view2 = getCurrentFocus();
                        if (view2 != null) {
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                    }
                    Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }
            }
        });

    }
}