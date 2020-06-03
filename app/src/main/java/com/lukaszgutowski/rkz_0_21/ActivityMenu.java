package com.lukaszgutowski.rkz_0_21;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button buttonWprowadzanieDanych = (Button) findViewById(R.id.buttonWprowadzanieDanych);
        buttonWprowadzanieDanych.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMenu.this, ActivityWprowadzanieDanych.class);
                startActivity(intent);
            }
        });

        Button buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCautionZMenu = new Intent(ActivityMenu.this, activity_caution.class);
                startActivity(intentCautionZMenu);
            }
        });

        Button buttonInformacje = (Button) findViewById(R.id.buttonInformacje);
        buttonInformacje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInformacje = new Intent(ActivityMenu.this, ActivityHelp.class);
                startActivity(intentInformacje);
            }
        });
    }
}
