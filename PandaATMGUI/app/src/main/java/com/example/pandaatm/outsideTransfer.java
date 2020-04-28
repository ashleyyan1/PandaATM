package com.example.pandaatm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class outsideTransfer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initialize click listeners
        findViewById(R.id.clearButton4).setOnClickListener(buttonClickListener);
        findViewById(R.id.enterButton3).setOnClickListener(buttonClickListener);
        findViewById(R.id.previous3).setOnClickListener(buttonClickListener);

    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clearButton4:     //if clear button is clicked, clear text fields
                    EditText acctNum = findViewById(R.id.acctNum);
                    EditText amountTransfer = findViewById(R.id.amountTransfer);
                    acctNum.getText().clear();
                    amountTransfer.getText().clear();
                    break;
//                case R.id.enterButton3:
//                    Intent login = new Intent(
//
//                    break;
                case R.id.previous3:        //if previous button is clicked, go to transfer type screen
                    finish();
                    break;
            }
        }
    };


}
