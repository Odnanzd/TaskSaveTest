package com.example.tasksave.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.tasksave.dao.UserDAO;
import com.example.tasksave.objetos.User;
import com.example.tasksave.servicesreceiver.AlarmReceiver;
import com.example.tasksave.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class activity_splash_screen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1500;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.splashImageView);

        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        int mode = uiModeManager.getNightMode();
        if (mode == UiModeManager.MODE_NIGHT_YES) {
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

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

// Agendar o BroadcastReceiver para ser chamado a cada minuto (ajuste conforme necessário)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_MAX;

                @SuppressLint("WrongConstant")
                NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
                channel.setDescription(description);

                long[] pattern = {0, 1000, 500, 1000};
                channel.setVibrationPattern(pattern);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                FirebaseUser usuarioatual = FirebaseAuth.getInstance().getCurrentUser();
                SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("ArquivoPrimeiroAcesso", Context.MODE_PRIVATE);
                SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvarSenha", Context.MODE_PRIVATE);
                SharedPreferences sharedPrefs3 = getApplicationContext().getSharedPreferences("ArquivoFingerPrint", Context.MODE_PRIVATE);

                if (!sharedPrefs2.getBoolean("PrimeiroAcesso", false)) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_welcome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if (!sharedPrefs.getBoolean("SalvarSenha", false)) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }
                if(usuarioatual != null) {

                    if (sharedPrefs.getBoolean("SalvarSenha", false) && sharedPrefs3.getBoolean("AcessoFingerPrint", false)) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_fingerprint.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if (sharedPrefs.getBoolean("SalvarSenha", false) && !sharedPrefs3.getBoolean("AcessoFingerPrint", false)) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_main.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }
}