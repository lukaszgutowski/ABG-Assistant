package com.lukaszgutowski.rkz_0_21;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityWprowadzanieDanych extends AppCompatActivity {


    static double dajSbe ( double hco3, double ph) {
        return 0.9287 * (hco3 - 24.4 + 14.83 * (ph - 7.4));
    }



    static double dajSzacowaneHco3KwOddOstra ( double paco2) {
        double deltaPaco2 = paco2 - 40;
        return deltaPaco2 * 0.1 + 24;
    }

    static double dajSzacowaneHco3KwOddPrzewlA ( double paco2) {
        double deltaPaco2 = paco2 - 40;
        return deltaPaco2 * 0.4 + 24;
    }

    static double dajSzacowaneHco3KwOddPrzewlB ( double paco2) {
        double deltaPaco2 = paco2 - 40;
        return deltaPaco2 * 0.5 + 24;
    }

    //szacowane kompensacje dla zasadowic oddechowaych
    static double dajSzacowaneHco3ZasOddOstra ( double paco2) {
        double deltaPaco2 = 40 - paco2;
        return 24 - deltaPaco2 * 0.2;
    }

    static double dajSzacowaneHco3ZasOddPrzewlA ( double paco2) {
        double deltaPaco2 = 40 - paco2;
        return 24 - deltaPaco2 * 0.4;
    }

    static double dajSzacowaneHco3ZasOddPrzewlB ( double paco2) {
        double deltaPaco2 = 40 - paco2;
        return 24 - deltaPaco2 * 0.5;
    }


    //szacowane kompensacje dla zab metabolicznych
    static double dajPrzewidywanePaco2 (double hco3){
        return 1.5 * hco3 + 8;
    }



    static double dajPrzewidywanePaco2Zasadowe (double hco3){
        return (0.7* (hco3 - 24)) + 40;
    }





    static double dajAgc (double alb, double ag) {
        return (0.25*(40-alb)) + ag;
        // in [g/l]
    }





    /////////////////////////////////////////////////////
    /////                                           /////
    /////         Boston approach + AGc             /////
    /////                                           /////
    /////////////////////////////////////////////////////



    static String metodaNadrzednaAgc (double ph, double paco2, double hco3, double agc){

        if(ph < 6.32){
            return "incorrect pH" ;

        }
        else if(ph >8){
            return "incorrect pH" ;

        }
        else if(paco2 > 374){
            return "incorrect PaCO2" ;

        }
        else if(hco3 >150){
            return "incorrect HCO3" ;

        }

        else if  (ph < ( 6.1 + Math.log10( hco3 / (0.03 * paco2))) - 0.015 || ph > ( 6.1 + Math.log10( hco3 / (0.03 * paco2))) + 0.015 ) {
            return "Incorrect data. Henderson–Hasselbalch equation is not fulfilled.";
        }

        else if(agc < 3 ){
            return "Low anion gap - most probably caused by laboratory error. See ‘more informations’." ;

        }

        else if(ph < 7.36){
            return kwasicaAgc (hco3, paco2, agc);

        }
        else if(ph > 7.44){
            return zasadowicaAgc (hco3, paco2, agc);
        }

        else if(ph >= 7.36 && ph < 7.4){
            return normatywne1Agc (hco3, paco2, agc);
        }

        else{
            // so >=7.4 && <=7.44
            return normatywne2Agc (hco3, paco2, agc);
        }

    }





    static double dajAgcGapGapRatio (double agc, double hco3){
        if (hco3 > 23.99){
            return (agc - 7) / (0.01);
        }
        else {
            return (agc - 7) / (24 - hco3);
        }
    }





    static String kwasicaAgc (double hco3, double paco2, double agc){
        double przewidywanePaco2 = dajPrzewidywanePaco2(hco3);
        double agcGapGapRatio = dajAgcGapGapRatio(agc, hco3);
        double szacowaneHco3KwOddOstra = dajSzacowaneHco3KwOddOstra (paco2);
        double szacowaneHco3KwOddPrzewlA = dajSzacowaneHco3KwOddPrzewlA (paco2);
        double szacowaneHco3KwOddPrzewlB = dajSzacowaneHco3KwOddPrzewlB (paco2);

        if ( paco2 <= 44){

            if ( hco3 < 22 ){


                if (paco2 < 0.95 *(przewidywanePaco2 -2) ){
                    // System.out.println("kwasica metaboliczna i zasadowica oddechowa");

                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis and respiratory alkalosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (agcGapGapRatio < 0.8 ){
                            return "high anion gap metabolic acidosis and respiratory alkalosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis and respiratory alkalosis";
                        }

                        else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis and respiratory alkalosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis and respiratory alkalosis";
                        }
                    }
                }

                if (paco2 >= 0.95 * (przewidywanePaco2 -2) && paco2 < (przewidywanePaco2 -2)){
                    // System.out.println("kwasica metaboliczna i prawdopodobnie zasadowica oddechowa");

                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis, probably with respiratory alkalosis";

                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (agcGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis";
                        }

                        else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis, probably with respiratory alkalosis";
                        }
                    }
                }

                else if (paco2 > 1.05 * (przewidywanePaco2 + 2) ){
                    // System.out.println("kwasica metaboliczna i kwasica oddechowa");

                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis and respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (agcGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis and respiratory acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis and respiratory acidosis";
                        }

                        else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis and respiratory acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis and respiratory acidosis";
                        }
                    }

                }

                else if (paco2 > (przewidywanePaco2 + 2) && paco2 <= 1.05 * (przewidywanePaco2 + 2) ){
                    // System.out.println("kwasica metaboliczna i prawdopodobna kwasica oddechowa");

                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis, probably with respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (agcGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis";
                        }

                        else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis, probably with respiratory acidosis";
                        }
                    }

                }


                else {
                    //System.out.println(czysta kwasica metaboliczna);
                    //czyli mamy zakres paCO2 >= (przewPaco2 -2) && paCO2 <= (przewPaco2 + 2)
                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (agcGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis";
                        }

                        else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis";
                        }
                    }

                }


            }
            else {
                // czyli hco3 >= 22          wszystkie powyżej 25 równanie HH wyrzuca jako błąd niezgodny z równaniem
                //gdy zgodne z HH: kwasica oddechowa (możliwe dla max paco2 44 i hco3 24 w teroeii, w programie hco3 25 teżB przejdzei ze względu na +/- 0.02)

                return "Make sure that entered data are correct; consider presence of respiratory acidosis.";

            }



        }


        else {
            //czyli gdy paCO2 > 44
            //System.out.println("kwasica oddechowa");
            // nie ma sensu sprawdzać HCO3 - bo poniżej 22 i tak jest zaburzenie mieszane,


            //tutaj sprwdzam oczekiwane paCO2 i porównuję z wpisanym


            if  (hco3 < 0.95 * szacowaneHco3KwOddOstra) {

                //return "kwasica oddechowa i kwasica metaboliczna";

                if (agc <= 11){
                    return "respiratory acidosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8){
                        return "respiratory acidosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    }

                    else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2){
                        return "respiratory acidosis and high anion gap metabolic acidosis";
                    }

                    else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2){
                        return "respiratory acidosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    }

                    else {
                        return "respiratory acidosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            }

            if  (hco3 >= 0.95 * szacowaneHco3KwOddOstra && hco3 < (0.99 * szacowaneHco3KwOddOstra)) {

                //return "kwasica oddechowa i prawdoposobnie kwasica metaboliczna";

                if (agc <= 11){
                    return "respiratory acidosis, probably with normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    }

                    else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis";
                    }

                    else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    }

                    else {
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            }

            else if (hco3 >= (0.99 * szacowaneHco3KwOddOstra) && hco3 <= (1.01 * szacowaneHco3KwOddOstra)){
                //System.out.println("ostra kwasica oddechowa");
                return "acute respiratory acidosis";
            }

            else if (hco3 > (1.01 * szacowaneHco3KwOddOstra) && hco3 < szacowaneHco3KwOddPrzewlA) {
                //System.out.println("kwasica oddechowa");
                return "partially compensated respiratory acidosis";
            }

            else if (hco3 >=  szacowaneHco3KwOddPrzewlA && hco3 <= szacowaneHco3KwOddPrzewlB) {
                //System.out.println("przewlekła kwasica oddechowa");
                return "chronic respiratory acidosis";
            }

            else if (hco3 >=  szacowaneHco3KwOddPrzewlB && hco3 <= (1.05 *szacowaneHco3KwOddPrzewlB)) {
                //System.out.println("przewlekła kwasica oddechowa i prawdopodobnie zas metaboliczna");
                return "respiratory acidosis, probably with metabolic alkalosis";
            }

            else {
                return "respiratory acidosis and metabolic alkalosis";
            }

            //  }
        }


    }

    static String zasadowicaAgc (double hco3, double paco2, double agc) {
        double przewidywanePaco2Zasadowe = dajPrzewidywanePaco2Zasadowe(hco3);
        double agcGapGapRatio = dajAgcGapGapRatio(agc, hco3);
        double szacowaneHco3ZasOddOstra = dajSzacowaneHco3ZasOddOstra (paco2);
        double szacowaneHco3ZasOddPrzewlA = dajSzacowaneHco3ZasOddPrzewlA (paco2);
        double szacowaneHco3ZasOddPrzewlB = dajSzacowaneHco3ZasOddPrzewlB (paco2);

        if (paco2 < 36) {
            //zasadowica oddechowa, teraz sprawdzam kompensację

            if (hco3 < 0.95 * szacowaneHco3ZasOddOstra) {
                //System.out.println("zasadowica oddechowa i kwasica metaboliczna");

                if (agc <= 11) {
                    return "respiratory alkalosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis";
                    } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }
            }

            if (hco3 >= (0.95 * szacowaneHco3ZasOddOstra) && hco3 < (0.99 * szacowaneHco3ZasOddOstra)) {
                //System.out.println("zasadowica oddechowa i prawdopodobnie kwasica metaboliczna");

                if (agc <= 11) {
                    return "respiratory alkalosis , probably with normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis , possible additional Normal anion gap metabolic acidosis";
                    } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis";
                    } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            } else if (hco3 >= (0.99 * szacowaneHco3ZasOddOstra) && hco3 <= (1.01 * szacowaneHco3ZasOddOstra)) {
                //System.out.println("ostra kwasica oddechowa");
                return "acute respiratory alkalosis";
            } else if (hco3 > (1.01 * szacowaneHco3ZasOddOstra) && hco3 < szacowaneHco3ZasOddPrzewlA) {
                //System.out.println("kwasica oddechowa");
                return "partially compensated respiratory alkalosis";
            } else if (hco3 >= szacowaneHco3ZasOddPrzewlA && hco3 <= szacowaneHco3ZasOddPrzewlB) {
                //System.out.println("przewlekła kwasica oddechowa");
                return "chronic respiratory alkalosis";
            } else if (hco3 > szacowaneHco3ZasOddPrzewlB && hco3 <= (1.05 * szacowaneHco3ZasOddPrzewlB)) {
                //System.out.println("przewlekła kwasica oddechowa");
                return "respiratory alkalosis, probably with metabolic alkalosis";
            } else {
                // that is when hco3 > szacowaneHco3ZasOddPrzewlB
                return "respiratory alkalosis and metabolic alkalosis";
            }
        }




        else {   // czyli paCO2 >=36
            if (hco3 <= 26) {
                return "Make sure that entered data are correct; consider presence of respiratory and metabolic alkalosis.";



            } else {
                //zas metaboliczna, czyli powyżej >26


                if (paco2 <  0.95 * (przewidywanePaco2Zasadowe - 2) ) {
                    return "metabolic alkalosis and respiratory alkalosis";

                } else if (paco2 >= 0.95 * ((przewidywanePaco2Zasadowe - 2) ) && paco2 <   (przewidywanePaco2Zasadowe - 2)) {
                    return "metabolic alkalosis, probably with respiratory alkalosis";

                } else if (paco2 >= (przewidywanePaco2Zasadowe - 2)  && paco2 <= (przewidywanePaco2Zasadowe + 2)) {
                    return "metabolic alkalosis";

                } else if (paco2 >= (przewidywanePaco2Zasadowe + 2)  && paco2 <= 1.05 *(przewidywanePaco2Zasadowe + 2)) {
                    return "metabolic alkalosis, probably with respiratory acidosis";

                } else {   //czyli paCO2 > 1.05 * (ocz paco2 + 2)
                    return "metabolic alkalosis and respiratory acidosis";
                }
            }
        }
    }

    static String normatywne1Agc (double hco3, double paco2, double agc) {
        double agcGapGapRatio = dajAgcGapGapRatio(agc, hco3);

        if (paco2 < 36){
            //zas odd i kw met

            if (agc <= 11) {
                return "respiratory alkalosis and normal anion gap metabolic acidosis";
                //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
            } else {
                //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                if (agcGapGapRatio < 0.8) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis";
                } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                } else {
                    return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                }
            }

        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (hco3 >= 22 && hco3 <= 26){

                if (agc <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8) {
                        return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                        return "possible high anion gap metabolic acidosis";
                    } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                        return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "high anion gap metabolic acidosis and  metabolic alkalosis";
                    }
                }
            }

            else {  //hco3 != <22-26>
                if (agc <= 11) {
                    return "Make sure that entered data are correct; check electrolytes concentration to exclude existence acid-base disturbances.";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }

            }
        }

        else {       //czyli paco2 >44

            if (hco3 > 26){
                return "respiratory acidosis and metabolic alkalosis";
            }

            else {
                return "Make sure that entered data are correct; consider presence of respiratory acidosis and metabolic alkalosis.";


            }
        }
    }

    static String normatywne2Agc (double hco3, double paco2, double agc) {
        //czyli pH >=7.4 && <= 7.44
        double agcGapGapRatio = dajAgcGapGapRatio(agc, hco3);

        if (paco2 < 36){
            //zas odd i kw met


            if (agc <= 11) {
                return "respiratory alkalosis and normal anion gap metabolic acidosis";
                //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
            } else {
                //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                if (agcGapGapRatio < 0.8) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis";
                } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                } else {
                    return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                }
            }
            //pozostałe warianty HCO3 nie istnieją, są niezgodne z HH
        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (hco3 >= 22 && hco3 <= 26){

                if (agc <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8) {
                        return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                        return "possible high anion gap metabolic acidosis";
                    } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                        return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "high anion gap metabolic acidosis and  metabolic alkalosis";
                    }
                }
            }

            else if (hco3 < 21) {
                if (agc <= 11) {
                    return "Make sure that entered data are correct; check electrolytes concentration to exclude existence acid-base disturbances.";
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }
            }
            else if (hco3 >= 21 && hco3 < 22){
                if (agc <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (agcGapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (agcGapGapRatio >= 0.8 && agcGapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (agcGapGapRatio > 1.2 && agcGapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }

            }

            else {  //hco3 >26>
                return "Make sure that entered data are correct; consider presence of metabolic alkalosis and respiratory acidosis.";

            }
        }

        else {       //czyli paco2 >44

            return "respiratory acidosis and metabolic alkalosis";

        }
    }





    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    //
    //                      Boston approach
    //
    //
    //
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    static String metodaNadrzedna (double ph, double paco2, double hco3, double ag){

        if(ph < 6.32){
            return "incorrect pH" ;

        }
        else if(ph >8){
            return "incorrect pH" ;

        }
        else if(paco2 > 374){
            return "incorrect PaCO2" ;

        }
        else if(hco3 >150){
            return "incorrect HCO3" ;

        }

        else if  (ph < ( 6.1 + Math.log10( hco3 / (0.03 * paco2))) - 0.015 || ph > ( 6.1 + Math.log10( hco3 / (0.03 * paco2))) + 0.015 ) {
            return "Incorrect data. Henderson–Hasselbalch equation is not fulfilled.";
        }

        else if(ag < 3 ){
            return "Low anion gap. Most probably laboratory error. See ‘more informations’." ;

        }

        else if(ph < 7.36){
            return kwasica (hco3, paco2, ag);

        }
        else if(ph > 7.44){
            return zasadowica (hco3, paco2, ag);
        }

        else if(ph >= 7.36 && ph < 7.4){
            return normatywne1 (hco3, paco2, ag);
        }

        else{
            return normatywne2 (hco3, paco2, ag);
        }

    }




    static double dajGapGapRatio (double ag, double hco3){
        if (hco3 > 23.99){
            return (ag - 7) / (0.01);
        }
        else {
            return (ag - 7) / (24 - hco3);
        }
    }



    static String kwasica (double hco3, double paco2, double ag){
        double przewidywanePaco2 = dajPrzewidywanePaco2(hco3);
        double gapGapRatio = dajGapGapRatio(ag, hco3);
        double szacowaneHco3KwOddOstra = dajSzacowaneHco3KwOddOstra (paco2);
        double szacowaneHco3KwOddPrzewlA = dajSzacowaneHco3KwOddPrzewlA (paco2);
        double szacowaneHco3KwOddPrzewlB = dajSzacowaneHco3KwOddPrzewlB (paco2);

        if ( paco2 <= 44){

            if ( hco3 < 22 ){


                if (paco2 < 0.95 *(przewidywanePaco2 -2) ){
                    // System.out.println("kwasica metaboliczna i zasadowica oddechowa");

                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis and respiratory alkalosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8 ){
                            return "high anion gap metabolic acidosis and respiratory alkalosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis and respiratory alkalosis";
                        }

                        else if (gapGapRatio > 1.2 && gapGapRatio <= 2){
                            return "high anion gap metabolic acidosis and respiratory alkalosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis and respiratory alkalosis";
                        }
                    }
                }

                if (paco2 >= 0.95 * (przewidywanePaco2 -2) && paco2 < (przewidywanePaco2 -2)){
                    // System.out.println("kwasica metaboliczna i prawdopodobnie zasadowica oddechowa");

                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis, probably with respiratory alkalosis";

                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis";
                        }

                        else if (gapGapRatio > 1.2 && gapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis, probably with respiratory alkalosis";
                        }
                    }
                }

                else if (paco2 > 1.05 * (przewidywanePaco2 + 2) ){
                    // System.out.println("kwasica metaboliczna i kwasica oddechowa");

                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis and respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis and respiratory acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis and respiratory acidosis";
                        }

                        else if (gapGapRatio > 1.2 && gapGapRatio <= 2){
                            return "high anion gap metabolic acidosis and respiratory acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis and respiratory acidosis";
                        }
                    }

                }

                else if (paco2 > (przewidywanePaco2 + 2) && paco2 <= 1.05 * (przewidywanePaco2 + 2) ){
                    // System.out.println("kwasica metaboliczna i prawdopodobna kwasica oddechowa");

                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis, probably with respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis";
                        }

                        else if (gapGapRatio > 1.2 && gapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis, probably with respiratory acidosis";
                        }
                    }

                }


                else {
                    //System.out.println(czysta kwasica metaboliczna);
                    //czyli mamy zakres paCO2 >= (przewPaco2 -2) && paCO2 <= (przewPaco2 + 2)
                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis";
                        }

                        else if (gapGapRatio > 1.2 && gapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis";
                        }
                    }

                }


            }
            else {
                // czyli hco3 >= 22          wszystkie powyżej 25 równanie HH wyrzuca jako błąd niezgodny z równaniem
                //gdy zgodne z HH: kwasica oddechowa (możliwe dla max paco2 44 i hco3 24 w teroeii, w programie hco3 25 teżB przejdzei ze względu na +/- 0.02)

                return "Make sure that entered data are correct; consider presence of respiratory acidosis.";

            }



        }


        else {
            //czyli gdy paCO2 > 44
            //System.out.println("kwasica oddechowa");
            // nie ma sensu sprawdzać HCO3 - bo poniżej 22 i tak jest zaburzenie mieszane,


                //tutaj sprwdzam oczekiwane paCO2 i porównuję z wpisanym


                if  (hco3 < 0.95 * szacowaneHco3KwOddOstra) {

                    //return "kwasica oddechowa i kwasica metaboliczna";

                    if (ag <= 11){
                        return "respiratory acidosis and normal anion gap metabolic acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (gapGapRatio < 0.8){
                            return "respiratory acidosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2){
                            return "respiratory acidosis and high anion gap metabolic acidosis";
                        }

                        else if (gapGapRatio > 1.2 && gapGapRatio <= 2){
                            return "respiratory acidosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "respiratory acidosis and high anion gap metabolic acidosis and metabolic alkalosis";
                        }
                    }


                }

            if  (hco3 >= 0.95 * szacowaneHco3KwOddOstra && hco3 < (0.99 * szacowaneHco3KwOddOstra)) {

                //return "kwasica oddechowa i prawdoposobnie kwasica metaboliczna";

                if (ag <= 11){
                    return "respiratory acidosis, probably with normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    }

                    else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis";
                    }

                    else if (gapGapRatio > 1.2 && gapGapRatio <= 2){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    }

                    else {
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            }

                else if (hco3 >= (0.99 * szacowaneHco3KwOddOstra) && hco3 <= (1.01 * szacowaneHco3KwOddOstra)){
                    //System.out.println("ostra kwasica oddechowa");
                    return "acute respiratory acidosis";
                }

                else if (hco3 > (1.01 * szacowaneHco3KwOddOstra) && hco3 < szacowaneHco3KwOddPrzewlA) {
                    //System.out.println("kwasica oddechowa");
                    return "partially compensated respiratory acidosis";
                }

                else if (hco3 >=  szacowaneHco3KwOddPrzewlA && hco3 <= szacowaneHco3KwOddPrzewlB) {
                    //System.out.println("przewlekła kwasica oddechowa");
                    return "chronic respiratory acidosis";
                }

                else if (hco3 >=  szacowaneHco3KwOddPrzewlB && hco3 <= (1.05 *szacowaneHco3KwOddPrzewlB)) {
                    //System.out.println("przewlekła kwasica oddechowa i prawdopodobnie zas metaboliczna");
                    return "respiratory acidosis, probably with metabolic alkalosis";
                }

                else {
                    return "respiratory acidosis and metabolic alkalosis";
                }

          //  }
        }


    }

    static String zasadowica (double hco3, double paco2, double ag) {
        double przewidywanePaco2Zasadowe = dajPrzewidywanePaco2Zasadowe(hco3);
        double gapGapRatio = dajGapGapRatio(ag, hco3);
        double szacowaneHco3ZasOddOstra = dajSzacowaneHco3ZasOddOstra (paco2);
        double szacowaneHco3ZasOddPrzewlA = dajSzacowaneHco3ZasOddPrzewlA (paco2);
        double szacowaneHco3ZasOddPrzewlB = dajSzacowaneHco3ZasOddPrzewlB (paco2);

        if (paco2 < 36) {
            //zasadowica oddechowa, teraz sprawdzam kompensację

            if (hco3 < 0.95 * szacowaneHco3ZasOddOstra) {
                //System.out.println("zasadowica oddechowa i kwasica metaboliczna");

                if (ag <= 11) {
                    return "respiratory alkalosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }
            }

            if (hco3 >= (0.95 * szacowaneHco3ZasOddOstra) && hco3 < (0.99 * szacowaneHco3ZasOddOstra)) {
                //System.out.println("zasadowica oddechowa i prawdopodobnie kwasica metaboliczna");

                if (ag <= 11) {
                    return "respiratory alkalosis , probably with normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis , possible additional Normal anion gap metabolic acidosis";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            } else if (hco3 >= (0.99 * szacowaneHco3ZasOddOstra) && hco3 <= (1.01 * szacowaneHco3ZasOddOstra)) {
                //System.out.println("ostra kwasica oddechowa");
                return "acute respiratory alkalosis";
            } else if (hco3 > (1.01 * szacowaneHco3ZasOddOstra) && hco3 < szacowaneHco3ZasOddPrzewlA) {
                //System.out.println("kwasica oddechowa");
                return "partially compensated respiratory alkalosis";
            } else if (hco3 >= szacowaneHco3ZasOddPrzewlA && hco3 <= szacowaneHco3ZasOddPrzewlB) {
                //System.out.println("przewlekła kwasica oddechowa");
                return "chronic respiratory alkalosis";
            } else if (hco3 > szacowaneHco3ZasOddPrzewlB && hco3 <= (1.05 * szacowaneHco3ZasOddPrzewlB)) {
                //System.out.println("przewlekła kwasica oddechowa");
                return "respiratory alkalosis, probably with metabolic alkalosis";
            } else {
                // that is when hco3 > szacowaneHco3ZasOddPrzewlB
                return "respiratory alkalosis and metabolic alkalosis";
            }
        }




        else {   // czyli paCO2 >=36
            if (hco3 <= 26) {
                return "Make sure that entered data are correct; consider presence of respiratory and metabolic alkalosis.";



            } else {
                //zas metaboliczna, czyli powyżej >26


                if (paco2 <  0.95 * (przewidywanePaco2Zasadowe - 2) ) {
                    return "metabolic alkalosis and respiratory alkalosis";

                } else if (paco2 >= 0.95 * ((przewidywanePaco2Zasadowe - 2) ) && paco2 <   (przewidywanePaco2Zasadowe - 2)) {
                    return "metabolic alkalosis, probably with respiratory alkalosis";

                } else if (paco2 >= (przewidywanePaco2Zasadowe - 2)  && paco2 <= (przewidywanePaco2Zasadowe + 2)) {
                    return "metabolic alkalosis";

                } else if (paco2 >= (przewidywanePaco2Zasadowe + 2)  && paco2 <= 1.05 *(przewidywanePaco2Zasadowe + 2)) {
                    return "metabolic alkalosis, probably with respiratory acidosis";

                } else {   //czyli paCO2 > 1.05 * (ocz paco2 + 2)
                    return "metabolic alkalosis and respiratory acidosis";
                }
            }
        }
    }

    static String normatywne1 (double hco3, double paco2, double ag) {
        double gapGapRatio = dajGapGapRatio(ag, hco3);

        if (paco2 < 36){
                //zas odd i kw met

                if (ag <= 11) {
                    return "respiratory alkalosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }

        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (hco3 >= 22 && hco3 <= 26){

                if (ag <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "possible high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "high anion gap metabolic acidosis and  metabolic alkalosis";
                    }
                }
            }

            else {  //hco3 != <22-26>
                if (ag <= 11) {
                    return "Make sure that entered data are correct; check electrolytes concentration to exclude existence acid-base disturbances.";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }

            }
        }

        else {       //czyli paco2 >44

            if (hco3 > 26){
                return "respiratory acidosis and metabolic alkalosis";
            }

            else {
                return "Make sure that entered data are correct; consider presence of respiratory acidosis and metabolic alkalosis.";


            }
        }
    }


    static String normatywne2 (double hco3, double paco2, double ag) {
        //czyli pH >=7.4 && <= 7.44
        double gapGapRatio = dajGapGapRatio(ag, hco3);

        if (paco2 < 36){
            //zas odd i kw met


                if (ag <= 11) {
                    return "respiratory alkalosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }
            //pozostałe warianty HCO3 nie istnieją, są niezgodne z HH
        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (hco3 >= 22 && hco3 <= 26){

                if (ag <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "possible high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "high anion gap metabolic acidosis and  metabolic alkalosis";
                    }
                }
            }

            else if (hco3 < 21) {
                if (ag <= 11) {
                    return "Make sure that entered data are correct; check electrolytes concentration to exclude existence acid-base disturbances.";
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }
            }
            else if (hco3 >= 21 && hco3 < 22){
                if (ag <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (gapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (gapGapRatio >= 0.8 && gapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (gapGapRatio > 1.2 && gapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }

            }

            else {  //hco3 >26>
                    return "Make sure that entered data are correct; consider presence of metabolic alkalosis and respiratory acidosis.";

            }
        }

        else {       //czyli paco2 >44

                return "respiratory acidosis and metabolic alkalosis";

        }
    }








    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    //
    //                      SBE  Kopenhagen Approach
    //
    //
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    static String metodaNadrzednaSbe (double ph, double sbe, double paco2, double hco3, double ag){


        if(ph < 6.32){
            return "incorrect pH" ;

        }
        else if(ph >8){
            return "incorrect pH" ;

        }
        else if(paco2 > 374){
            return "incorrect PaCO2" ;

        }
        else if(hco3 >150){
            return "incorrect HCO3" ;

        }

        else if  (ph < ( 6.1 + Math.log10( hco3 / (0.03 * paco2))) - 0.015 || ph > ( 6.1 + Math.log10( hco3 / (0.03 * paco2))) + 0.015 ) {
            return "Incorrect data. Henderson–Hasselbalch equation is not fulfilled.";
        }

        else if(ag < 3 ){
            return "Low anion gap. Most probably laboratory error. See ‘more informations’." ;

        }

        else if(ph < 7.36){
            return kwasica (hco3, sbe, paco2, ag);

        }
        else if(ph > 7.44){
            return zasadowica (hco3, sbe, paco2, ag);
        }

        else if(ph >= 7.36 && ph < 7.4){
            return normatywne1 (hco3, sbe, paco2, ag);
        }

        else{
            return normatywne2 (hco3, sbe, paco2, ag);
        }

    }




    static double dajSbeGapGapRatio (double ag, double hco3){
        if (hco3 > 23.99){
            return (ag - 7) / (0.01);
        }
        else {
            return (ag - 7) / (24 - hco3);
        }
    }



    static String kwasica (double hco3, double sbe, double paco2, double ag){
        double sbeGapGapRatio = dajSbeGapGapRatio(ag, hco3);


        if ( paco2 <= 44){

            if ( sbe < -2 ){


                if ( Math.abs(paco2 - 40) < (0.95 *  Math.abs(sbe)) ){
                    // System.out.println("kwasica metaboliczna i zasadowica oddechowa");

                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis and respiratory alkalosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeGapGapRatio < 0.8 ){
                            return "high anion gap metabolic acidosis and respiratory alkalosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis and respiratory alkalosis";
                        }

                        else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis and respiratory alkalosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis and respiratory alkalosis";
                        }
                    }
                }

                if (Math.abs(paco2 - 40) >= (0.95 *  Math.abs(sbe)) && Math.abs(paco2 - 40) < (0.99 *  Math.abs(sbe))){
                    // System.out.println("kwasica metaboliczna i prawdopodobnie zasadowica oddechowa");

                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis, probably with respiratory alkalosis";

                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis";
                        }

                        else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis, probably with respiratory alkalosis";
                        }
                    }
                }

                else if (Math.abs(paco2 - 40) > (1.05 *  Math.abs(sbe))){
                    // System.out.println("kwasica metaboliczna i kwasica oddechowa");

                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis and respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis and respiratory acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis and respiratory acidosis";
                        }

                        else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis and respiratory acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis and respiratory acidosis";
                        }
                    }

                }

                else if (Math.abs(paco2 - 40) > (1.01 *  Math.abs(sbe)) && Math.abs(paco2 - 40) <= (1.05 *  Math.abs(sbe))){
                    // System.out.println("kwasica metaboliczna i prawdopodobna kwasica oddechowa");

                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis, probably with respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis";
                        }

                        else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis, probably with respiratory acidosis";
                        }
                    }

                }


                else {
                    //System.out.println(czysta kwasica metaboliczna);
                    //czyli mamy zakres Math.abs(paco2 - 40) > (0.99 *  Math.abs(sbe)) && Math.abs(paco2 - 40) <= (1.01 *  Math.abs(sbe)))
                    if (ag <= 11){
                        return "normal anion gap metabolic acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis";
                        }

                        else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis";
                        }
                    }

                }


            }
            else {
                // czyli sbe >= -2          wszystkie powyżej 25 równanie HH wyrzuca jako błąd niezgodny z równaniem
                //gdy zgodne z HH: kwasica oddechowa (możliwe dla max paco2 44 i hco3 24 w teorii, w programie hco3 25 teżB przejdzei ze względu na +/- 0.02)

                return "Make sure that entered data are correct; consider presence of respiratory acidosis.";

            }



        }

        //////////////////////tutaj kwasice oddechowe////////////////////////////

        else {
            //czyli gdy paCO2 > 44
            //System.out.println("kwasica oddechowa");


            if  (sbe < -2.1) {

                //return "kwasica oddechowa i kwasica metaboliczna";

                if (ag <= 11){
                    return "respiratory acidosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeGapGapRatio < 0.8){
                        return "respiratory acidosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    }

                    else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2){
                        return "respiratory acidosis and high anion gap metabolic acidosis";
                    }

                    else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2){
                        return "respiratory acidosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    }

                    else {
                        return "respiratory acidosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            }

            if  (sbe >= -2.1 && sbe < -2) {

                //return "kwasica oddechowa i prawdopodobnie kwasica metaboliczna";

                if (ag <= 11){
                    return "respiratory acidosis, probably with normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeGapGapRatio < 0.8){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    }

                    else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis";
                    }

                    else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    }

                    else {
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            }

            else if (sbe >= -2 && sbe <= 2){
                //System.out.println("ostra kwasica oddechowa");
                return "acute respiratory acidosis";
            }

            else if (sbe > 2 && sbe < 0.99*(0.4 * (paco2 - 40))) {
                //System.out.println("kwasica oddechowa");
                return "partially compensated respiratory acidosis";
            }

            else if (sbe >=  0.99*(0.4 * (paco2 - 40)) && sbe <= 1.01 * (0.4 * (paco2 - 40))) {
                //System.out.println("przewlekła kwasica oddechowa");
                return "chronic respiratory acidosis";
            }

            else if (sbe >  1.01 * (0.4 * (paco2 - 40)) && sbe <= 1.05 * (0.4 * (paco2 - 40))) {
                //System.out.println("przewlekła kwasica oddechowa i prawdopodobnie zas metaboliczna");
                return "respiratory acidosis, probably with metabolic alkalosis";
            }

            else {
                return "respiratory acidosis and metabolic alkalosis";
            }

        }


    }

    static String zasadowica (double hco3, double sbe, double paco2, double ag) {
        double sbeGapGapRatio = dajSbeGapGapRatio(ag, hco3);

        if (paco2 < 36) {
            //zasadowica oddechowa, teraz sprawdzam kompensację

            if (sbe < -2.1) {
                //System.out.println("zasadowica oddechowa i kwasica metaboliczna");

                if (ag <= 11) {
                    return "respiratory alkalosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeGapGapRatio < 0.8) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }
            }

            if (sbe >= -2.1 && sbe < -2) {
                //System.out.println("zasadowica oddechowa i prawdopodobnie kwasica metaboliczna");

                if (ag <= 11) {
                    return "respiratory alkalosis , probably with normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeGapGapRatio < 0.8) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis , possible additional Normal anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            } else if (sbe >= -2 && sbe <= 2) {
                //System.out.println("ostra zasadowica oddechowa");
                return "acute respiratory alkalosis";

            } else if (sbe > 2 && sbe < 0.99*(0.4 * (paco2 - 40))) {
                //System.out.println("zasadowica oddechowa");
                return "partially compensated respiratory alkalosis";

            } else if (sbe >=  0.99*(0.4 * (paco2 - 40)) && sbe <= 1.01 * (0.4 * (paco2 - 40))) {
                //System.out.println("przewlekła zasadowica oddechowa");
                return "chronic respiratory alkalosis";

            } else if (sbe >  1.01 * (0.4 * (paco2 - 40)) && sbe <= 1.05 * (0.4 * (paco2 - 40))) {
                //System.out.println("zas odd + prawd zas met");
                return "respiratory alkalosis, probably with metabolic alkalosis";

            } else {
                // that is when hco3 > szacowaneHco3ZasOddPrzewlB
                return "respiratory alkalosis and metabolic alkalosis";
            }
        }




        else {   // czyli paCO2 >=36
            if (sbe <= 2) {
                return "Make sure that entered data are correct; consider presence of respiratory and metabolic alkalosis.";



            } else {
                //zas metaboliczna, czyli sbe powyżej >2


                if (Math.abs(paco2 - 40) < (0.95 *  ( 0.6 * Math.abs(sbe))) ) {
                    return "metabolic alkalosis and respiratory alkalosis";

                } else if (Math.abs(paco2 - 40) >= (0.95 * ( 0.6 *  Math.abs(sbe))) && Math.abs(paco2 - 40) < (0.99 * ( 0.6 *  Math.abs(sbe)))) {
                    return "metabolic alkalosis, probably with respiratory alkalosis";

                } else if (Math.abs(paco2 - 40) >= (0.99 * ( 0.6 *  Math.abs(sbe))) && Math.abs(paco2 - 40) <= (1.01 * ( 0.6 *  Math.abs(sbe)))) {
                    return "metabolic alkalosis";

                } else if (Math.abs(paco2 - 40) > (1.01 * ( 0.6 *  Math.abs(sbe))) && Math.abs(paco2 - 40) <= (1.05 * ( 0.6 *  Math.abs(sbe)))) {
                    return "metabolic alkalosis, probably with respiratory acidosis";

                } else {   //czyli paCO2  - 40 > 1.05 *  0.6 *  Math.abs(sbe)
                    return "metabolic alkalosis and respiratory acidosis";
                }
            }
        }
    }

    static String normatywne1 (double hco3, double sbe, double paco2, double ag) {
        double sbeGapGapRatio = dajSbeGapGapRatio(ag, hco3);

        if (paco2 < 36){
            //zas odd i kw met

            if (ag <= 11) {
                return "respiratory alkalosis and normal anion gap metabolic acidosis";
                //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
            } else {
                //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                if (sbeGapGapRatio < 0.8) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis";
                } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                } else {
                    return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                }
            }

        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (sbe >= -2 && sbe <= 2){

                if (ag <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeGapGapRatio < 0.8) {
                        return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
                        return "possible high anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
                        return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "high anion gap metabolic acidosis and  metabolic alkalosis";
                    }
                }
            }

            else {  //sbe != <-2 - 2>
                if (ag <= 11) {
                    return "Make sure that entered data are correct; check electrolytes concentration to exclude existence acid-base disturbances.";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeGapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }

            }
        }

        else {       //czyli paco2 >44

            if (sbe > 2){
                return "respiratory acidosis and metabolic alkalosis";
            }

            else {
                return "Make sure that entered data are correct; consider presence of respiratory acidosis and metabolic alkalosis.";


            }
        }
    }


    static String normatywne2 (double hco3, double sbe, double paco2, double ag) {
        //czyli pH >=7.4 && <= 7.44
        double sbeGapGapRatio = dajSbeGapGapRatio(ag, hco3);

        if (paco2 < 36){
            //zas odd i kw met


            if (ag <= 11) {
                return "respiratory alkalosis and normal anion gap metabolic acidosis";
                //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
            } else {
                //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                if (sbeGapGapRatio < 0.8) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis";
                } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                } else {
                    return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                }
            }
            //pozostałe warianty HCO3 nie istnieją, są niezgodne z HH
        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (sbe >= -2 && sbe <= 2){

                if (ag <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeGapGapRatio < 0.8) {
                        return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
                        return "possible high anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
                        return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "high anion gap metabolic acidosis and  metabolic alkalosis";
                    }
                }
            }

            else if (sbe < -2) {
                if (ag <= 11) {
                    return "Make sure that entered data are correct; check electrolytes concentration to exclude existence acid-base disturbances.";
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeGapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }
            }
