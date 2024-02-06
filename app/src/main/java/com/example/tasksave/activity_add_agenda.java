package com.example.tasksave;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


public class activity_add_agenda extends AppCompatActivity {
    public void onBackPressed() {

        SharedPreferences.Editor prefsEditor = getSharedPreferences("arquivoSalvar2", MODE_PRIVATE).edit();
        prefsEditor.clear();
        prefsEditor.commit();
        SharedPreferences.Editor prefsEditor2 = getSharedPreferences("arquivoSalvar3", MODE_PRIVATE).edit();
        prefsEditor2.clear();
        prefsEditor2.commit();

        Intent intent = new Intent(activity_add_agenda.this, activity_agenda.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

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
        TextView charCountTextView = findViewById(R.id.text_view_contador);
        TextView charCountTextView2 = findViewById(R.id.textView8);
        ImageView imageView = findViewById(R.id.imageView4);


        Switch switchCompat = findViewById(R.id.switch1);
        switchCompat.setChecked(false);
        textView.setVisibility(View.GONE);
        textView1.setVisibility(View.GONE);

        editDescricao.requestFocus();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_add_agenda.this, activity_agenda.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    AtualizarHora();
                    AtualizarData();
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
        String msg_error;
        msg_error = "Os campos não podem ser vazios.";

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editNome.getText().toString().equals("") || editDescricao.getText().toString().equals("")) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        View currentFocus = getCurrentFocus();
                        if (currentFocus != null) {
                            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                        }
                    }
                    Snackbar snackbar = Snackbar.make(view, msg_error, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                } else {
                    if (switchCompat.isChecked()){

                        Calendar calendar = Calendar.getInstance();
                        int horasInsert = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutosInsert = calendar.get(Calendar.MINUTE);

                        int horaCompleta = horasInsert * 100 + minutosInsert;

                        LocalDate dataAtual = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String dataAtualFormatada = dataAtual.format(formatter);

                        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                        String dataEscolhida = sharedPrefs.getString("arquivo_Data2", dataAtualFormatada);
                        LocalDate localdataEscolhida = LocalDate.parse(dataEscolhida, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvar3", Context.MODE_PRIVATE);
                        int horaEscolhida = sharedPrefs2.getInt("arquivo_Hora2", 00);
                        int minutoEscolhido = sharedPrefs2.getInt("arquivo_Minuto2", 00);

                        int horaCompletaEscolhida = horaEscolhida * 100 + minutoEscolhido;

                        Agenda agenda = new Agenda(-1, editNome.getText().toString(), editDescricao.getText().toString(),
                                localdataEscolhida, horaEscolhida, minutoEscolhido, true, false, dataAtual,
                                -1, -1, dataAtual,horasInsert ,minutosInsert, false);

//                        long idSequencial = agendaDAO.inserir(agenda);

                        if (horaCompleta<=horaCompletaEscolhida) {

                            long idSequencial = agendaDAO.inserir(agenda);
                            agenda.setId(idSequencial);
                            long idAgenda = agenda.getId();

                            SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                            SharedPreferences.Editor saveEdit = save.edit();
                            saveEdit.clear();
                            saveEdit.commit();
                            Intent intent = new Intent(activity_add_agenda.this, activity_agenda.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            Toast.makeText(activity_add_agenda.this, "Tarefa Salva. Nº " + idAgenda, Toast.LENGTH_LONG).show();
                        } else {

                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                View currentFocus = getCurrentFocus();
                                if (currentFocus != null) {
                                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                                }
                            }
                            Snackbar snackbar = Snackbar.make(view, "O horário definido não pode ser menor que o horário atual.", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }

//                            SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor saveEdit = save.edit();
//                            saveEdit.clear();
//                            saveEdit.commit();
//                            Intent intent = new Intent(activity_add_agenda.this, activity_agenda.class);
//                            startActivity(intent);

                    } else {

                        Calendar calendar = Calendar.getInstance();
                        int horasInsert = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutosInsert = calendar.get(Calendar.MINUTE);
                        LocalDate dataAtual = LocalDate.now();
//
                        Agenda agenda = new Agenda(-1, editNome.getText().toString(), editDescricao.getText().toString(),
                                dataAtual, -1, -1, false, false, dataAtual,
                                -1, -1, dataAtual, horasInsert, minutosInsert, false);

                        long idSequencial = agendaDAO.inserir(agenda);

                        if (idSequencial > 0) {

                            agenda.setId(idSequencial);

                            long idAgenda = agenda.getId();

                            Toast.makeText(activity_add_agenda.this, "Tarefa Salva. Nº " + idAgenda, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity_add_agenda.this, "Erro", Toast.LENGTH_LONG).show();
                        }

                        SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor saveEdit = save.edit();
                        saveEdit.clear();
                        saveEdit.commit();
                        Intent intent = new Intent(activity_add_agenda.this, activity_agenda.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    }
                }
            }
        });
        editDescricao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    EnviarTarefa();
                    handled = true;
                }
                return handled;
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
        Boolean validadorData = sharedPrefs.getBoolean("arquivo_att_data", false);

        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
        String dataEscolhida = sharedPrefs2.getString("arquivo_Data", "");

        if (validadorData) {

            TextView textView = findViewById(R.id.textView5);
            textView.setText(dataEscolhida);

        } else if(!validadorData) {

            TextView textView = findViewById(R.id.textView5);
            LocalDate dataAtual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
            String dataFormatada = dataAtual.format(formatter);
            textView.setText(dataFormatada);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void AtualizarHora() {

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvar3", Context.MODE_PRIVATE);
        Boolean validador = sharedPrefs.getBoolean("arquivo_att_hora", false);

        SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvar3", Context.MODE_PRIVATE);
        String horaEscolhido = sharedPrefs2.getString("arquivo_Hora", "");
        String minutoEscolhido = sharedPrefs2.getString("arquivo_Minuto", "");
        Log.d("verificar", "validor:"+validador);

        if (validador) {

            TextView textView2 = findViewById(R.id.textView4);
            textView2.setText(horaEscolhido + ":" + minutoEscolhido);
            Log.d("verificação","caso 1:true");

        } else if(!validador) {

            TextView textView = findViewById(R.id.textView4);
            @SuppressLint({"NewApi", "LocalSuppress"}) LocalTime horaAtual = LocalTime.now();
            @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
            String horaFormatada = horaAtual.format(formatter1);
            textView.setText(horaFormatada);
            Log.d("verificação","caso 2: false");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void EnviarTarefa() {

        Calendar calendar = Calendar.getInstance();
        int horasInsert = calendar.get(Calendar.HOUR_OF_DAY);
        int minutosInsert = calendar.get(Calendar.MINUTE);

        AgendaDAO agendaDAO = new AgendaDAO(this);

        EditText editNome = findViewById(R.id.editTextText);
        EditText editDescricao = findViewById(R.id.editTextText2);
        TextView textView = findViewById(R.id.textView5);
        TextView textView1 = findViewById(R.id.textView4);
        Switch switchCompat = findViewById(R.id.switch1);

        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        String dataFormatada = dataAtual.format(formatter);
        textView.setText(dataFormatada);

        LocalTime horaAtual = LocalTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        String horaFormatada = horaAtual.format(formatter1);
        textView1.setText(horaFormatada);

        if (editNome.getText().toString().equals("") || editDescricao.getText().toString().equals("")) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
            }
            Toast.makeText(activity_add_agenda.this, "Erro", Toast.LENGTH_SHORT).show();

        } else {
            if (switchCompat.isChecked()) {

                SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                String dataEscolhida = sharedPrefs.getString("arquivo_Data2", "");
                LocalDate localdataEscolhida = LocalDate.parse(dataEscolhida, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("arquivoSalvar3", Context.MODE_PRIVATE);
                int horaEscolhida = sharedPrefs2.getInt("arquivo_Hora2", 00);
                int minutoEscolhido = sharedPrefs2.getInt("arquivo_Minuto2", 00);

                Agenda agenda = new Agenda(-1, editNome.getText().toString(), editDescricao.getText().toString(),
                        localdataEscolhida, horaEscolhida, minutoEscolhido, true, false,
                        dataAtual, -1, -1, dataAtual, horasInsert, minutosInsert, false);

                long idSequencial = agendaDAO.inserir(agenda);

                if (idSequencial > 0) {
                    agenda.setId(idSequencial);
                    long idAgenda = agenda.getId();

                    Toast.makeText(activity_add_agenda.this, "Tarefa Salva. Nº " + idAgenda, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity_add_agenda.this, "Erro", Toast.LENGTH_LONG).show();
                }

                SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                SharedPreferences.Editor saveEdit = save.edit();
                saveEdit.clear();
                saveEdit.commit();
                finish();

            } else {

                Agenda agenda = new Agenda(-1, editNome.getText().toString(), editDescricao.getText().toString(),
                        dataAtual, -1, -1, false, false, dataAtual, -1,
                        -1, dataAtual, horasInsert, minutosInsert, false);

                long idSequencial = agendaDAO.inserir(agenda);

                if (idSequencial > 0) {

                    agenda.setId(idSequencial);

                    long idAgenda = agenda.getId();

                    Toast.makeText(activity_add_agenda.this, "Tarefa Salva. Nº " + idAgenda, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity_add_agenda.this, "Erro", Toast.LENGTH_LONG).show();
                }

                SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                SharedPreferences.Editor saveEdit = save.edit();
                saveEdit.clear();
                saveEdit.commit();
                finish();
            }
        }
    }
}

