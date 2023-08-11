package com.example.tasksave;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

                LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                listaagenda.add(new Agenda(titulo, descricao, localdataagenda, horaagenda, minutoagenda));

            } while (cursor.moveToNext());
        }

        cursor.close();
        // Convertendo a lista de objetos Agenda em arrays separados para o CustomAdapter
        String[] titulos = new String[listaagenda.size()];
        String[] descricoes = new String[listaagenda.size()];
        String[] datas = new String[listaagenda.size()];
        String[] horas = new String[listaagenda.size()];

        for (int i = 0; i < listaagenda.size(); i++) {
            titulos[i] = listaagenda.get(i).getNomeAgenda();
            descricoes[i] = listaagenda.get(i).getDescriçãoAgenda();
            datas[i] = listaagenda.get(i).getDataAgendaString();
            horas[i] = listaagenda.get(i).getHoraAgenda() + ":" + listaagenda.get(i).getMinutoAgenda();
        }

        // Configurando o CustomAdapter para a ListView
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), titulos, descricoes, datas, horas);
        ListView listView = findViewById(R.id.list_view_agenda); // Substitua "sua_listview_id" pelo ID correto da sua ListView
        listView.setAdapter(customAdapter);
    }


    }


    // Método para abrir ou criar o banco de dados
//    private void abrirOuCriarDB() {
//        // Abra ou crie o banco de dados com o nome "database"
//        try {
//            database = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
//
//            // Crie a tabela "agenda" com a coluna "titulo"
//            database.execSQL("CREATE TABLE IF NOT EXISTS agenda (titulo TEXT);");
//            database.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//}

        // Método para inserir dados na tabela "agenda"
//        private void inserirDados () {
//
//            try {
//                database = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
//
//                // Crie um ContentValues para armazenar os valores a serem inseridos na tabela
//                ContentValues values = new ContentValues();
//                values.put("titulo", "Atividade Fernando");
//
//                // Insira os dados na tabela "agenda"
//                database.insert("agenda", null, values);
//                database.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Método para listar dados na ListView
//        private void listarDados () {
//
//            try {
//
//                database = this.openOrCreateDatabase("database", MODE_PRIVATE, null);
//
//                // Consulte todos os dados da tabela "agenda"
//                Cursor cursor = database.rawQuery("SELECT titulo FROM agenda", null);
//
//                // Crie uma lista para armazenar os títulos
//                ArrayList<String> titulos = new ArrayList<>();
//
//                // Verifique se o cursor não é nulo e mova-o para a primeira posição
//                if (cursor != null && cursor.moveToFirst()) {
//                    do {
//                        // Obtenha o título da coluna "titulo" e adicione-o à lista
//                        @SuppressLint("Range")
//                        String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
//                        titulos.add(titulo);
//                    } while (cursor.moveToNext());
//                }
//
//                // Feche o cursor após o uso
//                if (cursor != null) {
//                    cursor.close();
//                }
//                // Configure o adaptador da ListView para exibir os títulos
//                ListView listView = findViewById(R.id.list_view_agenda);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
//                listView.setAdapter(adapter);
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }