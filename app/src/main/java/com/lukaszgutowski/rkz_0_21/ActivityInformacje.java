package com.lukaszgutowski.rkz_0_21;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityInformacje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacje);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




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


