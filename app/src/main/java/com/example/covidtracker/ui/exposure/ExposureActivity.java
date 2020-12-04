package com.example.covidtracker.ui.exposure;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.covidtracker.R;
import com.example.covidtracker.ui.status.StatusActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ExposureActivity extends AppCompatActivity implements InfoAdapter.ItemClickListener{

    public static ArrayList<InformationViewModel> data = new ArrayList<InformationViewModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exposure);
        try {
            Object result = new InfoLoader().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_information);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //infoAdapter.setClickListener(this);
        recyclerView.setAdapter(new InfoAdapter(this, data));
        Button button = (Button) findViewById(R.id.button_exp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExposureActivity.this, StatusActivity.class));
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private class InfoLoader extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            final String DATA_URL = "https://api.covidtracking.com/v1/states/current.json";
            data = (ArrayList<InformationViewModel>)DataQuery.fetchArticleData(DATA_URL); //Sets up ArrayList with all data for all states
            return null;
        }
    }

}