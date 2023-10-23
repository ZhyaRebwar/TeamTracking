package com.example.teamtracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements  OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMyLocationClickListener{



    GoogleMap mMap;

    Marker marker;
    DBHelper db;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get the Intent that started this activity and retrieve the data
        Intent intent = getActivity().getIntent();
        int message = intent.getIntExtra("id", 0);

        db = new DBHelper(getActivity().getApplicationContext());
        ArrayList<ArrayList<String>> records = db.getFriendLocations( message );
        int size = records.get(0).size();
        int size2 = records.size();
        if( size > 0){
            for(int loop=0; loop<size; loop++){

                double latitude = Double.parseDouble( records.get(2).get(loop) );
                double longitude = Double.parseDouble( records.get(3).get(loop) );
                String usesrname = records.get(1).get(loop);

                LatLng anywhere = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(anywhere).title( usesrname ));
            }
        }


        // Get the current location
        GPSTracker gpsTracker = new GPSTracker(getContext());
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        db.updateLocation( message, latitude, longitude);

        // Create LatLng object for current location
        LatLng currentLocation = new LatLng(latitude, longitude);

        // Move camera to current location
        CircleOptions circleOptions = new CircleOptions()
                .center(currentLocation)
                .radius(2000)  // radius in meters
                .strokeWidth(3)
                .strokeColor(Color.RED)
                .fillColor(Color.RED);
        mMap.addCircle( circleOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
        System.out.println( " the current locations changed.");

        // Get the current location
        mMap.setOnInfoWindowClickListener(this);
    }


    public void updateText(double latitude, double longitude){

        LatLng anywhere = new LatLng(latitude, longitude);

        mMap.moveCamera( CameraUpdateFactory.newLatLng( anywhere ) );
        mMap.setMinZoomPreference(2);
    }

    public void refreshMarker(){
        mMap.clear();

        // Get the Intent that started this activity and retrieve the data
        Intent intent = getActivity().getIntent();
        int message = intent.getIntExtra("id", 0);

        db = new DBHelper(getActivity().getApplicationContext() );
        ArrayList<ArrayList<String>> records = db.getFriendLocations( message );
        int size = records.get(0).size();
        int size2 = db.numOfRow();
        if( size2 > 0){
            for(int loop=0; loop<size; loop++){

                int latitude = Integer.parseInt( records.get(2).get(loop) );
                int longitude = Integer.parseInt( records.get(3).get(loop) );
                String username = records.get(1).get(loop);

                LatLng anywhere = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(anywhere).title( username ));
            }
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapToShow);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}