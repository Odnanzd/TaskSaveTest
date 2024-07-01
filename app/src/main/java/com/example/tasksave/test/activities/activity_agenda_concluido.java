package com.example.tasksave.test.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tasksave.test.conexaoSQLite.Conexao;
import com.example.tasksave.test.baseadapter.CustomAdapterConcluido;
import com.example.tasksave.R;
import com.example.tasksave.test.objetos.Agenda;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class activity_agenda_concluido extends AppCompatActivity {

    ListView listView;
    ArrayList<Long> listaIDs = new ArrayList<>();
    private Conexao con;
    private SQLiteDatabase db;
    ImageView imageView;
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @SuppressLint({"MissingInflatedId", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_concluido);
        listView = (ListView) findViewById(R.id.listview2);
        imageView = findViewById(R.id.imageView4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        ListarAgenda();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ListarAgenda() {

        con = new Conexao(this);
        db = con.getWritableDatabase();

        List<Agenda> listaagenda = new ArrayList<Agenda>();
        listaIDs.clear();

        Cursor cursor = db.rawQuery("SELECT * FROM agenda WHERE finalizado = 1;", null);

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
                String dataagendaInsert = cursor.getString(cursor.getColumnIndex("dataAgendaInsert"));
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



                LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate localdataagendaFim = LocalDate.parse(dataagendaFim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate localdataagendaInsert = LocalDate.parse(dataagendaInsert, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                listaagenda.add(new Agenda(ID, titulo, descricao, localdataagenda, horaagenda, minutoagenda,
                        lembrete, finalizado, localdataagendaFim, horaAgendaFim, minutoAgendaFim, localdataagendaInsert,
                        horaAgendaInsert, minutoAgendaInsert, agendaAtrasoDB, repetirLembrete, repetirLembreteModo, notificouTarefa));
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
        String[] datasAgendaFim = new String[listaagenda.size()];
        String[] horasAgendaFim = new String[listaagenda.size()];

        String[] datasAgendaInsert = new String[listaagenda.size()];
        String[] horasAgendaInsert = new String[listaagenda.size()];

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

            datasAgendaFim[i] = listaagenda.get(i).getDataAgendaFimString();
            int horaAgendaFim = listaagenda.get(i).getHoraAgendaFim();
            int minutoAgendaFim = listaagenda.get(i).getMinutoAgendaFim();
            String horaAgendaFimFormatada = String.format(Locale.getDefault(), "%02d:%02d", horaAgendaFim, minutoAgendaFim);
            horasAgendaFim[i] = horaAgendaFimFormatada;

            datasAgendaInsert[i] = listaagenda.get(i).getDataAgendaInsertString();
            int horaAgendaInsert = listaagenda.get(i).getHoraAgendaInsert();
            int minutoAgendaInsert = listaagenda.get(i).getMinutoAgendaInsert();
            String horaAgendaInsertFormatada = String.format(Locale.getDefault(), "%02d:%02d", horaAgendaInsert, minutoAgendaInsert);
            horasAgendaInsert[i] = horaAgendaInsertFormatada;


        }

        // Configurando o CustomAdapter para a ListView
        CustomAdapterConcluido customAdapter = new CustomAdapterConcluido(getApplicationContext(), listaIDs,
                titulos, descricoes, datas, horas, lembretes, datasAgendaFim, horasAgendaFim, datasAgendaInsert, horasAgendaInsert);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long idTarefa = listaIDs.get(position);

                String Titulo = customAdapter.getItemTitulo(position).toString();
                String Descricao =  customAdapter.getItemDescricao(position).toString();
                String dataItemInsert = customAdapter.getItemDataInsert(position).toString();
                String horaItemInsert = customAdapter.getItemHoraInsert(position).toString();
                String dataItemFim = customAdapter.getItemDataFim(position).toString();
                String horaItemFim = customAdapter.getItemHoraFim(position).toString();
                Boolean lembreteItem = customAdapter.getItemLembrete(position);

                Dialog dialog = new Dialog(activity_agenda_concluido.this, R.style.DialogAboveKeyboard2);
                dialog.setContentView(R.layout.activity_item_selected_agenda_concluido); // Defina o layout do diálogo
                dialog.setCancelable(true); // Permita que o usuário toque fora do diálogo para fechá-lo
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                TextView textView = dialog.findViewById(R.id.textView3);
                EditText editTextTitulo = dialog.findViewById(R.id.titulo_text_view);
                EditText editTextDescricao = dialog.findViewById(R.id.descricao_text_view);
                TextView textViewDataInsert = dialog.findViewById(R.id.textViewDataInsert);
                TextView textViewHoraInsert = dialog.findViewById(R.id.textViewHoraInsert);
                TextView textViewDataFim = dialog.findViewById(R.id.textViewDataFim);
                TextView textViewHoraFim = dialog.findViewById(R.id.textViewHoraFim);
                ImageView imageViewBack = dialog.findViewById(R.id.imageView4);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
                LocalDate localdataInsert = LocalDate.parse(dataItemInsert);
                String dataInsertFormatada = localdataInsert.format(formatter);

                LocalDate localdataFim = LocalDate.parse(dataItemFim);
                String dataFormatadaFim = localdataFim.format(formatter);

                editTextTitulo.setText(Titulo);
                editTextTitulo.setEnabled(false);
                editTextDescricao.setText(Descricao);
                editTextDescricao.setEnabled(false);
                textViewDataInsert.setText(dataInsertFormatada);
                textViewHoraInsert.setText(horaItemInsert);
                textViewDataFim.setText(dataFormatadaFim);
                textViewHoraFim.setText(horaItemFim);

                textView.setText("Tarefa concluída nº "+idTarefa);

                imageViewBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




            }
        });
    }
}