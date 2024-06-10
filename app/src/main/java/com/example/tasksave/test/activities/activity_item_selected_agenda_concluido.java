package com.example.tasksave.test.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tasksave.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class activity_item_selected_agenda_concluido extends AppCompatActivity {
    TextView textView;
    EditText editTextTitulo;
    EditText editTextDescricao;
    TextView textViewInsert;
    TextView textViewConcluido;
    TextView textViewDataInsert;
    TextView textViewHoraInsert;
    TextView textViewDataFim;
    TextView textViewHoraFim;
    ImageView imageViewBack;
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected_agenda_concluido);

        textView = findViewById(R.id.textView3);
        editTextTitulo = findViewById(R.id.titulo_text_view);
        editTextDescricao = findViewById(R.id.descricao_text_view);
        textViewDataInsert = findViewById(R.id.textViewDataInsert);
        textViewHoraInsert = findViewById(R.id.textViewHoraInsert);
        textViewDataFim = findViewById(R.id.textViewDataFim);
        textViewHoraFim = findViewById(R.id.textViewHoraFim);
        imageViewBack = findViewById(R.id.imageView4);

        String titulo = getIntent().getStringExtra("tituloItem");
        String descricao = getIntent().getStringExtra("descricaoItem");
        String dataInsert = getIntent().getStringExtra("dataItemInsert");
        String horaInsert = getIntent().getStringExtra("horaItemInsert");
        String dataFim = getIntent().getStringExtra("dataItemFim");
        String horaFim = getIntent().getStringExtra("horaItemFim");
        Boolean lembrete = getIntent().getBooleanExtra("lembreteItem", false);
        long idTarefa = getIntent().getLongExtra("idTarefa", -1);


        Log.d("Verificação intents: ", "Titulo: "+titulo+", descricao: "+descricao+" datainsert:" +
                " "+dataInsert+", id: "+idTarefa+", horaInsert: "+horaInsert+", dataFim: "+dataFim+", HoraFim: "+horaFim);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        LocalDate localdataInsert = LocalDate.parse(dataInsert);
        String dataInsertFormatada = localdataInsert.format(formatter);

        LocalDate localdataFim = LocalDate.parse(dataFim);
        String dataFormatadaFim = localdataFim.format(formatter);

        editTextTitulo.setText(titulo);
        editTextTitulo.setEnabled(false);
        editTextDescricao.setText(descricao);
        editTextDescricao.setEnabled(false);
        textViewDataInsert.setText(dataInsertFormatada);
        textViewHoraInsert.setText(horaInsert);
        textViewDataFim.setText(dataFormatadaFim);
        textViewHoraFim.setText(horaFim);

        textView.setText("Tarefa concluída nº "+idTarefa);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });




    }
}