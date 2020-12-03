package com.example.covidtracker.ui.exposure;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.covidtracker.R;
import com.example.covidtracker.ui.status.StatusActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ExposureActivity extends AppCompatActivity {

    public static ArrayList<Information> list = new ArrayList<Information>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exposure);

        Button button = (Button) findViewById(R.id.button_exp);

        new InfoLoader().execute();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExposureActivity.this, StatusActivity.class));
            }
        });
    }

    private class InfoLoader extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            final String DATA_URL = "https://api.covidtracking.com/v1/states/current.json";
            final String POP_URL = "api.census.gov/data/2019/pep/charagegroups?get=POP,NAME&for=state:";
            list = (ArrayList<Information>)DataQuery.fetchArticleData(DATA_URL); //Sets up ArrayList with all data for all states
            return null;
        }
    }

}