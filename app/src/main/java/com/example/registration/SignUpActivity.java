package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.registration.databinding.ActivitySignUpBinding;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;



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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.btnSignUp.setOnClickListener(view -> {

            Editable username = binding.usernameId.getText();
            Editable email = binding.emailId.getText();
            Editable password = binding.passwordId.getText();

            boolean isUsername = username != null && !username.toString().isEmpty();
            boolean isEmail = email != null && !email.toString().isEmpty();
            boolean isPassword = password != null && !password.toString().isEmpty();

            if (isUsername && isEmail && isPassword){
                progressDialog.show();
                auth.createUserWithEmailAndPassword( username.toString() , email.toString() ).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        huser user = new huser(binding.usernameId.toString(), binding.emailId.toString(), binding.passwordId.toString());

                        String id = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        database.getReference().child("users").child(id).setValue(user);

                        Toast.makeText(SignUpActivity.this, "User Created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                }else {
                        if (!isUsername){
                            binding.usernameId.setError("Username is required");
                        }
                        if (!isEmail)
                            binding.emailId.setError("Email is require");

                        if (!isPassword)
                            binding.passwordId.setError("password is require");

                    }

        });

        binding.alreadyhaveaccountId.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this , SigninActivity.class);
            startActivity(intent);
        });
        binding.googleBtnId.setOnClickListener(view -> signIn());
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
                        huser.setUserid(user.getUid());
                        huser.setUsername(user.getDisplayName());
                        database.getReference().child("huser").child(user.getUid()).setValue(huser);

                        Intent intent = new Intent(SignUpActivity.this , MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(this, "Sign in With google", Toast.LENGTH_SHORT).show();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignUpActivity.this , (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(binding.getRoot(), "Authentication Faield." , Snackbar.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                });
    }

}
