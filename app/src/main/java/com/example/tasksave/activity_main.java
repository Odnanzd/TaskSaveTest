package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class activity_main extends activity_login {

    public ImageView imageView;
    public TextView text_view_main;


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
        alterarNomeTextView();

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

    //MÉTODOS


    public void alterarNomeTextView() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            String usernametext = (String) b.get("arqName");
            text_view_main.setText(usernametext);
        }
    }
}