package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class transactionScreenn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_screenn);

        findViewById(R.id.withdrawBtn).setOnClickListener(buttonClickListener);
        findViewById(R.id.depositBtn).setOnClickListener(buttonClickListener);
        findViewById(R.id.transferBtn).setOnClickListener(buttonClickListener);
        findViewById(R.id.balanceBtn).setOnClickListener(buttonClickListener);
        findViewById(R.id.cancelBtn).setOnClickListener(buttonClickListener);

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.withdrawBtn:
                    Intent withdraw = new Intent(transactionScreenn.this, withdrawScreen.class);
                    startActivity(withdraw);
                    break;
                case R.id.depositBtn:
                    Intent deposit = new Intent(transactionScreenn.this, depositChoiceScreen.class);
                    startActivity(deposit);
                    break;
                case R.id.transferBtn:
                    Intent transfer = new Intent(transactionScreenn.this, transferType.class);
                    startActivity(transfer);
                    break;
                case R.id.balanceBtn:
                    Intent balance = new Intent(transactionScreenn.this, balanceScreen.class);
                    startActivity(balance);
                    break;
                case R.id.cancelBtn:
                    Intent cancel = new Intent(transactionScreenn.this, MainActivity.class);
                    startActivity(cancel);
                    break;
                default:
                    break;

            }
        }
    };


}


