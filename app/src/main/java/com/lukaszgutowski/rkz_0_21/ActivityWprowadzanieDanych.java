package com.lukaszgutowski.rkz_0_21;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityWprowadzanieDanych extends AppCompatActivity {



    static double dajAgc (double alb, double ag) {
        return (2.5*(40-alb)) + ag;
            }

    static String metodaNadrzednaAgc (double ph, double paco2, double hco3, double agc){
        if(ph < 7.38){
            return kwasicaAgc (hco3, paco2, agc);

        }
        else if(ph > 7.42){
            return zasadowicaAgc (hco3, paco2, agc);
        }

        else{
            return "pH w normie";
        }

    }


    static String kwasicaAgc (double hco3, double paco2, double agc){
        double przewidywanePaco2 = dajPrzewidywanePaco2(hco3);
        double odpowiedzHco3 = dajOdpowiedzHco3(hco3, paco2);

        if ( hco3 < 22 ){
            System.out.println("kwasica metaboliczna");

            if (paco2 < (przewidywanePaco2 -2) ){
                System.out.println("kwasica metaboliczna i zasadowica oddechowa");
                return "kwasica metaboliczna i zasadowica oddechowa";
            }

            else if (paco2 > (przewidywanePaco2 +2) ){
                System.out.println("kwasica metaboliczna i kwasica oddechowa");
                return "kwasica metaboliczna i kwasica oddechowa";
            }

            else {

                if (agc <16){
                    System.out.println("kwasica metaboliczna");
                    return "kwasica metaboliczna";
                }
                else {
                    System.out.println("kwasica metaboliczna z wysoką luką anionową");

                    return "kwasica metaboliczna z wysoką luką anionową";
                }

            }
        }
        else if (paco2 > 42){

            System.out.println("kwasica oddechowa");

            if (odpowiedzHco3 == 1){
                System.out.println("ostra kwasica oddechowa");
                return "ostra kwasica oddechowa";
            }

            else if (odpowiedzHco3 < 1){
                System.out.println("kwasica oddechowa i metaboliczna");
                return "kwasica oddechowa i metaboliczna";
            }

            else if (odpowiedzHco3 >= 4 && odpowiedzHco3 <= 5 ){
                System.out.println("przewlekła kwasica oddechowa");
                return "przewlekła kwasica oddechowa";
            }

            else if (odpowiedzHco3 > 5 ){
                System.out.println("kwasica oddechowa i zasadowica metaboliczna");
                return "kwasica oddechowa i zasadowica metaboliczna";
            }

        }

        return "nieprawidłowe dane";

    }

    static String zasadowicaAgc (double hco3, double paco2, double agc){
        double przewidywanePaco2Zasadowe = dajPrzewidywanePaco2Zasadowe(hco3);
        double odpowiedzHco3Zasadowe = dajOdpowiedzHco3Zasadowe(hco3, paco2);

        if (hco3 > 26){
            System.out.println("zasadowica metaboliczna");

            if (paco2 < przewidywanePaco2Zasadowe-2){
                System.out.println("zasadowica metaboliczna i oddechowa");
                return "zasadowica metaboliczna i oddechowa";
            }

            else if (paco2 > przewidywanePaco2Zasadowe+2){
                System.out.println("zasadowica metaboliczna i kwasica oddechowa");
                return "zasadowica metaboliczna i kwasica oddechowa";
            }

            else {
                System.out.println("zasadowica metaboliczna");
                return "zasadowica metaboliczna";
            }
        }


        else if (paco2 < 38){
            System.out.println("zasadowica oddechowa");

            if (odpowiedzHco3Zasadowe == 2){
                System.out.println("ostra zasadowica oddechowa");
                return "ostra zasadowica oddechowa";
            }

            else if (odpowiedzHco3Zasadowe < 2){
                System.out.println("zasadowica oddechowa i zasadowica metaboliczna");
                return "zasadowica oddechowa i zasadowica metaboliczna";
            }

            else if (odpowiedzHco3Zasadowe > 4 && odpowiedzHco3Zasadowe < 5){
                System.out.println("przewlekłą zasadowica oddechowa");
                return "przewlekła zasadowica oddechowa";
            }

            else if (odpowiedzHco3Zasadowe > 5){
                System.out.println("zasadowica oddechowa i kwasica metaboliczna");
                return "zasadowica oddechowa i kwasica metaboliczna";
            }
            else {
                System.out.println("zasadowica oddechowa");
                return "zasadowica oddechowa";
            }
        }


        return "nieprawidłowe dane";
    }





    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    //
    //                      Physiological HH
    //
    //
    //
    //
    //
    ///
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////






    static String metodaNadrzedna (double ph, double paco2, double hco3, double ag){
        if(ph < 7.38){
            return kwasica (hco3, paco2, ag);

        }
        else if(ph > 7.42){
            return zasadowica (hco3, paco2, ag);
        }

        else{
            return "pH w normie";
        }

    }

    static double dajPrzewidywanePaco2 (double hco3){
        return 1.5 * hco3 + 8;
    }
    static double dajOdpowiedzHco3(double hco3, double paco2){
        // sprawdzić czy math.round działa dobrze, czy wywalić to *10/10
        return Math.round((hco3- 24) / ((paco2 - 40) / 10))*10/10;
    }
    static double dajPrzewidywanePaco2Zasadowe (double hco3){
        return (0.7* (hco3 - 24)) + 40;
    }
    static double dajOdpowiedzHco3Zasadowe (double hco3, double paco2){
        return Math.round((24 - hco3) / ((40 - paco2) / 10))*10/10;
    }


    static String kwasica (double hco3, double paco2, double ag){
        double przewidywanePaco2 = dajPrzewidywanePaco2(hco3);
        double odpowiedzHco3 = dajOdpowiedzHco3(hco3, paco2);

        if ( hco3 < 22 ){
            System.out.println("kwasica metaboliczna");

            if (paco2 < (przewidywanePaco2 -2) ){
                System.out.println("kwasica metaboliczna i zasadowica oddechowa");
                return "kwasica metaboliczna i zasadowica oddechowa";
            }

            else if (paco2 > (przewidywanePaco2 +2) ){
                System.out.println("kwasica metaboliczna i kwasica oddechowa");
                return "kwasica metaboliczna i kwasica oddechowa";
            }

            else {
                //
                //sprawdzić wartości AG
                if (ag <16){
                    System.out.println("kwasica metaboliczna");
                    return "kwasica metaboliczna";
                }
                else {
                    System.out.println("kwasica metaboliczna z wysoką luką anionową");

                    return "kwasica metaboliczna z wysoką luką anionową";
                }

            }
        }
        else if (paco2 > 42){

            System.out.println("kwasica oddechowa");

            if (odpowiedzHco3 == 1){
                System.out.println("ostra kwasica oddechowa");
                return "ostra kwasica oddechowa";
            }

            else if (odpowiedzHco3 < 1){
                System.out.println("kwasica oddechowa i metaboliczna");
                return "kwasica oddechowa i metaboliczna";
            }

            else if (odpowiedzHco3 >= 4 && odpowiedzHco3 <= 5 ){
                System.out.println("przewlekła kwasica oddechowa");
                return "przewlekła kwasica oddechowa";
            }

            else if (odpowiedzHco3 > 5 ){
                System.out.println("kwasica oddechowa i zasadowica metaboliczna");
                return "kwasica oddechowa i zasadowica metaboliczna";
            }

        }

        return "nieprawidłowe dane";

    }

    static String zasadowica (double hco3, double paco2, double ag){
        double przewidywanePaco2Zasadowe = dajPrzewidywanePaco2Zasadowe(hco3);
        double odpowiedzHco3Zasadowe = dajOdpowiedzHco3Zasadowe(hco3, paco2);

        if (hco3 > 26){
            System.out.println("zasadowica metaboliczna");

            if (paco2 < przewidywanePaco2Zasadowe-2){
                System.out.println("zasadowica metaboliczna i oddechowa");
                return "zasadowica metaboliczna i oddechowa";
            }

            else if (paco2 > przewidywanePaco2Zasadowe+2){
                System.out.println("zasadowica metaboliczna i kwasica oddechowa");
                return "zasadowica metaboliczna i kwasica oddechowa";
            }

            else {
                System.out.println("kwasica metaboliczna");
                return "zasadowica metaboliczna";
            }
        }


        else if (paco2 < 38){
            System.out.println("zasadowica oddechowa");

            if (odpowiedzHco3Zasadowe == 2){
                System.out.println("ostra zasadowica oddechowa");
                return "ostra zasadowica oddechowa";
            }

            else if (odpowiedzHco3Zasadowe < 2){
                System.out.println("zasadowica oddechowa i zasadowica metaboliczna");
                return "zasadowica oddechowa i zasadowica metaboliczna";
            }

            else if (odpowiedzHco3Zasadowe > 4 && odpowiedzHco3Zasadowe < 5){
                System.out.println("przewlekłą zasadowica oddechowa");
                return "przewlekła zasadowica oddechowa";
            }

            else if (odpowiedzHco3Zasadowe > 5){
                System.out.println("zasadowica oddechowa i kwasica metaboliczna");
                return "zasadowica oddechowa i kwasica metaboliczna";
            }
            else {
                System.out.println("zasadowica oddechowa");
                return "zasadowica oddechowa";
            }
        }


        return "nieprawidłowe dane";
    }









    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    //
    //                      SBE
    //
    //
    //
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    static String metodaNadrzednaSbe (double ph, double sbe, double paco2, double hco3, double ag){
        if(ph < 7.38){
            return kwasicaSbe (sbe, paco2, ag, hco3);

        }
        else if(ph > 7.42){
            return zasadowicaSbe (sbe, paco2, ag);
        }

        else{
            return "pH w normie";
        }

    }






    static String kwasicaSbe (double sbe, double paco2, double ag, double hco3){

        if ( sbe < -2){
            System.out.println("kwasica metaboliczna");

            if (Math.abs(paco2 - 40) < sbe){
                System.out.println("kwasica metaboliczna i zasadowica oddechowa");
                return "kwasica metaboliczna i zasadowica oddechowa";
            }

            else if (Math.abs(paco2 - 40) > sbe ){
                System.out.println("kwasica metaboliczna i kwasica oddechowa");
                return "kwasica metaboliczna i kwasica oddechowa";
            }

            else {

                if (ag <16){
                    System.out.println("kwasica metaboliczna");
                    return "kwasica metaboliczna";

                }
                else {
                    System.out.println("kwasica metaboliczna z wysoką luką anionową");


                    if(ag-12 / 24-hco3 == 1){
                        return "kwasica metaboliczna z wysoką luką anionową";
                    }
                    else if (ag-12 / 24-hco3 < 1){
                        return "kwasica metaboliczna";
                    }
                    else{
                        return "kwasica metaboliczna i zasadowica metaboliczna";
                    }



                }

            }

        }
        else if (paco2 > 42){

            System.out.println("kwasica oddechowa");

            if (sbe >= -2 && sbe <=2){
                System.out.println("ostra kwasica oddechowa");
                return "ostra kwasica oddechowa";
            }

            else if (sbe < 0.4*(paco2 - 40)){

                System.out.println("kwasica oddechowa i metaboliczna");
                return "kwasica oddechowa metaboliczna";

            }

            else if (sbe == 0.4*(paco2 - 40)){
                System.out.println("przewlekła kwasica oddechowa");
                return "przewlekła kwasica oddechowa";
            }

            else if (sbe > 0.4*(paco2 - 40)){
                System.out.println("kwasica oddechowa i zasadowica metaboliczna");
                return "kwasica oddechowa i zasadowica metaboliczna";
            }

        }

        return "nieprawidłowe dane";

    }


    static String zasadowicaSbe (double sbe, double paco2, double ag){

        if ( sbe > 2){
            System.out.println("zasadowica metaboliczna");

            if (paco2 < (0.6 * sbe)-2){
                System.out.println("zasadowica metaboliczna i oddechowa");
                return "zasadowica metaboliczna i oddechowa";
            }

            else if (paco2 > (-0.6 * sbe)+2){
                System.out.println("zasadowica metaboliczna i kwasica oddechowa");
                return "zasadowica metaboliczna i kwasica oddechowa";
            }

            else {
                System.out.println("zasadowica metaboliczna");
                return "zasasadowica metaboliczna";
            }
        }


        else if (paco2 < 38){
            System.out.println("zasadowica oddechowa");

            if (sbe >= -2 && sbe <= 2){
                System.out.println("ostra zasadowica oddechowa");
                return "ostra zasadowica oddechowa";
            }

            else if (sbe > 2){
                System.out.println("zasadowica oddechowa i zasadowica metaboliczna");
                return "zasadowica oddechowa i zasadowica metaboliczna";
            }

            else if ( sbe == 0.4 * (paco2 - 40)){
                System.out.println("przewlekłą zasadowica oddechowa");
                return "przewlekła zasadowica oddechowa";
            }

            else if (sbe < 0.4 * (paco2 - 40)){
                System.out.println("zasadowica oddechowa i kwasica metaboliczna");
                return "zasadowica oddechowa i kwasica metaboliczna";
            }

            else {
                return "zasadowica oddechowa";
            }

        }


        return "nieprawidłowe dane";
    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wprowadzanie_danych);

        Button buttonMenu = (Button) findViewById(R.id.buttonMenu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityWprowadzanieDanych.this, ActivityMenu.class);
                startActivity(intent);
            }
        });


        Button buttonDiagnoza = (Button) findViewById(R.id.buttonDiagnoza);
        buttonDiagnoza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText editTextPh = (EditText) findViewById(R.id.editTextPh);
                EditText editTextPaco2 = (EditText) findViewById(R.id.editTextPaco2);
                EditText editTextHco3 = (EditText) findViewById(R.id.editTextHco3);
                EditText editTextAg = (EditText) findViewById(R.id.editTextAg);
                EditText editTextAlb = (EditText) findViewById(R.id.editTextAlb);
                EditText editTextSbe = (EditText) findViewById(R.id.editTextSbe);
                /*EditText editTextImie = (EditText) findViewById(R.id.editTextImie);
                    String Imie = editTextImie.getText().toString();
                EditText editTextNazwisko = (EditText) findViewById(R.id.editTextNazwisko);
                    String Nazwisko = editTextNazwisko.getText().toString();
                */


                if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 && editTextSbe.length() !=0 ){
                    //Toast.makeText(ActivityWprowadzanieDanych.this, "obliczanie metodą fizjologiczną i z AGc", Toast.LENGTH_SHORT).show();
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());
                    double sbe = Double.parseDouble(editTextSbe.getText().toString());

                    double agc= dajAgc(alb, ag);


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag ));
                    //intentPrzejdzDoDiagnoz1.putExtra("4", Imie);
                    //intentPrzejdzDoDiagnoz1.putExtra("5", Nazwisko);

                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 && editTextSbe.length() !=0 ){
                    //Toast.makeText(ActivityWprowadzanieDanych.this, "obliczanie metodą fizjologiczną i z AGc", Toast.LENGTH_SHORT).show();
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());
                    double sbe = Double.parseDouble(editTextSbe.getText().toString());

                    double agc= dajAgc(alb, ag);


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag ));
                    //intentPrzejdzDoDiagnoz1.putExtra("4", Imie);

                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 && editTextSbe.length() !=0 ){
                    //Toast.makeText(ActivityWprowadzanieDanych.this, "obliczanie metodą fizjologiczną i z AGc", Toast.LENGTH_SHORT).show();
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());
                    double sbe = Double.parseDouble(editTextSbe.getText().toString());

                    double agc= dajAgc(alb, ag);


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag ));
                    //intentPrzejdzDoDiagnoz1.putExtra("5", Nazwisko);

                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 && editTextSbe.length() !=0 ){
                    //Toast.makeText(ActivityWprowadzanieDanych.this, "obliczanie metodą fizjologiczną i z AGc", Toast.LENGTH_SHORT).show();
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());
                    double sbe = Double.parseDouble(editTextSbe.getText().toString());

                    double agc= dajAgc(alb, ag);


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag ));

                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());

                    double agc= dajAgc(alb, ag);


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    //intentPrzejdzDoDiagnoz1.putExtra("4", Imie);
                    //intentPrzejdzDoDiagnoz1.putExtra("5", Nazwisko);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());

                    double agc= dajAgc(alb, ag);


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    //intentPrzejdzDoDiagnoz1.putExtra("4", Imie);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());

                    double agc= dajAgc(alb, ag);


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    //intentPrzejdzDoDiagnoz1.putExtra("5", Nazwisko);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextSbe.length() !=0 ) {
                    //Toast.makeText(ActivityWprowadzanieDanych.this, "obliczanie metodą fizjologiczną i z AGc", Toast.LENGTH_SHORT).show();
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double sbe = Double.parseDouble(editTextSbe.getText().toString());


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag));
                    //intentPrzejdzDoDiagnoz1.putExtra("4", Imie);
                    //intentPrzejdzDoDiagnoz1.putExtra("5", Nazwisko);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextSbe.length() !=0 ) {
                    //Toast.makeText(ActivityWprowadzanieDanych.this, "obliczanie metodą fizjologiczną i z AGc", Toast.LENGTH_SHORT).show();
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double sbe = Double.parseDouble(editTextSbe.getText().toString());


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag));
                    //intentPrzejdzDoDiagnoz1.putExtra("4", Imie);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextSbe.length() !=0 ) {
                    //Toast.makeText(ActivityWprowadzanieDanych.this, "obliczanie metodą fizjologiczną i z AGc", Toast.LENGTH_SHORT).show();
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double sbe = Double.parseDouble(editTextSbe.getText().toString());


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag));
                    //intentPrzejdzDoDiagnoz1.putExtra("5", Nazwisko);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextSbe.length() !=0 ) {
                    //Toast.makeText(ActivityWprowadzanieDanych.this, "obliczanie metodą fizjologiczną i z AGc", Toast.LENGTH_SHORT).show();
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double sbe = Double.parseDouble(editTextSbe.getText().toString());


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag));
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());

                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    //intentPrzejdzDoDiagnoz1.putExtra("4", Imie);
                    //intentPrzejdzDoDiagnoz1.putExtra("5", Nazwisko);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());

                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    //intentPrzejdzDoDiagnoz1.putExtra("4", Imie);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());

                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    //intentPrzejdzDoDiagnoz1.putExtra("5", Nazwisko);
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());

                    double agc= dajAgc(alb, ag);


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    startActivity(intentPrzejdzDoDiagnoz1);

                }

                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());


                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));;
                    startActivity(intentPrzejdzDoDiagnoz1);

                }


                else {
                    if(editTextPh.length() ==0)
                        editTextPh.setError("nie wprowadzono pH ");
                    if(editTextPaco2.length() ==0)
                        editTextPaco2.setError("nie wprowadzono PaCO2 ");
                    if(editTextHco3.length() ==0)
                        editTextHco3.setError("nie wprowadzono HCO3 ");
                    if(editTextAg.length() ==0)
                        editTextAg.setError("nie wprowadzono Ag ");
                }



            }
        });

    }
}
