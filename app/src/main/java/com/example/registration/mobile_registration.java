package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class mobile_registration extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText t1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_registration);

        t1 = findViewById(R.id.t1);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(t1);
        b1 = findViewById(R.id.b1);

        b1.setOnClickListener(view -> {
            Intent intent = new Intent(mobile_registration.this , otpProcess.class);
            intent.putExtra("mobile" , ccp.getFullNumberWithPlus().replace(" " , ""));
            startActivity(intent);
        });
    }
}