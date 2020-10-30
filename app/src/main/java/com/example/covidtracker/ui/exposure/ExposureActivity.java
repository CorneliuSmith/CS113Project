package com.example.covidtracker.ui.exposure;
import com.example.covidtracker.ui.login.LoginActivity;
import com.example.covidtracker.ui.map.MapsActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.covidtracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ExposureActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exposure);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap = googleMap;

        LatLng riteAid = new LatLng(33.182, -117.369);
        LatLng lincoln = new LatLng(33.1896, -117.349);
        gMap.addMarker(new MarkerOptions().position(riteAid).title("Positive"));
        gMap.addMarker(new MarkerOptions().position(lincoln).title("Positive"));
        gMap.setMinZoomPreference(12);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(33.18229, -117.3264)));
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setZoomGesturesEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setRotateGesturesEnabled(false);
    }


}