package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class selectAccountTransferFrom extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account_transfer_from);
        radioGroup = findViewById(R.id.radioGroup);
        findViewById(R.id.previous2).setOnClickListener(buttonClickListener);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        radioGroup = findViewById(R.id.radioGroup);
        select = findViewById(R.id.select2);
        select.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(selectedId);
            switch (v.getId()) {
                case R.id.select2:
                    Intent transTo = new Intent(selectAccountTransferFrom.this, selectAccountTransferTo.class);
                    startActivity(transTo);
                    break;
            }
        });
    }

    private View.OnClickListener buttonClickListener = v -> {
        switch(v.getId()){
            case R.id.previous2:
                finish();
                break;
        }
    };
}