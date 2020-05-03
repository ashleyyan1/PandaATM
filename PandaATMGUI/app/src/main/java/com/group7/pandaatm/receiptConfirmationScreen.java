package com.group7.pandaatm;

import android.app.Activity;
import android.os.Bundle;

public class receiptConfirmationScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_confirmation_screen);
    }
    @Override
    public void onBackPressed() {}//Disables Android's Back Button
}
