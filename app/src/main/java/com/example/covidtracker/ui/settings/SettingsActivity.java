package com.example.covidtracker.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.covidtracker.R;
import com.example.covidtracker.ui.login.LoginActivity;
import com.example.covidtracker.ui.map.MapActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button button = (Button) findViewById(R.id.logout_Button);

        button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }

        });
    }
}
