package com.example.tasksave.servicesreceiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.time.LocalDate;
import java.util.Calendar;

public class AlarmScheduler {
    @SuppressLint({"ScheduleExactAlarm", "NewApi"})
    public static void scheduleAlarm(Context context, Calendar calendar, String title, String content, int repeatMode, long id, LocalDate localDate) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("repeatMode", repeatMode);
        intent.putExtra("idLong", id);

        intent.putExtra("dataIntent", localDate.toString());
        Log.d("TESTE STRING DATA", "DATA: "+localDate);

        int idInt = (int) id;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                idInt,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}