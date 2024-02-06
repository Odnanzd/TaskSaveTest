package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class activity_main extends AppCompatActivity {

    public ImageView imageView;
    public TextView text_view_main;
    private long pressedTime;

    public ImageView imageView1;

    public ImageView imageView2;
    public ImageView numeroIcon;
    private static final int GALLERY_REQUEST_CODE = 1001;
    private Conexao con;
    private SQLiteDatabase db;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view_circle_agenda);
        text_view_main = findViewById(R.id.textView);
        imageView1 = findViewById(R.id.image_view_circle_logout);
        imageView2 = findViewById(R.id.imageView2);
        numeroIcon = findViewById(R.id.iconnumero);

        ExibirUsername();
        VerificarAtrasos();


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_main.this, "Saindo...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(activity_main.this, activity_agenda.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        }
    });
}

    // Sobrescrever o método onActivityResult para tratar a imagem selecionada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Obter o URI da imagem selecionada
            ImageView imageView = findViewById(R.id.imageView2);
            imageView.setImageURI(data.getData());
        }
    }

        public void ExibirUsername() {

        UserDAO userDAO = new UserDAO(this);
        List<User> userList = userDAO.ListarNome();

        if (userList.size() > 0) {
            User user = userList.get(0);
            text_view_main.setText("Olá, " + user.getUsername());
        } else {
            text_view_main.setText("Usuário");
        }

        }
    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Pressione Voltar de novo para sair.", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
    public void VerificarAtrasos() {

        con = new Conexao(this);
        db = con.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM agenda WHERE agendaAtraso = 1 AND finalizado = 0", null);

        if (cursor.getCount() == 0) {

            numeroIcon.setVisibility(View.GONE);

        } else if (cursor.getCount() == 1) {

            numeroIcon.setVisibility(View.VISIBLE);

        } else if (cursor.getCount() == 2) {

            numeroIcon.setImageResource(R.drawable.numero2icon);
            numeroIcon.setVisibility(View.VISIBLE);

        } else if (cursor.getCount() == 3) {

            numeroIcon.setImageResource(R.drawable.numero3icon);
            numeroIcon.setVisibility(View.VISIBLE);

        } else if (cursor.getCount() == 4) {

            numeroIcon.setImageResource(R.drawable.numero4icon);
            numeroIcon.setVisibility(View.VISIBLE);
        } else if (cursor.getCount() == 5) {

            numeroIcon.setImageResource(R.drawable.numero5icon);
            numeroIcon.setVisibility(View.VISIBLE);
        } else if (cursor.getCount() == 6) {

            numeroIcon.setImageResource(R.drawable.numero6icon);
            numeroIcon.setVisibility(View.VISIBLE);

        } else if (cursor.getCount() == 7) {

            numeroIcon.setImageResource(R.drawable.numero7icon);
            numeroIcon.setVisibility(View.VISIBLE);

        } else if (cursor.getCount() == 8) {

            numeroIcon.setImageResource(R.drawable.numero8icon);
            numeroIcon.setVisibility(View.VISIBLE);
        } else if (cursor.getCount() >= 9) {

            numeroIcon.setImageResource(R.drawable.numero9icon);
            numeroIcon.setVisibility(View.VISIBLE);

        }
    }
}