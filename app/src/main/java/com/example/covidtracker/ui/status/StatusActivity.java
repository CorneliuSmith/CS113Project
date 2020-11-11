package com.example.covidtracker.ui.status;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.covidtracker.R;
import com.example.covidtracker.ui.exposure.ExposureActivity;
import com.example.covidtracker.ui.map.MapActivity;

import androidx.appcompat.app.AppCompatActivity;

public class StatusActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

       /* Button button = (Button) findViewById(R.id.button_sta); */

        ImageButton mapsButton = (ImageButton)findViewById(R.id.mapsButton);
        mapsButton.setImageResource(R.drawable.mapIcon);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatusActivity.this, MapActivity.class));
            }
        });
    }
}
