package com.example.tasksave;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class activity_fingerprint extends activity_login {


    Button button_fing2;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        button_fing2 = findViewById(R.id.button_fing2);
        imageView = findViewById(R.id.image_view_button_fing);

        ChecarBiometria();
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(activity_fingerprint.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(activity_fingerprint.this, "Erro de autenticação " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(activity_fingerprint.this, "Sucesso", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvar", Context.MODE_PRIVATE);
                if (!sharedPrefs.getBoolean("primeiroAcesso", false)) {
                    Intent intent = new Intent(activity_fingerprint.this, activity_login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(activity_fingerprint.this, activity_main.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(activity_fingerprint.this, "Erro", Toast.LENGTH_SHORT).show();
            }
        });
        BiometricPrompt.PromptInfo.Builder promptInfo = CaixaDialogo();
        promptInfo.setNegativeButtonText("Cancelar");
        biometricPrompt.authenticate(promptInfo.build());

        imageView.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promtInfo = CaixaDialogo();
            promtInfo.setNegativeButtonText("Cancelar");
            biometricPrompt.authenticate(promtInfo.build());
        });
        button_fing2.setOnClickListener(view -> {
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

    private void ChecarBiometria() {
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
        TextView txtinfo = findViewById(R.id.txt_fing);
        txtinfo.setText(info);
    }

    void enableButton(boolean enable) {
        imageView.setEnabled(enable);
        button_fing2.setEnabled(true);
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

        }

