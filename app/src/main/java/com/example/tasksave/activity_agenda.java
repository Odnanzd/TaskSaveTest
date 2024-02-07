package com.example.tasksave;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class activity_agenda extends AppCompatActivity implements CustomAdapter.OnItemLongClickListener {

    private Conexao con;
    private SQLiteDatabase db;
    FloatingActionButton floatingActionButton;
    TextView textView;

    ListView listView;
    ImageView imageView;
    ImageView imageView2;
    ArrayList<Long> listaIDs = new ArrayList<>();
    Button button;

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
//        button = findViewById(R.id.button3);
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

        Intent intent = new Intent(activity_agenda.this, activity_add_agenda.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), listaIDs, titulos, descricoes, datas, horas, lembretes);
        listView.setAdapter(customAdapter);
        customAdapter.setOnItemLongClickListener(this);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(activity_agenda.this, activity_item_selected_agenda.class);
//                long idTarefa = listaIDs.get(position);
//
//                intent.putExtra("idTarefa", idTarefa);
//                intent.putExtra("tituloItem", customAdapter.getItemTitulo(position).toString());
//                intent.putExtra("descricaoItem", customAdapter.getItemDescricao(position).toString());
//                intent.putExtra("dataItem", customAdapter.getItemData(position).toString());
//                intent.putExtra("horaItem", customAdapter.getItemHora(position).toString());
//                intent.putExtra("lembreteItem", customAdapter.getItemLembrete(position));
//                // Inicia a nova Activity
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//            }
//        });
    }
    @Override
    public boolean onItemLongClick(int position) {

        View view = listView.getChildAt(position - listView.getFirstVisiblePosition());
        if (view != null) {
            CheckBox checkBox = view.findViewById(R.id.checkbox1);
            if (checkBox.getVisibility() == View.VISIBLE) {
                // Se estiver visível, define a visibilidade como GONE para removê-la
                checkBox.setVisibility(View.GONE);
            } else {
                // Se não estiver visível, define a visibilidade como VISIBLE para exibi-la
                checkBox.setVisibility(View.VISIBLE);
                ImageView imageViewPalito = view.findViewById(R.id.imageViewpalito);
                imageViewPalito.setVisibility(View.GONE);
            }
        }
        // Retorna false para indicar que o evento de clique longo não foi consumido
        return true;
    }
}