//            else if (hco3 >= 21 && hco3 < 22){
//                if (ag <= 11) {
//                    return "correct results";
//
//                } else {
//                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku
//
//                    if (sbeGapGapRatio < 0.8) {
//                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
//                    } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
//                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
//                    } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
//                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
//                    } else {
//                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
//                    }
//                }
//
//            }

            else {  //sbe >2>
                return "Make sure that entered data are correct; consider presence of metabolic alkalosis and respiratory acidosis.";

            }
        }

        else {       //czyli paco2 >44

            return "respiratory acidosis and metabolic alkalosis";

        }
    }











    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    //
    //                      SBE + AGc   Kopenhagen Approach
    //
    //
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    static String metodaNadrzednaSbeAgc (double ph, double sbe, double paco2, double hco3, double agc){


        if(ph < 6.32){
            return "incorrect pH" ;

        }
        else if(ph >8){
            return "incorrect pH" ;

        }
        else if(paco2 > 374){
            return "incorrect PaCO2" ;

        }
        else if(hco3 >150){
            return "incorrect HCO3" ;

        }

        else if  (ph < ( 6.1 + Math.log10( hco3 / (0.03 * paco2))) - 0.015 || ph > ( 6.1 + Math.log10( hco3 / (0.03 * paco2))) + 0.015 ) {
            return "Incorrect data. Henderson–Hasselbalch equation is not fulfilled.";
        }

        else if(agc < 3 ){
            return "Low anion gap. Most probably laboratory error. See ‘more informations’." ;

        }

        else if(ph < 7.36){
            return kwasicaSbeAgc (hco3, sbe, paco2, agc);

        }
        else if(ph > 7.44){
            return zasadowicaSbeAgc (hco3, sbe, paco2, agc);
        }

        else if(ph >= 7.36 && ph < 7.4){
            return normatywne1SbeAgc (hco3, sbe, paco2, agc);
        }

        else{
            return normatywne2SbeAgc (hco3, sbe, paco2, agc);
        }

    }




    static double dajSbeAgcGapGapRatio (double agc, double hco3){
        if (hco3 > 23.99){
            return (agc - 7) / (0.01);
        }
        else {
            return (agc - 7) / (24 - hco3);
        }
    }



    static String kwasicaSbeAgc (double hco3, double sbe, double paco2, double agc){
        double sbeAgcGapGapRatio = dajSbeAgcGapGapRatio(agc, hco3);


        if ( paco2 <= 44){

            if ( sbe < -2 ){


                if ( Math.abs(paco2 - 40) < (0.95 *  Math.abs(sbe)) ){
                    // System.out.println("kwasica metaboliczna i zasadowica oddechowa");

                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis and respiratory alkalosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeAgcGapGapRatio < 0.8 ){
                            return "high anion gap metabolic acidosis and respiratory alkalosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis and respiratory alkalosis";
                        }

                        else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis and respiratory alkalosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis and respiratory alkalosis";
                        }
                    }
                }

                if (Math.abs(paco2 - 40) >= (0.95 *  Math.abs(sbe)) && Math.abs(paco2 - 40) < (0.99 *  Math.abs(sbe))){
                    // System.out.println("kwasica metaboliczna i prawdopodobnie zasadowica oddechowa");

                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis, probably with respiratory alkalosis";

                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeAgcGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis";
                        }

                        else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, probably with respiratory alkalosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis, probably with respiratory alkalosis";
                        }
                    }
                }

                else if (Math.abs(paco2 - 40) > (1.05 *  Math.abs(sbe))){
                    // System.out.println("kwasica metaboliczna i kwasica oddechowa");

                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis and respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeAgcGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis and respiratory acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis and respiratory acidosis";
                        }

                        else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis and respiratory acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis and respiratory acidosis";
                        }
                    }

                }

                else if (Math.abs(paco2 - 40) > (1.01 *  Math.abs(sbe)) && Math.abs(paco2 - 40) <= (1.05 *  Math.abs(sbe))){
                    // System.out.println("kwasica metaboliczna i prawdopodobna kwasica oddechowa");

                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis, probably with respiratory acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeAgcGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis";
                        }

                        else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, probably with respiratory acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis, probably with respiratory acidosis";
                        }
                    }

                }


                else {
                    //System.out.println(czysta kwasica metaboliczna);
                    //czyli mamy zakres Math.abs(paco2 - 40) > (0.99 *  Math.abs(sbe)) && Math.abs(paco2 - 40) <= (1.01 *  Math.abs(sbe)))
                    if (agc <= 11){
                        return "normal anion gap metabolic acidosis";
                        //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                    }

                    else {
                        //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                        if (sbeAgcGapGapRatio < 0.8){
                            return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                        }

                        else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2){
                            return "high anion gap metabolic acidosis";
                        }

                        else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2){
                            return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                        }

                        else {
                            return "high anion gap metabolic acidosis and metabolic alkalosis";
                        }
                    }

                }


            }
            else {
                // czyli sbe >= -2  wszystkie powyżej 25 równanie HH wyrzuca jako błąd niezgodny z równaniem
                //gdy zgodne z HH: kwasica oddechowa (możliwe dla max paco2 44 i hco3 24 w teorii, w programie hco3 25 teżB przejdzei ze względu na +/- 0.02)

                return "Make sure that entered data are correct; consider presence of respiratory acidosis.";

            }



        }

        //////////////////////tutaj kwasice oddechowe////////////////////////////

        else {
            //czyli gdy paCO2 > 44
            //System.out.println("kwasica oddechowa");


            if  (sbe < -2.1) {

                //return "kwasica oddechowa i kwasica metaboliczna";

                if (agc <= 11){
                    return "respiratory acidosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeAgcGapGapRatio < 0.8){
                        return "respiratory acidosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    }

                    else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2){
                        return "respiratory acidosis and high anion gap metabolic acidosis";
                    }

                    else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2){
                        return "respiratory acidosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    }

                    else {
                        return "respiratory acidosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            }

            if  (sbe >= -2.1 && sbe < -2) {

                //return "kwasica oddechowa i prawdopodobnie kwasica metaboliczna";

                if (agc <= 11){
                    return "respiratory acidosis, probably with normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                }

                else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeAgcGapGapRatio < 0.8){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    }

                    else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis";
                    }

                    else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2){
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    }

                    else {
                        return "respiratory acidosis, probably with high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            }

            else if (sbe >= -2 && sbe <= 2){
                //System.out.println("ostra kwasica oddechowa");
                return "acute respiratory acidosis";
            }

            else if (sbe > 2 && sbe < 0.99*(0.4 * (paco2 - 40))) {
                //System.out.println("kwasica oddechowa");
                return "partially compensated respiratory acidosis";
            }

            else if (sbe >=  0.99*(0.4 * (paco2 - 40)) && sbe <= 1.01 * (0.4 * (paco2 - 40))) {
                //System.out.println("przewlekła kwasica oddechowa");
                return "chronic respiratory acidosis";
            }

            else if (sbe >  1.01 * (0.4 * (paco2 - 40)) && sbe <= 1.05 * (0.4 * (paco2 - 40))) {
                //System.out.println("przewlekła kwasica oddechowa i prawdopodobnie zas metaboliczna");
                return "respiratory acidosis, probably with metabolic alkalosis";
            }

            else {
                return "respiratory acidosis and metabolic alkalosis";
            }

        }


    }

    static String zasadowicaSbeAgc (double hco3, double sbe, double paco2, double agc) {
        double sbeAgcGapGapRatio = dajSbeAgcGapGapRatio(agc, hco3);

        if (paco2 < 36) {
            //zasadowica oddechowa, teraz sprawdzam kompensację

            if (sbe < -2.1) {
                //System.out.println("zasadowica oddechowa i kwasica metaboliczna");

                if (agc <= 11) {
                    return "respiratory alkalosis and normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeAgcGapGapRatio < 0.8) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2) {
                        return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }
            }

            if (sbe >= -2.1 && sbe < -2) {
                //System.out.println("zasadowica oddechowa i prawdopodobnie kwasica metaboliczna");

                if (agc <= 11) {
                    return "respiratory alkalosis , probably with normal anion gap metabolic acidosis";
                    //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeAgcGapGapRatio < 0.8) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis , possible additional Normal anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2) {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "respiratory alkalosis, probably with high anion gap metabolic acidosis and metabolic alkalosis";
                    }
                }


            } else if (sbe >= -2 && sbe <= 2) {
                //System.out.println("ostra zasadowica oddechowa");
                return "acute respiratory alkalosis";

            } else if (sbe > 2 && sbe < 0.99*(0.4 * (paco2 - 40))) {
                //System.out.println("zasadowica oddechowa");
                return "partially compensated respiratory alkalosis";

            } else if (sbe >=  0.99*(0.4 * (paco2 - 40)) && sbe <= 1.01 * (0.4 * (paco2 - 40))) {
                //System.out.println("przewlekła zasadowica oddechowa");
                return "chronic respiratory alkalosis";

            } else if (sbe >  1.01 * (0.4 * (paco2 - 40)) && sbe <= 1.05 * (0.4 * (paco2 - 40))) {
                //System.out.println("zas odd + prawd zas met");
                return "respiratory alkalosis, probably with metabolic alkalosis";

            } else {
                // that is when hco3 > szacowaneHco3ZasOddPrzewlB
                return "respiratory alkalosis and metabolic alkalosis";
            }
        }




        else {   // czyli paCO2 >=36
            if (sbe <= 2) {
                return "Make sure that entered data are correct; consider presence of respiratory and metabolic alkalosis.";



            } else {
                //zas metaboliczna, czyli sbe powyżej >2


                if (Math.abs(paco2 - 40) < (0.95 *  ( 0.6 * Math.abs(sbe))) ) {
                    return "metabolic alkalosis and respiratory alkalosis";

                } else if (Math.abs(paco2 - 40) >= (0.95 * ( 0.6 *  Math.abs(sbe))) && Math.abs(paco2 - 40) < (0.99 * ( 0.6 *  Math.abs(sbe)))) {
                    return "metabolic alkalosis, probably with respiratory alkalosis";

                } else if (Math.abs(paco2 - 40) >= (0.99 * ( 0.6 *  Math.abs(sbe))) && Math.abs(paco2 - 40) <= (1.01 * ( 0.6 *  Math.abs(sbe)))) {
                    return "metabolic alkalosis";

                } else if (Math.abs(paco2 - 40) > (1.01 * ( 0.6 *  Math.abs(sbe))) && Math.abs(paco2 - 40) <= (1.05 * ( 0.6 *  Math.abs(sbe)))) {
                    return "metabolic alkalosis, probably with respiratory acidosis";

                } else {   //czyli paCO2  - 40 > 1.05 *  0.6 *  Math.abs(sbe)
                    return "metabolic alkalosis and respiratory acidosis";
                }
            }
        }
    }

    static String normatywne1SbeAgc (double hco3, double sbe, double paco2, double agc) {
        double sbeAgcGapGapRatio = dajSbeAgcGapGapRatio(agc, hco3);

        if (paco2 < 36){
            //zas odd i kw met

            if (agc <= 11) {
                return "respiratory alkalosis and normal anion gap metabolic acidosis";
                //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
            } else {
                //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                if (sbeAgcGapGapRatio < 0.8) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                } else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis";
                } else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                } else {
                    return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                }
            }

        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (sbe >= -2 && sbe <= 2){

                if (agc <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeAgcGapGapRatio < 0.8) {
                        return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2) {
                        return "possible high anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2) {
                        return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "high anion gap metabolic acidosis and  metabolic alkalosis";
                    }
                }
            }

            else {  //sbe != <-2 - 2>
                if (agc <= 11) {
                    return "Make sure that entered data are correct; check electrolytes concentration to exclude existence acid-base disturbances.";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeAgcGapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }

            }
        }

        else {       //czyli paco2 >44

            if (sbe > 2){
                return "respiratory acidosis and metabolic alkalosis";
            }

            else {
                return "Make sure that entered data are correct; consider presence of respiratory acidosis and metabolic alkalosis.";


            }
        }
    }


    static String normatywne2SbeAgc (double hco3, double sbe, double paco2, double agc) {
        //czyli pH >=7.4 && <= 7.44
        double sbeAgcGapGapRatio = dajSbeAgcGapGapRatio(agc, hco3);

        if (paco2 < 36){
            //zas odd i kw met


            if (agc <= 11) {
                return "respiratory alkalosis and normal anion gap metabolic acidosis";
                //nie daję tu zakresu 3-11 bo wartosci poniżej 3 powinny być odrzucane od razu jako błąd
            } else {
                //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                if (sbeAgcGapGapRatio < 0.8) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                } else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis";
                } else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2) {
                    return "respiratory alkalosis and high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                } else {
                    return "respiratory alkalosis and high anion gap metabolic acidosis and metabolic alkalosis";
                }
            }
            //pozostałe warianty HCO3 nie istnieją, są niezgodne z HH
        }

        else if (paco2 >= 36 && paco2 <= 44){

            if (sbe >= -2 && sbe <= 2){

                if (agc <= 11) {
                    return "correct results";

                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeAgcGapGapRatio < 0.8) {
                        return "high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2) {
                        return "possible high anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2) {
                        return "high anion gap metabolic acidosis, possible additional metabolic alkalosis";
                    } else {
                        return "high anion gap metabolic acidosis and  metabolic alkalosis";
                    }
                }
            }

            else if (sbe < -2) {
                if (agc <= 11) {
                    return "Make sure that entered data are correct; check electrolytes concentration to exclude existence acid-base disturbances.";
                } else {
                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku

                    if (sbeAgcGapGapRatio < 0.8) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
                    } else if (sbeAgcGapGapRatio >= 0.8 && sbeAgcGapGapRatio <= 1.2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
                    } else if (sbeAgcGapGapRatio > 1.2 && sbeAgcGapGapRatio <= 2) {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
                    } else {
                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
                    }
                }
            }
