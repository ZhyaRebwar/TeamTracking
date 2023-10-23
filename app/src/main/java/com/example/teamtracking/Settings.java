package com.example.teamtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    TextView usernameSettigns, passwordSettings, phoneNumberSettings, emailSettings;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        usernameSettigns = (TextView) findViewById(R.id.usernameSettigns);
        passwordSettings = (TextView) findViewById(R.id.passwordSettings);
        phoneNumberSettings = (TextView) findViewById(R.id.phoneNumberSettings);
        emailSettings = (TextView) findViewById(R.id.emailSettings);

        // Get the Intent that started this activity and retrieve the data
        Intent intent = getIntent();
        int message = intent.getIntExtra("id", 0);

        // Create an instance of the DBHelper class
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        // Call the getData method on the DBHelper instance
        Cursor cursor = dbHelper.getData(message);



        //we should write getColumnIndexOrThrow instead of getColumnIndex, or it is not working
        if (cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            int phoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

            // Use the retrieved data here...
            usernameSettigns.setText(usernameSettigns.getText() + username);
            passwordSettings.setText(passwordSettings.getText() + password);
            phoneNumberSettings.setText(phoneNumberSettings.getText() + "" + phoneNumber);
            emailSettings.setText(emailSettings.getText() + email);
        }
        cursor.close();

    }

    public void backToHome(View view){
        // Get the Intent that started this activity and retrieve the data
        Intent getInfo = getIntent();
        int message = getInfo.getIntExtra("id", 0);
        
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", message);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }

    public void gotoEdit(View view){
        Intent intent = new Intent(getApplicationContext(), EditAccount.class);
        // Get the Intent that started this activity and retrieve the data
        Intent getInfo = getIntent();
        int message = getInfo.getIntExtra("id", 0);
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", message);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }
}