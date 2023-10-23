package com.example.teamtracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MapsActivity extends AppCompatActivity implements ScrollingFragment.FragmentScrollListener {


    MapsFragment maps;
    ScrollingFragment scrollData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        int message = intent.getIntExtra("id", 0);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        maps = new MapsFragment();
        fragmentTransaction.add(R.id.fragment_container, maps);

        scrollData = new ScrollingFragment();
        fragmentTransaction.add(R.id.fragment_container2, scrollData);

        fragmentTransaction.commit();
    }

    @Override
    public void inputSend(double latitude, double longitude) {
        maps.updateText(latitude, longitude);
    }
    public  void removeMarker(){maps.refreshMarker();}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);
    }
}
