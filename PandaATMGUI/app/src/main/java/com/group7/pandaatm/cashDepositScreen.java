package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class cashDepositScreen extends AppCompatActivity {

    TextView oneValue;
    TextView fiveValue;
    TextView tenValue;
    TextView twentyValue;
    TextView fiftyValue;
    TextView hundredValue;

    //initialize counters to zero
    int oneCounter = 0;
    int fiveCounter = 0;
    int tenCounter = 0;
    int twentyCounter = 0;
    int fiftyCounter = 0;
    int hundredCounter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_deposit_screen);

        oneValue = findViewById(R.id.oneCounter);
        fiveValue = findViewById(R.id.fiveCounter);
        tenValue = findViewById(R.id.tenCounter);
        twentyValue = findViewById(R.id.twentyCounter);
        fiftyValue = findViewById(R.id.fiftyCounter);
        hundredValue = findViewById(R.id.hundredCounter);


        //initialize click listeners
        findViewById(R.id.enterButton).setOnClickListener(buttonClickListener);
        findViewById(R.id.cancelButton).setOnClickListener(buttonClickListener);

        findViewById(R.id.addOne).setOnClickListener(buttonClickListener);
        findViewById(R.id.subOne).setOnClickListener(buttonClickListener);
        findViewById(R.id.addFive).setOnClickListener(buttonClickListener);
        findViewById(R.id.subFive).setOnClickListener(buttonClickListener);
        findViewById(R.id.addTen).setOnClickListener(buttonClickListener);
        findViewById(R.id.subTen).setOnClickListener(buttonClickListener);
        findViewById(R.id.addTen).setOnClickListener(buttonClickListener);
        findViewById(R.id.addTwenty).setOnClickListener(buttonClickListener);
        findViewById(R.id.subTwenty).setOnClickListener(buttonClickListener);
        findViewById(R.id.addFifty).setOnClickListener(buttonClickListener);
        findViewById(R.id.subFifty).setOnClickListener(buttonClickListener);
        findViewById(R.id.addHundred).setOnClickListener(buttonClickListener);
        findViewById(R.id.subHundred).setOnClickListener(buttonClickListener);


    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int totalBills = oneCounter + fiveCounter + tenCounter + twentyCounter + fiftyCounter + hundredCounter;
            switch (v.getId()) {
                case R.id.addOne:
                    if (totalBills < 500) {                        //total bill limit TBD
                        oneCounter++;
                        oneValue.setText("(" + oneCounter + ")");
                    }
                    break;
                case R.id.subOne:
                    if (oneCounter > 0) {                           //cannot have negative amount of bills
                        oneCounter--;
                        oneValue.setText("("+ oneCounter +")");
                    }
                    break;
                case R.id.addFive:
                    if (totalBills < 500) {
                        fiveCounter++;
                        fiveValue.setText("(" + fiveCounter + ")");
                    }
                    break;
                case R.id.subFive:
                    if (fiveCounter > 0) {
                        fiveCounter--;
                        fiveValue.setText("("+ fiveCounter +")");
                    }
                    break;
                case R.id.addTen:
                    if (totalBills < 500) {
                        tenCounter++;
                        tenValue.setText("(" + tenCounter + ")");
                    }
                    break;
                case R.id.subTen:
                    if (tenCounter > 0) {
                        tenCounter--;
                        tenValue.setText("("+ tenCounter +")");
                    }
                    break;
                case R.id.addTwenty:
                    if (totalBills < 500) {
                        twentyCounter++;
                        twentyValue.setText("(" + twentyCounter + ")");
                    }
                    break;
                case R.id.subTwenty:
                    if (twentyCounter> 0) {
                        twentyCounter--;
                        twentyValue.setText("("+ twentyCounter +")");
                        break;
                    }
                case R.id.addFifty:
                    if (totalBills < 10) {
                        fiftyCounter++;
                        fiftyValue.setText("(" + fiftyCounter + ")");
                    }
                    break;
                case R.id.subFifty:
                    if (fiftyCounter > 0) {
                        fiftyCounter--;
                        fiftyValue.setText("("+ fiftyCounter +")");
                    }
                    break;
                case R.id.addHundred:
                    if (totalBills < 500) {
                        hundredCounter++;
                        hundredValue.setText("(" + hundredCounter + ")");
                    }
                    break;
                case R.id.subHundred:
                    if (hundredCounter > 0) {
                        hundredCounter--;
                        hundredValue.setText("("+ hundredCounter +")");
                    }
                    break;
                case R.id.enterButton:
                    //if(depositSuccessful)
                    Intent depSuccessful = new Intent(cashDepositScreen.this, depositSuccessful.class);
                    startActivity(depSuccessful);
                    break;
                case R.id.cancelButton:         //if previous button is clicked, go to deposit choice screen
                    Intent cancel = new Intent(cashDepositScreen.this, MenuScreen.class);
                    startActivity(cancel);
                    break;
                default:
                    break;
            }
        }

    };
}


