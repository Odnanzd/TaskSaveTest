package com.example.tasksave.test.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tasksave.test.conexaoSQLite.Conexao;
import com.example.tasksave.test.baseadapter.CustomAdapter;
import com.example.tasksave.R;
import com.example.tasksave.test.dao.AgendaDAO;
import com.example.tasksave.test.dao.AgendaDAOMYsql;
import com.example.tasksave.test.dao.UsuarioDAOMYsql;
import com.example.tasksave.test.objetos.Agenda;
import com.example.tasksave.test.objetos.AgendaAWS;
import com.example.tasksave.test.servicesreceiver.AlarmScheduler;
import com.example.tasksave.test.sharedPreferences.SharedPreferencesUsuario;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityAgenda extends AppCompatActivity implements CustomAdapter.OnItemSelectionChangedListener, CustomAdapter.OnItemActionListener {

    private Conexao con;
    private SQLiteDatabase db;
    TextView textView;

    ListView listView;
    ImageView imageView;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageViewBalao;
    ArrayList<Integer> listaIDs = new ArrayList<>();
    ArrayList<Integer> repetirModoLembrete2 = new ArrayList<>();

    CardView cardViewPendents, cardViewTodos, cardViewAtrasados;
    private boolean clickTodos, clickPendentes, clickAtradasados;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    ImageView imageViewLixeira;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"MissingInflatedId", "MissingSuperCall"})
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ActivityAgenda.this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @SuppressLint({"NewApi", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        imageView3 = findViewById(R.id.button_mais_agenda);
        textView = findViewById(R.id.text_view_agenda_validador);
        listView = findViewById(R.id.list_view_agenda);
        imageView = findViewById(R.id.icon_concluido);
        imageView2 = findViewById(R.id.imageView4);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        imageViewLixeira = findViewById(R.id.lixeira);
        imageViewBalao = findViewById(R.id.imageViewBalaoFala);

        cardViewPendents = findViewById(R.id.cardViewPendentes);
        cardViewTodos = findViewById(R.id.cardViewTodos);
        cardViewAtrasados = findViewById(R.id.cardViewAtrasados);

        shimmerFrameLayout = findViewById(R.id.shimmerface);

        clickTodos = true;
        clickPendentes = false;
        clickAtradasados = false;

//        VerificaLista();
        checkChanges();



        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAgenda.this, ActivityMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ActivityAgenda.this, ActivityAgendaConcluido.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        imageView3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                DialogAddAgenda();

            }
        });

        cardViewTodos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                clickTodos = true;
                clickPendentes = false;
                clickAtradasados = false;
                checkChanges();
            }
        });

        cardViewPendents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                clickPendentes = true;
                clickTodos = false;
                clickAtradasados = false;
                checkChanges();

            }
        });

        cardViewAtrasados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAtradasados = true;
                clickPendentes = false;
                clickTodos = false;
                checkChanges();
            }
        });


    }

    @SuppressLint("NewApi")
    public void checkChanges() {

        Log.d("TESTE CHECKCHAGNES", "CHECKGANDES");

        textView.setVisibility(View.GONE);

        AgendaDAO agendaDAO = new AgendaDAO(ActivityAgenda.this);

        if (clickTodos) {

            cardViewTodos.setCardBackgroundColor(getResources().getColor(R.color.blue16));
            cardViewPendents.setCardBackgroundColor(getResources().getColor(R.color.brancoGelo));
            cardViewAtrasados.setCardBackgroundColor(getResources().getColor(R.color.brancoGelo));

            SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityAgenda.this);

            boolean primeiroAgenda = sharedPreferencesUsuario.getPrimeiroAcessoAgenda();

            if(!primeiroAgenda) {

                shimmerFrameLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                shimmerFrameLayout.showShimmer(true);
                cardViewPendents.setEnabled(false);
                cardViewAtrasados.setEnabled(false);
                tarefasSelect();

            }else {

                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.hideShimmer();
                shimmerFrameLayout.stopShimmer();
                listView.setVisibility(View.VISIBLE);
                VerificaLista(0);
                ListarAgenda(0);

            }


        } else if (clickPendentes) {

            cardViewPendents.setCardBackgroundColor(getResources().getColor(R.color.blue16));
            cardViewTodos.setCardBackgroundColor(getResources().getColor(R.color.brancoGelo));
            cardViewAtrasados.setCardBackgroundColor(getResources().getColor(R.color.brancoGelo));


            VerificaLista(1);
            ListarAgenda(1);

        } else if (clickAtradasados) {

            cardViewAtrasados.setCardBackgroundColor(getResources().getColor(R.color.blue16));
            cardViewTodos.setCardBackgroundColor(getResources().getColor(R.color.brancoGelo));
            cardViewPendents.setCardBackgroundColor(getResources().getColor(R.color.brancoGelo));

            VerificaLista(2);
            ListarAgenda(2);

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void DialogAddAgenda() {

        SharedPreferences.Editor prefsEditor = getSharedPreferences("arquivoSalvar2", MODE_PRIVATE).edit();
        prefsEditor.clear();
        prefsEditor.commit();
        SharedPreferences.Editor prefsEditor2 = getSharedPreferences("arquivoSalvarData", MODE_PRIVATE).edit();
        prefsEditor2.clear();
        prefsEditor2.commit();

        Dialog dialog = new Dialog(ActivityAgenda.this, R.style.DialogAboveKeyboard);
        dialog.setContentView(R.layout.dialog_add_agenda); // Defina o layout do diálogo
        dialog.setCancelable(true); // Permita que o usuário toque fora do diálogo para fechá-lo
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        EditText editNome = dialog.findViewById(R.id.editTextText);
        EditText editDescricao = dialog.findViewById(R.id.editTextText2);
        FrameLayout buttonSalvar = dialog.findViewById(R.id.framelayoutTarefa);
        ImageView imageViewInserir = dialog.findViewById(R.id.button_login);

        TextView textView = dialog.findViewById(R.id.textView5);
        TextView textView1 = dialog.findViewById(R.id.textView4);
        TextView charCountTextView = dialog.findViewById(R.id.text_view_contador);
        TextView charCountTextView2 = dialog.findViewById(R.id.textView8);
        TextView textViewRepeat = dialog.findViewById(R.id.textViewRepetirLembrete);
        ProgressBar progressBarCircular = dialog.findViewById(R.id.progressBarCircularTarefa);
        editNome.requestFocus();


        Switch switchCompat = dialog.findViewById(R.id.switch1);
        switchCompat.setChecked(false);
        textView.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);

        textViewRepeat.setOnClickListener(new View.OnClickListener() {
            private boolean dialogDisplayed = false;
            private String textoSelecionado;

            @Override
            public void onClick(View v) {

                if (!dialogDisplayed) {
                    dialogDisplayed = true;

                    // Desativar a capacidade de clicar na TextView
                    textViewRepeat.setClickable(false);

                    // Exibir o diálogo
                    Dialog dialog2 = new Dialog(ActivityAgenda.this, R.style.DialogTheme2);
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


                    if (textoSelecionado != null) {

                        switch (textoSelecionado) {

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
                            textoSelecionado = radioButton.getText().toString();
                            textViewRepeat.setText(textoSelecionado);

                            // Fechar o diálogo
                            dialog2.dismiss();
                        }
                    });


                    dialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            // Ativar a capacidade de clicar na TextView após o diálogo ser fechado
                            textViewRepeat.setClickable(true);
                            dialogDisplayed = false;
                        }
                    });

                    dialog2.show();
                    dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Criar o DatePickerDialog com a data atual
                DatePickerDialog dialog = new DatePickerDialog(ActivityAgenda.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String selectedDate;
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
                        selectedDate = sdf.format(selectedCalendar.getTime());

                        String selectedDate2;
                        Calendar selectedCalendar2 = Calendar.getInstance();
                        selectedCalendar2.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                        selectedDate2 = sdf2.format(selectedCalendar2.getTime());

                        SharedPreferences prefs = getSharedPreferences("arquivoSalvarData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("arquivo_Data", selectedDate2);
                        editor.apply();

                        textView.setText(selectedDate);

                        Log.d("Verificação Data", "Data:" + selectedDate2);
                    }
                }, year, month, dayOfMonth);
                dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dialog.show();
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Crie o TimePickerDialog com a hora atual definida
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityAgenda.this, new TimePickerDialog.OnTimeSetListener() {
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
                        textView1.setText(hora_formatada + ":" + minuto_Formatado);

                        int horadefinida = hourOfDay;
                        int minutodefinido = minute;

                        SharedPreferences prefs = getSharedPreferences("arquivoSalvar3", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("arquivo_Hora", horadefinida);
                        editor.putInt("arquivo_Minuto", minutodefinido);
                        editor.apply();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                    }

                }, hour, minute, true);

                // Mostrar o diálogo
                timePickerDialog.show();
            }
        });

        AgendaDAO agendaDAO = new AgendaDAO(this);
        String msg_error;
        msg_error = "Os campos não podem ser vazios.";

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setCancelable(false);

                progressBarCircular.setVisibility(View.VISIBLE);
                imageViewInserir.setVisibility(View.GONE);
                buttonSalvar.setClickable(false);

                if (editNome.getText().toString().equals("") || editDescricao.getText().toString().equals("")) {

                    Context context = dialog.getContext();

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                    View view2 = dialog.getCurrentFocus();

                    if (view2 != null) {

                        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        Log.d("MSG ", "TESTE" + view2);

                    }
                    progressBarCircular.setVisibility(View.GONE);
                    imageViewInserir.setVisibility(View.VISIBLE);
                    buttonSalvar.setClickable(true);

                    Snackbar snackbar = Snackbar.make(view, msg_error, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                } else {

                    if (switchCompat.isChecked()) {

                        LocalDate dataAtual = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String dataAtualFormatada = dataAtual.format(formatter);

                        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvarData", Context.MODE_PRIVATE);
                        String dataEscolhida = sharedPrefs.getString("arquivo_Data", dataAtualFormatada);
                        LocalDate localdataEscolhida = LocalDate.parse(dataEscolhida, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvar3", Context.MODE_PRIVATE);

                        int horaEscolhida = sharedPrefs2.getInt("arquivo_Hora", 00);
                        int minutoEscolhido = sharedPrefs2.getInt("arquivo_Minuto", 00);


                        Calendar calendar = Calendar.getInstance();
                        int horasInsert = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutosInsert = calendar.get(Calendar.MINUTE);

                        int horaCompleta = horasInsert * 100 + minutosInsert;

                        int horaCompletaEscolhida = horaEscolhida * 100 + minutoEscolhido;

                        String textoTextViewRepetir = textViewRepeat.getText().toString();

                        int repetirLembreteModoDB = 0;
                        boolean repetirLembreteDB = true;

                        if (!textoTextViewRepetir.equals("Não repetir")) {

                            switch (textoTextViewRepetir) {

                                case "Todo dia":
                                    repetirLembreteModoDB = 1;
                                    break;
                                case "Toda semana":
                                    repetirLembreteModoDB = 2;
                                    break;
                                case "Todo mês":
                                    repetirLembreteModoDB = 3;
                                    break;
                                case "Todo ano":
                                    repetirLembreteModoDB = 4;
                                    break;

                            }
                        } else {

                            repetirLembreteDB = false;
                            repetirLembreteModoDB = 0;

                        }

                        if (localdataEscolhida.isEqual(dataAtual) && horaCompletaEscolhida < horaCompleta) {

                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                View currentFocus = getCurrentFocus();
                                if (currentFocus != null) {
                                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                                }
                            }
                            progressBarCircular.setVisibility(View.GONE);
                            imageViewInserir.setVisibility(View.VISIBLE);
                            buttonSalvar.setClickable(true);

                            Snackbar snackbar = Snackbar.make(view, "O horário definido não pode ser menor que o horário atual.", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();


                        } else {

                            SharedPreferences.Editor prefsEditor = getSharedPreferences("arquivoSalvar2", MODE_PRIVATE).edit();
                            prefsEditor.clear();
                            prefsEditor.commit();

                            SharedPreferences.Editor prefsEditor2 = getSharedPreferences("arquivoSalvarData", MODE_PRIVATE).edit();
                            prefsEditor2.clear();
                            prefsEditor2.commit();

                            inserirTarefaBD(editNome.getText().toString(), editDescricao.getText().toString(),
                                    true, repetirLembreteDB, repetirLembreteModoDB, localdataEscolhida,
                                    horaEscolhida, minutoEscolhido, null, -1, -1,
                                    dataAtual, horasInsert, minutosInsert, 0, false, false, dialog);


                        }
                    } else {

                        Calendar calendar = Calendar.getInstance();
                        int horasInsert = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutosInsert = calendar.get(Calendar.MINUTE);

                        SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor saveEdit = save.edit();
                        saveEdit.clear();
                        saveEdit.commit();

                        inserirTarefaBD(editNome.getText().toString(), editDescricao.getText().toString(),
                                false, false, 0, null,
                                -1, -1, null, -1, -1,
                                dataAtual, horasInsert, minutosInsert, 0, false, false, dialog);



                    }


                }
            }
        });

        editNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nada a fazer antes da mudança do texto
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Atualizar o contador de caracteres
                int currentLength = charSequence.length();
                charCountTextView.setText(currentLength + "/14");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nada a fazer depois da mudança do texto
            }
        });
        editDescricao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nada a fazer antes da mudança do texto
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Atualizar o contador de caracteres
                int currentLength = charSequence.length();
                charCountTextView2.setText(currentLength + "/20");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nada a fazer depois da mudança do texto
            }
        });

        dialog.show();

        boolean teste = editNome.hasFocus();
        Log.d("MSG", "MSG FOCUS" + teste);

        if (editNome.hasFocus()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }


    public void VerificaLista(int atraso) {


        con = new Conexao(this);
        db = con.getWritableDatabase();

        String sql;
        String sql2;

        if(atraso==0) {
            sql="SELECT * FROM agenda WHERE finalizado = 0";
            sql2="SELECT * FROM agenda WHERE finalizado = 1";
        }else {
            sql="SELECT * FROM agenda WHERE finalizado = 0 AND agendaAtraso ="+atraso;
            sql2="SELECT * FROM agenda WHERE finalizado = 1 AND agendaAtraso ="+atraso;

        }

        Cursor cursor = db.rawQuery(sql, null);
        Cursor cursor2 = db.rawQuery(sql2, null);
        Log.d("Aqui", "Aqui" + cursor.getCount() + "-" + cursor2.getCount());

        if (cursor.getCount() == 0 && cursor2.getCount() == 0) {

            textView.setText("Você não possui nenhuma tarefa a ser feito.");
            textView.setTextSize(15);
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);


        } else if (cursor.getCount() >= 1 && cursor2.getCount() == 0) {
            Log.d("IF ", "AQUI IF2");
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);

        } else if (cursor.getCount() == 0 && cursor2.getCount() >= 1) {
            Log.d("IF ", "AQUI IF3");
            imageView.setVisibility(View.VISIBLE);
            textView.setText("Você não possui nenhuma tarefa a ser feito.");
            textView.setVisibility(View.VISIBLE);
            textView.setTextSize(15);

        } else if (cursor.getCount() >= 1 && cursor2.getCount() >= 1) {
            Log.d("IF ", "AQUI IF4");
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

        }
        {

        }
        cursor2.close();
        cursor.close();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ListarAgenda(int atraso) {

        con = new Conexao(this);
        db = con.getWritableDatabase();

        listView.setVisibility(View.VISIBLE);

        List<Agenda> listaagenda = new ArrayList<Agenda>();
        listaIDs.clear();
        repetirModoLembrete2.clear();

        String sql = "";

        Cursor cursor = null;
        LocalDate localdataagenda=null;
        LocalDate localdataagendaFim =null;


        if(atraso==0){
            sql = "SELECT * FROM agenda WHERE finalizado = 0";
        }else {
            sql = "SELECT * FROM agenda WHERE finalizado = 0 AND agendaAtraso ="+atraso;
        }

        Log.d("ATRASO", "ATRASO: "+sql+"INT ATRASO: "+atraso);

        try {
                cursor = db.rawQuery(sql, null);
                Log.d("CURSOR SIZE", "CURSOR: "+cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range")
                    int ID = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range")
                    String titulo = cursor.getString(cursor.getColumnIndex("nomeTarefa"));
                    @SuppressLint("Range")
                    String descricao = cursor.getString(cursor.getColumnIndex("descricaoTarefa"));
                    @SuppressLint("Range")
                    String dataagenda = cursor.getString(cursor.getColumnIndex("dataAgenda"));
                    if (dataagenda!=null) {
                        localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                    @SuppressLint("Range")
                    int horaagenda = cursor.getInt(cursor.getColumnIndex("horaAgenda"));
                    @SuppressLint("Range")
                    int minutoagenda = cursor.getInt(cursor.getColumnIndex("minutoAgenda"));
                    @SuppressLint("Range")
                    int lembreteDB = cursor.getInt(cursor.getColumnIndex("lembretedefinido"));
                    boolean lembrete = (lembreteDB != 0);
                    @SuppressLint("Range")
                    int finalizadoDB = cursor.getInt(cursor.getColumnIndex("finalizado"));
                    boolean finalizado = (finalizadoDB != 0);
                    @SuppressLint("Range")
                    String dataagendaFim = cursor.getString(cursor.getColumnIndex("dataAgendaFim"));
                    if (dataagendaFim!=null) {
                        localdataagendaFim = LocalDate.parse(dataagendaFim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                    @SuppressLint("Range")
                    int horaAgendaFim = cursor.getInt(cursor.getColumnIndex("horaAgendaFim"));
                    @SuppressLint("Range")
                    int minutoAgendaFim = cursor.getInt(cursor.getColumnIndex("minutoAgendaFim"));
                    @SuppressLint("Range")
                    String dataAgendaInsert = cursor.getString(cursor.getColumnIndex("dataAgendaInsert"));
                    @SuppressLint("Range")
                    int horaAgendaInsert = cursor.getInt(cursor.getColumnIndex("horaAgendaInsert"));
                    @SuppressLint("Range")
                    int minutoAgendaInsert = cursor.getInt(cursor.getColumnIndex("minutoAgendaInsert"));
                    @SuppressLint("Range")
                    int agendaAtrasoDB = cursor.getInt(cursor.getColumnIndex("agendaAtraso"));
                    @SuppressLint("Range")
                    int repetirLembreteDB = cursor.getInt(cursor.getColumnIndex("repetirLembrete"));
                    boolean repetirLembrete = (repetirLembreteDB != 0);
                    @SuppressLint("Range")
                    int repetirLembreteModo = cursor.getInt(cursor.getColumnIndex("repetirModo"));
                    @SuppressLint("Range")
                    int notificouTarefaDB = cursor.getInt(cursor.getColumnIndex("notificouTarefa"));
                    boolean notificouTarefa = (notificouTarefaDB != 0);


//                    LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                    LocalDate localdataagendaFim = LocalDate.parse(dataagendaFim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate localdataagendaInsert = LocalDate.parse(dataAgendaInsert, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    listaagenda.add(new Agenda(ID, titulo, descricao, localdataagenda, horaagenda, minutoagenda,
                            lembrete, finalizado, localdataagendaFim, horaAgendaFim, minutoAgendaFim, localdataagendaInsert,
                            horaAgendaInsert, minutoAgendaInsert, agendaAtrasoDB, repetirLembrete, repetirLembreteModo, notificouTarefa));
                    listaIDs.add(ID);
                    repetirModoLembrete2.add(repetirLembreteModo);

                } while (cursor.moveToNext());
            }

            cursor.close();

        }catch (Exception e ) {
            e.printStackTrace();
        }

        // Convertendo a lista de objetos Agenda em arrays separados para o CustomAdapter
        String[] titulos = new String[listaagenda.size()];
        String[] descricoes = new String[listaagenda.size()];
        String[] datas = new String[listaagenda.size()];
        String[] horas = new String[listaagenda.size()];
        boolean[] lembretes = new boolean[listaagenda.size()];
        int[] ids = new int[listaagenda.size()];
        boolean[] repetirLembrete = new boolean[listaagenda.size()];
        int[] repetirModoLembrete = new int[listaagenda.size()];
        boolean[] notificouTarefa = new boolean[listaagenda.size()];

        for (int i = 0; i < listaagenda.size(); i++) {
            ids[i] = listaIDs.get(i);
            titulos[i] = listaagenda.get(i).getNomeAgenda();
            descricoes[i] = listaagenda.get(i).getDescriçãoAgenda();
            datas[i] = listaagenda.get(i).getDataAgendaString();
            int hora = listaagenda.get(i).getHoraAgenda();
            int minuto = listaagenda.get(i).getMinutoAgenda();
            String horaFormatada = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto);
            horas[i] = horaFormatada;
            lembretes[i] = listaagenda.get(i).getLembrete();
            repetirLembrete[i] = listaagenda.get(i).getRepetirLembrete();
            repetirModoLembrete[i] = repetirModoLembrete2.get(i);
            notificouTarefa[i] = listaagenda.get(i).isNotificado();
        }

        // Configurando o CustomAdapter para a ListView
        CustomAdapter customAdapter = new CustomAdapter(ActivityAgenda.this, listaIDs, titulos, descricoes, datas, horas,
                lembretes, repetirLembrete, repetirModoLembrete2, notificouTarefa);
        listView.setAdapter(customAdapter);
        customAdapter.setOnItemSelectionChangedListener(this);
        customAdapter.setOnItemActionListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                imageViewLixeira.setVisibility(View.GONE);
                customAdapter.notifyDataSetChanged();
                refreshData();
                swipeRefreshLayout.setRefreshing(false);
            }

        });

        imageViewLixeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém os IDs dos itens selecionados
                ArrayList<Long> selectedIds = customAdapter.getSelectedIds();

                if (!selectedIds.isEmpty()) {

                    dialogDeletaTarefa(selectedIds, customAdapter);

                } else {
                    // Se nenhum item estiver selecionado, você pode mostrar uma mensagem para o usuário
                    Toast.makeText(ActivityAgenda.this, "Nenhum item selecionado", Toast.LENGTH_SHORT).show();
                }
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Atualize a exibição dos checkboxes em todos os itens da lista

                AgendaDAO agendaDAO = new AgendaDAO(ActivityAgenda.this);


                customAdapter.setShowCheckboxes(!customAdapter.isShowCheckboxes());
                listView.clearChoices(); // Limpa as seleções anteriores
                customAdapter.notifyDataSetChanged();

                if (customAdapter.isShowCheckboxes()) {

                    if (agendaDAO.hasTwoOrMoreTasks()) {

                        CheckBox checkBox2 = findViewById(R.id.checkBox);
                        checkBox2.setVisibility(View.VISIBLE);

                        checkBox2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isChecked = checkBox2.isChecked();
                                customAdapter.selectAllItems(isChecked);
                            }
                        });
                        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // Percorre todos os itens na lista e define o estado da checkbox para cada um deles
                                if (isChecked) {

                                    for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                                        View itemView = listView.getChildAt(i);
                                        CheckBox checkBoxItem = itemView.findViewById(R.id.checkbox1);
                                        checkBoxItem.setChecked(isChecked);
                                    }

                                    // Notifica o adaptador sobre as mudanças
                                    ((CustomAdapter) listView.getAdapter()).setShowCheckboxes(isChecked);
                                    ((CustomAdapter) listView.getAdapter()).notifyDataSetChanged();

                                } else {

                                    for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                                        View itemView = listView.getChildAt(i);
                                        CheckBox checkBoxItem = itemView.findViewById(R.id.checkbox1);
                                        checkBoxItem.setChecked(false);
                                    }

                                    // Notifica o adaptador sobre as mudanças
                                    ((CustomAdapter) listView.getAdapter()).setShowCheckboxes(true);
                                    ((CustomAdapter) listView.getAdapter()).notifyDataSetChanged();
                                }
                            }
                        });

                    }
                } else {
                    CheckBox checkBox2 = findViewById(R.id.checkBox);
                    checkBox2.setVisibility(View.GONE);
                    checkBox2.setChecked(false);
                }


                return true; // Indica que o evento de clique longo foi tratado

            }
        });
    }

    @Override
    public void onItemSelectionChanged(boolean hasSelection) {

        ImageView imageView2 = findViewById(R.id.lixeira);

        imageView2.setVisibility(hasSelection ? View.VISIBLE : View.GONE);
    }

    private void excluirItensDoBanco(ArrayList<Long> ids) {
        // Abra a conexão com o banco de dados
        Conexao con = new Conexao(this);
        SQLiteDatabase db = con.getWritableDatabase();
        // Exclua os itens com os IDs fornecidos
        for (long id : ids) {
            db.delete("agenda", "id = ?", new String[]{String.valueOf(id)});
            AlarmScheduler.cancelAlarm(ActivityAgenda.this, id);
        }
        // Feche a conexão com o banco de dados
        db.close();
    }
    public void excluiItensAWS(ArrayList<Long> ids, CustomAdapter customAdapter, Dialog dialog) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            try {

                ProgressBar progressBar = dialog.findViewById(R.id.progressBarSim);
                TextView textViewSim = dialog.findViewById(R.id.textViewSim);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                    textViewSim.setVisibility(View.GONE);
                });

                SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityAgenda.this);
                String emailShared = sharedPreferencesUsuario.getEmailLogin();

                UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
                int idUsuario = usuarioDAOMYsql.idUsarioAWS(emailShared);

                AgendaDAOMYsql agendaDAOMYsql = new AgendaDAOMYsql();
                boolean sucesso = agendaDAOMYsql.deleteTasks(ids, idUsuario);

                if (sucesso) {

                    Log.d("SUCESSO", "SUCESSO AO EXCLUIR");
                    runOnUiThread(() -> {
                        customAdapter.clearSelectedIds();
                        Toast.makeText(getApplicationContext(), "Tarefa(s) excluída(s)", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        atualizarLista();
                    });

                } else {
                    Log.d("ERRO", "ERRO AO EXCLUIR");
                    Toast.makeText(getApplicationContext(), "Erro ao processar ação", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    @SuppressLint("NewApi")
    private void atualizarLista() {
        // Restaure a lista de itens
//        VerificaLista();
        checkChanges();
    }

    @SuppressLint("NewApi")
    private void refreshData() {
//        VerificaLista();
        checkChanges();
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemDeleted(int position) {
//        VerificaLista();
        checkChanges();
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemUpdated(int position) {
//        ListarAgenda();
//        VerificaLista();
    }

    @SuppressLint("NewApi")
    private Calendar convertToCalendar(LocalDate date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public void inserirTarefaBD(String nomeTarefa, String descTarefa, boolean lembreteTarefa,
                               boolean repetirLembrete, int repetirModoLembrete, LocalDate dataTarefa,
                               int horaTarefa, int minutoTarefa, LocalDate dataTarefaFim,
                               int horaTarefaFim, int minutoTarefaFim, LocalDate dataTarefaInsert,
                               int horaTarefaInsert, int minutoTarefaInsert, int atrasoTarefa, boolean finalizadoTarefa,
                               boolean notificouTarefa, Dialog dialog) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            AgendaDAOMYsql agendaDAOMYsql = new AgendaDAOMYsql();

            try {

                SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityAgenda.this);
                String emailShared = sharedPreferencesUsuario.getEmailLogin();

                UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
                int id_usuario = usuarioDAOMYsql.idUsarioAWS(emailShared);

                AgendaAWS agendaAWS = new AgendaAWS();
                agendaAWS.setNome_tarefa(nomeTarefa);
                agendaAWS.setDescricao_tarefa(descTarefa);
                agendaAWS.setLembrete_tarefa(lembreteTarefa);
                agendaAWS.setRepetir_tarefa(repetirLembrete);
                agendaAWS.setRepetir_modo_tarefa(repetirModoLembrete);
                agendaAWS.setData_tarefa(dataTarefa);
                agendaAWS.setHora_tarefa(horaTarefa);
                agendaAWS.setMinuto_tarefa(minutoTarefa);
                agendaAWS.setData_tarefa_fim(dataTarefaFim);
                agendaAWS.setHora_tarefa_fim(horaTarefaFim);
                agendaAWS.setMinuto_tarefa_fim(minutoTarefaFim);
                agendaAWS.setData_tarefa_insert(dataTarefaInsert);
                agendaAWS.setHora_tarefa_insert(horaTarefaInsert);
                agendaAWS.setMinuto_tarefa_insert(minutoTarefaInsert);
                agendaAWS.setAtraso_tarefa(atrasoTarefa);
                agendaAWS.setFinalizado_tarefa(finalizadoTarefa);
                agendaAWS.setNotificou_tarefa(notificouTarefa);
                agendaAWS.setUsuario_id(id_usuario);

                int tarefaID = agendaDAOMYsql.salvaTarefaAWS(agendaAWS);

                runOnUiThread(() -> {

                    if (tarefaID != -1) {
                        Toast.makeText(getApplicationContext(), "Sucesso ao inserir Tarefa: "+tarefaID, Toast.LENGTH_SHORT).show();

                        salvaTarefaSQLite(tarefaID, nomeTarefa, descTarefa, dataTarefa,
                                horaTarefa, minutoTarefa, lembreteTarefa, finalizadoTarefa, dataTarefaFim,
                                horaTarefaFim, minutoTarefaFim, dataTarefaInsert, horaTarefaInsert,
                                minutoTarefaInsert, atrasoTarefa, repetirLembrete, repetirModoLembrete, notificouTarefa);

                        dialog.dismiss();
                        refreshData();

                    } else {
                        Toast.makeText(getApplicationContext(), "Erro.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                });

            } catch (Exception e) {
                Log.d("ERRO SQL CADASTRO", "Erro ao cadastrar usuário: " + e);
            }

        });

    }
    public void salvaTarefaSQLite(int tarefaID, String nomeTarefa, String descTarefa, LocalDate dataTarefa,
                                  int horaTarefa, int minutoTarefa, boolean lembreteTarefa, boolean finalizadoTarefa,
                                  LocalDate dataTarefaFim, int horaTarefaFim, int minutoTarefaFim, LocalDate dataTarefaInsert,
                                  int horaTarefaInsert, int minutoTarefaInsert, int atrasoTarefa, boolean repetirLembrete,
                                  int repetirModoLembrete, boolean notificouTarefa) {

        AgendaDAO agendaDAO = new AgendaDAO(ActivityAgenda.this);

        Agenda agenda = new Agenda(tarefaID, nomeTarefa, descTarefa, dataTarefa, horaTarefa, minutoTarefa, lembreteTarefa, finalizadoTarefa,
                dataTarefaFim, horaTarefaFim, minutoTarefaFim, dataTarefaInsert, horaTarefaInsert, minutoTarefaInsert, atrasoTarefa, repetirLembrete,
                repetirModoLembrete, notificouTarefa);

        agendaDAO.inserir(agenda);

        if(lembreteTarefa) {

            Calendar calendar2 = convertToCalendar(dataTarefa, horaTarefa, minutoTarefa);

            AlarmScheduler.scheduleAlarm(getApplicationContext(), calendar2.getTimeInMillis(), nomeTarefa, descTarefa,
                    repetirModoLembrete, tarefaID, dataTarefa);

        }

    }
    public void dialogDeletaTarefa(ArrayList<Long> ids, CustomAdapter customAdapter) {

        ImageView imageViewLixeira = findViewById(R.id.lixeira);
        CheckBox checkBox = findViewById(R.id.checkBox);

        Dialog dialogDeleta = new Dialog(ActivityAgenda.this, R.style.DialogTheme2);
        dialogDeleta.setContentView(R.layout.dialog_delete_tarefa); // Defina o layout do diálogo
        dialogDeleta.setCancelable(false); // Permita que o usuário toque fora do diálogo para fechá-lo
        dialogDeleta.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        Window window = dialogDeleta.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        FrameLayout frameLayoutNão  = dialogDeleta.findViewById(R.id.frameLayoutNao);
        FrameLayout frameLayoutSim = dialogDeleta.findViewById(R.id.frameLayoutSim);

        frameLayoutNão.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleta.dismiss();
            }
        });

        frameLayoutSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(() -> {

                frameLayoutNão.setEnabled(false);
                excluirItensDoBanco(ids);
                excluiItensAWS(ids, customAdapter, dialogDeleta);
                imageViewLixeira.setVisibility(View.GONE);
                checkBox.setVisibility(View.GONE);

                });
            }
        });

        dialogDeleta.show();
        dialogDeleta.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogDeleta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void tarefasSelect() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            try {


                AgendaDAOMYsql agendaDAOMYsql = new AgendaDAOMYsql();
                UsuarioDAOMYsql usuarioDAOMYsql = new UsuarioDAOMYsql();
                AgendaDAO agendaDAO = new AgendaDAO(ActivityAgenda.this);
                SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(ActivityAgenda.this);

                String emailShared = sharedPreferencesUsuario.getEmailLogin();
                int idUsuario = usuarioDAOMYsql.idUsarioAWS(emailShared);

                List<Agenda> agendaTarefa = agendaDAOMYsql.tarefasUsuario(idUsuario);

                if (agendaTarefa.isEmpty()) {
                    sharedPreferencesUsuario.armazenaPrimeiroAcessoAgenda(true);
                    Log.d(TAG, "Nenhuma tarefa encontrada para o usuário.");
                    runOnUiThread(() -> {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.hideShimmer();
                        shimmerFrameLayout.stopShimmer();
                        VerificaLista(0);
                        ListarAgenda(0);
                        cardViewPendents.setEnabled(true);
                        cardViewAtrasados.setEnabled(true);
                            });
                } else {
                    for (Agenda agenda : agendaTarefa) {

                        sharedPreferencesUsuario.armazenaPrimeiroAcessoAgenda(true);

                        long result = agendaDAO.inserir(agenda);

                    }
                    runOnUiThread(()-> {

                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.hideShimmer();
                        shimmerFrameLayout.stopShimmer();
                        VerificaLista(0);
                        ListarAgenda(0);
                        cardViewPendents.setEnabled(true);
                        cardViewAtrasados.setEnabled(true);

                            });
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        });
    }

}




