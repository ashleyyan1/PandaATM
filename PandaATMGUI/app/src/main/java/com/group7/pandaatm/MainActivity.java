package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.group7.pandaatm.data.SessionController;

public class MainActivity extends AppCompatActivity {

    private SessionController c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //initialize click listeners
        findViewById(R.id.bldg1ATM).setOnClickListener(buttonClickListener);
        findViewById(R.id.bldg17ATM).setOnClickListener(buttonClickListener);
        findViewById(R.id.bscATM).setOnClickListener(buttonClickListener);
        findViewById(R.id.libATM).setOnClickListener(buttonClickListener);
        findViewById(R.id.mktplaceATM).setOnClickListener(buttonClickListener);


    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int atmID = 0;
            switch (v.getId()) {
                case R.id.bldg1ATM: //if ATM on map is clicked, goes to login screen

                    //used for testing
                    Intent login = new Intent(MainActivity.this, loginScreen.class);
                    startActivity(login);
                    break;
                case R.id.bldg17ATM:
                    atmID = 2;
                    break;
                case R.id.mktplaceATM:
                    atmID = 3;
                    break;
                case R.id.bscATM:
                    atmID = 4;
                    break;
                case R.id.libATM:
                    atmID = 5;
                    break;
            }

        }
    };
}