package com.example.tasksave;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class activity_item_selected_agenda extends AppCompatActivity {
    TextView descricaoTextView;
    TextView tituloTextView;

    TextView dataTextView;
    TextView horaTextView;
    Button button;
    EditText editText;
    EditText editText2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected_agenda);

        tituloTextView = findViewById(R.id.titulo_text_view);
        descricaoTextView = findViewById(R.id.descricao_text_view);
        dataTextView = findViewById(R.id.textView11);
        horaTextView = findViewById(R.id.textView12);
        button = findViewById(R.id.button2);
        editText = findViewById(R.id.titulo_text_view);
        editText2 = findViewById(R.id.descricao_text_view);

        // Recebe os extras da Intent
        String titulo = getIntent().getStringExtra("tituloItem");
        String descricao = getIntent().getStringExtra("descricaoItem");
        String data = getIntent().getStringExtra("dataItem");
        String hora = getIntent().getStringExtra("horaItem");
        Boolean lembrete = getIntent().getBooleanExtra("lembreteItem", false);
        long idTarefa = getIntent().getLongExtra("idTarefa", -1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        LocalDate localdataEscolhida = LocalDate.parse(data);
        String dataFormatada = localdataEscolhida.format(formatter);


        // Exibindo os dados nos TextViews

        tituloTextView.setText(titulo);
        descricaoTextView.setText(descricao);

        if(lembrete == true) {
            dataTextView.setText(dataFormatada);
            horaTextView.setText(hora);
        }else {
            dataTextView.setText("Não definido");
            horaTextView.setVisibility(View.INVISIBLE);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar nada aqui
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Verifica se o EditText não está vazio
                if (s.length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não é necessário implementar nada aqui
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar nada aqui
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Verifica se o EditText não está vazio
                if (s.length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não é necessário implementar nada aqui
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String novoTitulo = editText.getText().toString();
                String novaDescricao = editText2.getText().toString();

                // Aqui você deve pegar o ID da tarefa (que você passou como um extra na Intent)

                        // Atualize os valores no banco de dados
                AgendaDAO agendaDAO = new AgendaDAO(activity_item_selected_agenda.this);
                boolean atualizado = agendaDAO.Atualizar(idTarefa, novoTitulo, novaDescricao);

                if (atualizado) {
                    // Atualização bem-sucedida
                    Toast.makeText(activity_item_selected_agenda.this, "Tarefa atualizada com sucesso", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    // Algo deu errado na atualização
                    Toast.makeText(activity_item_selected_agenda.this, "Erro ao atualizar a tarefa", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
