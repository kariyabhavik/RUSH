package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registration.databinding.ActivitySignUpBinding;
import com.example.registration.models.huser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("creating Account");
        progressDialog.setMessage("we're creating your account");

        binding.signUpIdBtn.setOnClickListener(view -> {
            progressDialog.show();
            auth.createUserWithEmailAndPassword
                    (binding.emailId.getText().toString() ,binding.passwordId.getText().toString()).addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            huser user = new huser(binding.usernameId.getText().toString(),binding.emailId.getText().toString(),binding.passwordId.getText().toString());

                            String id = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                            database.getReference().child("users").child(id).setValue(user);

                            Toast.makeText(SignUpActivity.this, "User Created successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.alreadyhaveaccountId.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this , SigninActivity.class);
            startActivity(intent);
        });
    }
}