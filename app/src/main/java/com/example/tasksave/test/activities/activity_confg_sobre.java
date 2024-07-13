package com.example.tasksave.test.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tasksave.R;
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.VersaoAPP;
import com.example.tasksave.test.servicos.ServicosATT;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesConfg;

import org.w3c.dom.Text;

public class activity_confg_sobre extends AppCompatActivity {
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        if(processoAtt) {

            Toast.makeText(getBaseContext(), "Por favor, aguarde.", Toast.LENGTH_LONG).show();

        }else {
            Intent intent = new Intent(activity_confg_sobre.this, activity_config.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }
    private LinearLayout linearLayoutAtt, linearLayoutAttAuto;
    private TextView textViewVersaoApp;
    private ServicosATT servicosATT;
    private ImageView imageViewBack, imageViewSeta;
    private ProgressBar progressBar;
    private Switch aSwitch;
    private boolean processoAtt=false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg_sobre);

        linearLayoutAtt = findViewById(R.id.linearLayout2);
        linearLayoutAttAuto = findViewById(R.id.linearLayoutAtt);

        textViewVersaoApp = findViewById(R.id.textViewVersaoApp);
        imageViewBack = findViewById(R.id.imageView4);
        imageViewSeta = findViewById(R.id.imageView5);

        progressBar = findViewById(R.id.progressbar1);

        aSwitch = findViewById(R.id.switchAttAuto);


        textViewVersaoApp.setText(obterVersaoAtual());
        attAutoSwitch();

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

        linearLayoutAttAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aSwitch.toggle();

                if (aSwitch.isChecked()) {
                    attAutoPositivo();
                }else {
                    attAutoNegativo();
                }
            }
        });




    }
    private class VerificaVersaoTask extends AsyncTask<Void, Void, VersaoAPP> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Mostra a barra de progresso antes de iniciar a tarefa
            Toast.makeText(getBaseContext(),"Aguarde...", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            imageViewSeta.setVisibility(View.GONE);
            linearLayoutAtt.setEnabled(false);
            imageViewBack.setEnabled(false);
            linearLayoutAttAuto.setEnabled(false);
            processoAtt = true;
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
            linearLayoutAttAuto.setEnabled(true);
            processoAtt = false;

            // Configura o ServicosATT com a versão obtida
            String versaoAtual = obterVersaoAtual();

            servicosATT = new ServicosATT(activity_confg_sobre.this, versaoAtual, versaoAPP.getVersionDB());

            boolean sucesso = servicosATT.verificaAtt();

            if(sucesso) {

                servicosATT.dialogAtt(versaoAPP.getVersionTextApp(), versaoAPP.getVersionText1(), versaoAPP.getVersionText2(), versaoAPP.getVersionText3());

            }else {
                Toast.makeText(getBaseContext(),"Não há atualizações disponíveis", Toast.LENGTH_LONG).show();
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
                }else{
                    Toast.makeText(getBaseContext(), "Você não tem as permissões necessárias para prosseguir com a atualização", Toast.LENGTH_LONG).show();
                }
            } else {
                verificarPermissoes();
            }
        }
    private void verificarPermissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(), "Você não tem as permissões necessárias para prosseguir com a atualização", Toast.LENGTH_LONG).show();
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
    public void attAutoSwitch() {

        SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(activity_confg_sobre.this);
        boolean attAuto = sharedPreferencesConfg.getAtualiza();

        if (!attAuto) {
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }
    }

    public void attAutoPositivo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity_confg_sobre.this);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setMessage("Deseja confirmar as alterações e sair? ");
        builder.setNegativeButton("Não", (dialog, which) -> {
            attAutoSwitch();
        });
        builder.setPositiveButton("Sim", (dialog, which) -> {
            // Ação para o botão OK

            SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(activity_confg_sobre.this);
            sharedPreferencesConfg.armazenaAtualiza(false);

            finish();
            Toast.makeText(getBaseContext(), "Atualização automática ativada", Toast.LENGTH_SHORT).show();

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void attAutoNegativo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity_confg_sobre.this);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setMessage("Deseja confirmar as alterações e sair? ");
        builder.setNegativeButton("Não", (dialog, which) -> {
            attAutoSwitch();

        });
        builder.setPositiveButton("Sim", (dialog, which) -> {
            // Ação para o botão OK
            SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(activity_confg_sobre.this);
            sharedPreferencesConfg.armazenaAtualiza(true);

            finish();
            Toast.makeText(getBaseContext(), "Atualização automática desativada", Toast.LENGTH_SHORT).show();

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}