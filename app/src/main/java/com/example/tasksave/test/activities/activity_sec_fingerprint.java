package com.example.tasksave.test.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import com.example.tasksave.R;

public class activity_sec_fingerprint extends AppCompatActivity {

    private ImageView imageViewBack;
    private LinearLayout linearLayoutFingerprint;
    private Switch aSwitch;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity_sec_fingerprint.this, activity_sec_priv.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_fingerprint);

        imageViewBack = findViewById(R.id.imageView4);
        aSwitch = findViewById(R.id.switch1);
        linearLayoutFingerprint = findViewById(R.id.linearLayout2);


        fingerprintSwitch();
        boolean validador = checarBiometria();

        if (!validador) {
            linearLayoutFingerprint.setEnabled(false);
            Toast.makeText(getBaseContext(), "Não foi encontrado hardware disponível ou impressão cadadastrada no aparelho.", Toast.LENGTH_LONG).show();
        }else {
            linearLayoutFingerprint.setEnabled(true);
        }
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_sec_fingerprint.this, activity_sec_priv.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        linearLayoutFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aSwitch.toggle();
                if (aSwitch.isChecked()) {
                    attFingerprintPositivo();
                }else {
                    attFingerprintNegativo();
                }
            }
        });
    }
    public void fingerprintSwitch() {

        SharedPreferences sharedPrefs3 = getApplicationContext().getSharedPreferences("ArquivoFingerPrint", Context.MODE_PRIVATE);
        boolean fingerprint = sharedPrefs3.getBoolean("AcessoFingerPrint", false);

        if (fingerprint) {
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }
    }
    public void attFingerprintPositivo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity_sec_fingerprint.this);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setMessage("Deseja confirmar as alterações e sair? ");
        builder.setNegativeButton("Não", (dialog, which) -> {
            fingerprintSwitch();
        });
        builder.setPositiveButton("Sim", (dialog, which) -> {
            // Ação para o botão OK
            SharedPreferences prefs = getSharedPreferences("ArquivoFingerPrint", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("AcessoFingerPrint", true);
            editor.commit();
            finish();
            Toast.makeText(getBaseContext(), "Biometria ativada", Toast.LENGTH_SHORT).show();

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void attFingerprintNegativo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity_sec_fingerprint.this);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setMessage("Deseja confirmar as alterações e sair? ");
        builder.setNegativeButton("Não", (dialog, which) -> {
            fingerprintSwitch();

        });
        builder.setPositiveButton("Sim", (dialog, which) -> {
            // Ação para o botão OK
            SharedPreferences prefs = getSharedPreferences("ArquivoFingerPrint", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("AcessoFingerPrint", false);
            editor.commit();
            finish();
            Toast.makeText(getBaseContext(), "Biometria desativada", Toast.LENGTH_SHORT).show();

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private boolean checarBiometria() {

        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK |
                BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                return false;

        }
        return false;
    }
}