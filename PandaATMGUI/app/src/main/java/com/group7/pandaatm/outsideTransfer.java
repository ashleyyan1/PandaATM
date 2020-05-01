package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class outsideTransfer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_transfer);

        //initialize click listeners
        findViewById(R.id.clearButton2).setOnClickListener(buttonClickListener);
        findViewById(R.id.enterButton2).setOnClickListener(buttonClickListener);
        findViewById(R.id.cancelButton3).setOnClickListener(buttonClickListener);

    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clearButton2:     //if clear button is clicked, clear text fields
                    EditText acctNum = findViewById(R.id.acctNum);
                    EditText amountTransfer = findViewById(R.id.amountTransfer);
                    acctNum.getText().clear();
                    amountTransfer.getText().clear();
                    break;
                case R.id.enterButton2:
                    //if(transSuccessful)
//                    Intent transSuccessful = new Intent(outsideTransfer.this, transferSuccessful.class);
//                    startActivity(transSuccessful);
                    //if(transUnsuccessful)
                    Intent transUnsuccessful = new Intent(outsideTransfer.this, transferUnsuccessful.class);
                    startActivity(transUnsuccessful);
                    break;
                case R.id.cancelButton3:        //if previous button is clicked, go to transfer type screen
                    finish();
                    break;
            }
        }
    };


}
