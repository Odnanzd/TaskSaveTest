package com.example.tasksave.test.servicesreceiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlarmScheduler {
    @SuppressLint({"ScheduleExactAlarm", "NewApi"})
    public static void scheduleAlarm(Context context, long triggerAtMillis, String title, String content, int repeatMode, long id, LocalDateTime localDateTime) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("repeatMode", repeatMode);
        intent.putExtra("idLong", id);

        intent.putExtra("dataIntent", localDateTime.toString());
//        Log.d("TESTE STRING DATA", "DATA: "+localDate);

        int idInt = (int) id;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                idInt,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        SharedPreferences sharedPrefs3 = context.getSharedPreferences("ArquivoNotifica", Context.MODE_PRIVATE);
        boolean notifica = sharedPrefs3.getBoolean("NaoNotifica", false);


        if (alarmManager != null && !notifica) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            Log.d("AlarmScheduler", "Intent" + triggerAtMillis);
        }
    }
    @SuppressLint("NewApi")
    public static void cancelAlarm(Context context, long id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int idInt = (int) id;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                idInt,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d("AlarmScheduler", "Alarme cancelado para o ID: " + id);
        }else {
            Log.d("AlarmScheduler", "Alarme não encontrado para o ID: "+id);
        }
    }
}