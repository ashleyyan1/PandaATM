package com.example.pandaatm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class cashDepositScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_deposit_screen);

        //initialize click listeners
        findViewById(R.id.clearButton1).setOnClickListener(buttonClickListener);
        findViewById(R.id.enterButton).setOnClickListener(buttonClickListener);
        findViewById(R.id.previous).setOnClickListener(buttonClickListener);

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clearButton1:     //if clear button is clicked, clear text fields
                    EditText cashAmt = findViewById(R.id.cashamt);
                    cashAmt.getText().clear();
                    break;
//                case R.id.enterButton:
//                    Intent enter = new Intent(cashDepositScreen.this, checkDepositScreen.class);
//                    startActivity(deposit);
//                    break;
                case R.id.previous:         //if previous button is clicked, go to deposit choice screen
                    finish();
                default:
                    break;
            }
        }

    };
}


