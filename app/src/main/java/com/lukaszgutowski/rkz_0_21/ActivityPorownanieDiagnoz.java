package com.lukaszgutowski.rkz_0_21;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPorownanieDiagnoz extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porownanie_diagnoz);





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
            UzyskanyWynikFizjologiczny = "za mało danych";
        };

        String UzyskanyWynikFizjologicznyAgc = getIntent().getStringExtra("2");
        if (UzyskanyWynikFizjologicznyAgc == null || UzyskanyWynikFizjologicznyAgc == "")
        {
            UzyskanyWynikFizjologicznyAgc = "nie wprowadzono stężenia Albumin";
        };

        String UzyskanyWynikBaseExcess = getIntent().getStringExtra("3");
        if (UzyskanyWynikBaseExcess == null || UzyskanyWynikBaseExcess == "")
        {
            UzyskanyWynikBaseExcess = "nie wprowadzono SBE";
        };

        String Imie = getIntent().getStringExtra("4");
        if (Imie == null || Imie == "")
        {
            Imie = "-";
        };
        String Nazwisko = getIntent().getStringExtra("5");
        if (Nazwisko == null || Nazwisko == "")
        {
            Nazwisko = "-";
        };





        TextView textViewWynikMetodyFizjologicznej = (TextView) findViewById(R.id.textViewWynikMetodyFizjologicznej);
        textViewWynikMetodyFizjologicznej.setText(UzyskanyWynikFizjologiczny);

        TextView textViewWynikMetodyFizjologicznejZAgc = (TextView) findViewById(R.id.textViewWynikMetodyFizjologicznejZAgc);
        textViewWynikMetodyFizjologicznejZAgc.setText(UzyskanyWynikFizjologicznyAgc);

        TextView textViewWynikMetodyBaseExcess = (TextView) findViewById(R.id.textViewWynikMetodyBaseExcess);
        textViewWynikMetodyBaseExcess.setText(UzyskanyWynikBaseExcess);

        Toast.makeText(ActivityPorownanieDiagnoz.this, "Obliczenia zakończone pomyślnie", Toast.LENGTH_SHORT).show();
        //Toast.makeText(ActivityPorownanieDiagnoz.this, Imie, Toast.LENGTH_SHORT).show();

        TextView textViewZgodnoscWynikow = findViewById(R.id.textViewZgodnoscWynikow);

        if ( UzyskanyWynikFizjologiczny.equals(UzyskanyWynikFizjologicznyAgc) && UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess ) ) {
            textViewZgodnoscWynikow.setText("Zgodność wyników wyniosła 100%");
        }


        else if (UzyskanyWynikFizjologicznyAgc.equals("nie wprowadzono stężenia Albumin") && UzyskanyWynikBaseExcess.equals("nie wprowadzono SBE" )){

            textViewZgodnoscWynikow.setText("nie można porównać zgodności wyników");

        }

        else if (UzyskanyWynikFizjologicznyAgc.equals("nieprawidłowe dane") && UzyskanyWynikBaseExcess.equals("nieprawidłowe dane")){

            textViewZgodnoscWynikow.setText("nie można porównać zgodności wyników");

        }


        else if (UzyskanyWynikFizjologicznyAgc.equals("nie wprowadzono stężenia Albumin")) {
            if ( UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess)){
                textViewZgodnoscWynikow.setText("Porównano 2 metody.Zgodność wyników wyniosła 100%");
            }
            else {
                textViewZgodnoscWynikow.setText("Porównano 2 metody. Zgodność wyników wyniosła 0%");
            }

        }

        else if (UzyskanyWynikFizjologicznyAgc.equals("nieprawidłowe dane")) {
            if ( UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess)){
                textViewZgodnoscWynikow.setText("Porównano 2 metody.Zgodność wyników wyniosła 100%");
            }
            else {
                textViewZgodnoscWynikow.setText("Porównano 2 metody. Zgodność wyników wyniosła 0%");
            }

        }


        else if (UzyskanyWynikBaseExcess.equals("nieprawidłowe dane")){
            if ( UzyskanyWynikFizjologiczny.equals(UzyskanyWynikFizjologicznyAgc)){
                textViewZgodnoscWynikow.setText("Porównano 2 metody.Zgodność wyników wyniosła 100%");
            }
            else {
                textViewZgodnoscWynikow.setText("Porównano 2 metody. Zgodność wyników wyniosła 0%");
            }
        }


        else if (UzyskanyWynikBaseExcess.equals("nie wprowadzono SBE")){
            if ( UzyskanyWynikFizjologiczny.equals(UzyskanyWynikFizjologicznyAgc)){
                textViewZgodnoscWynikow.setText("Porównano 2 metody.Zgodność wyników wyniosła 100%");
            }
            else {
                textViewZgodnoscWynikow.setText("Porównano 2 metody. Zgodność wyników wyniosła 0%");
            }
        }


        else if (UzyskanyWynikFizjologiczny.equals(UzyskanyWynikBaseExcess)){
            textViewZgodnoscWynikow.setText("Zgodność wyników wyniosła 66%");
        }
        else if (UzyskanyWynikFizjologicznyAgc.equals(UzyskanyWynikBaseExcess)){
            textViewZgodnoscWynikow.setText("Zgodność wyników wyniosła 66%");
        }
        else if (UzyskanyWynikFizjologicznyAgc.equals(UzyskanyWynikFizjologiczny)) {
            textViewZgodnoscWynikow.setText("Zgodność wyników wyniosła 66%");
        }
        else {
            textViewZgodnoscWynikow.setText("Zgodność wyników wyniosła 0%");
        }


        Button buttonDodajDoBazy = (Button) findViewById(R.id.buttonDodajDoBazy);
        buttonDodajDoBazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UzyskanyWynikFizjologiczny = getIntent().getStringExtra("1");
                if (UzyskanyWynikFizjologiczny == null || UzyskanyWynikFizjologiczny == "")
                {
                    UzyskanyWynikFizjologiczny = "za mało danych";
                };

                String UzyskanyWynikFizjologicznyAgc = getIntent().getStringExtra("2");
                if (UzyskanyWynikFizjologicznyAgc == null || UzyskanyWynikFizjologicznyAgc == "")
                {
                    UzyskanyWynikFizjologicznyAgc = "nie wprowadzono stężenia Albumin";
                };

                String UzyskanyWynikBaseExcess = getIntent().getStringExtra("3");
                if (UzyskanyWynikBaseExcess == null || UzyskanyWynikBaseExcess == "")
                {
                    UzyskanyWynikBaseExcess = "nie wprowadzono SBE";
                };

                String Imie = getIntent().getStringExtra("4");
                if (Imie == null || Imie == "")
                {
                    Imie = "-";
                };
                String Nazwisko = getIntent().getStringExtra("5");
                if (Nazwisko == null || Nazwisko == "")
                {
                    Nazwisko = "-";
                };

                Intent intentDoBazyZPorownania = new Intent(ActivityPorownanieDiagnoz.this, ActivityBaza.class);
                intentDoBazyZPorownania.putExtra("1a", UzyskanyWynikFizjologiczny);
                intentDoBazyZPorownania.putExtra("2a", UzyskanyWynikFizjologicznyAgc);
                intentDoBazyZPorownania.putExtra("3a", UzyskanyWynikBaseExcess);
                intentDoBazyZPorownania.putExtra("4a", Imie);
                intentDoBazyZPorownania.putExtra("5a", Nazwisko);
                startActivity(intentDoBazyZPorownania);
            }
        });

       }

    }

