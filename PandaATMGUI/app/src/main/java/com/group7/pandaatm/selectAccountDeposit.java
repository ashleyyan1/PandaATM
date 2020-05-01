package com.group7.pandaatm;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.group7.pandaatm.data.Message;
import com.group7.pandaatm.data.SessionController;

import java.io.IOException;
import java.util.ArrayList;

public class selectAccountDeposit extends AppCompatActivity {
    Button select;
    Spinner accountSpinner;
    ArrayList<String> accountNames;
    ArrayList<Integer> accountInteger;
    int nextIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account_deposit);
        accountSpinner = findViewById(R.id.accountSpinner);
        addListenerOnButton();
        Intent creationIntent = getIntent();
        accountNames = creationIntent.getStringArrayListExtra("accountNames");
        accountInteger = creationIntent.getIntegerArrayListExtra("accountIds");
        nextIntent = creationIntent.getIntExtra("nextIntent", -1);
        populateSpinner(accountNames);
    }

    public void addListenerOnButton() {
        select = findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.select:
                        //Gets AccountID from Spinner selected ID + AccountID Array List
                        int value = accountSpinner.getSelectedItemPosition();
                        int accountID = accountInteger.get(value);
                        System.out.println("Selected ID: " + accountID);
                        Thread worker6 = new Thread(() -> {
                            try {
                                SessionController c = SessionController.getInstance();
                                Message msgSendAccountID = new Message(16);
                                msgSendAccountID.addIntegerM(accountID);
                                c.sendMessage(msgSendAccountID);
                                Message msgRecieveAccountData = c.readMessage();
                                switch (nextIntent) {
                                    case 1://Deposit
                                        if(msgRecieveAccountData.flag() == 14) {
                                            double amount = msgRecieveAccountData.getDoubleMessages().get(0);
                                            int billCount = msgRecieveAccountData.getIntegerMessages().get(0);
                                            runOnUiThread(() -> {
                                                Intent dep = new Intent(selectAccountDeposit.this, cashDepositScreen.class);
                                                dep.putExtra("accountID", accountID);
                                                dep.putExtra("amount", amount);
                                                dep.putExtra("billCount", billCount);
                                                startActivity(dep);
                                            });
                                        }
                                        else
                                        {
                                            //Catestropic Error
                                            System.exit(1);
                                        }
                                        break;
                                    case 2://Withdrawal
                                        if(msgRecieveAccountData.flag() == 14) {
                                            double amount = msgRecieveAccountData.getDoubleMessages().get(0);
                                            double minRequiredBal = -1;
                                            if(msgRecieveAccountData.getDoubleMessages().size() == 2) {
                                                minRequiredBal = msgRecieveAccountData.getDoubleMessages().get(1);
                                            }
                                            double finalMinRequiredBal = minRequiredBal;
                                            runOnUiThread(() -> {
                                                Intent dep = new Intent(selectAccountDeposit.this, withdrawScreen.class);
                                                dep.putExtra("accountID", accountID);
                                                dep.putExtra("amount", amount);
                                                if(finalMinRequiredBal >= 0) {
                                                    dep.putExtra("isChecking", true);
                                                    dep.putExtra("min", finalMinRequiredBal);
                                                }
                                                else {
                                                    dep.putExtra("isChecking", false);
                                                }
                                                startActivity(dep);
                                            });
                                        }
                                        else
                                        {
                                            //Catestropic Error
                                            System.exit(1);
                                        }
                                        break;
                                    case 3://Transfer Source
                                        if(msgRecieveAccountData.flag() == 14) {
                                            double amount = msgRecieveAccountData.getDoubleMessages().get(0);
                                            double minRequiredBal = -1;
                                            if(msgRecieveAccountData.getDoubleMessages().size() == 2) {
                                                minRequiredBal = msgRecieveAccountData.getDoubleMessages().get(1);
                                            }
                                            double finalMinRequiredBal = minRequiredBal;
                                            Message msgRecieveAccountDestList = c.readMessage();
                                            if(msgRecieveAccountDestList.flag() == 15) {
                                                ArrayList<String> accountNames = msgRecieveAccountDestList.getTextMessages();
                                                ArrayList<Integer> accountIDs = msgRecieveAccountDestList.getIntegerMessages();
                                                runOnUiThread(() -> {
                                                    Intent dep = new Intent(selectAccountDeposit.this, selectAccountDeposit.class);
                                                    dep.putExtra("accountNames", accountNames);
                                                    dep.putExtra("accountIds", accountIDs);
                                                    dep.putExtra("nextIntent", 4);
                                                    dep.putExtra("accountNameSource", accountNames.get(value));
                                                    dep.putExtra("accountIDSource", accountID);
                                                    dep.putExtra("amountSource", amount);
                                                    if (finalMinRequiredBal >= 0) {
                                                        dep.putExtra("isChecking", true);
                                                        dep.putExtra("min", finalMinRequiredBal);
                                                    } else {
                                                        dep.putExtra("isChecking", false);
                                                    }
                                                    startActivity(dep);
                                                });
                                            }
                                            else {
                                                //Catestropic Error
                                                System.exit(1);
                                            }
                                        }
                                        else
                                        {
                                            //Catestropic Error
                                            System.exit(1);
                                        }
                                        break;
                                    case 4://Transfer Target
                                        double amount = msgRecieveAccountData.getDoubleMessages().get(0);
                                        double minRequiredBal = -1;
                                        if(msgRecieveAccountData.getDoubleMessages().size() == 2) {
                                            minRequiredBal = msgRecieveAccountData.getDoubleMessages().get(1);
                                        }
                                        double finalMinRequiredBal = minRequiredBal;
                                        Message msgRecieveAccountDestList = c.readMessage();
                                        if(msgRecieveAccountData.flag() == 14) {
                                            runOnUiThread(() -> {
                                                Intent dep = new Intent(selectAccountDeposit.this, cashDepositScreen.class);
                                                dep.putExtra("accountNameSrc", getIntent().getStringExtra("accountNameSource"));
                                                dep.putExtra("amountSrc", getIntent().getIntExtra("amountSource", 0));
                                                boolean isSrcChecking = getIntent().getBooleanExtra("isChecking", false)
                                                if (isSrcChecking) {
                                                    dep.putExtra("isCheckingSrc", true);
                                                    dep.putExtra("minSrc", getIntent().getIntExtra("min", 0));
                                                } else {
                                                    dep.putExtra("isCheckingSrc", false);
                                                }
                                                dep.putExtra("accountNameTar", accountNames.get(value));
                                                dep.putExtra("amountTar", amount);
                                                if (finalMinRequiredBal >= 0) {
                                                    dep.putExtra("isCheckingTar", true);
                                                    dep.putExtra("minTar", finalMinRequiredBal);
                                                } else {
                                                    dep.putExtra("isCheckingTar", false);
                                                }
                                                startActivity(dep);
                                            });
                                        }
                                        else
                                        {
                                            //Catestropic Error
                                            System.exit(1);
                                        }
                                        break;
                                    case 5://Account Inquiry

                                        break;
                                    default://Bleh
                                        break;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    case R.id.previous:

                        break;
                }
            }
        });

    }

    public void populateSpinner(ArrayList<String> names) {
        ArrayAdapter<String> accountSelectorAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, names);
        accountSpinner.setAdapter(accountSelectorAdapter);
    }
}
