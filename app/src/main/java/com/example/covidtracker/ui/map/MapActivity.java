package com.example.covidtracker.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.covidtracker.R;
import com.example.covidtracker.ui.settings.SettingsActivity;
import com.example.covidtracker.ui.status.StatusActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapping);

        Button button = (Button) findViewById(R.id.button_map);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapActivity.this, SettingsActivity.class));
            }
        });
    }
}
