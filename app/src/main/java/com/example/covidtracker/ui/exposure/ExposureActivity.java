package com.example.covidtracker.ui.exposure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import com.example.covidtracker.R;
import com.example.covidtracker.ui.status.StatusActivity;

import androidx.appcompat.app.AppCompatActivity;

public class ExposureActivity extends AppCompatActivity {
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    String strDate = dateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exposure);

        TextView exposureText = (TextView) findViewById(R.id.textView2);
        exposureText.setText(strDate);

        Button button = (Button) findViewById(R.id.button_exp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExposureActivity.this, StatusActivity.class));
            }
        });
    }
}