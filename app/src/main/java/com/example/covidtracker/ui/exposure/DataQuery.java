package com.example.covidtracker.ui.exposure;

import android.os.health.SystemHealthManager;
import android.text.TextUtils;
import android.util.Log;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class DataQuery {

    private DataQuery() { }

    /**
     *
     * @param requestUrl
     * @return
     */
    public static List<Information> fetchArticleData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            System.out.println("oopsie");
        }

        List<Information> data = extractArticle(jsonResponse);

        return data;
    }

    /**
     *
     * @param articleJSON
     * @return
     */
    private static List<Information> extractArticle(String articleJSON) {
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }
        List<Information> data = new ArrayList<>();

        try {
            String name;
            int positiveIncrease, deathConfirmed;

            Scanner reader = new Scanner(articleJSON);
            String line;
            JSONArray jsonArr;
            JSONObject jsonLine;
            int index = 0;

            line = reader.nextLine();
            jsonArr = new JSONArray(line);

            while(index < jsonArr.length()){
                jsonLine = jsonArr.getJSONObject(index);
                name = jsonLine.getString("state");
                positiveIncrease = jsonLine.getInt("positiveIncrease");
                try {
                    deathConfirmed = jsonLine.getInt("deathConfirmed");
                } catch (JSONException n){deathConfirmed = 1;} //null pointer was caught while reading deaths
                if(positiveIncrease > 0 && deathConfirmed > 0){
                    data.add(new Information(name, positiveIncrease, deathConfirmed));
               }
                index++;
            } //End while
            Collections.sort(data, new Comparator<Information>() { //Sorts data by state name alphabetically
                @Override
                public int compare(Information information, Information t1) {
                    return information.compareTo(t1);
                }
            });

            for(int i = 0; i < data.size(); i++){
                System.out.println(data.get(i));
            }
        } catch (JSONException e) {
            Log.e("DataQuery", "Problem parsing the articles JSON results", e);
        }

        // Return the list of articles
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
