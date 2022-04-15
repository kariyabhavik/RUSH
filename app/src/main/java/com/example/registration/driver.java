package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class driver extends AppCompatActivity {
Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);


        submit = findViewById(R.id.submit);

        submit.setOnClickListener(view -> {
            Intent intent = new Intent(driver.this,driver_ui.class);
            startActivity(intent);
        });
    }
}