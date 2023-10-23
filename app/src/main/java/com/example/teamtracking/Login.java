package com.example.teamtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    DBHelper db;
    EditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        usernameInput = (EditText) findViewById(R.id.usernameInputLogin);
        passwordInput = (EditText) findViewById(R.id.passwordInputLogin);
    }

    public void gotoSignUp(View view){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    public void checkAccountLogin(View view){
        //check whether the account exists or not...
        db = new DBHelper(this);
        ArrayList<ArrayList<String>> records = db.getRecords();
        int size = records.get(0).size();

        String usernameI = usernameInput.getText().toString().replaceAll(" ", "");
        String PasswordI = passwordInput.getText().toString().replaceAll(" ", "");
        boolean checkLogin = false;
        int getId = 0;

        for(int loop=0; loop<size; loop++){

            if( records.get(0).get(loop).equals(usernameI) && records.get(1).get(loop).equals(PasswordI) ){
                checkLogin = true;
                getId = Integer.parseInt( records.get(4).get(loop) );
                break;
            }
        }

        if( checkLogin){

            Toast.makeText(getApplicationContext(), "The Account has been Loged In", Toast.LENGTH_SHORT).show();
            //then intent to go to another(home) page...
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("id", getId);
            intent.putExtras(dataBundle);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "The account is not Loged In, please try again", Toast.LENGTH_SHORT).show();
        }
    }
}