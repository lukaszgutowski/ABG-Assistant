package com.lukaszgutowski.rkz_0_21;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ActivityBaza extends AppCompatActivity {
    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baza2);
        myDb = new DatabaseHelper(this);

        String diagnoza = getIntent().getStringExtra("1a");

        //przypisuje znowu wartość z ActivityPorownanieDiagnoz do zmiennej. Do tegomiejsca wszystko działa i dochodzi
        //String diagnozab = getIntent().getStringExtra("2a");

        //String diagnozac = getIntent().getStringExtra("3a");
        String name = getIntent().getStringExtra("4a");
        String surname = getIntent().getStringExtra("5a");

        if (diagnoza != null || diagnoza == "") {
            boolean isInserted = myDb.insertData(name, surname, diagnoza);
            if (isInserted == true) {
                Toast.makeText(ActivityBaza.this, "Dane wprowadzono", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ActivityBaza.this, "Danych nie wprowadzono", Toast.LENGTH_LONG).show();
            }

            Cursor res = myDb.getAllData();
            if (res.getCount() == 0) {
                // show message
                showMessage("Error", "Nie wprowadzono danych");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("Id :" + res.getString(0) + "\n");
                buffer.append("Name :" + res.getString(1) + "\n");
                buffer.append("Surname :" + res.getString(2) + "\n");
                buffer.append("Diagnoza :" + res.getString(3) + "\n\n");
            }

            showMessage("Data", buffer.toString());



        }
        else {
            Cursor res = myDb.getAllData();
            if (res.getCount() == 0) {
                // show message
                showMessage("Error", "Nie wprowadzono danych");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("Id :" + res.getString(0) + "\n");
                buffer.append("Name :" + res.getString(1) + "\n");
                buffer.append("Surname :" + res.getString(2) + "\n");
                buffer.append("Diagnoza :" + res.getString(3) + "\n\n");
            }
            showMessage("Data", buffer.toString());
        }

        Button buttonMenuZBazy = (Button) findViewById(R.id.buttonMenuZBazy);
        buttonMenuZBazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenuZBazy = new Intent(ActivityBaza.this, ActivityMenu.class);
                startActivity(intentMenuZBazy);
            }
        });
    }




    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


}









