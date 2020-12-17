package com.example.covidtracker.ui.exposure;

import android.text.TextUtils;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataQuery {

    private DataQuery() { }

    /**
     *
     * @param requestUrl
     * @return
     */
    public static List<InformationViewModel> fetchArticleData(String requestUrl) {
        URL stateURL = createUrl(requestUrl);
        String stateResponse = null;
        try {
            stateResponse = makeHttpRequest(stateURL);
        } catch (IOException e) {
            System.out.println("oopsie");
        }
        List<InformationViewModel> data = extractArticle(stateResponse);
        return data;
    }

    /**
     *
     * @param stateJSON
     * @return
     */
    private static List<InformationViewModel> extractArticle(String stateJSON) {
        if (TextUtils.isEmpty(stateJSON)) {
            return null;
        }
        List<InformationViewModel> data = new ArrayList<>();
        try {
            String name;
            int positiveIncrease, death;
            int totalPositiveIncrease, totalDeath;
            totalPositiveIncrease = totalDeath = 0;
            Scanner reader = new Scanner(stateJSON);
            String line;
            JSONArray jsonArr;
            JSONObject jsonLine;
            int index = 0;
            line = reader.nextLine();
            jsonArr = new JSONArray(line);

            while(index < jsonArr.length()){ //Reading Data API JSON response
                jsonLine = jsonArr.getJSONObject(index);
                name = jsonLine.getString("state");
                try{
                    positiveIncrease = jsonLine.getInt("positiveIncrease");
                } catch (JSONException js){
                    positiveIncrease = 1;
                }
                try {
                    death = jsonLine.getInt("death");
                } catch (JSONException n) {//null pointer was caught while reading deaths
                    death = 1;
                }
                if(positiveIncrease > 0 && death > 0){
                    data.add(new InformationViewModel(name, death, positiveIncrease));
                    totalDeath += death;
                    totalPositiveIncrease += positiveIncrease;
               }
                index++; //Tracks position in JSONarr
            } //End while
            reader.close();
            //Create last object displaying nationwide confirmed deaths and increase in cases
            data.add(0, (new InformationViewModel("United States", totalDeath, totalPositiveIncrease)));
        } catch (JSONException e) {
            Log.e("DataQuery", "Problem parsing the articles JSON results", e);
        }

        return data;
    }

    /**
     *
     * @param stringUrl
     * @return
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            System.out.println("stuff");
        }
        return url;
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                System.out.println("Stuff");
            }
        } catch (IOException e) {
            System.out.println("Stuff");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
