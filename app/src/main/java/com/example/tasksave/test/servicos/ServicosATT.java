package com.example.tasksave.test.servicos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.tasksave.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ServicosATT {

    String urlVersao;
    String versaoAtual;
    String versaoTeste;
    String versaoDB;
    Context context;

    public ServicosATT(Context context, String versaoAtual, String versaoDB) {
        this.versaoAtual =  versaoAtual;
        this.versaoDB = versaoDB;
        this.versaoTeste = "2.2";
        this.context = context;
        this.urlVersao = "https://raw.githubusercontent.com/Odnanzd/TaskSaveAPK/main/versao.txt";
    }

public boolean verificaAtt() {

    if (versaoAtual == null || versaoDB == null) {
        Log.e("VerificaAtt", "versaoAtual ou versaoDB são nulos");
//        return; // Saia do método se algum valor for nulo
    }

    if (versaoDB.compareTo(versaoAtual) > 0) {
        Log.d("TESTE BOOBLEAN", "TESTE");

        return true;
    }else {
        return false;
    }
}

    public void dialogAtt(String textoVers, String texto01, String texto02, String texto03) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        View sheetview= LayoutInflater.from(context).inflate(R.layout.dialog_att_versao,null);
        bottomSheetDialog.setContentView(sheetview);
        bottomSheetDialog.setCancelable(false);


        FrameLayout frameLayout = bottomSheetDialog.findViewById(R.id.framelayout1);
        FrameLayout frameLayoutNao = bottomSheetDialog.findViewById(R.id.framelayout2);

        TextView textViewTextoVersao = bottomSheetDialog.findViewById(R.id.textViewTextoVersao);
        TextView textViewTexto1 = bottomSheetDialog.findViewById(R.id.textViewTexto1);
        TextView textViewTexto2 = bottomSheetDialog.findViewById(R.id.textViewTexto2);
        TextView textViewTexto3 = bottomSheetDialog.findViewById(R.id.textViewTexto3);

        textViewTextoVersao.setText(textoVers);
        textViewTexto1.setText(texto01);
        textViewTexto2.setText(texto02);
        textViewTexto3.setText(texto03);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setClickable(false);
                baixarAtualizacao();
            }
        });

        frameLayoutNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = context.getSharedPreferences("ArquivoATT", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("NaoATT", true);
                editor.commit();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();

            }
        });
    }
    public void baixarAtualizacao() {

        String urlAPK = "https://raw.githubusercontent.com/Odnanzd/TaskSaveAPK/main/TaskSave.apk"; // URL do APK no GitHub
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlAPK).build();

        Dialog dialog2 = new Dialog(context, R.style.DialogAboveKeyboard);
        dialog2.setContentView(R.layout.dialog_progress_bar); // Defina o layout do diálogo
        dialog2.setCancelable(false); // Permita que o usuário toque fora do diálogo para fechá-lo
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.getWindow().setGravity(Gravity.CENTER);

        ProgressBar progressBar1 = dialog2.findViewById(R.id.progressBarDialog);

        ((Activity) context).runOnUiThread(() -> dialog2.show());
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

                    ((Activity) context).runOnUiThread(() -> progressBar1.setVisibility(View.VISIBLE));

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        totalBytesRead += bytesRead;
                        fos.write(buffer, 0, bytesRead);

                        final int progress = (int) ((totalBytesRead * 100) / fileSize);
                        ((Activity) context).runOnUiThread(() -> progressBar1.setProgress(progress));
                    }

                    fos.flush();
                    fos.close();
                    inputStream.close();

                    ((Activity) context).runOnUiThread(() -> {
                        progressBar1.setVisibility(View.GONE);
                        dialog2.dismiss();
                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                        instalarApk(apkFile);
                    });

                }
            }
        });

    }
    public void instalarApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        Uri apkUri = FileProvider.getUriForFile(context, "com.example.tasksave.test.provider", apkFile);
        intent.setData(apkUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

}
