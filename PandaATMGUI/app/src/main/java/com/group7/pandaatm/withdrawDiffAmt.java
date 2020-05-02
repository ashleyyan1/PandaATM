package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.group7.pandaatm.data.Message;
import com.group7.pandaatm.data.SessionController;

import java.io.IOException;

public class withdrawDiffAmt extends AppCompatActivity {

    private String accountName;
    private double accountMax;
    private boolean isChecking;
    private double min;
    private int billAvailableCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_diff_amt);
        Intent pastIntent = getIntent();
        accountName = pastIntent.getStringExtra("accountName");
        accountMax = pastIntent.getDoubleExtra("accountMax", -1);
        isChecking = pastIntent.getBooleanExtra("isChecking", false);
        billAvailableCount = pastIntent.getIntExtra("billAvailableCount", -1);
        if(isChecking)
            min = pastIntent.getDoubleExtra("min", -1);
        //initialize click listeners
        findViewById(R.id.clearButton1).setOnClickListener(buttonClickListener);
        findViewById(R.id.enterButton1).setOnClickListener(buttonClickListener);
        findViewById(R.id.cancelButton2).setOnClickListener(buttonClickListener);

    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText withdrawAmt = findViewById(R.id.withdrawAmt);
            switch (v.getId()) {
                case R.id.clearButton1:     //if clear button is clicked, clear text fields
                    withdrawAmt.getText().clear();
                    break;
                case R.id.enterButton1:
                    String amountText = withdrawAmt.getText().toString();
                    try{
                        double amount = Double.parseDouble(amountText);
                        if(amount < accountMax) {
                            if (amount % 20 == 0) {
                                if (amount / 20 < billAvailableCount) {
                                    Thread worker13 = new Thread(() -> {
                                        try {
                                            SessionController c = SessionController.getInstance();
                                            Message msgSendAmount = new Message(18);
                                            msgSendAmount.addDoubleM(amount);
                                            c.sendMessage(msgSendAmount);
                                            Intent enter = new Intent(withdrawDiffAmt.this, ConfirmationScreen.class);
                                            startActivity(enter);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    worker13.start();
                                } else {
                                    //TODO show error, request can not be supplied by ATM, do nothing
                                }
                            }
                            else {
                                //TODO show error, value is not divisible by twenty, and do nothing
                            }
                        }
                        else {
                            //TODO show error, Not enough in your bank account, and do nothing
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.cancelButton2:        //if previous button is clicked, go to withdraw screen
                    Thread worker14 = new Thread(() -> {
                        try {
                            SessionController c = SessionController.getInstance();
                            Message msgCancelRequest = new Message(17);
                            c.sendMessage(msgCancelRequest);
                            runOnUiThread(() -> {
                                Intent exit = new Intent(withdrawDiffAmt.this, MenuScreen.class);
                                startActivity(exit);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    worker14.start();
                    break;
            }
        }
    };

}
