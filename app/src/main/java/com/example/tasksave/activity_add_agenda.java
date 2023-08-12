package com.example.tasksave;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
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
                SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                SharedPreferences.Editor saveEdit = save.edit();
                saveEdit.clear();
                saveEdit.commit();
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
                    if (switchCompat.isChecked()) {

                      SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                      String dataEscolhida = sharedPrefs.getString("arquivo_Data2", "");
                      LocalDate localdataEscolhida = LocalDate.parse(dataEscolhida, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                      SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvar3", Context.MODE_PRIVATE);
                      int horaEscolhida = sharedPrefs2.getInt("arquivo_Hora2",-1);
                      int minutoEscolhido = sharedPrefs2.getInt("arquivo_Minuto2",-1);

                        Agenda agenda = new Agenda(editNome.getText().toString(), editDescricao.getText().toString(),
                        localdataEscolhida, horaEscolhida, minutoEscolhido, true);
                        long id = agendaDAO.inserir(agenda);
                        Toast.makeText(activity_add_agenda.this, "Tarefa Salva.", Toast.LENGTH_LONG).show();

                        SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor saveEdit = save.edit();
                        saveEdit.clear();
                        saveEdit.commit();
                        finish();

                    } else {

                        Agenda agenda = new Agenda(editNome.getText().toString(), editDescricao.getText().toString(),
                                dataAtual, -1, -1, false);
                        long id = agendaDAO.inserir(agenda);
                        Toast.makeText(activity_add_agenda.this, "Tarefa Salva.", Toast.LENGTH_LONG).show();
                        SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor saveEdit = save.edit();
                        saveEdit.clear();
                        saveEdit.commit();
                        finish();
                    }
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        AtualizarData();
        AtualizarHora();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void AtualizarData() {

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
        String dataEscolhida = sharedPrefs.getString("arquivo_Data", "");

        if (!dataEscolhida.isEmpty()) {
            TextView textView = findViewById(R.id.textView5);
            textView.setText(dataEscolhida);
        }
    }
    public void AtualizarHora() {
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvar3", Context.MODE_PRIVATE);
        String horaEscolhido = sharedPrefs.getString("arquivo_Hora", "");
        String minutoEscolhido = sharedPrefs.getString("arquivo_Minuto", "");

        if (!horaEscolhido.isEmpty() | !minutoEscolhido.isEmpty()) {
            TextView textView2 = findViewById(R.id.textView4);
            textView2.setText(horaEscolhido + ":" +minutoEscolhido);
        }
    }

    }

