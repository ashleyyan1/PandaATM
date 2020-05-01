package com.group7.pandaatm;

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
        findViewById(R.id.cancelButton1).setOnClickListener(buttonClickListener);
        findViewById(R.id.diffAmt).setOnClickListener(buttonClickListener);
        findViewById(R.id.twentybutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.fortybutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.sixtybutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.eightybutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.onehundredbutton).setOnClickListener(buttonClickListener);
        findViewById(R.id.twohundredbutton).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = v -> {
        switch (v.getId()) {
            case R.id.cancelButton1:        //if cancel button is clicked, go to main screen
                Intent cancel = new Intent(withdrawScreen.this, transactionScreen.class);
                startActivity(cancel);
                break;
            case R.id.diffAmt:              //if different amount is clicked, go to withdraw diffAmt class
                Intent diffAmt = new Intent(withdrawScreen.this, withdrawDiffAmt.class);
                startActivity(diffAmt);
                break;
            case R.id.twentybutton:
            case R.id.fortybutton:
            case R.id.sixtybutton:
            case R.id.eightybutton:
            case R.id.onehundredbutton:
            case R.id.twohundredbutton:
                Intent withdrawSuccess = new Intent(withdrawScreen.this, withdrawSuccessful.class);
                startActivity(withdrawSuccess);
                break;
            default:
                break;
        }
    };


}
