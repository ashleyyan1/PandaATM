package com.example.pandaatm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class checkDepositScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_deposit_screen);

        findViewById(R.id.clearButton2).setOnClickListener(buttonClickListener);
        findViewById(R.id.enterButton1).setOnClickListener(buttonClickListener);
        findViewById(R.id.previous1).setOnClickListener(buttonClickListener);

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clearButton2:
                    EditText checkAmt = findViewById(R.id.checkamt);
                    checkAmt.getText().clear();
                    break;
//                case R.id.enterButton1:
//                    Intent enter = new Intent(checkDepositScreen.this, checkDepositScreen.class);
//                    startActivity(enter);
//                    break;
                case R.id.previous1:
                    finish();
                    break;
                default:
                    break;
            }
        }

    };

}


