package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class depositUnsuccessful extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_unsuccessful_screen);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.dateTxt6);
        textViewDate.setText(currentDate);

        SimpleDateFormat format = new SimpleDateFormat("HH:MM:SS");
        String time = format.format(calendar.getTime());
        TextView textview = findViewById(R.id.timeText6);
        textview.setText(time);
        
        findViewById(R.id.main).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = v -> {
        switch (v.getId()) {
            case R.id.main:    //if cancel button is clicked, go back to main screen
//                AlertDialog.Builder insufficientFundsAlert = new AlertDialog.Builder(depositUnsuccessful.this);
//                insufficientFundsAlert.setMessage("Insufficient Funds.");
//                insufficientFundsAlert.setTitle("Unable to process...");
//                insufficientFundsAlert.setPositiveButton("OK", null);
//                insufficientFundsAlert.setCancelable(false);
//                insufficientFundsAlert.create().show();
                Intent main = new Intent(depositUnsuccessful.this, MenuScreen.class);
                startActivity(main);
                break;
            default:
                break;
        }
    };
}
