package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class withdrawSuccessful extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_successful_screen);

        findViewById(R.id.logout1).setOnClickListener(buttonClickListener);
        findViewById(R.id.yes1).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = v -> {
        switch (v.getId()) {
            case R.id.logout1:    //if cancel button is clicked, go back to main screen
                Intent main = new Intent(withdrawSuccessful.this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.yes1:
                Intent trans = new Intent(withdrawSuccessful.this, MenuScreen.class);
                startActivity(trans);
                break;
            default:
                break;
        }
    };
}
