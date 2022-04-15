package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class otpProcess extends AppCompatActivity {
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    EditText t2;
    Button b2;
    String phonenumber;
    String otpid;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_process);
        phonenumber = getIntent().getStringExtra("mobile");
        t2 = findViewById(R.id.t2);
        b2 = findViewById(R.id.b2);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(otpProcess.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");
        database = FirebaseDatabase.getInstance();
        initiateotp();

        b2.setOnClickListener(view -> {
            if (t2.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext() , "Blank field cannot be processed" , Toast.LENGTH_LONG).show();
            }
            else if (t2.getText().toString().length()!=6){
                Toast.makeText(getApplicationContext() , "invalid OTP" , Toast.LENGTH_LONG).show();
            }else{
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid , t2.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void initiateotp(){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                        {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
                            {
                                otpid = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
                            {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e)
                            {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.show();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        startActivity(new Intent(otpProcess.this , MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"Signup Code Error" , Toast.LENGTH_LONG).show();
                    }
                });
    }
}