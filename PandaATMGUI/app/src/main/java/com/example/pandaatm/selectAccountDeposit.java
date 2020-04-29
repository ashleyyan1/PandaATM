package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class selectAccountDeposit extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account_deposit);
        radioGroup = findViewById(R.id.radioGroup);
        findViewById(R.id.previous).setOnClickListener(buttonClickListener);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        radioGroup = findViewById(R.id.radioGroup);
        select = findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                switch (v.getId()) {
                    case R.id.select:
                        Intent dep = new Intent(selectAccountDeposit.this, cashDepositScreen.class);
                        startActivity(dep);
                        break;
                }
            }
        });

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.previous:
                    finish();
                    break;
            }
        }
    };
}
