package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class transferType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initialize click listeners
        findViewById(R.id.checkingAcct).setOnClickListener(buttonClickListener);
        findViewById(R.id.savingsAcct).setOnClickListener(buttonClickListener);
        findViewById(R.id.cancelButton3).setOnClickListener(buttonClickListener);


    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkingAcct:     //if checking button is clicked, go to outside transfer class
                    Intent checkingAcc = new Intent(transferType.this, outsideTransfer.class);
                    startActivity(checkingAcc);
                    break;
                case R.id.savingsAcct:
                    Intent savingsAcct = new Intent(transferType.this, outsideTransfer.class);
                    startActivity(savingsAcct);
                    break;
                case R.id.cancelButton3:    //if cancel button is clicked, go to main class
                    Intent cancel = new Intent(transferType.this, MainActivity.class);
                    startActivity(cancel);
                    break;
                default:
                    break;
            }
        }
    };

}
