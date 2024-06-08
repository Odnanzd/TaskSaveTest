package com.example.tasksave.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

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
    Calendar selectedDateCalendar;
    Calendar selectedHourCalendar;
    ImageView imageViewBack;
    ImageView imageViewCheck;
    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        finish();
    }

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
        imageViewBack = findViewById(R.id.imageView4);
        imageViewCheck = findViewById(R.id.imageViewCheck);


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
        selectedDateCalendar = Calendar.getInstance();
        selectedHourCalendar = Calendar.getInstance();

        if(lembreteTarefa) {
            @SuppressLint({"NewApi", "LocalSuppress"})
            Date date = Date.from(localdataEscolhida.atStartOfDay(ZoneId.systemDefault()).toInstant());
            selectedDateCalendar.setTime(date);

            String[] timeParts = horaTarefa.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            selectedHourCalendar.set(Calendar.HOUR_OF_DAY, hour);
            selectedHourCalendar.set(Calendar.MINUTE, minute);


        }


 linearLayoutData.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {

        dialogDateUser(selectedDateCalendar);

     }
 });

 linearLayoutHora.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {

         dialogHourUser(selectedHourCalendar);

     }
 });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        boolean estadoOriginal = aswitch.isChecked();
        aswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Torne o botão visível quando o Switch for alterado

            if(estadoOriginal!=aswitch.isChecked()) {

                imageViewCheck.setVisibility(View.VISIBLE);

            }else {
                imageViewCheck.setVisibility(View.GONE);
            }

        });
                editTextTitulo.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // Não é necessário implementar nada aqui
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // Verifica se o EditText não está vazio
                                String novoTitulo = s.toString();
                                boolean saoIguais = novoTitulo.equals(tituloTarefa);

                                if (s.length() > 0 && !saoIguais) {
                                    imageViewCheck.setVisibility(View.VISIBLE);
                                } else {
                                    imageViewCheck.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // Não é necessário implementar nada aqui
                            }
                        });
                    editTextDesc.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // Não é necessário implementar nada aqui
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // Verifica se o EditText não está vazio
                                String novoDes = s.toString();
                                boolean saoIguais = novoDes.equals(descTarefa);

                                if (s.length() > 0 && !saoIguais) {
                                    imageViewCheck.setVisibility(View.VISIBLE);
                                } else {
                                    imageViewCheck.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                                // Não é necessário implementar nada aqui
                            }
                        });


    }


    public void dialogDateUser(Calendar calendar) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(activity_item_selected_agenda_test.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                if (calendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH) &&
                        calendar.get(Calendar.DAY_OF_MONTH) == selectedCalendar.get(Calendar.DAY_OF_MONTH)) {

                    imageViewCheck.setVisibility(View.GONE);

                } else {
                    imageViewCheck.setVisibility(View.VISIBLE);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                String selectedDate = sdf.format(selectedCalendar.getTime());

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate2 = sdf2.format(selectedCalendar.getTime());

                SharedPreferences prefs = getSharedPreferences("arquivoSalvarDataEdit", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("arquivo_Data_Edit", selectedDate2);
                editor.apply();

                textViewData.setText(selectedDate);
                Log.d("DATA", "DATA: "+selectedDate2);


            }
        }, year, month, dayOfMonth);

        dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());  // Definir a data mínima para a data atual
        dialog.show();
    }

    public void dialogHourUser(Calendar calendar) {

        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = calendar.get(java.util.Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(activity_item_selected_agenda_test.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Date time = new Date();
                time.setHours(hourOfDay);
                time.setMinutes(minute);
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

                String hora_formatada = hourFormat.format(time);
                String minuto_Formatado = minuteFormat.format(time);

                // Lidar com a hora selecionada pelo usuário
                textViewHora.setText(hora_formatada+":"+minuto_Formatado);

                int horadefinida = hourOfDay;
                int minutodefinido = minute;

                SharedPreferences prefs = getSharedPreferences("arquivoSalvar3", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("arquivo_Hora", horadefinida);
                editor.putInt("arquivo_Minuto", minutodefinido);
                editor.apply();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                imageViewCheck.setVisibility(View.VISIBLE);

            }
        }, hour, minute, true);

        // Mostrar o diálogo
        timePickerDialog.show();

    }

}