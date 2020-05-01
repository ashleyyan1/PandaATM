package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.group7.pandaatm.data.Message;
import com.group7.pandaatm.data.SessionController;

import java.io.IOException;

public class loginScreen extends AppCompatActivity {
    private EditText cardNumber;
    private EditText pinNumber;
    private Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_login);

        //initialize click listeners
        findViewById(R.id.loginButton).setOnClickListener(buttonClickListener);
        findViewById(R.id.clearButton).setOnClickListener(buttonClickListener);
        findViewById(R.id.backToMapButton).setOnClickListener(buttonClickListener);

        cardNumber = findViewById(R.id.cardNumberText);
        pinNumber = findViewById(R.id.pinText);
        buttonConfirm = findViewById(R.id.loginButton);

        cardNumber.addTextChangedListener(loginWatcher);
        pinNumber.addTextChangedListener(loginWatcher);

    }
    private TextWatcher loginWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String cardNumberr = cardNumber.getText().toString().trim();
            String pinNumberr = pinNumber.getText().toString().trim();

            buttonConfirm.setEnabled(!cardNumberr.isEmpty() && !pinNumberr.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText cardNum = findViewById(R.id.cardNumberText);
            EditText pinNum = findViewById(R.id.pinText);
            switch (v.getId()) {
                case R.id.loginButton:      //if login button is clicked, go to transaction screen
                    System.out.println("Preparing Card Details");
                    String cardNumber = cardNum.getText().toString();
                    String pin = pinNum.getText().toString();
                    long cardNumberLong = 0L;
                    int pinNumberInt = 0;
                    try {
                        cardNumberLong = Long.parseLong(cardNumber);
                        pinNumberInt = Integer.parseInt(pin);
                    } catch (NumberFormatException e) {
                        //TODO somehow a non-numeric-char got in the cardNumber /shrug
                    }
                    System.out.println("Card Number: " + cardNumberLong);
                    System.out.println("Pin: " + pinNumberInt);
                    Message msgLoginDetails = new Message(2);
                    msgLoginDetails.setCardNumber(cardNumberLong);
                    msgLoginDetails.addIntegerM(pinNumberInt);
                    Thread worker2 = new Thread(() -> {
                        try {
                            SessionController c = SessionController.getInstance();
                            c.sendMessage(msgLoginDetails);
                            Message msgLoginVerification = c.readMessage();
                            System.out.println("ATM Session Response Flag: " + msgLoginVerification.flag());
                            if (msgLoginVerification.flag() == 3) {//Card Number not found
                                //TODO Show alert card number not found, show alert, allow new card
                            } else if (msgLoginVerification.flag() == 4) {
                                //TODO Show Card is locked, show alert, kick user out back to MainActivity
                                c.terminateSession();
                                runOnUiThread(() -> {
                                    Intent map = new Intent(loginScreen.this, MainActivity.class);
                                    startActivity(map);
                                });
                            } else if (msgLoginVerification.flag() == 5) {
                                //TODO Successful, go to transactionScreen, maybe show animation beforehand
                                runOnUiThread(() -> {
                                    Intent mainMenu = new Intent(loginScreen.this, transactionScreen.class);
                                    startActivity(mainMenu);
                                });
                            } else if (msgLoginVerification.flag() == 23) {
                                //TODO Card is Expired, show Alert, allow new card
                            } else if (msgLoginVerification.flag() == 24) {
                                //TODO Pin Number invalid, show Alert, only allow changing pin, or cancel
                            }
                        } catch (IOException e) {
                            //Should not happen
                        }
                    });
                    worker2.start();
                    break;
                    // if login button is clicked, go to transaction screen
                    //check if fields are null
//                    if (cardNum.getText().toString().equals(number) && pinNum.getText().toString().equals(pin)) {
//                        Intent login = new Intent(loginScreen.this, transactionScreen.class);
//                        startActivity(login);
//                    } else {
//                        AlertDialog.Builder errorAlert = new AlertDialog.Builder(loginScreen.this);
//                        errorAlert.setMessage("Wrong card number or PIN.");
//                        errorAlert.setTitle("Error Message...");
//                        errorAlert.setPositiveButton("OK", null);
//                        errorAlert.setCancelable(true);
//                        errorAlert.create().show();
//                    }
//                    break;
                case R.id.clearButton:      //if clean button is clicked, clear text fields
                    cardNum.getText().clear();
                    pinNum.getText().clear();
                    break;
                case R.id.backToMapButton:
                    System.out.println("Cancelling ATM Request");
                    Message msgCancelATMRequest = new Message(0);
                    Thread worker3 = new Thread(() -> {
                        SessionController c = null;
                        try {
                            c = SessionController.getInstance();
                            c.sendMessage(msgCancelATMRequest);
                            c.terminateSession();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(() -> {
                            Intent mainActivity = new Intent(loginScreen.this, MainActivity.class);
                            startActivity(mainActivity);
                        });
                    });
                    worker3.start();
                    break;
                default:
                    break;
            }

        }

    };

}