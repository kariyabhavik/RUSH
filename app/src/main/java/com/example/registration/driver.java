package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.registration.databinding.ActivityDriverBinding;
import com.example.registration.models.DriverRegistration;
import com.example.registration.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class driver extends AppCompatActivity {

    ActivityDriverBinding binding;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;

    boolean isValid(Editable editable) {
        return editable != null && !editable.toString().isEmpty();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(driver.this);
        progressDialog.setTitle("Register Driver");
        progressDialog.setMessage("we're creating your account");

        binding.btnDriverReg.setOnClickListener(view -> {

            Editable drivername = binding.etFullname.getText();
            Editable driveremail = binding.etEmail.getText();
            Editable password = binding.etPassword.getText();
            Editable phonenumber = binding.etPhonenumber.getText();
            Editable c1licensenumber = binding.etClLicenseNumber.getText();
            Editable drivinglicense = binding.etDriverLicenseNumber.getText();
            Editable pannumber = binding.etPanNumber.getText();
            boolean emt = binding.cbEmt.isChecked();
            boolean validdriver = binding.cbAmbulanceDriver.isChecked();
            boolean evoc = binding.cbEvoc.isChecked();


            boolean isdrivername = isValid(drivername);
            boolean isdriveremail = isValid(driveremail);
            boolean ispassword = isValid(password);
            boolean isphonenumber = isValid(phonenumber);
            boolean isc1licensenumber = isValid(c1licensenumber);
            boolean isdrivinglicense = isValid(drivinglicense);
            boolean ispannumber = isValid(pannumber);


            if (isdrivername && isdriveremail && ispassword && isphonenumber && isc1licensenumber && isdrivinglicense && ispannumber && emt && validdriver && evoc) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(driveremail.toString(), password.toString()).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        DriverRegistration driverRegistration = new DriverRegistration(drivername.toString(), driveremail.toString(), password.toString(), phonenumber.toString()
                                , c1licensenumber.toString(), drivinglicense.toString(), pannumber.toString(), emt, validdriver, evoc);
                        String id = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        database.getReference().child("driver").child(id).setValue(driverRegistration).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e("Tag TAg", "Result " + task.isSuccessful());

                                if (task.isSuccessful()) {
                                    database.getReference().child("user").child(id).setValue(new User(driverRegistration.getUserName(), driverRegistration.getEmail(), driverRegistration.getPassword())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(driver.this, "User Created successfully", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(driver.this, driver_ui.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }

                            }
                        });


                    } else {
                        Toast.makeText(driver.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                if (isdrivername) {
                    binding.etFullname.setError("Name is required");
                }
                if (!isdriveremail) {
                    binding.etEmail.setError("email is required");
                }
                if (!ispassword) {
                    binding.etPassword.setError("password is required");
                }
                if (isphonenumber) {
                    binding.etPhonenumber.setError("phonenumber is required");
                }
                if (isc1licensenumber) {
                    binding.etClLicenseNumber.setError("C1license number is required");
                }
                if (isdrivinglicense) {
                    binding.etDriverLicenseNumber.setError("driving license is required");
                }
                if (ispannumber) {
                    binding.etPanNumber.setError("pan number is required");
                }
                if (emt) {
                    binding.cbEmt.setError("emt is required");
                }
                if (validdriver) {
                    binding.cbAmbulanceDriver.setError(" is required");
                }
                if (evoc) {
                    binding.cbEvoc.setError("evoc is required");
                }
            }
        });


    }


}