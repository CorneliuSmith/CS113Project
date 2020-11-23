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
import java.util.Iterator;

import static android.graphics.Color.rgb;


public class ExposureActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static HashMap<String, CountyInformation> counties =  new HashMap<String, CountyInformation>();
    public static ArrayList<String> countyFIPS = new ArrayList<String>();

    private class CountyInformation{
        String FIPS;
        int population;
        int infected;
        PolygonOptions boundary;
        boolean isMulti;
        PolygonOptions[] multi;
        int index;

        public  CountyInformation(){
            FIPS = "";
            population = infected = 0;
            boundary = new PolygonOptions();
            isMulti = false;
            index = 0;
        }

        public CountyInformation(String FIPS){
            this.FIPS = FIPS;
            population = infected = 0;
            isMulti = false;
            index = 0;
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

        public void setMultiSize(int size){
            multi = new PolygonOptions[size];
            index = 0;
        }

        public PolygonOptions getBoundary(){
            return boundary;
        }

        public PolygonOptions getMultiPoly(int index){
            return multi[index];
        }

        /*
        Method takes in JSONArray of double coordinates and parses them into the PolygonOption object

        Method takes in only single JSONArray at a time, but isMulti does allow for population of PolygonOptions array
         */
        public void addCoordinate(JSONArray coord){
            ArrayList<Double> temp = new ArrayList<Double>();
            JSONArray dummy;
            if(isMulti){
                for (int i = 0; i < coord.length(); i++) {
                    try {
                        dummy = (JSONArray) coord.get(i);
                        multi[index] = new PolygonOptions();
                        multi[index].add(new LatLng((Double) dummy.get(1), (Double) dummy.get(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                index++;
            }else {
                boundary = new PolygonOptions();
                for (int i = 0; i < coord.length(); i++) {
                    try {
                        dummy = (JSONArray) coord.get(i);
                        boundary.add(new LatLng((Double) dummy.get(1), (Double) dummy.get(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void checkSeverity(GoogleMap gMap){
            if (this.population == 0) {
                gMap.addPolygon(boundary.fillColor(Color.RED));
            }else {
                double severity = (Math.pow(this.infected, 2)) / this.population * 255;
                if (isMulti) {
                    for (int i = 0; i < index; i++) {
                        gMap.addPolygon(multi[i].fillColor(rgb((int) severity, 0, 0)));
                    }
                } else {
                    gMap.addPolygon(boundary.fillColor(rgb((int) severity, 0, 0)));
                }
            }
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

        int index;

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
                    other = "{" + line.substring(index - 1, line.length() - 3) + "}"; //properly formats string to JSON format
                    other = other.replaceFirst("\\[", "");

                    //Sunstring 51-56 returns FIPS value for each county
                    line = line.substring(51, 56);
                    counties.put(line, new CountyInformation(line));//CountyInformation object is created with FIPS as corresponding key

                    if (multi == -1) {//single polygon
                        info = new JSONObject(other);//Creates JSONObject with only coordinates key
                        coordinates = info.getJSONArray(coord);
                        counties.get(line).addCoordinate(coordinates);
                    } else { //Multi polygon
                        counties.get(line).setIsMulti(true);
                        ArrayList<JSONArray> jsonList = new ArrayList<JSONArray>();
                        index = other.indexOf("],[["); //Gives us index of where first polygon ends
                        String piece = "{\"coordinates\":" + other.substring(other.indexOf("[") + 1, index) + "}";// {"coordinates":[[...]],"
                        while(other.contains("],[[")) {
                            info = new JSONObject(piece);
                            coordinates = info.getJSONArray(coord);
                            jsonList.add(coordinates);
                            other = other.substring(index + 3); //Moves to next double bracket array [[..]]
                            index = other.indexOf("],[[");
                            if(index != -1){
                                piece = "{\"coordinates\":" + other.substring(other.indexOf("[") , index) + "}";
                            } else { //If no other polygons to find, parse last one
                                piece = "{\"coordinates\":" + other.substring(other.indexOf("["), other.length() - 2) + "}";
                                info = new JSONObject(piece);
                                coordinates = info.getJSONArray(coord);
                                jsonList.add(coordinates);
                            }

                        } //End while
                        counties.get(line).setMultiSize(jsonList.size());
                        for(int i = 0; i < jsonList.size(); i++){
                            counties.get(line).addCoordinate(jsonList.get(i));
                        }
                    }

                }catch(Exception e){ } //Bandaid for IO exceptions while parsing

            }
            read.close();


            in = getResources().openRawResource(R.raw.covid_confirmed_usafacts);
            read = new BufferedReader((new InputStreamReader(in)));
            String[] infected;

            InputStream otherIn = getResources().openRawResource(R.raw.covid_county_population_usafacts);
            BufferedReader popRead = new BufferedReader(new InputStreamReader(otherIn));
            String otherLine;
            String[] population;
            line = otherLine = "";
            while((line!= null) && (otherLine != null)){
                try {
                    line = read.readLine();
                    otherLine = popRead.readLine();

                    if(otherLine.startsWith("0") && !line.startsWith("0")){
                        otherLine = popRead.readLine();
                    } else if (line.startsWith("0") && !otherLine.startsWith("0")){
                        line = read.readLine();
                    }

                    infected = line.split(","); //Infection count
                    population = otherLine.split(","); //Population count
                    if (infected[0].length() < 5) { //Some counties only have 4 digit FIPS codes
                        counties.get("0" + infected[0]).setInfected(Integer.parseInt(infected[infected.length - 1]));
                        countyFIPS.add("0" + infected[0]);
                        counties.get("0" + population[0]).setPopulation(Integer.parseInt(population[population.length - 1]));
                        countyFIPS.add("0" + population[0]);
                    } else {
                        counties.get(infected[0]).setInfected(Integer.parseInt(infected[infected.length - 1]));
                        countyFIPS.add(infected[0]);
                        counties.get(population[0]).setPopulation(Integer.parseInt(population[population.length - 1]));
                        countyFIPS.add(population[0]);
                    }
                } catch (Exception e){}
            }
            read.close();
            popRead.close();



                for (int i = 0; i < countyFIPS.size(); i++) {
                   try {
                       counties.get(countyFIPS.get(i)).checkSeverity(gMap);
                   } catch (Exception e){}
                }

        } catch (Exception e) { e.printStackTrace(); }
    }

        public String cleanLine(String raw) {
            return raw.replaceAll("\\s", "");
    }

}
