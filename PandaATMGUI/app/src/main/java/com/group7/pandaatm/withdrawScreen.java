package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.group7.pandaatm.data.Message;
import com.group7.pandaatm.data.SessionController;

import java.io.IOException;

public class withdrawScreen extends AppCompatActivity {

    String accountName;
    double accountMax;
    boolean isChecking;
    double min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_screen);
        Intent pastIntent = getIntent();
        accountName = pastIntent.getStringExtra("accountName");
        accountMax = pastIntent.getDoubleExtra("amount", -1);
        isChecking = pastIntent.getBooleanExtra("isChecking", false);
        if(isChecking)
            min = pastIntent.getDoubleExtra("min", -1);
        //initialize click listeners
        findViewById(R.id.cancelButton1).setOnClickListener(buttonClickListener);
        findViewById(R.id.diffAmt).setOnClickListener(buttonClickListener);
        findViewById(R.id.twentybutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.fortybutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.sixtybutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.eightybutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.onehundredbutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.twohundredbutton).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = v -> {
        int amount = -1;
        switch (v.getId()) {
            case R.id.cancelButton1:        //if cancel button is clicked, go to main screen
                Thread worker11 = new Thread(() -> {
                    try {
                        SessionController c = SessionController.getInstance();
                        Message msgCancelWithdrawal = new Message(17);
                        c.sendMessage(msgCancelWithdrawal);
                        runOnUiThread(() -> {
                            Intent cancel = new Intent(withdrawScreen.this, MenuScreen.class);
                            startActivity(cancel);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case R.id.diffAmt:              //if different amount is clicked, go to withdraw diffAmt class
                Intent diffAmt = new Intent(withdrawScreen.this, withdrawDiffAmt.class);
                diffAmt.putExtra("accountName", accountName);
                diffAmt.putExtra("amount", amount);
                diffAmt.putExtra("isChecking", isChecking);
                if(isChecking) {
                    diffAmt.putExtra("min", min);
                }
                startActivity(diffAmt);
                break;
            case R.id.twentybutton:
                amount = 20;
                break;
            case R.id.fortybutton:
                amount = 40;
                break;
            case R.id.sixtybutton:
                amount = 60;
                break;
            case R.id.eightybutton:
                amount = 80;
                break;
            case R.id.onehundredbutton:
                amount = 100;
                break;
            case R.id.twohundredbutton:
                amount = 200;
                break;
            default:
                throw new IllegalArgumentException("Unknown Button Pressed");
        }
        if(amount != -1) {
            if(amount < accountMax) {
                int finalAmount = amount;
                Thread worker12 = new Thread(() -> {
                    try {
                        SessionController c = SessionController.getInstance();
                        Message msgSendAmount = new Message(18);
                        msgSendAmount.addDoubleM(finalAmount);
                        c.sendMessage(msgSendAmount);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                worker12.start();
            }
        }
    };


}
