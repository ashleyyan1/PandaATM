package com.group7.pandaatm;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group7.pandaatm.data.Message;
import com.group7.pandaatm.data.SessionController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.dateTxt3);
        textViewDate.setText(currentDate);

        SimpleDateFormat format = new SimpleDateFormat("HH:MM:SS");
        String time = format.format(calendar.getTime());
        TextView textview = findViewById(R.id.timeText3);
        textview.setText(time);


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
                int finalAtmID = atmID;
                Thread worker1 = new Thread(() -> {
                    try {
                        c = SessionController.getInstance();
                        Message msgRequestATM = new Message(6);
                        msgRequestATM.addIntegerM(finalAtmID);
                        c.sendMessage(msgRequestATM);
                        System.out.println("Sent ATM Request Message");
                        Message msgATMRequestReponse = c.readMessage();
                        System.out.println("ATM Request Response Message Flag: " + msgATMRequestReponse.flag());
                        if (msgATMRequestReponse.flag() == 8) {
                            //TODO Access Denied, create an alert
                            AlertDialog.Builder beingUsedAlert = new AlertDialog.Builder(MainActivity.this);
                            beingUsedAlert.setMessage("ATM is currently in use.");
                            beingUsedAlert.setTitle("Bad Login...");
                            beingUsedAlert.setPositiveButton("OK", null);
                            beingUsedAlert.setCancelable(false);
                            beingUsedAlert.create().show();
                            c.terminateSession();
                        } else if (msgATMRequestReponse.flag() == 22) {
                            //Access Granted, TODO go to log in screen
                            c.setAddress(msgATMRequestReponse.getTextMessages().get(0));
                            runOnUiThread(() -> {
                                Intent login = new Intent(MainActivity.this, loginScreen.class);
                                login.putExtra("ATMAddress", msgATMRequestReponse.getTextMessages().get(0));
                                startActivity(login);
                            });
                        } else if (msgATMRequestReponse.flag() == 7) {
                            //TODO maybe display error before crashing program
                            c.terminateSession();
                            System.exit(1);//Communication error, quit program
                        } else {
                            c.terminateSession();
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

    @Override
    public void onBackPressed() {}
}
