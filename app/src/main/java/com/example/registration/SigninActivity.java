package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.example.registration.databinding.ActivitySigninBinding;

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

        binding.btnSignUp.setOnClickListener(view -> {

            Editable email = binding.etEmail.getText();
            Editable password = binding.etPassword.getText();

            boolean isEmail = email != null && !email.toString().isEmpty();
            boolean isPassword = password != null && !password.toString().isEmpty();

            if (isEmail && isPassword) {
                progressDialog.show();
                auth.signInWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener(task -> {
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
                    binding.etEmail.setError("Email is required");
                }
                if (!isPassword)
                    binding.etPassword.setError("Password is required");

            }

        });
        binding.tvClickForSignup.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
            startActivity(intent);
        });



        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}