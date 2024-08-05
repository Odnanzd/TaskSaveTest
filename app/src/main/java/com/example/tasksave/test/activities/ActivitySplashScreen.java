package com.example.tasksave.test.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.tasksave.test.conexaoMYSQL.ConnectionClass;
import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.User;
import com.example.tasksave.R;
import com.example.tasksave.test.servicesreceiver.AlarmScheduler;
import com.example.tasksave.test.servicos.ServicosATT;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesConfg;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

public class ActivitySplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1500;
    ImageView imageView;
    Connection con;
    String str, str2;
    TextView textViewInfo;
    Button buttonTenta;
    private ServicosATT servicosATT;

    private class ConsultaBancoDados extends AsyncTask<Void, Void, Void> {
        ProgressBar progressBar;

        public ConsultaBancoDados(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {


            SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivitySplashScreen.this);

            if (!isNetworkConnected(ActivitySplashScreen.this)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textViewInfo.setVisibility(View.VISIBLE);
                        textViewInfo.setText("Você está sem internet.");

                        buttonTenta.setVisibility(View.VISIBLE);
                        buttonTenta.setText("Iniciar no MODO OFFLINE");
                        buttonTenta.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ExibirFingerprint();
                            }
                        });
                    }
                });
            }

            ConnectionClass connectionClass = new ConnectionClass();

                try {
                    con = connectionClass.CONN();
                    if (con == null) {
                        str = "Erro de autenticação";

                    } else {

                        User user = new User();
                        user.setEmail_usuario(sharedPreferencesUsuario.getEmailLogin());
                        user.setSenha_usuario(sharedPreferencesUsuario.getSenhaLogin());

                        UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
                        ResultSet resultSet = usuarioDAOMYsql.autenticaUsuarioAWS(user);

                        SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(ActivitySplashScreen.this);
                        boolean arquivoATT = sharedPreferencesConfg.getAtualiza();

                        if(!arquivoATT) {

                            String versaoAtual = obterVersaoAtual();
                            double versaoDBApp = usuarioDAOMYsql.getVersionAPP();
                            String versaoDBAppString = String.valueOf(versaoDBApp);
                            servicosATT = new ServicosATT(ActivitySplashScreen.this, versaoAtual, versaoDBAppString);
                            boolean attDisponivel = servicosATT.verificaAtt();


                            if(attDisponivel) {

                                try {

                                    String versaoTextoAPP = usuarioDAOMYsql.getTextoVersaoAPP();
                                    String versaoTexto1 = usuarioDAOMYsql.getTexto1APP();
                                    String versaoTexto2 = usuarioDAOMYsql.getTexto2APP();
                                    String versaoTexto3 = usuarioDAOMYsql.getTexto3APP();


                                    sharedPreferencesConfg.armazenaTextoAPP(versaoTextoAPP);
                                    sharedPreferencesConfg.armazenaTexto1(versaoTexto1);
                                    sharedPreferencesConfg.armazenaTexto2(versaoTexto2);
                                    sharedPreferencesConfg.armazenaTexto3(versaoTexto3);


                                } catch (Exception e) {

                                    Log.d("ERRO SQL AUT", "ERRO SQL" + e);

                                    // Retorne valores padrão em caso de erro
                                }

                                sharedPreferencesConfg.armazenaAtualizaDisponivel(true);


                            }else {

                                sharedPreferencesConfg.armazenaAtualizaDisponivel(false);

                            }
                        }

                        if (resultSet.next()) {
                            // Sucesso na autenticação
                            str = "Sucesso";
                            if (sharedPreferencesUsuario.getSalvarSenha() && sharedPreferencesUsuario.getBiometriaUsuario()) {



                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ExibirFingerprint(); // Chama o método ExibirFingerprint() na thread principal
                                    }
                                });

                            } else if (sharedPreferencesUsuario.getSalvarSenha() && !sharedPreferencesUsuario.getBiometriaUsuario()) {

                                Intent intent = new Intent(ActivitySplashScreen.this, ActivityMain.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else {
                                // Falha na autenticação
                                str2 = "ERRO";
                                Toast.makeText(ActivitySplashScreen.this, "ERRO", Toast.LENGTH_SHORT).show();
                            }
                        }else {


                            AgendaDAO agendaDAO = new AgendaDAO(ActivitySplashScreen.this);
                            ArrayList<Long> ids = agendaDAO.idTarefasLembrete();

                            for(long id : ids) {
                                int idInt = (int) id;
                                AlarmScheduler.cancelAlarm(ActivitySplashScreen.this, idInt);
                            }
                            agendaDAO.excluiTabelaAgenda();
                            sharedPreferencesConfg.clearShareds();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    str2 = "ERRO";
                                    Toast.makeText(getBaseContext(), "Dados inválidos, realizar login novamente.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                } catch(SQLException e){

                    Log.d("ERRO SQL AUT", "ERRO SQL" + e);
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE); // Oculta o ProgressBar
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.splashImageView);
        textViewInfo = findViewById(R.id.textViewInfo);
        buttonTenta = findViewById(R.id.button_fing_tenta);

        carregaTema();
        gifThemeMode();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.default_notification_channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_MAX;

            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            long[] pattern = {0, 1000, 500, 1000};
            channel.setVibrationPattern(pattern);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivitySplashScreen.this);

                if (!sharedPreferencesUsuario.getPrimeiroAcesso()) {

                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityWelcome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if (!sharedPreferencesUsuario.getSalvarSenha()) {

                    SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(ActivitySplashScreen.this);

                    AgendaDAO agendaDAO = new AgendaDAO(ActivitySplashScreen.this);
                    ArrayList<Long> ids = agendaDAO.idTarefasLembrete();

                    for(long id : ids) {
                        int idInt = (int) id;
                        AlarmScheduler.cancelAlarm(ActivitySplashScreen.this, idInt);
                    }
                    agendaDAO.excluiTabelaAgenda();

                    sharedPreferencesConfg.clearShareds();

                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }else {

                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    new ConsultaBancoDados(progressBar).execute();
                }

            }
        }, SPLASH_TIME_OUT);

    }

    public void ExibirFingerprint() {

        ChecarBiometria();

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(ActivitySplashScreen.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                buttonTenta.setVisibility(View.VISIBLE);
                Toast.makeText(ActivitySplashScreen.this, "Erro de autenticação " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(ActivitySplashScreen.this, "Sucesso", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ActivitySplashScreen.this, ActivityMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                buttonTenta.setVisibility(View.VISIBLE);
                Toast.makeText(ActivitySplashScreen.this, "Erro", Toast.LENGTH_SHORT).show();
            }
        });
        BiometricPrompt.PromptInfo.Builder promptInfo = CaixaDialogo();
        promptInfo.setNegativeButtonText("Cancelar");
        biometricPrompt.authenticate(promptInfo.build());

        imageView.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promtInfo = CaixaDialogo();
            buttonTenta.setVisibility(View.GONE);
            promtInfo.setNegativeButtonText("Cancelar");
            biometricPrompt.authenticate(promtInfo.build());
        });
        buttonTenta.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promtInfo = CaixaDialogo();
            promtInfo.setDeviceCredentialAllowed(true);
            biometricPrompt.authenticate(promtInfo.build());
        });
    }

    BiometricPrompt.PromptInfo.Builder CaixaDialogo() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login fingerprint")
                .setSubtitle("Faça o login utilizando sua impressão digital");
    }

    public void ChecarBiometria () {
        String info;
        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK |
                BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                info = "Impressão digital detectada";
                enableButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info = "Dispositivo não possui hardware de impressão digital";
                enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info = "Dispositivo está com o hardware comprometido";
                enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info = "Não há autentição fingerprint definido neste dispositivo";
                enableButton(true, false);
                break;
            default:
                info = "Erro desconhecido";
                break;

        }
        textViewInfo.setVisibility(View.VISIBLE);
        textViewInfo.setText(info);
    }
    void enableButton(boolean enable) {

        imageView.setEnabled(enable);

    }
    void enableButton(boolean enable, boolean enroll) {
        enableButton(enable);
        if (!enroll) return;
        Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.BIOMETRIC_WEAK);
        startActivity(enrollIntent);
    }

    public void gifThemeMode() {

        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        int mode = uiModeManager.getNightMode();

        SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(ActivitySplashScreen.this);
        String temaCarregado = sharedPreferencesConfg.getTema();

        if (temaCarregado.equals("Escuro")) {
            // Modo escuro ativado
            Glide.with(this)
                    .load(R.raw.tasksavegid) // Substitua gif_escuro pelo nome do seu GIF escuro na pasta res/raw
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

        } else if(temaCarregado.equals("Claro")) {
            // Modo escuro não ativado
            Glide.with(this)
                    .load(R.raw.tasksavelight) // Substitua gif_claro pelo nome do seu GIF claro na pasta res/raw
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

        }else if (mode == UiModeManager.MODE_NIGHT_YES) {
            // Modo escuro ativado
            Glide.with(this)
                    .load(R.raw.tasksavegid) // Substitua gif_escuro pelo nome do seu GIF escuro na pasta res/raw
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

        } else {
            // Modo escuro não ativado
            Glide.with(this)
                    .load(R.raw.tasksavelight) // Substitua gif_claro pelo nome do seu GIF claro na pasta res/raw
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

        }
    }

    public void carregaTema() {

        SharedPreferencesConfg sharedPreferencesConfg = new SharedPreferencesConfg(ActivitySplashScreen.this);
        String temaCarregado = sharedPreferencesConfg.getTema();

        switch (temaCarregado) {

            case "Automático":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "Escuro":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "Claro":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
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