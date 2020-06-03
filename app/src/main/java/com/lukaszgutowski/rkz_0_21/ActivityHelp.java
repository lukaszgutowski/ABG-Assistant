package com.lukaszgutowski.rkz_0_21;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




        Button buttonHelpDoMenu = (Button) findViewById(R.id.buttonHelpDoMenu);
        buttonHelpDoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHelpDoMenu = new Intent(ActivityHelp.this, ActivityMenu.class);
                startActivity(intentHelpDoMenu);
            }
        });


    }

}