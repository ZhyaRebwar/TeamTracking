package com.example.teamtracking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


//gar class'aka public nabet natuanet instantiate'y le warbgrin

public class ScrollingFragment extends Fragment {

    //similar to onClickListener
    public interface FragmentScrollListener{
        void inputSend(double latitude, double longitude);

        void removeMarker();
    }

    private FragmentScrollListener listener;

    private ListView obj;
    DBHelper db;
    MapsFragment mMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scrolling, container, false);

        // Get the Intent that started this activity and retrieve the data
        Intent intent = getActivity().getIntent();
        int message = intent.getIntExtra("id", 0);

        db = new DBHelper( getContext() );

        ArrayList<ArrayList<String>> records = db.getFriendLocations( message );

        ArrayAdapter adapt = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_row_layout_maps, R.id.nameTextView, records.get(1))  {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String user_id = records.get(0).get(position);
                String username = records.get(1).get(position);
                String latitude = records.get(2).get(position);
                String longitude = records.get(3).get(position);

                TextView tv = view.findViewById(R.id.nameTextView);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listener.inputSend(Double.parseDouble(latitude) , Double.parseDouble(longitude));
                    }
                });

                Button deleteButton = view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        System.out.println(latitude + " is the id");

                        db.deleteFriends( message, Integer.parseInt(  user_id));
                        notifyDataSetChanged();


                        records.get(0).remove( position );
                        records.get(1).remove( position );
                        records.get(2).remove( position );
                        records.get(3).remove( position );

                        listener.removeMarker();
                    }
                });
                return view;
            }
        };


        obj = view.findViewById(R.id.items);
        obj.setAdapter(adapt);

        return view;
    }



    //to attach to activity
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        //to check if the activity(context) implements this interface:
        if(context instanceof FragmentScrollListener){
            listener = (FragmentScrollListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement FragmentScrollListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }
}