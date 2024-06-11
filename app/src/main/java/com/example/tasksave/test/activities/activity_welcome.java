package com.example.tasksave.test.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.tasksave.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class activity_welcome extends AppCompatActivity{

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "AtualizacaoApp";
    private static final int MANAGE_STORAGE_PERMISSION_REQUEST_CODE = 2;

    Button buttonEntrar;
    Button buttonCadastrar;
    ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonEntrar = findViewById(R.id.buttonEntrar);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
        progressBar = findViewById(R.id.progressBar);

        buttonEntrar.setClickable(false);
        buttonCadastrar.setClickable(false);

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

    private void verificarPermissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            verificarAtualizacao();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                verificarAtualizacao();
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


    public void verificarAtualizacao() {

        String str="Atualização disponível, realizando download...Por favor, peço que aguarde.";

        String versaoAtual = obterVersaoAtual();
        String versaoTeste = "2.0";
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
                if (versaoMaisRecente.compareTo(versaoTeste) > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogAtt();
//                            Toast.makeText(activity_welcome.this, str, Toast.LENGTH_SHORT).show();
                        }
                    });
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

        Dialog dialog2 = new Dialog(activity_welcome.this, R.style.DialogAboveKeyboard);
        dialog2.setContentView(R.layout.dialog_progress_bar); // Defina o layout do diálogo
        dialog2.setCancelable(false); // Permita que o usuário toque fora do diálogo para fechá-lo
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.getWindow().setGravity(Gravity.CENTER);

        ProgressBar progressBar1 = dialog2.findViewById(R.id.progressBarDialog);

        runOnUiThread(() -> dialog2.show());
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = "Aplicativo baixado, prosseguir com o processo de instalação.";
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "TaskSave.apk");

                // Deleta o arquivo antigo se existir
                if (apkFile.exists()) {
                    apkFile.delete();
                }

                ResponseBody body = response.body();
                if (body != null) {
                    long fileSize = body.contentLength();
                    InputStream inputStream = body.byteStream();
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    byte[] buffer = new byte[2048];
                    int bytesRead;
                    long totalBytesRead = 0;

                    runOnUiThread(() -> progressBar1.setVisibility(View.VISIBLE));

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        totalBytesRead += bytesRead;
                        fos.write(buffer, 0, bytesRead);

                        final int progress = (int) ((totalBytesRead * 100) / fileSize);
                        runOnUiThread(() -> progressBar1.setProgress(progress));
                    }

                    fos.flush();
                    fos.close();
                    inputStream.close();

                    runOnUiThread(() -> {
                        progressBar1.setVisibility(View.GONE);
                        dialog2.dismiss();
                        Toast.makeText(activity_welcome.this, str, Toast.LENGTH_SHORT).show();
                    });

                    instalarApk(apkFile);
                }
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
    void dialogAtt() {

        Dialog dialog = new Dialog(activity_welcome.this, R.style.DialogAboveKeyboard);
        dialog.setContentView(R.layout.dialog_att_versao); // Defina o layout do diálogo
        dialog.setCancelable(false); // Permita que o usuário toque fora do diálogo para fechá-lo
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        FrameLayout frameLayout = dialog.findViewById(R.id.framelayout1);
        FrameLayout frameLayoutNao = dialog.findViewById(R.id.framelayout2);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setClickable(false);
                frameLayoutNao.setClickable(false);
                baixarEInstalarAtualizacao();
            }
        });

        frameLayoutNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }



}

