package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registration.databinding.ActivitySigninBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SigninActivity extends AppCompatActivity {

    ActivitySigninBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Objects.requireNonNull(getSupportActionBar()).hide();

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SigninActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");

        binding.signUpIdBtn.setOnClickListener(view -> {

            Editable email = binding.emailId.getText();
            Editable password = binding.passwordId.getText();

            boolean isEmail = email != null && !email.toString().isEmpty();
            boolean isPassword = password != null && !password.toString().isEmpty();

            if (isEmail && isPassword) {
                progressDialog.show();
                auth.signInWithEmailAndPassword(email.toString(), password.toString()).
                        addOnCompleteListener(task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SigninActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                if (!isEmail){
                    binding.emailId.setError("Email is required");
                }
                if (!isPassword)
                    binding.passwordId.setError("Password is required");

            }

        });
        binding.signintextId.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}