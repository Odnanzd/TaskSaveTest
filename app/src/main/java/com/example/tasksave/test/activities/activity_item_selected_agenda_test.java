package com.example.tasksave.test.activities;

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
import android.widget.CompoundButton;
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
    String tituloTarefa;
    String descTarefa;
    String dataTarefa;
    String horaTarefa;
    boolean lembreteTarefa;
    private Calendar selectedDate;
    private Calendar selectedHour;

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
        tituloTarefa = intent.getStringExtra("tituloIntent");
        descTarefa = intent.getStringExtra("descIntent");
        dataTarefa = intent.getStringExtra("dataIntent");
        horaTarefa = intent.getStringExtra("horaIntent");
        lembreteTarefa = intent.getBooleanExtra("lembreteIntent", false);
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

        editTextTitulo.addTextChangedListener(textWatcher);
        editTextDesc.addTextChangedListener(textWatcher);
        aswitch.setOnCheckedChangeListener(switchListener);

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
        selectedDate = (Calendar) selectedDateCalendar.clone();
        selectedHour = (Calendar) selectedHourCalendar.clone();


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

    }

    private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkForChanges();
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkForChanges();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void checkForChanges() {
        boolean text1Changed = !editTextTitulo.getText().toString().equals(tituloTarefa);
        boolean text2Changed = !editTextDesc.getText().toString().equals(descTarefa);
        boolean switchChanged = aswitch.isChecked() != lembreteTarefa;
        boolean dateChanged = !isSameDay(selectedDate, selectedDateCalendar);
        boolean hourChanged = !horaIgual(selectedHour, selectedHourCalendar);


        if (text1Changed || text2Changed || switchChanged || dateChanged|| hourChanged) {

            imageViewCheck.setVisibility(View.VISIBLE);

        } else {

            imageViewCheck.setVisibility(View.GONE);

        }
    }
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
    private boolean horaIgual(Calendar hora1, Calendar hora2) {

        return hora1.get(Calendar.HOUR_OF_DAY) == hora2.get(Calendar.HOUR_OF_DAY) &&
                hora1.get(Calendar.MINUTE) == hora2.get(Calendar.MINUTE);

    }


    public void dialogDateUser(Calendar calendar) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(activity_item_selected_agenda_test.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                String selectedDate2 = sdf.format(selectedDate.getTime());

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate3 = sdf2.format(selectedDate.getTime());

                SharedPreferences prefs = getSharedPreferences("arquivoSalvarDataEdit", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("arquivo_Data_Edit", selectedDate3);
                editor.apply();
                checkForChanges();

                textViewData.setText(selectedDate2);
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

                selectedHour.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedHour.set(Calendar.MINUTE, minute);
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

                checkForChanges();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

            }
        }, hour, minute, true);

        // Mostrar o diálogo
        timePickerDialog.show();

    }

}