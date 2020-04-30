package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
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


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int number = 12345;
            int pin = 1234;
            EditText cardNum = findViewById(R.id.cardNumberText);
            EditText pinNum = findViewById(R.id.pinText);

            switch (v.getId()) {
                case R.id.loginButton:
                    Intent login = new Intent(loginScreen.this, transactionScreen.class);
                    startActivity(login);
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
                default:
                    break;
            }

        }

    };
}
