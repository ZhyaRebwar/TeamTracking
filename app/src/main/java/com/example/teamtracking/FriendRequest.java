package com.example.teamtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendRequest extends AppCompatActivity {

    EditText searchUsernameInput;
    private ListView items;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        searchUsernameInput = (EditText) findViewById(R.id.searchUsernameInput);
    }

    public void searchUser(View view){

        /// Get the Intent that started this activity and retrieve the data
        Intent intent = getIntent();
        int message = intent.getIntExtra("id", 0);

        db = new DBHelper( getApplicationContext() );

        ArrayList<ArrayList<String>> records = db.searchRecordsFriendRequest( message,  searchUsernameInput.getText().toString() );

        ArrayAdapter adapt = new ArrayAdapter( this , R.layout.custom_row_layout, R.id.nameTextView, records.get(0) )  {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // get the values of each text on the screen
                String name = records.get(0).get(position);
                String password = records.get(1).get(position);
                String phone = records.get(2).get(position);
                String email = records.get(3).get(position);
                int id = Integer.parseInt(records.get(4).get(position));

                Button send_friend_request = view.findViewById(R.id.send_friend_request);
                send_friend_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        System.out.println( message + " is the message " + id + " is the id");
                        db.sendFriendRequest( message, id);
                        Toast.makeText(FriendRequest.this, "The friend request is sent", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
                return view;
            }
        };


        items = (ListView) findViewById(R.id.items);
        items.setAdapter(adapt);
    }
}