package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class activity_main extends AppCompatActivity {

    public ImageView imageView;
    public TextView text_view_main;
    private Conexao con;
    List<User> ListUser;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public void onBackPressed() {
    }

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view_circle_agenda);
        text_view_main = findViewById(R.id.textView);
        ExibirUsername();



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(activity_main.this, activity_agenda.class);
                //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent2);
            }
        });

        }

        public void ExibirUsername() {
        try {
            SQLiteDatabase db = con.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT username FROM USER", null);

            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                @SuppressLint("Range")
                String txt = cursor.getString(cursor.getColumnIndex("username"));
                text_view_main.setText("Olá" + txt);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }