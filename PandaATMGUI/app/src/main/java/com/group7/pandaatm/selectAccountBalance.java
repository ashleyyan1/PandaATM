package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class selectAccountBalance extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account_balance);
        radioGroup = findViewById(R.id.radioGroup);
        findViewById(R.id.previous3).setOnClickListener(buttonClickListener);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        radioGroup = findViewById(R.id.radioGroup);
        select = findViewById(R.id.select3);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                switch (v.getId()) {
                    case R.id.select3:
                        Intent bal = new Intent(selectAccountBalance.this, balanceScreen.class);
                        startActivity(bal);
                        break;
                }
            }
        });

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.previous3:
                    finish();
                    break;
            }
        }
    };
}
