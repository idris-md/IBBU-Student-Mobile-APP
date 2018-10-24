package com.ibbumobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    View msgSec;
    View callSec;
    View msgSA;
    View callSA;
    View msgAmb;
    View callAmb;
    View msgSUGWel;
    View callSUGWel;
    View msgSUGPRO;
    View callSUGPRO;
    View msgNansWell;
    View callNanisWel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        msgSUGWel = findViewById(R.id.msgSUGWel);
        msgSUGWel.setOnClickListener(this);
        callSUGWel = findViewById(R.id.callSUGWel);
        callSUGWel.setOnClickListener(this);
        msgSUGPRO = findViewById(R.id.msgSUGPRO);
        msgSUGPRO.setOnClickListener(this);
        callSUGPRO = findViewById(R.id.callSUGPRO);
        callSUGWel.setOnClickListener(this);
        msgNansWell = findViewById(R.id.msgNanWel);
        msgNansWell.setOnClickListener(this);
        callNanisWel = findViewById(R.id.callNanWel);
        callNanisWel.setOnClickListener(this);

        msgSec = findViewById(R.id.msgSec);
        msgSec.setOnClickListener(this);
        callSec = findViewById(R.id.callSec);
        callSec.setOnClickListener(this);

        msgSA = findViewById(R.id.msgSA);
        msgSA.setOnClickListener(this);
        callSA = findViewById(R.id.callSA);
        callSA.setOnClickListener(this);

        msgAmb = findViewById(R.id.msgAmb);
        msgAmb.setOnClickListener(this);
        callAmb = findViewById(R.id.callAmb);
        callAmb.setOnClickListener(this);


    }


    private void makeCall(String phoneNum) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNum.trim())));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(callIntent);
    }

    private void sendMessage() {

        Uri SMS_URI = Uri.parse("smsto:+2348065468035");
        Intent sms = new Intent(Intent.ACTION_VIEW, SMS_URI);
        sms.putExtra("sms_body", "From U14/FNS/CSC/066");
        startActivity(sms);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.msgAmb:
                sendMessage();
                break;
            case R.id.callAmb:
                makeCall("08065468035");
                break;

            case R.id.msgNanWel:
                sendMessage();

                break;
            case R.id.callNanWel:
                makeCall("08065468035");

                break;
            case R.id.msgSUGPRO:
                sendMessage();

                break;
            case R.id.callSUGPRO:
                makeCall("08065468035");

                break;

            case R.id.msgSec:
                sendMessage();

                break;
            case R.id.callSec:
                makeCall("08065468035");

                break;
            case R.id.msgSUGWel:
                sendMessage();

                break;
            case R.id.callSUGWel:
                makeCall("08065468035");

                break;

        }

    }
}
