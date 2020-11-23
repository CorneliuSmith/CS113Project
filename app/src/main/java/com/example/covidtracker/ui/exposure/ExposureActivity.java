package com.example.covidtracker.ui.exposure;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.covidtracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static android.graphics.Color.rgb;


public class ExposureActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static HashMap<String, CountyInformation> counties =  new HashMap<String, CountyInformation>();
    public static JSONObject obj;

    private class CountyInformation{
        String FIPS;
        int population;
        int infected;
        PolygonOptions boundary;
        boolean isMulti;

        public  CountyInformation(){
            FIPS = "";
            population = infected = 0;
            boundary = new PolygonOptions();
            isMulti = false;
        }

        public CountyInformation(String FIPS){
            this.FIPS = FIPS;
            population = infected = 0;
            boundary = new PolygonOptions();
            isMulti = false;
        }

        public void setFIPS(String FIPS){
            this.FIPS = FIPS;
        }

        public void setPopulation(int population){
            this.population = population;
        }

        public void setInfected(int infected) {
            this.infected = infected;
        }

        public void setIsMulti(boolean isMulti){ this.isMulti = isMulti;}

        public PolygonOptions getBoundary(){
            return boundary;
        }

        /*
        Method takes in JSONArray of double coordinates and parses them into the PolygonOption object
         */
        public void addCoordinate(JSONArray coord){
            ArrayList<Double> temp = new ArrayList<Double>();
            JSONArray dummy;
            for(int i = 0; i < coord.length(); i++){
                try {
                    dummy = (JSONArray) coord.get(i);
                    //temp = (ArrayList<Double>) dummy.get(0);
                    boundary.add(new LatLng((Double) dummy.get(1), (Double)dummy.get(0)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public void checkSeverity(){

            if (this.population == 0)
                    boundary.fillColor(Color.WHITE);
            double severity = (this.infected * 255) / this.population;
            boundary.fillColor(rgb((int) severity, 0, 0));
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

        readFiles(gMap);
            //GeoJsonLayer layer = new GeoJsonLayer(gMap, R.raw.geojson, this);
            //layer.addLayerToMap();



        //getCoordinates();

        //gMap.setMinZoomPreference(6);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(33, -117)));

        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setZoomGesturesEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setRotateGesturesEnabled(false);

    }

        public void readFiles(GoogleMap gMap){
        InputStream in = getResources().openRawResource(R.raw.geojson);
        BufferedReader read = new BufferedReader(new InputStreamReader(in));

        String line;
        String coord = "coordinates";

        int index = 0;

        JSONObject info;
        JSONArray coordinates;
        String other;

        try{
            while (((line = read.readLine()) != null)) {
                try {
                    line = read.readLine();
                    line = cleanLine(line);
                    index = line.indexOf(coord);

                    //Check for single or multipolygons
                    int multi = line.indexOf("MultiPolygon");
                    //if (multi == -1) {//single polygon
                        other = "{" + line.substring(index - 1, line.length() - 3) + "}"; //properly formats string to JSON format
                        other = other.replaceFirst("\\[", "");
                        info = new JSONObject(other);//Creates JSONObject with only coordinates key
                        coordinates = info.getJSONArray(coord);


                    //Sunstring 51-56 returns FIPS value for each county
                    line = line.substring(51, 56);
                    counties.put(line, new CountyInformation(line));//CountyInformation object is created with FIPS as corresponding key
                    counties.get(line).addCoordinate(coordinates);

                }catch(Exception e){ }

            }
            read.close();
            in = getResources().openRawResource(R.raw.countyinfectioncount);
            read = new BufferedReader((new InputStreamReader(in)));
            String[] first;

            InputStream otherIn = getResources().openRawResource(R.raw.countypopulation);
            BufferedReader popRead = new BufferedReader(new InputStreamReader(otherIn));
            String otherLine;
            String[] second;

            while(((line = read.readLine()) != null) && (otherLine = popRead.readLine()) != null){
                try {
                    line = read.readLine();
                    otherLine = popRead.readLine();
                    first = line.split(","); //Infection count
                    second = otherLine.split(","); //Population count
                    if (first[0].length() < 5) { //Some counties only have 4 digit FIPS codes
                        counties.get("0" + first[0]).setInfected(Integer.parseInt(first[1]));
                        counties.get("0" + first[0]).setPopulation(Integer.parseInt(second[1]));
                        counties.get("0" + first[0]).checkSeverity();
                        gMap.addPolygon(counties.get("0" + first[0]).getBoundary()).setVisible(true);
                    } else {
                        counties.get(first[0]).setInfected(Integer.parseInt(first[1]));
                        counties.get(first[0]).setPopulation(Integer.parseInt(second[1]));
                        counties.get(first[0]).checkSeverity();
                        gMap.addPolygon(counties.get(first[0]).getBoundary()).setVisible(true);
                    }
                } catch (Exception e){}
            }
            read.close();
            popRead.close();


        } catch (IOException e) {
                e.printStackTrace();
        }

        }

        public String cleanLine(String raw) {
            return raw.replaceAll("\\s", "");
    }

}
