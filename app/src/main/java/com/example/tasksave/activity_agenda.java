package com.example.tasksave;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class activity_agenda extends AppCompatActivity {

    private Conexao con;
    private SQLiteDatabase db;
    FloatingActionButton floatingActionButton;
    TextView textView;

    ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        floatingActionButton = findViewById(R.id.button_mais_agenda);
        textView = findViewById(R.id.text_view_agenda_validador);
        listView = (ListView) findViewById(R.id.list_view_agenda);
        VerificaLista();
        ListarAgenda();


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

        Intent intent = new Intent(activity_agenda.this, activity_add_agenda.class);
        startActivity(intent);
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

        Cursor cursor = db.rawQuery("SELECT * FROM agenda;", null);

        if (cursor.getCount() == 0) {
            textView.setText("Você ainda não possui nenhuma tarefa");
        } else {
            textView.setVisibility(View.GONE);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ListarAgenda() {

        List<Agenda> listaagenda = new ArrayList<Agenda>();

        Cursor cursor = db.rawQuery("SELECT * FROM agenda;", null);

        if (cursor.moveToFirst()) {
            do {
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

                LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                listaagenda.add(new Agenda(titulo, descricao, localdataagenda, horaagenda, minutoagenda, lembrete));

            } while (cursor.moveToNext());
        }

        cursor.close();

        // Convertendo a lista de objetos Agenda em arrays separados para o CustomAdapter
        String[] titulos = new String[listaagenda.size()];
        String[] descricoes = new String[listaagenda.size()];
        String[] datas = new String[listaagenda.size()];
        String[] horas = new String[listaagenda.size()];
        boolean[] lembretes = new boolean[listaagenda.size()];

        for (int i = 0; i < listaagenda.size(); i++) {
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
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), titulos, descricoes, datas, horas, lembretes);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(activity_agenda.this, activity_item_selected_agenda.class);

                intent.putExtra("tituloItem", customAdapter.getItem(position).toString());
                intent.putExtra("descricaoItem", customAdapter.getItemDescricao(position).toString());
                intent.putExtra("dataItem", customAdapter.getItemData(position).toString());
                intent.putExtra("horaItem", customAdapter.getItemHora(position).toString());
                intent.putExtra("lembreteItem", customAdapter.getItemLembrete(position));
                // Inicia a nova Activity
                startActivity(intent);
            }
        });
    }
}
