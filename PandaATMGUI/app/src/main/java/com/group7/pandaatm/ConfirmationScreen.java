package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.group7.pandaatm.data.Message;
import com.group7.pandaatm.data.SessionController;

import java.io.IOException;

public class ConfirmationScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);
        findViewById(R.id.confirmNo).setOnClickListener(buttonClickListener);
        findViewById(R.id.confirmYes).setOnClickListener(buttonClickListener);;
    }

    /*
    Sending Message IDs
    19 = confirmTransaction
    17 = transactionCancelled
    ------------------------
    20 - Insufficient Balance
    12 - Transaction Successful
    21 - Non enough bills available in ATM
     */

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.confirmNo:
                    Thread worker20 = new Thread(() -> {
                        try {
                            SessionController c = SessionController.getInstance();
                            c.sendMessage(new Message(17));
                            runOnUiThread(() -> {
                                Intent dep = new Intent(ConfirmationScreen.this, MenuScreen.class);
                                startActivity(dep);
                                finish();
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    worker20.start();
                    break;
                case R.id.confirmYes:
                    Thread worker21 = new Thread(() -> {
                        try {
                            SessionController c = SessionController.getInstance();
                            c.sendMessage(new Message(19));
                            Message m = c.readMessage();

                             if (m.flag() == 12) {//Transaction Successful
                                runOnUiThread(() -> {
                                    Intent dep = new Intent(ConfirmationScreen.this, transactionSuccessful.class);
                                    startActivity(dep);
                                    finish();
                                });
                            }
                            else if (m.flag() == 20 || m.flag() == 21) {//Transaction Failed
                                runOnUiThread(() -> {
                                    Intent dep = new Intent(ConfirmationScreen.this, transactionUnsuccessful.class);
                                    dep.putExtra("Error Message", m.getTextMessages().get(0));
                                    startActivity(dep);
                                    finish();
                                });
                            }
                            else{//shouldn't happen
                                System.exit(1);
                            }
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    });
                    worker21.start();
                    break;
            }//end switch
        }//end onClick()
    };
    @Override
    public void onBackPressed() {}//Disables Android's Back Button
}//end ConfirmationScreen