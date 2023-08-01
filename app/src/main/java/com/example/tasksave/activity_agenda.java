package com.example.tasksave;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class activity_agenda extends AppCompatActivity {

    private SQLiteDatabase database;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        floatingActionButton = findViewById(R.id.button_mais_agenda);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void showCustomDialog() {


        final Dialog dialog = new Dialog(activity_agenda.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_add_agenda);

        final EditText editNome = dialog.findViewById(R.id.editTextText);
        final EditText editDescricao = dialog.findViewById(R.id.editTextText2);
        final Button buttonSalvar = dialog.findViewById(R.id.button_login);

        final CalendarView calendarView = dialog.findViewById(R.id.calendarView);
        final TimePicker timePicker = dialog.findViewById(R.id.timePicker);

        final TextView textView = dialog.findViewById(R.id.textView5);
        final TextView textView1 = dialog.findViewById(R.id.textView4);

        final FloatingActionButton floatingActionButton1 = dialog.findViewById(R.id.floatingActionButton2);

        SwitchCompat switchCompat = dialog.findViewById(R.id.switch1);
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
                dialog.dismiss();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_agenda.this, activity_add_agenda_data.class);
                startActivity(intent);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_agenda.this, activity_add_agenda_hora.class);
                startActivity(intent);
            }
        });

        AgendaDAO agendaDAO = new AgendaDAO(this);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editNome.getText().toString().equals("") || editDescricao.getText().toString().equals("")) {

                    Toast.makeText(activity_agenda.this, "Os campos não podem ser vazios.", Toast.LENGTH_LONG).show();

                } else {
                    if (switchCompat.isChecked()){

                        long selectedDateMillis = calendarView.getDate();
                        LocalDate selectedDate = Instant.ofEpochMilli(selectedDateMillis).atZone(ZoneId.systemDefault()).toLocalDate();
                        int selectedHour = timePicker.getHour();
                        int selectedMinute = timePicker.getMinute();


                        Agenda agenda = new Agenda(editNome.getText().toString(), editDescricao.getText().toString(),
                        selectedDate, selectedHour, selectedMinute);
                        long id = agendaDAO.inserir(agenda);
                        Toast.makeText(activity_agenda.this, "Tarefa Salva.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    } else {

                        Agenda agenda = new Agenda(editNome.getText().toString(), editDescricao.getText().toString(),
                                null, -1, -1);
                        long id = agendaDAO.inserir(agenda);
                        Toast.makeText(activity_agenda.this, "Tarefa Salva.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    }
                            }
                    });

        dialog.show();


    }
}


    // Método para abrir ou criar o banco de dados
//    private void abrirOuCriarDB() {
//        // Abra ou crie o banco de dados com o nome "database"
//        try {
//            database = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
//
//            // Crie a tabela "agenda" com a coluna "titulo"
//            database.execSQL("CREATE TABLE IF NOT EXISTS agenda (titulo TEXT);");
//            database.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//}

        // Método para inserir dados na tabela "agenda"
//        private void inserirDados () {
//
//            try {
//                database = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
//
//                // Crie um ContentValues para armazenar os valores a serem inseridos na tabela
//                ContentValues values = new ContentValues();
//                values.put("titulo", "Atividade Fernando");
//
//                // Insira os dados na tabela "agenda"
//                database.insert("agenda", null, values);
//                database.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Método para listar dados na ListView
//        private void listarDados () {
//
//            try {
//
//                database = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
//
//                // Consulte todos os dados da tabela "agenda"
//                Cursor cursor = database.rawQuery("SELECT titulo FROM agenda", null);
//
//                // Crie uma lista para armazenar os títulos
//                ArrayList<String> titulos = new ArrayList<>();
//
//                // Verifique se o cursor não é nulo e mova-o para a primeira posição
//                if (cursor != null && cursor.moveToFirst()) {
//                    do {
//                        // Obtenha o título da coluna "titulo" e adicione-o à lista
//                        @SuppressLint("Range")
//                        String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
//                        titulos.add(titulo);
//                    } while (cursor.moveToNext());
//                }
//
//                // Feche o cursor após o uso
//                if (cursor != null) {
//                    cursor.close();
//                }
//                // Configure o adaptador da ListView para exibir os títulos
//                ListView listView = findViewById(R.id.list_view_agenda);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
//                listView.setAdapter(adapter);
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }