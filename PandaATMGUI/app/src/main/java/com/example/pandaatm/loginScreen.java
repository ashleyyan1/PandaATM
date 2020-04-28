package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
public class loginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_login);

        //initialize click listeners
        findViewById(R.id.loginButton).setOnClickListener(buttonClickListener);
        findViewById(R.id.clearButton).setOnClickListener(buttonClickListener);

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginButton:      //if login button is clicked, go to transaction screen
                    Intent login = new Intent(loginScreen.this, transactionScreenn.class);
                    startActivity(login);
                    break;
                case R.id.clearButton:      //if clean button is clicked, clear text fields
                    EditText cardNum = findViewById(R.id.cardNumberText);
                    EditText pinNum = findViewById(R.id.pinText);
                    cardNum.getText().clear();
                    pinNum.getText().clear();
                    break;
                default:
                    break;

            }
        }
    };
}
