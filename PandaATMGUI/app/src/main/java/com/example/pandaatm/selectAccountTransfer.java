package com.example.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class selectAccountTransfer extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account_transfer);
        radioGroup = findViewById(R.id.radioGroup);
        findViewById(R.id.previous2).setOnClickListener(buttonClickListener);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        radioGroup = findViewById(R.id.radioGroup);
        select = findViewById(R.id.select2);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                switch (v.getId()) {
                    case R.id.select2:
                        Intent trans = new Intent(selectAccountTransfer.this, outsideTransfer.class);
                        startActivity(trans);
                        break;
                    case R.id.previous2:
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
                case R.id.previous2:
                    finish();
                    break;
            }
        }
    };
}