package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class transferUnsuccessful extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_unsuccessful_screen);

        findViewById(R.id.main2).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = v -> {
        switch (v.getId()) {
            case R.id.main2:    //if cancel button is clicked, go back to main screen
                Intent main = new Intent(transferUnsuccessful.this, MenuScreen.class);
                startActivity(main);
                break;
            default:
                break;
        }
    };
}
