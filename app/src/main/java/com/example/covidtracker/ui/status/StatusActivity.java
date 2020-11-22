package com.example.covidtracker.ui.status;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
/*import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity; */
import android.view.MenuItem;
import android.widget.TextView;



import com.example.covidtracker.R;
import com.example.covidtracker.ui.exposure.ExposureActivity;
import com.example.covidtracker.ui.settings.SettingsActivity;
import com.example.covidtracker.ui.map.MapActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StatusActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);



        ImageButton mapsButton = (ImageButton)findViewById(R.id.mapsButton);
        mapsButton.setImageResource(R.drawable.mapicon);

        mapsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatusActivity.this, MapActivity.class));
            }
        });



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.mobile_navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.navigation_home:
                    {
                        break;
                    }

                    case R.id.navigation_Settings:
                    {
                        Intent one = new Intent(StatusActivity.this, SettingsActivity.class);
                        startActivity(one);
                        break;
                    }

                    case R.id.navigation_notifications:
                    {
                        Intent two = new Intent(StatusActivity.this, ExposureActivity.class);
                        startActivity(two);
                        break;
                    }
                }

                return false;
            }

        });

    }
}
