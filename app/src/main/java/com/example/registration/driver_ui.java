package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class driver_ui extends AppCompatActivity {
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ui);


        imageview = findViewById(R.id.imageView6);

        imageview.setOnClickListener(view -> {
            Intent intent = new Intent(driver_ui.this,MapsActivityone.class);
            startActivity(intent);
        });
    }
}