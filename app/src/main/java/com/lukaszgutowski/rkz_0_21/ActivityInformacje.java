package com.lukaszgutowski.rkz_0_21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityInformacje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacje);



        Button buttonInformacjeDoMenu = (Button) findViewById(R.id.buttonInformacjeDoMenu);
        buttonInformacjeDoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInformacjeDoMenu = new Intent(ActivityInformacje.this, ActivityMenu.class);
                startActivity(intentInformacjeDoMenu);
            }
        });


    }

}


