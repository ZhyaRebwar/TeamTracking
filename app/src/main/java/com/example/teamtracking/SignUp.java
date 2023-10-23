package com.example.teamtracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputLayout;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;


public class SignUp extends AppCompatActivity {

    private DBHelper db;
    EditText usernameInput, passwordInput, confirmPasswordInput, contactInfoInput, emailInput;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        contactInfoInput = findViewById(R.id.contactInfoInput);
        emailInput = findViewById(R.id.emailInput);

        db = new DBHelper(this);
    }

    public void gotoLogin(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void createAccount(View view)
    {
        String usernameI = usernameInput.getText().toString();
        String passwordI = passwordInput.getText().toString();
        String confirmPasswordI = confirmPasswordInput.getText().toString();
        String contactII = contactInfoInput.getText().toString();
        String emailI = emailInput.getText().toString();
        boolean checkValidation = true;

        // username validation
        if (usernameI.isEmpty()) {
            // show an error message for empty username
            checkValidation = false;
        } else if (usernameI.length() < 6 || usernameI.length() > 20) {
            // show an error message for username length
            checkValidation = false;
        }

        // password validation
        if (passwordI.isEmpty()) {
            // show an error message for empty password
            checkValidation = false;
        } else if (passwordI.length() < 8 || passwordI.length() > 20) {
            // show an error message for password length
            checkValidation = false;
        } else if (!passwordI.matches(".*\\d.*")) {
            // show an error message for missing digit(s)
            checkValidation = false;
        } else if (!passwordI.matches(".*[a-zA-Z].*")) {
            // show an error message for missing letter(s)
            checkValidation = false;
        }

        // confirm password validation
        if (!confirmPasswordI.equals(passwordI)) {
            // show an error message for password mismatch
            checkValidation = false;
        }

        // contact validation
        if (contactII.isEmpty()) {
            // show an error message for empty contact
            checkValidation = false;
        } else if (!contactII.matches("\\d+")) {
            // show an error message for non-digit contact
            checkValidation = false;
        } else if (contactII.length() < 10 || contactII.length() > 12) {
            // show an error message for invalid contact length
            checkValidation = false;
        }

        // email validation
        if (emailI.isEmpty()) {
            // show an error message for empty email
            checkValidation = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailI).matches()) {
            // show an error message for invalid email format
            checkValidation = false;
        }


        // Create a new instance of DBHelper class
        DBHelper db = new DBHelper(this);

        // Call the insertValues method with the user's location as an additional parameter
        if (checkValidation && db.insertValues(usernameI, passwordI, contactII, emailI, 0.00, 0.00) ) {
            Toast.makeText(getApplicationContext(), "The Account has been created", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "The account is not created, please try again", Toast.LENGTH_SHORT).show();
        }

    }
}