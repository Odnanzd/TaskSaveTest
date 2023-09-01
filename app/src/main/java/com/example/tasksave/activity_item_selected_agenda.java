package com.example.tasksave;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
    EditText editTextTitulo;
    EditText editTextDescricao;
    Button button2;
    TextView textViewContador;
    TextView textViewContador2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected_agenda);


        tituloTextView = findViewById(R.id.titulo_text_view);
        descricaoTextView = findViewById(R.id.descricao_text_view);
        dataTextView = findViewById(R.id.textView11);
        horaTextView = findViewById(R.id.textView12);
        button = findViewById(R.id.button2);
        button2 = findViewById(R.id.button);
        editTextTitulo = findViewById(R.id.titulo_text_view);
        editTextDescricao = findViewById(R.id.descricao_text_view);
        textViewContador = findViewById(R.id.text_view_contador1);
        textViewContador2 = findViewById(R.id.text_view_contador2);


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

        String titulo2 = getIntent().getStringExtra("tituloItem");
        int tamanhoTitulo = titulo2.length();
        textViewContador.setText(tamanhoTitulo + "/14");

        String descricao2 = getIntent().getStringExtra("descricaoItem");
        int tamanhoDescricao = descricao2.length();
        textViewContador2.setText(tamanhoDescricao + "/14");


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


        editTextTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar nada aqui
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Verifica se o EditText não está vazio
                String novoTitulo = s.toString();
                boolean saoIguais = novoTitulo.equals(titulo);

                if (s.length() > 0 && !saoIguais) {
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
        editTextDescricao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar nada aqui
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Verifica se o EditText não está vazio
                String novoTitulo = s.toString();
                boolean saoIguais = novoTitulo.equals(titulo);

                if (s.length() > 0 && !saoIguais) {
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


                String novoTitulo = editTextTitulo.getText().toString();
                String novaDescricao = editTextDescricao.getText().toString();

                // Aqui você deve pegar o ID da tarefa (que você passou como um extra na Intent)

                        // Atualize os valores no banco de dados

                AgendaDAO agendaDAO = new AgendaDAO(activity_item_selected_agenda.this);
                boolean atualizado = agendaDAO.Atualizar(idTarefa, novoTitulo, novaDescricao);

                if (atualizado) {
                    // Atualização bem-sucedida
                    Toast.makeText(activity_item_selected_agenda.this, "Tarefa atualizada.", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    // Algo deu errado na atualização
                    Toast.makeText(activity_item_selected_agenda.this, "Erro ao atualizar a tarefa", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmaExclusao();
            }
        });
        editTextTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Atualizar o contador de caracteres
                int currentLength = charSequence.length();
                textViewContador.setText(currentLength + "/14");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nada a fazer depois da mudança do texto
            }
        });
        editTextDescricao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nada a fazer antes da mudança do texto
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Atualizar o contador de caracteres
                int currentLength = charSequence.length();
                textViewContador2.setText(currentLength + "/20");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nada a fazer depois da mudança do texto
            }
        });

    }
    public void ExcluirTarefa() {

        long idTarefa = getIntent().getLongExtra("idTarefa", -1);
        AgendaDAO agendaDAO = new AgendaDAO(activity_item_selected_agenda.this);
        boolean excluir = agendaDAO.Excluir(idTarefa);

        if (excluir) {
            Toast.makeText(activity_item_selected_agenda.this, "Tarefa Excluida.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity_item_selected_agenda.this, activity_agenda.class);
            startActivity(intent);
        } else {
            Toast.makeText(activity_item_selected_agenda.this, "Erro ao excluir tarefa.", Toast.LENGTH_SHORT).show();
        }
    }
    public void ConfirmaExclusao() {

        AlertDialog.Builder msgbox = new AlertDialog.Builder(activity_item_selected_agenda.this);
        msgbox.setTitle("Excluir");
        msgbox.setIcon(android.R.drawable.ic_menu_delete);
        msgbox.setMessage("Você realmente deseja excluir a tarefa?");
        msgbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExcluirTarefa();
            }
        });
        msgbox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        msgbox.show();
    }


}
