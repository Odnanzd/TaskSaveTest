package com.example.tasksave.test.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tasksave.R;

public class ActivityConfgSecPermissao extends AppCompatActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityConfgSecPermissao.this, ActivitySecPriv.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private ImageView imageViewMidia, imageViewTotal, imageViewBack;
    private LinearLayout linearLayoutMidia, linearLayoutTotal, linearLayoutTotal2;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int MANAGE_STORAGE_PERMISSION_REQUEST_CODE = 2;
    private static final String TAG = "AtualizacaoApp";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_permissao);

        imageViewMidia = findViewById(R.id.imageViewCheck);
        imageViewTotal = findViewById(R.id.imageViewSeta2);
        imageViewBack = findViewById(R.id.imageView4);

        linearLayoutMidia = findViewById(R.id.linearLayout2);
        linearLayoutTotal = findViewById(R.id.linearLayoutSeg);
        linearLayoutTotal2 = findViewById(R.id.linearLayout4);

        verificarPermissaoLerEscrever();
        verificarPermissaoAcessoTotal();

        linearLayoutMidia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solicitarPermissaoLerEscrever();
            }
        });

        linearLayoutTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, MANAGE_STORAGE_PERMISSION_REQUEST_CODE);
                }
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityConfgSecPermissao.this, ActivitySecPriv.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }

    private void solicitarPermissaoLerEscrever() {
        // Check if permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Permissão já concedida.", Toast.LENGTH_SHORT).show();
        }
    }

    public void verificarPermissaoAcessoTotal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                imageViewTotal.setImageResource(R.drawable.icon_check_perm2);
            } else {
                imageViewTotal.setImageResource(R.drawable.icon_negative_2);
            }
        } else {

            linearLayoutTotal2.setVisibility(View.GONE);

        }
    }

    private void verificarPermissaoLerEscrever() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            imageViewMidia.setImageResource(R.drawable.icon_check_perm2);
        } else {
            imageViewMidia.setImageResource(R.drawable.icon_negative_2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                verificarPermissaoLerEscrever();
                Toast.makeText(this, "Permissão concedida.", Toast.LENGTH_SHORT).show();
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
                    verificarPermissaoAcessoTotal();
                    Toast.makeText(this, "Acesso total ao armazenamento concedido.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permissão de acesso total ao armazenamento não concedida.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}