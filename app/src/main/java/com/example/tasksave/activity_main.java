package com.example.tasksave;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class activity_main extends activity_login {



    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public void onBackPressed() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    }
