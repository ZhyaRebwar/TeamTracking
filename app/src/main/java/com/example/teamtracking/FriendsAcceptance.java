package com.example.teamtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendsAcceptance extends AppCompatActivity {

    private ListView friend_request_pending;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_acceptance);

        /// Get the Intent that started this activity and retrieve the data
        Intent intent = getIntent();
        int message = intent.getIntExtra("id", 0);

        db = new DBHelper( getApplicationContext() );

        ArrayList<ArrayList<String>> records = db.getFriendRequestsById( message );

        System.out.println( records.get(2) +  " are the records");

        ArrayAdapter adapt = new ArrayAdapter( this , R.layout.custom_row_layout_accept_friend_request, R.id.nameTextView, records.get(2) )  {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String user_id = records.get(0).get(position);
                String friend_id = records.get(1).get(position);

                Button accept_friend_request = view.findViewById(R.id.accept_friend_request);
                accept_friend_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.insertFriends( Integer.parseInt( user_id ), Integer.parseInt( friend_id ) );
                        db.deleteFriendRequest( Integer.parseInt( user_id ), Integer.parseInt( friend_id ) );

                        Toast.makeText( getApplicationContext(), "The friend request is sent", Toast.LENGTH_SHORT).show();

                        records.get(0).remove( position );
                        records.get(1).remove( position );
                        records.get(2).remove( position );

                        notifyDataSetChanged();
                    }
                });

                Button delete_friend_request = view.findViewById(R.id.delete_friend_request);
                delete_friend_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.deleteFriendRequest( Integer.parseInt( user_id ), Integer.parseInt( friend_id ) );

                        Toast.makeText( getApplicationContext(), "The friend request is rejected", Toast.LENGTH_SHORT).show();

                        records.get(0).remove( position );
                        records.get(1).remove( position );
                        records.get(2).remove( position );

                        notifyDataSetChanged();
                    }
                });
                return view;
            }
        };

        friend_request_pending = (ListView) findViewById(R.id.friend_request_pending);
        friend_request_pending.setAdapter(adapt);
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
}