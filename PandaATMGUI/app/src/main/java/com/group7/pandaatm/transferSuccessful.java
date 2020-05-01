package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class transferSuccessful extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_successful_screen);

        findViewById(R.id.logout2).setOnClickListener(buttonClickListener);
        findViewById(R.id.yes2).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = v -> {
        switch (v.getId()) {
            case R.id.logout2:    //if cancel button is clicked, go back to main screen
                Intent main = new Intent(transferSuccessful.this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.yes2:
                Intent trans = new Intent(transferSuccessful.this, MenuScreen.class);
                startActivity(trans);
                break;
            default:
                break;
        }
    };
}
