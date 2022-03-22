package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class amb_reg_first extends AppCompatActivity {

    public Button button;
    public CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amb_reg_first);

        final Button button = (Button) findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
                if (checkBox.isChecked()) {
                    Toast.makeText(amb_reg_first.this, "checked", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(amb_reg_first.this, "Missing Check box", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //start
        //after clicking next goes to the amb_reg_second page


        View btn_ambulance = findViewById(R.id.next);
        btn_ambulance.setOnClickListener(view -> {
            Intent intent = new Intent(this , amb_reg_second.class);
            startActivity(intent);
        });

        //end
        //.
    }
}