package com.lukaszgutowski.rkz_0_21;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPorownanieDiagnoz extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porownanie_diagnoz);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);





        Button buttonInformations = (Button) findViewById(R.id.buttonInformations);
        buttonInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInformacjeZPorownanieDiagnoz = new Intent(ActivityPorownanieDiagnoz.this, ActivityInformacje.class);
                startActivity(intentInformacjeZPorownanieDiagnoz);
            }
        });




        Button buttonWrocDoMenu = (Button) findViewById(R.id.buttonWrocDoMenu);
        buttonWrocDoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWrocDoMenu = new Intent(ActivityPorownanieDiagnoz.this, ActivityMenu.class);
                startActivity(intentWrocDoMenu);
            }
        });


         String UzyskanyWynikFizjologiczny = getIntent().getStringExtra("1");
        if (UzyskanyWynikFizjologiczny == null || UzyskanyWynikFizjologiczny == "")
        {
            UzyskanyWynikFizjologiczny = "corrupted data";
        };

        String UzyskanyWynikFizjologicznyAgc = getIntent().getStringExtra("2");
        if (UzyskanyWynikFizjologicznyAgc == null || UzyskanyWynikFizjologicznyAgc == "")
        {
            UzyskanyWynikFizjologicznyAgc = "You didn't enter Alb";
        };

        String UzyskanyWynikBaseExcess = getIntent().getStringExtra("3");
        if (UzyskanyWynikBaseExcess == null || UzyskanyWynikBaseExcess == "")
        {
            UzyskanyWynikBaseExcess = "You didn't enter SBE / aHCO3";
        };

        String UzyskanyWynikBaseExcessAgc = getIntent().getStringExtra("4");
        if (UzyskanyWynikBaseExcessAgc == null || UzyskanyWynikBaseExcessAgc == "")
        {
            UzyskanyWynikBaseExcessAgc = "You didn't enter Alb";
        };

        String deltaph = getIntent().getStringExtra("X");








        TextView textViewWynikMetodyFizjologicznej = (TextView) findViewById(R.id.textViewWynikMetodyFizjologicznej);
        textViewWynikMetodyFizjologicznej.setText(UzyskanyWynikFizjologiczny);

        TextView textViewWynikMetodyFizjologicznejZAgc = (TextView) findViewById(R.id.textViewWynikMetodyFizjologicznejZAgc);
        textViewWynikMetodyFizjologicznejZAgc.setText(UzyskanyWynikFizjologicznyAgc);

        TextView textViewWynikMetodyBaseExcess = (TextView) findViewById(R.id.textViewWynikMetodyBaseExcess);
        textViewWynikMetodyBaseExcess.setText(UzyskanyWynikBaseExcess);



        TextView textViewWynikMetodyBaseExcess2 = (TextView) findViewById(R.id.textViewWynikMetodyBaseExcess2);
        textViewWynikMetodyBaseExcess2.setText(UzyskanyWynikBaseExcessAgc);







        Toast.makeText(ActivityPorownanieDiagnoz.this, deltaph, Toast.LENGTH_LONG).show();


        TextView textViewZgodnoscWynikow = findViewById(R.id.textViewZgodnoscWynikow);

        if ( UzyskanyWynikFizjologiczny.equals(UzyskanyWynikFizjologicznyAgc) && UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess ) && UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcessAgc)) {
            textViewZgodnoscWynikow.setText("The compliance of the results is 100%");
        }

        // only 1 method
        else if (UzyskanyWynikFizjologicznyAgc.equals("You didn't enter Alb") && UzyskanyWynikBaseExcess.equals("You didn't enter SBE / aHCO3")) {
            //    && UzyskanyWynikBaseExcessAgc.equals("you didn't enter Alb")
            textViewZgodnoscWynikow.setText("Only one approach has been used. The compliance of the results can't be calculated");
            //TODO zastanowic sie czy nie skrocic tekstu, bo troche wystaje
            }

        // only 2 methods : 1, 2
        else if (UzyskanyWynikBaseExcess.equals("You didn't enter SBE / aHCO3")&& UzyskanyWynikBaseExcessAgc.equals("You didn't enter Alb")) {
            if ( UzyskanyWynikFizjologiczny.equals(UzyskanyWynikFizjologicznyAgc)){
                textViewZgodnoscWynikow.setText("Comparing 2 approaches. The compliance of the results is 100%");
            }
            else {
                textViewZgodnoscWynikow.setText("Comparing 2 approaches. The compliance of the results is 0%");
            }

        }


        // only 2 methods : 1, 3
        else if (UzyskanyWynikFizjologicznyAgc.equals("You didn't enter Alb") && UzyskanyWynikBaseExcessAgc.equals("You didn't enter Alb")) {
            if ( UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess)){
                textViewZgodnoscWynikow.setText("Comparing 2 approaches. The compliance of the results is 100%");
            }
            else {
                textViewZgodnoscWynikow.setText("Comparing 2 approaches. The compliance of the results is 0%");
            }

        }



        else if (UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess) &&  UzyskanyWynikFizjologiczny.equals(UzyskanyWynikFizjologicznyAgc)){
            textViewZgodnoscWynikow.setText("The compliance of the results is 75%");
        }

        else if (UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess) &&  UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcessAgc)){
            textViewZgodnoscWynikow.setText("The compliance of the results is 75%");
        }

        else if (UzyskanyWynikFizjologiczny.equals(UzyskanyWynikFizjologicznyAgc) &&  UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcessAgc)){
            textViewZgodnoscWynikow.setText("The compliance of the results is 75%");
        }


        else if (UzyskanyWynikFizjologicznyAgc.equals(UzyskanyWynikBaseExcess) &&  UzyskanyWynikFizjologicznyAgc.equals(UzyskanyWynikBaseExcessAgc)){
            textViewZgodnoscWynikow.setText("The compliance of the results is 75%");
        }




        else if (UzyskanyWynikFizjologicznyAgc.equals(UzyskanyWynikBaseExcess)){
            textViewZgodnoscWynikow.setText("The compliance of the results is 50%");
        }

        else if (UzyskanyWynikFizjologicznyAgc.equals(UzyskanyWynikFizjologiczny)) {
            textViewZgodnoscWynikow.setText("The compliance of the results is 50%");
        }

        else if (UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess)) {
            textViewZgodnoscWynikow.setText("The compliance of the results is 50%");
        }

        else if (UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcessAgc)) {
            textViewZgodnoscWynikow.setText("The compliance of the results is 50%");
        }

        else if (UzyskanyWynikFizjologicznyAgc.equals(UzyskanyWynikBaseExcessAgc)) {
            textViewZgodnoscWynikow.setText("The compliance of the results is 50%");
        }

        else if (UzyskanyWynikBaseExcess.equals(UzyskanyWynikBaseExcessAgc)) {
            textViewZgodnoscWynikow.setText("The compliance of the results is 50%");
        }



        else {
            textViewZgodnoscWynikow.setText("The compliance of the results is 0%");
        }




       }

    }

