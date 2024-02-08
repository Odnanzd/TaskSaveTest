package com.example.tasksave;


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
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class activity_agenda extends AppCompatActivity implements CustomAdapter.OnItemSelectionChangedListener {

    private Conexao con;
    private SQLiteDatabase db;
    FloatingActionButton floatingActionButton;
    TextView textView;

    ListView listView;
    ImageView imageView;
    ImageView imageView2;
    ArrayList<Long> listaIDs = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(activity_agenda.this, activity_main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @SuppressLint({"NewApi", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        floatingActionButton = findViewById(R.id.button_mais_agenda);
        textView = findViewById(R.id.text_view_agenda_validador);
        listView = findViewById(R.id.list_view_agenda);
        imageView = findViewById(R.id.icon_concluido);
        imageView2 = findViewById(R.id.imageView4);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        VerificaLista();
        ListarAgenda();
        VerificaAgendaComLembretes();

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_agenda.this, activity_main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(activity_agenda.this, activity_agenda_concluido.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


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

        SharedPreferences.Editor prefsEditor = getSharedPreferences("arquivoSalvar2", MODE_PRIVATE).edit();
        prefsEditor.clear();
        prefsEditor.commit();
        SharedPreferences.Editor prefsEditor2 = getSharedPreferences("arquivoSalvar3", MODE_PRIVATE).edit();
        prefsEditor2.clear();
        prefsEditor2.commit();

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_add_agenda); // Defina o layout do diálogo
        dialog.setCancelable(true); // Permita que o usuário toque fora do diálogo para fechá-lo



        EditText editNome = dialog.findViewById(R.id.editTextText);
        EditText editDescricao = dialog.findViewById(R.id.editTextText2);
        Button buttonSalvar = dialog.findViewById(R.id.button_login);
        TextView textView = dialog.findViewById(R.id.textView5);
        TextView textView1 = dialog.findViewById(R.id.textView4);
        TextView charCountTextView = dialog.findViewById(R.id.text_view_contador);
        TextView charCountTextView2 = dialog.findViewById(R.id.textView8);
        ImageView imageView = dialog.findViewById(R.id.imageView4);


        Switch switchCompat = dialog.findViewById(R.id.switch1);
        switchCompat.setChecked(false);
        textView.setVisibility(View.GONE);
        textView1.setVisibility(View.GONE);

        editDescricao.requestFocus();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
//                    AtualizarHora();
//                    AtualizarData();
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
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Criar o DatePickerDialog com a data atual
                DatePickerDialog dialog = new DatePickerDialog(activity_agenda.this, new DatePickerDialog.OnDateSetListener() {
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
                        selectedDate2 = sdf2.format(selectedCalendar.getTime());
                        SharedPreferences prefs = getSharedPreferences("arquivoSalvarData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("arquivo_Data", selectedDate2);

                        textView.setText(selectedDate);

                        Log.d("Verificação Data", "Data:" + year + (month + 1) + dayOfMonth);
                    }
                }, year, month, dayOfMonth);

                dialog.show();
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final java.util.Calendar calendar = java.util.Calendar.getInstance();
                int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = calendar.get(java.util.Calendar.MINUTE);

                // Crie o TimePickerDialog com a hora atual definida
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity_agenda.this, new TimePickerDialog.OnTimeSetListener() {
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
                        textView1.setText(hora_formatada+":"+minuto_Formatado);

                        int horadefinida = hourOfDay;
                        int minutodefinido = minute;

                        SharedPreferences prefs = getSharedPreferences("arquivoSalvar3", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("arquivo_Hora", horadefinida);
                        editor.putInt("arquivo_Minuto", minutodefinido);
                        editor.apply();

                    }
                }, hour, minute, DateFormat.is24HourFormat(activity_agenda.this));

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


                        Agenda agenda = new Agenda(-1, editNome.getText().toString(), editDescricao.getText().toString(),
                                localdataEscolhida, horaEscolhida, minutoEscolhido, true, false, dataAtual,
                                -1, -1, dataAtual,horasInsert ,minutosInsert, false);

//                        if (horaCompleta<=horaCompletaEscolhida) {

                            long idSequencial = agendaDAO.inserir(agenda);
                            agenda.setId(idSequencial);
                            long idAgenda = agenda.getId();

                            SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                            SharedPreferences.Editor saveEdit = save.edit();
                            saveEdit.clear();
                            saveEdit.commit();
                            dialog.dismiss();
                            Toast.makeText(activity_agenda.this, "Tarefa Salva. Nº " + idAgenda, Toast.LENGTH_LONG).show();

//                        } else {
//
//                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                            if (imm != null) {
//                                View currentFocus = getCurrentFocus();
//                                if (currentFocus != null) {
//                                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
//                                }
//                            }
//                            Snackbar snackbar = Snackbar.make(view, "O horário definido não pode ser menor que o horário atual.", Snackbar.LENGTH_SHORT);
//                            snackbar.setBackgroundTint(Color.WHITE);
//                            snackbar.setTextColor(Color.BLACK);
//                            snackbar.show();
//                        }
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

                            Toast.makeText(activity_agenda.this, "Tarefa Salva. Nº " + idAgenda, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity_agenda.this, "Erro", Toast.LENGTH_LONG).show();
                        }

                        SharedPreferences save = getApplicationContext().getSharedPreferences("arquivoSalvar2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor saveEdit = save.edit();
                        saveEdit.clear();
                        saveEdit.commit();
                        dialog.dismiss();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        VerificaLista();
        ListarAgenda();
    }

    public void VerificaLista() {


        con = new Conexao(this);
        db = con.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM agenda WHERE finalizado = 0;", null);
        Cursor cursor2 = db.rawQuery("SELECT * FROM agenda WHERE finalizado = 1;", null);

        if (cursor.getCount() == 0 && cursor2.getCount() == 0) {

            textView.setText("Você não possui nenhuma tarefa a ser feito.");
            textView.setTextSize(15);
            imageView.setVisibility(View.GONE);

        } else if (cursor.getCount() >= 1 && cursor2.getCount() == 0) {

            imageView.setVisibility(View.GONE);

        } else if (cursor.getCount() == 0 && cursor2.getCount() >= 1) {

            imageView.setVisibility(View.VISIBLE);
            textView.setText("Você não possui nenhuma tarefa a ser feito.");
            textView.setTextSize(15);

        } else if (cursor.getCount() >= 1 && cursor2.getCount() >= 1) {
            imageView.setVisibility(View.VISIBLE);
        }
        {

        }
        cursor2.close();
        cursor.close();

    }

    public void VerificaAgendaComLembretes() {

        con = new Conexao(this);
        db = con.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT lembretedefinido FROM agenda WHERE lembretedefinido = 1;", null);

        Log.d("Verificação cursor DB", "Numero de lembretes = " + cursor.getCount());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ListarAgenda() {

        List<Agenda> listaagenda = new ArrayList<Agenda>();
        listaIDs.clear();

        Cursor cursor = db.rawQuery("SELECT * FROM agenda WHERE finalizado = 0;", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                long ID = cursor.getLong(cursor.getColumnIndex("id"));
                @SuppressLint("Range")
                String titulo = cursor.getString(cursor.getColumnIndex("nomeTarefa"));
                @SuppressLint("Range")
                String descricao = cursor.getString(cursor.getColumnIndex("descricaoTarefa"));
                @SuppressLint("Range")
                String dataagenda = cursor.getString(cursor.getColumnIndex("dataAgenda"));
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
                boolean agendaAtraso = (agendaAtrasoDB != 0);


                LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate localdataagendaFim = LocalDate.parse(dataagendaFim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate localdataagendaInsert = LocalDate.parse(dataAgendaInsert, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                listaagenda.add(new Agenda(ID, titulo, descricao, localdataagenda, horaagenda, minutoagenda,
                        lembrete, finalizado, localdataagendaFim, horaAgendaFim, minutoAgendaFim, localdataagendaInsert,
                        horaAgendaInsert, minutoAgendaInsert, agendaAtraso));
                listaIDs.add(ID);

            } while (cursor.moveToNext());
        }

        cursor.close();

        // Convertendo a lista de objetos Agenda em arrays separados para o CustomAdapter
        String[] titulos = new String[listaagenda.size()];
        String[] descricoes = new String[listaagenda.size()];
        String[] datas = new String[listaagenda.size()];
        String[] horas = new String[listaagenda.size()];
        boolean[] lembretes = new boolean[listaagenda.size()];
        long[] ids = new long[listaagenda.size()];

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
        }

        // Configurando o CustomAdapter para a ListView
        CustomAdapter customAdapter = new CustomAdapter(activity_agenda.this, listaIDs, titulos, descricoes, datas, horas, lembretes);
        listView.setAdapter(customAdapter);
        customAdapter.setOnItemSelectionChangedListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customAdapter.notifyDataSetChanged();
                refreshData();
                swipeRefreshLayout.setRefreshing(false);
            }

        });



        ImageView imageViewLixeira = findViewById(R.id.lixeira);
        imageViewLixeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém os IDs dos itens selecionados
                ArrayList<Long> selectedIds = customAdapter.getSelectedIds();
                if (!selectedIds.isEmpty()) {

                    AlertDialog.Builder msgbox = new AlertDialog.Builder(activity_agenda.this);
                    msgbox.setTitle("Excluir");
                    msgbox.setIcon(android.R.drawable.ic_menu_delete);
                    msgbox.setMessage("Você realmente deseja excluir a(s) tarefa(s)?");
                    msgbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            excluirItensDoBanco(selectedIds);
                            atualizarLista();
                            customAdapter.clearSelectedIds();
                            imageViewLixeira.setVisibility(View.GONE);
                            CheckBox checkBox = findViewById(R.id.checkBox);
                            checkBox.setVisibility(View.GONE);
                        }
                    });
                    msgbox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    msgbox.show();

                } else {
                    // Se nenhum item estiver selecionado, você pode mostrar uma mensagem para o usuário
                    Toast.makeText(activity_agenda.this, "Nenhum item selecionado", Toast.LENGTH_SHORT).show();
                }
            }
        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Atualize a exibição dos checkboxes em todos os itens da lista
                Log.d("Verificação On item", "Teste");

                customAdapter.setShowCheckboxes(!customAdapter.isShowCheckboxes());
                listView.clearChoices(); // Limpa as seleções anteriores
                customAdapter.notifyDataSetChanged();

                if(customAdapter.isShowCheckboxes()) {

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
        }
        // Feche a conexão com o banco de dados
        db.close();
    }
    @SuppressLint("NewApi")
    private void atualizarLista() {
        // Restaure a lista de itens
        VerificaLista();
        ListarAgenda();
    }
    @SuppressLint("NewApi")
    private void refreshData() {
        VerificaLista();
        ListarAgenda();
    }
    private void AbrirDialogDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Criar o DatePickerDialog com a data atual
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("Verificação Data", "Data:" + year + (month + 1) + dayOfMonth);
            }
        }, year, month, dayOfMonth);
        dialog.show();
    }
    private void AbrirDialogTimePicker() {

        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = calendar.get(java.util.Calendar.MINUTE);

        // Crie o TimePickerDialog com a hora atual definida
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Lidar com a hora selecionada pelo usuário

                int horadefinida = hourOfDay;
                int minutodefinido = minute;

                String time = "Hora selecionada: " + hourOfDay + ":" + minute;

            }
        }, hour, minute, DateFormat.is24HourFormat(this));

        // Mostrar o diálogo
        timePickerDialog.show();
    }
}



