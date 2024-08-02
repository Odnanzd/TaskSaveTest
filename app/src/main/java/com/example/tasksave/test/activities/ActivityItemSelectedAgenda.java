package com.example.tasksave.test.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tasksave.R;
import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.servicesreceiver.AlarmScheduler;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ActivityItemSelectedAgenda extends AppCompatActivity {

    EditText editTextTitulo;
    EditText editTextDesc;
    Switch aswitch;
    TextView textViewData;
    TextView textViewHora;
    TextView textViewRepetir;

    LinearLayout linearLayoutData;
    LinearLayout linearLayoutHora;
    LinearLayout linearLayoutRepetir;
    LinearLayout linearLayoutDefinirLembrete;
    Calendar selectedDateCalendar;
    Calendar selectedHourCalendar;
    ImageView imageViewBack;
    ImageView imageViewCheck;
    String tituloTarefa;
    String descTarefa;
    String dataTarefa;
    String horaTarefa;
    String modoSelecionado;
    String modoSelecionadoOriginal;
    boolean lembreteTarefa;
    ImageView imageViewSalvar;
    long idTarefa;
    int repetirModoLembrete;
    Date date;
    int hourTarefa;
    int minuteTarefa;
    LocalDate localdataEscolhida;
    boolean repetirLembrete;
    String dataFormatada;
    int repetirModoLembreteSelecionado;
    private Calendar selectedDate;
    private Calendar selectedHour;
    private boolean isDatePickerShown = false;
    private boolean isHourPickerShown = false;


    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {

        if(checkForChanges()) {
            cancelarAtt();
        }else {
            Intent intent = new Intent(ActivityItemSelectedAgenda.this, ActivityAgenda.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected_agenda);


        editTextTitulo = findViewById(R.id.titulo_text_view);
        editTextDesc = findViewById(R.id.descricao_text_view);
        aswitch = findViewById(R.id.switch1);
        textViewData = findViewById(R.id.textView5);
        textViewHora = findViewById(R.id.textView6);
        textViewRepetir = findViewById(R.id.textView12);
        linearLayoutData = findViewById(R.id.linearLayout4);
        linearLayoutHora = findViewById(R.id.linearLayout6);
        linearLayoutRepetir = findViewById(R.id.linearLayout8);
        imageViewBack = findViewById(R.id.imageView4);
        imageViewCheck = findViewById(R.id.imageViewCheck);
        linearLayoutDefinirLembrete = findViewById(R.id.linearLayout3);
        imageViewSalvar  = findViewById(R.id.imageViewCheck);



        Intent intent = getIntent();
        idTarefa = intent.getLongExtra("idTarefa", 0);
        tituloTarefa = intent.getStringExtra("tituloIntent");
        descTarefa = intent.getStringExtra("descIntent");
        dataTarefa = intent.getStringExtra("dataIntent");
        horaTarefa = intent.getStringExtra("horaIntent");
        lembreteTarefa = intent.getBooleanExtra("lembreteIntent", false);
        repetirLembrete = intent.getBooleanExtra("repetirLembreteIntent", false);
        repetirModoLembrete = intent.getIntExtra("repetirModoIntent", -1);

        if(lembreteTarefa) {
            @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            localdataEscolhida = LocalDate.parse(dataTarefa);
            dataFormatada = localdataEscolhida.format(formatter);
        }

        editTextTitulo.setText(tituloTarefa);
        editTextDesc.setText(descTarefa);

        if (lembreteTarefa) {

            textViewData.setText(dataFormatada);
            textViewHora.setText(horaTarefa);
            aswitch.setChecked(true);

        } else {

            textViewData.setVisibility(View.GONE);
            textViewHora.setVisibility(View.GONE);

        }

        editTextTitulo.addTextChangedListener(textWatcher);
        editTextDesc.addTextChangedListener(textWatcher);
        aswitch.setOnCheckedChangeListener(switchListener);

        if (repetirLembrete) {

            switch (repetirModoLembrete) {

                case 1:
                    textViewRepetir.setText("Todo dia");
                    modoSelecionado="Todo dia";
                    modoSelecionadoOriginal="Todo dia";
                    break;
                case 2:
                    textViewRepetir.setText("Toda semana");
                    modoSelecionado="Toda semana";
                    modoSelecionadoOriginal="Toda semana";
                    break;
                case 3:
                    textViewRepetir.setText("Todo mês");
                    modoSelecionado="Todo mês";
                    modoSelecionadoOriginal="Todo mês";
                    break;
                case 4:
                    textViewRepetir.setText("Todo ano");
                    modoSelecionado="Todo ano";
                    modoSelecionadoOriginal="Todo ano";
                    break;
            }
        }else {
            modoSelecionado="Não repetir";
            modoSelecionadoOriginal="Não repetir";
        }

        selectedDateCalendar = Calendar.getInstance();
        selectedHourCalendar = Calendar.getInstance();

        if (lembreteTarefa) {

            date = Date.from(localdataEscolhida.atStartOfDay(ZoneId.systemDefault()).toInstant());
            selectedDateCalendar.setTime(date);

            String[] timeParts = horaTarefa.split(":");
            hourTarefa = Integer.parseInt(timeParts[0]);
            minuteTarefa = Integer.parseInt(timeParts[1]);
            selectedHourCalendar.set(Calendar.HOUR_OF_DAY, hourTarefa);
            selectedHourCalendar.set(Calendar.MINUTE, minuteTarefa);


        }
        selectedDate = (Calendar) selectedDateCalendar.clone();
        selectedHour = (Calendar) selectedHourCalendar.clone();


        linearLayoutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                linearLayoutData.setClickable(false);

                if (aswitch.isChecked()) {

                    dialogDateUser(selectedDateCalendar);
                } else {

                }
            }
        });

        linearLayoutHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                linearLayoutHora.setClickable(false);

                if (aswitch.isChecked()) {
                    dialogHourUser(selectedHourCalendar);
                } else {

                }

            }
        });
        linearLayoutDefinirLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aswitch.toggle();
            }
        });


        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForChanges()) {
                    cancelarAtt();
                }else {
                    Intent intent = new Intent(ActivityItemSelectedAgenda.this, ActivityAgenda.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {

                    textViewHora.setVisibility(View.VISIBLE);
                    textViewData.setVisibility(View.VISIBLE);
                    checkForChanges();

                } else {

                    textViewHora.setVisibility(View.GONE);
                    textViewData.setVisibility(View.GONE);
                    checkForChanges();
                }
            }
        });

        linearLayoutRepetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(aswitch.isChecked()) {

                  linearLayoutRepetir.setClickable(false);

                  dialogRepeater(modoSelecionado);

              }else {

              }


            }
        });
        imageViewSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attDados(view);
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkForChanges();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private boolean checkForChanges() {

        boolean text1Changed = !editTextTitulo.getText().toString().equals(tituloTarefa);
        boolean text2Changed = !editTextDesc.getText().toString().equals(descTarefa);
        boolean switchChanged = aswitch.isChecked() != lembreteTarefa;
        boolean dateChanged = !isSameDay(selectedDate, selectedDateCalendar);
        boolean hourChanged = !horaIgual(selectedHour, selectedHourCalendar);
        boolean repeaterChanged = !repeaterIgual(modoSelecionadoOriginal);


        if(aswitch.isChecked()) {

            if ((text1Changed || text2Changed || switchChanged || dateChanged || hourChanged || repeaterChanged)
                    && localdataEscolhida != null && hourTarefa != 0 &&
                    !editTextTitulo.getText().toString().equals("") && !editTextDesc.getText().toString().equals("")) {


                imageViewCheck.setVisibility(View.VISIBLE);
                return true;

            } else {

                imageViewCheck.setVisibility(View.GONE);
                return false;


            }
        }else {

            if ((text1Changed || text2Changed || switchChanged ) &&
                    !editTextTitulo.getText().toString().equals("") && !editTextDesc.getText().toString().equals("")) {

                imageViewCheck.setVisibility(View.VISIBLE);
                return true;

            } else {

                imageViewCheck.setVisibility(View.GONE);
                return false;


            }
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
    private boolean repeaterIgual(String repeaterOriginal) {

       return textViewRepetir.getText().toString().equals(repeaterOriginal);

    }


    public void dialogDateUser(Calendar calendar) {

        if (isDatePickerShown) {
            return;
        }
        isDatePickerShown = true;

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(ActivityItemSelectedAgenda.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(year, month, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                String selectedDate2 = sdf.format(calendar.getTime());

                localdataEscolhida = convertCalendarToLocalDate(calendar);
                Log.d("Teste calendar","LOCALDATE: "+localdataEscolhida);

                textViewData.setText(selectedDate2);

                isDatePickerShown = false;
                checkForChanges();


            }
        }, year, month, dayOfMonth);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    linearLayoutData.setClickable(true);
                    isDatePickerShown = false;
              }
             });
                dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());  // Definir a data mínima para a data atual
        dialog.show();
    }

    public void dialogHourUser(Calendar calendar) {

        if (isHourPickerShown) {
            return;
        }
        isHourPickerShown = true;

        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = calendar.get(java.util.Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityItemSelectedAgenda.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                Date time = new Date();
                time.setHours(hourOfDay);
                time.setMinutes(minute);
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

                String hora_formatada = hourFormat.format(time);
                String minuto_Formatado = minuteFormat.format(time);

                // Lidar com a hora selecionada pelo usuário
                textViewHora.setText(hora_formatada + ":" + minuto_Formatado);

                hourTarefa = hourOfDay;
                minuteTarefa = minute;

                Log.d("TESTE HORA", "MINUTO: "+minuteTarefa);

                isHourPickerShown = false;

                checkForChanges();


            }
        }, hour, minute, true);

        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isHourPickerShown = false;
                linearLayoutHora.setClickable(true);
            }
        });

        // Mostrar o diálogo
        timePickerDialog.show();

    }

    void dialogRepeater(String textoRepeater) {

        Dialog dialog2 = new Dialog(ActivityItemSelectedAgenda.this, R.style.DialogTheme2);
        dialog2.setContentView(R.layout.dialog_repeat_reminder); // Defina o layout do diálogo
        dialog2.setCancelable(true); // Permita que o usuário toque fora do diálogo para fechá-lo
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        Window window = dialog2.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        RadioGroup radioGroup = dialog2.findViewById(R.id.RadioGroup);
        RadioButton radioButtonDefault = dialog2.findViewById(R.id.radioNaoRepetir);
        RadioButton radioButtonDia = dialog2.findViewById(R.id.radioTododia);
        RadioButton radioButtonSemana = dialog2.findViewById(R.id.radioTodaSemana);
        RadioButton radioButtonMes = dialog2.findViewById(R.id.radioTodoMes);
        RadioButton radioButtonAno = dialog2.findViewById(R.id.radioTodoAno);

        if (textoRepeater != null) {

            switch (textoRepeater) {

                case "Todo dia":
                    radioButtonDia.setChecked(true);
                    break;
                case "Toda semana":
                    radioButtonSemana.setChecked(true);
                    break;
                case "Todo mês":
                    radioButtonMes.setChecked(true);
                    break;
                case "Todo ano":
                    radioButtonAno.setChecked(true);
                    break;
                case "Não repetir":
                    radioButtonDefault.setChecked(true);
            }
        } else {
            radioButtonDefault.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Obter a string do RadioButton selecionado
                RadioButton radioButton = dialog2.findViewById(checkedId);
                modoSelecionado = radioButton.getText().toString();
                textViewRepetir.setText(modoSelecionado);

                if(modoSelecionado.equals("Não repetir")) {
                    repetirLembrete=false;
                    repetirModoLembrete=0;
                }else if(modoSelecionado.equals("Todo dia")) {
                    repetirLembrete=true;
                    repetirModoLembrete=1;
                }else if(modoSelecionado.equals("Toda semana")) {
                    repetirLembrete=true;
                    repetirModoLembrete=2;
                }else if(modoSelecionado.equals("Todo mês")) {
                    repetirLembrete=true;
                    repetirModoLembrete=3;
                }else if(modoSelecionado.equals("Todo ano")) {
                    repetirLembrete=true;
                    repetirModoLembrete=4;
                }

                Log.d("TESTE REPETIR", "TESTE: "+repetirModoLembreteSelecionado);
                // Fechar o diálogo
                checkForChanges();
                dialog2.dismiss();
            }
        });
        dialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Ativar a capacidade de clicar na TextView após o diálogo ser fechado
                linearLayoutRepetir.setClickable(true);
            }
        });
        dialog2.show();
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

