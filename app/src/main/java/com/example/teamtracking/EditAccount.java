package com.example.teamtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class EditAccount extends AppCompatActivity {

    private DBHelper db;
    EditText usernameInputEdit, passwordInputEdit, confirmPasswordInputEdit, contactInfoInputEdit, emailInputEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        usernameInputEdit = (EditText) findViewById(R.id.usernameInputEdit);
        passwordInputEdit = (EditText) findViewById(R.id.passwordInputEdit);
        confirmPasswordInputEdit = findViewById(R.id.confirmPasswordInputEdit);
        contactInfoInputEdit = findViewById(R.id.contactInfoInputEdit);
        emailInputEdit = findViewById(R.id.emailInputEdit);

        // Get the Intent that started this activity and retrieve the data
        Intent intent = getIntent();
        int message = intent.getIntExtra("id", 0);


        // Create an instance of the DBHelper class
        db = new DBHelper(getApplicationContext());
        // Call the getData method on the DBHelper instance
        Cursor cursor = db.getData(message);

        //we should write getColumnIndexOrThrow instead of getColumnIndex, or it is not working
        if (cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            System.out.println( username + " is the username");
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

            // Use the retrieved data here...
            usernameInputEdit.setText( username);
            passwordInputEdit.setText( password);
            confirmPasswordInputEdit.setText( password);
            contactInfoInputEdit.setText( phoneNumber);
            emailInputEdit.setText( email);
        }
        cursor.close();
    }


    public void goBack(View view){
        // Get the Intent that started this activity and retrieve the data
        Intent getInfo = getIntent();
        int message = getInfo.getIntExtra("id", 0);

        Intent intent = new Intent(getApplicationContext(), Settings.class);
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", message);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }

    public void editAccount(View view){

        String usernameI = usernameInputEdit.getText().toString();
        String passwordI = passwordInputEdit.getText().toString();
        String confirmPasswordI = confirmPasswordInputEdit.getText().toString();
        String contactII = contactInfoInputEdit.getText().toString();
        String emailI = emailInputEdit.getText().toString();
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


        // Get the Intent that started this activity and retrieve the data
        Intent getInfo = getIntent();
        int message = getInfo.getIntExtra("id", 0);

        if( checkValidation ){
            db.updateRecord( message, usernameI, passwordI, contactII, emailI);
            Toast.makeText(getApplicationContext(), "The Account has been Updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("id", message);
            intent.putExtras(dataBundle);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "The account is not Updated, please try again", Toast.LENGTH_SHORT).show();
        }

    }
}