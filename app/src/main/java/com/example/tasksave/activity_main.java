package com.example.tasksave;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

public class activity_main extends AppCompatActivity {

    public ImageView imageView;
    public TextView text_view_main;
    private long pressedTime;

    public ImageView imageView1;

    public ImageView imageView2;
    private static final int GALLERY_REQUEST_CODE = 1001;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view_circle_agenda);
        text_view_main = findViewById(R.id.textView);
        imageView1 = findViewById(R.id.image_view_circle_logout);
        imageView2 = findViewById(R.id.imageView2);

        ExibirUsername();
//        mostrarNotificacao(this, "teste", "teste");


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

//    @SuppressLint("MissingPermission")
//
//    private void mostrarNotificacao(Context context, String titulo, String descricao) {
//        int notificationId = (int) System.currentTimeMillis();
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle(titulo)
//                .setContentText(descricao)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        // Intent para abrir a atividade ao tocar na notificação (ajuste conforme sua necessidade)
//        Intent intent = new Intent(context, activity_agenda.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//        builder.setContentIntent(pendingIntent);
//
//        // Construir o gerenciador de notificações e exibir a notificação
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(notificationId, builder.build());
//    }

}