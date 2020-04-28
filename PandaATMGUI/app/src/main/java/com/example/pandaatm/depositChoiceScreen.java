package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class depositChoiceScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_choice_screen);

        //initialize click listeners
        findViewById(R.id.cashDeposit).setOnClickListener(buttonClickListener);
        findViewById(R.id.checkDeposit).setOnClickListener(buttonClickListener);
        findViewById(R.id.cancelButton1).setOnClickListener(buttonClickListener);

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                    case R.id.cashDeposit:      //if cash button is clicked, go to cash deposit screen
                    Intent cash = new Intent(depositChoiceScreen.this, cashDepositScreen.class);
                    startActivity(cash);
                    break;
                case R.id.checkDeposit:         //if check is clicked, go to check deposit screen
                    Intent deposit = new Intent(depositChoiceScreen.this, checkDepositScreen.class);
                    startActivity(deposit);
                    break;
                case R.id.cancelButton1:        //if cancel button is clicked, go to main screen
                    Intent cancel = new Intent(depositChoiceScreen.this, MainActivity.class);
                    startActivity(cancel);
                    break;
                default:

            }
        }
    };
}


