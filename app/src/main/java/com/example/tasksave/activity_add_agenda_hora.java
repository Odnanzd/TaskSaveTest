package com.example.tasksave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class activity_add_agenda_hora extends AppCompatActivity {

    TimePicker timePicker;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda_hora);
        timePicker = findViewById(R.id.timePicker);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                Date time = new Date();
                time.setHours(hourOfDay);
                time.setMinutes(minute);
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

                String hora_formatada = hourFormat.format(time);
                String minuto_Formatado = minuteFormat.format(time);

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences prefs = getSharedPreferences("arquivoSalvar3", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("arquivo_Hora", hora_formatada);
                        editor.putString("arquivo_Minuto", minuto_Formatado);
                        editor.apply();
                        finish();
                    }
                });
            }
        });

    }
}