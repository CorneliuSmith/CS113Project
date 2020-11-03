package com.example.covidtracker.ui.exposure;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CountyInformation extends AsyncTask<URL, Integer, Long> {
    private static final int COUNTY_NAME = 0;
    private static final int STATE = 1;
    private static final int POPULATION_IND = 2;
    private static final int INFECTED = 3;

    public static HashMap<String, String[]> counties;
    public static String[] countyInfo;

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(URL... urls) {
        try {
            URL url = new URL("https://usafactsstatic.blob.core.windows.net/public/data/covid-19/covid_county_population_usafacts.csv");
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000); //Times out after 10 seconds of connection
            String line = "";
            String[] split;
            String[] storage = new String[4];

            BufferedReader read = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            while((line = read.readLine()) != null) {
                split = line.split(","); //splits entire line of .csv into separate indices
                storage = new String[]{split[COUNTY_NAME], split[STATE], split[POPULATION_IND], split[INFECTED]};
                counties.put(split[0], storage); //Puts read county into static hashmap for map reference later
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
