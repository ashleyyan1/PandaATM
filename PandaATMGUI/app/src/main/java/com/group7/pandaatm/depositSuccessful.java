package com.group7.pandaatm;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class depositSuccessful extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_successful_screen);

        findViewById(R.id.logout).setOnClickListener(buttonClickListener);
        findViewById(R.id.yes).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = v -> {
        switch (v.getId()) {
            case R.id.logout:    //if cancel button is clicked, go back to main screen
                Intent main = new Intent(depositSuccessful.this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.yes:
                Intent trans = new Intent(depositSuccessful.this, transactionScreen.class);
                startActivity(trans);
                break;
            default:
                break;
        }
    };
}
