package com.example.tasksave.test.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;

import com.example.tasksave.test.conexaoSQLite.Conexao;
import com.example.tasksave.R;
import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.servicos.ServicosATT;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class activity_main extends AppCompatActivity {
    private ServicosATT servicosATT;


    public ImageView imageView;
    public TextView text_view_main;
    private long pressedTime;

    public ImageView imageView1;

    public ImageView imageView2;
    public ImageView numeroIcon;
    private static final int GALLERY_REQUEST_CODE = 1001;
    private Conexao con;
    private SQLiteDatabase db;
    public ImageView imageViewMenuConfig;
    public ImageView imageViewMenuCalendar;
    private TextView textViewPendente;
    private TextView textViewData;


    private LinearLayout linearLayoutAgenda, linearLayoutCalendar, linearLayoutSenha,
            linearLayoutArquivo, linearLayoutConfig, linearLayoutLogout;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view_main = findViewById(R.id.textView21);

        linearLayoutAgenda = findViewById(R.id.linearLayoutAgenda);
        linearLayoutCalendar = findViewById(R.id.linearLayoutData);
        linearLayoutSenha = findViewById(R.id.linearLayoutPassw);
        linearLayoutArquivo = findViewById(R.id.linearLayoutArquiv);
        linearLayoutConfig = findViewById(R.id.linearLayoutConfig);
        linearLayoutLogout = findViewById(R.id.linearLayoutLogot);

        textViewPendente = findViewById(R.id.textViewContador);
        textViewData = findViewById(R.id.textView19);

        dataAtualFormatada();


        ExibirUsername();
//        VerificarAtrasos();
        attContadorPendente();
        ChecarBiometria();

        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("ArquivoATT", Context.MODE_PRIVATE);
        boolean arquivoATT = sharedPrefs2.getBoolean("NaoATT", false);

        if(!arquivoATT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    verificarPermissoes();
                }
            } else {
                verificarPermissoes();
            }
        }


        linearLayoutConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentConfig = new Intent(activity_main.this, activity_config.class);
                intentConfig.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentConfig);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        linearLayoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_main.this, "Saindo...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        linearLayoutAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(activity_main.this, activity_agenda.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        linearLayoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(activity_main.this, activity_calendar.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
//            }
//        });
    }
    private void verificarPermissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            dialogAtt();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
//        VerificarAtrasos();
    }

    // Sobrescrever o método onActivityResult para tratar a imagem selecionada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Obter o URI da imagem selecionada
            ImageView imageView = findViewById(R.id.imageView2);
            imageView.setImageURI(data.getData());
        }
    }

    public void ExibirUsername() {

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvarUser", Context.MODE_PRIVATE);
        String sharedPrd = sharedPrefs.getString("userString", "");

        String[] Nomes = sharedPrd.split(" ");
        if (Nomes.length > 0) {
            // A primeira palavra estará no índice 0 do array
            String primeiroNome = Nomes[0];
            text_view_main.setText("Olá, "+primeiroNome);

        } else {

            text_view_main.setText("Olá, "+sharedPrd);
        }
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Pressione Voltar de novo para sair.", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public void dialogFingerprint() {


        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("ArquivoPrimeiroAcessoFingerPrint", Context.MODE_PRIVATE);
        SharedPreferences sharedPrefs3 = getApplicationContext().getSharedPreferences("arquivoSalvarSenha", Context.MODE_PRIVATE);

        if(!sharedPrefs2.getBoolean("PrimeiroAcessoFingerPrint", false) && sharedPrefs3.getBoolean("SalvarSenha", false)){

            Dialog dialog = new Dialog(activity_main.this, R.style.DialogAboveKeyboard);
            dialog.setContentView(R.layout.dialog_fingerprint); // Defina o layout do diálogo
            dialog.setCancelable(true); // Permita que o usuário toque fora do diálogo para fechá-lo
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

            Button button = dialog.findViewById(R.id.button_login);
            Button button2 = dialog.findViewById(R.id.button_login2);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences prefs = getSharedPreferences("ArquivoFingerPrint", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("AcessoFingerPrint", true);
                    editor.commit();

                    SharedPreferences prefs2 = getSharedPreferences("ArquivoPrimeiroAcessoFingerPrint", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = prefs2.edit();
                    editor2.putBoolean("PrimeiroAcessoFingerPrint", true);
                    editor2.commit();

                    dialog.dismiss();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences prefs = getSharedPreferences("ArquivoFingerPrint", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("AcessoFingerPrint", false);
                    editor.commit();

                    SharedPreferences prefs2 = getSharedPreferences("ArquivoPrimeiroAcessoFingerPrint", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = prefs2.edit();
                    editor2.putBoolean("PrimeiroAcessoFingerPrint", true);
                    editor2.commit();

                    dialog.dismiss();
                }
            });


            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }



    }
    private void ChecarBiometria() {

        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK |
                BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("Verificar Biometreia", "Sucesso");
                dialogFingerprint();
                break;

        }
    }
    public void dialogAtt() {

        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("ArquivoAttDisp", Context.MODE_PRIVATE);

        SharedPreferences sharedPrefs3 = getApplicationContext().getSharedPreferences("ArquivoTextoAPP", Context.MODE_PRIVATE);
        SharedPreferences sharedPrefs4 = getApplicationContext().getSharedPreferences("ArquivoTexto1", Context.MODE_PRIVATE);
        SharedPreferences sharedPrefs5 = getApplicationContext().getSharedPreferences("ArquivoTexto2", Context.MODE_PRIVATE);
        SharedPreferences sharedPrefs6 = getApplicationContext().getSharedPreferences("ArquivoTexto3", Context.MODE_PRIVATE);

        String versaoTextoAPP = sharedPrefs3.getString("ATT", "");
        String versaoTexto1 = sharedPrefs4.getString("ATT", "");
        String versaoTexto2 = sharedPrefs5.getString("ATT", "");
        String versaoTexto3 = sharedPrefs6.getString("ATT", "");


        if(sharedPrefs2.getBoolean("Atualizacao", false)) {
            servicosATT = new ServicosATT(activity_main.this, "1", "100");
            servicosATT.dialogAtt(versaoTextoAPP, versaoTexto1, versaoTexto2, versaoTexto3);
        }
    }
    public void attContadorPendente() {

        AgendaDAO agendaDAO = new AgendaDAO(this);

        String contador = agendaDAO.verificaTarefaPendente();

        textViewPendente.setText(contador);

    }
    public void dataAtualFormatada() {
        Date currentDate = Calendar.getInstance().getTime();

        // Obtenha a locale atual do dispositivo
        Locale currentLocale = Locale.getDefault();

        // Formate a data conforme o idioma do telefone
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, currentLocale);
        String formattedDate = dateFormat.format(currentDate);

        // Mostre a data formatada em um TextView (opcional)
        textViewData.setText(formattedDate);
    }



}