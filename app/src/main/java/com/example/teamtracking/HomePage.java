package com.example.teamtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {

    ImageView settings, friend_request, friend_acceptance, friends_map_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        settings = findViewById(R.id.settings);
        friend_acceptance = findViewById(R.id.friend_acceptance);
        friend_request = findViewById(R.id.friend_request);
        friends_map_location = findViewById(R.id.friends_map_location);

        // Get the Intent that started this activity and retrieve the data
        Intent intent = getIntent();
        int message = intent.getIntExtra("id", 0);

        // Do something with the data
        friends_map_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                System.out.println("The Maps button is clicked... ");

                //then intent to go to another(home) page...
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", message);
                intent.putExtras(dataBundle);
                System.out.println("3333333333333");
                startActivity(intent);
            }
        });

        friend_acceptance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                System.out.println("The friend acceptance button is clicked... ");

                //then intent to go to another(home) page...
                Intent intent = new Intent(getApplicationContext(), FriendsAcceptance.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", message);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });



        friend_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                System.out.println("The friend request button is clicked... ");

                //then intent to go to another(home) page...
                Intent intent = new Intent(getApplicationContext(), FriendRequest.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", message);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //then intent to go to another(home) page...
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", message);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    public void logOut(View view){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}