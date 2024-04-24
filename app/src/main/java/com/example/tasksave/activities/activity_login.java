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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tasksave.R;
import com.example.tasksave.dao.usuarioDAOMYsql;
import com.example.tasksave.objetos.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.ResultSet;
import java.sql.SQLException;

public class activity_login extends AppCompatActivity {
    public EditText input_Nome;
    public EditText input_Password;
    private Button button_login;
    CheckBox checkBox;
    private Button button_cadastro;
    private FrameLayout frameLayout;
    private TextView textView;
    private ProgressBar progressBar;

    @Override
    public void onBackPressed() {

    }

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_Nome = findViewById(R.id.editTextEmail);
        input_Password = findViewById(R.id.editTextSenha);
        button_cadastro = findViewById(R.id.buttonLogin2);
//        input_Nome.requestFocus();
        checkBox = findViewById(R.id.checkBox2);
        frameLayout = findViewById(R.id.framelayout1);
        textView = findViewById(R.id.textviewbutton);
        progressBar = findViewById(R.id.progressbar1);

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

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_Nome.getText().toString().equals("") || input_Password.getText().toString().equals("")) {

                    String msg_error2 = "Os campos não podem ser vazios.";
                    Snackbar snackbar = Snackbar.make(view, msg_error2, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                } else {
                    textView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    frameLayout.setClickable(false);
                    button_cadastro.setClickable(false);
//                    InserirUser(view);
                    AutenticarUser();
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

        String emailUser = input_Nome.getText().toString();
        String senhaUser = input_Password.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailUser, senhaUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    if (checkBox.isChecked()) {

                        SharedPreferences prefs = getSharedPreferences("arquivoSalvarSenha", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("SalvarSenha", true);
                        editor.commit();
                    }
                    SharedPreferences prefs = getSharedPreferences("ArquivoPrimeiroAcesso", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("PrimeiroAcesso", true);
                    editor.commit();

                    Toast.makeText(activity_login.this, "Sucesso!", Toast.LENGTH_SHORT);

                    Intent intentMain = new Intent(activity_login.this, activity_main.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentMain);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                } else {

                    String erro;

                    try {
                        throw task.getException();
                    }catch (Exception e) {
                        erro="E-mail e/ou senha errada(s).";
                    }
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    frameLayout.setClickable(true);
                    button_cadastro.setClickable(true);
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    public void AutenticarUser() {

        try {

            String emailUser = input_Nome.getText().toString();
            String senhaUser = input_Password.getText().toString();

            User user = new User();
            user.setEmail_usuario(emailUser);
            user.setSenha_usuario(senhaUser);

            usuarioDAOMYsql usuarioDAOMYsql = new usuarioDAOMYsql();
            ResultSet resultSet = usuarioDAOMYsql.autenticaUsuarioAWS(user);

            if(resultSet.next()) {

                Toast.makeText(this, "Sucesso ao autenticar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_login.this, activity_main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }else {

                Toast.makeText(this, "Erro ao autenticar", Toast.LENGTH_SHORT).show();
            }

        }catch (SQLException e) {
            Log.d("ERRO SQL AUT", "ERRO SQL" + e);
        }

    }


}



