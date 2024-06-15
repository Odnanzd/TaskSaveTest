package com.example.tasksave.test.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasksave.R;
import com.example.tasksave.test.dao.AgendaDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class activity_item_selected_agenda extends Dialog {
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
    CheckBox checkboxConcluido;
    ImageView imageView;
    TextView textViewLembrete;
    private String arquivoTitulo;
    private String arquivoDesc;
    private long arquivoId;
    private String arquivoData;
    private String arquivoHora;
    private boolean arquivoLembrete;
    private AgendaDAO agendaDAO;
    public void onBackPressed() {
        dismiss();
    }

    public activity_item_selected_agenda(Context context, String titulo, String descricao, long id, String data, String hora, boolean lembrete, AgendaDAO agendaDAO) {
        super(context);
        this.arquivoTitulo = titulo;
        this.arquivoDesc = descricao;
        this.arquivoId = id;
        this.arquivoData = data;
        this.arquivoHora = hora;
        this.arquivoLembrete = lembrete;
        this.agendaDAO = agendaDAO;
    }

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
        checkboxConcluido = findViewById(R.id.checkBoxConcluido);
        imageView = findViewById(R.id.imageView4);
        textViewLembrete = findViewById(R.id.textViewLembretenaodefinido);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        LocalDate localdataEscolhida = LocalDate.parse(arquivoData);
        String dataFormatada = localdataEscolhida.format(formatter);


        int tamanhoTitulo = arquivoTitulo.length();
        textViewContador.setText(tamanhoTitulo + "/14");


        int tamanhoDescricao = arquivoDesc.length();
        textViewContador2.setText(tamanhoDescricao + "/20");


        // Exibindo os dados nos TextViews

        tituloTextView.setText(arquivoTitulo);
        descricaoTextView.setText(arquivoDesc);

        if(arquivoLembrete) {
            dataTextView.setText(dataFormatada);
            horaTextView.setText(arquivoHora);
            textViewLembrete.setVisibility(View.GONE);
        }else {
            textViewLembrete.setVisibility(View.VISIBLE);
            textViewLembrete.setText("Lembrete não definido");
            dataTextView.setVisibility(View.GONE);
            horaTextView.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        checkboxConcluido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    editTextDescricao.setEnabled(false);
                    editTextTitulo.setEnabled(false);
                    dataTextView.setEnabled(false);
                    horaTextView.setEnabled(false);
                    tituloTextView.setText(arquivoTitulo);
                    descricaoTextView.setText(arquivoDesc);
                    button.setEnabled(true);
                    button.setText("Concluir");

                } else {
                    editTextDescricao.setEnabled(true);
                    editTextTitulo.setEnabled(true);
                    dataTextView.setEnabled(true);
                    horaTextView.setEnabled(true);
                    button.setEnabled(false);
                    button.setText("Atualizar");
                }
            }
        });



        editTextTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar nada aqui
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Verifica se o EditText não está vazio
                String novoTitulo = s.toString();
                boolean saoIguais = novoTitulo.equals(arquivoTitulo);

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
                String novoDes = s.toString();
                boolean saoIguais = novoDes.equals(arquivoDesc);

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

                if(checkboxConcluido.isChecked()) {

                    Calendar calendar = Calendar.getInstance();
                    int horasFim = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutosFim = calendar.get(Calendar.MINUTE);
                    @SuppressLint({"NewApi", "LocalSuppress"})
                    LocalDate dataAtual = LocalDate.now();

                    boolean finalizado = agendaDAO.AtualizarStatus(arquivoId, 1, dataAtual, horasFim, minutosFim);

                    if (finalizado) {

                        Toast.makeText(getContext(), "Tarefa concluída.", Toast.LENGTH_SHORT).show();
                        dismiss();

                    } else {
                        // Algo deu errado na atualização

                        Toast.makeText(getContext(), "Erro ao atualizar a tarefa", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    String novoTitulo = editTextTitulo.getText().toString();
                    String novaDescricao = editTextDescricao.getText().toString();

                    // Aqui você deve pegar o ID da tarefa (que você passou como um extra na Intent)

                    // Atualize os valores no banco de dados

//                    AgendaDAO agendaDAO = new AgendaDAO(activity_item_selected_agenda.this);
                    boolean atualizado = agendaDAO.atualizarTitDesc(arquivoId, novoTitulo, novaDescricao);

                    if (atualizado) {
                        // Atualização bem-sucedida
                        Toast.makeText(getContext(), "Tarefa atualizada.", Toast.LENGTH_SHORT).show();

                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Erro ao atualizar a tarefa", Toast.LENGTH_SHORT).show();
                    }
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

        boolean excluir = agendaDAO.Excluir(arquivoId);

        if (excluir) {
            Toast.makeText(getContext(), "Tarefa Excluida.", Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            Toast.makeText(getContext(), "Erro ao excluir tarefa.", Toast.LENGTH_SHORT).show();
        }
    }
    public void ConfirmaExclusao() {

        AlertDialog.Builder msgbox = new AlertDialog.Builder(getContext());
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
