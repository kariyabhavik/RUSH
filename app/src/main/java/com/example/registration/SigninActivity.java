package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionScene;

import com.example.registration.models.huser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.example.registration.databinding.ActivitySigninBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SigninActivity extends AppCompatActivity {

  ActivitySigninBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    Button btn_login_mobile;

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
        database = FirebaseDatabase.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        btn_login_mobile = findViewById(R.id.btn_login_mobile);
        btn_login_mobile.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this , mobile_registration.class);
            startActivity(intent);
        });


        //start
        //this is a intent for registration  of ambulance


        View btn_ambulance = findViewById(R.id.btn_ambulance);
        btn_ambulance.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this , amb_reg_first.class);
            startActivity(intent);
        });

        // end
        //..

        //this is the intent of hospitaol button

        View btn_hospital = findViewById(R.id.btn_hospital);
        btn_hospital.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this , hos_reg_one.class);
            startActivity(intent);
        });
        //end




        binding.btnGoogle.setOnClickListener(view -> signIn());
    }
    int RC_SIGN_IN = 65;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        huser huser = new huser();
                        assert user != null;
                        huser.setUserid(user.getUid());
                        huser.setUsername(user.getDisplayName());
                        database.getReference().child("huser").child(user.getUid()).setValue(huser);

                        Intent intent = new Intent(SigninActivity.this , MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(this, "Sign in With google", Toast.LENGTH_SHORT).show();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(SigninActivity.this , (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(binding.getRoot(), "Authentication Faield." , Snackbar.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                });
    }


}