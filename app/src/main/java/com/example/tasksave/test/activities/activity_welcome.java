package com.example.tasksave.test.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import com.example.tasksave.R;
import com.example.tasksave.test.dao.usuarioDAOMYsql;
import com.example.tasksave.test.servicos.ServicosATT;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class activity_welcome extends AppCompatActivity{
    private ServicosATT servicosATT;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "AtualizacaoApp";
    private static final int MANAGE_STORAGE_PERMISSION_REQUEST_CODE = 2;

    Button buttonEntrar;
    Button buttonCadastrar;
    ProgressBar progressBar;
    private RelativeLayout loadingOverlay;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonEntrar = findViewById(R.id.buttonEntrar);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
        progressBar = findViewById(R.id.progressBar);
        loadingOverlay = findViewById(R.id.loadingOverlay);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MANAGE_STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                verificarPermissoes();
            }
        } else {
            verificarPermissoes();
        }


        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity_welcome.this, activity_login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_welcome.this, activity_cadastro.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

    }
    private class VerificaVersaoTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Mostra a barra de progresso antes de iniciar a tarefa
            loadingOverlay.setVisibility(View.VISIBLE);
            buttonEntrar.setVisibility(View.GONE);
            buttonCadastrar.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(Void... voids) {
            // Consulta ao banco de dados em segundo plano
            return versaoDB();
        }

        @Override
        protected void onPostExecute(String versaoDB) {
            super.onPostExecute(versaoDB);
            // Oculta a barra de progresso após a conclusão da tarefa
            loadingOverlay.setVisibility(View.GONE);
            buttonEntrar.setVisibility(View.VISIBLE);
            buttonCadastrar.setVisibility(View.VISIBLE);

            // Configura o ServicosATT com a versão obtida
            String versaoAtual = obterVersaoAtual();
            servicosATT = new ServicosATT(activity_welcome.this, versaoAtual, versaoDB);
            boolean sucesso = servicosATT.verificaAtt();
            Log.d("TESTE BOLEAN", "TESTE"+sucesso);
        }
    }

    public String versaoDB() {
        try {
            usuarioDAOMYsql usuarioDAOMYsql = new usuarioDAOMYsql();
            double versaoDBApp = usuarioDAOMYsql.getVersionAPP();
            return String.valueOf(versaoDBApp);
        } catch (Exception e) {
            Log.d("ERRO SQL AUT", "ERRO SQL" + e);
            return "0.0"; // Retorne uma versão padrão em caso de erro
        }
    }

    private void verificarPermissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            new VerificaVersaoTask().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new VerificaVersaoTask().execute();
            } else {
                Log.e(TAG, "Permissão negada para escrever no armazenamento externo.");
                Toast.makeText(this, "Permissão negada para escrever no armazenamento externo.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MANAGE_STORAGE_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    verificarPermissoes();
                } else {
                    Toast.makeText(this, "Permissão de acesso total ao armazenamento não concedida.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public String obterVersaoAtual() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}

