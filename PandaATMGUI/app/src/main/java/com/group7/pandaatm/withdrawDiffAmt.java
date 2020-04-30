package com.group7.pandaatm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class withdrawDiffAmt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_diff_amt);

        //initialize click listeners
        findViewById(R.id.clearButton1).setOnClickListener(buttonClickListener);
        findViewById(R.id.enterButton1).setOnClickListener(buttonClickListener);
        findViewById(R.id.cancelButton2).setOnClickListener(buttonClickListener);

    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clearButton1:     //if clear button is clicked, clear text fields
                    EditText withdrawAmt = findViewById(R.id.withdrawAmt);
                    withdrawAmt.getText().clear();
                    break;
//                case R.id.enterButton2:
//                    Intent enter = new Intent(withdrawDiffAmt.this, checkDepositScreen.class);
//                    startActivity(enter);
//                    break;
                case R.id.cancelButton2:        //if previous button is clicked, go to withdraw screen
                    finish();
                    break;
            }
        }
    };

}
