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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tasksave.test.conexaoSQLite.Conexao;
import com.example.tasksave.R;
import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.dao.AgendaDAOMYsql;
import com.example.tasksave.test.objetos.Agenda;
import com.example.tasksave.test.servicesreceiver.AlarmScheduler;
import com.example.tasksave.test.servicos.ServicosATT;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesConfg;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityMain extends AppCompatActivity {
    private ServicosATT servicosATT;


    public ImageView imageView;
    public TextView text_view_main;
    private long pressedTime;
    private static final int GALLERY_REQUEST_CODE = 1001;
    private TextView textViewPendente;
    private TextView textViewData;


    private LinearLayout linearLayoutAgenda, linearLayoutCalendar, linearLayoutSenha,
            linearLayoutArquivo, linearLayoutConfig, linearLayoutLogout;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

//        executaTeste();
        LocalDateTime dateTimeAtual = LocalDateTime.now();

        LocalDate localdate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        Instant instant = dateTimeAtual.atZone(ZoneId.systemDefault()).toInstant();
        Timestamp ts = Timestamp.valueOf("");

        Log.d("TESTE LOCAL DATE", "LocalDateTime: "+dateTimeAtual);
        Log.d("TESTE LOCAL DATE", "LocalDate: "+localdate);
        Log.d("TESTE LOCAL DATE", "LocalTime: "+localTime);
//        Log.d("TESTE LOCAL DATE", "LocalDateTime: "+localDateTime);



        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("ArquivoATT", Context.MODE_PRIVATE);
        boolean arquivoATT = sharedPrefs2.getBoolean("NaoATT", false);

        Log.d("TESTE ARQUIVO", "TESTE ARQUIVO"+arquivoATT);

        if(!arquivoATT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    dialogAtt();
                }
            } else {
                verificarPermissoes();
            }
        }
        verificarPermissaoNotifica();


        linearLayoutConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentConfig = new Intent(ActivityMain.this, ActivityConfg.class);
                intentConfig.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentConfig);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        linearLayoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityMain.this, "Saindo...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        linearLayoutAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ActivityMain.this, ActivityAgenda.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        linearLayoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ActivityMain.this, ActivityCalendar.class);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("TESTE VERIFICAR PERMI", "PERMISSAO");

        }else {
            dialogAtt();
        }
    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    protected void onResume() {
//        super.onResume();
////        VerificarAtrasos();
//    }

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

        SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityMain.this);
        String sharedPrd = sharedPreferencesUsuario.getUsuarioLogin();

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

        SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityMain.this);

        if(!sharedPreferencesUsuario.getPrimeiroAcessoBiometriaUsuario() && sharedPreferencesUsuario.getSalvarSenha()){

            Dialog dialog = new Dialog(ActivityMain.this, R.style.DialogAboveKeyboard);
            dialog.setContentView(R.layout.dialog_fingerprint); // Defina o layout do diálogo
            dialog.setCancelable(true); // Permita que o usuário toque fora do diálogo para fechá-lo
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

            Button button = dialog.findViewById(R.id.button_login);
            Button button2 = dialog.findViewById(R.id.button_login2);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sharedPreferencesUsuario.armazenaBiometria(true);

                    sharedPreferencesUsuario.armazenaPrimeiroAcessoFingerprint(true);

                    dialog.dismiss();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sharedPreferencesUsuario.armazenaBiometria(false);

                    sharedPreferencesUsuario.armazenaPrimeiroAcessoFingerprint(true);


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
                dialogFingerprint();
                break;

        }
    }
    public void dialogAtt() {

        SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(ActivityMain.this);
        boolean attDisp = sharedPreferencesConfg.getAtualizaDisponivel();

        String versaoTextoAPP = sharedPreferencesConfg.getTextoAPP();
        String versaoTexto1 = sharedPreferencesConfg.getTexto1();
        String versaoTexto2 = sharedPreferencesConfg.getTexto2();
        String versaoTexto3 = sharedPreferencesConfg.getTexto3();

        if(attDisp) {
            servicosATT = new ServicosATT(ActivityMain.this, "1", "100");
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
    public void verificarPermissaoNotifica() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, você pode enviar notificações
            } else {
                // Permissão negada, trate o caso adequadamente
            }
        }
    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void executaTeste() {
//
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
//
//            AgendaDAO agendaDAO = new AgendaDAO(ActivityMain.this);
//            AgendaDAOMYsql agendaDAOMYsql = new AgendaDAOMYsql();
//
//            List<Agenda> agendaMYSQL = agendaDAOMYsql.tarefasUsuario(14);
//
//            for(Agenda agenda : agendaMYSQL) {
//                Log.d("Teste List", "MYSQL: " +agenda.getNomeAgenda());
//                Log.d("Teste List", "MYSQL: " +agenda.getDescriçãoAgenda());
//                Log.d("Teste List", "MYSQL: " +agenda.getDate());
//                Log.d("Teste List", "MYSQL: " +agenda.getHoraAgenda());
//                Log.d("Teste List", "MYSQL: " +agenda.getMinutoAgenda());
//                Log.d("Teste List", "MYSQL: " +agenda.getLembrete());
//                Log.d("Teste List", "MYSQL: " +agenda.getFinalizado());
//                Log.d("Teste List", "MYSQL: " +agenda.getDataAgendaFimString());
//                Log.d("Teste List", "MYSQL: " +agenda.getHoraAgendaFim());
//                Log.d("Teste List", "MYSQL: " +agenda.getMinutoAgendaFim());
//                Log.d("Teste List", "MYSQL: " +agenda.getDataAgendaInsertString());
//                Log.d("Teste List", "MYSQL: " +agenda.getHoraAgendaInsert());
//                Log.d("Teste List", "MYSQL: " +agenda.getMinutoAgendaInsert());
//                Log.d("Teste List", "MYSQL: " +agenda.getAgendaAtraso());
//                Log.d("Teste List", "MYSQL: " +agenda.getRepetirLembrete());
//                Log.d("Teste List", "MYSQL: " +agenda.getRepetirModo());
//                Log.d("Teste List", "MYSQL: " +agenda.isNotificado());
//            }
//
//            List<Agenda> agendaSQLite = agendaDAO.tarefasAgenda();
//
//            for(Agenda agenda : agendaSQLite) {
//                Log.d("Teste List", "SQLITE: " +agenda.getNomeAgenda());
//                Log.d("Teste List", "SQLITE: " +agenda.getDescriçãoAgenda());
//                Log.d("Teste List", "SQLITE: " +agenda.getDate());
//                Log.d("Teste List", "SQLITE: " +agenda.getHoraAgenda());
//                Log.d("Teste List", "SQLITE: " +agenda.getMinutoAgenda());
//                Log.d("Teste List", "SQLITE: " +agenda.getLembrete());
//                Log.d("Teste List", "SQLITE: " +agenda.getFinalizado());
//                Log.d("Teste List", "SQLITE: " +agenda.getDataAgendaFimString());
//                Log.d("Teste List", "SQLITE: " +agenda.getHoraAgendaFim());
//                Log.d("Teste List", "SQLITE: " +agenda.getMinutoAgendaFim());
//                Log.d("Teste List", "SQLITE: " +agenda.getDataAgendaInsertString());
//                Log.d("Teste List", "SQLITE: " +agenda.getHoraAgendaInsert());
//                Log.d("Teste List", "SQLITE: " +agenda.getMinutoAgendaInsert());
//                Log.d("Teste List", "SQLITE: " +agenda.getAgendaAtraso());
//                Log.d("Teste List", "SQLITE: " +agenda.getRepetirLembrete());
//                Log.d("Teste List", "SQLITE: " +agenda.getRepetirModo());
//                Log.d("Teste List", "SQLITE: " +agenda.isNotificado());
//            }
//
//
//            boolean areEqual = compareLists(agendaSQLite, agendaMYSQL);
//
//                Log.d("SAO IGUAIS: ", "IGUAIS: "+areEqual);
//
//                });
//
//    }
//    public static <Agenda> boolean compareLists(List<Agenda> list1, List<Agenda> list2) {
//        if (list1.size() != list2.size()) {
//            Log.d("Teste List", "Tetse: " + list1.size() + "/" + list2.size());
//            return false;
//        }
//
//        for (int i = 0; i < list1.size(); i++) {
//            if (!list1.get(i).equals(list2.get(i))) {
//                Log.d("Teste List", "Item diferente: " + list1.get(i) + " != " + list2.get(i));
//                return false;
//            }
//        }
//
//        return true;
//    }
}