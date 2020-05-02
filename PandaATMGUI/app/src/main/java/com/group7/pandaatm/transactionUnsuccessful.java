package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class transactionUnsuccessful extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_unsuccessful_screen);
        Intent dep = getIntent();

        String errorMsg = dep.getStringExtra("Error Message");
        TextView errorMsgBox = findViewById(R.id.textView3);
        errorMsgBox.setText(errorMsg);

        findViewById(R.id.main2).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main2:
                    Intent dep2 = new Intent(transactionUnsuccessful.this, MenuScreen.class);
                    startActivity(dep2);
                    break;
                default:
                    break;
            }//end onClick()
        }
    };
}
