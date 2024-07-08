package com.example.tasksave.test.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tasksave.R;
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.VersaoAPP;
import com.example.tasksave.test.servicos.ServicosATT;

import org.w3c.dom.Text;

public class activity_confg_sobre extends AppCompatActivity {


    private LinearLayout linearLayoutAtt;
    private TextView textViewVersaoApp;
    private ServicosATT servicosATT;
    private ImageView imageViewBack, imageViewSeta;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_sobre);

        linearLayoutAtt = findViewById(R.id.linearLayout2);
        textViewVersaoApp = findViewById(R.id.textViewVersaoApp);
        imageViewBack = findViewById(R.id.imageView4);
        imageViewSeta = findViewById(R.id.imageView5);

        progressBar = findViewById(R.id.progressbar1);


        textViewVersaoApp.setText(obterVersaoAtual());

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(activity_confg_sobre.this, activity_config.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        linearLayoutAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaPermissão();
            }
        });



    }
    private class VerificaVersaoTask extends AsyncTask<Void, Void, VersaoAPP> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Mostra a barra de progresso antes de iniciar a tarefa
            progressBar.setVisibility(View.VISIBLE);
            imageViewSeta.setVisibility(View.GONE);
            linearLayoutAtt.setEnabled(false);
            imageViewBack.setEnabled(false);
        }

        @Override
        protected VersaoAPP doInBackground(Void... voids) {
            // Consulta ao banco de dados em segundo plano
            return getVersionInfo();
        }

        @Override
        protected void onPostExecute(VersaoAPP versaoAPP) {
            super.onPostExecute(versaoAPP);
            // Oculta a barra de progresso após a conclusão da tarefa
            progressBar.setVisibility(View.GONE);
            imageViewSeta.setVisibility(View.VISIBLE);
            linearLayoutAtt.setEnabled(true);
            imageViewBack.setEnabled(true);

            // Configura o ServicosATT com a versão obtida
            String versaoAtual = obterVersaoAtual();

            servicosATT = new ServicosATT(activity_confg_sobre.this, versaoAtual, versaoAPP.getVersionDB());

            boolean sucesso = servicosATT.verificaAtt();

            if(sucesso) {

                servicosATT.dialogAtt(versaoAPP.getVersionTextApp(), versaoAPP.getVersionText1(), versaoAPP.getVersionText2(), versaoAPP.getVersionText3());

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
    public void verificaPermissão() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    new VerificaVersaoTask().execute();
                }
            } else {
                verificarPermissoes();
            }
        }
    private void verificarPermissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        }else {
            new VerificaVersaoTask().execute();
        }
    }
    public VersaoAPP getVersionInfo() {
        try {
            UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
            String versaoDBApp = String.valueOf(usuarioDAOMYsql.getVersionAPP());
            String versaoTextoAPP = usuarioDAOMYsql.getTextoVersaoAPP();
            String versaoTexto1 = usuarioDAOMYsql.getTexto1APP();
            String versaoTexto2 = usuarioDAOMYsql.getTexto2APP();
            String versaoTexto3 = usuarioDAOMYsql.getTexto3APP();

            return new VersaoAPP(versaoDBApp, versaoTextoAPP, versaoTexto1, versaoTexto2, versaoTexto3);

        } catch (Exception e) {

            Log.d("ERRO SQL AUT", "ERRO SQL" + e);
            return new VersaoAPP("0.0", "Erro", "Erro", "Erro", "Erro");
            // Retorne valores padrão em caso de erro
        }
    }
}