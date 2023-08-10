package com.example.tasksave;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class activity_agenda extends AppCompatActivity {

    private Conexao con;
    private SQLiteDatabase db;
    FloatingActionButton floatingActionButton;
    TextView textView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        floatingActionButton = findViewById(R.id.button_mais_agenda);
        textView = findViewById(R.id.text_view_agenda_validador);

        VerificaLista();


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

    @Override
    protected void onResume() {
        super.onResume();

        // Atualize a lista de tarefas aqui, por exemplo, chamando o método VerificaLista()
        VerificaLista();
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