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
import android.content.SharedPreferences;
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
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.User;
import com.example.tasksave.R;
import com.example.tasksave.test.servicos.ServicosATT;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

public class activity_splash_screen extends AppCompatActivity {
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


            SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(activity_splash_screen.this);

            if (!isNetworkConnected(activity_splash_screen.this)) {
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


                        SharedPreferences sharedPrefs5 = getApplicationContext().getSharedPreferences("ArquivoATT", Context.MODE_PRIVATE);
                        boolean arquivoATT = sharedPrefs5.getBoolean("NaoATT", false);

                        if(!arquivoATT) {

                            String versaoAtual = obterVersaoAtual();
                            double versaoDBApp = usuarioDAOMYsql.getVersionAPP();
                            String versaoDBAppString = String.valueOf(versaoDBApp);
                            servicosATT = new ServicosATT(activity_splash_screen.this, versaoAtual, versaoDBAppString);
                            boolean attDisponivel = servicosATT.verificaAtt();
                            Log.d("ATT DIS", "ATT"+attDisponivel);

                            if(attDisponivel) {

                                SharedPreferences prefs0 = getSharedPreferences("ArquivoTextoAPP", MODE_PRIVATE);
                                SharedPreferences prefs1 = getSharedPreferences("ArquivoTexto1", MODE_PRIVATE);
                                SharedPreferences prefs2 = getSharedPreferences("ArquivoTexto2", MODE_PRIVATE);
                                SharedPreferences prefs03 = getSharedPreferences("ArquivoTexto3", MODE_PRIVATE);

                                SharedPreferences.Editor editor0 = prefs0.edit();
                                SharedPreferences.Editor editor1 = prefs1.edit();
                                SharedPreferences.Editor editor2 = prefs2.edit();
                                SharedPreferences.Editor editor03 = prefs03.edit();

                                try {

                                    String versaoTextoAPP = usuarioDAOMYsql.getTextoVersaoAPP();
                                    String versaoTexto1 = usuarioDAOMYsql.getTexto1APP();
                                    String versaoTexto2 = usuarioDAOMYsql.getTexto2APP();
                                    String versaoTexto3 = usuarioDAOMYsql.getTexto3APP();

                                    editor0.putString("ATT", versaoTextoAPP);
                                    editor0.commit();
                                    editor1.putString("ATT", versaoTexto1);
                                    editor1.commit();
                                    editor2.putString("ATT", versaoTexto2);
                                    editor2.commit();
                                    editor03.putString("ATT", versaoTexto3);
                                    editor03.commit();


                                } catch (Exception e) {

                                    Log.d("ERRO SQL AUT", "ERRO SQL" + e);

                                    // Retorne valores padrão em caso de erro
                                }

                                SharedPreferences prefs3 = getSharedPreferences("ArquivoAttDisp", MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = prefs3.edit();
                                editor3.putBoolean("Atualizacao", true);
                                editor3.commit();

                            }else {
                                SharedPreferences prefs3 = getSharedPreferences("ArquivoAttDisp", MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = prefs3.edit();
                                editor3.putBoolean("Atualizacao", false);
                                editor3.commit();
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

                                Intent intent = new Intent(activity_splash_screen.this, activity_main.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else {
                                // Falha na autenticação
                                str2 = "ERRO";
                                Toast.makeText(getBaseContext(), "ERRO", Toast.LENGTH_SHORT).show();
                            }
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

                SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(activity_splash_screen.this);

                if (!sharedPreferencesUsuario.getPrimeiroAcesso()) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_welcome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if (!sharedPreferencesUsuario.getSalvarSenha()) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_login.class);
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
        BiometricPrompt biometricPrompt = new BiometricPrompt(activity_splash_screen.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                buttonTenta.setVisibility(View.VISIBLE);
                Toast.makeText(activity_splash_screen.this, "Erro de autenticação " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(activity_splash_screen.this, "Sucesso", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(activity_splash_screen.this, activity_main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                buttonTenta.setVisibility(View.VISIBLE);
                Toast.makeText(activity_splash_screen.this, "Erro", Toast.LENGTH_SHORT).show();
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

        SharedPreferences sharedPreferences = getSharedPreferences("ArquivoTema", MODE_PRIVATE);
        String temaCarregado = sharedPreferences.getString("ArqTemaString", "Automático");

        if (temaCarregado.equals("Escuro")) {
            // Modo escuro ativado
            Glide.with(this)
                    .load(R.raw.tasksavegid) // Substitua gif_escuro pelo nome do seu GIF escuro na pasta res/raw
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

            Log.d("TEMA ESCOLHIDO", "IF 1"+temaCarregado);
        } else if(temaCarregado.equals("Claro")) {
            // Modo escuro não ativado
            Glide.with(this)
                    .load(R.raw.tasksavelight) // Substitua gif_claro pelo nome do seu GIF claro na pasta res/raw
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
            Log.d("TEMA ESCOLHIDO", "IF 2"+temaCarregado);

        }else if (mode == UiModeManager.MODE_NIGHT_YES) {
            // Modo escuro ativado
            Glide.with(this)
                    .load(R.raw.tasksavegid) // Substitua gif_escuro pelo nome do seu GIF escuro na pasta res/raw
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
            Log.d("TEMA ESCOLHIDO", "IF 3"+temaCarregado);
        } else {
            // Modo escuro não ativado
            Glide.with(this)
                    .load(R.raw.tasksavelight) // Substitua gif_claro pelo nome do seu GIF claro na pasta res/raw
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
            Log.d("TEMA ESCOLHIDO", "IF 4"+temaCarregado);
        }
    }

    public void carregaTema() {

        SharedPreferences sharedPreferences = getSharedPreferences("ArquivoTema", MODE_PRIVATE);
        String temaCarregado = sharedPreferences.getString("ArqTemaString", "Automático");

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