//            else if (hco3 >= 21 && hco3 < 22){
//                if (ag <= 11) {
//                    return "correct results";
//
//                } else {
//                    //sprawdzam stosunek luka/luka i przyporządkowuje zaburzenie w zależności od wyniku
//
//                    if (sbeGapGapRatio < 0.8) {
//                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis , possible additional normal anion gap metabolic acidosis.";
//                    } else if (sbeGapGapRatio >= 0.8 && sbeGapGapRatio <= 1.2) {
//                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis";
//                    } else if (sbeGapGapRatio > 1.2 && sbeGapGapRatio <= 2) {
//                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis, possible additional metabolic alkalosis.";
//                    } else {
//                        return "Make sure that entered data are correct; consider presence of high anion gap metabolic acidosis and gap metabolic alkalosis.";
//                    }
//                }
//
//            }

            else {  //sbe >2>
                return "Make sure that entered data are correct; consider presence of metabolic alkalosis and respiratory acidosis.";

            }
        }

        else {       //czyli paco2 >44

            return "respiratory acidosis and metabolic alkalosis";

        }
    }













    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wprowadzanie_danych);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                //EditText editTextSbe = (EditText) findViewById(R.id.editTextSbe);



                if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 && editTextAlb.length() !=0 ){
                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());
                    double alb = Double.parseDouble(editTextAlb.getText().toString());

                    double agc= dajAgc(alb, ag);
                    double sbe= dajSbe(hco3, ph);



                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("2", metodaNadrzednaAgc(ph, paco2, hco3, agc ));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("4", metodaNadrzednaSbeAgc(ph, sbe, paco2, hco3, agc ));

                    startActivity(intentPrzejdzDoDiagnoz1);

                }





                else if (editTextPh.length() !=0 && editTextPaco2.length() !=0 && editTextHco3.length() !=0 && editTextAg.length() !=0 ){

                    double ph = Double.parseDouble(editTextPh.getText().toString());
                    double paco2 = Double.parseDouble(editTextPaco2.getText().toString());
                    double hco3 = Double.parseDouble(editTextHco3.getText().toString());
                    double ag = Double.parseDouble(editTextAg.getText().toString());

                    double sbe= dajSbe(hco3, ph);

                    Intent intentPrzejdzDoDiagnoz1 = new Intent(ActivityWprowadzanieDanych.this, ActivityPorownanieDiagnoz.class);
                    intentPrzejdzDoDiagnoz1.putExtra("1", metodaNadrzedna(ph, paco2, hco3, ag ));
                    intentPrzejdzDoDiagnoz1.putExtra("3", metodaNadrzednaSbe(ph, sbe, paco2, hco3, ag ));
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
