package com.example.covidtracker.ui.exposure;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import com.example.covidtracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.GroundOverlay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ExposureActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static HashMap<String, String[]> counties;

    public class CountyInformation extends AsyncTask<URL, Integer, Long> {

        //Indices for reading ActNow API
        private static final int COUNTY_FIPS = 0;
        private static final int STATE = 2;
        private static final int COUNTY_NAME = 3;
        private static final int POPULATION_INDEX = 8;
        private static final int CASE_INDEX = 23;

        //Indices for reading some other API for coordinates
        private static final int LATITUDE = 0;
        private static final int LONGITUDE = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(URL... urls) {
            try {
                URL actNow = new URL("https://api.covidactnow.org/v2/counties.csv?apiKey=5035fb4640c64817a4bbb23a6f135289");
                HttpURLConnection connect = (HttpURLConnection) actNow.openConnection();

                //File tester = new File(connect.getInputStream());
                BufferedReader read = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                String line = "";
                String[] split;
                while ((line = read.readLine()) != null) {
                    split = line.split(",");
                    String[] countyInfo = {split[STATE], split[COUNTY_NAME], split[POPULATION_INDEX], split[CASE_INDEX], "", ""};//Empty Strings for lat/long coordinates
                    counties.put(split[COUNTY_FIPS], countyInfo);
                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }



    private GoogleMap gMap;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exposure);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //new FetchDataTask().execute(URL);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap = googleMap;
        //GeoJsonLayer layer;
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

