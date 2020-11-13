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
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class ExposureActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static HashMap<String, String[]> counties =  new HashMap<String, String[]>();
    public static JSONObject obj;

    public class CountyInformation extends AsyncTask<URL, Integer, JSONObject> {

        public JSONObject obj;
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
        protected JSONObject doInBackground(URL... urls) {
            try {
                URL actNow = new URL(urls.toString());
                HttpURLConnection connect = (HttpURLConnection) actNow.openConnection();

                /*
                //File tester = new File(connect.getInputStream());
                BufferedReader read = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                String line = "";
                String[] split;
                String[] store = new String[6];
                while ((line = read.readLine()) != null) {
                    split = line.split(",");
                    //String[] countyInfo = {split[STATE], split[COUNTY_NAME], split[POPULATION_INDEX], split[CASE_INDEX], "", ""};//Empty Strings for lat/long coordinates
                    store[0] = split[STATE];
                    store[1] = split[COUNTY_NAME];
                    store[2] = split[POPULATION_INDEX];
                    store[3] = split[CASE_INDEX];
                    store[4] = store[5] = "";
                    counties.put(split[COUNTY_FIPS], store);
                }
                */

                //System.out.println("shazbot" + counties.size());
                //URL celeste = new URL("https://doc-00-7o-docs.googleusercontent.com/docs/securesc/k0u30gf6u7fqfnkjs2ca2mu9gqj4rkrf/knlrjs91mgtrkpvpmoguid52esm0t10p/1605252525000/16212445481544018714/16212445481544018714/1CVeXOgv4f53ikJoWUaZFGQyGpDgF1oHO?e=download&authuser=1&nonce=jh9jp9ktu63ha&user=16212445481544018714&hash=an6dl66mc6dua4nqeae97t253p0ibpc5");
                //HttpURLConnection connectCounty = (HttpURLConnection) celeste.openConnection();
                //KmlLayer layer = new KmlLayer(gMap, connectCounty.getInputStream(), this);

                BufferedReader read = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                String jsonText = "";
                StringBuilder build = new StringBuilder();
                while ((jsonText = read.readLine()) != null){
                    build.append(jsonText);
                }
                jsonText = build.toString();
                obj = new JSONObject(jsonText);
                return obj;
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch (JSONException e) {
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
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        try {
            GeoJsonLayer layer = new GeoJsonLayer(gMap, R.raw.geojson, this);
            layer.addLayerToMap();
            GeoJsonPointStyle style = layer.getDefaultPointStyle();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gMap.setMinZoomPreference(4);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40, -99)));

        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setZoomGesturesEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setRotateGesturesEnabled(false);

    }

    public void readFile(){
        //JSONObject jObj = new JSONObject(t);

    }


    }





