package com.example.tasksave;

import static android.content.Context.MODE_PRIVATE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class activity_agenda extends AppCompatActivity {

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        abrirOuCriarDB();
        inserirDados();
        listarDados();
    }

    // Método para abrir ou criar o banco de dados
    private void abrirOuCriarDB() {
        // Abra ou crie o banco de dados com o nome "database"
        database = this.openOrCreateDatabase("database", MODE_PRIVATE, null);

        // Crie a tabela "agenda" com a coluna "titulo"
        database.execSQL("CREATE TABLE IF NOT EXISTS agenda (titulo TEXT);");
    }

    // Método para inserir dados na tabela "agenda"
    private void inserirDados() {
        // Crie um ContentValues para armazenar os valores a serem inseridos na tabela
        ContentValues values = new ContentValues();
        values.put("titulo", "Atividade Fernando");

        // Insira os dados na tabela "agenda"
        database.insert("agenda", null, values);
    }

    // Método para listar dados na ListView
    private void listarDados() {
        // Consulte todos os dados da tabela "agenda"
        Cursor cursor = database.rawQuery("SELECT titulo FROM agenda", null);

        // Crie uma lista para armazenar os títulos
        ArrayList<String> titulos = new ArrayList<>();

        // Verifique se o cursor não é nulo e mova-o para a primeira posição
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Obtenha o título da coluna "titulo" e adicione-o à lista
                @SuppressLint("Range")
                String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                titulos.add(titulo);
            } while (cursor.moveToNext());
        }

        // Feche o cursor após o uso
        if (cursor != null) {
            cursor.close();
        }

        // Configure o adaptador da ListView para exibir os títulos
        ListView listView = findViewById(R.id.list_view_agenda);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
        listView.setAdapter(adapter);
    }
}