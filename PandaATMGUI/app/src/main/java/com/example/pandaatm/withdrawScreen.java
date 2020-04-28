package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class withdrawScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_screen);

        //initialize click listeners
        findViewById(R.id.cancelButton2).setOnClickListener(buttonClickListener);
        findViewById(R.id.diffAmt).setOnClickListener(buttonClickListener);

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancelButton2:        //if cancel button is clicked, go to main screen
                    Intent cancel = new Intent(withdrawScreen.this, MainActivity.class);
                    startActivity(cancel);
                    break;
                case R.id.diffAmt:              //if different amount is clicked, go to withdraw diffAmt class
                    Intent diffAmt = new Intent(withdrawScreen.this, withdrawDiffAmt.class);
                    startActivity(diffAmt);
                    break;
                default:
                    break;
            }
        }

    };


}
