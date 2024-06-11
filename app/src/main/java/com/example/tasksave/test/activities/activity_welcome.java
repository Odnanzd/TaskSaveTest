package com.example.tasksave.test.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import com.example.tasksave.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class activity_welcome extends AppCompatActivity{

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "AtualizacaoApp";

    Button buttonEntrar;
    Button buttonCadastrar;
    private Double versionAPPDouble;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonEntrar = findViewById(R.id.buttonEntrar);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            buttonCadastrar.setClickable(false);
            buttonEntrar.setClickable(false);
            verificarAtualizacao();
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                verificarAtualizacao();
            } else {
                Log.e(TAG, "Permissão negada para escrever no armazenamento externo.");
            }
        }
    }

    public void verificarAtualizacao() {

        String str="Atualização disponível, realizando download...Por favor, peço que aguarde.";

        String versaoAtual = obterVersaoAtual();
        String urlVersao = "https://raw.githubusercontent.com/Odnanzd/TaskSaveAPK/main/versao.txt"; // URL do arquivo de versão no GitHub
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlVersao).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String versaoMaisRecente = response.body().string().trim();
                if (versaoMaisRecente.compareTo(versaoAtual) > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity_welcome.this, str, Toast.LENGTH_SHORT).show();
                        }
                    });
                    baixarEInstalarAtualizacao();
                }else {
                    buttonCadastrar.setClickable(true);
                    buttonEntrar.setClickable(true);
                }
            }
        });
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
    private void baixarEInstalarAtualizacao() {
        String urlAPK = "https://raw.githubusercontent.com/Odnanzd/TaskSaveAPK/main/TaskSave.apk"; // URL do APK no GitHub
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlAPK).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str="Aplicativo baixado, prosseguir com o processo de instalação.";
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "TaskSave.apk");
                try (FileOutputStream fos = new FileOutputStream(apkFile)) {
                    fos.write(response.body().bytes());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity_welcome.this, str, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                instalarApk(apkFile);
            }
        });
    }

    private void instalarApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        Uri apkUri = FileProvider.getUriForFile(this, "com.example.tasksave.test.provider", apkFile);
        intent.setData(apkUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

}

