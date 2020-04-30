package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pandaatm.data.Message;
import com.example.pandaatm.data.SessionController;

import java.io.IOException;

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
                    atmID = 1;
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
            if (atmID != 0) {
                Thread worker1 = new Thread(() -> {
                    try {
                        c = SessionController.getInstance();
                        System.out.println("Sending Message");
                        c.sendMessage(new Message(6));
                        System.out.println("Sent Message");
                        Message msgATMRequestReponse = c.readMessage();
                        System.out.println("Message Read");
                        if(msgATMRequestReponse.flag() == 8) {
                            //TODO Access Denied, create an alert
                        }
                        else if(msgATMRequestReponse.flag() == 22) {
                            //Access Granted, TODO go to log in screen
                            runOnUiThread(() -> {
                                Intent login = new Intent(MainActivity.this, loginScreen.class);
                                startActivity(login);
                            });
                        }
                        else if(msgATMRequestReponse.flag() == 7)
                            //TODO maybe display error before crashing program
                            System.exit(30);//Communication error, quit program
                        else {
                            //TODO bad communication, display crashing program
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            //TODO Show error
                        });
                    }

                });
                worker1.start();
            }
        }
    };
}