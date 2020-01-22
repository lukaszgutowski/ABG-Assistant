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
        if(ph < 7.36){
            return kwasicaAgc (hco3, paco2, agc);

        }
        else if(ph > 7.44){
            return zasadowicaAgc (hco3, paco2, agc);
        }

        else{
            return normatywneAgc (hco3, paco2, agc);
        }

    }


    static String kwasicaAgc (double hco3, double paco2, double agc){
        double przewidywanePaco2 = dajPrzewidywanePaco2(hco3);
        double odpowiedzHco3 = dajOdpowiedzHco3(hco3, paco2);
        double gapGapRatio = dajGapGapRatio(agc, hco3);

        if ( paco2 <= 44){

            if ( hco3 >= 22 ){
                return "error";
            }
            else {
                //metabolic acidosis, checking if pCO2 response is accurate

                if (paco2 < ( (przewidywanePaco2 -2) - (przewidywanePaco2 * 0.05) )  ){
                    // System.out.println("kwasica metaboliczna i zasadowica oddechowa");

                    if (agc <= 11){
                        return "Nagma + respiratory alkalosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "Hagma, respiratory alkalosis, probably also Nagma ";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                            return "Hagma, respiratory alkalosis, possible Nagma";
                        }

                        else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                            return "Hagma + respiratory alkalosis";
                        }

                        else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                            return "Hagma, respiratory alkalosis, possible metabolic alkalosis";
                        }

                        else {
                            return "Hagma, respiratory alkalosis, probably also metabolic alkalosis";
                        }
                    }
                }

                else if (paco2 > ( (przewidywanePaco2 +2) + (przewidywanePaco2 * 0.05) ) ){
                    // System.out.println("kwasica metaboliczna i kwasica oddechowa");

                    if (agc <= 11){
                        return "Nagma + respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "Hagma, respiratory acidosis, probably also Nagma ";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                            return "Hagma, respiratory acidosis, possible Nagma";
                        }

                        else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                            return "Hagma + respiratory acidosis";
                        }

                        else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                            return "Hagma, respiratory acidosis, possible metabolic alkalosis";
                        }

                        else {
                            return "Hagma, respiratory acidosis, probably also metabolic alkalosis";
                        }
                    }

                }

                else {
                    //System.out.println(czysta kwasica metaboliczna);
                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "Hagma, probably also Nagma ";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                            return "Hagma, possible Nagma";
                        }

                        else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                            return "Hagma";
                        }

                        else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                            return "Hagma, possible metabolic alkalosis";
                        }

                        else {
                            return "Hagma, probably also metabolic alkalosis";
                        }
                    }

                }

            }



        }


        else {
            //czyli gdy paCO2 > 44
            //System.out.println("kwasica oddechowa");


            if ( hco3 < 22){
                //System.out.println("kwasica oddechowa i metaboliczna");

                if (agc <= 11){
                    return "Nagma + respiratory acidosis";
                    //TODO może warto byłoby tu odwrócić kolejność w przyszłości - najpierw kw oddechowa
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8){
                        return "Hagma, respiratory acidosis, probably also Nagma ";
                    }

                    else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                        return "Hagma, respiratory acidosis, possible Nagma";
                    }

                    else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                        return "Hagma + respiratory acidosis";
                    }

                    else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                        return "Hagma, respiratory acidosis, possible metabolic alkalosis";
                    }

                    else {
                        return "Hagma, respiratory acidosis, probably also metabolic alkalosis";
                    }
                }

            }

            else {
                //tutaj sprwdzam oczekiwane paCO2 i porównuję z wpisanym


                //TODO sprawdzić czy zaspany nie pomyliłem.spytac Kai o poprawność matematyczną rozwiązania
                if (odpowiedzHco3  < 1 - (odpowiedzHco3 * 0.05) ){
                    //System.out.println("kwasica oddechowa i kwasica metaboliczna");
                    //return "kwasica oddechowa i kwasica metaboliczna";

                    if (agc <= 11){
                        return "Nagma + respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "Hagma, respiratory acidosis, probably also Nagma ";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                            return "Hagma, respiratory acidosis, possible Nagma";
                        }

                        else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                            return "Hagma + respiratory acidosis";
                        }

                        else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                            return "Hagma, respiratory acidosis, possible metabolic alkalosis";
                        }

                        else {
                            return "Hagma, respiratory acidosis, probably also metabolic alkalosis";
                        }
                    }


                }
                else if (odpowiedzHco3 >= 1 - (odpowiedzHco3 * 0.05) && odpowiedzHco3 <= 1 + (odpowiedzHco3 * 0.05)){
                    //System.out.println("ostra kwasica oddechowa");
                    return "ostra kwasica oddechowa";
                }

                else if (odpowiedzHco3 >= 1 + (odpowiedzHco3 * 0.05) && odpowiedzHco3 < 4) {
                    //System.out.println("kwasica oddechowa");
                    return "kwasica oddechowa";
                }

                else if (odpowiedzHco3 >= 4 && odpowiedzHco3 <= 5 + (odpowiedzHco3 * 0.05)) {
                    //System.out.println("przewlekła kwasica oddechowa");
                    return "przewlekła kwasica oddechowa";
                }

                else {
                    return "kwasica oddechowa i zasadowica metaboliczna";
                }

            }
        }

        // return "wrong input data";

    }

    static String zasadowicaAgc (double hco3, double paco2, double agc) {
        double przewidywanePaco2Zasadowe = dajPrzewidywanePaco2Zasadowe(hco3);
        double odpowiedzHco3Zasadowe = dajOdpowiedzHco3Zasadowe(hco3, paco2);
        double gapGapRatio = dajGapGapRatio(agc, hco3);

        if (paco2 < 36) {

            if (hco3 > 26) {
                return "respiratory and metabolic alkalosis";
            } else {
                //zasadowica oddechowa, teraz sprawdzam kompensację
                //czyli parametry <= 26

                if (odpowiedzHco3Zasadowe < 2 - (odpowiedzHco3Zasadowe * 0.05)) {
                    //System.out.println("zasadowica oddechowa i kwasica metaboliczna");
                    //return "zasadowica oddechowa i kwasica metaboliczna";

                    if (agc <= 11) {
                        return "Nagma + respiratory alkalosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    } else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8) {
                            return "Hagma, respiratory alkalosis, probably also Nagma ";
                        } else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99) {
                            return "Hagma, respiratory alkalosis, possible Nagma";
                        } else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01) {
                            return "Hagma + respiratory alkalosis";
                        } else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2) {
                            return "Hagma, respiratory alkalosis, possible metabolic alkalosis";
                        } else {
                            return "Hagma, respiratory alkalosis, probably also metabolic alkalosis";
                        }
                    }


                } else if (odpowiedzHco3Zasadowe >= 2 - (odpowiedzHco3Zasadowe * 0.05) && odpowiedzHco3Zasadowe <= 2 + (odpowiedzHco3Zasadowe * 0.05)) {
                    //System.out.println("ostra kwasica oddechowa");
                    return "acute respiratory alkalosis";
                } else if (odpowiedzHco3Zasadowe >= 2 + (odpowiedzHco3Zasadowe * 0.05) && odpowiedzHco3Zasadowe < 4) {
                    //System.out.println("kwasica oddechowa");
                    return "respiratory alkalosis ";
                } else if (odpowiedzHco3Zasadowe >= 4 && odpowiedzHco3Zasadowe <= 5 + (odpowiedzHco3Zasadowe * 0.05)) {
                    //System.out.println("przewlekła kwasica oddechowa");
                    return "chronic respiratory alkalosis";
                } else {
                    return "respiratory alkalosis and metabolic alkalosis";
                }

            }

        }

        else {   // czyli paCO2 >=36
            if (hco3 <= 26) {
                return "error";
            } else {
                //zas metaboliczna


                if (paco2 < ((przewidywanePaco2Zasadowe - 2) - (przewidywanePaco2Zasadowe * 0.05))) {
                    return "metabolic and respiratory alkalosis";
                } else if (paco2 >= ((przewidywanePaco2Zasadowe - 2) - (przewidywanePaco2Zasadowe * 0.05)) && paco2 <= ((przewidywanePaco2Zasadowe - 2) - (przewidywanePaco2Zasadowe * 0.05))) {
                    return "metabolic alkalosis";
                } else {   //czyli paCO2 > ocz paco2
                    return "metabolic alkalosis and respiratory acidosis";
                }
            }
        }
    }

    static String normatywneAgc (double hco3, double paco2, double agc) {
        double przewidywanePaco2 = dajPrzewidywanePaco2(hco3);
        double odpowiedzHco3 = dajOdpowiedzHco3(hco3, paco2);
        double gapGapRatio = dajGapGapRatio(agc, hco3);

        if (paco2 < 36){
            if (hco3 < 22){
                //zas odd i kw met

                if (agc <= 11) {
                    return "Nagma + respiratory alkalosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "Hagma, respiratory alkalosis, probably also Nagma ";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99) {
                        return "Hagma, respiratory alkalosis, possible Nagma";
                    } else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01) {
                        return "Hagma + respiratory alkalosis";
                    } else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2) {
                        return "Hagma, respiratory alkalosis, possible metabolic alkalosis";
                    } else {
                        return "Hagma, respiratory alkalosis, probably also metabolic alkalosis";
                    }
                }
            }
            else {   // hco3 >= 22
                return "error";
            }
        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (hco3 >= 22 && hco3 <= 26){

                if (agc <= 11) {
                    return "everything is OK. To be sure check if there is no electrolytes abnormality";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "Hagma, probably also Nagma ";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99) {
                        return "Hagma, possible Nagma";
                    } else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01) {
                        return "Hagma ";
                    } else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2) {
                        return "Hagma, possible metabolic alkalosis";
                    } else {
                        return "Hagma, probably also metabolic alkalosis";
                    }
                }
            }

            else {  //hco3 != <22-26>
                return "error";
            }
        }

        else {       //czyli paco2 >44

            if (hco3 > 26){
                return "respiratory acidosis and metabolic alkalosis";
            }

            else {
                return "error";
            }
        }
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
        if(ph < 7.36){
            return kwasica (hco3, paco2, ag);

        }
        else if(ph > 7.44){
            return zasadowica (hco3, paco2, ag);
        }

        else{
            return normatywne (hco3, paco2, ag);
        }

    }

    static double dajPrzewidywanePaco2 (double hco3){
        return 1.5 * hco3 + 8;
    }
    static double dajOdpowiedzHco3(double hco3, double paco2){
        //TODO sprawdzić czy math.abs działa dobrze, czy kombinować dalej
        return Math.round (Math.abs(24 - hco3) / (Math.abs(paco2 - 40) / 10))*10/10;
    }
    static double dajPrzewidywanePaco2Zasadowe (double hco3){
        return (0.7* (hco3 - 24)) + 40;
    }
    static double dajOdpowiedzHco3Zasadowe (double hco3, double paco2){
        return Math.round((hco3 - 24) / ((paco2 -40) / 10))*10/10;
        // długo rozkminiałem co ja tu wymyśliłem. Ale jest dobrze: wyliczam delta hco3 i delta paco2. Czyli wiem ile się zmieiniły od wartości idealnej
        // porównoje ich stosunek (ale paCO2 zmniejszam 10x, bo musi być stosunek 1:10) Teraz wynik równania =1 oznacza, że na 1 HCO3 jest 10 paCO2.
    }
    static double dajGapGapRatio (double ag, double hco3){
        return Math.abs(ag - 7) / Math.abs(24 - hco3);
    }


    static String kwasica (double hco3, double paco2, double ag){
        double przewidywanePaco2 = dajPrzewidywanePaco2(hco3);
        double odpowiedzHco3 = dajOdpowiedzHco3(hco3, paco2);
        double gapGapRatio = dajGapGapRatio(ag, hco3);

        if ( paco2 <= 44){

            if ( hco3 >= 22 ){
                return "error";
            }
            else {
                //metabolic acidosis, checking if pCO2 response is accurate

                if (paco2 < ( (przewidywanePaco2 -2) - (przewidywanePaco2 * 0.05) )  ){
                    // System.out.println("kwasica metaboliczna i zasadowica oddechowa");

                    if (ag <= 11){
                        return "Nagma + respiratory alkalosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "Hagma, respiratory alkalosis, probably also Nagma ";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                            return "Hagma, respiratory alkalosis, possible Nagma";
                        }

                        else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                            return "Hagma + respiratory alkalosis";
                        }

                        else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                            return "Hagma, respiratory alkalosis, possible metabolic alkalosis";
                        }

                        else {
                            return "Hagma, respiratory alkalosis, probably also metabolic alkalosis";
                        }
                    }
                }

                else if (paco2 > ( (przewidywanePaco2 +2) + (przewidywanePaco2 * 0.05) ) ){
                    // System.out.println("kwasica metaboliczna i kwasica oddechowa");

                    if (ag <= 11){
                        return "Nagma + respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "Hagma, respiratory acidosis, probably also Nagma ";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                            return "Hagma, respiratory acidosis, possible Nagma";
                        }

                        else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                            return "Hagma + respiratory acidosis";
                        }

                        else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                            return "Hagma, respiratory acidosis, possible metabolic alkalosis";
                        }

                        else {
                            return "Hagma, respiratory acidosis, probably also metabolic alkalosis";
                        }
                    }

                }

                else {
                    //System.out.println(czysta kwasica metaboliczna);
                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "Hagma, probably also Nagma ";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                            return "Hagma, possible Nagma";
                        }

                        else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                            return "Hagma";
                        }

                        else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                            return "Hagma, possible metabolic alkalosis";
                        }

                        else {
                            return "Hagma, probably also metabolic alkalosis";
                        }
                    }

                }

            }



        }


        else {
            //czyli gdy paCO2 > 44
            //System.out.println("kwasica oddechowa");


            if ( hco3 < 22){
                //System.out.println("kwasica oddechowa i metaboliczna");

                if (ag <= 11){
                    return "Nagma + respiratory acidosis";
                    //TODO może warto byłoby tu odwrócić kolejność w przyszłości - najpierw kw oddechowa
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8){
                        return "Hagma, respiratory acidosis, probably also Nagma ";
                    }

                    else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                        return "Hagma, respiratory acidosis, possible Nagma";
                    }

                    else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                        return "Hagma + respiratory acidosis";
                    }

                    else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                        return "Hagma, respiratory acidosis, possible metabolic alkalosis";
                    }

                    else {
                        return "Hagma, respiratory acidosis, probably also metabolic alkalosis";
                    }
                }

            }

            else {
                //tutaj sprwdzam oczekiwane paCO2 i porównuję z wpisanym


                //TODO sprawdzić czy zaspany nie pomyliłem.spytac Kai o poprawność matematyczną rozwiązania
                if (odpowiedzHco3  < 1 - (odpowiedzHco3 * 0.05) ){
                    //System.out.println("kwasica oddechowa i kwasica metaboliczna");
                    //return "kwasica oddechowa i kwasica metaboliczna";

                    if (ag <= 11){
                        return "Nagma + respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "Hagma, respiratory acidosis, probably also Nagma ";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99){
                            return "Hagma, respiratory acidosis, possible Nagma";
                        }

                        else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01){
                            return "Hagma + respiratory acidosis";
                        }

                        else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2){
                            return "Hagma, respiratory acidosis, possible metabolic alkalosis";
                        }

                        else {
                            return "Hagma, respiratory acidosis, probably also metabolic alkalosis";
                        }
                    }


                }
                else if (odpowiedzHco3 >= 1 - (odpowiedzHco3 * 0.05) && odpowiedzHco3 <= 1 + (odpowiedzHco3 * 0.05)){
                    //System.out.println("ostra kwasica oddechowa");
                    return "ostra kwasica oddechowa";
                }

                else if (odpowiedzHco3 >= 1 + (odpowiedzHco3 * 0.05) && odpowiedzHco3 < 4) {
                    //System.out.println("kwasica oddechowa");
                    return "kwasica oddechowa";
                }

                else if (odpowiedzHco3 >= 4 && odpowiedzHco3 <= 5 + (odpowiedzHco3 * 0.05)) {
                    //System.out.println("przewlekła kwasica oddechowa");
                    return "przewlekła kwasica oddechowa";
                }

                else {
                    return "kwasica oddechowa i zasadowica metaboliczna";
                }

            }
        }

        // return "wrong input data";

    }

    static String zasadowica (double hco3, double paco2, double ag) {
        double przewidywanePaco2Zasadowe = dajPrzewidywanePaco2Zasadowe(hco3);
        double odpowiedzHco3Zasadowe = dajOdpowiedzHco3Zasadowe(hco3, paco2);
        double gapGapRatio = dajGapGapRatio(ag, hco3);

        if (paco2 < 36) {

            if (hco3 > 26) {
                return "respiratory and metabolic alkalosis";
            } else {
                //zasadowica oddechowa, teraz sprawdzam kompensację
                //czyli parametry <= 26

                if (odpowiedzHco3Zasadowe < 2 - (odpowiedzHco3Zasadowe * 0.05)) {
                    //System.out.println("zasadowica oddechowa i kwasica metaboliczna");
                    //return "zasadowica oddechowa i kwasica metaboliczna";

                    if (ag <= 11) {
                        return "Nagma + respiratory alkalosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    } else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8) {
                            return "Hagma, respiratory alkalosis, probably also Nagma ";
                        } else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99) {
                            return "Hagma, respiratory alkalosis, possible Nagma";
                        } else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01) {
                            return "Hagma + respiratory alkalosis";
                        } else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2) {
                            return "Hagma, respiratory alkalosis, possible metabolic alkalosis";
                        } else {
                            return "Hagma, respiratory alkalosis, probably also metabolic alkalosis";
                        }
                    }


                } else if (odpowiedzHco3Zasadowe >= 2 - (odpowiedzHco3Zasadowe * 0.05) && odpowiedzHco3Zasadowe <= 2 + (odpowiedzHco3Zasadowe * 0.05)) {
                    //System.out.println("ostra kwasica oddechowa");
                    return "acute respiratory alkalosis";
                } else if (odpowiedzHco3Zasadowe >= 2 + (odpowiedzHco3Zasadowe * 0.05) && odpowiedzHco3Zasadowe < 4) {
                    //System.out.println("kwasica oddechowa");
                    return "respiratory alkalosis ";
                } else if (odpowiedzHco3Zasadowe >= 4 && odpowiedzHco3Zasadowe <= 5 + (odpowiedzHco3Zasadowe * 0.05)) {
                    //System.out.println("przewlekła kwasica oddechowa");
                    return "chronic respiratory alkalosis";
                } else {
                    return "respiratory alkalosis and metabolic alkalosis";
                }

            }

        }

        else {   // czyli paCO2 >=36
            if (hco3 <= 26) {
                return "error";
            } else {
                //zas metaboliczna


                if (paco2 < ((przewidywanePaco2Zasadowe - 2) - (przewidywanePaco2Zasadowe * 0.05))) {
                    return "metabolic and respiratory alkalosis";
                } else if (paco2 >= ((przewidywanePaco2Zasadowe - 2) - (przewidywanePaco2Zasadowe * 0.05)) && paco2 <= ((przewidywanePaco2Zasadowe - 2) - (przewidywanePaco2Zasadowe * 0.05))) {
                    return "metabolic alkalosis";
                } else {   //czyli paCO2 > ocz paco2
                    return "metabolic alkalosis and respiratory acidosis";
                }
            }
        }
    }

    static String normatywne (double hco3, double paco2, double ag) {
        double przewidywanePaco2 = dajPrzewidywanePaco2(hco3);
        double odpowiedzHco3 = dajOdpowiedzHco3(hco3, paco2);
        double gapGapRatio = dajGapGapRatio(ag, hco3);

        if (paco2 < 36){
            if (hco3 < 22){
                //zas odd i kw met

                if (ag <= 11) {
                    return "Nagma + respiratory alkalosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "Hagma, respiratory alkalosis, probably also Nagma ";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99) {
                        return "Hagma, respiratory alkalosis, possible Nagma";
                    } else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01) {
                        return "Hagma + respiratory alkalosis";
                    } else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2) {
                        return "Hagma, respiratory alkalosis, possible metabolic alkalosis";
                    } else {
                        return "Hagma, respiratory alkalosis, probably also metabolic alkalosis";
                    }
                }
            }
            else {   // hco3 >= 22
                return "error";
            }
        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (hco3 >= 22 && hco3 <= 26){

                if (ag <= 11) {
                    return "everything is OK. To be sure check if there is no electrolytes abnormality";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "Hagma, probably also Nagma ";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio < 0.99) {
                        return "Hagma, possible Nagma";
                    } else if (gapGapRatio >= 0.99 && gapGapRatio <= 1.01) {
                        return "Hagma ";
                    } else if (gapGapRatio > 1.01 && gapGapRatio <= 1.2) {
                        return "Hagma, possible metabolic alkalosis";
                    } else {
                        return "Hagma, probably also metabolic alkalosis";
                    }
                }
            }

            else {  //hco3 != <22-26>
                return "error";
            }
        }

        else {       //czyli paco2 >44

            if (hco3 > 26){
                return "respiratory acidosis and metabolic alkalosis";
            }

            else {
                return "error";
            }
        }
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
        getSupportActionBar().hide();
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
                        editTextPh.setError("pH not entered ");
                    if(editTextPaco2.length() ==0)
                        editTextPaco2.setError("PaCO2 not entered ");
                    if(editTextHco3.length() ==0)
                        editTextHco3.setError("HCO3 not entered ");
                    if(editTextAg.length() ==0)
                        editTextAg.setError("AG not entered ");
                }



            }
        });

    }
}
