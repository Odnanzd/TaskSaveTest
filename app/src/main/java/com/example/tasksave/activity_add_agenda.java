package com.example.tasksave;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class activity_add_agenda extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);


        EditText editNome = findViewById(R.id.editTextText);
        EditText editDescricao = findViewById(R.id.editTextText2);
        Button buttonSalvar = findViewById(R.id.button_login);

        CalendarView calendarView = findViewById(R.id.calendarView);
        TimePicker timePicker = findViewById(R.id.timePicker);

        TextView textView = findViewById(R.id.textView5);
        TextView textView1 = findViewById(R.id.textView4);

        FloatingActionButton floatingActionButton1 = findViewById(R.id.floatingActionButton2);

        Switch switchCompat = findViewById(R.id.switch1);
        switchCompat.setChecked(false);
        textView.setVisibility(View.GONE);
        textView1.setVisibility(View.GONE);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                }
            }
        });

        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        String dataFormatada = dataAtual.format(formatter);
        textView.setText(dataFormatada);

        LocalTime horaAtual = LocalTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        String horaFormatada = horaAtual.format(formatter1);
        textView1.setText(horaFormatada);


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_add_agenda.this, activity_add_agenda_data.class);
                startActivity(intent);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_add_agenda.this, activity_add_agenda_hora.class);
                startActivity(intent);
            }
        });

        AgendaDAO agendaDAO = new AgendaDAO(this);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editNome.getText().toString().equals("") || editDescricao.getText().toString().equals("")) {

                    Toast.makeText(activity_add_agenda.this, "Os campos não podem ser vazios.", Toast.LENGTH_LONG).show();

                } else {
                    if (switchCompat.isChecked()){

                        long selectedDateMillis = calendarView.getDate();
                        LocalDate selectedDate = Instant.ofEpochMilli(selectedDateMillis).atZone(ZoneId.systemDefault()).toLocalDate();
                        int selectedHour = timePicker.getHour();
                        int selectedMinute = timePicker.getMinute();


                        Agenda agenda = new Agenda(editNome.getText().toString(), editDescricao.getText().toString(),
                        selectedDate, selectedHour, selectedMinute);
                        long id = agendaDAO.inserir(agenda);
                        Toast.makeText(activity_add_agenda.this, "Tarefa Salva.", Toast.LENGTH_LONG).show();
                        finish();

                    } else {

                        Agenda agenda = new Agenda(editNome.getText().toString(), editDescricao.getText().toString(),
                                null, -1, -1);
                        long id = agendaDAO.inserir(agenda);
                        Toast.makeText(activity_add_agenda.this, "Tarefa Salva.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    }
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();



                            }
                    });







    }
    }