@SuppressLint("NewApi")
public void attDados(View view) {

    AgendaDAO agendaDAO = new AgendaDAO(ActivityItemSelectedAgenda.this);

    String textViewTitAtt = editTextTitulo.getText().toString();
    String textViewDescAtt = editTextDesc.getText().toString();

    Calendar calendar = Calendar.getInstance();
    int horasInsert = calendar.get(Calendar.HOUR_OF_DAY);
    int minutosInsert = calendar.get(Calendar.MINUTE);

    int horaCompleta = horasInsert * 100 + minutosInsert;

    int horaCompletaEscolhida = hourTarefa * 100 + minuteTarefa;

    LocalDate dataAtual = LocalDate.now();

    if (!aswitch.isChecked()) {

        agendaDAO.atualizarTitDesc(idTarefa, textViewTitAtt, textViewDescAtt);
        AlarmScheduler.cancelAlarm(getApplicationContext(), idTarefa);

        Intent intent = new Intent(ActivityItemSelectedAgenda.this, ActivityAgenda.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    } else {

        if (localdataEscolhida.isEqual(dataAtual) && horaCompletaEscolhida < horaCompleta) {

            Snackbar snackbar = Snackbar.make(view, "O horário definido não pode ser menor que o horário atual.", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();

        } else {

            if (aswitch.isChecked() && !repetirLembrete) {

                Calendar calendar2 = convertToCalendar(localdataEscolhida, hourTarefa, minuteTarefa);

                agendaDAO.atualizarAll(idTarefa, textViewTitAtt, textViewDescAtt, localdataEscolhida, hourTarefa, minuteTarefa,
                        1, 0, 0, 0);
                AlarmScheduler.cancelAlarm(getApplicationContext(), idTarefa);

//                AlarmScheduler.scheduleAlarm(getApplicationContext(), calendar2.getTimeInMillis(), textViewTitAtt,
//                        textViewDescAtt, 0, idTarefa, localdataEscolhida);

                Intent intent = new Intent(ActivityItemSelectedAgenda.this, ActivityAgenda.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (aswitch.isChecked() && repetirLembrete) {

                LocalDate dataAlterada = LocalDate.now();


                if (repetirModoLembrete == 1) {
                    while (dataAlterada.isAfter(localdataEscolhida)) {
                        localdataEscolhida = localdataEscolhida.plusDays(1);
                    }
                } else if (repetirModoLembrete == 2) {
                    while (dataAlterada.isAfter(localdataEscolhida)) {
                        localdataEscolhida = localdataEscolhida.plusWeeks(1);
                    }
                } else if (repetirModoLembrete == 3) {
                    while (dataAlterada.isAfter(localdataEscolhida)) {
                        localdataEscolhida = localdataEscolhida.plusMonths(1);
                    }
                } else if (repetirModoLembrete == 4) {
                    while (dataAlterada.isAfter(localdataEscolhida)) {
                        localdataEscolhida = localdataEscolhida.plusYears(1);
                    }
                }


                if (dataAtual.isAfter(localdataEscolhida)) {
                    Calendar calendar2 = convertToCalendar(localdataEscolhida, hourTarefa, minuteTarefa);

                    agendaDAO.atualizarAll(idTarefa, textViewTitAtt, textViewDescAtt, localdataEscolhida, hourTarefa, minuteTarefa,
                            1, 1, repetirModoLembrete, 0);

                    AlarmScheduler.cancelAlarm(getApplicationContext(), idTarefa);

//                    AlarmScheduler.scheduleAlarm(getApplicationContext(), calendar2.getTimeInMillis(), textViewTitAtt,
//                            textViewDescAtt, repetirModoLembrete, idTarefa, localdataEscolhida);

                    Intent intent = new Intent(ActivityItemSelectedAgenda.this, ActivityAgenda.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if (localdataEscolhida.isEqual(dataAtual) && horaCompletaEscolhida > horaCompleta) {
                    Calendar calendar2 = convertToCalendar(localdataEscolhida, hourTarefa, minuteTarefa);

                    agendaDAO.atualizarAll(idTarefa, textViewTitAtt, textViewDescAtt, localdataEscolhida, hourTarefa, minuteTarefa,
                            1, 1, repetirModoLembrete, 0);

                    AlarmScheduler.cancelAlarm(getApplicationContext(), idTarefa);

//                    AlarmScheduler.scheduleAlarm(getApplicationContext(), calendar2.getTimeInMillis(), textViewTitAtt,
//                            textViewDescAtt, repetirModoLembrete, idTarefa, localdataEscolhida);

                    Intent intent = new Intent(ActivityItemSelectedAgenda.this, ActivityAgenda.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    Calendar calendar2 = convertToCalendar(localdataEscolhida, hourTarefa, minuteTarefa);

                    agendaDAO.atualizarAll(idTarefa, textViewTitAtt, textViewDescAtt, localdataEscolhida, hourTarefa, minuteTarefa,
                            1, 1, repetirModoLembrete, 0);

                    AlarmScheduler.cancelAlarm(getApplicationContext(), idTarefa);

//                    AlarmScheduler.scheduleAlarm(getApplicationContext(), calendar2.getTimeInMillis(), textViewTitAtt,
//                            textViewDescAtt, repetirModoLembrete, idTarefa, localdataEscolhida);

                    Intent intent = new Intent(ActivityItemSelectedAgenda.this, ActivityAgenda.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        }
    }
}
    @SuppressLint("NewApi")
    public static LocalDate convertCalendarToLocalDate(Calendar calendar) {

        if (calendar == null) {
            throw new IllegalArgumentException("Calendar object cannot be null");
        }

        // Obter o Date a partir do Calendar
        Date date = calendar.getTime();

        // Converter Date para LocalDate
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    @SuppressLint("NewApi")
    private Calendar convertToCalendar(LocalDate date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(java.util.Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    public void cancelarAtt() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityItemSelectedAgenda.this);
        builder.setTitle("Cancelar");
        builder.setMessage("Deseja cancelar as alterações e sair? ");
        builder.setNegativeButton("Não", (dialog, which) -> {

        });
        builder.setPositiveButton("Sim", (dialog, which) -> {
            // Ação para o botão OK
            finish();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
