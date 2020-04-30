package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class selectAccountWithdraw extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account_withdraw);
        radioGroup = findViewById(R.id.radioGroup);
        findViewById(R.id.previous1).setOnClickListener(buttonClickListener);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        radioGroup = findViewById(R.id.radioGroup);
        select = findViewById(R.id.select1);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                switch (v.getId()) {
                    case R.id.select1:
                        Intent withdraw = new Intent(selectAccountWithdraw.this, withdrawScreen.class);
                        startActivity(withdraw);
                        break;
                    case R.id.previous1:
                        finish();
                        break;
                }
            }
        });
    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.previous1:
                    finish();
                    break;
            }
        }
    };
}
