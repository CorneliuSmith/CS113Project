package com.example.covidtracker.ui.exposure;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.covidtracker.R;

import java.util.ArrayList;

public class InfoAdapter extends ArrayAdapter<Information> {
    public InfoAdapter(Activity context, ArrayList<Information> information){
        super(context, 0, information);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){

        }

        return null;
    }
}
