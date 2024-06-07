package com.example.tasksave.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tasksave.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class activity_item_selected_agenda_test extends AppCompatActivity {

    EditText editTextTitulo;
    EditText editTextDesc;
    Switch aswitch;
    TextView textViewData;
    TextView textViewHora;
    TextView textViewRepetir;

    LinearLayout linearLayoutData;
    LinearLayout linearLayoutHora;
    LinearLayout linearLayoutRepetir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected_agenda_test);


        editTextTitulo = findViewById(R.id.titulo_text_view);
        editTextDesc = findViewById(R.id.descricao_text_view);
        aswitch = findViewById(R.id.switch1);
        textViewData = findViewById(R.id.textView5);
        textViewHora = findViewById(R.id.textView6);
        textViewRepetir = findViewById(R.id.textView12);
        linearLayoutData = findViewById(R.id.linearLayout5);
        linearLayoutHora = findViewById(R.id.linearLayout7);
        linearLayoutRepetir = findViewById(R.id.linearLayout9);


        Intent intent = getIntent();
        long idTarefa = intent.getLongExtra("idTarefa", 0);
        String tituloTarefa = intent.getStringExtra("tituloIntent");
        String descTarefa = intent.getStringExtra("descIntent");
        String dataTarefa = intent.getStringExtra("dataIntent");
        String horaTarefa = intent.getStringExtra("horaIntent");
        boolean lembreteTarefa = intent.getBooleanExtra("lembreteIntent", false);
        boolean repetirLembrete = intent.getBooleanExtra("repetirLembreteIntent", false);
        int repetirModoLembrete = intent.getIntExtra("repetirModoIntent", 0);

         @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
         @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate localdataEscolhida = LocalDate.parse(dataTarefa);
         @SuppressLint({"NewApi", "LocalSuppress"}) String dataFormatada = localdataEscolhida.format(formatter);


        editTextTitulo.setText(tituloTarefa);
        editTextDesc.setText(descTarefa);

        if(lembreteTarefa) {

            textViewData.setText(dataFormatada);
            textViewHora.setText(horaTarefa);
            aswitch.setChecked(true);
        }else {
            textViewData.setText("");
            textViewHora.setText("");
        }

        if (repetirLembrete) {

        switch (repetirModoLembrete) {
            case 1:
                textViewRepetir.setText("Todo dia");
                break;
            case 2:
                textViewRepetir.setText("Toda Semana");
                break;
            case 3:
                textViewRepetir.setText("Todo Mês");
                break;
            case 4:
                textViewRepetir.setText("Todo ano");
        }
    }

 linearLayoutData.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         if(repetirLembrete) {

             @SuppressLint({"NewApi", "LocalSuppress"})
             Date date = Date.from(localdataEscolhida.atStartOfDay(ZoneId.systemDefault()).toInstant());
             Calendar calendarDate = Calendar.getInstance();
             calendarDate.setTime(date);
             dialogDateUser(calendarDate);

         }else {
             dialogDate();
         }
     }
 });


    }
    public void dialogDate() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(activity_item_selected_agenda_test.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String selectedDate;
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                selectedDate = sdf.format(selectedCalendar.getTime());

                String selectedDate2;
                Calendar selectedCalendar2 = Calendar.getInstance();
                selectedCalendar2.set(year, month, dayOfMonth);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate2 = sdf2.format(selectedCalendar2.getTime());

                SharedPreferences prefs = getSharedPreferences("arquivoSalvarDataEdit", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("arquivo_Data_Edit", selectedDate2);
                editor.apply();

                textViewData.setText(selectedDate);

                Log.d("Verificação Data", "Data:" +selectedDate2);
            }
        }, year, month, dayOfMonth);
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

    public void dialogDateUser(Calendar calendar) {

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(activity_item_selected_agenda_test.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String selectedDate;
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                selectedDate = sdf.format(selectedCalendar.getTime());

                String selectedDate2;
                Calendar selectedCalendar2 = Calendar.getInstance();
                selectedCalendar2.set(year, month, dayOfMonth);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate2 = sdf2.format(selectedCalendar2.getTime());

                SharedPreferences prefs = getSharedPreferences("arquivoSalvarDataEdit", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("arquivo_Data_Edit", selectedDate2);
                editor.apply();

                textViewData.setText(selectedDate);

                Log.d("Verificação Data", "Data:" +selectedDate2);
            }
        }, year, month, dayOfMonth);
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }
}