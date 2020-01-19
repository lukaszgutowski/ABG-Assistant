package com.lukaszgutowski.rkz_0_21;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonWprowadzanieDanych = (Button) findViewById(R.id.buttonWprowadzanieDanych);
        buttonWprowadzanieDanych.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMenu.this, ActivityWprowadzanieDanych.class);
                startActivity(intent);
            }
        });

//        Button buttonBazaPacjentow = (Button) findViewById(R.id.buttonBazaPacjentow);
//        buttonBazaPacjentow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentBazaPacjentowZMenu = new Intent(ActivityMenu.this, ActivityBaza.class);
//                startActivity(intentBazaPacjentowZMenu);
//            }
//        });

        Button buttonInformacje = (Button) findViewById(R.id.buttonInformacje);
        buttonInformacje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInformacje = new Intent(ActivityMenu.this, ActivityInformacje.class);
                startActivity(intentInformacje);
            }
        });
    }
}
