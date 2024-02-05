package com.example.tasksave;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class activity_add_agenda_data extends AppCompatActivity {

    CalendarView calendarView1;
    FloatingActionButton floating_button_add_data;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda_data);

        calendarView1 = findViewById(R.id.calendar_view_data);
        floating_button_add_data = findViewById(R.id.floating_button_add_data);
        calendarView1.setMinDate(System.currentTimeMillis());

        calendarView1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {


                String selectedDate;
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, day);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
                selectedDate = sdf.format(selectedCalendar.getTime());

                String selectedDate2;
                Calendar selectedCalendar2 = Calendar.getInstance();
                selectedCalendar2.set(year, month, day);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate2 = sdf2.format(selectedCalendar.getTime());


                floating_button_add_data.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        Boolean validador = true;
                        SharedPreferences prefs = getSharedPreferences("arquivoSalvar2", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("arquivo_Data", selectedDate);
                        editor.putString("arquivo_Data2", selectedDate2);
                        editor.putBoolean("arquivo_att_data", validador);
                        editor.apply();
                        finish();
                    }
                });
            }
        });



    }
}