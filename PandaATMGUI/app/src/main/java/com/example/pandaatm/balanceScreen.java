package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class balanceScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_screen);

        //initialize click listeners
        findViewById(R.id.cancelButton4).setOnClickListener(buttonClickListener);
        findViewById(R.id.continueButton).setOnClickListener(buttonClickListener);

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancelButton4:    //if cancel button is clicked, go back to main screen
                    Intent cancel = new Intent(balanceScreen.this, transactionScreen.class);
                    startActivity(cancel);
                    break;
//                case R.id.continueButton:
            }
        }
    };
    
}