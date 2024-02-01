package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    public void onReceive(Context context, Intent intent) {
        // Coloque aqui a lógica para verificar as tarefas e mostrar notificações

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AgendamentoService.enqueueWork(context, intent);

        } else {
            AgendamentoService.enqueueWork(context, intent);
        }
    }
}

