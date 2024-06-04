package com.example.tasksave.servicesreceiver;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.tasksave.R;
import com.example.tasksave.activities.activity_agenda;
import com.example.tasksave.activities.activity_main;
import com.example.tasksave.dao.AgendaDAO;
import com.example.tasksave.servicesreceiver.AgendamentoService;

import java.time.LocalDate;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {


    private static final String CHANNEL_ID = "alarm_channel";
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override


    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        // Coloque aqui a lógica para verificar as tarefas e mostrar notificações

//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
////            AgendamentoService.enqueueWork(context, intent);
//            Log.d("AlarmReceiver", "Alarme recebido! Ação executada.");
//
//
//        } else {
////            AgendamentoService.enqueueWork(context, intent);
//            Log.d("AlarmReceiver", "Alarme recebido! Ação executada.");
//        }
//    }

        showNotification(context, title, content);
    }


    @SuppressLint("MissingPermission")
    private void showNotification(Context context, String titulo, String descricao) {

        createNotificationChannel(context);


        Intent intent = new Intent(context, activity_main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.tasksavelogo_notific) // Substitua por um ícone seu
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Cria o canal de notificação apenas no Android 8.0 (API nível 26) e superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Channel";
            String description = "Channel for Alarm";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registra o canal no sistema
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}

